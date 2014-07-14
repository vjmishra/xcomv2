#!/bin/ksh
# This script is used to kickstart the CDT build on any environment. The hostname is extracted dynamically and then fed in the correct target for the xpedxCDTBuild.xml ant script.
# 8/1/2011 - Mahmoud Lamriben

ENVIRONMENT
CDTBUILDHOME=/xpedx/sci_build/CDT/
DATE=`date +%Y-%m-%d` 
TIMESTAMP=`date +"%Y%m%d-%H%M"`
#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=stage;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=masterconfig;; 
 
 *);;
esac

# change to the directory where xpedxCDTBuild.xml resides
cd $CDTBUILDHOME
# execute the build
cdtLogFile=/xpedx/sci_build/CDT/cdt_import-$TIMESTAMP.log
nohup /xpedx/sterling/Foundation/bin/sci_ant.sh -f xpedxCDTBuild.xml cdt_"$ENVIRONMENT"_import -logfile $cdtLogFile &  
echo "Import cdt process started. Below is the log file generated. Press Ctrl+C to exit out of tail..." 
# pause for a few seconds so the file can be read
sleep 5
tail -f $cdtLogFile