#!/bin/sh

# Based on:
# https://github.com/opencomputeproject/onie/blob/8f610cc0e9690182d4045f800dbcc4e021a4f843/demo/installer/u-boot-arch/install.sh

#  Copyright (C) 2014,2015 Curt Brune <curt@cumulusnetworks.com>
#  Copyright (C) 2015 david_yang <david_yang@accton.com>
#
#  SPDX-License-Identifier:     GPL-2.0

set -e

BISDN_ENABLE_NOS_MODE=1

hw_load() {
    echo "cp.b $img_start \$loadaddr $img_sz"
}

# Delete all partitions on /dev/sda except the diag (e.g., ACCTON-DIAG)
platform_erase_disk()
{
    local blk_dev="$1"
    local oIFS part parts part_num part_label

    oIFS=$IFS
    IFS="
"
    # remove all other NOS partitions

    # ugly: find a better way to get only partitions
    parts="$(sgdisk -p /dev/sda | grep '^ ' || true)"
    for part in $parts; do
        part_num="$(echo $part | awk '{print $1}')"
        part_label="$(echo $part | awk '{print $7}')"

        # keep diag partition intact
        [ -n "$DIAG_PART_NAME" ] && [ "$part_label" = "$DIAG_PART_NAME" ] && continue
        echo "removing partition $blk_dev$part_num ($part_label)"
        sgdisk -d $part_num $blk_dev || {
           echo "Error: unable to delete partition $part_num on $blk_dev"
           exit 1
        }
    done
    IFS=$oIFS
    partprobe $blk_dev
}

platform_get_firmware_type()
{
	echo "u-boot"
}

. ./platform.conf

platform_install_bootloader_entry()
{
    local blk_dev=$1
    local bisdn_linux_part=$2
    local bisdn_linux_mnt=$3
    local separator=

    if [ -f fitImage ]; then
	cp fitImage $bisdn_linux_mnt/boot/uImage
    fi

    if [ ! -f $bisdn_linux_mnt/boot/uImage ]; then
        echo "Error: No kernel image in root fs"
        exit 1
    fi

    # Fix broken u-boot environment variables (presumably left by another NOS).
    machine_fixups

    # Work-around to support yocto warrior-style fit node names which used '@'.
    if grep -q "kernel@1" $bisdn_linux_mnt/boot/uImage; then
        separator="@"
    else
        separator="-"
    fi

    # Find GUID of BISDN Linux partition and FIT configuration unit name, then
    # build a u-boot command string for loading and booting the kernel.
    hw_load_str="$(hw_load $blk_dev $bisdn_linux_part $separator)"

    echo "Updating U-Boot environment variables"
    (cat <<EOF
hw_load $hw_load_str
copy_img echo "Loading BISDN Linux $platform image..." && run hw_load
nos_bootcmd run copy_img && setenv bootargs quiet console=\$consoledev,\$baudrate && bootm \$loadaddr
EOF
    ) > /tmp/env.txt

    fw_setenv -f -s /tmp/env.txt
}
