# use systemd to manage networks
# See http://www.freedesktop.org/software/systemd/man/systemd.network.html
PACKAGECONFIG_append = ""

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI_remove = ""

SRC_URI_prepend = ""

SRC_URI += " \
  file://0001-networkd-fix-link-up.patch \
  file://0002-networkd-do-not-send-ipv6.patch \
"
