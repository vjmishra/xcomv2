#!/bin/sh
echo Calling setupfiles.sh from INSTALL_DIR/bin folder
cd /xpedx/sterling/Foundation/bin
pwd

alreadyIncluded="false"
export alreadyIncluded
. ./setupfiles.sh
