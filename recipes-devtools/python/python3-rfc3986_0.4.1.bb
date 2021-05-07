DESCRIPTION = "Validating URI References per RFC 3986"
HOMEPAGE = "https://rfc3986.rtfd.org"
SECTION = "devel/python"
LICENSE = "Apache-2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=03731a0e7dbcb30cecdcec77cc93ec29"

SRC_URI[md5sum] = "b2b48cd36dabb82d5eaa54bbfb20d382"
SRC_URI[sha256sum] = "5ac85eb132fae7bbd811fa48d11984ae3104be30d44d397a351d004c633a68d2"

inherit setuptools3 pypi

# DEPENDS_default: python-pip

DEPENDS += " \
        python3-pip \
        "

# RDEPENDS_default: 
RDEPENDS_${PN} += " \
        "
