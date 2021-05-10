include systemd.inc

SRCREV = "511646b8ac5c82f210b16920044465756913d238"

SRC_URI += " \
    file://0001-v245-backport-add-vlan-protocol-parameter-for-bridge.patch \
    file://0001-v247-backport-basic-cap-list-parse-print-numerical-capabilities.patch \
    file://0001-v247-backport-network-drop-IPv6LL-address-when-LinkLocalAddressing.patch \
    file://0001-v248-network-introduce-network_verify.patch \
    file://0002-v248-network-disable-LinkLocalAddressing-and-IPv6AcceptRA.patch \
"
