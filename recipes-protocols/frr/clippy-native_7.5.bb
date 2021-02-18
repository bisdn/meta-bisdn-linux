require frr.inc

GIT_BRANCH = "stable/7.5"
# commit hash of release tag frr-7.5
SRCREV = "35e42b176e957ad76db4cd909f56458286c76e28"

PR = "r1"

inherit native

EXTRA_OECONF  = " \
	       --disable-babeld \
	       --disable-bgp-announce \
	       --disable-bgp-vnc \
	       --disable-bgpd \
	       --disable-doc \
	       --disable-eigrpd \
	       --disable-isisd \
	       --disable-ldpd \
	       --disable-nhrpd \
 	       --disable-ospf6d \
 	       --disable-ospfapi \
 	       --disable-ospfclient \
 	       --disable-ospfd \
 	       --disable-pbrd \
 	       --disable-pimd \
 	       --disable-ripd \
 	       --disable-ripngd \
 	       --disable-silent-rules \
	       --disable-vtysh \
 	       --disable-watchfrr \
	       --disable-zebra \
	       --disable-zeromq \
	       --enable-clippy-only \
	       "

do_compile_prepend_class-native () {
       export PYTHONHOME=${RECIPE_SYSROOT_NATIVE}/usr
       install -m 0755 -d ${WORKDIR}/build/tests/isisd
}

do_install() {
    install -d ${D}${base_libdir}
    install -c -m 755 ${WORKDIR}/build/lib/clippy ${D}${base_libdir}/clippy
}

BBCLASSEXTEND = "native nativesdk"
