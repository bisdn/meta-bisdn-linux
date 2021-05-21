require frr.inc

GIT_BRANCH = "stable/7.5"
# commit hash of release tag frr-7.5.1
SRCREV = "df7ab485bde1a511f131f7ad6b70cb43c48c8e6d"

SRC_URI += " \
	file://0001-lib-correctly-exit-CLI-nodes-on-file-config-load.patch \
	file://0002-vtysh-fix-searching-commands-in-parent-nodes.patch \
	file://0001-reformat-python-files.patch \
	file://0002-tests-make-generate-support-bundle-python3-only.patch \
	file://0003-tools-Fix-warning-when-running-frr-reload.py.patch \
	file://0004-tools-make-frr-reload.py-python3-only.patch \
"

PR="r3"

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
