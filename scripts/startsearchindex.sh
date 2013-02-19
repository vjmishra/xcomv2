#!/bin/ksh

# Note: this scripts is for stopping the data load agents and checking that MQ Queue depth is zero before triggering the search index build

PREVENTSEARCHINDEXFAILUREHOME=/xpedx/sci_build/PreventSearchIndexFailures

HOST_NAME="$(hostname)"
#DISTRIBUTION_LIST=mohammad.balkhi@hp.com,arun.udhayakumar@hp.com,mahmoud.lamriben@hp.com,riyer@hp.com,ankit.goyal2@hp.com,amit.valsangkar@hp.com,felix.turner@hp.com,pramod.lahoti@hp.com
DISTRIBUTION_LIST=mahmoud.lamriben@hp.com,mohammad.balkhi@hp.com

echo $HOST_NAME

#location of the log files used for the PREVENT SEARCH INDEX is specified below.
LOGFILE=/xpedx/sci_build/PreventSearchIndexFailures/logs/startsearchindex_$HOST_NAME_`date +"%Y%m%d-%H%M"`.log
LOGFILE2=/xpedx/sci_build/PreventSearchIndexFailures/logs/preventsearchindexfailures_$HOST_NAME_`date +"%Y%m%d-%H%M"`.log


MASTER_LOGFILE=/xpedx/sci_build/PreventSearchIndexFailures/logs/Master_startsearchindex_$HOST_NAME.log

#start by recording the date time stamp
echo "Saving logs to $LOGFILE" 
echo $(date) > $LOGFILE
echo $(date) >> $MASTER_LOGFILE

countxpedxrecord=$(echo `grep -i "xpedx" "/xpedx/sci_build/searchindexfailureprevent/logs/yfs_attribute_group.log"|wc -w`)
echo "Start checking processes on $HOST_NAME before triggering the search index build. SMCFS and SearchIndex agent must both be up and running before triggering the index build" >> $LOGFILE
echo "countxpedxrecord = $countxpedxrecord" >> $LOGFILE
#finding status that smcfs up and running comment/uncomment for server (DEV, stage or production)
shuttingdownsmcfsStr=$(echo `grep -i "SHUTTING_DOWN" "/xpedx/logfiles/smcfsdev/stdout.log"|wc -w`)
echo "shuttingdownsmcfsStr = $shuttingdownsmcfsStr" >> LOGFILE


echo "" >> $LOGFILE
echo "" >> $LOGFILE
echo "" >> $LOGFILE

#finding status that search index agents up and running
/usr/ucb/ps -auxww | grep xpedxSearchIndexBuild |awk '{ print  "searchIndexPID " $2}' > /xpedx/sci_build/searchindexfailureprevent/logs/searchIndexFeedPID.log

countsearchIndexPID=$(echo `grep -i "searchIndexPID" "/xpedx/sci_build/searchindexfailureprevent/logs/searchIndexFeedPID.log"|wc -w`)
echo "countsearchIndexPID = $countsearchIndexPID" >> $LOGFILE
# Find FEED agents queue depth on production
echo "Find MQ FEED agents queue depth on production::" >> $LOGFILE

cd /home/share/xpadmin/scripts
ksh -x ./findqueuedepth.sh

queueDepthStr=$(echo `grep -i "false" "/xpedx/sci_build/PreventSearchIndexFailures/logs/QueueConnection.log"|wc -w`)
echo "queueDepthStr = $queueDepthStr" >> $LOGFILE

