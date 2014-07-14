#ksh
DATE=`date +%Y-%m-%d`
HOST_NAME="$(hostname)"
echo "Starting smcfs on $HOST_NAME"
#PROP_DIR=/xpedx/sci_build/smcfs/smcfs/dev/Foundation/properties

case "$HOST_NAME" in
 zxpappd01) ENVIRONMENT=dev;;
 zxpappd02) ENVIRONMENT=dev;;
 zxpappt01) ENVIRONMENT=stg;; 
 zxpapps01) ENVIRONMENT=ps;; 
 zxpagnt01) ENVIRONMENT=prd;; 
 zxpagnt02) ENVIRONMENT=prd;; 
 zxpappmc01) ENVIRONMENT=mc;; 
 *);;
esac


#
#./stopsmcfs$ENVIRONMENT.sh

#start the order updates 
cd /home/share/xpadmin/scripts/
ksh -x stoporderupdates.sh
sleep 180
cd /xpedx/scripts
./stopsmcfs$ENVIRONMENT.sh

