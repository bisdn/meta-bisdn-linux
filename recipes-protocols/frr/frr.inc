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
	   clippy-native \
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

SRC_URI = "git://github.com/FRRouting/frr.git;protocol=https \
           file://frr.init \
           file://frr.default \
           file://watchfrr.init \
           file://watchfrr.default \
           file://volatiles.03_frr \
           file://frr.pam \
	   file://daemons \
	   file://daemons.conf \
	   file://frr.service \
         "
	 
S = "${WORKDIR}/git"

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)}"
PACKAGECONFIG[cap] = "--enable-capabilities,--disable-capabilities,libcap"
PACKAGECONFIG[pam] = "--with-libpam, --without-libpam, libpam"

inherit autotools update-rc.d useradd systemd pkgconfig python3native

SYSTEMD_PACKAGES = "${PN}" 
# ${PN}-bgpd ${PN}-isisd ${PN}-ospf6d ${PN}-ospfd ${PN}-ripd ${PN}-ripngd

#SYSTEMD_SERVICE_${PN}-bgpd = "bgpd.service"
#SYSTEMD_SERVICE_${PN}-isisd = "isisd.service"
#SYSTEMD_SERVICE_${PN}-ospf6d = "ospf6d.service"
#SYSTEMD_SERVICE_${PN}-ospfd = "ospfd.service"
#SYSTEMD_SERVICE_${PN}-ripd = "ripd.service"
#SYSTEMD_SERVICE_${PN}-ripngd = "ripngd.service"
SYSTEMD_SERVICE_${PN} = "frr.service"

EXTRA_OECONF_class-target = "\
  --sbindir=${libdir}/frr \
  --sysconfdir=${sysconfdir}/frr \
  --localstatedir=${localstatedir}/run/frr \
  --enable-exampledir=${docdir}/frr/examples/ \
  --enable-vtysh \
  --enable-isisd \
  --enable-ospfclient=yes \
  --enable-ospfd \
  --enable-bgpd \	
  --enable-multipath=64 \
  --enable-user=frr \
  --enable-group=frr \
  --enable-vty-group=frrvty \
  --enable-configfile-mask=0640 \
  --enable-logfile-mask=0640 \
  --enable-rtadv \
  --enable-linux24-tcp-md5 \
  --enable-cumulus \
  --enable-datacenter \
  --enable-irdp \
  --disable-doc \
  --enable-watchfrr \
  ap_cv_cc_pie=no \
  ${SNMP_CONF} \
  CLIPPY_ROOT=${RECIPE_SYSROOT_NATIVE} \
  "

#  --enable-systemd=yes 

#  
#  ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', '--enable-watchfrr', '--disable-watchfrr', d)} 


CACHED_CONFIGUREVARS += "ac_cv_path_PERL='/usr/bin/env perl'"

do_install () {
    # Install init script and default settings
    install -m 0755 -d ${D}${sysconfdir}/default ${D}${sysconfdir}/init.d \ 
    ${D}${sysconfdir}/frr ${D}${sysconfdir}/default/volatiles 
    install -m 0644 ${WORKDIR}/frr.default ${D}${sysconfdir}/default/frr
    install -m 0644 ${WORKDIR}/watchfrr.default ${D}${sysconfdir}/default/watchfrr
    install -m 0755 ${WORKDIR}/frr.init ${D}${sysconfdir}/init.d/frr 
    install -m 0755 ${WORKDIR}/watchfrr.init ${D}${sysconfdir}/init.d/watchfrr 
    install -m 0644 ${WORKDIR}/volatiles.03_frr  ${D}${sysconfdir}/default/volatiles/volatiles.03_frr 

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

    # Fix hardcoded paths
    sed -i 's!/usr/sbin/!${sbindir}/!g' ${D}${sysconfdir}/init.d/* 
    sed -i 's!/usr/bin/!${bindir}/!g' ${D}${sysconfdir}/init.d/frr 
    sed -i 's!/etc/!${sysconfdir}/!g' ${D}${sysconfdir}/init.d/* ${D}${sysconfdir}/default/watchfrr 
    sed -i 's!/var/!${localstatedir}/!g' ${D}${sysconfdir}/init.d/* ${D}${sysconfdir}/default/volatiles/volatiles.03_frr 
    sed -i 's!^PATH=.*!PATH=${base_sbindir}:${sbindir}:${base_bindir}:${bindir}!' ${D}${sysconfdir}/init.d/* 

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

    # Remove sysinit script if sysvinit is not in DISTRO_FEATURES
    if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'false', 'true', d)}; then
        rm -rf ${D}${sysconfdir}/init.d/
        # rm -f ${D}${sysconfdir}/default/watchfrr
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

# Split into a main package and separate per-protocol packages
#PACKAGE_BEFORE_${PN}_class-target = "${PN}-ospfd ${PN}-ospf6d ${PN}-bgpd 
#                     ${PN}-ripd ${PN}-ripngd ${PN}-isisd 
#                     ${PN}-ospfclient ${PN}-watchfrr 
#		     "

#RDEPENDS_${PN}_class-target += "${PN}-bgpd ${PN}-isisd ${PN}-ospf6d ${PN}-ospfd ${PN}-ripd ${PN}-ripngd ${PN}-watchfrr"


#FILES_${PN}-ospfd       = "${sbindir}/ospfd ${libdir}/libospf.so.*"
#FILES_${PN}-ospf6d      = "${sbindir}/ospf6d"
#FILES_${PN}-bgpd        = "${sbindir}/bgpd"
#FILES_${PN}-ripd        = "${sbindir}/ripd"
#FILES_${PN}-ripngd      = "${sbindir}/ripngd"
#FILES_${PN}-isisd       = "${sbindir}/isisd"
#FILES_${PN}-ospfclient  = "${sbindir}/ospfclient ${libdir}/libospfapiclient.so.*"
#FILES_${PN}-watchfrr    = "${sbindir}/watchfrr ${sysconfdir}/default/watchfrr"

# ${sysconfdir}/init.d/watchfrr

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

# Main init script starts all deamons
# Seperate init script for watchfrr
INITSCRIPT_PACKAGES                     = "${PN} ${PN}-watchfrr"
INITSCRIPT_NAME_${PN}                   = "frr"
INITSCRIPT_PARAMS_${PN}                 = "defaults 15 85"
INITSCRIPT_NAME_${PN}-watchfrr       = "watchfrr"
INITSCRIPT_PARAMS_${PN}-watchfrr     = "defaults 90 10"

# Add frr's user and group
USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "--system frr ; --system frrvty"
USERADD_PARAM_${PN} = "--system --home ${localstatedir}/run/frr/ -M -g frr -G frrvty --shell /bin/false frr"

pkg_postinst_${PN} () {
    if [ -z "$D" ] && [ -e /etc/init.d/populate-volatile.sh ] ; then
           ${sysconfdir}/init.d/populate-volatile.sh update
    fi
}

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
    systemctl  stop bgpd
}

pkg_prerm_${PN}-ripd () {
    systemctl  stop ripd
}

pkg_prerm_${PN}-ripngd () {
   systemctl  stop ripngd
}

pkg_prerm_${PN}-isisd () {
    systemctl stop isisd
}

BBCLASSEXTEND = "native nativesdk"
