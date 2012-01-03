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
#unzip $CURRENTZIPFILE 
nohup /xpedx/ant/apache-ant-1.7.1/bin/ant -f build.xml -logfile /xpedx/sci_build/WebChannel/SWC-Target-`date +"%Y%m%d-%H%M"`.log &
