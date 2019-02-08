FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://ip6tables.rules \
    file://ip6tables.service \
    file://iptables.rules \
    file://iptables.service \
"

inherit systemd

do_install_append() {
    install -d ${D}${sysconfdir}/iptables
    install -m 0644 ${WORKDIR}/iptables.rules \
                    ${WORKDIR}/ip6tables.rules \
                    ${D}${sysconfdir}/iptables

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/iptables.service \
                    ${WORKDIR}/ip6tables.service \
                    ${D}${systemd_system_unitdir}

    sed -i -e 's,@SBINDIR@,${sbindir},g' ${D}${systemd_system_unitdir}/iptables.service \
                                         ${D}${systemd_system_unitdir}/ip6tables.service
}

SYSTEMD_SERVICE_${PN} = "iptables.service ip6tables.service"
