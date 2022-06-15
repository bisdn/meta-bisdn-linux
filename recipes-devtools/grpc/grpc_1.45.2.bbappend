# gRPC does not provide a way to use an external googletest package, so
# we need to use the submodule way. For some reason, this hangs on
# checkout so do the checkout manually. Luckily we only need googletest.

FILESEXTRAPATHS:append := "${THISDIR}/files:"

SRCREV_gtest = "0e402173c97aea7a00749e825b194bfede4f2e45"

SRC_URI:append = " \
           git://github.com/google/googletest.git;protocol=https;name=gtest;destsuffix=git/third_party/googletest;branch=main \
           file://1.45.2/0001-CMakelists.txt-allow-building-the-grpc_cli-utility-o.patch \
"

# build grpc_cli as well
EXTRA_OECMAKE:append = " \
    -DgRPC_BUILD_CLI=ON \
"

PACKAGE_BEFORE_PN:prepend = "${PN}-cli "

FILES:${PN}-cli = " \
    ${bindir}/grpc_cli \
    ${libdir}/libgrpc++test_config${SOLIBS} \
"
