#!/bin/ksh
# This script starts the search index agents only 

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME

#since dev moved to xpagnt01, I created a second script (copief from startagentsdev.sh) and changed the below condition.
echo "Please confirm that you want to trigger the incremental search index build on $HOST_NAME by typing the hostname again:\c"
read Answer

#compare host name to zone name (zones start with z. ex: zxpappd01 for dev)
if [[ "$HOST_NAME" = "z$Answer" ]];then
	cd
	cd /xpedx/sterling/Foundation/bin
		
	./triggeragent.sh INCR_CATALOG_INDEX_BUILD
	
	exit 0
else
	echo "Your answer didn't match the host name $HOST_NAME. Please try again."	
	exit 1
fi
