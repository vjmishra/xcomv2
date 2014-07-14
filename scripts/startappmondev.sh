#!/bin/ksh
# This script starts the smcfs weblogic server. It merely calls the startsmcfsdev.sh script in /xpedx/wldomain/xpdev/

cd /xpedx/wldomain/xpdev/servers/appmondev
rm -R cache tmp

cd /xpedx/scripts/
ksh -x ./startappmondev.sh