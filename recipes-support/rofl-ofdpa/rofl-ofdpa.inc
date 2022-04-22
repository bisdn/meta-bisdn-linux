# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Convenience library to interact with Broadcom OF-DPA based switches"
HOMEPAGE = "https://github.com/bisdn/rofl-ofdpa"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=815ca599c9df247a0c7f619bab123dad"

DEPENDS = "rofl-common"

SRC_URI = "git://github.com/bisdn/rofl-ofdpa.git;protocol=https"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
