#ksh
XPDEXHOME=/xpedx
SMCFSDEPLOYMENTHOME=$XPDEXHOME/deployments
EARFILEHOME=$XPDEXHOME/sterling/Foundation/external_deployments
DATE=`date +%Y-%m-%d`
TIMESTAMP=`date +"%Y%m%d-%H%M"`
CURRENTZIPFILE="smcfs-${DATE}.zip"
ZIPFILEHOME=/home/share/xpadmin/buildsource

cd $SMCFSDEPLOYMENTHOME
mv /xpedx/sterling/Foundation/external_deployments/smcfs.ear $SMCFSDEPLOYMENTHOME/smcfs.ear.new
#cd $XPDEXHOME/wldomain/xpdev
#./stopsmcfsdev.sh
# ML: remove the cache/tmp folders before starting the servers. This way, the deploy will not affect the site if it's being used. 
#cd /xpedx/wldomain/xpdev/servers/smcfsdev
#rm -R cache tmp
cd $SMCFSDEPLOYMENTHOME
mv smcfs.ear archive/smcfs.ear."${TIMESTAMP}"
mv smcfs.ear.new smcfs.ear
