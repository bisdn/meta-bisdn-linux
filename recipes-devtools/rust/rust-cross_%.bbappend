do_compile:prepend:arm() {
    if ${@bb.utils.contains('TUNE_FEATURES', 'neon', 'false', 'true', d)}; then
            # disable neon explicitly
            sed -i 's/"features": "\(.*\)"/"features": "\1,-neon"/' ${WORKDIR}/targets/arm-poky-linux-gnueabi.json
            # disable replacing smovs* with neon
            sed -i 's/"features": "\(.*\)"/"features": "\1,-neon-fpmovs"/' ${WORKDIR}/targets/arm-poky-linux-gnueabi.json
    fi
}
