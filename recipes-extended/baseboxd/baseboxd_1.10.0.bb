require baseboxd.inc
inherit meson

TARGET_LDFLAGS_remove = "-Wl,--as-needed"
TARGET_LDFLAGS_append = " -Wl,--no-as-needed"

SRCREV = "e3cefdf514d7fd3a019effc7fdf0de2f2b23f4ea"

# install service and sysconfig
do_install_append() {
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
