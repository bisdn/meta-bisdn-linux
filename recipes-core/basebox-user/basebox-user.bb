# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "basebox user"
LICENSE = "MIT"
SECTION = "core"
RDEPENDS_${PN} = "sudo"

inherit useradd

USER = "basebox"
PASSWORD = "b-isdn"
USERADD_PARAM_${PN} = "-P ${PASSWORD} ${USER}"
USERADD_PACKAGES = "${PN}"

do_install () {
    install -m 0755 -d ${D}${sysconfdir}/sudoers.d
    echo "${USER} ALL=(ALL) ALL" > ${D}${sysconfdir}/sudoers.d/${USER}
}
