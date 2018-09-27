# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de> and Henrike Wissing <henrike.wissing@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

require bisdn-minimal-image.bb

IMAGE_FEATURES += " package-management"

BISDN_SWITCH_IMAGE_EXTRA_INSTALL += "\
    baseboxd \
    file \
    frr \
    iproute2-ss \
    kernel-module-linux-kernel-bde \
    kernel-module-linux-user-bde \
    less \
    lmsensors-fancontrol \
    lmsensors-pwmconfig \
    lmsensors-sensors \
    lmsensors-sensorsconfconvert \
    lmsensors-sensorsdetect \
    ofagent \
    ofdpa \
    ofdpa-grpc \
    procps \
    python \
    python-modules \
    python-msgpack \
    python-ryu \
    python2-ofdpa \
    tcpdump \
    "

IMAGE_LINGUAS = " "

LICENSE = "MIT"

do_install_motd_issue_date() {
			   echo "image built on ${DATE}" >> ${IMAGE_ROOTFS}${sysconfdir}/motd
}

ROOTFS_POSTPROCESS_COMMAND += " do_install_motd_issue_date ;"
