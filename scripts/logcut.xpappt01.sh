#!/bin/sh
cp -p /xpedx/logfiles/swcstg/stdout.log /xpedx/logfiles/swcstg/stdout.log.$(date +%Y-%m-%d)
cat < /dev/null > /xpedx/logfiles/swcstg/stdout.log
find /xpedx/logfiles/swcstg/*log* -mtime +14 -exec rm -f {} \;
cp -p /xpedx/logfiles/smcfsstg/stdout.log /xpedx/logfiles/smcfsstg/stdout.log.$(date +%Y-%m-%d)
cat < /dev/null > /xpedx/logfiles/smcfsstg/stdout.log
find /xpedx/logfiles/smcfsstg/*log* -mtime +14 -exec rm -f {} \;
