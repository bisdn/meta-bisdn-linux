OS_RELEASE_FIELDS += " BUILD_ID"

do_compile[nostamp] = "1"
do_install[nostamp] = "1"

do_compile_remove[vardeps] = "BUILD_ID"

## BUILD_ID[vardepsexclude] = "DATETIME"

## os-release.bb in meta/recipes-core assigns the
## following vars:
## ID = "${DISTRO}"
## NAME = "${DISTRO_NAME}"
## VERSION = "${DISTRO_VERSION}${@' (%s)' % DISTRO_CODENAME if 'DISTRO_CODENAME' in d else ''}"
## VERSION_ID = "${DISTRO_VERSION}"
## PRETTY_NAME = "${DISTRO_NAME} ${VERSION}"
## BUILD_ID ?= "${DATETIME}"
