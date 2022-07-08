FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += " \
  file://basebox-change-config.1 \
  file://onie-bisdn-uninstall.1 \
  file://onie-bisdn-upgrade.1 \
  file://onie-bisdn-rescue.1 \
"

do_install:append() {
  install -g 0 -o 0 -m 0644 ${WORKDIR}/basebox-*.1 ${D}${datadir}/man/man1/
  gzip ${D}${datadir}/man/man1/basebox-*.1

  install -g 0 -o 0 -m 0644 ${WORKDIR}/onie-*.1 ${D}${datadir}/man/man1/
  gzip ${D}${datadir}/man/man1/onie-*.1
}
