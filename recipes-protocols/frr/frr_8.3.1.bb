require frr.inc

GIT_BRANCH = "stable/8.3"
# commit hash of release tag frr-8.3.1
SRCREV = "a74f7a9ad9623e6f9654fe4a7177e5da0b194828"

PR="r0"

DEPENDS += " \
        clippy-native \
        libyang2 \
"
do_compile:prepend:class-target () {
        export PYTHONHOME=${RECIPE_SYSROOT_NATIVE}/usr
}

FILES:${PN} += " \
  ${datadir}/yang \
"
