#!/bin/ksh
# This script kills the agent passed in the first parameter by calling the stopagent.sh script

ksh -x /home/share/xpadmin/scripts/stopagent.sh $1

exit 0