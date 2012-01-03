#!/bin/ksh
# This script starts the agents

cd
cd /xpedx/sterling/Foundation/bin

DATE=`date +%Y-%m-%d`
HOST_NAME="$(hostname)"
echo "Starting agents on $HOST_NAME"
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=stg;; 
 zxpapps01) ENVIRONMENT=ps;; 
 zxpagnt01) ENVIRONMENT=prod01;; 
 zxpagnt02) ENVIRONMENT=prod02;; 
 *);;
esac

echo "calling startagents$ENVIRONMENT.sh"
ksh -x startagents$ENVIRONMENT.sh
 
