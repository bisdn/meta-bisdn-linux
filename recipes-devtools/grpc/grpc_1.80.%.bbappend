FILESEXTRAPATHS:append := "${THISDIR}/files:"

SRC_URI:append = " \
           file://0001-CMakelists.txt-allow-building-the-grpc_cli-utility-o.patch \
"

# build grpc_cli as well
EXTRA_OECMAKE:append:class-target = " \
    -DgRPC_BUILD_CLI=ON \
"

PACKAGE_BEFORE_PN:prepend = "${PN}-cli "

FILES:${PN}-cli = " \
    ${bindir}/grpc_cli \
    ${libdir}/libgrpc++test_config${SOLIBS} \
"
