require frr.inc

GIT_BRANCH = "stable/8.1"
# commit hash of release tag frr-8.1
SRCREV = "1752bfdfc14409c667f10b7ee2c99b9014abb271"

SRC_URI += " \
        file://0001-tools-fix-backing-up-previous-logs-in-generate_suppo.patch \
"

PR="r2"

DEPENDS += " \
        clippy-native \
        libyang2 \
"
do_compile_prepend_class-target () {
        export PYTHONHOME=${RECIPE_SYSROOT_NATIVE}/usr
}

FILES_${PN} += " \
  ${datadir}/yang \
"
