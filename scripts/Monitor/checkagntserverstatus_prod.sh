#!/bin/ksh
set +x
cd /home/share/xpadmin/scripts/

TMPFILE1=/xpedx/logfiles/TestTmpFile_`date +"%Y%m%d-%H%M"`.log
echo "TMPFILE1 = $TMPFILE1"
TMPFILE2=/xpedx/logfiles/TestTmpFile2_`date +"%Y%m%d-%H%M"`.log
echo "TMPFILE2 = $TMPFILE2"

#trach just servers up/down in these two files.
TMPAgentsDown=/xpedx/logfiles/AgentsDown_`date +"%Y%m%d-%H%M"`.log
TMPAgentsUp=/xpedx/logfiles/AgentsUp_`date +"%Y%m%d-%H%M"`.log

#header file is used for sending email in html. see this file for to/cc email info.
HEADERFILE=/home/share/xpadmin/scripts/Monitor/MailHeader.txt

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
 case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=dev;; 
 *);;
esac
echo "testing on $ENVIRONMENT"

if ["$HOST_NAME" -eq "zxpagnt01" -o "$HOST_NAME" -eq "zxpagnt02"]; then 
	echo "WARNING!\nThis script should not be executed on the agent boxes themselves. It needs to be executed on either dev/stg/or prodsupport"
	echo "\nExiting...."
	exit 0
fi 

LOGFILE=/xpedx/logfiles/AgentServersdown_$ENVIRONMENT_`date +"%Y%m%d-%H%M"`.log
echo "<html>" > $LOGFILE
echo "<HEAD>" >> $LOGFILE

#echo "<meta http-equiv=""Content-Type"" content=""text/html; charset=us-ascii"">" >> $LOGFILE
#echo "<meta name=""Generator"" content=""Microsoft Word 12 (filtered medium)"">" >> $LOGFILE
#echo "<style><!--" >> $LOGFILE
#echo "h1" >> $LOGFILE
#echo "	{font-size:18.0pt;" >> $LOGFILE
#echo "	font-family:""Calibri"",""sans-serif"";" >> $LOGFILE
#echo "	color:red;" >> $LOGFILE
#echo "	font-weight:normal;}" >> $LOGFILE
#echo "h2" >> $LOGFILE
#echo "	{font-size:14.0pt;" >> $LOGFILE
#echo "	font-family:""Calibri"",""sans-serif"";" >> $LOGFILE
#echo "	color:red;" >> $LOGFILE
#echo "	font-weight:normal;}" >> $LOGFILE
#echo "--></style>" >> $LOGFILE


echo "</HEAD>" >> $LOGFILE
echo "<BODY>" >> $LOGFILE

#/usr/ucb/ps -auxww | grep xpbuild | grep -v grep |awk '{ print  "commandxxx -9 " $2}' 
#echo "contents of killagentprocesses.sh are: " 
#cat killagentprocesses.sh 

#set -A Arr_Agent_Server_Names  xpedxMaxCustFeedServer xpedxMaxUOMFeedServer XPXOrderConfirmationEmailServer OrderApprovalEmailsServer xpedxMaxPriceBookFeedServer XPXResetPasswordServer xpedxMaxItemBranchFeedServer XPXInvoiceAgentServer xpedxMaxCustXrefFeedServer XPXLoadInvoiceServer XPXOrderStatusEmailServer xpedxMaxEntServer XpedxDataFeedMQServer xpedxSearchIndexBuild xpedxMaxDivFeedServer HealthMonitor
#note: since the searchindex agent has two JVMs, only put one instance in the array below
set -A Arr_Agent_Server_Names HealthMonitor OrderApprovalEmailsServer XPEDXAgentServer XpedxDataFeedMQServer xpedxMaxCustFeedServer xpedxMaxCustXrefFeedServer xpedxMaxDivFeedServer xpedxMaxEntServer xpedxMaxItemBranchFeedServer xpedxMaxPriceBookFeedServer xpedxMaxUOMFeedServer xpedxSearchIndexBuild XPXInvoiceAgentServer XPXLoadInvoiceServer XPXOrderConfirmationEmailServer XPXOrderStatusEmailServer XPXResetPasswordServer FakeAgentOnXpagnt01 FakeAgentOnXpagnt02

