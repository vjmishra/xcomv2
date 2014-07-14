#!/bin/ksh
# This script starts the smcfs weblogic server. It merely calls the startswcdev.sh script in /xpedx/wldomain/xpdev/

cd /xpedx/wldomain/xpdev/servers/swcdev
rm -R cache tmp

cd /xpedx/scripts/
ksh -x ./startswcdev.sh