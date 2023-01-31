# use systemd to manage networks
# See http://www.freedesktop.org/software/systemd/man/systemd.network.html
PACKAGECONFIG:append = " coredump networkd resolved"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://10-any_interface_is_enough.conf \
    file://10-disable-llmnr.conf \
    file://20-network-io.conf \
"

FILES:${PN} += "\
    ${sysconfdir}/systemd/resolved.conf.d/10-disable-llmnr.conf \
    ${sysconfdir}/sysctl.d/20-network-io.conf \
"

do_install:append() {
   # systemd-sysctl
   install -d ${D}${sysconfdir}/sysctl.d/
   install -m 0644 ${WORKDIR}/20-network-io.conf ${D}${sysconfdir}/sysctl.d/

   # systemd-networkd-wait-online
   install -d ${D}${systemd_system_unitdir}/systemd-networkd-wait-online.service.d/
   install -m 0644 ${WORKDIR}/10-any_interface_is_enough.conf ${D}${systemd_system_unitdir}/systemd-networkd-wait-online.service.d/10-any_interface_is_enough.conf

   # systemd-resolvd disable llmnr
   install -d ${D}${sysconfdir}/systemd/resolved.conf.d/
   install -m 0644 ${WORKDIR}/10-disable-llmnr.conf ${D}${sysconfdir}/systemd/resolved.conf.d/10-disable-llmnr.conf
}

USERADD_PARAM:${PN} += "--system --home /dev/null systemd-journal-gateway"
