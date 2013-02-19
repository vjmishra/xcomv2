/usr/ucb/ps -auxww | grep XPXResetPasswordServer |awk '{ print  "kill -9 " $2}' > devkillAgentproceses.sh

/usr/ucb/ps -auxww | grep XPXInventoryIndUpdateServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep  OrderApprovalEmailsServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep Healt |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep XPXOrderConfirmationEmailServer  |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh

/usr/ucb/ps -auxww | grep XPEDXAgentServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep xpedxSearchIndexBuild |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep XPXOrderStatusEmailServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh


/usr/ucb/ps -auxww | grep EmailAgentServer|awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh

/usr/ucb/ps -auxww | grep xpedxMaxCustFeedServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep xpedxMaxUOMFeedServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep  xpedxMaxPriceBookFeedServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep xpedxMaxItemBranchFeedServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep XPXInvoiceAgentServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep xpedxMaxCustXrefFeedServer  |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep XPXLoadInvoiceServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep xpedxMaxEntServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep XPXCustomerProfileMigrationServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep xpedxMaxDivFeedServer |awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
/usr/ucb/ps -auxww | grep XpedxDataFeedMQServer|awk '{ print  "kill -9 " $2}' >> devkillAgentproceses.sh
cd /xpedx/sci_build/CDT/fetch
LOGFILE=/xpedx/sci_build/CDT/entityupdatedb-`date +"%Y%m%d-%H%M"`.log
#update the database for PORT already in use
/xpedx/sterling/Foundation/bin/sci_ant.sh -f entitiesdb.xml -v -logfile $LOGFILE entitybuild; 




