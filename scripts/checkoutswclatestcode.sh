#!/bin/ksh
XPDEXHOME=/xpedx
# Note: even though this script is for checking out webchannel code, since the checkout task is in the same file as smcfs, we're keeping the directory pointed to SMCFSBUILDHOME.
SMCFSBUILDHOME=/xpedx/sci_build/smcfs
DATE=`date +%Y-%m-%d`

#location of the zip files used for the build is specified below.
LOGFILE=/xpedx/sci_build/smcfs/BorlandSWCCheckout-`date +"%Y%m%d-%H%M"`.log

cd $SMCFSBUILDHOME
#change protection so we can delete all readonly files without getting prompted.

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

  
         

cd $SMCFSBUILDHOME
echo "about to checkout smcfs code using checkoutswclatestcode.sh script " >> $LOGFILE
echo "Saving logs to $LOGFILE" 

# execute the checkout script using sci_ant
# pass CheckoutSWC target to get just smcfs
/xpedx/sterling/Foundation/bin/sci_ant.sh -f CheckoutBorlandLatestCode.xml -v -logfile $LOGFILE CheckoutSWC ;
#show log file immediately after. 
#sleep 4000
#tail -300 $LOGFILE
exit 0

	


 

