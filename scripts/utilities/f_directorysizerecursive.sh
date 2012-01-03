#!/bin/bash

f_directorysizerecursive()
{
#remove old log file
rm -R /home/share/xpadmin/scripts/utilities/directorysizelisting.log
cd $1
#sh
for size in `ls`
do
echo "" >> /home/share/xpadmin/scripts/utilities/directorysizelisting.log
du -h $size >> /home/share/xpadmin/scripts/utilities/directorysizelisting.log
echo $size done >> /home/share/xpadmin/scripts/utilities/directorysizelisting.log
echo $size done
echo "" >> /home/share/xpadmin/scripts/utilities/directorysizelisting.log

#done is the end of for loop
done 

}

Directory=/xpedx/sterling
f_directorysizerecursive $Directory