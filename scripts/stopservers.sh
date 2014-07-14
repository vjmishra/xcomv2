#ksh
DATE=`date +%Y-%m-%d`
HOST_NAME="$(hostname)"
echo "stopping smcfs on $HOST_NAME"
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=stg;; 
 zxpapps01) ENVIRONMENT=ps;; 
 zxpagnt01) ENVIRONMENT=prd;; 
 zxpagnt02) ENVIRONMENT=prd;; 
 zxpappmc01) ENVIRONMENT=mc;; 
 *);;
esac

cd /home/share/xpadmin/scripts
if nohup ksh -x stopsmcfs.sh; then
	echo "Finished stopping SMCFS server... "
	if nohup ksh -x stopswc.sh; then	
		echo "Finished stopping SWC server... "
	else
		echo "ERROR/FAILURE! There were errors while stopping the SWC servers. Check logs..." 1>&2 
		exit 1
	fi			
	CURRENTDIR=`pwd`
	#view current deployments
	cd /xpedx/deployments/
	ls -lt
	#change back to previous dir
	cd $CURRENTDIR
else
	echo "ERROR/FAILURE! There were errors while stopping the SMCFS servers. Check logs..." 1>&2 
	exit 1
fi			
		
		
 