require recipes-kernel/linux-libc-headers/linux-libc-headers.inc

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2).split('.')[0]}"
MIN_VER = "${@oe.utils.trim_version("${PV}", 2).split('.')[1]}"

do_install_armmultilib () {
	if [ ${MAJ_VER} -gt 5 ]; then
		ARM_KVM_HEADER=""
	else
		if [ ${MAJ_VER} -eq 5 ] && [ ${MIN_VER} -ge 8 ]; then
			ARM_KVM_HEADER=""
		else
			ARM_KVM_HEADER="asm/kvm.h"
		fi
	fi
	oe_multilib_header asm/auxvec.h asm/bitsperlong.h asm/byteorder.h asm/fcntl.h asm/hwcap.h asm/ioctls.h $ARM_KVM_HEADER asm/kvm_para.h asm/mman.h asm/param.h asm/perf_regs.h asm/bpf_perf_event.h
	oe_multilib_header asm/posix_types.h asm/ptrace.h  asm/setup.h  asm/sigcontext.h asm/siginfo.h asm/signal.h asm/stat.h  asm/statfs.h asm/swab.h  asm/types.h asm/unistd.h
}

SRC_URI:append:libc-musl = "\
    file://0001-libc-compat.h-fix-some-issues-arising-from-in6.h.patch \
    file://0003-remove-inclusion-of-sysinfo.h-in-kernel.h.patch \
    file://0001-libc-compat.h-musl-_does_-define-IFF_LOWER_UP-DORMAN.patch \
    file://0001-include-linux-stddef.h-in-swab.h-uapi-header.patch \
   "

SRC_URI:append = "\
    file://0001-scripts-Use-fixed-input-and-output-files-instead-of-.patch \
    file://0001-kbuild-install_headers.sh-Strip-_UAPI-from-if-define.patch \
"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

SRC_URI[md5sum] = "753adc474bf799d569dec4f165ed92c3"
SRC_URI[sha256sum] = "dcdf99e43e98330d925016985bfbc7b83c66d367b714b2de0cbbfcbf83d8ca43"

DEPENDS += "rsync-native"
