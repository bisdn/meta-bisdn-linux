FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
  file://basebox-change-config.1 \
  file://client_cfg_purge.1 \
  file://client_classcolortable_dump.1 \
  file://client_debugcomp.1 \
  file://client_debuglvl.1 \
  file://client_drivshell.1 \
  file://client_event.1 \
  file://client_flowtable_dump.1 \
  file://client_grouptable_dump.1 \
  file://client_mirror_port_dump.1 \
  file://client_oam_dump.1 \
  file://client_port_table_dump.1 \
  file://client_tunnel_dump.1 \
  file://onie-bisdn-uninstall.1 \
  file://onie-bisdn-upgrade.1 \
"

do_install_append() {
  install -g 0 -o 0 -m 0644 ${WORKDIR}/basebox-*.1 ${D}${datadir}/man/man1/
  gzip ${D}${datadir}/man/man1/basebox-*.1

  install -g 0 -o 0 -m 0644 ${WORKDIR}/client_*.1 ${D}${datadir}/man/man1/
  gzip ${D}${datadir}/man/man1/client_*.1

  install -g 0 -o 0 -m 0644 ${WORKDIR}/onie-*.1 ${D}${datadir}/man/man1/
  gzip ${D}${datadir}/man/man1/onie-*.1
}
