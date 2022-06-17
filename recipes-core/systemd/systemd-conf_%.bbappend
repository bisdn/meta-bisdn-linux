do_install:prepend() {
	# restore old match to match only on management ports
	sed -i 's/Name=!veth\*/Name=en\* eth\*/' ${WORKDIR}/wired.network
}
