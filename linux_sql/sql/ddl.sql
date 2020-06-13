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

--INSERT INTO host_info (id, hostname, cpu_number, cpu_architecture, cpu_model, cpu_mhz, l2_cache, total_mem, timestamp)
--VALUES (DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);

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

--INSERT INTO host_usage ("timestamp", host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available)
--VALUES (DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT, DEFAULT);