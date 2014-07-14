#!ksh
# This script is used to kickstart the CDT build on any environment. The hostname is extracted dynamically and then fed in the correct target for the xpedxCDTBuild.xml ant script.
# 8/1/2011 - Mahmoud Lamriben

#ENVIRONMENT
#location of the xml files used for the CDT build is specified below.
CDTSOURCEDIR=/xpedx/sci_build/smcfs/smcfs/dev/dbdump/cdt/XpedxMasterConfig
#CURRENTCDTZIPFILE="XpedxMasterConfig-${DATE}.zip"
CDTBUILDHOME=/xpedx/sci_build/CDT/
CDTIMPORTDIR=/xpedx/sci_build/CDT/import/
CDTARCHIVEDIR=/xpedx/sci_build/CDT/archive/
#make the logs directory if not there already. 
mkdir /xpedx/sci_build/CDT/logs/
chmod 774 /xpedx/sci_build/CDT/logs/

DATE=`date +%Y-%m-%d`
DATETIME=`date +"%Y-%m-%d-%H%M"` 
#Check which server 
HOST_NAME="$(hostname)"
echo $HOST_NAME

#NOTE: MUST CHANGE dev to prod for xpagnt01 once the testing has been completed
case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=sandbox1;;
 zxpappint01) ENVIRONMENT=integration;;
 zxpappt01) ENVIRONMENT=stage;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=masterconfig;;
 *);;
esac

mkdir $CDTARCHIVEDIR
#Before the CDT build, backup/archive the current CDT

cd $CDTBUILDHOME
TMPZIPFILE=XpedxMasterConfig-${DATETIME}.zip
zip -r $TMPZIPFILE import
mv $TMPZIPFILE $CDTARCHIVEDIR

#now copy the source CDT file over to the destination 
cp $CDTSOURCEDIR/*.xml  $CDTIMPORTDIR

# change to the directory where xpedxCDTBuild.xml resides
cd $CDTBUILDHOME
# execute the build

LOGFILE=/xpedx/sci_build/CDT/logs/cdtImport-`date +"%Y%m%d-%H%M"`.log
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
