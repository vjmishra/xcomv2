#!/bin/ksh

################################################################################################################################
###Script to find the List of Agents Not Running #####################
echo "Finding agents Server running test"

HOST_NAME="$(hostname)"

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=sandbox;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=masterconfig;;  
 *);;
esac

###Path /xpedx/xpbuild/EmailTest
#### For Bash Arr_Agent_Server_Names =(XpedxDataFeedMQServer xpedxMaxDivFeedServer xpedxMaxEntServer mnm jbc xpedxMaxItemBranchFeedServer xpedxMaxCustFeedServer)


### Initialization of the Agent Servers to be checked ###
set -A Arr_Agent_Server_Names  XpedxDataFeedMQServer xpedxMaxDivFeedServer xpedxMaxEntServer  xpedxMaxItemBranchFeedServer xpedxMaxCustFeedServer XPXOrderConfirmationEmailServer OrderApprovalEmailsServer xpedxMaxUOMFeedServer
RunningAgents=""
Count=0


#/xpedx/sterling/Foundation/logs
LOGFILE=/xpedx/sterling/Foundation/logs/AgentServersdown`date +"%Y%m%d-%H%M"`.log
TMPFILE=/xpedx/sterling/Foundation/logs/AgentServersTmpFile`date +"%Y%m%d-%H%M"`.log
/usr/ucb/ps -auxww | grep XpedxDataFeedMQServer |awk '{ print  "XpedxDataFeedMQServer " $2}' >>  $TMPFILE
/usr/ucb/ps -auxww | grep xpedxMaxDivFeedServer |awk '{ print  "xpedxMaxDivFeedServer " $2}' >> $TMPFILE
/usr/ucb/ps -auxww | grep xpedxMaxEntServer |awk '{ print  "xpedxMaxEntServer " $2}' >> $TMPFILE
/usr/ucb/ps -auxww | grep xpedxMaxItemBranchFeedServer |awk '{ print  "xpedxMaxItemBranchFeedServer " $2}' >> $TMPFILE
/usr/ucb/ps -auxww | grep xpedxMaxCustFeedServer |awk '{ print  "xpedxMaxCustFeedServer " $2}' >> $TMPFILE
/usr/ucb/ps -auxww | grep XPXOrderConfirmationEmailServer |awk '{ print  "XPXOrderConfirmationEmailServer " $2}' >> $TMPFILE
/usr/ucb/ps -auxww | grep OrderApprovalEmailsServer |awk '{ print  "OrderApprovalEmailsServer " $2}' >> $TMPFILE
/usr/ucb/ps -auxww | grep xpedxMaxUOMFeedServer |awk '{ print  "xpedxMaxUOMFeedServer " $2}' >> $TMPFILE

##/usr/ucb/ps -auxww | grep jbc |awk '{ print  "jbc " $2}' >> /xpedx/sterling/Foundation/logs/CheckAgentServer.log




echo $HOST_NAME
echo $ENVIRONMENT
echo $RunningAgents
for AgntSrvrname in ${Arr_Agent_Server_Names[@]}
	do
		###countxpedxAgentServerPID=$(echo `grep -w $AgntSrvrname "/xpedx/sterling/Foundation/logs/CheckAgentServer.log"|wc -l`)
		countxpedxAgentServerPID=$(echo `grep -w $AgntSrvrname $TMPFILE|wc -l`)
		
		#Note: All agents spawn exactly 3/Three process when they're started. So we need to check if the process count is < 3 and if so, then that agent is indeed DOWN and should be added to the alert. 
		if [ "${countxpedxAgentServerPID}" -lt 3 ]; then 

		   echo "Agent server Not running"
		   echo $AgntSrvrname 

			if [ "${Count}" -lt 1 ]; then			 
				 echo "Below is the list of Agents Not Running: \n" >> $LOGFILE
			fi
			echo $AgntSrvrname >> $LOGFILE
			Count=$((Count+1))
			RunningAgents=$RunningAgents" "$AgntSrvrname
			countxpedxAgentServerPID=0
			echo $RunningAgents
		fi
	done
rm -f $TMPFILE
#Send an email alert ONLY if the Count is > 0. Use comma separated list of emails.
if [ "${Count}" -gt 0 ]; then
	/bin/mailx -s "List of Agent Servers Down on <$ENVIRONMENT> - Total Agents Down: $Count"   xpedxNGNotifications@hp.com< $LOGFILE
else
	 echo "\nAll the agents are up. No alert will be sent out." >> $LOGFILE
fi

