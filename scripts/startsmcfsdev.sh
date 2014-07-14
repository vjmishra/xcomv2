#!/bin/ksh
# This script starts the smcfs weblogic server. It merely calls the startsmcfsdev.sh script in /xpedx/wldomain/xpdev/

cd /xpedx/wldomain/xpdev/servers/smcfsdev
rm -R cache tmp

cd /xpedx/scripts
ksh -x ./startsmcfsdev.sh