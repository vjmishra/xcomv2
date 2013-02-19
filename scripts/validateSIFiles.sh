HOST_NAME="$(hostname)"

DATE=`date +%Y%m%d`

LOCALFOLDER=/xpedx/sterling/searchindex/SearchIndex/xpedx/xpedx/MasterCatalog/CatalogIndex_$DATE*
#LOCALFOLDER=/xpedx/sterling/searchindex/SearchIndex/xpedx/xpedx/MasterCatalog/CatalogIndex_20130206*
cd /xpedx/sterling/searchindex/SearchIndex/xpedx/xpedx/MasterCatalog
CATALOGFOLDER=CatalogIndex_$DATE*

echo $CATALOGFOLDER
if [ -d $CATALOGFOLDER ]; then
echo "on server" $HOST_NAME "Directory Name" $LOCALFOLDER 

cd $LOCALFOLDER
ls -ltr

#echo 'ls -ltr'
echo "on xpapp01/02 server" $LOCALFOLDER/en_US 
cd $LOCALFOLDER/en_US
ls -ltr

#echo 'ls -ltr'

else

echo " no File/Folder on server" $HOST_NAME "Please check on server"

fi