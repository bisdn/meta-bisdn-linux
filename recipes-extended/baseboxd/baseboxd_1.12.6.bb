require baseboxd.inc
inherit meson

TARGET_LDFLAGS:remove = "-Wl,--as-needed"
TARGET_LDFLAGS:append = " -Wl,--no-as-needed"

SRCREV = "2ec3fd92a6df180e958fdfd014028d3412f56bd3"

# install service and sysconfig
do_install:append() {
   # add directories
   install -d ${D}${sysconfdir}/default \
              ${D}${systemd_unitdir}/system

   # install service file and config
   install -m 0644 ${S}/pkg/systemd/sysconfig.template ${D}${sysconfdir}/default/baseboxd

   # HACK: baseboxd.service is installed into the wrong dir. we should configure this proper.
   rm -rf ${D}/usr/lib
   install -m 0644 ${B}/baseboxd.service ${D}${systemd_unitdir}/system

   # update service file
   sed -i -e 's,/etc/sysconfig/baseboxd,/etc/default/baseboxd,g' \
          ${D}${systemd_unitdir}/system/baseboxd.service
}
