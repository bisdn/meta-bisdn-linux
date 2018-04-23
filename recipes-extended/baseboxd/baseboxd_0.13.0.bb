# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "A tiny OpenFlow controller for OF-DPA switches"
HOMEPAGE = "https://github.com/bisdn/basebox"
LICENSE = "MPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=815ca599c9df247a0c7f619bab123dad"

DEPENDS = "gflags glog grpc grpc-native libnl protobuf protobuf-native rofl-common rofl-ofdpa"

SRC_URI = "git://github.com/bisdn/basebox.git \
           file://grpc-configure.patch"
SRCREV = "e42e3eca99658556db2d4c3dd7ea0dee82347084"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
