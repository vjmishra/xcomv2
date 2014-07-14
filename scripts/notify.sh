#!/bin/bash
# Script expects 3 arguments
#$1 = buld type (EX: SWC, SMCFS, CDT, COM)
#$2 = Build status (Success, or Errors)
#3 list of email addresses to send out the email to.
echo "Script $0 was called with $@ ..."
if [ "$#" != "3" ]; then
	echo "This email script requires 3 arguments"
	exit 1
fi


HOST_NAME="$(hostname)"
case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=DEV;;
 zxpappt01) ENVIRONMENT=Staging;; 
 zxpapps01) ENVIRONMENT=ProdSupport;; 
 zxpagnt01) ENVIRONMENT=Production;; 
 zxpagnt02) ENVIRONMENT=Production;; 
 zxpappmc01) ENVIRONMENT=MasterConfig;; 
 *);;
esac

# email subject
SUBJECT="$1 build has completed with $2 on $HOST_NAME"
echo "SUBJECT = $SUBJECT"

# Email To ?
EMAIL=$3
# Email text/message
EMAILMESSAGE="/tmp/emailmessage.txt"
echo "Please check the build log files on $HOST_NAME for more information."> $EMAILMESSAGE
# send an email using /bin/mail
/bin/mail -s ${SUBJECT} ${EMAIL} < $EMAILMESSAGE
