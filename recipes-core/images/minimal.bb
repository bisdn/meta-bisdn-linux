# Copyright (C) 2017 Tobias Jungel <tobias.jungel@bisdn.de> and Henrike Wissing <henrike.wissing@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

IMAGE_INSTALL = "packagegroup-core-boot \
    packagegroup-base-extended \
    ${BISDN_SWITCH_IMAGE_EXTRA_INSTALL} \
    ${CORE_IMAGE_EXTRA_INSTALL}"

BISDN_SWITCH_IMAGE_EXTRA_INSTALL = "\
    basebox-user \
    bash \
    e2fsprogs-resize2fs \
    e2fsprogs-tune2fs \
    haveged \
    i2c-tools \
    iproute2 \
    kernel-modules \
    less \
    onie-tools \
    openssh-sftp \
    openssh-sftp-server \
    parted \
    strace \
    util-linux \
    util-linux-blkid \
    "

BISDN_SWITCH_IMAGE_EXTRA_INSTALL:append:x86-64 = " grub"

IMAGE_FEATURES += "ssh-server-openssh"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

COPY_LIC_MANIFEST = "1"

IMAGE_FSTYPES = "tar.xz"

include onie-nos-installer.inc

inherit core-image

# opkg logs the current time as Installed-Time, so replace it with a fixed value
opkgstatus_reproducible () {
        if [ "${REPRODUCIBLE_TIMESTAMP_ROOTFS}" != "" ]; then
                if [ -f ${IMAGE_ROOTFS}${localstatedir}/lib/opkg/status ]; then
                        sed -i 's/Installed-Time: .*/Installed-Time: ${REPRODUCIBLE_TIMESTAMP_ROOTFS}/' \
                        ${IMAGE_ROOTFS}${localstatedir}/lib/opkg/status
                fi
        fi
}
ROOTFS_POSTPROCESS_COMMAND:append = " opkgstatus_reproducible;"

# Strip atime and ctime from rootfs tar, as they are dynamic and break
# reproducibility.
IMAGE_CMD_TAR = "tar --pax-option='delete=atime,delete=ctime'"
