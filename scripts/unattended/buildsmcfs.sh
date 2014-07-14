#!ksh

ENVIRONMENT
XPDEXHOME=/xpedx
SMCFSBUILDHOME=/xpedx/sci_build/smcfs
DATE=`date +%Y-%m-%d`
CURRENTZIPFILE="smcfs-${DATE}.zip"

#location of the zip files used for the build is specified below.
ZIPFILEHOME=/home/share/xpadmin/buildsource

cd $SMCFSBUILDHOME
cp $ZIPFILEHOME/$CURRENTZIPFILE .
rm -R $SMCFSBUILDHOME/smcfs
unzip $CURRENTZIPFILE 
if [ "$?" != "0" ]; then
	echo "Could not find/unzip $CURRENTZIPFILE zip file. Aborting the build!" 1>&2
	exit 1
fi
#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 *);;
esac

# Xpedxsmcfsbuild.xml is the ANT build script. This file is in CVS and is not automatically copied from CVS during each build. 
# Need to copy it so that any changes to it will be reflected in the build

cp /xpedx/sci_build/smcfs/smcfs/dev/build/Xpedxsmcfsbuild.xml $SMCFSBUILDHOME
if [ "$?" != "0" ]; then
	echo "Could not copy Xpedxsmcfsbuild.xml to the build directory!" 1>&2
	exit 1
fi


cp /xpedx/sci_build/smcfs/smcfs/dev/build/Xpedxsmcfsbuild.properties $SMCFSBUILDHOME
if [ "$?" != "0" ]; then
	echo "Could not copy Xpedxsmcfsbuild.properties to the build directory. Exiting the build process!" 1>&2
	exit 1
fi

# execute the build script using sci_ant
/xpedx/sterling/Foundation/bin/sci_ant.sh -f Xpedxsmcfsbuild.xml -Dbuild.env="$ENVIRONMENT" smcfsBuildWithoutCVSFetch -logfile /xpedx/sci_build/smcfs/smcfsBuild-`date +"%Y%m%d-%H%M"`.log &


