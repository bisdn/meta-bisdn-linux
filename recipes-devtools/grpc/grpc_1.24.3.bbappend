# gRPC does not provide a way to use an external googletest package, so
# we need to use the submodule way. For some reason, this hangs on
# checkout so do the checkout manually. Luckily we only need googletest.

FILESEXTRAPATHS:append := "${THISDIR}/files:"

SRC_URI += " \
           git://github.com/google/googletest.git;protocol=https;name=gtest;tag=release-1.8.0;destsuffix=git/third_party/googletest;branch=main \
           git://github.com/google/benchmark.git;protocol=https;name=benchmark;tag=v1.5.0;destsuffix=git/third_party/benchmark \
           file://1.24.3/force_gflags_libname.patch \
           file://1.24.3/add_grpc_cli_build_option.patch \
           file://1.24.3/0001-fix-oe-patch.patch \
"

# build grpc_cli as well
EXTRA_OECMAKE:append = " \
    -DgRPC_BUILD_CLI=ON \
"

# gRPC_BUILD_CODEGEN

# remove grpc_cli from -dev and move it to the main package
FILES:${PN}-dev:remove = "${bindir}"
FILES:${PN}-dev += "${bindir}/*plugin"
FILES:${PN} += "${bindir}/grpc_cli"
