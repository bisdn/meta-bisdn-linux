# use systemd to manage networks
# See http://www.freedesktop.org/software/systemd/man/systemd.network.html
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-241:"

SRC_URI += " \
  file://0001-networkd-fix-link-up.patch \
  file://0002-networkd-do-not-send-ipv6.patch \
"
