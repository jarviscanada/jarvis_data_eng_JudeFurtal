# Linux Cluster Monitoring Agent

## Introduction

The Linux Cluster Monitoring Agent is a cluster monitoring solution tool that is used by the Jarvis Linux Cluster Administration (LCA) team to monitor cluster resources such as CPU, Memory, etc. The tool is designed to operate on an arbitrary number of nodes/servers in a network, thus making it a scalable system. The monitoring agent records the hardware specifications of each node in the cluster and monitors the node's resource usage data every minute. The data collected from the agents are stored in a Relational Database Management System (RDBMS) database. The data stored the in the database can be used by the LCA team to generate usage reports and plan future resource strategies, such as adding/removing nodes or upgrading hardware on assets. Finally, if the LCA team decides to grow their cluster size, the Linux Cluster Monitoring Agent can be distributed to all the new nodes and can begin collecting data instantly. This shows proves that the Linux Cluster Monitoring Agent is flexible, reliable, and fully scalable.

## Quick Start
- Start a psql instance using psql_docker.sh
```sh
./scripts/psql_docker.sh start|stop|create [db_username][db_password]
```
- Create tables using ddl.sql
```sh
psql -h psql_host -U psql_user -d db_name -f sql/ddl.sql
```
- Insert hardware specs data into the db using host_info.sh
```sh
./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```
- Insert hardware usage data into the db using host_usage.sh
```sh
./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```
- Crontab setup
```sh
#create/edit crontab jobs
bash crontab -e

#add job to crontab
* * * * * bash /home/centos/dev/jrvs/bootcamp/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log
```
## Architecture Diagram

<p align="center">
<img src="https://github.com/jarviscanada/jarvis_data_eng_JudeFurtal/blob/develop/linux_sql/assets/arch_diagram.png">
</p>

The diagram above shows the architecture for the Linux cluster. All the Linux hosts are connected to each other in a network via the network switch and one of the host's contains the database. The agents execute the bash scripts on the individual hosts and the data is sent through the network to the database.

# Database Modeling

The database shown in the architectural diagram is called the `host_agent` and it consists of two tables. The first table is called `host_info` and it contains all the hardware specifications of the host. Since the hardware specifications are assumed to be constant, information in this table is inserted only once. The columns of the `host_info` table are:
- `id`: A unique number assigned to each node. It is auto incremented by the RDBMS and is the table's primary key.
- `hostname`:  It is the full name for each host in the network. Each host name in the table is unique.
- `cpu_number`:  The number of CPU's contained in the host machine.
- `cpu_architecture`: Displays the CPU architecture which specifies the node's operating system's configuration.
- `cpu_model`: Shows the make, model, and processor base frequency for the node's CPU.
- `cpu_mhz`: The clock rate of the CPU. It determines the processing speed of the CPU.
- `L2_cache`: The size of the L2 cache in kB. The cache is used to store data frequently accessed by the CPU.
- `timestamp`: It is the current time in UTC time zone displayed as year-month-day hour:minute:seconds.

The second table is called `host_usage` and it contains all the data pertaining to the host's usage of its resources. The information in this table is inserted each minute as the usage data is always changing. The columns for the `host_usage` table are:
- `timestamp`: It is the current time in UTC time zone obtained from the RDBMS.
- `host_id`: It is the same id found in the host_info table, and it is also the table's foreign key. 
- `memory_free`: The amount of idle memory (in MB) that is not being used.
- `cpu_idle`: The time the CPU spends idle shown as a percentage.
- `cpu_kernel`: The time spent running the kernel code shown as a percentage.
- `disk_io`: The number of disk inputs/outputs (read and write to disk).
- `disk_available`: The available disk space in the root directory shown in MB.

# Script Description

- [psql_docker.sh](https://github.com/jarviscanada/jarvis_data_eng_JudeFurtal/blob/develop/linux_sql/scripts/psql_docker.sh): When the script is executed it allows for three options. The first option is to create a psql docker container with the given username and password. The second option is to start a stopped psql docker container. Finally, the third option allows to stop a running psql docker container.

- `host_info.sh`: This script is executed only once for each node during installation time. It collects all the hardware specifications of the host system and then inserts it into the `host_info` table found the `host_agent` database.

- `host_usage.sh`: This script is executed every minute by the crontab job. It collects all the current resource usage data from the host system and then inserts them into the `host_usage` table found in the `host_agent` database.

- `crontab`: A program used to execute jobs in a specific time/schedule. It is used to run the `host_usage.sh` script every minute.

- `queries.sql`: The LCA team may want to manage the cluster in a more efficient way or they might need to plan for future resources. This can be done effectively by getting data based on which CPUs have the largest memory size, which hosts use the most memory and if there are any potential node failures.

# Usage

1. Database and Table Initialization

Before deploying the agent, a psql docker container must be created to house the PostgreSQL database, this is accomplished by running the psql_docker.sh script. After the psql docker container is created and has been started the database and tables must be created. This is done by running `ddl.sql` using psql.  

```sh
#Create and start a PostgreSQL database instance with docker
./scripts/psql_docker.sh start db_password

#Initialize the database and tables
psql -h localhost -U postgres -d host_agent -f sql/ddl.sql
```
2. host_info.sh Usage

This script will only be executed once for each node in the cluster. The script `host_info.sh` will fetch all the systems hardware specifications and then it will insert this data into the `host_info` table found in the `host_agent` database. 

```sh
#Insert the hardware specifications of the node into the host_info table
./scripts/host_info.sh psql_host psql_port db_name psql_user psql_password
```

3. host_usage.sh Usage

This script gathers the node's current resource usage information and inserts the data into the `host_usage` table found in the `host_agent` database. The usage information can be is inserted into the table manually or it can be automated using crontab.
 
```sh
#Insert the node's current resource usage information into the host_usage table
./scripts/host_usage.sh psql_host psql_port db_name psql_user psql_password
```

4. crontab Setup

In order to repeatedly execute the `host_usage.sh` script every minute, a crontab job must be created. The following steps shows how to create and verify the crontab job.

```sh
#create/edit crontab jobs
crontab -e

#add the crontab job to automate the host_usage.sh script
* * * * * bash /home/centos/dev/jrvs/bootcamp/linux_sql/host_agent/scripts/host_usage.sh localhost 5432 host_agent postgres password > /tmp/host_usage.log

#list all current crontab jobs
crontab -l

#validate that the crontab job executed the host_usage.sh script by seeing the log file 
cat /tmp/host_usage.log
```
5. queries.sql Usage

To obtain certain data such as which CPUs have the largest memory size or which host has the most free memory, can be accomplished by running the `queries.sql` using psql. Information from these queries can be used for cluster management and future resource planning.

```sh
#Use psql to execute the queries.sql file to obtain cluster data
psql -h localhost -U postgres -d host_agent -f sql/queries.sql
```

# Improvements

1. The first improvement would be to have the system send notifications/warnings when the node's resource usage passes certain thresholds.
 
2. The second improvement would be to edit the `queries.sql` file from calculating the average used memory in percentage over 5-minute intervals to 1-minute intervals. This will allow the date to become more widespread.

3. The third improvement would be to have the tables in the database contain more information such as the number of processes in uninterruptible sleep, the number of interrupts per second, the CPU time stolen from a virtual machine etc.

