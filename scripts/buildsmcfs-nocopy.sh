#!ksh

ENVIRONMENT
XPDEXHOME=/xpedx
SMCFSBUILDHOME=/xpedx/sci_build/smcfs
DATE=`date +%Y-%m-%d`
CURRENTZIPFILE="smcfs-${DATE}.zip"

#location of the zip files used for the build is specified below.
ZIPFILEHOME=/home/share/xpadmin/buildsource
LOGFILE=/xpedx/sci_build/smcfs/smcfsBuild-`date +"%Y%m%d-%H%M"`.log

cd $SMCFSBUILDHOME
#cp $ZIPFILEHOME/$CURRENTZIPFILE .
#rm -R $SMCFSBUILDHOME/smcfs
#unzip $CURRENTZIPFILE 
#if [ "$?" != "0" ]; then
#	echo "Could not find/unzip $CURRENTZIPFILE zip file. Aborting the build!" 1>&2
#	exit 1
#fi
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
 zxpaappmc01) ENVIRONMENT=mc;; 
 *);;
esac

# Xpedxsmcfsbuild.xml is the ANT build script. This file is in CVS and is not automatically copied from CVS during each build. 
# Need to copy it so that any changes to it will be reflected in the build

cp /xpedx/sci_build/smcfs/smcfs/dev/build/Xpedxsmcfsbuild.xml $SMCFSBUILDHOME
if [ "$?" != "0" ]; then
	echo "Could not copy Xpedxsmcfsbuild.xml to the build directory!" 1>&2
	echo "Could not copy Xpedxsmcfsbuild.xml to the build directory!" 1>&2 >> $LOGFILE
	exit 1
fi

echo "about to copy /xpedx/sci_build/smcfs/smcfs/dev/build/Xpedxsmcfsbuild.properties to $SMCFSBUILDHOME" >> $LOGFILE
cp /xpedx/sci_build/smcfs/smcfs/dev/build/Xpedxsmcfsbuild.properties $SMCFSBUILDHOME
if [ "$?" != "0" ]; then
	echo "Could not copy Xpedxsmcfsbuild.properties to the build directory. Exiting the build process!" 1>&2
	echo "Could not copy Xpedxsmcfsbuild.properties to the build directory. Exiting the build process!" 1>&2 >> $LOGFILE
	exit 1
fi

#grant this script execute rights after the unzip.
echo "about to copy /xpedx/sci_build/smcfs/smcfs/dev/build/recur_dos2unix.sh to /xpedx/sci_build/smcfs/smcfs/dev/PCAExtensions" >> $LOGFILE
cp /xpedx/sci_build/smcfs/smcfs/dev/build/recur_dos2unix.sh /xpedx/sci_build/smcfs/smcfs/dev/PCAExtensions
cd /xpedx/sci_build/smcfs/smcfs/dev/PCAExtensions
chmod +x recur_dos2unix.sh

         

cd $SMCFSBUILDHOME
echo "about to execute Xpedxsmcfsbuild.xml ant script " >> $LOGFILE
echo "about to execute Xpedxsmcfsbuild.xml ant script. Saving logs to $LOGFILE" 

# execute the build script using sci_ant
#ML: Don't send the process to the background (i.e remove & at the end of the below command) so that we can chain the commands and wait for execution of the build before triggering the next command.
nohup /xpedx/sterling/Foundation/bin/sci_ant.sh -f Xpedxsmcfsbuild.xml -Dbuild.env="$ENVIRONMENT" smcfsBuildWithoutCVSFetch -logfile $LOGFILE &

	#show log file immediately after. 
	#echo "Press ctrl+c to exit out of the log file..." >> $LOGFILE
	#sleep 5
	tail -f $LOGFILE

	#echo "SUCCESS! Completed the build without errors. Continuing with the deployment" >> $LOGFILE
	#echo "Moving smcfs.ear to /xpedx/deployments" >> $LOGFILE
	#Deploy the ear file so that it's not overriden by the next swc build
	#cd /home/share/xpadmin/scripts/
	#ksh -x deploysmcfs.sh >> $LOGFILE
	#ftp the call center zip file to the common share viewable by the BIMs.
	#ksh -x copycallcentercomzip.sh >> $LOGFILE
	#exit 0
#else
#	echo "ERROR/FAILURE! There were errors in the build. Check logs..." 1>&2 >> $LOGFILE
#	exit 1
#fi


 

