#!/bin/ksh
# This script starts the swc weblogic server. It merely calls the startswc$ENVIRONMENT.sh script in /xpedx/wldomain/xp$ENVIRONMENT/
# You can use this script to start swc without having to worry about which environment it's being executed on. 
#Note: The agent servers don't really have weblogic installed. so the script will never really get called on any of them. 
#Updated by: Mahmoud Lamriben
#Last Updated: 10/03/2011
  
HOST_NAME="$(hostname)"
echo "Starting swc on $HOST_NAME"
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=stg;; 
 zxpapps01) ENVIRONMENT=ps;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 *);;
esac

cd /xpedx/wldomain/xp$ENVIRONMENT/servers/swc$ENVIRONMENT
rm -R cache tmp
rm -R stage/swc/swc.ear

#start the smcfs server
cd /xpedx/scripts
nohup ./startswc$ENVIRONMENT.sh &

#show log file immediately after. 
echo "Press ctrl+c to exit out of the log file..."
sleep 3
tail -f /xpedx/logfiles/swc$ENVIRONMENT/stdout.log