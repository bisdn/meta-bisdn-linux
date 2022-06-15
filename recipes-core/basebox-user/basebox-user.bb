# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "basebox user"
LICENSE = "MIT"
SECTION = "core"
RDEPENDS_${PN} = "sudo"

inherit useradd

USER = "basebox"
# generated with openssl passwd -6 b-isdn
PASSWORD = "\$6\$2zTHd7.l2MZBTq5j\$LAwBkh6ILoFcqQyKhwzWe/jm.y/R3Mv0tsOhEbssq.ZLNiXivGpQUmKUWJSvoncQMj/jboLCQAH689wRfUy18."
USERADD_PARAM_${PN} = "-p '${PASSWORD}' ${USER}"
USERADD_PACKAGES = "${PN}"

do_install () {
    install -m 0755 -d ${D}${sysconfdir}/sudoers.d
    echo "${USER} ALL=(ALL) ALL" > ${D}${sysconfdir}/sudoers.d/${USER}
}
