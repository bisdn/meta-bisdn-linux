#!/bin/sh

# Platform init for csp7551

set -o errexit -o nounset

# Assuming i2c-i801 driver is not loaded (it prevents fpga_driver from
# working properly).
modprobe fpga_driver
modprobe accton_i2c_cpld
modprobe x86-64-accton-csp7551-sfp

create_i2c_dev 24c64 0x57 0
create_i2c_dev cpld_csp7551 0x62 33
create_i2c_dev cpld_csp7551 0x64 34

for i in {1..32}; do
  create_i2c_dev csp7551_port$i 0x50 $i
done
