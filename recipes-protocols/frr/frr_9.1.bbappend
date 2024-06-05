FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
           file://frr.service \
           file://frr@.service \
           file://support_bundle_commands.conf;subdir=git/tools/etc/frr \
           "

PR = "r1"

SYSTEMD_AUTO_ENABLE = "enable"

FRR_DAEMONS ?= "zebra staticd bgpd ospfd ospf6d ripd ripngd isisd pimd ldpd nhrpd eigrpd babeld sharpd pbrd bfdd pathd mgmtd"
FRR_EXTRA_CONF ?= "cumulus datacenter"

PACKAGECONFIG ??= " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'pam', d)} \
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
    ${@bb.utils.filter('FRR_DAEMONS', 'pathd', d)} \
    ${@bb.utils.filter('FRR_DAEMONS', 'mgmtd', d)} \
    ${@bb.utils.filter('FRR_EXTRA_CONF', 'cumulus', d)} \
    ${@bb.utils.filter('FRR_EXTRA_CONF', 'datacenter', d)} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'snmp', d)} \
    ospfclient \
    "

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
PACKAGECONFIG[pathd] = "--enable-pathd,--disable-pathd,"
PACKAGECONFIG[mgmtd] = "--enable-mgmtd,--disable-mgmtd,"

do_install:append:class-target () {
    # remove the global config
    rm ${D}${sysconfdir}/frr/frr.conf

    # remove the integrated config
    rm ${D}${sysconfdir}/frr/vtysh.conf

    # Install configurations for the daemons
    install -m 0755 -d ${D}${sysconfdir}/default ${D}${sysconfdir}/frr
    for f in vtysh ${FRR_DAEMONS}; do
        touch ${D}${sysconfdir}/frr/$f.conf
    done

    sed -i '/_options/s/-A/--daemon -A/g' ${D}${sysconfdir}/frr/daemons

    chown frr:frrvty ${D}${sysconfdir}/frr
    chown frr:frr ${D}${sysconfdir}/frr/*.conf
    chmod 750 ${D}${sysconfdir}/frr
    chmod 640 ${D}${sysconfdir}/frr/*.conf

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/frr*.service ${D}${systemd_system_unitdir}
    fi

    # create default log directory for frr
    install -m 0755 -d ${D}/var/log/frr
    chown frr:frr ${D}/var/log/frr
}

# Indicate that the default files are configuration files
CONFFILES:${PN} = "${sysconfdir}/frr/vtysh.conf ${sysconfdir}/frr/frr.conf"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'staticd', '${sysconfdir}/frr/staticd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'bgpd', '${sysconfdir}/frr/bgpd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ospfd', '${sysconfdir}/frr/ospfd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ospf6d', '${sysconfdir}/frr/ospf6d.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ripd', '${sysconfdir}/frr/ripd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ripngd', '${sysconfdir}/frr/ripngd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'isisd', '${sysconfdir}/frr/isisd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'pimd', '${sysconfdir}/frr/pimd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'ldpd', '${sysconfdir}/frr/ldpd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'nhrpd', '${sysconfdir}/frr/nhrpd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'eigrpd', '${sysconfdir}/frr/eigrpd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'babeld', '${sysconfdir}/frr/babeld.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'sharpd', '${sysconfdir}/frr/sharpd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'pbrd', '${sysconfdir}/frr/pbrd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'bfdd', '${sysconfdir}/frr/bfdd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'pathd', '${sysconfdir}/frr/pathd.conf', '', d)}"
CONFFILES:${PN} += " ${@bb.utils.contains('FRR_DAEMONS', 'mgmtd', '${sysconfdir}/frr/mgmtd.conf', '', d)}"
