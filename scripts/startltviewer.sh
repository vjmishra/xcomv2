#!/bin/sh

# WARNING: This file is created by the Configuration Wizard.
# Any changes to this script may be lost when adding extensions to this configuration.

rm -R /xpedx/wldomain/xpps/servers/ltviewer/tmp

rm -R /xpedx/wldomain/xpps/servers/ltviewer/cache

mv /xpedx/logfiles/ltviewer/stdout.log /xpedx/logfiles/ltviewer/stdout.log.`date +"%Y%m%d-%H%M"`

# Added 4.20.11 at the request of Jon Gottschalk with Sterline for Visual VM 
echo "Setting JMX Parameters; Port=1649"
JAVA_OPTIONS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1649 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
export JAVA_OPTIONS

ulimit -n 8192

DOMAIN_HOME="/xpedx/wldomain/xpps"

${DOMAIN_HOME}/bin/startManagedWebLogic.sh ltviewer

