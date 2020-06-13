#!/bin/bash

lscpu_out=$(lscpu)

#id=1
#echo "$id"

hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "Model name:" | awk '{print $3 " " $4 " " $5 " " $6 " " $7}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "L2 cache:" | awk '{print $3+0}' | xargs)
total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2}')
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp)
             VALUES ('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$total_mem', '$timestamp');"
psql -h localhost -U postgres -d host_agent -c "$insert_stmt"

#echo "$insert_stmt"