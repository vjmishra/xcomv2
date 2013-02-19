#!/bin/ksh
 
HOST_NAME="$(hostname)"
echo "Starting smcfs on $HOST_NAME"
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

cd /xpedx/java/

pwd
cp /home/share/xpadmin/install_files/java/jdk-6u37-solaris-sparc-32bit.sh .
cp /home/share/xpadmin/install_files/java/jdk-6u37-solaris-sparcv9-64bit.sh .
chmod +x jdk-6u37-solaris-sparc-32bit.sh
chmod +x jdk-6u37-solaris-sparcv9-64bit.sh

# first install the 32bit JDK (as per the documentation)
#./jdk-6u37-solaris-sparc-32bit.sh 

#now install the new 64bit version
#./jdk-6u37-solaris-sparc9-64bit.sh