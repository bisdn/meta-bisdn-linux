# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "OpenFlow protocol endpoint written in C++"
HOMEPAGE = "https://github.com/bisdn/rofl-common"
LICENSE = "MPLv2"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=5d425c8f3157dbf212db2ec53d9e5132"

DEPENDS = " \
    glog \
    openssl \
"

SRC_URI = " \
	git://github.com/bisdn/rofl-common.git \
	file://001-fix-configure-glog.patch \
        "

S = "${WORKDIR}/git"

inherit autotools pkgconfig
