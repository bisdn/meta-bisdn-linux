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

	# Switch to src directory to make globbing (wildcard expansion) work
	cd "$3" || exit 1
	while read -r line; do
		case "$line" in
		"#"*)
			# comment
			;;
		"-"*)
			if [ "$2" = "-" ]; then
				# Globbing after removing the leading '-'
				for fpath in ./${line:1}; do
					remove_from_backup "${fpath}" $3 $4
				done
			fi
			;;
		/*)
			if [ "$2" = "+" ]; then
				for fpath in ./${line}; do
					add_to_backup "$fpath" $3 $4
				done
			fi
			;;
		esac
	done < $1
	cd -
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
	parse_file "$1/$SYSTEM_BACKUP_FILE" "+" $1 $2
	parse_file "$1/$USER_BACKUP_FILE" "+" $1 $2

	# step 2 - remove files to drop
	parse_file "$1/$SYSTEM_BACKUP_FILE" "-" $1 $2
	parse_file "$1/$USER_BACKUP_FILE" "-" $1 $2

	apply_fixups $1 $2

	# step 3 - check if anything is left
	[ -n "$(find $2 -type f)" ] || return 0

	# step 4 - remove empty directories
	find $2 -depth -type d -exec rmdir -p --ignore-fail-on-non-empty {} \;

	DO_RESTORE=true
}

# $1 backup storage dir $2 restore target
restore_backup()
{
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
	    cp -a $file $2
    done
}
