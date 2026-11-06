FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://0001-Remove-emsign-437.patch \
    file://0002-Restore-unaffected-emSign-roots-439-440.patch \
    file://0003-remove-Comodo-AAA-Certificate-Services-CA-certificat.patch \
"
