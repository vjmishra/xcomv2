#!/bin/ksh

 
cd
cd /xpedx/sterling/Foundation/bin

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME

if [[ "$HOST_NAME" = "zxpappd01" ]];then
	chmod +x ./xpedxStartIntegrationServer.sh
	#Integration MQ Feed Server:
	# Commented out on 11/11/2011 to allow the prodsupport box consume the attributes/items from the production queues. Need to re-enable once we go live.
	nohup ./xpedxStartIntegrationServer.sh port=1690 xpedxMaxUOMFeedServer> /xpedx/sterling/Foundation/logs/MaxUOM_`date +"%Y%m%d_%H%M"`.log 2>&1 &
	nohup ./xpedxStartIntegrationServer.sh port=1691 XpedxDataFeedMQServer> /xpedx/sterling/Foundation/logs/Item_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./xpedxStartIntegrationServer.sh port=1692 xpedxMaxDivFeedServer > /xpedx/sterling/Foundation/logs/MaxDiv_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./xpedxStartIntegrationServer.sh port=1693 xpedxMaxEntServer> /xpedx/sterling/Foundation/logs/MaxEntitlement_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./xpedxStartIntegrationServer.sh port=1694 xpedxMaxItemBranchFeedServer> /xpedx/sterling/Foundation/logs/MaxIB_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./xpedxStartIntegrationServer.sh port=1695 xpedxMaxCustFeedServer> /xpedx/sterling/Foundation/logs/MaxCust_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./xpedxStartIntegrationServer.sh port=1696 xpedxMaxCustXrefFeedServer> /xpedx/sterling/Foundation/logs/MaxCI_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./xpedxStartIntegrationServer.sh port=1697 xpedxMaxPriceBookFeedServer> /xpedx/sterling/Foundation/logs/MaxPricebook_`date +"%Y%m%d-%H%M"`.log 2>&1 &
	nohup ./xpedxStartIntegrationServer.sh port=1698 XPXOrderConfirmationEmailServer > /xpedx/sterling/Foundation/logs/OrderConfirmationEmails_`date +"%Y%m%d-%H%M"`.log 2>&1 & 
	
	exit 0
else
	echo "WARNING!: You can only execute this script on the $HOST_NAME (staging) server. Be careful where you start agents. "	
	exit 1
fi

 
