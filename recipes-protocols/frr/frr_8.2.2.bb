require frr.inc

GIT_BRANCH = "stable/8.2"
# commit hash of release tag frr-8.2.2
SRCREV = "79188bf710e92acf42fb5b9b0a2e9593a5ee9b05"

PR="r1"

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
