SUMMARY = "A library for applications dealing with netlink sockets"
HOMEPAGE = "http://www.infradead.org/~tgr/libnl/"
SECTION = "libs/network"

PE = "1"
PR = "r5"

LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

DEPENDS = "flex-native bison-native"

##https://github.com/thom311/${BPN}/releases/download/${BPN}${@d.getVar('PV').replace('.','_')}/${BP}.tar.gz

SRC_URI = " \
    git://github.com/thom311/${BPN}.git;protocol=https;branch=main \
    file://0001-cache-fix-new-object-in-callback-v2-on-updated-objec.patch \
    file://0002-route-fix-IPv6-ecmp-route-deleted-nexthop-matching.patch \
    file://0003-nexthop-add-a-identical-helper-function.patch \
    file://0004-route-use-the-new-helper-function-for-comparing-next.patch \
    file://0005-link-bonding-parse-and-expose-bonding-options.patch \
    file://0006-WIP-add-info-slave-data-support.patch \
    file://0007-link-bonding-expose-state-on-enslaved-interfaces.patch \
    file://0008-bridge-vlan-add-per-vlan-stp-state-object-and-cache.patch \
    file://0009-route-route_obj-treat-each-IPv6-link-local-route-as-.patch \
    file://0010-link-ignore-incomplete-bridge-updates.patch \
"

# commit hash of release tag libnl3_8_0
SRCREV = "6b2533c02813ce21b27ea8318fbe1af95652a39e"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

do_configure:prepend() {
  mkdir -p ${S}/doc/m4
}

FILES:${PN} = "${libdir}/libnl-3.so.* \
               ${libdir}/libnl.so.* \
               ${sysconfdir}"
RREPLACES:${PN} = "libnl2"
RCONFLICTS:${PN} = "libnl2"
FILES:${PN}-dev += "${libdir}/libnl/cli/*/*.la"
FILES:${PN}-staticdev += "${libdir}/libnl/cli/*/*.a"

PACKAGES += "${PN}-cli ${PN}-genl ${PN}-idiag ${PN}-nf ${PN}-route ${PN}-xfrm"
FILES:${PN}-cli   = "${libdir}/libnl-cli-3.so.* \
                     ${libdir}/libnl/cli/*/*.so \
                     ${bindir}/genl-ctrl-list \
                     ${bindir}/idiag-socket-details \
                     ${bindir}/nf-* \
                     ${bindir}/nl-*"
FILES:${PN}-genl  = "${libdir}/libnl-genl-3.so.* \
                     ${libdir}/libnl-genl.so.*"
FILES:${PN}-idiag = "${libdir}/libnl-idiag-3.so.*"
FILES:${PN}-nf    = "${libdir}/libnl-nf-3.so.*"
FILES:${PN}-route = "${libdir}/libnl-route-3.so.*"
FILES:${PN}-xfrm  = "${libdir}/libnl-xfrm-3.so.*"
RREPLACES:${PN}-genl = "libnl-genl2"
RCONFLICTS:${PN}-genl = "libnl-genl2"
