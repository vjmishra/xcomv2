#!/bin/ksh

ENVIRONMENT=
XPDEXHOME=/xpedx
SMCFSBUILDHOME=/xpedx/sci_build/smcfs
DATE=`date +%Y-%m-%d`
LOGFILE=/xpedx/sci_build/smcfs/smcfsBuildTest-`date +"%Y%m%d-%H%M"`.log

HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpaappmc01) ENVIRONMENT=mc;; 
 *);;
esac
cd $SMCFSBUILDHOME
	nohup /xpedx/sterling/Foundation/bin/sci_ant.sh -f XpedxsmcfsbuildTest.xml -Dbuild.env="$ENVIRONMENT" smcfsBuildWithoutCVSFetch -logfile $LOGFILE 
	#show log file immediately after. 
	#sleep 5
	echo "tail -f $LOGFILE"
	echo "SUCCESS! Completed the build without errors. Continuing with the deployment" >> $LOGFILE
