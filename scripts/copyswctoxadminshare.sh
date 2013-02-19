HOST_NAME="$(hostname)"
echo $HOST_NAME



#WebChannel in file (checkoutswclatestcode.sh)

cd /xpedx/sci_build/WebChannel

DATETIME=`date +%Y-%m-%d`

zip -r WebChannel-${DATETIME}.zip WebChannel

cp WebChannel-${DATETIME}.zip /home/share/xpadmin/buildsource

#rm -R WebChannel-${DATETIME}.zip
echo "SUCCESS!copied"
