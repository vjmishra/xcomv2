#!/bin/sh

################################################################
#Copy this script to your path from where you want to search for all the files and directories in subdirectories recursively.
# JIRA XNGTP-69 - C.Ctr: Staging, Hyperlinks - Custom Links fail to activate until 2nd click
# Added .java files to be processed by this script.
#################################################################

recur_dos2unix()
{

#
# read all file and directory names in the current directory
#
for dirname in `ls`
do

#
# if this is directory
#
if [ -d "$dirname" ]; then

        #echo "the directory name is $dirname"
        cd $dirname
        recur_dos2unix
        cd ..
fi

#
# if this is a plain file then execute command
#
if [ -f "$dirname" ]; then

        curdir=`pwd`
        
        case "$dirname" in
                *.xml ) /usr/bin/dos2unix $curdir/$dirname;;
                *.xsl ) /usr/bin/dos2unix $curdir/$dirname;;
                *.ywx ) /usr/bin/dos2unix $curdir/$dirname;;
                *.yuix ) /usr/bin/dos2unix $curdir/$dirname;;
                *.ycml ) /usr/bin/dos2unix $curdir/$dirname;;
                *.ythm ) /usr/bin/dos2unix $curdir/$dirname;;
                *.properties ) /usr/bin/dos2unix $curdir/$dirname;;
                *.MF ) /usr/bin/dos2unix $curdir/$dirname;;
                *.java ) /usr/bin/dos2unix $curdir/$dirname;;
                *.jsp ) /usr/bin/dos2unix $curdir/$dirname;;
                * ) ;;
        esac
        
        #echo "the file name inside is $curdir/$dirname"
fi

done
} 
recur_dos2unix
