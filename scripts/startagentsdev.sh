#!/bin/ksh
# This script starts the agents

cd
cd /xpedx/sterling/Foundation/bin

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME

# WARNING: YOU MUST CHOOSE DIFFERENT JMX PORTS FOR EACH AGENT STARTED USING THIS SCRIPT BELOW. 
if [[ "$HOST_NAME" = "zxpappd01" ]];then
	echo "time: "`date +"%Y%m%d-%H%M"`
	chmod +x ./xpedxagentserver.sh
	chmod +x ./xpedxStartIntegrationServer.sh
	
	#Integration Server:
	#nohup ./startIntegrationServer.sh port=1655 XpedxDataFeedMQServer > /xpedx/sterling/Foundation/logs/Item_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	#Password Server:
	nohup ./xpedxagentserver.sh port=1656 XPXResetPasswordServer > /xpedx/sterling/Foundation/logs/ResetPwd_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	#Health Monitor:
	nohup ./startHealthMonitor.sh 1 > /xpedx/sterling/Foundation/logs/HealthMonitor_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	#Update_Invoice Agent: 
	nohup ./xpedxagentserver.sh port=1657 XPXInvoiceAgentServer > /xpedx/sterling/Foundation/logs/InvoiceUpdate_`date +"%Y%m%d-%H%M"`.log 2>&1 &

	# start the order approval emails agent
	nohup ./xpedxStartIntegrationServer.sh port=1659 OrderApprovalEmailsServer > /xpedx/sterling/Foundation/logs/OrderApprovalEmails_`date +"%Y%m%d-%H%M"`.log 2>&1 &

	#Catalog index build. Note: We're starting 2 JVMs/instances of this agent. It was a recommendation of the Sterling Engineering team for performance reasons. 
	#Note: If catalog builds need to be enabled in dev, just uncomment the below one or two lines. 
	nohup ./xpedxSearchIndexagentserver.sh port=1660 xpedxSearchIndexBuild > /xpedx/sterling/Foundation/logs/SIB_`date +"%Y%m%d-%H%M"`_J1.log 2>&1 &
	nohup ./xpedxSearchIndexagentserver.sh port=1661 xpedxSearchIndexBuild > /xpedx/sterling/Foundation/logs/SIB_`date +"%Y%m%d-%H%M"`_J2.log 2>&1 & 


	nohup ./xpedxStartIntegrationServer.sh port=1662 XPXLoadInvoiceServer > /xpedx/sterling/Foundation/logs/LoadInvoiceServer_`date +"%Y%m%d-%H%M"`.log 2>&1 & 
	#After the above Agent Server has started (Message that all Services for Agent have been successfully started) then run the following  Trigger Agent. 
	#pause for 5mins before triggering the invoiceupdate agent (per Winston Edwards)
	sleep 300
	./triggeragent.sh Invoice_Update 
	exit 0
else
	echo "You can only execute this script on the $HOST_NAME (dev) server. Be careful where you start agents. "	
	exit 1
fi
