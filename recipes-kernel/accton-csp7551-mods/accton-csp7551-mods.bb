SUMMARY = "CSP 7551 platform modules"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

inherit module

# Platform drivers taken from:
# https://github.com/ShoneWu/sonic-buildimage/tree/202205_fd5ddba_csp7551/platform/barefoot/sonic-platform-modules-accton/csp7551/modules
SRC_URI = " \
          file://Makefile \
          file://fpga_driver.c \
          file://accton_i2c_cpld.c \
          file://x86-64-accton-csp7551-sfp.c \
          file://0001-accton-csp7551-sfp-update-for-Linux-6.1.patch;striplevel=4 \
          file://0002-accton-csp7551-cpld-update-for-Linux-6.1.patch;striplevel=4 \
          file://COPYING \
          "

S = "${WORKDIR}"

# Let platform init script load modules as needed
KERNEL_MODULE_PROBECONF = "fpga_driver"
module_conf_fpga_driver = "blacklist fpga_driver"
