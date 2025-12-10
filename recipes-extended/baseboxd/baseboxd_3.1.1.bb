require baseboxd.inc
inherit meson

TARGET_LDFLAGS:remove = "-Wl,--as-needed"
TARGET_LDFLAGS:append = " -Wl,--no-as-needed"

SRCREV = "02c20e6c67cc3bb0e3039180b062b8b130782dd6"

# install service and sysconfig
do_install:append() {
   # add directories
   install -d ${D}${sysconfdir}/default \
              ${D}${systemd_unitdir}/system

   # install service file and config
   install -m 0644 ${S}/pkg/systemd/sysconfig.template ${D}${sysconfdir}/default/baseboxd

   # update service file
   sed -i -e 's,/etc/sysconfig/baseboxd,/etc/default/baseboxd,g' \
          ${D}${systemd_unitdir}/system/baseboxd.service

   # install default configuration directory and copy files over
   install -d ${D}/usr/share/baseboxd/default_configurations/simple-l2-bridge
   install -m 0644 ${S}/examples/networkd/simple-l2-bridge/* ${D}/usr/share/baseboxd/default_configurations/simple-l2-bridge/
}
