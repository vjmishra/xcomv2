#!ksh

ENVIRONMENT
DATE=`date +%Y-%m-%d`
 
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
 
LOGFILE=/xpedx/sci_build/buildall_$ENVIRONMENT_`date +"%Y%m%d-%H%M"`.log
cd /home/share/xpadmin/scripts/
#ML: This build script executes all the build scripts in sequence: smcfs, swc, and then cdt
echo "SMCFS, SWC, and CDT build about to start ..."
# Start smcfs build and if sucessful, then proceed to swc, and cdt
if nohup ksh -x buildsmcfs.sh; then			 
	echo "SUCCESS! Completed the smcfs build without errors. Continuing with the deployment" >> $LOGFILE
	echo "Moving smcfs.ear to /xpedx/deployments" >> $LOGFILE	 
	# sucess: then proceed to swc	 
	if nohup ksh -x buildswc.sh; then			 
		echo "SUCCESS! Completed the SWC build without errors. Continuing with the deployment" >> $LOGFILE			 	 			
		./sendnotification.sh SMCFS-AND-SWC Success mahmoud.lamriben@hp.com,balkhi.mohammad@hp.com $LOGFILE
	else
		echo "ERROR/FAILURE! There were errors in the SWC build. Check logs..." 1>&2 >> $LOGFILE
		./sendnotification.sh SMCFS-AND-SWC Error mahmoud.lamriben@hp.com,balkhi.mohammad@hp.com $LOGFILE
		exit 1
	fi
else
	echo "ERROR/FAILURE! There were errors in the SMCFS build. Check logs..." 1>&2 >> $LOGFILE
	./sendnotification.sh SMCFS-AND-SWC Error mahmoud.lamriben@hp.com,balkhi.mohammad@hp.com $LOGFILE
	exit 1
fi


 

