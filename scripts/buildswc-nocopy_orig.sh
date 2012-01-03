#ksh
XPDEXHOME=/xpedx
SWCBUILDHOME=/xpedx/sci_build/WebChannel
DATE=`date +%Y-%m-%d`
CURRENTZIPFILE="WebChannel-${DATE}.zip"
ZIPFILEHOME=/home/share/xpadmin/buildsource

cd $SWCBUILDHOME

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties
case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpaappmc01) ENVIRONMENT=mc;; 
 *);;
esac

#copy the the webtrends tag files from the appropriate environment files. 
#copy the webtrends.js file
cp /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/js/webtrends/webtrends_$ENVIRONMENT.js /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/js/webtrends/webtrends.js
if [ "$?" != "0" ]; then
	echo "Could not copy the /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/js/webtrends/webtrends_$ENVIRONMENT.js file!" 1>&2
	echo "Could not copy the /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/js/webtrends/webtrends_$ENVIRONMENT.js file!" 1>&2 >> $LOGFILE
	exit 1
fi
#copy the webtrends.html file
cp /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/htmls/webtrends/webtrends_$ENVIRONMENT.html /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/htmls/webtrends/webtrends.html
if [ "$?" != "0" ]; then
	echo "Could not copy the /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/htmls/webtrends/webtrends_$ENVIRONMENT.html file!" 1>&2
	echo "Could not copy the /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/htmls/webtrends/webtrends_$ENVIRONMENT.html html!" 1>&2 >> $LOGFILE
	exit 1
fi

LOGFILE=/xpedx/sci_build/WebChannel/SWC-Target-`date +"%Y%m%d-%H%M"`.log
#ML: Don't send the process to the background (i.e remove & at the end of the below command) so that we can chain the commands and wait for execution of the build before triggering the next command.
if nohup /xpedx/ant/apache-ant-1.7.1/bin/ant -f build.xml -logfile $LOGFILE; then	
	#show log file immediately after. 
	echo "DO NOT press ctrl+c or you will kill this build process. To view the log file, please start another ssh session. ..." >> $LOGFILE
	#sleep 5
	echo "tail -f $LOGFILE"

	echo "SUCCESS! Completed the build without errors. Continuing with the deployment"  >> $LOGFILE
	echo "Moving swc.ear to /xpedx/deployments"  >> $LOGFILE
	#Deploy the ear file so that it's not overriden by the next swc build
	cd /home/share/xpadmin/scripts/
	ksh -x deployswc.sh  >> $LOGFILE
	exit 0
else
	echo "ERROR/FAILURE! There were errors in the build. Check logs..." 1>&2  >> $LOGFILE
	exit 1
fi



