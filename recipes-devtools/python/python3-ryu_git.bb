DESCRIPTION = "Ryu component-based software defined networking framework"
HOMEPAGE = "http://osrg.github.io/ryu/"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

PV = "4.34+git${SRCPV}"
SRCREV = "c776e4cb68600b2ee0a4f38364f4a355502777f1"

SRCNAME = "ryu"
SRC_URI = "git://github.com/osrg/${SRCNAME}.git;protocol=https"

S = "${WORKDIR}/git"

inherit setuptools3

FILES_${PN} += "${datadir}/etc/${SRCNAME}/*"

DEPENDS += " \
        python3-pip \
        python3-pbr-native \
        "

RDEPENDS_${PN} += " \
        python3-eventlet \
        python3-msgpack \
        python3-netaddr \
        python3-oslo.config \
        python3-ovs \
        python3-routes \
        python3-six \
        python3-tinyrpc \
        python3-webob \
	python3-wrapt (>=1.7.0) \
	python3-greenlet \
	python3-stevedore (>=1.20) \
        "
FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += " \
  file://ofdpa.patch \
  file://0001-pcaplib.py-add-option-to-flush-each-packet.patch \
  file://ryu-manager \
  file://ryu-manager.service \
"

inherit systemd

do_install_append() {
    # add directories
    install -d ${D}${sysconfdir}/default \
               ${D}${systemd_unitdir}/system

    # install service and config file
    install -m 0644 ${WORKDIR}/ryu-manager ${D}${sysconfdir}/default
    install -m 0644 ${WORKDIR}/ryu-manager.service ${D}${systemd_unitdir}/system

}

FILES_${PN} += " \
    ${sysconfdir}/default \
    ${systemd_unitdir}/system \
"
CONFFILES_${PN} += " ${sysconfdir}/default/ryu-manager"
