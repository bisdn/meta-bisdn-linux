# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de> and Henrike Wissing <henrike.wissing@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

require minimal.bb

IMAGE_FEATURES += " package-management"

BISDN_SWITCH_IMAGE_EXTRA_INSTALL += "\
    baseboxd \
    baseboxd-tools \
    bridge-utils  \
    curl \
    ethtool \
    file \
    frr \
    ipcalc \
    iproute2-ss \
    iptables \
    jq \
    keepalived \
    kernel-module-linux-kernel-bde \
    kernel-module-linux-user-bde \
    less \
    lmsensors-fancontrol \
    lmsensors-pwmconfig \
    lmsensors-sensors \
    lmsensors-sensorsconfconvert \
    lmsensors-sensorsdetect \
    man-db \
    man-pages \
    mstpd \
    ofagent \
    ofdpa \
    ofdpa-grpc \
    ofdpa-tools \
    procps \
    python \
    python-modules \
    python-msgpack \
    python-redis \
    python-requests \
    python-ryu \
    python2-ofdpa \
    python3 \
    python3-pip \
    radvd \
    tcpdump \
    tmux \
    vim \
    "

IMAGE_LINGUAS = " "

LICENSE = "MIT"
