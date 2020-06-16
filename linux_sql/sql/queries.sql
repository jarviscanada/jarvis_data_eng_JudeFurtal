SELECT cpu_number,
       host_id,
       total_mem,
       ROW_NUMBER() OVER (PARTITION BY cpu_number)
FROM host_info
INNER JOIN host_usage ON host_info.id = host_usage.host_id
ORDER BY total_mem DESC;

SELECT host_id,
       hostname,
       date_trunc('hour', host_usage.timestamp) + date_part('minute', host_usage.timestamp)::int / 5 * interval '5 min' AS timestamp,
       ROUND(AVG(CEIL((total_mem - (memory_free*1000))*100::NUMERIC/total_mem))) AS avg_used_mem_percentage
FROM host_info
INNER JOIN host_usage ON host_info.id = host_usage.host_id
GROUP BY host_id, hostname, host_usage.timestamp, total_mem, memory_free;