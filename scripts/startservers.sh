#ksh
DATE=`date +%Y-%m-%d`
HOST_NAME="$(hostname)"
echo "starting smcfs and SWC on $HOST_NAME"
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

cd /home/share/xpadmin/scripts/
if nohup ksh -x startswc.sh; then
	echo "Finished starting SWC server... "
	if nohup ksh -x startsmcfs.sh; then	
		echo "Finished stostarting SMCFS server... "
	else
		echo "ERROR/FAILURE! There were errors while starting the SMCFS servers. Check logs..." 1>&2 
		exit 1
	fi						 	 
else
	echo "ERROR/FAILURE! There were errors while starting the SWC server. Check logs..." 1>&2 
	exit 1
fi			
		
		
 