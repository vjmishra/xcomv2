#!/bin/ksh
XPDEXHOME=/xpedx
# Note: this scripts is for purging search index from database

PREVENTSEARCHINDEXFAILUREHOME=/xpedx/sci_build/PreventUserLoginFailures

HOST_NAME="$(hostname)"
echo $HOST_NAME


case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpaappmc01) ENVIRONMENT=mc;; 
 *);;
esac

#location of the log files used for the PREVENT SEARCH INDEX is specified below.

LOGFILE=/xpedx/sci_build/PreventUserLoginFailures/logs/preventuserloginfailures_$HOST_NAME_`date +"%Y%m%d-%H%M"`.log
MASTER_LOGFILE=/xpedx/sci_build/PreventUserLoginFailures/logs/Master_preventuserloginfailures_$HOST_NAME.log
        
cd $PREVENTSEARCHINDEXFAILUREHOME
echo "about to delete login failure attempt records from the database in order to prevent account lockouts " >> $MASTER_LOGFILE
echo "Saving logs to $LOGFILE" 

# execute the script using sci_ant

if /xpedx/sterling/Foundation/bin/sci_ant.sh -f preventuserloginfailures.xml -v -logfile $LOGFILE ptlt_user_login_fail_purge; then
	echo "Completed purging records from the ptlt_user_login_fail table " >> $MASTER_LOGFILE
	exit 0
else
	echo "ERROR/FAILURE! There were errors while executing the preventuserloginfailures.sh script. Check the logs for more info..." 1>&2 >> $LOGFILE
	echo "ERROR/FAILURE! There were errors while executing the preventuserloginfailures.sh script. Check the logs for more info..." >> $MASTER_LOGFILE
	#if there's an error, log it to the master file also. 
	echo "Contents from $LOGFILE are shown below: " >> $MASTER_LOGFILE
	cat $LOGFILE >> $MASTER_LOGFILE
	echo "End of $LOGFILE Contents. Please review and fix the issue if necessary. " >> $MASTER_LOGFILE
	
	exit 1
fi