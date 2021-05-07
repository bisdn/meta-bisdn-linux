DESCRIPTION = "Better dates and times for Python"
HOMEPAGE = "https://github.com/crsmithdev/arrow"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=458d41a4064e4dc109666cfd941a29e4"

SRC_URI[md5sum] = "fafb41dadf2134688797bf1102eebeb6"
SRC_URI[sha256sum] = "a15ecfddf334316e3ac8695e48c15d1be0d6038603b33043930dcf0e675c86ee"

inherit setuptools3 pypi

RDEPENDS_${PN} += " \
        python3-dateutil \
        "
