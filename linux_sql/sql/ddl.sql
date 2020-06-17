--Create the host_info table to hold the host's hardware specifications
CREATE TABLE IF NOT EXISTS PUBLIC.host_info
  (
     id               SERIAL PRIMARY KEY NOT NULL,
     hostname         VARCHAR UNIQUE NOT NULL,
     cpu_number       INT NOT NULL,
     cpu_architecture VARCHAR NOT NULL,
     cpu_model        VARCHAR NOT NULL,
     cpu_mhz          NUMERIC (7,3) NOT NULL,
     l2_cache         INT NOT NULL,
     total_mem        INT NOT NULL,
     timestamp        VARCHAR NOT NULL
  );

--Create the host_usage table to hold the host's resource usage data
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage
  (
     "timestamp"    TIMESTAMP NOT NULL,
     host_id        SERIAL NOT NULL,
     memory_free    INT NOT NULL,
     cpu_idle       INT NOT NULL,
     cpu_kernel     INT NOT NULL,
     disk_io        INT NOT NULL,
     disk_available INT NOT NULL,
     FOREIGN KEY (host_id) REFERENCES host_info (id)
  );