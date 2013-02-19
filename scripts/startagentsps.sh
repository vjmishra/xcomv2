#!/bin/ksh
# This script starts the agents

cd
cd /xpedx/sterling/Foundation/bin

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME

if [[ "$HOST_NAME" = "zxpapps01" ]];then
	#chmod +x ./xpedxagentserver.sh
	#chmod +x ./xpedxStartIntegrationServer.sh

	#xpedxStartIntegrationServer.sh  is the same as  startIntegrationServer.sh except it adds a new memory parameters to specify the size of the jvm.
	# use xpedxstartIntegrationServer instead of  startIntegrationServer
	#This script is executed the same way as startIntegrationServer.sh so just change your startup command (or command in your script) to use the new script. 
	#The only change that I have made to this file is to add Memory Parameters to the startup process

	#Start MQ Dataload agent
	#nohup ./xpedxStartIntegrationServer.sh port=1655 XpedxDataFeedMQServer > /xpedx/sterling/Foundation/logs/Item_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./startHealthMonitor.sh 1 > /xpedx/sterling/Foundation/logs/HealthMonitor_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./xpedxagentserver.sh port=1656 XPXResetPasswordServer > /xpedx/sterling/Foundation/logs/ResetPwd_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	# start the order approval emails agent
	nohup ./xpedxStartIntegrationServer.sh port=1659 OrderApprovalEmailsServer > /xpedx/sterling/Foundation/logs/OrderApprovalEmails_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	#Orderconfirmation agents
    nohup ./xpedxStartIntegrationServer.sh port=1698 XPXOrderConfirmationEmailServer > /xpedx/sterling/Foundation/logs/OrderConfirmationEmails_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	#Integration Server:
	#nohup ./xpedxStartIntegrationServer.sh xpedxMaxUOMFeedServer> ../logs/MaxUOM_`date +"%Y%m%d_%H%M"`.log 2>&1 &  
	#nohup ./xpedxStartIntegrationServer.sh xpedxMaxDivFeedServer > ../logs/MaxDiv_`date +"%Y%m%d_%H%M"`.log 2>&1 &
	#nohup ./xpedxStartIntegrationServer.sh xpedxMaxPriceBookFeedServer> ../logs/Pricebook_`date +"%Y%m%d_%H%M"`.log 2>&1 &
	#nohup ./xpedxStartIntegrationServer.sh xpedxMaxEntServer > ../logs/MaxEnt_`date +"%Y%m%d_%H%M"`.log 2>&1 &
	#nohup ./xpedxStartIntegrationServer.sh xpedxMaxItemBranchFeedServer> ../logs/MaxItemBranch_`date +"%Y%m%d_%H%M"`.log 2>&1 &
	#nohup ./xpedxStartIntegrationServer.sh xpedxMaxCustFeedServer> ../logs/MaxCust_`date +"%Y%m%d_%H%M"`.log 2>&1 &

	#Catalog index build. Note: We're starting 2 JVMs/instances of this agent. It was a recommendation of the Sterling Engineering team for performance reasons. 
	#Note: If catalog builds need to be enabled in dev, just uncomment the below one or two lines. 
	nohup ./xpedxSearchIndexagentserver.sh port=1660 xpedxSearchIndexBuild > /xpedx/sterling/Foundation/logs/SIB_`date +"%Y%m%d-%H%M"`_J1.log 2>&1 &
	nohup ./xpedxSearchIndexagentserver.sh port=1661 xpedxSearchIndexBuild > /xpedx/sterling/Foundation/logs/SIB_`date +"%Y%m%d-%H%M"`_J2.log 2>&1 & 

	exit 0
else
	echo "WARNING!: You can only execute this script on the $HOST_NAME server. Be careful where you start agents. "	
	exit 1
fi
