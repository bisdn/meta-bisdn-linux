# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Convenience library to interact with Broadcom OF-DPA based switches"
HOMEPAGE = "https://github.com/bisdn/rofl-ofdpa"
LICENSE = "MPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=815ca599c9df247a0c7f619bab123dad"

DEPENDS = "rofl-common"

SRC_URI = "git://github.com/bisdn/rofl-ofdpa.git"
SRCREV = "4de04a6f97e621792ada5cfc2266f9c4ebe77583"

S = "${WORKDIR}/git"

inherit autotools pkgconfig
