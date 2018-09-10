SUMMARY = "BGP/OSPF/RIP routing daemon"
DESCRIPTION = "FRRouting is free software that implements and manages various IPv4 and IPv6 \
routing protocols. \
\
Currently FRRouting supports BGP4, BGP4+, OSPFv2, OSPFv3, RIPv1, RIPv2, RIPng, \
IS-IS, PIM-SM/MSDP, LDP and Babel as well as very early support for EIGRP and \
NHRP."

HOMEPAGE = "https://frrouting.org/"
SECTION = "net"

LICENSE = "GPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING-LGPLv2.1;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = " \
           bison-native \
           c-ares \
           flex-native \
           json-c \
           ncurses \
           perl-native \
           readline \
	   "

do_compile_prepend_class-target () {
       export PYTHONHOME=${RECIPE_SYSROOT_NATIVE}/usr
       install -m 0755 -d ${WORKDIR}/build/tests/isisd
}

DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'snmp', 'net-snmp', '', d)}"
SNMP_CONF="${@bb.utils.contains('DISTRO_FEATURES', 'snmp', '--enable-snmp', '', d)}"

# the "ip" command from busybox is not sufficient (flush by protocol flushes all routes)
RDEPENDS_${PN}_class-target += " \
			    iproute2 \
			    python \
			    bash \
			    "

GIT_BRANCH ?= "master"
SRC_URI = "git://github.com/FRRouting/frr.git;protocol=https;branch=${GIT_BRANCH} \
           file://frr.default \
           file://frr.pam \
	   file://daemons \
	   file://daemons.conf \
	   file://frr.service \
	  "

S = "${WORKDIR}/git"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)}"
PACKAGECONFIG[cap] = "--enable-capabilities,--disable-capabilities,libcap"
PACKAGECONFIG[pam] = "--with-libpam, --without-libpam, libpam"

inherit autotools useradd systemd pkgconfig python3native

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "frr.service"

EXTRA_OECONF_class-target = "\
  --localstatedir=${localstatedir}/run/frr \
  --sbindir=${libdir}/frr \
  --sysconfdir=${sysconfdir}/frr \
  --enable-bgpd \
  --enable-configfile-mask=0640 \
  --enable-cumulus \
  --enable-datacenter \
  --enable-exampledir=${docdir}/frr/examples/ \
  --enable-group=frr \
  --enable-isisd \
  --enable-irdp \
  --enable-linux24-tcp-md5 \
  --enable-logfile-mask=0640 \
  --enable-multipath=64 \
  --enable-ospfclient=yes \
  --enable-ospfd \
  --enable-rtadv \
  --enable-user=frr \
  --enable-vty-group=frrvty \
  --enable-vtysh \
  --enable-watchfrr \
  --disable-doc \
  ap_cv_cc_pie=no \
  ${SNMP_CONF} \
  CLIPPY_ROOT=${RECIPE_SYSROOT_NATIVE} \
  "
#  --enable-systemd=yes

CACHED_CONFIGUREVARS += "ac_cv_path_PERL='/usr/bin/env perl'"

do_install () {
    # Install init script and default settings
    install -m 0755 -d ${D}${sysconfdir}/default ${D}${sysconfdir}/frr
    install -m 0644 ${WORKDIR}/frr.default ${D}${sysconfdir}/default/frr

    # Install sample configurations for the daemons
    for f in bgpd vtysh isisd ospfd ripngd zebra ripd ospf6d; do
        install -m 0640 ${S}/$f/$f.conf.sample ${D}${sysconfdir}/frr/$f.conf.sample
    done

    for f in bgpd vtysh isisd ospfd ripngd zebra ripd ospf6d; do
        touch ${D}${sysconfdir}/frr/$f.conf
    done

    install -m 0640 ${WORKDIR}/daemons* ${D}${sysconfdir}/frr/

    chown frr:frrvty ${D}${sysconfdir}/frr
    chown frr:frr ${D}${sysconfdir}/frr/*.conf
    chmod 750 ${D}${sysconfdir}/frr
    chmod 640 ${D}${sysconfdir}/frr/*.conf

    # Install frr
    oe_runmake install DESTDIR=${D} prefix=${prefix} \
    	    sbindir=${libdir}/frr \
            sysconfdir=${sysconfdir}/frr \
            localstatedir=${localstatedir}/run/frr

    # For PAM
    for feature in ${DISTRO_FEATURES}; do
        if [ "$feature" = "pam" ]; then
            install -D -m 644 ${WORKDIR}/frr.pam ${D}/${sysconfdir}/pam.d/frr
            break
        fi
    done

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${sysconfdir}/tmpfiles.d
        echo "d /var/run/frr 0755 frr frr -" \
        > ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf
    fi

    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/frr.service ${D}${systemd_unitdir}/system
}

DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-systemctl-native', '', d)}"
pkg_postinst_${PN}_append () {
	if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd sysvinit', 'true', 'false', d)}; then
        if [ -n "$D" ]; then
            OPTS="--root=$D"
        fi
        systemctl $OPTS mask frr.service
    fi
}

# Indicate that the default files are configuration files
CONFFILES_${PN} = "${sysconfdir}/default/frr \
                   ${sysconfdir}/frr/bgpd.conf \
                   ${sysconfdir}/frr/vtysh.conf \
                   ${sysconfdir}/frr/isisd.conf \
                   ${sysconfdir}/frr/ospfd.conf \
                   ${sysconfdir}/frr/ripngd.conf \
                   ${sysconfdir}/frr/zebra.conf \
                   ${sysconfdir}/frr/ripd.conf \
                   ${sysconfdir}/frr/ospf6d.conf \
                  "
CONFFILES_${PN}-watchfrr = "${sysconfdir}/default/watchfrr"

# Stop the names being rewritten due to the internal shared libraries
DEBIAN_NOAUTONAME_${PN}-ospfd = "1"
DEBIAN_NOAUTONAME_${PN}-ospfclient = "1"

# Add frr's user and group
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system frr ; --system frrvty"
USERADD_PARAM_${PN} = "--system --home ${localstatedir}/run/frr/ -M -g frr -G frrvty --shell /bin/false frr"

# Stop apps before uninstall
pkg_prerm_${PN} () {
    systemctl stop frr
}

pkg_prerm_${PN}-ospfd () {
    systemctl stop ospfd
}

pkg_prerm_${PN}-ospf6d () {
    systemctl stop ospf6d
}

pkg_prerm_${PN}-bgpd () {
    systemctl stop bgpd
}

pkg_prerm_${PN}-ripd () {
    systemctl  stop ripd
}

pkg_prerm_${PN}-ripngd () {
   systemctl stop ripngd
}

pkg_prerm_${PN}-isisd () {
    systemctl stop isisd
}
