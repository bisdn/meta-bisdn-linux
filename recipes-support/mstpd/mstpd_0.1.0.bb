SUMMARY = "Multiple Spanning Tree Protocol Daemon"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4325afd396febcb659c36b49533135d4"

SRC_URI = "\
  git://github.com/bisdn/mstpd.git;branch=${PV};protocol=https \
  file://bridge-stp.conf"

PV:append = "+bisdn6"
SRCREV = "e3424dff8667bd901a9a349ab18718159b65b263"

S = "${WORKDIR}/git"

inherit autotools systemd

EXTRA_OECONF = "--sbindir=${base_sbindir}"

# Uncomment the following lines to enable debug output in the binary.
# Output is sent as level 4.
# Port Information state machine:
CFLAGS += " -DPISM_ENABLE_LOG"
# Port Role Transitions state machine:
CFLAGS += " -DPRTSM_ENABLE_LOG"
#
# Packets (printf, always active):
CFLAGS += " -DPACKET_DEBUG"

FILES:${PN} += "${systemd_system_unitdir}/mstpd.service"

SYSTEMD_SERVICE:${PN} = "mstpd.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"

do_install:append() {
   # we do not use it nor can we use it, and shipping it will add a unnecessary
   # depdency on python (2)
   rm ${D}${libexecdir}/mstpctl-utils/ifquery

   install -d ${D}${systemd_unitdir}/system
   install -m 0644 ${WORKDIR}/build/utils/mstpd.service ${D}${systemd_unitdir}/system

   install -m 0644 ${WORKDIR}/bridge-stp.conf ${D}${sysconfdir}/bridge-stp.conf
}
