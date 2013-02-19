#!ksh
# This script is used to kickstart the CDT build on any environment. The hostname is extracted dynamically and then fed in the correct target for the xpedxCDTBuild.xml ant script.
# 8/1/2011 - Mahmoud Lamriben

#ENVIRONMENT
#location of the zip files used for the build is specified below.
BUILDSOURCEHOME=/home/share/xpadmin/buildsource
CURRENTCDTZIPFILE="XpedxMasterConfig-${DATE}.zip"
CDTBUILDHOME=/xpedx/sci_build/CDT/
CDTZIPDIR=/xpedx/sci_build/CDT/zipdir
CDTARCHIVEDIR=/xpedx/sci_build/CDT/archive
DATE=`date +%Y-%m-%d` 
 
#Check which server 
HOST_NAME="$(hostname)"
echo $HOST_NAME

#NOTE: MUST CHANGE dev to prod for xpagnt01 once the testing has been completed
case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=stage;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=masterconfig;; 
 
 *);;
esac

mkdir $CDTARCHIVEDIR
#Before the CDT build, backup/archive the current CDT zip file on the destination server
cp $CDTZIPDIR/XpedxMasterConfig.zip  $CDTARCHIVEDIR/XpedxMasterConfig-`date +"%Y%m%d-%H%M"`.zip

#now copy the source CDT file over to the destination server
cp $BUILDSOURCEHOME/XpedxMasterConfig-${DATE}.zip  $CDTZIPDIR/XpedxMasterConfig.zip

# change to the directory where xpedxCDTBuild.xml resides
cd $CDTBUILDHOME
# execute the build
LOGFILE=/xpedx/sci_build/CDT/cdtImport-`date +"%Y%m%d-%H%M"`.log
#nohup /xpedx/sterling/Foundation/bin/sci_ant.sh -f xpedxCDTBuild.xml cdt_"$ENVIRONMENT"_import -logfile $LOGFILE &  
echo "WARNING: DO NOT press ctrl+c or the process will be stopped. You can tail the log file in a separate window..."

if nohup /xpedx/sterling/Foundation/bin/sci_ant.sh -f xpedxCDTBuild.xml cdt_"$ENVIRONMENT"_import -logfile $LOGFILE; then		
	./sendnotification.sh CDT Success mahmoud.lamriben@hp.com,balkhi.mohammad@hp.com $LOGFILE
	
	exit 0
else
	echo "ERROR/FAILURE! There were errors in the build. Check logs..." 1>&2 >> $LOGFILE
	./sendnotification.sh CDT Error mahmoud.lamriben@hp.com,mohammad.balkhi@hp.com $LOGFILE
	exit 1
fi

tail -20 $LOGFILE