require systemd.inc

SRC_URI += " \
    file://0001-partition-fix-build-with-newer-linux-btrfs.h-uapi-he.patch \
    file://0001-basic-linux-update-linux-uapi-headers.patch \
    file://0001-BRPORT_LOCKED.patch \
    file://0001-NO_LL_LEARN.patch \
"
