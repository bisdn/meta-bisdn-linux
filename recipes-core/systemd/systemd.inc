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

# values taken from BISDN Linux 5.2.1's /etc/passwd and /etc/group
EXTRA_OEMESON:append = " \
 -Dadm-gid=4 \
 -Dtty-gid=5 \
 -Ddisk-gid=6 \
 -Dlp-gid=7 \
 -Dkmem-gid=15 \
 -Dinput-gid=19 \
 -Ddialout-gid=20 \
 -Dcdrom-gid=24 \
 -Dtape-gid=26 \
 -Daudio-gid=29 \
 -Dutmp-gid=43 \
 -Dvideo-gid=44 \
 -Dkvm-gid=47 \
 -Dusers-gid=100 \
 -Dwheel-gid=80 \
 -Dsystemd-journal-gid=990 \
 -Dsgx-gid=48 \
 -Drender-gid=995 \
 -Dsystemd-timesync-uid=991 \
 -Dsystemd-resolve-uid=992 \
 -Dsystemd-network-uid=993 \
"
