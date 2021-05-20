SUMMARY = "A tiny LRU cache implementation and decorator"
DESCRIPTION = "repoze.lru is a LRU (least recently used) cache implementation. \
Keys and values that are not used frequently will be evicted from the cache faster \
than keys and values that are used frequently. \
"
HOMEPAGE = "https://pypi.python.org/pypi/repoze.lru"
SECTION = "devel/python"
LICENSE = "BSD-Modification-copyright"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=2c33cdbc6bc9ae6e5d64152fdb754292"

SRC_URI[md5sum] = "c08cc030387e0b1fc53c5c7d964b35e2"
SRC_URI[sha256sum] = "0429a75e19380e4ed50c0694e26ac8819b4ea7851ee1fc7583c8572db80aff77"

inherit setuptools3 pypi
