require frr.inc

GIT_BRANCH = "stable/8.0"
# commit hash of release tag frr-8.0
SRCREV = "9931db75f7730381ad4fba16efd39cbb67749470"

SRC_URI += " \
	file://0002-vtysh-fix-searching-commands-in-parent-nodes.patch \
	file://0004-tools-make-frr-reload.py-python3-only.patch \
	file://0005-tools-fix-error-handling-in-generate_support_bundle..patch \
	file://0006-tools-generate-human-readable-output-in-support-bund.patch \
"

PR="r1"

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
