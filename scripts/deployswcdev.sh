#ksh
XPDEXHOME=/xpedx
SWCDEPLOYMENTHOME=$XPDEXHOME/deployments
EARFILEHOME=$XPDEXHOME/sterling/Foundation/external_deployments
DATE=`date +%Y-%m-%d`
TIMESTAMP=`date +"%Y%m%d-%H%M"`
CURRENTZIPFILE="WebChannel-${DATE}.zip"
ZIPFILEHOME=/home/share/xpadmin/buildsource

cd $SWCDEPLOYMENTHOME 
mv /xpedx/sterling/Foundation/external_deployments/swc.ear /xpedx/deployments/swc.ear.new
#cd $XPDEXHOME/wldomain/xpdev
#./stopswcdev.sh
# ML: remove the cache/tmp folders before starting the servers. This way, the deploy will not affect the site if it's being used. 
#cd /xpedx/wldomain/xpdev/servers/swcdev
#rm -R cache tmp
cd $SWCDEPLOYMENTHOME
mv $SWCDEPLOYMENTHOME/swc.ear $SWCDEPLOYMENTHOME/archive/swc.ear."${TIMESTAMP}"
mv $SWCDEPLOYMENTHOME/swc.ear.new $SWCDEPLOYMENTHOME/swc.ear
zip -u $SWCDEPLOYMENTHOME/swc.ear /xpedx/sci_build/smcfs/smcfs/dev/dist/Foundation/smcfs.jar
