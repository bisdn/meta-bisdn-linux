# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "BISDN-Linux ONIE toools"
HOMEPAGE = "https://www.bisdn.de/"
LICENSE = "CLOSED"
SECTION = "base"
DEPENDS = "bash"

SRC_URI = " \
  file://onie-bisdn-uninstall \
"


S = "${WORKDIR}"

do_install () {
  install -m 0755 -d ${D}${bindir}
  install -m 0755 onie-bisdn-uninstall ${D}${bindir}
}

FILES_${PN} = "/"
