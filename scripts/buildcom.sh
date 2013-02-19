#!/bin/ksh

XPDEXHOME=/xpedx
SMCFSBUILDHOME=/xpedx/sci_build/smcfs
DATE=`date +%Y-%m-%d`

#location of the zip files used for the build is specified below.
LOGFILE=/xpedx/sci_build/smcfs/COM_Build-`date +"%Y%m%d-%H%M"`.log

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
  zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpaappmc01) ENVIRONMENT=mc;; 
 *);;
esac

# Xpedxsmcfsbuild.xml is the ANT build script. This file is in CVS and is not automatically copied from CVS during each build. 
# Need to copy it so that any changes to it will be reflected in the build
         

cd $SMCFSBUILDHOME
echo "about to execute Xpedxsmcfsbuild.xml ant script " >> $LOGFILE
echo "about to execute Xpedxsmcfsbuild.xml ant script. Saving logs to $LOGFILE" 

# execute the build script using sci_ant
#ML: Don't send the process to the background (i.e remove & at the end of the below command) so that we can chain the commands and wait for execution of the build before triggering the next command.
#call the ant script and pass the build-pca target. see Xpedxsmcfsbuild.xml for more details
/xpedx/sterling/Foundation/bin/sci_ant.sh -f Xpedxsmcfsbuild.xml -Dbuild.env="$ENVIRONMENT" build-pca -logfile $LOGFILE
	#show log file immediately after. 
sleep 3
echo "tail -f $LOGFILE"
echo "SUCCESS! Completed the COM build without errors. " >> $LOGFILE
	 


 

