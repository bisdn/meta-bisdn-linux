# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de> and Henrike Wissing <henrike.wissing@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

require minimal.bb

IMAGE_FEATURES += " package-management"

BISDN_SWITCH_IMAGE_EXTRA_INSTALL += "\
    baseboxd-tools \
    bridge-utils  \
    curl \
    docker-ce \
    ethtool \
    file \
    frr \
    git \
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
    procps \
    python3 \
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
