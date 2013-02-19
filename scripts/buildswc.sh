#ksh
XPDEXHOME=/xpedx
SWCBUILDHOME=/xpedx/sci_build/WebChannel
DATE=`date +%Y-%m-%d`
ZIPFILEHOME=/home/share/xpadmin/buildsource
LOGFILE=/xpedx/sci_build/WebChannel/SWC-Target-`date +"%Y%m%d-%H%M"`.log

#Check which server, set customer overrides for mortals
HOST_NAME="$(hostname)"
echo $HOST_NAME
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties
echo "SWC build started on $HOST_NAME" > $LOGFILE
echo "Build Start TimeStamp: " `date` >> $LOGFILE

# note: we are setting MasterConfig as a dev server so that it's property files is retrieved from dev. otherwise the build will fail because the ant script expects, dev, staging, prod and prodsupport
#zxpaappmc01) ENVIRONMENT=dev;; 
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

# temporarily look for a different smcfs zip file name for the sandbox while we're testing agile. 
if [ "$HOST_NAME" = "zxpappd02" ]; then
	CURRENTZIPFILE="WebChannel-sandbox-${DATE}.zip"
else
	CURRENTZIPFILE="WebChannel-${DATE}.zip"	
fi

cd $SWCBUILDHOME
cp $ZIPFILEHOME/$CURRENTZIPFILE .
echo "Using $CURRENTZIPFILE for the build: "  >> $LOGFILE
ls -lt $CURRENTZIPFILE >> $LOGFILE

cd /xpedx/sci_build/WebChannel/WebChannel/
chmod -R +w *

#backup the smcfs directory first. 
cd /home/share/xpadmin/scripts/
ksh -x backupswc.sh

#delete the current smcfs folder to recreate it fresh
cd $SWCBUILDHOME
rm -R /xpedx/sci_build/WebChannel/WebChannel 
unzip $CURRENTZIPFILE 
if [ "$?" != "0" ]; then
	echo "Could not find/unzip $CURRENTZIPFILE zip file. Aborting the build!" 1>&2
	exit 1
fi

#add write permission recursively
cd /xpedx/sci_build/WebChannel/WebChannel/
chmod -R +w *



#copy the the webtrends tag files from the appropriate environment files. 
#copy the webtrends.js file
#since prodsupport doesn't have a webtrends profile yet, don't copy these files
#if [ "$ENVIRONMENT" != "prodsupport" ]; then
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
	

#fi


# cd back to the swc build home directory
cd $SWCBUILDHOME

#ML: Don't send the process to the background (i.e remove & at the end of the below command) so that we can chain the commands and wait for execution of the build before triggering the next command.
if nohup /xpedx/ant/apache-ant-1.7.1/bin/ant -f build.xml -Dbuild.env="$ENVIRONMENT" -logfile $LOGFILE; then	
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
	./sendnotification.sh SWC Success mahmoud.lamriben@hp.com,balkhi.mohammad@hp.com $LOGFILE
	echo "Build Completion TimeStamp: " `date` >> $LOGFILE
	exit 0
else
	echo "ERROR/FAILURE! There were errors in the build. Check logs..." 1>&2  >> $LOGFILE
	echo "Build With Errors Completion TimeStamp: " `date` >> $LOGFILE
	./sendnotification.sh SWC Error mahmoud.lamriben@hp.com,balkhi.mohammad@hp.com $LOGFILE
	
	exit 1
fi



