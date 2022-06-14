FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
    file://ip6tables.rules \
    file://iptables.rules \
"

do_install:append() {
    install -d ${D}${sysconfdir}/iptables
    install -m 0644 ${WORKDIR}/iptables.rules \
                    ${WORKDIR}/ip6tables.rules \
                    ${D}${sysconfdir}/iptables
}
