#!/bin/ksh

ENVIRONMENT
XPDEXHOME=/xpedx
SMCFSBUILDHOME=/xpedx/sci_build/smcfs
DATE=`date +%Y-%m-%d`
#location of the zip files used for the build is specified below.
ZIPFILEHOME=/home/share/xpadmin/buildsource
mkdir /xpedx/sci_build/smcfs/logs/
chmod 774 /xpedx/sci_build/smcfs/logs/
LOGFILE=/xpedx/sci_build/smcfs/logs/smcfsBuild-`date +"%Y%m%d-%H%M"`.log

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties
echo "SMCFS build started on $HOST_NAME" > $LOGFILE
echo "Build Start TimeStamp: " `date` >> $LOGFILE


# note: we are setting MasterConfig as a dev server so that it's property files is retrieved from dev. otherwise the build will fail because the ant script expects, dev, staging, prod and prodsupport
#zxpaappmc01) ENVIRONMENT=dev;; 
case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=sandbox1;;
 zxpappint01) ENVIRONMENT=integration;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=dev;; 
 *);;
esac

# temporarily look for a different smcfs zip file name for the sandbox while we're testing agile. 
if [ "$HOST_NAME" = "zxpappd02" ]; then
	CURRENTZIPFILE="smcfs-sandbox-${DATE}.zip"
else
	CURRENTZIPFILE="smcfs-${DATE}.zip"
fi




cd $SMCFSBUILDHOME
cp $ZIPFILEHOME/$CURRENTZIPFILE .
#change protection so we can delete all readonly files without getting prompted.
chmod -R +w *
echo "Using $CURRENTZIPFILE for the build: "  >> $LOGFILE
ls -lt $CURRENTZIPFILE >> $LOGFILE

#backup the smcfs directory first. 
cd /home/share/xpadmin/scripts/
ksh -x backupsmcfs.sh
echo "Finished backing up smcfs directory"
cd $SMCFSBUILDHOME
#delete the current smcfs folder to recreate it fresh
rm -R $SMCFSBUILDHOME/smcfs
unzip $CURRENTZIPFILE 
echo "Finished unzipping $CURRENTZIPFILE"
#change protection so we can delete all readonly files without getting prompted.
chmod -R +w *

if [ "$?" != "0" ]; then
	echo "Could not find/unzip $CURRENTZIPFILE zip file. Aborting the build!" 1>&2
	exit 1
fi

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

#before triggering the build, stop the order updates coming from the legacy/WebMethods
echo "about to stop Order Updates " >> $LOGFILE
cd /home/share/xpadmin/scripts/
ksh -x stoporderupdates.sh
echo "completed stoping of Order Updates " >> $LOGFILE

cd $SMCFSBUILDHOME
echo "about to execute Xpedxsmcfsbuild.xml ant script " >> $LOGFILE
echo "about to execute Xpedxsmcfsbuild.xml ant script. Saving logs to $LOGFILE" 

# execute the build script using sci_ant
#ML: Don't send the process to the background (i.e remove & at the end of the below command) so that we can chain the commands and wait for execution of the build before triggering the next command.
if nohup /xpedx/sterling/Foundation/bin/sci_ant.sh -f Xpedxsmcfsbuild.xml -Dbuild.env="$ENVIRONMENT" smcfsBuildWithoutCVSFetch -logfile $LOGFILE; then			
	#show log file immediately after. 
	echo "Press ctrl+c to exit out of the log file..." >> $LOGFILE
	#sleep 5
	echo "tail -f $LOGFILE"

	echo "SUCCESS! Completed the build without errors. Continuing with the deployment" >> $LOGFILE
	echo "Moving smcfs.ear to /xpedx/deployments" >> $LOGFILE
	#Deploy the ear file so that it's not overriden by the next swc build
	cd /home/share/xpadmin/scripts/
	ksh -x deploysmcfs.sh >> $LOGFILE
	#ftp the call center zip file to the common share viewable by the BIMs.
	if [ "$HOST_NAME" != "zxpappmc01" ]; then
		ksh -x copycallcentercomzip.sh >> $LOGFILE
	fi
	./sendnotification.sh SMCFS Success mahmoud.lamriben@hp.com,balkhi.mohammad@hp.com $LOGFILE
	echo "SMCFS Build Completion TimeStamp: " `date` >> $LOGFILE
	exit 0
else
	echo "ERROR/FAILURE! There were errors in the build. Check logs..." 1>&2 >> $LOGFILE
	echo "SMCFS Build With Errors Completion TimeStamp: " `date` >> $LOGFILE
	./sendnotification.sh SMCFS Error mahmoud.lamriben@hp.com,mohammad.balkhi@hp.com $LOGFILE
	exit 1
fi


 

