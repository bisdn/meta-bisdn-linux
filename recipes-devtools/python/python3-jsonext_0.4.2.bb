DESCRIPTION = "Well-structured helpers to help serializing commonly \
encountered structures to JSON (like datetime, to_dict(), etc."
HOMEPAGE = "http://github.com/mbr/jsonext"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI[md5sum] = "59207a244eba23bf628ed24935e4eee7"
SRC_URI[sha256sum] = "e7634e0b8d1a668bd2c92db5498f6162573feb72ac050c4415384e773b1ea091"

inherit setuptools3 pypi

RDEPENDS:${PN} += " \
        python3-six \
        python3-arrow \
        "
