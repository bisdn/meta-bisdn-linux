FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = "\
    file://0001-Split-ap_sta_set_authorized-into-two-steps.patch \
    file://0002-netlink-allow-listening-for-neighbor-events.patch \
    file://0003-add-wired-driver-extra-options.patch \
    file://0004-import-base-MAB-handling-code-from-Cumulus.patch \
    file://0005-use-a-timer-for-mab-instead-of-forcing-drivers-to-se.patch \
    file://0006-drivers-add-a-linux-wired-driver.patch \
    file://0007-driver_linux_wired-handle-neighs-going-away.patch \
    file://0008-driver_wired_linux-flush-all-entries-on-start-stop.patch \
    file://0009-driver_wired_linux-wait-for-bridge-attachment.patch \
    file://defconfig \
    file://hostapd-wired.conf \
    file://hostapd-wired@.service \
"

FILES:${PN}:append = " \
   ${systemd_system_unitdir}/hostapd-wired@.service \
"

do_install:append() {
    install -m 0755 -d ${D}${sysconfdir}/hostapd
    install -m 0640 ${WORKDIR}/hostapd-wired.conf ${D}${sysconfdir}/hostapd/hostapd-wired.conf.example
    chmod 750 ${D}${sysconfdir}/hostapd

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/hostapd-wired@.service ${D}${systemd_system_unitdir}
    fi
}
