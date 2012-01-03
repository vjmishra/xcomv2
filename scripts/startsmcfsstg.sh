#!/bin/sh

# WARNING: This file is created by the Configuration Wizard.
# Any changes to this script may be lost when adding extensions to this configuration.

rm -R /xpedx/wldomain/xpstg/servers/smcfsstg/tmp

rm -R /xpedx/wldomain/xpstg/servers/smcfsstg/cache

mv /xpedx/logfiles/smcfsstg/stdout.log /xpedx/logfiles/smcfsstg/stdout.log.`date +%Y-%m-%d`

# Added 4.20.11 at the request of Jon Gottschalk with Sterline for Visual VM 
echo "Setting JMX Parameters; Port=1651"
JAVA_OPTIONS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1651 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
export JAVA_OPTIONS

ulimit -n 8192

DOMAIN_HOME="/xpedx/wldomain/xpstg"

${DOMAIN_HOME}/bin/startManagedWebLogic.sh smcfsstg

