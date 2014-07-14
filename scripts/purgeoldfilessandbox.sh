#!/bin/sh 
#  
#=============================================================================
#PURGE LOG FILES
#=============================================================================
# THIS SCRIPT IS USED TO DELETE FILES IN THE BELOW FOLDERS TO KEEP DISK SPACE USAGE FROM REACHING 100% CAPACITY
# 
# NOTE THAT THE RETENTION POLICY IS DIFFERENT BETWEEN DEV, STG, and PROD
#
#1- Foundation\Logs
#2- Foundation\bin\backups
#3- Foundation\tmp
#4- Ear files (/xpedx/deployments/archive)
#5-/xpedx/sci_build/smcfs/
#6-/xpedx/sci_build/WebChannel/
#7-/xpedx/sci_build/CDT/
#8-/xpedx/logfiles/swcdev/
#9-/xpedx/logfiles/smcfsdev/

#specify log file to output to.
#mkdir /xpedx/logfiles/purgelogs/
#chmod 775 /xpedx/logfiles/purgelogs/

LOGFILENAME=/xpedx/logfiles/purgelogs/purgeoutput_sandbox_`date +"%Y%m%d-%H%M"`.log
#Set default retention period value
DaysToRetain=7

echo "Started purging log files"
echo "Disk space before purge "
df -h
#declare reusable function
# Parameters: Directory, searchPattern, DaysToRetain,  $LOGFILENAME
purgeoldfiles() 
{ 
	# deletes files specified in arg1 that are older than number of days specified in arg3
	echo ""
	echo "Function called with arguments: " $@	
	echo "" >> $4
	
	echo "changing directory to $1"
	echo "changing directory to $1" >> $4
	cd $1
	pwd
	
	echo "purging files older than $3 days from $1" 
	echo "purging files older than $3 days from $1" >> $4 
	
	echo "PROCESSING FILES IN DIRECTORY: $1"
	echo "PROCESSING FILES IN DIRECTORY: $1" >> $4
		
	find $2 -type f -mtime +$3 -print >> $4
	
	#Remove the files in above command
	find $2 -type f -mtime +$3 -exec rm {} \; >> $4
	
	#Shows files older than 7 days  
	find $2 -type f -mtime +$3 -print >> $4
  	echo "" >> $4
	echo "Finished deleting files older than $3 days from $1"
  	echo "Finished deleting files older than $3 days from $1" >> $4
		
}

purgeoldfiles_StarPattern() 
{ 
 
	# deletes files specified in arg1 that are older than number of days specified in arg3
	echo ""
	echo "Function called with arguments: " $@	
	echo "" >> $4
	
	echo "changing directory to $1"
	echo "changing directory to $1" >> $4
	cd $1
	pwd
	
	echo "purging files older than $3 days from $1" 
	echo "purging files older than $3 days from $1" >> $4 
	
	echo "PROCESSING FILES IN DIRECTORY: $1"
	echo "PROCESSING FILES IN DIRECTORY: $1" >> $4
		
	find *.$2* -type f -mtime +$3 -print >> $4
	
	#Remove the files in above command
	find *.$2* -type f -mtime +$3 -exec rm {} \; >> $4
	
	#Shows files older than 7 days  
	find *.$2* -type f -mtime +$3 -print >> $4
  	echo "" >> $4
	echo "Finished deleting files older than $3 days from $1"
  	echo "Finished deleting files older than $3 days from $1" >> $4
		
}

purgeoldfiles_StarPatternStar() 
{ 
 
	# deletes files specified in arg1 that are older than number of days specified in arg3
	echo ""
	echo "Function called with arguments: " $@	
	echo "" >> $4
	
	echo "changing directory to $1"
	echo "changing directory to $1" >> $4
	cd $1
	pwd
	
	echo "purging files older than $3 days from $1" 
	echo "purging files older than $3 days from $1" >> $4 
	
	echo "PROCESSING FILES IN DIRECTORY: $1"
	echo "PROCESSING FILES IN DIRECTORY: $1" >> $4
		
	find *$2* -type f -mtime +$3 -print >> $4
	
	#Remove the files in above command
	find *$2* -type f -mtime +$3 -exec rm {} \; >> $4
	
	#Shows files older than 7 days  
	find *$2* -type f -mtime +$3 -print >> $4
  	echo "" >> $4
	echo "Finished deleting files older than $3 days from $1"
  	echo "Finished deleting files older than $3 days from $1" >> $4
		
}


#1.2 PROCESS /xpedx/sterling/Foundation/logs  sci.log.* and agentserver.log.* folder/files. these take up too much space on staging.
# Per Winston Edwards, the sci.log.* and agentserver.log.* files are duplicates of the logs created by log4J in the stdout log file. So there's no need to retain them for longer than 1 day to save on space. 
Directory=/xpedx/sterling/Foundation/logs
SearchPattern=log
DaysToRetain=1
purgeoldfiles_StarPattern $Directory $SearchPattern $DaysToRetain $LOGFILENAME

Directory=/xpedx/sterling/Foundation/logs
SearchPattern=/xpedx/sterling/Foundation/logs
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME
 

#2) PROCESS /xpedx/sterling/Foundation/bin/backups FOLDER
Directory=/xpedx/sterling/Foundation/bin/backups
SearchPattern=/xpedx/sterling/Foundation/bin/backups
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME

#4) PROCESS /xpedx/deployments/archive FOLDER
Directory=/xpedx/deployments/archive
SearchPattern=/xpedx/deployments/archive
DaysToRetain=15
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME
 
#5) PROCESS /xpedx/sci_build/smcfs FOLDER
#5.1- delete *.log files older than specified days 
Directory=/xpedx/sci_build/smcfs
SearchPattern=log
DaysToRetain=30
purgeoldfiles_StarPattern $Directory $SearchPattern $DaysToRetain $LOGFILENAME