AgentsDowns=""
Count=0
CountUp=0
if [ "$HOST_NAME" = "zxpappd01" ]; then
		echo "about to ssh to xpagnt01 ... " 
		#echo "about to establish an ssh session to zxpagnt01 agent servers ... "  >> $LOGFILE	
		#run  commands on xpagnt01
		
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  HealthMonitor | grep -v grep" |awk '{ print  "HealthMonitor " $2}' > $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  OrderApprovalEmailsServer | grep -v grep" |awk '{ print  "OrderApprovalEmailsServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  XPEDXAgentServer | grep -v grep" |awk '{ print  "XPEDXAgentServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  XpedxDataFeedMQServer | grep -v grep" |awk '{ print  "XpedxDataFeedMQServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxMaxCustFeedServer | grep -v grep" |awk '{ print  "xpedxMaxCustFeedServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxMaxCustXrefFeedServer | grep -v grep" |awk '{ print  "xpedxMaxCustXrefFeedServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxMaxDivFeedServer | grep -v grep" |awk '{ print  "xpedxMaxDivFeedServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxMaxEntServer | grep -v grep" |awk '{ print  "xpedxMaxEntServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxMaxItemBranchFeedServer | grep -v grep" |awk '{ print  "xpedxMaxItemBranchFeedServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxMaxPriceBookFeedServer | grep -v grep" |awk '{ print  "xpedxMaxPriceBookFeedServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxMaxUOMFeedServer | grep -v grep" |awk '{ print  "xpedxMaxUOMFeedServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxSearchIndexBuild | grep -v grep" |awk '{ print  "xpedxSearchIndexBuild " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  XPXInvoiceAgentServer | grep -v grep" |awk '{ print  "XPXInvoiceAgentServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  XPXLoadInvoiceServer | grep -v grep" |awk '{ print  "XPXLoadInvoiceServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  XPXOrderConfirmationEmailServer | grep -v grep" |awk '{ print  "XPXOrderConfirmationEmailServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  XPXOrderStatusEmailServer | grep -v grep" |awk '{ print  "XPXOrderStatusEmailServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  XPXResetPasswordServer | grep -v grep" |awk '{ print  "XPXResetPasswordServer " $2}' >> $TMPFILE1
		ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  FakeAgentOnXpagnt01 | grep -v grep" |awk '{ print  "FakeAgentOnXpagnt01 " $2}' >> $TMPFILE1

		echo "completed ssh to xpagnt01 ... "  
		#echo "about to establish an ssh session to zxpagnt02 agent servers ... "  >> $LOGFILE
		echo "about to ssh to xpagnt02 ... " 
		#run same commands on xpagnt02	
		
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  HealthMonitor | grep -v grep" |awk '{ print  "HealthMonitor " $2}' > $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  OrderApprovalEmailsServer | grep -v grep" |awk '{ print  "OrderApprovalEmailsServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  XPEDXAgentServer | grep -v grep" |awk '{ print  "XPEDXAgentServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  XpedxDataFeedMQServer | grep -v grep" |awk '{ print  "XpedxDataFeedMQServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  xpedxMaxCustFeedServer | grep -v grep" |awk '{ print  "xpedxMaxCustFeedServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  xpedxMaxCustXrefFeedServer | grep -v grep" |awk '{ print  "xpedxMaxCustXrefFeedServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  xpedxMaxDivFeedServer | grep -v grep" |awk '{ print  "xpedxMaxDivFeedServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  xpedxMaxEntServer | grep -v grep" |awk '{ print  "xpedxMaxEntServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  xpedxMaxItemBranchFeedServer | grep -v grep" |awk '{ print  "xpedxMaxItemBranchFeedServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  xpedxMaxPriceBookFeedServer | grep -v grep" |awk '{ print  "xpedxMaxPriceBookFeedServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  xpedxMaxUOMFeedServer | grep -v grep" |awk '{ print  "xpedxMaxUOMFeedServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  xpedxSearchIndexBuild | grep -v grep" |awk '{ print  "xpedxSearchIndexBuild " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  XPXInvoiceAgentServer | grep -v grep" |awk '{ print  "XPXInvoiceAgentServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  XPXLoadInvoiceServer | grep -v grep" |awk '{ print  "XPXLoadInvoiceServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  XPXOrderConfirmationEmailServer | grep -v grep" |awk '{ print  "XPXOrderConfirmationEmailServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  XPXOrderStatusEmailServer | grep -v grep" |awk '{ print  "XPXOrderStatusEmailServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  XPXResetPasswordServer | grep -v grep" |awk '{ print  "XPXResetPasswordServer " $2}' >> $TMPFILE2
		ssh zxpagnt02 "/usr/ucb/ps -auxww | grep  FakeAgentOnXpagnt02 | grep -v grep" |awk '{ print  "FakeAgentOnXpagnt02 " $2}' >> $TMPFILE1


		#echo "Completed ssh sessions on agent servers ... "  >> $LOGFILE
		echo "Completed ssh sessions on agent servers . about to loop through the list..."   

		for AgntSrvrname in ${Arr_Agent_Server_Names[@]}
			do
				###countOfAgentServerPIDs=$(echo `grep -w $AgntSrvrname "/xpedx/sterling/Foundation/logs/CheckAgentServer.log"|wc -l`)
				echo "processing agent: $AgntSrvrname..."
				countOfAgentServerPIDs=$(echo `grep -w $AgntSrvrname $TMPFILE1|wc -l`)
				countOfAgentServerPIDs2=$(echo `grep -w $AgntSrvrname $TMPFILE2|wc -l`)
				echo "Count of $AgntSrvrname on xpagnt01 = $countOfAgentServerPIDs"
				echo "Count of $AgntSrvrname on xpagnt02 = $countOfAgentServerPIDs2"
				
				#Note: All agents spawn exactly 3/Three process when they're started. So we need to check if the process count is < 3 and if so, then that agent is indeed DOWN and should be added to the alert. 
				#note: if statement must include space after [ and before ]  or will fail. 
				if [ ${countOfAgentServerPIDs} -lt 3 -a ${countOfAgentServerPIDs2} -lt 3 ]
				then 
#				   echo $AgntSrvrname 
				   #echo "Agent server $AgntSrvrname Not running on neither xpagnt01 or xpagnt02"
				   
					if [ ${Count} -lt 1 ]
					then		
						echo "<H1>List of Agents Down in Production</H1>" > $TMPAgentsDown
						#echo "Below is the list of Agents Not Running: <br/>" >> $LOGFILE
					fi
					Count=$((Count+1))
					echo "<font color=""red"">$Count- $AgntSrvrname</font><br/>" >> $TMPAgentsDown
					AgentsDowns=$AgentsDowns" "$AgntSrvrname
					#reset the count to zero for the next iteration
					countOfAgentServerPIDs=0
					countOfAgentServerPIDs2=0			
					#echo $AgentsDowns
				else
					#track servers that are up
					if [ ${CountUp} -lt 1 ]
					then			 
						 echo "<br/><H1>List of Agents Up in Production</H1>" > $TMPAgentsUp
					fi
					CountUp=$((CountUp+1))
					echo "<font color=""green"">$CountUp- $AgntSrvrname</font><br/>" >> $TMPAgentsUp
				
				fi
			done
			
		#Append the list of agenst down or up to the main log file
		cat $TMPAgentsDown >> $LOGFILE
		cat $TMPAgentsUp >> $LOGFILE
		echo "<br/><br/><br/>" >> $LOGFILE
		
		#echo "Contents of TMPFILE1 are: \n"
		#cat $TMPFILE1
		#echo "\n\n\n"
		
		#echo "Contents of TMPFILE2 are: \n"
		#cat $TMPFILE2
		#echo "\n\n\n"
		
		rm -f $TMPFILE1
		rm -f $TMPFILE2
		rm -f $TMPAgentsDown
		rm -f $TMPAgentsUp
		
		
		#close the body of the html tags
		echo "</BODY>" >> $LOGFILE
		echo "</HTML>" >> $LOGFILE
		echo "contents of the email to be sent out:\r\r\r"
		cat $HEADERFILE $LOGFILE
		if [ "${Count}" -gt 0 ]; then
			#/bin/mailx -t -s "TESTING: List of Agent Servers Down on <PRODUCTION(xpagnt01 and 02)> - Total Agents Down: $Count" < $HEADERFILE < $LOGFILE
			#/bin/mailx -t < $HEADERFILE < $LOGFILE
			cat $HEADERFILE $LOGFILE |/bin/mailx -t
		else
			echo "\nAll the agents are up. No alert will be sent out." 
			echo "\nAll the agents are up. No alert will be sent out." >> $LOGFILE
		fi
fi
exit 0


#if [ `ssh zxpagnt01 "/usr/ucb/ps -auxww | grep  xpedxMaxPriceBookFeedServer | grep -v grep"` -gt 0 ] then
#fi
#/usr/ucb/ps -auxww | grep $1 | grep -v grep |awk '{ print  "kill -9 " $2}'
#if [ `ssh zxpagnt01 "ps -ef | grep ora_ | grep -v grep | wc -l"` -gt 0 ] then
#fi
#/usr/ucb/ps -auxww | grep $1 | grep -v grep |awk '{ print  "kill -9 " $2}'
