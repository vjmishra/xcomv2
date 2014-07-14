#!ksh

# PRECONDITION: This script assumes that you are not using NOHUP when starting agents. 
# 
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
ksh -x ../deploysmcfsstg.sh
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
ksh -x ../deployswcstg.sh
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


