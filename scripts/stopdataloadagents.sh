#!/bin/ksh
#findqueuedepthprod2.sh 

HOST_NAME="$(hostname)"
echo "Stopping data load agents on $HOST_NAME"

echo "about to select all feed agents on other server (xpagnt02)in file to remove precess " >> $MASTER_LOGFILE
 /usr/ucb/ps -auxww | grep xpedxMaxUOMFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
 /usr/ucb/ps -auxww | grep xpedxMaxDivFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
 /usr/ucb/ps -auxww | grep xpedxMaxEntServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
 /usr/ucb/ps -auxww | grep xpedxMaxItemBranchFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
 /usr/ucb/ps -auxww | grep xpedxMaxCustFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
 /usr/ucb/ps -auxww | grep xpedxMaxCustXrefFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
 /usr/ucb/ps -auxww | grep xpedxMaxPriceBookFeedServer | grep -v grep |awk '{ print  "kill -2  " $2}' >> killprocesses.sh
 
 
 echo "about to stop all data load agents  " >> $MASTER_LOGFILE
ksh -x ./killprocesses.sh
exit 0

