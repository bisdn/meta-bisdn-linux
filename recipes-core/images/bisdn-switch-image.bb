# Copyright (C) 2017 Tobias Jungel <tobias.jungel@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

IMAGE_FEATURES += " package-management"

IMAGE_INSTALL = "packagegroup-core-boot \
    packagegroup-base-extended \
    ${BISDN_SWITCH_IMAGE_EXTRA_INSTALL} \
    ${CORE_IMAGE_EXTRA_INSTALL}"


BISDN_SWITCH_IMAGE_EXTRA_INSTALL = "\
    baseboxd \
    bash \
    dropbear \
    e2fsprogs-resize2fs \
    e2fsprogs-tune2fs \
    grub \
    i2c-tools \
    iproute2 \
    iproute2-ss \
    kernel-module-linux-kernel-bde \
    kernel-module-linux-user-bde \
    kernel-modules \
    less \
    lmsensors-fancontrol \
    lmsensors-pwmconfig \
    lmsensors-sensors \
    lmsensors-sensorsconfconvert \
    lmsensors-sensorsdetect \
    ofagent \
    ofdpa \
    ofdpa-grpc \
    onie-tools \
    onl \
    parted \
    procps \
    python \
    python-modules \
    python-msgpack \
    python2-ofdpa \
    tcpdump \
    util-linux \
    util-linux-blkid \
    "

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_FSTYPES += " tar.xz"

do_install_motd_issue_date() {
			   echo "image built on ${DATE}" >> ${IMAGE_ROOTFS}${sysconfdir}/motd
}

ROOTFS_POSTPROCESS_COMMAND += " do_install_motd_issue_date ;"
