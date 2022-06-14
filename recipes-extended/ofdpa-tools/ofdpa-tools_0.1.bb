# Copyright (C) 2020 Ricardo Santos <ricardo.santos@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "BISDN Linux Switch Tcpdump"
HOMEPAGE = "https://www.bisdn.de/"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://switch_tcpdump;beginline=11;endline=27;md5=7cea826ca53066a324a52d1d8c3b1ddf"
SECTION = "base"

RDEPENDS:${PN} += "ofdpa"

SRC_URI = " \
  file://switch_tcpdump \
"

S = "${WORKDIR}"

do_install () {
  install -m 0755 -d ${D}${bindir}
  install -m 0755 switch_tcpdump  ${D}${bindir}
}

FILES:${PN} = "/"
