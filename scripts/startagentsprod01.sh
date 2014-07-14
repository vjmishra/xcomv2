#!/bin/ksh
# This script starts the PROD SUPPORT agents. PLEASE READ BELOW!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
# WARNING!: DO NOT START ANY OF THE DATA LOAD INTEGRATION SERVERS BELOW FROM ANY BOX OTHER THAN THE XPAPPS01.ipaper.com SERVER
# All data loads must go against the "Golden-copy" database (NGPST1)
cd
cd /xpedx/sterling/Foundation/bin

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME

#Integration Server:
# SEE startagentsps.sh. MUST NOT START ANY DATALOAD AGENTS ON AGENT SERVERS OR PROD APP SERVERS.

echo $HOST_NAME
# Check that the host name is not the productionsupport box and it's the agent01 server, if so, then you can start the agents inside the if statement

if [[ "$HOST_NAME" = "zxpagnt01" ]];then
	chmod +x ./xpedxagentserver.sh
	chmod +x ./xpedxStartIntegrationServer.sh
	
	#Integration Server:
	#nohup ./xpedxStartIntegrationServer.sh port=1691 XpedxDataFeedMQServer> /xpedx/sterling/Foundation/logs/Item_`date +"%Y%m%d_%H%M"`.log 2>&1 &
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

	#This agent reprocesses orders
	nohup ./xpedxagentserver.sh port=1663 XPEDXAgentServer > /xpedx/sterling/Foundation/logs/Reprocess_`date +"%Y%m%d-%H%M"`.log 2>&1 & 
	
	nohup ./xpedxStartIntegrationServer.sh port=1698 XPXOrderConfirmationEmailServer > /xpedx/sterling/Foundation/logs/OrderConfirmationEmails_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	
	nohup ./xpedxStartIntegrationServer.sh port=1662 XPXLoadInvoiceServer > /xpedx/sterling/Foundation/logs/LoadInvoiceServer_`date +"%Y%m%d-%H%M"`.log 2>&1 & 
	sleep 10
	nohup ./startIntegrationServer.sh XPXOrderStatusEmailServer > /xpedx/sterling/Foundation/logs/XPXOrderStatusEmailServer_`date +"%Y%m%d-%H%M"`.log &
	
	#After the above Agent Server has started (Message that all Services for Agent have been successfully started) then run the following  Trigger Agent. 
	#pause for 5mins before triggering the invoiceupdate agent (per Winston Edwards)
	sleep 300
	./triggeragent.sh Invoice_Update 
	exit 0
	
else
	echo "WARNING!: You can only execute this script on the $HOST_NAME  (production agent server1) box. Be careful not to start agents on the wrong box. "
	exit 1
fi

