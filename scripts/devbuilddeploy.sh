
HOST_NAME="$(hostname)"
echo $HOST_NAME


if [[ "$HOST_NAME" = "zxpappd01" ]];then

#ksh -x ./devbuildinitiatenotification.sh
#sleep 300

#ksh -x ./selectAgentprocessID.sh

#ksh -x ./devkillAgentproceses.sh

ksh -x ./checkoutsmcfslatestcode.sh

ksh -x ./buildsmcfs-nocopy.sh

ksh -x ./checkoutswclatestcode.sh

ksh -x ./buildswc-nocopy.sh

#wait 30  to 40 minuts during start/deployment of smcfs and swc with xpadmin user

ksh -x ./entityupdatedb.sh

#sleep 1200

#ksh -x ./startagents.sh

exit 0
	
	
else
	echo "WARNING!: You can only execute this script on the $HOST_NAME (DEV) server. Be careful where you start build. "	
	exit 1
fi




