DESCRIPTION = "A Python re-implementation of the Rails routes system."
HOMEPAGE = "http://routes.groovie.org"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=90976c1a0e3029278f882cfe2e84a6ae"

PV = "2.4.1+git${SRCPV}"
SRCREV = "2dcef8079cf09f427eeb0be62374f6c1a52bf59d"

SRCNAME = "Routes"
SRC_URI = "git://github.com/bbangert/routes.git"

S = "${WORKDIR}/git"

inherit setuptools3

RDEPENDS_${PN} += "python3-repoze.lru"
