# CCACHE will leak into python's sysdata module, and any attempt to build
# modules locally will attemp to build with ccache.
# Since we do not ship a ccache package by default, make sure we do not build
# python with ccache so building modules works with provided packages.
CCACHE_DISABLE = '1'
