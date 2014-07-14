#!ksh

#change to the scripts directory
cd
cd scripts/unattended

#build smcfs
ksh -x ./buildsmcfs.sh
if [ "$?" != "0" ]; then
	echo "SMCFS build failed. Please check the log file and try again. Aborting the build!" 1>&2
	exit 1
fi

#deploy smcfs ear
ksh -x ../deploysmcfsdev.sh
if [ "$?" != "0" ]; then
	echo "Couldn't deploy smcfs.ear to the deployments directory. Please check the log file and try again. Aborting the build!" 1>&2
	exit 1
fi

#build swc
ksh -x ./buildswc.sh
if [ "$?" != "0" ]; then
	echo "SWC build failed. Please check the log file and try again. Aborting the build!" 1>&2
	exit 1

fi

#deploy swc ear
ksh -x ../deployswcdev.sh
if [ "$?" != "0" ]; then
	echo "Couldn't deploy swc.ear to the deployments directory. Please check the log file and try again. Aborting the build!" 1>&2
	exit 1
fi

#build CDT
ksh -x ./buildcdt.sh
if [ "$?" != "0" ]; then
	echo "Couldn't execute the CDT build. Please check the log file and try again. Aborting the build!" 1>&2
	exit 1
fi


