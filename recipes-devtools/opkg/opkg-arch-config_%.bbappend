# allow replacing the arch.conf when going back to 4.x
CONFFILES:${PN}:remove = " ${sysconfdir}/opkg/arch.conf"
