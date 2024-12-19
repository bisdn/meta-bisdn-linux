FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = "\
    file://802.1x-auth@.service \
    file://802.1x-handler.sh \
    file://hostapd-wired.conf \
    file://hostapd-wired@.service \
"

CONFFILES:${PN}:append = "/etc/hostapd-wired"

FILES:${PN}:append = " \
   ${systemd_system_unitdir}/802.1x-auth@.service \
   ${systemd_system_unitdir}/hostapd-wired@.service \
"

do_install:append() {
    install -m 0755 -d ${D}${sysconfdir}/hostapd
    install -m 0640 ${WORKDIR}/hostapd-wired.conf ${D}${sysconfdir}/hostapd/
    chmod 750 ${D}${sysconfdir}/hostapd

    install -m 0755 -d ${D}${libexecdir}
    install -m 0755 ${WORKDIR}/802.1x-handler.sh ${D}${libexecdir}/

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/802.1x-auth@.service ${D}${systemd_system_unitdir}
        install -m 0644 ${WORKDIR}/hostapd-wired@.service ${D}${systemd_system_unitdir}
    fi
}
