SUMMARY = "Runs preinstall scripts on first boot of the target device"
SECTION = "devel"

# The revision of the recipe
PR = "r1"

LICENSE = "MIT"
# Checksum to make sure the license does not change without maintainer noticing
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# Files needed for the service
SRC_URI = "file://run-preinsts \
           file://run-preinsts.service"

S = "${WORKDIR}"

INITSCRIPT_NAME = "run-preinsts"

inherit systemd

# Runtime dependency on systemd
RDEPENDS_${PN} += "systemd"

# Install service on system
SYSTEMD_SERVICE_$target{PN} = "run-preinsts.service"

# Enable service on system
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

do_install() {
	install -d ${D}${sbindir}
	install -m 0755 ${WORKDIR}/run-preinsts ${D}${sbindir}/

	install -d ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/run-preinsts.service ${D}${systemd_unitdir}/system/

	sed -i -e 's:#SYSCONFDIR#:${sysconfdir}:g' \
               -e 's:#SBINDIR#:${sbindir}:g' \
               -e 's:#BASE_BINDIR#:${base_bindir}:g' \
               -e 's:#LOCALSTATEDIR#:${localstatedir}:g' \
               ${D}${sbindir}/run-preinsts \
               ${D}${systemd_unitdir}/system/run-preinsts.service
}

# Tell yocto you are adding all files to the package, or else it will see the
# files as not belonging to any package and give an error.
FILES_${PN} += "${WORKDIR}/run-preinsts.service"
FILES_${PN} += "${sbindir}/run-preinsts"
SYSTEMD_SERVICE_${PN} = "run-preinsts.service"
