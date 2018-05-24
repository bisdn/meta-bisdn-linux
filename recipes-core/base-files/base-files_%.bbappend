FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

dirs755 += "/mnt/onie-boot"

do_install_append () {
  cat >> ${D}${sysconfdir}/fstab <<EOF
# onie
LABEL=ONIE-BOOT      /mnt/onie-boot       auto       defaults,noauto       0  2
EOF
}
