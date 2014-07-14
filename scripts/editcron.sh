#!/bin/ksh
cd /var/spool/cron/crontabs
EDITOR=vi
export EDITOR
crontab -e