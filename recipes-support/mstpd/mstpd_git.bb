SUMMARY = "Multiple Spanning Tree Protocol Daemon"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4325afd396febcb659c36b49533135d4"

SRC_URI = "\
  git://github.com/mstpd/mstpd.git;protocol=https \
  file://0001-libnetlink-introduce-rta_nest-and-u8-u16-u64-helpers.patch \
  file://0002-libnetlink-Set-NLA_F_NESTED-in-rta_nest.patch \
  file://0003-libnetlink-Add-helper-to-add-a-group-via-setsockopt.patch \
  file://0004-WIP-set-CIST-MSTI-port-states-to-the-newly-added-per.patch \
  file://bridge-stp.conf"

PV = "0.0.8+git${SRCPV}"
SRCREV = "4d98707b9047a74bc473345875e60ca5a24f0c96"

S = "${WORKDIR}/git"

DEPENDS = "python"
RDEPENDS_${PN} = "python-core"

inherit autotools systemd

EXTRA_OECONF = "--sbindir=/sbin"

FILES_${PN} += "${systemd_system_unitdir}/mstpd.service"

SYSTEMD_SERVICE_${PN} = "mstpd.service"
SYSTEMD_AUTO_ENABLE_${PN} = "disable"

do_install_append() {
   install -d ${D}${systemd_unitdir}/system
   install -m 0644 ${WORKDIR}/build/utils/mstpd.service ${D}${systemd_unitdir}/system

   install -m 0644 ${WORKDIR}/bridge-stp.conf ${D}${sysconfdir}/bridge-stp.conf
}
