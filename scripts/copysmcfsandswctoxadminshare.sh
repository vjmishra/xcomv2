HOST_NAME="$(hostname)"
echo $HOST_NAME



cd /xpedx/sci_build/smcfs

DATETIME=`date +%Y-%m-%d`

zip -r smcfs-${DATETIME}.zip smcfs

cp smcfs-${DATETIME}.zip /home/share/xpadmin/buildsource

#rm -R smcfs-${DATETIME}.zip

#WebChannel in file (checkoutswclatestcode.sh)

cd /xpedx/sci_build/WebChannel

DATETIME=`date +%Y-%m-%d`

zip -r WebChannel-${DATETIME}.zip WebChannel

cp WebChannel-${DATETIME}.zip /home/share/xpadmin/buildsource

#rm -R WebChannel-${DATETIME}.zip
echo "SUCCESS!copied"
