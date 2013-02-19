#!/bin/bash
# script to send simple email
# Script expects 3 arguments
#$1 = buld type (EX: SWC, SMCFS, CDT, COM)
#$2 = Build status (Success, or Errors)
#3 list of email addresses to send out the email to.
usage()
{
echo "This email script requires at leats 3 arguments"
	echo "usage: sendnotification.sh buildtype buildStatus ListofEmailsToSendTo LogFilePath blnAttachLogFile"
	echo "	buildtype= SWC, SMCFS, CDT, COM, etc"
	echo "	buildStatus= Success or Error"
	echo "	listOfEmailsToSendTo: comma separated list of emails. "
	echo "	LogFilePath: path to the log file used by the build process. "
	echo "	blnAttachLogFile: true or false to indicate if the log file should be attached to the email. Use only on small files please. "
	echo "	Example: ./sendnotification.sh SWC Success mahmoud.lamriben@hp.com,mohammad.balkhi@hp.com /xpedx/sci_build/smcfs/smcfsBuild-20120608-0514.log true"
	echo "	Example2: ./sendnotification.sh SWC Success mahmoud.lamriben@hp.com /xpedx/sci_build/smcfs/smcfsBuild-20120608-0514.log"
	exit 1
}

if [ "$#" -lt "3" ]; then
	usage
fi


HOST_NAME="$(hostname)"
case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=DEV;;
 zxpappd02) ENVIRONMENT=SANDBOX;;
 zxpappt01) ENVIRONMENT=Staging;; 
 zxpapps01) ENVIRONMENT=ProdSupport;; 
 zxpagnt01) ENVIRONMENT=Production;; 
 zxpagnt02) ENVIRONMENT=Production;; 
 zxpappmc01) ENVIRONMENT=MasterConfig;; 
 *);;
esac

# email subject
case $2 in
  "Success")
     SUBJECT="SUCCESS - $1 build has completed with success on $ENVIRONMENT / $HOST_NAME"
     ;;
  "Error")
     SUBJECT="ERROR - $1 build has completed with errors on $ENVIRONMENT / $HOST_NAME"
     ;;
"Triggered")
     SUBJECT="SI_ON_Production - $1 build has been triggered on $ENVIRONMENT / $HOST_NAME"
     ;;
"Not_Triggered")
     SUBJECT="SI_ON_Production - $1 build has not been triggered on $ENVIRONMENT / $HOST_NAME, please contact Admin"
     ;;

esac

#echo $SUBJECT
# Email To ?
EMAIL=$3
#Check for log file
#LogFile
# Email text/message
#SUBJECT="Dev Build Intiated"
EMAILMESSAGE=$4
echo "$1 BUILD STATUS: $2" >> $EMAILMESSAGE	
echo "" >> $EMAILMESSAGE	
if [ "$#" -eq "4" ]; then
	#echo $4
	LogFile="$4"
	#a log file argument was included
	echo "Below are the last few lines of the $1 build log file [$LogFile] on $HOST_NAME. " >> $EMAILMESSAGE	
	echo "for more information, logon to the server to check the log file." >> $EMAILMESSAGE
	#include last 20 lines of the log file
	tail -20 $LogFile >> $EMAILMESSAGE
	echo "Please check the $1 build log file [$LogFile] on $HOST_NAME for more information." >> $EMAILMESSAGE
else
	echo "Please check the $1 build log file [$LogFile] on $HOST_NAME for more information." >> $EMAILMESSAGE	
fi

#check for attachment
#declare new attachment file name variable
#AttachFile
if [ "$#" -eq "5" ]; then
	if [ "$5" -eq "true" ]; then
		AttachFile="true"
	else
		AttachFile="false"
	fi
	LogFile="$4"
	#a log file argument was included
	echo "Below are the last few lines of the $1 build log file [$LogFile] on $HOST_NAME. " >> $EMAILMESSAGE	
	echo "for more information, logon to the server to check the log file." >> $EMAILMESSAGE	
	#include last 20 lines of the log file
	tail -20 $LogFile >> $EMAILMESSAGE
	echo "Please check the $1 build log file[$LogFile] on $HOST_NAME for more information." >> $EMAILMESSAGE
fi
 


# send an email using /bin/mail
#/bin/mail -s "$SUBJECT" "$EMAIL" < $EMAILMESSAGE
#echo "below are the contents of the $EMAILMESSAGE file"
#cat $EMAILMESSAGE

if [ "$#" -le "4" ]; then
	#no attachment or log file specified
	#this line sends an email
	/bin/mailx -s "$SUBJECT" "$EMAIL" < "$EMAILMESSAGE"
	exit 0
elif [ "$#" == "5" ]; then
	#This line converts a file (in this case a comma delimited report produced from my script), and attaches it to an email.  The OUTPUT variable is the output from whatever the script is writing.  The uuencode actually  encapsulates the output so it becomes a file vs. text streaming across the screen
	#unix2dos -ascii -437 $OUTPUT |uuencode /tmp/$THISSCRIPT.csv|mailx -s "$THISSCRIPT Output" mahmoud.lamriben@hp.com
	/bin/mailx -s "$SUBJECT" "$EMAIL" < "$EMAILMESSAGE"
	exit 0
else
	echo "Error. Please specify 3, 4, or 5 arguments"
	usage
fi



