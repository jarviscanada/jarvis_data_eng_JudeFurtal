#!/bin/bash

#setup arguments
cnt_act=$1 #short for container action
db_username=$2
db_password=$3

#start docker if it is not running
systemctl status docker || systemctl start docker

#check to see if the create container action was chosen
if [ "$cnt_act" == "create" ]; then

  line_count=$(docker container ls -a -f name=jrvs-psql | wc -l)

  #check to see if container already exists
  if [ "$line_count" -eq 2 ]; then
    echo "Error: container is already created"
    echo "Usage: start or stop container using, ./scripts/psql_docker.sh start or ./scripts/psql_docker.sh stop"
    exit 1
  fi

  #check to see if username or password for the database is missing
  if [ -z "$db_username" ]||[ -z "$db_password" ]; then
    echo "Error: database username or password is not passed through CLI arguments"
    echo "Usage: pass username and password as follows, ./scripts/psql_docker.sh create [db_username] [db_password]"
    exit 1
  fi

  #create the container
  docker volume create pgdata
  docker run --name jrvs-psql -e POSTGRES_PASSWORD="${db_password}" -e POSTGRES_USER="${db_username}" -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
  exit $?

  #container was not created
  if [ "$line_count" -ne 2 ]; then
    echo "Error: container is not created"
    echo "Usage: run the following to create the container, ./scripts/psql_docker.sh create [db_username] [db_password]"
    exit 1
  fi

#check to see if the start container action was chosen
elif [ "$cnt_act" == "start" ]; then
  if [ "$line_count" -ne 2 ]; then
    echo "Error: cannot start because the container is not created"
    exit 1
  else
    docker container start jrvs-psql
    exit $?
  fi

#check to see if the stop container action was chosen
elif [ "$cnt_act" == "stop" ]; then
  docker container stop jrvs-psql
  exit $?

#invalid action for container
else
  echo "Error: invalid action for container"
  echo "Usage: instead execute, ./scripts/psql_docker.sh start|stop|create [db_username][db_password]"
  exit 1
fi