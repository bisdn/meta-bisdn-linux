DESCRIPTION = "Websocket handler for the gevent pywsgi server, a Python network library"
HOMEPAGE = "https://gitlab.com/noppo/gevent-websocket"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=5aa2f1d4ea55d60497aa8c3debf29ab2"

SRC_URI[md5sum] = "e095bf3358175489a956949c1b4de9ff"
SRC_URI[sha256sum] = "7eaef32968290c9121f7c35b973e2cc302ffb076d018c9068d2f5ca8b2d85fb0"

inherit setuptools3 pypi

RDEPENDS_${PN} += " \
        python3-gevent \
        "
