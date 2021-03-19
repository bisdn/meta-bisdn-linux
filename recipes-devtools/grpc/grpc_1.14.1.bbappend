# gRPC does not provide a way to use an external googletest package, so
# we need to use the submodule way. For some reason, this hangs on
# checkout so do the checkout manually. Luckily we only need googletest.

# clear SRVREV, else bitbake will try to use it for everything
SRCREV_grpc := "${SRCREV}"
SRCREV = ""

FILESEXTRAPATHS_append := "${THISDIR}/files:"

SRC_URI = "git://github.com/grpc/grpc.git;protocol=https;branch=${BRANCH};name=grpc \
           git://github.com/google/googletest.git;protocol=https;name=gtest;tag=release-1.8.0;destsuffix=git/third_party/googletest \
           file://0001-CMakeLists.txt-Fix-libraries-installation-for-Linux.patch \
           file://force_gflags_libname.patch \
           file://add_grpc_cli_build_option.patch \
"

# build grpc_cli as well
EXTRA_OECMAKE_append = " \
    -DgRPC_BUILD_CLI=ON \
"

# gRPC_BUILD_CODEGEN

# remove grpc_cli from -dev and move it to the main package
FILES_${PN}-dev_remove = "${bindir}"
FILES_${PN}-dev += "${bindir}/*plugin"
FILES_${PN} += "${bindir}/grpc_cli"
