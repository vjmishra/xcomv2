#!/bin/ksh

CLASSPATH=/xpedx/weblogic/wlserver_10.3/server/lib/ojdbc6.jar;export CLASSPATH

PATH=/xpedx/java/jdk1.6.0_37/bin;export PATH

javac com/xpedx/nextgen/common/util/*.java

cd ..

java -cp itemload com.xpedx.nextgen.common.util.XPXItemLoadProcessor "$@"

