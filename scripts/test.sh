#!ksh
# This script starts the agents

cd
cd /xpedx/sterling/Foundation/bin

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME

if [ "$HOST_NAME" = "zxpappd01" ]
then 
	echo "you are on the $HOST_NAME dev box"
else
	echo "You can only execute this script on the $HOST_NAME (dev) server. Be careful where you start agents. "	
fi
