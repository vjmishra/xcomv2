HOST_NAME="$(hostname)"
echo $HOST_NAME

 
cd /xpedx/sci_build/smcfs

DATETIME=`date +%Y-%m-%d`

zip -r smcfs-${DATETIME}.zip smcfs

cp smcfs-${DATETIME}.zip /home/share/xpadmin/buildsource

#rm -R smcfs-${DATETIME}.zip
 
echo "SUCCESS!copied"
 
 