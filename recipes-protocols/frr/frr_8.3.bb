require frr.inc

GIT_BRANCH = "stable/8.3"
# commit hash of release tag frr-8.3
SRCREV = "d66a1ca8da269a4de0084db45c5a9fc3352ae16c"

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
