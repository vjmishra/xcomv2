#!/bin/sh
cp -p /xpedx/logfiles/swc02/stdout.log /xpedx/logfiles/swc02/stdout.log.$(date +%Y-%m-%d)
cat < /dev/null > /xpedx/logfiles/swc02/stdout.log
find /xpedx/logfiles/swc02/*log* -mtime +14 -exec rm -f {} \;
cp -p /xpedx/logfiles/smcfs02/stdout.log /xpedx/logfiles/smcfs02/stdout.log.$(date +%Y-%m-%d)
cat < /dev/null > /xpedx/logfiles/smcfs02/stdout.log
find /xpedx/logfiles/smcfs02/*log* -mtime +14 -exec rm -f {} \;
