SUMMARY = "libyang is a YANG data modelling language parser and toolkit written (and providing API) in C."
HOMEPAGE = "https://github.com/CESNET/libyang/"
SECTION = "libs/network"

DEPENDS = "libpcre"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=2982272c97a8e417a844857ca0d303b1"

SRC_URI = " \
    git://github.com/CESNET/libyang.git;protocol=https;branch=libyang1 \
"

SRCREV = "8c254b2b5d89d15244941bdfcff28aef141efd9f"

EXTRA_OECMAKE += " \
    -DENABLE_LYD_PRIV=ON \
"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-utils"

FILES_${PN} = " \
  ${libdir}/libyang.so.1* \
  ${libdir}/libyang1 \
"

FILES_${PN}-utils = " \
  ${bindir}/yanglint \
  ${bindir}/yangre \
"

inherit cmake
