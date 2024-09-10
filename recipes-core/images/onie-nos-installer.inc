# inherit image
SUMMARY = "Create BISDN Linux ONIE NOS Installer image"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
INHIBIT_DEFAULT_DEPS = "1"

INSTALL_ROOTFS = "${IMGDEPLOYDIR}/${IMAGE_BASENAME}-${MACHINE}.tar.xz"
INSTALL_KERNEL = "${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGETYPE}"

ONIEIMAGE_DIR ?= "${S}"
ONIEIMAGE_DIR_INSTALL = "${ONIEIMAGE_DIR}/installer/"

BISDN_ONIE_ADDITIONS ?= "${THISDIR}/../../scripts"
BISDN_ARCH ?= "x86_64"
BISDN_ONIE_PLATFORM ?= "${@'${ONIE_PLATFORM}'.replace('_','-')}"

ONIEIMAGE_SHELL_ARCHIVE_BODY ?= "${BISDN_ONIE_ADDITIONS}/installer/sharch_body.sh"
ONIEIMAGE_CONF_DIR ?= "${BISDN_ONIE_ADDITIONS}/installer/machine"

ONIEIMAGE_PAYLOAD_FILE = "${ONIEIMAGE_DIR}/onie_install.tar"
ONIEIMAGE_OUTPUT_FILE = "${ONIEIMAGE_DIR}/onie-installer.bin"

# we only use a tar.xz packed rootfs
IMAGE_FSTYPES = "tar.xz"

do_onie_bundle[depends] += "virtual/kernel:do_deploy"

do_onie_bundle () {
    mkdir --parent "${ONIEIMAGE_DIR_INSTALL}"
    cp "${INSTALL_KERNEL}" "${ONIEIMAGE_DIR_INSTALL}/"
    cp "${INSTALL_ROOTFS}" "${ONIEIMAGE_DIR_INSTALL}/rootfs.tar.xz"

    cp "${BISDN_ONIE_ADDITIONS}/installer/${BISDN_ARCH}/install.sh" "${ONIEIMAGE_DIR_INSTALL}/platform.sh"
    cp "${BISDN_ONIE_ADDITIONS}/installer/install.sh" "${ONIEIMAGE_DIR_INSTALL}"
    cp -r "${BISDN_ONIE_ADDITIONS}/installer/lib" "${ONIEIMAGE_DIR_INSTALL}"
    mkdir -p "${ONIEIMAGE_DIR_INSTALL}/machine"
    for platform in ${ONL_PLATFORM_SUPPORT}; do
        cp -r -L "${ONIEIMAGE_CONF_DIR}/${platform}" "${ONIEIMAGE_DIR_INSTALL}/machine"
    done

    # Generate the payload tar file
    tar -C "${ONIEIMAGE_DIR}" -cf "${ONIEIMAGE_PAYLOAD_FILE}" "installer"

    # Populate the shell archive body
    cp "${ONIEIMAGE_SHELL_ARCHIVE_BODY}" "${ONIEIMAGE_OUTPUT_FILE}"
    image_sha1=$(sha1sum "${ONIEIMAGE_PAYLOAD_FILE}" | awk '{print $1}')
    sed -i -e "s/%%IMAGE_SHA1%%/${image_sha1}/" "${ONIEIMAGE_OUTPUT_FILE}"
    build_date=$(date -d "${DATE}" +%Y-%m-%d)
    sed -i -e "s/%%BUILD_DATE%%/${build_date}/" "${ONIEIMAGE_OUTPUT_FILE}"

    # Generate the ONIE bin file
    cat "${ONIEIMAGE_PAYLOAD_FILE}" >> "${ONIEIMAGE_OUTPUT_FILE}"

    install -m 0644 "${ONIEIMAGE_OUTPUT_FILE}" "${DEPLOY_DIR_IMAGE}/onie-bisdn-${IMAGE_NAME}.bin"
    cd "${DEPLOY_DIR_IMAGE}"
    # remove old ONIE bin file
    if [ -L "./onie-bisdn-${IMAGE_LINK_NAME}.bin" ]; then
        OLD_IMAGE="$(readlink ./onie-bisdn-${IMAGE_LINK_NAME}.bin)"
        [ -f "$OLD_IMAGE" ] && rm -f "$OLD_IMAGE"
    fi
    ln -sf "./onie-bisdn-${IMAGE_NAME}.bin" "./onie-bisdn-${IMAGE_LINK_NAME}.bin"
}

SSTATETASKS += "do_onie_bundle"
SSTATE_SKIP_CREATION:task-onie-bundle = '1'

addtask do_onie_bundle after do_image_complete before do_build

