# Copyright (C) 2017 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

IMAGE_INSTALL = "packagegroup-core-boot \
    packagegroup-base-extended \
    ${BISDN_SWITCH_IMAGE_EXTRA_INSTALL} \
    ${CORE_IMAGE_EXTRA_INSTALL}"


BISDN_SWITCH_IMAGE_EXTRA_INSTALL = "\
    bash \
    dropbear \
    e2fsprogs-resize2fs \
    e2fsprogs-tune2fs \
    grub \
    iproute2 \
    kernel-module-linux-kernel-bde \
    kernel-module-linux-user-bde \
    kernel-modules \
    less \
    ofagent \
    ofdpa \
    parted \
    util-linux \
    util-linux-blkid \
    "

#MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += "python"
#MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += "python-modules"
#MACHINE_ESSENTIAL_EXTRA_RRECOMMENDS += "python-msgpack"

IMAGE_LINGUAS = " "

LICENSE = "MIT"


inherit core-image

