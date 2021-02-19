require frr.inc

GIT_BRANCH = "stable/7.5"
# commit hash of release tag frr-7.5
SRCREV = "35e42b176e957ad76db4cd909f56458286c76e28"

PR="r1"

DEPENDS += " \
        clippy-native \
        libyang \
"
do_compile_prepend_class-target () {
        export PYTHONHOME=${RECIPE_SYSROOT_NATIVE}/usr
}

FILES_${PN} += " \
  ${datadir}/yang \
"
