logfile=proxy
extname=.log
dest_path=/data/services/nginx/logs
src_path=/data/services/nginx/logs
hour=`perl -e 'use POSIX;print strftime("%Y%m%d%H",localtime(time-3600));'`
month=`perl -e 'use POSIX;print strftime("%Y%m",localtime(time-3600));'`
if [ ! -d $dest_path/$month ];then
mkdir $dest_path/$month
fi
destname=$dest_path/$month/$logfile$hour$extname
mv $src_path/$logfile$extname $destname
kill -USR1 `cat /data/services/nginx/logs/nginx.pid`
gzip $destname
