#!/bin/ksh

HOST_NAME="$(hostname)"
echo $HOST_NAME
if [[ "$HOST_NAME" = "zxpagnt01" ]];then
	desthostname=xpagnt02.ipaper.com 
	destusername=xpbuild 
	commandtorun='ksh -x stopdataloadagents.sh' 
	ssh $destusername@$desthostname $commandtorun
fi

