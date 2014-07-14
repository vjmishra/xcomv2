#!/bin/sh

# WARNING: This file is created by the Configuration Wizard.
# Any changes to this script may be lost when adding extensions to this configuration.

rm -R /xpedx/wldomain/xpstg/servers/swcstg/tmp

rm -R /xpedx/wldomain/xpstg/servers/swcstg/cache
TIMESTAMP=`date +"%Y%m%d-%H%M"`

mv /xpedx/logfiles/swcstg/stdout.log /xpedx/logfiles/swcstg/stdout.log."${TIMESTAMP}"

# Added 4.20.11 at the request of Jon Gottschalk with Sterline for Visual VM 
echo "Setting JMX Parameters; Port=1650"
JAVA_OPTIONS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1650 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
export JAVA_OPTIONS

ulimit -n 8192

DOMAIN_HOME="/xpedx/wldomain/xpstg"

${DOMAIN_HOME}/bin/startManagedWebLogic.sh swcstg

