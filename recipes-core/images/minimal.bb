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
    grub \
    haveged \
    i2c-tools \
    iproute2 \
    kernel-modules \
    less \
    onie-tools \
    onl \
    parted \
    strace \
    util-linux \
    util-linux-blkid \
    "

IMAGE_FEATURES += "ssh-server-openssh"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

COPY_LIC_MANIFEST = "1"

IMAGE_FSTYPES = "tar.xz"

include onie-nos-installer.inc

inherit core-image
