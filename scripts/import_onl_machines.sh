#!/usr/bin/env bash
#
#  SPDX-License-Identifier:     MPL-2.0

ONLPATH=$1
INSTALLER=$2

[ -d "$ONLPATH" ] || exit 1
[ -d "$INSTALLER" ] || exit 1

for archdir in "$ONLPATH/packages/platforms/"*/*; do
	arch=$(basename "$archdir")
	# we only support x86-64/grub
	[ "$arch" = "x86-64" ] || continue

	# find all platform-config directories
	platform_configs=$(find "$archdir" -type d -name platform-config)
	for config in $platform_configs; do
		# go through each revision of a platform
		for revdir in "$config"/*; do
			[ -d "$revdir" ] || continue
			# we are now at
			# .../as4630-54pe/platform-config/r0
			# revision is the directory name
			rev=$(basename "$revdir")

			# config file name has full device name:
			# .../as4630-54pe/platform-config/r0/src/lib/x86-64-accton-as4630-54pe-r0.yml
			# but the vendor name may be shortened, so use a
			# wildcard match
			configfile=$(ls -1 "$revdir"/src/lib/*.yml)

			# extract the full device name from filename
			onl_name=$(basename "$configfile")
			onl_name=${onl_name%.yml}

			# strip arch from front and revision from back
			plat_name=${onl_name#${arch}-}
			plat_name=${plat_name%-${rev}}

			# construct onie device name by replacing dashes with
			# underscores in the parts
			onie_name="${arch//-/_}-${plat_name//-/_}-${rev}"

			# target installer configuration directory
			machinedir="$INSTALLER/machine/$onie_name"

			# don't overwrite existing configs
			if [ -e "$machinedir" ]; then
				echo "skipping $onie_name"
				continue
			fi

			# Extract the configuration and create a platform.conf

			# these yml files have a reference to an external yml
			# file for the kernel, but we do not care about it, so
			# just convert it to a simple item:
			#   "<<: *foo" => "- foo"
			GRUB_SERIAL=$(cat "$configfile" | sed 's/<<: \*/- /' | yq -r .\"$onl_name\".grub.serial)
			KERNEL_ARGS=$(cat "$configfile" | sed 's/<<: \*/- /' | yq -r .\"$onl_name\".grub.args)

			# split the kernel args into
			# console args => grub
			# everything else => extra
			GRUB_CMDLINE_LINUX=""
			EXTRA_CMDLINE_LINUX=""
			for arg in $KERNEL_ARGS; do
				case "$arg" in
					console=*)
						GRUB_CMDLINE_LINUX="${GRUB_CMDLINE_LINUX:+${GRUB_CMDLINE_LINUX} }$arg"
						;;
					*)
						EXTRA_CMDLINE_LINUX="${EXTRA_CMDLINE_LINUX:+${EXTRA_CMDLINE_LINUX} }$arg"
						;;
				esac
			done

			mkdir -p "$machinedir"
			cat >>$machinedir/platform.conf << EOF
# This platform is untested and unsupported
GRUB_CMDLINE_LINUX="${GRUB_CMDLINE_LINUX}"
GRUB_SERIAL_COMMAND="serial ${GRUB_SERIAL}"
EXTRA_CMDLINE_LINUX="${EXTRA_CMDLINE_LINUX}"
EOF
			echo "imported $onie_name"
		done
	done
done
