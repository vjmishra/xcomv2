#!/bin/ksh
# This script starts the smcfs weblogic server. It merely calls the startsmcfs$ENVIRONMENT.sh script in /xpedx/wldomain/xp$ENVIRONMENT/
# You can use this script to start smcfs without having to worry about which environment it's being executed on. 
#Note: The agent servers don't really have weblogic installed. so the script will never really get called on any of them. 
#Updated by: Mahmoud Lamriben
#Last Updated: 10/03/2011
  
HOST_NAME="$(hostname)"
echo "Starting smcfs on $HOST_NAME"
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappint01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=stg;; 
 zxpapps01) ENVIRONMENT=ps;; 
 zxpagnt01) ENVIRONMENT=prd;; 
 zxpagnt02) ENVIRONMENT=prd;; 
 zxpappmc01) ENVIRONMENT=mc;; 
 
 *);;
esac

#remove cache/tmp files
cd /xpedx/wldomain/xp$ENVIRONMENT/servers/smcfs$ENVIRONMENT
rm -R cache tmp
rm -R stage/smcfs/smcfs.ear

#start the smcfs server
cd /xpedx/scripts
nohup ./startsmcfs$ENVIRONMENT.sh &

#wait 6mins, then start OUs
sleep 360
#start the order updates 
cd /home/share/xpadmin/scripts/
ksh -x startorderupdates.sh

#show log file immediately after. 
echo "Press ctrl+c to exit out of the log file..."
sleep 3
tail -f /xpedx/logfiles/smcfs$ENVIRONMENT/stdout.log