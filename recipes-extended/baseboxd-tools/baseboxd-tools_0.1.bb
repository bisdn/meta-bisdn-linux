# Copyright (C) 2018 Pierluigi Greto <pierluigi.greto@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "BISDN-Linux baseboxd tools"
HOMEPAGE = "https://www.bisdn.de/"
LICENSE = "CLOSED"
SECTION = "base"
RDEPENDS_baseboxd-tools = "bash"

SRC_URI = " \
  file://basebox-change-config \
"


S = "${WORKDIR}"

do_install () {
  install -m 0755 -d ${D}${bindir}
  install -m 0755 basebox-change-config ${D}${bindir}
}

FILES_${PN} = "/"