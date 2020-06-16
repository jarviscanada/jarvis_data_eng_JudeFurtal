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

![Alt](https://github.com/jarviscanada/jarvis_data_eng_JudeFurtal/blob/feature/README/linux_sql/assets/arch_diagram.png)

