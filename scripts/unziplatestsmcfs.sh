#!/bin/ksh

ENVIRONMENT
XPDEXHOME=/xpedx
SMCFSBUILDHOME=/xpedx/sci_build/smcfs
DATE=`date +%Y-%m-%d`
CURRENTZIPFILE="smcfs-${DATE}.zip"

#location of the zip files used for the build is specified below.
ZIPFILEHOME=/home/share/xpadmin/buildsource
LOGFILE=/xpedx/sci_build/smcfs/smcfsBuild-`date +"%Y%m%d-%H%M"`.log

cd $SMCFSBUILDHOME
cp $ZIPFILEHOME/$CURRENTZIPFILE .
rm -R $SMCFSBUILDHOME/smcfs
unzip $CURRENTZIPFILE 
if [ "$?" != "0" ]; then
	echo "Could not find/unzip $CURRENTZIPFILE zip file. Aborting the build!" 1>&2
	exit 1
fi
    
echo "Finished unzipping $CURRENTZIPFILE"
	