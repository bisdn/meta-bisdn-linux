# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de> and Henrike Wissing <henrike.wissing@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

require minimal.bb

IMAGE_FEATURES += " doc-pkgs package-management"

BISDN_SWITCH_IMAGE_EXTRA_INSTALL += "\
    baseboxd \
    baseboxd-tools \
    bridge-utils  \
    curl \
    docker-ce \
    ethtool \
    file \
    frr \
    git \
    grpc-cli \
    ipcalc \
    iproute2-ss \
    iptables \
    jq \
    keepalived \
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
    python3 \
    python3-ofdpa \
    python3-pip \
    python3-ryu \
    radvd \
    rsync \
    run-preinsts \
    tcpdump \
    tmux \
    vim \
    "

IMAGE_LINGUAS = "en-us"

LICENSE = "MIT"
