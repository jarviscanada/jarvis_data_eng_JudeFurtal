#!/bin/bash

#CLI arguments assigned to variables
psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#bash commands used to parse resource usage data
timestamp=$(date +"%Y-%m-%d %H:%M:%S")
memory_free=$(vmstat --unit M | awk 'FNR == 3 {print $4}')
cpu_idle=$(vmstat --unit M | awk 'FNR == 3 {print $15}')
cpu_kernel=$(vmstat --unit M | awk 'FNR == 3 {print $14}')
disk_io=$(vmstat -d | awk 'FNR == 3 {print $10}')
disk_available=$(df -BM | awk 'FNR == 6 {print $4+0}')

#resource usage data is inserted into the host_usage table
insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
             VALUES ('$timestamp', (SELECT id FROM PUBLIC.host_info), '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

#execution of the insert statement through psql CLI tool
PGPASSWORD=$psql_password psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"

#exit after successful execution of the script
exit 0