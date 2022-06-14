SUMMARY = "libyang2 is a YANG data modelling language parser and toolkit written (and providing API) in C."
HOMEPAGE = "https://github.com/CESNET/libyang/"
SECTION = "libs/network"

DEPENDS = "libpcre2"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=f3916d7d8d42a6508d0ea418cfff10ad"

# Prevent Yocto from being "helpful" and renaming it to libyang because the
# .so is only named libyang, to have separate packages for both libyang libs
DEBIAN_NOAUTONAME:${PN} = "1"
DEBIAN_NOAUTONAME:${PN}-doc = "1"
DEBIAN_NOAUTONAME:${PN}-src = "1"
DEBIAN_NOAUTONAME:${PN}-dev = "1"
DEBIAN_NOAUTONAME:${PN}-utils = "1"
DEBIAN_NOAUTONAME:${PN}-dbg = "1"

SRC_URI = " \
    git://github.com/CESNET/libyang.git;protocol=https;branch=master \
"

SRCREV = "87375f15159545a87a1e0de200f5d9d67e9091d7"

EXTRA_OECMAKE += " \
    -DENABLE_LYD_PRIV=ON \
"

S = "${WORKDIR}/git"

PACKAGES += "${PN}-utils"

FILES:${PN} = " \
  ${libdir}/libyang.so.2* \
  ${libdir}/libyang2 \
"

FILES:${PN}-utils = " \
  ${bindir}/yanglint \
  ${bindir}/yangre \
"

inherit cmake pkgconfig
