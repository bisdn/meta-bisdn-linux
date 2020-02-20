# Copyright (C) 2017 Tobias Jungel <tobias.jungel@bisdn.de> and Henrike Wissing <henrike.wissing@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

IMAGE_INSTALL = "packagegroup-core-boot \
    packagegroup-base-extended \
    ${BISDN_SWITCH_IMAGE_EXTRA_INSTALL} \
    ${CORE_IMAGE_EXTRA_INSTALL}"

BISDN_SWITCH_IMAGE_EXTRA_INSTALL = "\
    basebox-user \
    bash \
    dropbear \
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

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_FSTYPES += " tar.xz"
