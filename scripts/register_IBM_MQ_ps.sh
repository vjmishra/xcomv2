#!/bin/ksh
# This script registers the IBM MQ jars

cd
cd /xpedx/sterling/Foundation/bin

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
if [ "$HOST_NAME" = "zxpapps01" ]
then 

./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/com.ibm.mq.jar -targetJVM AGENT 
./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/com.ibm.mqjms.jar -targetJVM AGENT 
./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/connector.jar -targetJVM AGENT 
./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/jms.jar -targetJVM AGENT 
./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/jndi.jar -targetJVM AGENT 
./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/jta.jar -targetJVM AGENT 
./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/fscontext.jar -targetJVM AGENT 
./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/providerutil.jar -targetJVM AGENT 
./install3rdParty.sh WebLogic 10_3 -j /opt/mqm/java/lib/dhbcore.jar -targetJVM AGENT

# End of if statement
fi
