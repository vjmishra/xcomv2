#!/bin/ksh

# Note: this script is only a portion of the search index purge process. The other part is executed on the agent servers via the /home/share/xpadmin/scripts/purgesearchindexdb.sh. 
echo "about to delete old seach index physical folders which are older than 60 days..."
find /xpedx/sterling/searchindex/SearchIndex/xpedx/xpedx/MasterCatalog -type d -name "CatalogIndex_*" -mtime +60 -exec rm -R {} \;