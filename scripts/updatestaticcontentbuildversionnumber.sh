#!/bin/ksh
ENVIRONMENT
# First find the last build version#
HOST_NAME="$(hostname)"
echo $HOST_NAME
# First find the last build version# and store it 
case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappmc01) ENVIRONMENT=dev;; 
 zxpappt01) ENVIRONMENT=staging;; 
 zxpapps01) ENVIRONMENT=prodsupport;; 
 zxpagnt01) ENVIRONMENT=prod;; 
 zxpagnt02) ENVIRONMENT=prod;; 
 *);;
esac

UpdateBuildVersion()
{
while read myline
do
	property="`echo $myline | cut -d= -f1`"
	lastbuildversion="`echo $myline | cut -d= -f2`"
	case "$property" in
	  "yfs.xpedxBuildKey") 
		echo "$property = $lastbuildversion" 	  
		echo "lastbuildversion = $lastbuildversion"
		#Next, replace the last build version with a new one. `date +"%Y%m%d-%H%M"`
		newbuildversion=-cb`date +"%Y%m%d%H%M"`
		#echo "about to set newbuildversion to $newbuildversion"
		#update the old build version to the new build version number 
		find $1 -type f | xargs perl -pi -e "s/$lastbuildversion/$newbuildversion/g"
		#replace all files with the new build version
		#find $1 -type f | xargs perl -pi -e "s/$lastbuildversion/$newbuildversion/g"
	  ;;	   
	  #*)         echo "else" ;;
	esac
  #echo "$property = $lastbuildversion"
done < $2
}
#Param1: directory to search for files where we're replacing the old build version with a new one, 
#Param2: property files storing the last/current build version number.
#DirectoryToProcess=/xpedx/tmp/staticcontent/
#BuildVersionPropertyFile=/xpedx/tmp/staticcontent/customer_overrides.properties
#echo "before search/replace" 
#cat /xpedx/tmp/staticcontent/customer_overrides.properties | grep yfs.xpedxBuildKey
#UpdateBuildVersion $DirectoryToProcess $BuildVersionPropertyFile
#echo "after search/replace" 
#cat /xpedx/tmp/staticcontent/customer_overrides.properties | grep yfs.xpedxBuildKey

DirectoryToProcess=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties/
BuildVersionPropertyFile=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties/customer_overrides_$ENVIRONMENT.properties
echo "before search/replace of yfs.xpedxBuildKey" 
cat /xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties/customer_overrides_$ENVIRONMENT.properties | grep yfs.xpedxBuildKey
UpdateBuildVersion $DirectoryToProcess $BuildVersionPropertyFile
echo "after search/replace of yfs.xpedxBuildKey" 
cat /xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties/customer_overrides_$ENVIRONMENT.properties | grep yfs.xpedxBuildKey

#copy the newly modified proprety file to the bin folder
cp /xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties/customer_overrides_$ENVIRONMENT.properties /xpedx/sterling/Foundation/properties/customer_overrides.properties

echo "Completed processing global search and replace of old build version number."