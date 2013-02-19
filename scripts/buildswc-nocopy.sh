#ksh
XPDEXHOME=/xpedx
SWCBUILDHOME=/xpedx/sci_build/WebChannel
DATE=`date +%Y-%m-%d`
CURRENTZIPFILE="WebChannel-${DATE}.zip"
ZIPFILEHOME=/home/share/xpadmin/buildsource
LOGFILE=/xpedx/sci_build/WebChannel/SWC-Target-nocopy-`date +"%Y%m%d-%H%M"`.log
echo "Build Start TimeStamp: `date`" > $LOGFILE
#backup the smcfs directory first. 
cd /home/share/xpadmin/scripts/
ksh -x backupswc.sh

#add write permission recursively
cd /xpedx/sci_build/WebChannel/WebChannel/
#chmod -R +w *

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
echo "SWC build started on $HOST_NAME (without using a zip file)" >> $LOGFILE
echo "Build Start TimeStamp: " `date` >> $LOGFILE

#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties
case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
  zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 zxpappmc01) ENVIRONMENT=dev;; 
 *);;
esac

#copy the the webtrends tag files from the appropriate environment files. 
#copy the webtrends.js file
cd /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/js/webtrends/
chmod +w *
cp webtrends_$ENVIRONMENT.js webtrends.js
if [ "$?" != "0" ]; then
	echo "Could not copy the /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/js/webtrends/webtrends_$ENVIRONMENT.js file!" 1>&2
	echo "Could not copy the /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/js/webtrends/webtrends_$ENVIRONMENT.js file!" 1>&2 >> $LOGFILE
	exit 1
fi
#copy the webtrends.html file
cd /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/htmls/webtrends/
chmod +w *
cp webtrends_$ENVIRONMENT.html webtrends.html
if [ "$?" != "0" ]; then
	echo "Could not copy the /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/htmls/webtrends/webtrends_$ENVIRONMENT.html file!" 1>&2
	echo "Could not copy the /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx/htmls/webtrends/webtrends_$ENVIRONMENT.html html!" 1>&2 >> $LOGFILE
	exit 1
fi

# update the static content version number to burst the browser cache. 
cd /home/share/xpadmin/scripts/
./updatestaticcontentbuildversionnumber.sh

	# cd back to the swc build home directory
cd $SWCBUILDHOME
echo "Using $CURRENTZIPFILE for the build: "  >> $LOGFILE
ls -lt $CURRENTZIPFILE >> $LOGFILE

#ML: Don't send the process to the background (i.e remove & at the end of the below command) so that we can chain the commands and wait for execution of the build before triggering the next command.
if nohup /xpedx/ant/apache-ant-1.7.1/bin/ant -f build.xml -logfile $LOGFILE; then	
	#nohup /xpedx/ant/apache-ant-1.7.1/bin/ant -f build.xml -logfile /xpedx/sci_build/WebChannel/SWC-Target-`date +"%Y%m%d-%H%M"`.log;
	#show log file immediately after. 
	echo "DO NOT press ctrl+c or you will kill this build process. To view the log file, please start another ssh session. ..." >> $LOGFILE
	#sleep 5
	echo "tail -f $LOGFILE"

	echo "SUCCESS! Completed the build without errors. Continuing with the deployment"  >> $LOGFILE
	echo "Moving swc.ear to /xpedx/deployments"  >> $LOGFILE
	#Deploy the ear file so that it's not overriden by the next swc build
	cd /home/share/xpadmin/scripts/
	ksh -x deployswc.sh  >> $LOGFILE
	ksh -x postbuildscript.sh >> $LOGFILE
	if [ "$HOST_NAME" = "zxpappd01" ]; then
		#ftp the static content over to the s29exvmbuild server (only on xpappd01). check synchronizestaticcontent.sh script for help. 
		echo "synchronizing all the static contennt (anything that changed since 1/1/2000 ... "  >> $LOGFILE
		ksh -x synchronizestaticcontent.sh "2000-01-01 00:00"
		echo "Finished synchronizing static contennt... "  >> $LOGFILE
	fi
	./sendnotification.sh SWC Success mahmoud.lamriben@hp.com,mohammad.balkhi@hp.com,kirtesh.lathkar@hp.com,vijay.mishra@hp.com,sameer.vasudeva@hp.com $LOGFILE
	echo "Build Completion TimeStamp: " `date` >> $LOGFILE
	
	exit 0
else
	echo "ERROR/FAILURE! There were errors in the build. Check logs..." 1>&2  >> $LOGFILE
	./sendnotification.sh SWC Error mohammad.balkhi@hp.com,kirtesh.lathkar@hp.com,sameer.vasudeva@hp.co,vijay.mishra@hp.com,mahmoud.lamriben@hp.com $LOGFILE
	echo "Build with Errors Completion TimeStamp: " `date` >> $LOGFILE
	exit 1
fi



