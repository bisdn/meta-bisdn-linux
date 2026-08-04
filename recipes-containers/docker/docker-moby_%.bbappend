# Disable docker service
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
# we do not have seccomp in DISTRO_FEATURES, so do not enable it for docker
PACKAGECONFIG = "docker-init"
REQUIRED_DISTRO_FEATURES = "ipv6"
