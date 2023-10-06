# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "A tiny OpenFlow controller for OF-DPA switches"
HOMEPAGE = "https://github.com/bisdn/basebox"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=815ca599c9df247a0c7f619bab123dad"

DEPENDS = "\
    gflags \
    glog \
    grpc \
    grpc-native \
    libnl \
    openssl \
    protobuf \
    protobuf-native \
    rofl-common \
    rofl-ofdpa \
    systemd \
"

SRC_URI = "gitsm://github.com/bisdn/basebox.git;protocol=https;branch=jogo_ignore_frr_duplicate_onlink_route"
S = "${WORKDIR}/git"

inherit pkgconfig systemd

# should come from rofl...
TARGET_LDFLAGS:append = " -lssl"
TARGET_LDFLAGS:append = " -lcrypto"

FILES:${PN} += "/lib/systemd/system/baseboxd.service"
CONFFILES:${PN} += "${sysconfdir}/default/baseboxd"
# Enable baseboxd service (SYSTEMD_AUTO_ENABLE default is enable)
SYSTEMD_SERVICE:${PN} = "baseboxd.service"
