SUMMARY = "libyang2 is a YANG data modelling language parser and toolkit written (and providing API) in C."
HOMEPAGE = "https://github.com/CESNET/libyang/"
SECTION = "libs/network"

DEPENDS = "libpcre2"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f3916d7d8d42a6508d0ea418cfff10ad"

# Prevent Yocto from being "helpful" and renaming it to libyang because the
# .so is only named libyang, to have separate packages for both libyang libs
DEBIAN_NOAUTONAME_${PN} = "1"
DEBIAN_NOAUTONAME_${PN}-doc = "1"
DEBIAN_NOAUTONAME_${PN}-src = "1"
DEBIAN_NOAUTONAME_${PN}-dev = "1"
DEBIAN_NOAUTONAME_${PN}-utils = "1"
DEBIAN_NOAUTONAME_${PN}-dbg = "1"

SRC_URI = " \
    git://github.com/CESNET/libyang.git;protocol=https;branch=master \
    file://0001-cmake-use-pkg-config-for-extracting-pcre2-version.patch \
"

SRCREV = "69d9fff65abb58beb0bb6aa9ecacd572ca1dfc56"

EXTRA_OECMAKE += " \
    -DENABLE_LYD_PRIV=ON \
"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-utils"

FILES_${PN} = " \
  ${libdir}/libyang.so.2* \
  ${libdir}/libyang2 \
"

FILES_${PN}-utils = " \
  ${bindir}/yanglint \
  ${bindir}/yangre \
"

inherit cmake
