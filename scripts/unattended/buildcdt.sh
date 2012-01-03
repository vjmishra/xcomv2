#!ksh
# This script is used to kickstart the CDT build on any environment. The hostname is extracted dynamically and then fed in the correct target for the xpedxCDTBuild.xml ant script.
# 8/1/2011 - Mahmoud Lamriben

ENVIRONMENT
CDTBUILDHOME=/xpedx/sci_build/CDT/
DATE=`date +%Y-%m-%d` 
 
#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=stage;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 *);;
esac

# change to the directory where xpedxCDTBuild.xml resides
cd $CDTBUILDHOME
# execute the build
/xpedx/sterling/Foundation/bin/sci_ant.sh -f xpedxCDTBuild.xml cdt_"$ENVIRONMENT"_import -logfile /xpedx/sci_build/CDT/cdtImport-`date +"%Y%m%d-%H%M"`.log &  
