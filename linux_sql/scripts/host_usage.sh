#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5


timestamp=$(date +"%Y-%m-%d %H:%M:%S")
memory_free=$(vmstat --unit M | awk 'FNR == 3 {print $4}')
cpu_idle=$(vmstat --unit M | awk 'FNR == 3 {print $15}')
cpu_kernel=$(vmstat --unit M | awk 'FNR == 3 {print $14}')
disk_io=$(vmstat -d | awk 'FNR == 3 {print $10}')
disk_available=$(df -BM | awk 'FNR == 6 {print $4+0}')

insert_stmt="INSERT INTO host_usage (timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
             VALUES ('$timestamp', (SELECT id FROM PUBLIC.host_info), '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

#psql -h localhost -U postgres -d host_agent -c "$insert_stmt"
PGPASSWORD=$psql_password psql -h "$psql_host" -p "$psql_port" -d "$db_name" -U "$psql_user" -c "$insert_stmt"
#psql "host=$psql_host port=$psql_port dbname=$db_name user=$psql_user password=$psql_password"