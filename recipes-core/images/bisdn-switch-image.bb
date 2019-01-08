# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de> and Henrike Wissing <henrike.wissing@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

require bisdn-minimal-image.bb

IMAGE_FEATURES += " package-management"

BISDN_SWITCH_IMAGE_EXTRA_INSTALL += "\
    baseboxd \
    baseboxd-tools \
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
    polkit \
    procps \
    python \
    python-modules \
    python-msgpack \
    python-redis \
    python-requests \
    python-ryu \
    python2-ofdpa \
    radvd \
    tcpdump \
    "

IMAGE_LINGUAS = " "

LICENSE = "MIT"

do_install_motd_issue_date() {
			   echo "image built on ${DATE}" >> ${IMAGE_ROOTFS}${sysconfdir}/motd
}

do_install_motd_issue_date[nostamp] = "1"

ROOTFS_POSTPROCESS_COMMAND += " do_install_motd_issue_date ;"
