do_install:append () {
	# restore older syntax to allow going back to pre 4.8.0 images
	sed -i 's/@includedir/#includedir/' ${D}${sysconfdir}/sudoers
}
