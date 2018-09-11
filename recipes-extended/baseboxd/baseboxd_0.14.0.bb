# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "A tiny OpenFlow controller for OF-DPA switches"
HOMEPAGE = "https://github.com/bisdn/basebox"
LICENSE = "MPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=815ca599c9df247a0c7f619bab123dad"

DEPENDS = "openssl gflags glog grpc grpc-native libnl protobuf protobuf-native rofl-common rofl-ofdpa"

SRC_URI = "gitsm://github.com/bisdn/basebox.git \
           file://grpc-configure.patch"
SRCREV = "3afc013c8572c6c2883bae47f9b07a6eb0ffeec2"

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
# TODO enable baseboxd by default?
#SYSTEMD_SERVICE_${PN}_append = "baseboxd.service"
