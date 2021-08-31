FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
   file://system-backup.txt \
   file://user-backup.txt \
"

dirs755 += "/mnt/onie-boot"

do_install_append () {
  cat >> ${D}${sysconfdir}/fstab <<EOF
# onie
LABEL=ONIE-BOOT      /mnt/onie-boot       auto       defaults,noauto       0  2
EOF
  install -d ${D}${sysconfdir}/default
  install -m 0644 ${WORKDIR}/system-backup.txt ${D}${sysconfdir}/default/system-backup.txt
  install -m 0644 ${WORKDIR}/user-backup.txt ${D}${sysconfdir}/default/user-backup.txt
}

# /lib64 is required by tibit-poncntl
FILES_${PN}_append_x86-64 += "/lib64"

do_install_append_x86-64() {
    ln -sf lib ${D}/lib64
}
