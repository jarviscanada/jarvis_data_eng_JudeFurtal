# Linux Cluster Monitoring Agent

## Introduction

The Linux Cluster Monitoring Agent is a cluster monitoring solution tool that is used by the Jarvis Linux Cluster Administration (LCA) team to monitor cluster resources such as CPU, Memory, etc. The tool is designed to operate on an arbitrary number of nodes/servers in a network, thus making it a scalable system. The monitoring agent records the hardware specifications of each node in the cluster and monitors the node's resource usage data every minute. The data collected from the agents are stored in a Relational Database Management System (RDBMS) database. The data stored the in the database can be used by the LCA team to generate usage reports and plan future resource strategies, such as adding/removing nodes or upgrading hardware on assets. Finally, if the LCA team decides to grow their cluster size, the Linux Cluster Monitoring Agent can be distributed to all the new nodes and can begin collecting data instantly. This shows proves that the Linux Cluster Monitoring Agent is flexible, reliable, and fully scalable.

## Quick Start
-Start a psql instance using psql_docker.sh
```sh
./scripts/psql_docker.sh
```


