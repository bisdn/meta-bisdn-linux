FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://0001-drop-emSign-certificates-from-vendored-certifi.patch \
"
