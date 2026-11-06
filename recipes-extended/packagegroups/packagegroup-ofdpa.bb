SUMMARY = "Packagegroup for specifying required packages for Broadcom XGS support via OF-DPA"
LICENSE = "MPL-2.0"
LIC_FILES_CHCKSUM = "file://${COMMON_LICENSE_DIR}/MPL-2.0;md5=815ca599c9df247a0c7f619bab123dad"

inherit packagegroup

RDEPENDS:${PN} = "\
    baseboxd \
    ofagent \
    grpc-cli \
    ofdpa \
    ofdpa-grpc \
    ofdpa-tools \
    python3-ofdpa \
    python3-ryu \
"
