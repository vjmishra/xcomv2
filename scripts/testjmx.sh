#!/bin/ksh
# This script starts the search index agents only 

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME

#since dev moved to xpagnt01, I created a second script (copief from startagentsdev.sh) and changed the below condition.
#echo "Please confirm that you want to start the search index agents on $HOST_NAME by typing the hostname again:\c"
#read Answer

#compare host name to zone name (zones start with z. ex: zxpappd01 for dev)
#if [[ "$HOST_NAME" = "z$Answer" ]];then
	cd
	cd /xpedx/sterling/Foundation/bin
	LogFile1=/xpedx/sterling/Foundation/logs/SIB_`date +"%Y%m%d-%H%M"`_J1.log
	LogFile2=/xpedx/sterling/Foundation/logs/SIB_`date +"%Y%m%d-%H%M"`_J2.log
	
	#Catalog index build. Note: We're starting 2 JVMs/instances of this agent. It was a recommendation of the Sterling Engineering team for performance reasons. 
	#Note: If catalog builds need to be enabled in dev, just uncomment the below one or two lines. 
	nohup ./xpedxSearchIndexagentserver.sh -port=1660 xpedxSearchIndexBuild > $LogFile1 2>&1 &
	nohup ./xpedxSearchIndexagentserver.sh -port=1661 xpedxSearchIndexBuild > $LogFile2 2>&1 &

	echo "tailing log file $LogFile1. "
	echo $LogFile1
	echo $LogFile2
	
	tail -f $LogFile1
	
	exit 0
#else
#	echo "Your answer didn't match the host name $HOST_NAME. Please try again."	
#	exit 1
#fi