#5.2- delete *.zipfiles older than than specified days 
Directory=/xpedx/sci_build/smcfs
SearchPattern=zip
DaysToRetain=30
purgeoldfiles_StarPattern $Directory $SearchPattern $DaysToRetain $LOGFILENAME
 

#6) PROCESS /xpedx/sci_build/WebChannel/ FOLDER
#6.1- Shows .log files older than 7 days and pipes it to more.
Directory=/xpedx/sci_build/WebChannel/
SearchPattern=log
DaysToRetain=30
purgeoldfiles_StarPattern $Directory $SearchPattern $DaysToRetain $LOGFILENAME

Directory=/xpedx/sci_build/WebChannel/
SearchPattern=zip
DaysToRetain=30
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME


#7) PROCESS  FOLDER /xpedx/sci_build/CDT/*.log
#7.1) PROCESS   *.log
Directory=/xpedx/sci_build/CDT/
SearchPattern=log
DaysToRetain=30
purgeoldfiles_StarPattern $Directory $SearchPattern $DaysToRetain $LOGFILENAME

#7.2) PROCESS   *.zip
Directory=/xpedx/sci_build/CDT/
SearchPattern=zip
DaysToRetain=30
purgeoldfiles_StarPattern $Directory $SearchPattern $DaysToRetain $LOGFILENAME


#) PROCESS  /xpedx/logfiles/smcfsdev/*.log*
Directory=/xpedx/logfiles/smcfsdev/
SearchPattern=log
DaysToRetain=15
purgeoldfiles_StarPattern $Directory $SearchPattern $DaysToRetain $LOGFILENAME
 
 #) PROCESS  /xpedx/logfiles/swcstg/*.log*
Directory='/xpedx/logfiles/swcdev/'
SearchPattern=log
DaysToRetain=15
purgeoldfiles_StarPattern $Directory $SearchPattern $DaysToRetain $LOGFILENAME

#) PROCESS  OTHER BACKUP FOLDERS
Directory=/xpedx/sterling/Foundation/resources/ydkresources/backups
SearchPattern=/xpedx/sterling/Foundation/resources/ydkresources/backups
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME
 
Directory=/xpedx/sterling/Foundation/resources/backups 
SearchPattern=/xpedx/sterling/Foundation/resources/backups
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME
 
 #) PROCESS  FOLDER
Directory=/xpedx/sterling/Foundation/properties/backups
SearchPattern=/xpedx/sterling/Foundation/properties/backups
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME

Directory=/xpedx/sterling/Foundation/repository/scripts/backups
SearchPattern=/xpedx/sterling/Foundation/repository/scripts/backups
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME

Directory=/xpedx/sterling/Foundation/repository/factorysetup/platform_afc/XMLS/backups
SearchPattern=/xpedx/sterling/Foundation/repository/factorysetup/platform_afc/XMLS/backups
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME

Directory=/xpedx/sterling/Foundation/bin/schema/backups 
SearchPattern=/xpedx/sterling/Foundation/bin/schema/backups 
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME

# purge the output from this shell script file. 
Directory=/home/share/xpadmin/purgelogs 
SearchPattern=/home/share/xpadmin/purgelogs
DaysToRetain=5
purgeoldfiles $Directory $SearchPattern $DaysToRetain $LOGFILENAME

# purge the heap dump files > 100MB that are older than 7 days in the Foundation directory
find /xpedx/sterling/Foundation/ -name "*.hprof" -type f -size +100000000c -mtime +7 -exec rm {} \; 

# purge the heap dump files > 1GB that are older than 3 days in the Foundation directory
find /xpedx/sterling/Foundation/ -name "*.hprof" -type f -size +1000000000c -mtime +3 -exec rm {} \; 

# purge the heap dump files > 100MB that are older than 3 days in the wldomain directory
find /xpedx/wldomain/ -name "*.hprof" -type f -size +100000000c -mtime +7 -exec rm {} \; 

# purge the heap dump files > 1GB that are older than 3 days in the wldomain directory
find /xpedx/wldomain/ -name "*.hprof" -type f -size +1000000000c -mtime +3 -exec rm {} \; 

# purge the core files > 1GB that are older than 3 days in the Foundation directory
find /xpedx/sterling/Foundation/ -name "*core" -type f -size +1000000000c -mtime +3 -exec rm {} \; 

# purge the core files > 1GB that are older than 3 days in the wldomain directory
find /xpedx/wldomain/ -name "*core" -type f -size +1000000000c -mtime +3 -exec rm {} \; 

# purge the verbose  files > 100MB that are older than 7 days in the Foundation directory
find /xpedx/sterling/Foundation/ -name "*_VERBOSE_*" -type f -size +100000000c -mtime +7 -exec rm {} \; 

# purge the verbose files > 1GB that are older than 3 days in the Foundation directory
find /xpedx/sterling/Foundation/ -name "*_VERBOSE_*" -type f -size +1000000000c -mtime +3 -exec rm {} \; 

# purge the verbose  files > 100MB that are older than 7 days in the wldomain directory
find /xpedx/wldomain/ -name "*_VERBOSE_*" -type f -size +100000000c -mtime +7 -exec rm {} \; 

# purge the verbose files > 1GB that are older than 3 days in the wldomain directory
find /xpedx/wldomain/ -name "*_VERBOSE_*" -type f -size +1000000000c -mtime +3 -exec rm {} \; 

#find and purge old cent log files
find /xpedx/logfiles/cent/ -type f -mtime +30 -exec rm {} \; 

echo "Completed purging log files"
echo "Disk space after purge "
df -h
echo "The next cron schedule will run as below: "  
crontab -l
