include systemd.inc

SRCREV = "511646b8ac5c82f210b16920044465756913d238"

SRC_URI += " \
    file://0001-v245-backport-add-vlan-protocol-parameter-for-bridge.patch \
    file://0001-v247-backport-basic-cap-list-parse-print-numerical-capabilities.patch \
"
