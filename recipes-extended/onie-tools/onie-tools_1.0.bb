# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "BISDN Linux ONIE tools"
HOMEPAGE = "https://www.bisdn.de/"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MPL-2.0;md5=815ca599c9df247a0c7f619bab123dad"
SECTION = "base"
RDEPENDS_onie-tools = "bash ${@oe.utils.conditional('BISDN_ARCH', 'u-boot-arch', ' u-boot-fw-utils', '', d)}"

SRC_URI = " \
  file://onie-bisdn-uninstall \
  file://onie-bisdn-upgrade \
  file://onie-bisdn-rescue \
"


S = "${WORKDIR}"

do_install () {
  install -m 0755 -d ${D}${bindir}
  install -m 0755 onie-bisdn-uninstall ${D}${bindir}
  install -m 0755 onie-bisdn-upgrade ${D}${bindir}
  install -m 0755 onie-bisdn-rescue ${D}${bindir}
}

FILES_${PN} = "/"
