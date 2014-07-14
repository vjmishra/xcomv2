#!/bin/ksh
# This script zips up and backs up the WebChannel folder prior to doing any builds. 
# created on 10/11/2012 by mahmoud L.

HOST_NAME="$(hostname)"
echo $HOST_NAME
cd /xpedx/sci_build/WebChannel/

TIMESTAMP=`date +"%Y%m%d-%H%M"`

# if not already created, create the backup directory. 
mkdir /xpedx/sci_build/WebChannel/backup

#compress it
zip -r WebChannel-${TIMESTAMP}.zip WebChannel

#store the zip file in the backup directory
mv WebChannel-${TIMESTAMP}.zip /xpedx/sci_build/WebChannel/backup/
echo "finished backing up WebChannel-${TIMESTAMP}.zip to the /xpedx/sci_build/WebChannel/backup/ directory..." 