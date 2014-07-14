#!ksh
	echo "The smcfs build has completed. Check the logs before proceeding. Would you like to deploy the smcfs.ear to the /xpedx/deployments directory? "
    /usr/5bin/echo "( y/n ) : \c"
    read answer
    if [ "$answer" = "y" ] || [ "$answer" = "Y" ] || [ "$answer" = "yes" ] || [ "$answer" = "YES" ]
    then
		echo "About to call /scripts/deploysmcfs-env.sh"
		exit
	else
		echo "skipping the deployment step... Good bye!"
		exit
    fi
