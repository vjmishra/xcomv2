#!/bin/ksh
cd /xpedx/sci_build/PreventSearchIndexFailures/queudepth
export JAVA_HOME=/xpedx/java/jdk1.6.0_37/bin
export PATH=$JAVA_HOME
java -jar /xpedx/sci_build/PreventSearchIndexFailures/queudepth/QueueConnection.jar > /xpedx/sci_build/PreventSearchIndexFailures/logs/QueueConnection.log

exit 0