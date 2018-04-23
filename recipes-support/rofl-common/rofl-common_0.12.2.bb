# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "OpenFlow protocol endpoint written in C++"
HOMEPAGE = "https://github.com/bisdn/rofl-common"
LICENSE = "MPLv2"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=5d425c8f3157dbf212db2ec53d9e5132"

DEPENDS = "openssl glog"

SRC_URI = "git://github.com/bisdn/rofl-common.git"
SRCREV = "e6a23c4c1773b3af3c72792e1c0196657e617874"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

#FILES_${PN}-dev += ""
