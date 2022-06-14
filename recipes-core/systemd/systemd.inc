# use systemd to manage networks
# See http://www.freedesktop.org/software/systemd/man/systemd.network.html
PACKAGECONFIG:append = " coredump networkd resolved"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
    file://20-network-io.conf \
"

FILES:${PN} += "\
    ${sysconfdir}/sysctl.d/20-network-io.conf \
"

do_install:append() {
   # systemd-sysctl
   install -d ${D}${sysconfdir}/sysctl.d/
   install -m 0644 ${WORKDIR}/20-network-io.conf ${D}${sysconfdir}/sysctl.d/
}

USERADD_PARAM:${PN} += "--system --home /dev/null systemd-journal-gateway"
