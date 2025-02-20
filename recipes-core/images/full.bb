# Copyright (C) 2018 Tobias Jungel <tobias.jungel@bisdn.de> and Henrike Wissing <henrike.wissing@bisdn.de>
# Released under the MIT license (see COPYING.MIT for the terms)

require minimal.bb

IMAGE_FEATURES += " doc-pkgs package-management"

BISDN_SWITCH_IMAGE_EXTRA_INSTALL += "\
    baseboxd \
    baseboxd-tools \
    bash-completion \
    bridge-utils  \
    curl \
    docker-moby \
    ethtool \
    ethtool-bash-completion \
    file \
    freeradius \
    frr \
    git \
    git-bash-completion \
    grpc-cli \
    hostapd \
    ipcalc \
    iproute2-ss \
    iproute2-bash-completion \
    iptables \
    jq \
    keepalived \
    kmod-bash-completion \
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
    python3-docker \
    python3-ofdpa \
    python3-pip \
    python3-ryu \
    radvd \
    rsync \
    run-preinsts \
    systemd-bash-completion \
    tcpdump \
    tmux \
    util-linux-bash-completion \
    vim \
    "

IMAGE_LINGUAS = "en-us"

LICENSE = "MIT"
