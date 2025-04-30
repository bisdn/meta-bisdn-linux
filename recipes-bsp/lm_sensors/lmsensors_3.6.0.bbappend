# work around breackage from 86b20b84ec27 (lmsensors: Clean stale files
# for sensord to avoid incorrect GCC header dependencies)
EXTRA_OEMAKE:append = ' PROG_EXTRA="sensors ${PACKAGECONFIG_CONFARGS}"'
