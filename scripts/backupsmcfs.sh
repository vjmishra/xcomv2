#!/bin/ksh
# This script zips up and backs up the smcfs folder prior to doing any builds. 
# created on 10/11/2012 by mahmoud L.
#modified - testing GIT

HOST_NAME="$(hostname)"
echo $HOST_NAME
cd /xpedx/sci_build/smcfs/

TIMESTAMP=`date +"%Y%m%d-%H%M"`

# if not already created, create the backup directory. 
mkdir /xpedx/sci_build/smcfs/backup

#compress it
zip -r smcfs-${TIMESTAMP}.zip smcfs

#store the zip file in the backup directory
mv smcfs-${TIMESTAMP}.zip /xpedx/sci_build/smcfs/backup/
echo "finished backing up smcfs-${TIMESTAMP}.zip to the /xpedx/sci_build/smcfs/backup/ directory..." 