#!/bin/ksh
XPDEXHOME=/xpedx
# Note: this scripts is for purging cent logs from database

PURGEDATABASEHOME=/xpedx/sci_build/PurgeCentLogRecords
DATE=`date +%Y-%m-%d`

#location of the log files used for the purge is specified below.

LOGFILE=/xpedx/sci_build/PurgeCentLogRecords/logs/purgecentlogdb-`date +"%Y%m%d-%H%M"`.log

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

        
cd $PURGEDATABASEHOME
echo "about to PURGE database record code using purgecentlogdb.sh script " >> $LOGFILE
echo "Saving logs to $LOGFILE" >> $LOGFILE 
echo "Script executing on: $HOST_NAME..." >> $LOGFILE 


# execute the purging script using sci_ant
#set -x
#Par1- Delete the centlog records from the database
if /xpedx/sterling/Foundation/bin/sci_ant.sh -f purgecentlogdb.xml -v -logfile $LOGFILE purgecentlog; then
#Par2- Delete the centlog records from the physical drive	
	echo "about to delete old centlog physical files which are older than 60 days..." >> $LOGFILE 
	echo "Below are the files which are older than 60 days and will be deleted..." >> $LOGFILE 
	find /xpedx/logfiles/cent -type f -name "centLog*" -mtime +60 -print >> $LOGFILE
	find /xpedx/logfiles/cent -type f -name "centLog*" -mtime +60 -exec rm -R {} \;
	echo "Completed purging of cent log files older than 60 days..." >> $LOGFILE 
	#set +x
	exit 0
else
	echo "ERROR/FAILURE! There were errors. Check logs..." 1>&2 >> $LOGFILE
	cat -b $LOGFILE | more
	exit 1
fi
 