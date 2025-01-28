#!/bin/sh

# Copyright (C) 2020 BISDN GmbH
# Author: Jonas Gorski <jonas.gorski@bisdn.de>
#
# SPDX-License-Identifier:     GPL-2.0

SYSTEM_BACKUP_FILE="etc/default/system-backup.txt"
USER_BACKUP_FILE="etc/default/user-backup.txt"

# $1 src $2 dest
cp_path_with_attr() {
	local owner attr

	[ ! -d "$2" ] || return 0

	src_base="$(dirname $2)"
	dest_base="$(dirname $2)"

	cp_path_with_attr "$(dirname $1)" "$(dirname $2)"

	mkdir "$2"
	owner="$(stat -c %u:%g $1)"
	attr="$(stat -c %a $1)"

	[ -n "$DEBUG" ] && echo "DEBUG: setting owner to $owner for file $2"
	chown "$owner" "$2"
	[ -n "$DEBUG" ] && echo "DEBUG: setting attr to $attr for file $2"
	chmod "$attr" "$2"
}

# $1 path $2 src $3 dest
add_to_backup() {
	local oldpath relpath newpath src_base dst_base

	# realpath will resolve any symlinks, but symlinks may point to outside
	# of the temporary filesystem. Busybox's realpath does not support not
	# resolving symlinks, so refuse to touch them at all.
	[ -L "$2/$1" ] && return 0

	# we cannot back up what doesn't exist
	[  -e "$2/$1" ] || return 0

	oldpath="$(realpath $2/$1)"

	case "$oldpath" in
		"$2"*)
			;;
		*)
			echo "WARNING: refusing to backup file outside of filesystem: $1 => $oldpath" >&2
			return 0
			;;
	esac

	relpath="${oldpath#${2}/}"
	newpath="$3/$relpath"

	[ -n "$DEBUG" ] && echo "DEBUG: adding $relpath to backup"

	cp_path_with_attr "$(dirname $oldpath)" "$(dirname $newpath)"
	cp -a "$oldpath" "$(dirname $newpath)"
}

# $1 path $2 src $3 dest
remove_from_backup() {
	local oldpath relpath newpath

	# realpath will resolve any symlinks, but symlinks may point to outside
	# of the temporary filesystem. Busybox's realpath does not support not
	# resolving symlinks, so refuse to touch them at all.
	[ -L "$2/$1" ] && return 0

	# no need to delete non-existing files
	[  -e "$2/$1" ] || return 0

	oldpath="$(realpath $2/$1)"

	case "$oldpath" in
		"$2"*)
			;;
		*)
			echo "WARNING: refusing to delete file outside of filesystem: $1 => $oldpath" >&2
			return 0
			;;
	esac

	relpath="${oldpath#${2}/}"
	newpath="$3/$relpath"

	[ -n "$DEBUG" ] && echo "DEBUG: removing $relpath from backup"

	rm -rf "$newpath"
}

