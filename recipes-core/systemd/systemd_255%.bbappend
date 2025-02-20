require systemd.inc

SRC_URI += " \
    file://255/0001-network-bridge-add-support-for-NO_LL_LEARN.patch \
    file://255/0002-network-bridge-add-support-for-IFLA_BRPORT_LOCKED.patch \
    file://255/0003-network-bridge-add-support-for-IFLA_BRPORT_MAB.patch \
"
