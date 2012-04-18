#!/bin/sh
lib='./bin_ctx'
for jarfile in `ls lib/*.jar`
do
  lib=${lib}:./${jarfile}
done

deamon_log=./logs/deamon.log

echo "`date +'%Y-%m-%d %H:%M:%S'`:start the wapproxy deamon." >> $deamon_log

while [ 1 ];
do
        sleep 5
        ProInfo=`ps aux | grep java | grep ./bin_ctx: | grep -v grep`
        if [ ! -n "$ProInfo" ]
        then
                echo "`date +'%Y-%m-%d %H:%M:%S'`:wapproxy not found" >> $deamon_log
                java -cp ${lib} cn.gzjp.wap.proxy.OnlineProxyServer >>./console.txt 2>&1 &
                echo "`date +'%Y-%m-%d %H:%M:%S'`:wapproxy started." >> $deamon_log
        fi
done
echo "`date +'%Y-%m-%d %H:%M:%S'`:wapproxy deamon end." >> $deamon_log
