#ksh
XPDEXHOME=/xpedx
SWCBUILDHOME=/xpedx/sci_build/WebChannel
DATE=`date +%Y-%m-%d`
CURRENTZIPFILE="WebChannel-${DATE}.zip"
ZIPFILEHOME=/home/share/xpadmin/buildsource

cd $SWCBUILDHOME
cp $ZIPFILEHOME/$CURRENTZIPFILE .
cd /xpedx/sci_build/WebChannel/WebChannel/
chmod -R +w *

cd $SWCBUILDHOME
# make sure user has write perm before unzipping or will get warnings for each read-only file.
chmod -R +w /xpedx/sci_build/WebChannel/WebChannel
rm -R /xpedx/sci_build/WebChannel/WebChannel 
unzip $CURRENTZIPFILE 
if [ "$?" != "0" ]; then
	echo "Could not find/unzip $CURRENTZIPFILE zip file. Aborting the build!" 1>&2
	exit 1
fi
