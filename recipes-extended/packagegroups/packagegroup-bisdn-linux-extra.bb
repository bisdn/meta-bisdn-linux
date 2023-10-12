SUMMARY = "Packagegroup for specifying installable packages for BISDN Linux"
LICENSE = "MPL-2.0"
LIC_FILES_CHCKSUM = "file://${COMMON_LICENSE_DIR}/MPL-2.0;md5=815ca599c9df247a0c7f619bab123dad"

inherit packagegroup

# optional packages as dependencies of this package to allow easy building
RDEPENDS:${PN} = "\
"
