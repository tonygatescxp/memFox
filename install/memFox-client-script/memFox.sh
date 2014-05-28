#!/bin/sh
. ~/.bash_profile

getUrl="http://reportsys.uc.local:18080/memFox/servlet/getHostlist?val=type=getHost;group=all"
setUrl="http://reportsys.uc.local:18080/memFox/servlet/getStatus?val=type=updateData;"

strPassParam="pid,version,uptime"

if [ `ps -ef |grep memFox | grep -v $$ | wc -l` -gt 2 ]; then
        exit
fi

curl --connect-timeout 5 --retry 2 -m 5 ${getUrl} -o /tmp/cachelist1.tmp 2>/dev/null

if [ ! -e /tmp/cachelist1.tmp ]; then
	echo "download cache list failed!"
	exit
fi

cat /tmp/cachelist1.tmp | sed 's/},{/}\n{/g' | sed 's/]/\n/g' | sed 's/\[//g' | sed 's/"//g' | sed 's/}//g' | sed 's/{//g' > /tmp/cachelist.tmp
rm -f /tmp/cachelist1.tmp

paramList=""

while read line
do
        hostIP=`echo "$line" | awk -F',' '{print $1}' | awk -F':' '{print $2}'`
        hostPort=`echo "$line" | awk -F',' '{print $3}' | awk -F':' '{print $2}'`
		hostName=`echo "$line" | awk -F',' '{print $4}' | awk -F':' '{print $2}'`
		paramList=""
		echo "Collect memcached info from ${hostIP}:${hostPort}"
		
        echo "stats" | nc -w 3 ${hostIP} ${hostPort} > /dev/shm/memAdmin_curData.tmp 2>&1
		while read memInfo
		do
			pName=`echo ${memInfo} | awk '{print $2}' | sed 's/\n//g' | sed 's/\r//g'`
			pValue=`echo ${memInfo} | awk '{print $3}' | sed 's/\n//g' | sed 's/\r//g'`
			if [ -z "${pName}" -o -z "${pValue}" ]; then
				continue;
			fi
			if [ `echo ${strPassParam} | grep ${pName} | wc -l | awk '{print $1}'` -eq 1 ]; then
				continue;
			fi
			paramList=`echo "${paramList}${pName}:${pValue}'"`
		done < /dev/shm/memAdmin_curData.tmp
		rm -f /dev/shm/memAdmin_curData.tmp
		
		echo "stats slabs" | nc -w 3 ${hostIP} ${hostPort} > /dev/shm/memAdmin_curData.tmp 2>&1
		grep -v ":" /dev/shm/memAdmin_curData.tmp > /dev/shm/memAdmin_curData2.tmp
		while read memInfo
		do
			pName=`echo ${memInfo} | awk '{print $2}' | sed 's/\n//g' | sed 's/\r//g'`
			pValue=`echo ${memInfo} | awk '{print $3}' | sed 's/\n//g' | sed 's/\r//g'`
			if [ -z "${pName}" -o -z "${pValue}" ]; then
				continue;
			fi
			if [ `echo ${strPassParam} | grep ${pName} | wc -l | awk '{print $1}'` -eq 1 ]; then
				continue;
			fi
			paramList=`echo "${paramList}${pName}:${pValue}'"`
		done < /dev/shm/memAdmin_curData2.tmp
		rm -f /dev/shm/memAdmin_curData2.tmp
		
		grep ":" /dev/shm/memAdmin_curData.tmp | awk -F':' '{print $2}' | awk '{data[$1]+=$2}END{ for( key in data ) print key,data[key]}' > /dev/shm/memAdmin_curData2.tmp
		while read memInfo
		do
			pName=`echo ${memInfo} | awk '{print $1}' | sed 's/\n//g' | sed 's/\r//g'`
			pValue=`echo ${memInfo} | awk '{print $2}' | sed 's/\n//g' | sed 's/\r//g'`
			if [ -z "${pName}" -o -z "${pValue}" ]; then
				continue;
			fi
			if [ `echo ${strPassParam} | grep ${pName} | wc -l | awk '{print $1}'` -eq 1 ]; then
				continue;
			fi
			paramList=`echo "${paramList}${pName}:${pValue}'"`
		done < /dev/shm/memAdmin_curData2.tmp
		rm -f /dev/shm/memAdmin_curData2.tmp
		
		wget -T 3 -t 1 "${setUrl}hostIP=${hostIP};hostPort=${hostPort};paramList=${paramList}" -O /dev/null -o /dev/null
		
done < /tmp/cachelist.tmp

rm -f /dev/shm/memAdmin_curData.tmp
rm -f /tmp/cachelist.tmp
