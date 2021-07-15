# Copyright (C) 2018 Pierluigi Greto <pierluigi.greto@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "BISDN Linux baseboxd tools"
HOMEPAGE = "https://www.bisdn.de/"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/MPL-2.0;md5=815ca599c9df247a0c7f619bab123dad"
SECTION = "base"
RDEPENDS_baseboxd-tools = "bash"

SRC_URI = " \
  file://basebox-change-config \
  file://bundle-debug-info \
"


S = "${WORKDIR}"

do_install () {
  install -m 0755 -d ${D}${bindir}
  install -m 0755 basebox-change-config ${D}${bindir}
  install -m 0755 bundle-debug-info ${D}${bindir}
}

FILES_${PN} = "/"
