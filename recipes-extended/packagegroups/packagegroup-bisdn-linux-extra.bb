SUMMARY = "Packagegroup for specifying installable packages for BISDN Linux"
LICENSE = "MPL-2.0"
LIC_FILES_CHCKSUM = "file://${COMMON_LICENSE_DIR}/MPL-2.0;md5=815ca599c9df247a0c7f619bab123dad"

inherit packagegroup

# optional packages as dependencies of this package to allow easy building
RDEPENDS:${PN} = "\
    fping \
    iperf3 \
    ipmitool \
    net-tools \
    openvswitch \
    rp-pppoe \
    rp-pppoe-server \
    screen \
"

# utilities useful for debugging (zstd to extract coredumps)
RDEPENDS:${PN} += "\
    gdb \
    strace \
    zstd \
"

# Workaround a bug in bitbake where runall does not properly consider all
# transitive RDEPENDS chains, so add them explicitly.
# Can be dropped once the fix get backported or we switch to the next LTS Yocto.
#
# ref: https://git.yoctoproject.org/poky/commit/?id=61182659c212db24e52cdbcdbb043c7b0de86094
RDEPENDS:${PN} += " \
    python3-appdirs \
    python3-automat \
    python3-constantly \
    python3-hyperlink \
    python3-incremental \
    python3-pyhamcrest \
    python3-pyserial \
    python3-zopeinterface \
"
