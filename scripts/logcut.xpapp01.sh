#!/bin/sh
cp -p /xpedx/logfiles/swc01/stdout.log /xpedx/logfiles/swc01/stdout.log.$(date +%Y-%m-%d)
cat < /dev/null > /xpedx/logfiles/swc01/stdout.log
find /xpedx/logfiles/swc01/*log* -mtime +14 -exec rm -f {} \;
cp -p /xpedx/logfiles/smcfs01/stdout.log /xpedx/logfiles/smcfs01/stdout.log.$(date +%Y-%m-%d)
cat < /dev/null > /xpedx/logfiles/smcfs01/stdout.log
find /xpedx/logfiles/smcfs01/*log* -mtime +14 -exec rm -f {} \;
