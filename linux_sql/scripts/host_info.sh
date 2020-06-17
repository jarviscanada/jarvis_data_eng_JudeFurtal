#!/bin/bash

#CLI arguments assigned to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#output from the lscpu command stored in a variable
lscpu_out=$(lscpu)

#bash commands used to parse hardware specifications
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out"  | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "Model name:" | awk '{print $3 " " $4 " " $5 " " $6 " " $7}' | xargs)
cpu_mhz=$(echo "$lscpu_out"  | egrep "CPU MHz:" | awk '{print $3}' | xargs)
l2_cache=$(echo "$lscpu_out"  | egrep "L2 cache:" | awk '{print $3+0}' | xargs)
total_mem=$(grep MemTotal /proc/meminfo | awk '{print $2}')
timestamp=$(date +"%Y-%m-%d %H:%M:%S")

#hardware specifications are inserted into the host_info table
insert_stmt="INSERT INTO host_info (hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp)
             VALUES ('$hostname', '$cpu_number', '$cpu_architecture', '$cpu_model', '$cpu_mhz', '$l2_cache', '$total_mem', '$timestamp');"

#execution of the insert statement through psql CLI tool
PGPASSWORD=$psql_password psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"

#exit after successful execution of the script
exit 0