#!/bin/ksh
# This script starts the agents

cd
cd /xpedx/sterling/Foundation/bin

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=dev;; 
 *);;
esac
nohup ./xpedxagentserver.sh port=1663 XPEDXAgentServer > /xpedx/sterling/Foundation/logs/Reprocess_`date +"%Y%m%d-%H%M"`.log 2>&1 & 
exit 0