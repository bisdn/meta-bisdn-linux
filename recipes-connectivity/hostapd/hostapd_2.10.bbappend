FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = "\
    file://0001-netlink-allow-listening-for-neighbor-events.patch \
    file://0002-add-wired-driver-extra-options.patch \
    file://0003-import-base-MAB-handling-code-from-Cumulus.patch \
    file://0004-drivers-add-a-linux-wired-driver.patch \
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