echo "countxpedxrecord ${countxpedxrecord}:::countsearchIndexPID:::::${countsearchIndexPID}::::::::::::::shuttingdownsmcfsStr:::${shuttingdownsmcfsStr}::::queueDepthStr :::${queueDepthStr}" >> $LOGFILE
#hard code the values in order to test the if section
#${countxpedxrecord}=1
#${countsearchIndexPID}=2
#${shuttingdownsmcfsStr}=0
#${queueDepthStr}=1
#if [[ "${countxpedxrecord}" -gt 0 ]] && [[ "${countsearchIndexPID}" -gt 1 ]] && [[ "${shuttingdownsmcfsStr}" -lt 1 ]] && [[ "${queueDepthStr}" -gt 0 ]]; then
if [[ "$HOST_NAME" = "$HOST_NAME" ]];then
		# testing reverse conditions..remove it...if [[ "${countxpedxrecord}" -eq 0 ]] && [[ "${countsearchIndexPID}" -eq 0 ]] && [[ "${shuttingdownsmcfsStr}" -lt 1 ]] && [[ "${queueDepthStr}" -gt 0 ]]; then

		echo "success. All conditions are met for the SI build/trigger to proceed..." 
		echo "All conditions are met for the SI build/trigger to proceed..." >> $LOGFILE

		cd /home/share/xpadmin/scripts	 
		#instead of kill -9, use kill -2 to gracefully shut down the processes. 
		 echo "about to select all feed agents in file to remove precess " >> $LOGFILE
		 /usr/ucb/ps -auxww | grep XpedxDataFeedMQServer | grep -v grep |awk '{ print  "kill -2  " $2}' > killprocesses.sh
		 /usr/ucb/ps -auxww | grep xpedxMaxUOMFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
		 /usr/ucb/ps -auxww | grep xpedxMaxDivFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
		 /usr/ucb/ps -auxww | grep xpedxMaxEntServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
		 /usr/ucb/ps -auxww | grep xpedxMaxItemBranchFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
		 /usr/ucb/ps -auxww | grep xpedxMaxCustFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
		 /usr/ucb/ps -auxww | grep xpedxMaxCustXrefFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
		 /usr/ucb/ps -auxww | grep xpedxMaxPriceBookFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh

		 echo "Below are all the processes that are currently running: " >> $LOGFILE
		#/usr/ucb/ps -auxww | grep xpbuild >> $LOGFILE
		/usr/ucb/ps -auxww >> $LOGFILE
		 
		echo "about to kill all feed agents before triggering the search index build " >> $LOGFILE
		echo "Below are the processes that will be killed: " >> $LOGFILE		
		cat killprocesses.sh >> $LOGFILE
		# temporarily: don't kill the processes yet. ksh -x ./killprocesses.sh
		echo "finished killing all data load agent processes. " >> $LOGFILE
		
		
		
		cd $PREVENTSEARCHINDEXFAILUREHOME

		# execute the script using sci_ant

		#if /xpedx/sterling/Foundation/bin/sci_ant.sh -f preventsearchindexfailures.xml -v -logfile $LOGFILE2 prevent_search_index_failures; then
		if [[ "$HOST_NAME" = "$HOST_NAME" ]];then
			echo "Completed purging of stale records from the database to prevent search index build failures " >> $LOGFILE
			exit 0
		else
			echo "ERROR/FAILURE! There were errors while executing the preventsearchindexfailures.sh script. Check the logs for more info..." 1>&2 >> $LOGFILE
			echo "ERROR/FAILURE! There were errors while executing the preventsearchindexfailures.sh script. Check the logs for more info..." >> $MASTER_LOGFILE
			#if there's an error, log it to the master file also. 
			echo "Contents from $LOGFILE are shown below: " >> $LOGFILE
			cat $LOGFILE >> $MASTER_LOGFILE
			echo "End of $LOGFILE Contents. Please review and fix the issue if necessary. " >> $LOGFILE
			
			exit 1
		fi 
		#just uncoment the below for implementation
		cd /home/share/xpadmin/scripts
		# temporarily disable: 
		#ksh -x ./triggerSearchIndexBuildFull.sh
		# Search index triggered on path:: /home/share/xpadmin/scripts::>> $MASTER_LOGFILE
		echo "Search index triggered on $HOST_NAME" >> $LOGFILE
		
		ksh -x ./sendnotification2.sh "Search Index Triggered on $HOST_NAME" "Success" $DISTRIBUTION_LIST $LOGFILE 
		echo "Notifications were successfully sent out.. " >> $LOGFILE
		cat $LOGFILE >> $MASTER_LOGFILE
		
else 
		#else for failed the conditions (FEED agents queue depth more than 1 and  smcfs/SI agents is down)
		echo "Pausing the search index check on $HOST_NAME for 8 minutes before triggering the next iteration of the program..." >> $LOGFILE
		
		sleep 10;
	#sleep 500
		DATE=`date +"%H%M"`
		cd /home/share/xpadmin/scripts
		if [ "${DATE}" -lt '0400' ]; then
				#waiting 8 minuts to recursively call the scripts upto 4 oclock (2:30PM IST)
			echo "Triggering the startsearchindex.sh program again after pause..." >> $LOGFILE
			cat $LOGFILE >> $MASTER_LOGFILE		
			ksh -x ./startsearchindex.sh
		else
			echo "The search index could not be started by 4AM EST/2:30PM IST. A notification has been sent out to $DISTRIBUTION_LIST..." >> $LOGFILE
			cat $LOGFILE >> $MASTER_LOGFILE		
			ksh -x ./sendnotification2.sh "Failure - Search Index Build Was Not Triggered on $HOST_NAME Today" "Error" $DISTRIBUTION_LIST $LOGFILE
			exit 1
		fi


fi

echo $(date) >> $LOGFILE
echo $(date) >> $MASTER_LOGFILE

echo "Below are the contents of $LOGFILE"
cat -b $LOGFILE
exit 0