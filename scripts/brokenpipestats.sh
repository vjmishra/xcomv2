#!/bin/ksh

ENVIRONMENT
XPDEXHOME=/xpedx
DATE=`date +%Y-%m-%d`

   
HOST_NAME="$(hostname)"
echo $HOST_NAME


case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=dev;;  
 *);;
esac

case "$HOST_NAME" in
 zxpappd01) ENVSUFFIX=dev;;
 zxpappd02) ENVSUFFIX=dev;;
 zxpappt01) ENVSUFFIX=stg;; 
 zxpapps01) ENVSUFFIX=ps;; 
 zxpagnt01) ENVSUFFIX=;; 
 zxpagnt01) ENVSUFFIX=;; 
 zxpapp01) ENVSUFFIX=01;; 
 zxpapp02) ENVSUFFIX=02;; 
 zxpappmc01) ENVSUFFIX=dev;;  
 *);;
esac

LOGFILE=/xpedx/logfiles/broken-pipe-stats-$ENVIRONMENT_`date +"%Y%m%d-%H%M"`.log
 
#echo "started checking for broken pipe counts on $HOST_NAME ... " >> $LOGFILE
 
#first find broken pipe errors in the Foundation/logs directory
find /xpedx/sterling/Foundation/logs/ -type f -exec grep -il "Broken pipe" {} \; -exec  grep -cw "Broken pipe" {} \; >> $LOGFILE
#find /xpedx/sterling/Foundation/logs/ -type f -exec grep -il "DEBUG SMTP: QUIT failed with 451" {} \; -exec  grep -cw "DEBUG SMTP: QUIT failed with 451" {} \; >> $LOGFILE
#next find errors in swc stdout.log
find /xpedx/logfiles/swc$ENVSUFFIX/stdout.log -type f -exec grep -il "Broken pipe" {} \; -exec  grep -cw "Broken pipe" {} \; >> $LOGFILE
find /xpedx/logfiles/swc$ENVSUFFIX/ -type f -exec grep -il "Broken pipe" {} \; -exec  grep -cw "Broken pipe" {} \; >> $LOGFILE
#lastly find errors in smcfs stdout.log
find /xpedx/logfiles/smcfs$ENVSUFFIX/ -type f -exec grep -il "Broken pipe" {} \; -exec  grep -cw "Broken pipe" {} \; >> $LOGFILE

echo "Finished searching for Broken pipe errors on $HOST_NAME " >> $LOGFILE 
echo "" >> $LOGFILE 

ksh -x /home/share/xpadmin/scripts/sendnotification2.sh "Broken pipe" Success mahmoud.lamriben@hp.com,amit.valsangkar@hp.com,riyer@hp.com,ankit.goyal2@hp.com $LOGFILE
exit 0
