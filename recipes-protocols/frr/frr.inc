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
			    python3-core \
			    bash \
			    "

GIT_BRANCH ?= "master"
SRC_URI = "git://github.com/FRRouting/frr.git;protocol=https;branch=${GIT_BRANCH} \
           file://frr.pam \
           file://frr.service \
           file://support_bundle_commands.conf;subdir=git/tools/etc/frr \
           "

S = "${WORKDIR}/git"

FRR_DAEMONS ?= "zebra staticd bgpd ospfd ospf6d ripd ripngd isisd pimd ldpd nhrpd eigrpd babeld sharpd pbrd bfdd"
FRR_EXTRA_CONF ?= "cumulus datacenter"

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'systemd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'zebra', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'staticd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'bgpd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'ospfd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'ospf6d', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'ripd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'ripngd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'isisd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'pimd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'ldpd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'nhrpd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'eigrpd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'babeld', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'sharpd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'pbrd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'bfdd', d)} \
    ${@bb.utils.filter('FRR_EXTRA_CONF', 'cumulus', d)} \
    ${@bb.utils.filter('FRR_EXTRA_CONF', 'datacenter', d)} \
    "
PACKAGECONFIG[cap] = "--enable-capabilities,--disable-capabilities,libcap"
PACKAGECONFIG[pam] = "--with-libpam, --without-libpam, libpam"
PACKAGECONFIG[systemd] = "--enable-systemd,--disable-systemd,systemd"
PACKAGECONFIG[zebra] = "--enable-zebra,--disable-zebra,"
PACKAGECONFIG[staticd] = "--enable-staticd,--disable-staticd,"
PACKAGECONFIG[bgpd] = "--enable-bgpd,--disable-bgpd,"
PACKAGECONFIG[ospfd] = "--enable-ospfd,--disable-ospfd,"
PACKAGECONFIG[ospf6d] = "--enable-ospf6d,--disable-ospf6d,"
PACKAGECONFIG[ripd] = "--enable-ripd,--disable-ripd,"
PACKAGECONFIG[ripngd] = "--enable-ripngd,--disable-ripngd,"
PACKAGECONFIG[isisd] = "--enable-isisd,--disable-isisd,"
PACKAGECONFIG[pimd] = "--enable-pimd,--disable-pimd,"
PACKAGECONFIG[ldpd] = "--enable-ldpd,--disable-ldpd,"
PACKAGECONFIG[nhrpd] = "--enable-nhrpd,--disable-nhrpd,"
PACKAGECONFIG[eigrpd] = "--enable-eigrpd,--disable-eigrpd,"
PACKAGECONFIG[babeld] = "--enable-babeld,--disable-babeld,"
PACKAGECONFIG[sharpd] = "--enable-sharpd,--disable-sharpd,"
PACKAGECONFIG[pbrd] = "--enable-pbrd,--disable-pbrd,"
PACKAGECONFIG[bfdd] = "--enable-bfdd,--disable-bfdd,"
PACKAGECONFIG[cumulus] = "--enable-cumulus,--disable-cumulus,"
PACKAGECONFIG[datacenter] = "--enable-datacenter,--disable-datacenter,"

inherit autotools useradd systemd pkgconfig python3native

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICE_${PN} = "frr.service"

EXTRA_OECONF_class-target = "\
  --localstatedir=${localstatedir}/run/frr \
  --sbindir=${libdir}/frr \
  --sysconfdir=${sysconfdir}/frr \
  --enable-configfile-mask=0640 \
  --enable-exampledir=${docdir}/frr/examples/ \
  --enable-group=frr \
  --enable-logfile-mask=0640 \
  --enable-multipath=64 \
  --enable-user=frr \
  --enable-vty-group=frrvty \
  --enable-vtysh \
  --disable-doc \
  --with-clippy=${RECIPE_SYSROOT_NATIVE}/usr/lib/clippy \
  ap_cv_cc_pie=no \
  ${SNMP_CONF} \
  "

CACHED_CONFIGUREVARS += "ac_cv_path_PERL='/usr/bin/env perl'"

do_install () {
    # Install configurations for the daemons
    install -m 0755 -d ${D}${sysconfdir}/default ${D}${sysconfdir}/frr
    for f in vtysh ${FRR_DAEMONS}; do
        if [ -f ${S}/$f/$f.conf.sample ]; then
            install -m 0640 ${S}/$f/$f.conf.sample ${D}${sysconfdir}/frr/$f.conf.sample
        fi
        touch ${D}${sysconfdir}/frr/$f.conf
    done
    install -m 0640 ${S}/tools/etc/frr/support_bundle_commands.conf ${D}/${sysconfdir}/frr/support_bundle_commands.conf

    install -m 0640 ${S}/tools/etc/frr/daemons* ${D}${sysconfdir}/frr/
    sed -i '/_options/s/-A/--daemon -A/g' ${D}${sysconfdir}/frr/daemons

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

    # create default log directory for frr
    install -m 0755 -d ${D}/var/log/frr
    chown frr:frr ${D}/var/log/frr
}

DEPENDS_append = " ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd-systemctl-native', '', d)}"
pkg_postinst_${PN}_append () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'false', 'true', d)}; then
        if [ -n "$D" ]; then
            OPTS="--root=$D"
        fi
        systemctl $OPTS mask frr.service
    fi
}

# Indicate that the default files are configuration files
CONFFILES_${PN} = "${sysconfdir}/frr/vtysh.conf ${sysconfdir}/frr/frr.conf"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'staticd', '${sysconfdir}/frr/staticd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'bgpd', '${sysconfdir}/frr/bgpd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ospfd', '${sysconfdir}/frr/ospfd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ospf6d', '${sysconfdir}/frr/ospf6d.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ripd', '${sysconfdir}/frr/ripd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ripngd', '${sysconfdir}/frr/ripngd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'isisd', '${sysconfdir}/frr/isisd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'pimd', '${sysconfdir}/frr/pimd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ldpd', '${sysconfdir}/frr/ldpd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'nhrpd', '${sysconfdir}/frr/nhrpd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'eigrpd', '${sysconfdir}/frr/eigrpd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'babeld', '${sysconfdir}/frr/babeld.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'sharpd', '${sysconfdir}/frr/sharpd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'pbrd', '${sysconfdir}/frr/pbrd.conf', '', d)}"
CONFFILES_${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'bfdd', '${sysconfdir}/frr/bfdd.conf', '', d)}"
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
