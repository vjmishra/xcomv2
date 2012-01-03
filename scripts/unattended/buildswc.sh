#ksh
XPDEXHOME=/xpedx
SWCBUILDHOME=/xpedx/sci_build/WebChannel
DATE=`date +%Y-%m-%d`
CURRENTZIPFILE="WebChannel-${DATE}.zip"
ZIPFILEHOME=/home/share/xpadmin/buildsource

cd $SWCBUILDHOME
cp $ZIPFILEHOME/$CURRENTZIPFILE .
rm -R /xpedx/sci_build/WebChannel/WebChannel 
unzip $CURRENTZIPFILE 
/xpedx/ant/apache-ant-1.7.1/bin/ant -f build.xml -logfile /xpedx/sci_build/WebChannel/SWC-Target-`date +"%Y%m%d-%H%M"`.log &
