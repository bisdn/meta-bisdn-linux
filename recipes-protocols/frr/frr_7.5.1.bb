require frr.inc

GIT_BRANCH = "stable/7.5"
# commit hash of release tag frr-7.5.1
SRCREV = "df7ab485bde1a511f131f7ad6b70cb43c48c8e6d"

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
