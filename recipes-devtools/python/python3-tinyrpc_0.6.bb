DESCRIPTION = "A small, modular, transport and protocol neutral \
RPC library that, among other things, supports JSON-RPC and zmq."
HOMEPAGE = "http://github.com/mbr/tinyrpc"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI[md5sum] = "2663568ba3e1793da7b21003f0847fdd"
SRC_URI[sha256sum] = "b4d64227f023f1f0736ef137bd4c901c7652563e62c7a20329222c3a0b14d84f"

inherit setuptools3 pypi

RDEPENDS_${PN} += " \
        python3-six \
        python3-gevent \
        python3-requests \
        python3-websocket-client \
        python3-jsonext \
        python3-gevent-websocket \
        python3-werkzeug \
        python3-pyzmq \
        "
