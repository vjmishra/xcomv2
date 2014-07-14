#ksh
XPDEXHOME=/xpedx
SMCFSDEPLOYMENTHOME=$XPDEXHOME/deployments
EARFILEHOME=$XPDEXHOME/sterling/Foundation/external_deployments
DATE=`date +%Y-%m-%d`
CURRENTZIPFILE="smcfs-${DATE}.zip"
ZIPFILEHOME=/home/share/xpadmin/buildsource

cd $SMCFSDEPLOYMENTHOME
echo "in deploysmcfs.sh "
#Check for return status
if mv /xpedx/sterling/Foundation/external_deployments/smcfs.ear $SMCFSDEPLOYMENTHOME/smcfs.ear.new; then
	#cd $XPDEXHOME/wldomain/xpdev
	#./stopsmcfsdev.sh
	# ML: remove the cache/tmp folders before starting the servers. This way, the deploy will not affect the site if it's being used. 
	#cd /xpedx/wldomain/xpdev/servers/smcfsdev
	#rm -R cache tmp
	cd $SMCFSDEPLOYMENTHOME
	TimeStamp=`date +"%Y%m%d-%H%M"`
	mv smcfs.ear archive/smcfs.ear."${TimeStamp}"
	mv smcfs.ear.new smcfs.ear
	echo "SUCCESS! deployed smcfs.ear to $SMCFSDEPLOYMENTHOME"
else
	echo "ERROR/FAILURE! Could not deploy the smcfs.ear file. Check logs..." 1>&2
	exit 1
fi
