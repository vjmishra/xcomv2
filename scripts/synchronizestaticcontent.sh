#!ksh

HOST_NAME="$(hostname)"
echo $HOST_NAME

if [ $# -ne 1 ]; then
          echo "Please supply a valid date argument. For example: '2012-07-01 00:00'"
		  return 1
    fi
if [[ "$HOST_NAME" != "zxpappd01" ]];then
	echo "You can't run this script on any other server than xpappd01. Please be aware of the consequences of publishing static content from the wrong server to stg/production..."
	exit 1
fi
#create variable to store date to specify which files to upload (i.e since when the files have changed)
#ex: '2012-07-01 00:00'
FilesUpdatedSince=$1
echo "Synchronizing static content between dev server and mypublisher dev share...."
echo "Note: There are about 4000 files and 500 directories that will be scanned for changes. This may take a while. Please stand by...."

#/xpedx/python/Python-2.7.3/bin/python2.7 /home/share/xpadmin/scripts/ftpclient.py -c '2012-07-01 00:00' -f ".jsp,.java,.txt,.py" /xpedx/sci_build/WebChannel/WebChannel/main/webapp/swc /
 
echo "processing /xpedx/sci_build/WebChannel/WebChannel/main/webapp/commonImages..."
/xpedx/python/Python-2.7.3/bin/python2.7 /home/share/xpadmin/scripts/ftpclient.py -c "$FilesUpdatedSince" -f ".jsp,.java,.txt,.py" /xpedx/sci_build/WebChannel/WebChannel/main/webapp/commonImages /
echo " processing /xpedx/sci_build/WebChannel/WebChannel/main/webapp/swc ..."
/xpedx/python/Python-2.7.3/bin/python2.7 /home/share/xpadmin/scripts/ftpclient.py -c "$FilesUpdatedSince" -f ".jsp,.java,.txt,.py" /xpedx/sci_build/WebChannel/WebChannel/main/webapp/swc /
echo "processing /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedxCanada ..."
/xpedx/python/Python-2.7.3/bin/python2.7 /home/share/xpadmin/scripts/ftpclient.py -c "$FilesUpdatedSince" -f ".jsp,.java,.txt,.py" /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedxCanada /
echo "processing /xpedx/sci_build/WebChannel/WebChannel/main/webapp/BulkleyDunton ..."
/xpedx/python/Python-2.7.3/bin/python2.7 /home/share/xpadmin/scripts/ftpclient.py -c "$FilesUpdatedSince" -f ".jsp,.java,.txt,.py" /xpedx/sci_build/WebChannel/WebChannel/main/webapp/BulkleyDunton /
echo "processing /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx ..."
/xpedx/python/Python-2.7.3/bin/python2.7 /home/share/xpadmin/scripts/ftpclient.py -c "$FilesUpdatedSince" -f ".jsp,.java,.txt,.py" /xpedx/sci_build/WebChannel/WebChannel/main/webapp/xpedx /

echo "Finished Synchronizing static content ...."