# $1 file $2 mode (+|-) $3 src $4 dst
parse_file() {
	if [ ! -f "$1" ]; then
		return 0
	fi

	while read -r line; do
		case "$line" in
		"#"*)
			# comment
			;;
		"-"*)
			if [ "$2" = "-" ]; then
				remove_from_backup "${line:1}" $3 $4
			fi
			;;
		/*)
			if [ "$2" = "+" ]; then
				add_to_backup "$line" $3 $4
			fi
			;;
		esac
	done < $1
}

backup_systemd_state() {
	local enabled_services disabled_services service_files service_links
	local service enabled preset oneshot

	if [ -d "$1/lib/systemd/system" ]; then
		service_files=$(ls $1/lib/systemd/system)
	fi
	if [ -d "$1/etc/systemd/system/multi-user.target.wants" ]; then
		service_links=$(ls $1/etc/systemd/system/multi-user.target.wants)
	fi

	# collect explicitly disabled services
	for service in $service_files; do
		case "$service" in
			*@.service)
				# always disabled by default
				;;
			*.service)
				preset=$(grep "$service" $1/lib/systemd/system-preset/*.preset | cut -d ':' -f 2 | cut -d ' ' -f 1)
				# check only services enabled by default
				[ "$preset" = "enable" ] || continue

				wantedby=$(grep "^WantedBy=" $1/lib/systemd/system/$service || true)

				# skip oneshots, these got automatically disabled
				grep -q "^Type=oneshot" $1/lib/systemd/system/$service && continue

				# for now we only support multi-user services
				[ "$wantedby" = "WantedBy=multi-user.target" ] || continue

				# check for symlink in multi-user.target.wants
				enabled=$(find $1/etc/systemd/system/multi-user.target.wants/ -name $service)

				# nothing to do if still enabled
				[ -z "$enabled" ] || continue

				[ -n "$DEBUG" ] && echo "DEBUG: service disabled by user: $service"
				disabled_services="$disabled_services ${service%.service}"
				;;
		esac
	done

	# collect explicitly enabled services
	for service in $service_links; do
		case "$service" in
			*.service)
				service_file=$(readlink $1/etc/systemd/system/multi-user.target.wants/$service)
				base=$(basename $service_file)
				preset=$(grep "$base" $1/lib/systemd/system-preset/*.preset | cut -d ':' -f 2 | cut -d ' ' -f 1)

				# check only services disabled by default
				[ "$preset" != "enable" ] || continue

				[ -n "$DEBUG" ] && echo "DEBUG: service enabled by user: $service"
				enabled_services="$enabled_services ${service%.service}"
				;;
		esac
	done

	echo "$disabled_services" > $2/.SERVICES_DISABLED
	echo "$enabled_services" > $2/.SERVICES_ENABLED
}

# $1 src $2 dst
apply_fixups() {
	# releases pre 4.7.0 are missing gshadow in the backup list
	if [ -f "$2/etc/group" ] && [ ! -f "$2/etc/gshadow" ]; then
		[ -n "$DEBUG" ] && echo "DEBUG: /etc/group found but no /etc/gshadow"
		add_to_backup "/etc/gshadow" $1 $2
	fi
}

# $1 backup target $2 backup storage dir
create_backup()
{
	# step 1 - copy files to keep
	for file in $1/var/lib/opkg/info/*.conffiles; do
		[ -f "$file" ] || break
		parse_file $file "+" $1 $2
	done
	if [ -f "$1/$SYSTEM_BACKUP_FILE" ]; then
		parse_file "$1/$SYSTEM_BACKUP_FILE" "+" $1 $2
	fi

	if [ -f "$1/$USER_BACKUP_FILE" ]; then
		parse_file "$1/$USER_BACKUP_FILE" "+" $1 $2
	fi

	# step 2 - remove files to drop
	if [ -f "$1/$SYSTEM_BACKUP_FILE" ]; then
		parse_file "$1/$SYSTEM_BACKUP_FILE" "-" $1 $2
	fi
	if [ -f "$1/$SYSTEM_BACKUP_FILE" ]; then
		parse_file "$1/$USER_BACKUP_FILE" "-" $1 $2
	fi

	# step 3 - backup changed systemd service states
	backup_systemd_state $1 $2

	apply_fixups $1 $2

	# step 4 - check if anything is left
	[ -n "$(find $2 -type f)" ] || return 0

	# step 5 - remove empty directories
	find $2 -depth -type d -exec rmdir -p --ignore-fail-on-non-empty {} \;

	DO_RESTORE=true
}

bash_systemctl() {
	local dst=$1
	local action=$2
	local service=$3
	local base_service=${service/@*/@}

	if  [ "$action" = "enable" ]; then
		# check that the service exists in the target system
		if [ ! -f $dst/lib/systemd/system/$base_service.service ]; then
			echo "WARNING: cannot enable service $service.service: $base_service.service not found" >&2
		else
			# for any services to enable, create symlinks
			[ -n "$DEBUG" ] && echo "DEBUG: enabling service: $service.service"
			ln -fs /lib/systemd/system/$base_service.service \
				$dst/etc/systemd/system/multi-user.target.wants/$service.service
		fi
	elif [ "$action" = "disable" ]; then
		# for any services to disable, remove their symlinks
		[ -n "$DEBUG" ] && echo "DEBUG: disabling service: $service.service"
		rm -f $dst/etc/systemd/system/multi-user.target.wants/$service.service
	else
		echo "WARNING: unknown action $action for service $service" >&2
	fi
}

restore_systemd_state()
{
	local backup=$1
	local dst=$2
	local enabled_services disabled_service service baseservice

	if [ -f "$backup/.SERVICES_ENABLED" ]; then
		enabled_services=$(cat $backup/.SERVICES_ENABLED)
	fi
	if [ -f "$backup/.SERVICES_DISABLED" ]; then
		disabled_services=$(cat $backup/.SERVICES_DISABLED)
	fi

	for service in $enabled_services; do
		bash_systemctl "$dst" "enable" "$service"
	done

	for service in $disabled_services; do
		bash_systemctl "$dst" "disable" "$service"
	done

	rm -f $1/.SERVICES_ENABLED $2/.SERVICES_DISABLED
}

# $1 backup storage dir $2 restore target
restore_backup()
{
    # restore systemd services state
    restore_systemd_state $1 $2

    # rename existing target files if different
    for file in $(find $1 -type f); do
        basefile="${file#${1}/}"

        # nothing to do if it's a new file
        [ -f "$2/$basefile" ] || continue
        # nothing to do if they are the same
        cmp -s "$file" "$2/$basefile" && continue

	case "$basefile" in
		"etc/sudoers.d/"*)
			# sudo only ignores files with . or ending in ~
			BACKUP_SUFFIX=".default"
			;;
		*)
			BACKUP_SUFFIX="-default"
			;;
	esac

	[ -n "$DEBUG" ] && echo "DEBUG: $2/$basefile is different, creating backup as $basefile$BACKUP_SUFFIX" >&2

        mv "$2/$basefile" "$2/$basefile$BACKUP_SUFFIX"

    done

    # now copy the files
    for file in $1/*; do
	    [ -e "$file" ] || continue
	    cp -a $file $2
    done
}
