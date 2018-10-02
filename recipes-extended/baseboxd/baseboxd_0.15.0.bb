# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "A tiny OpenFlow controller for OF-DPA switches"
HOMEPAGE = "https://github.com/bisdn/basebox"
LICENSE = "MPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=815ca599c9df247a0c7f619bab123dad"

DEPENDS = "openssl gflags glog grpc grpc-native libnl protobuf protobuf-native rofl-common rofl-ofdpa"

SRC_URI = "gitsm://github.com/bisdn/basebox.git"
SRCREV = "42a8d4372c3a91849c6a173a827666095f5e9513"

S = "${WORKDIR}/git"

inherit autotools pkgconfig systemd

TARGET_LDFLAGS_append = " -lssl"
TARGET_LDFLAGS_append = " -lcrypto"

# install service
do_install_append() {
   # add directories
   install -d ${D}${sysconfdir}/default \
              ${D}${systemd_unitdir}/system

   # install service file and config
   install -m 0644 ${S}/pkg/systemd/sysconfig.template ${D}${sysconfdir}/default/baseboxd
   install -m 0644 ${S}/pkg/systemd/baseboxd.service ${D}${systemd_unitdir}/system

   # update service file
   sed -i -e 's,/etc/sysconfig/baseboxd,/etc/default/baseboxd,g' \
          -e 's,/sbin/baseboxd,${sbindir}/baseboxd,g' \
          ${D}${systemd_unitdir}/system/baseboxd.service
}

FILES_${PN} += "/lib/systemd/system/baseboxd.service"
