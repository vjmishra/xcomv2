#ksh
#this script is executed post the build process. It should only contain statements that are executed temporarily such as adding a jar file for a PMR during debug/testing
#add the commands needed below

#1) add the debug jar
HOST_NAME="$(hostname)"
#note: check the hosts below to specify which environments these postbuild events should apply to.
#Note the syntx of if/or statements.  Must have spaces between [ and " and between " and ] or won't work right. 
if [ "$HOST_NAME" = "zxpappd01" ] || [ "$HOST_NAME" = "zxpappd02" ] || [ "$HOST_NAME" = "zxpagnt01" ] || [ "$HOST_NAME" = "zxpagnt02" ] || [ "$HOST_NAME" = "zxpapps01" ] ;then
	cd /xpedx/deployments
	cp /home/share/xpadmin/PMR/357791/swc.jar .
	jar -uvf swc.ear swc.jar
	rm -R swc.jar
else
	echo "the postbuildscript isn't set to execute on $HOST_NAME. Exiting successfully"
fi

# Add more statements below if needed using this template

#if [ "$HOST_NAME" = "x" ] || [ "$HOST_NAME" = "y" ] ;then
#	cd somedirectory
#	execute some command
#fi

