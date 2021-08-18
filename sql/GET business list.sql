--- the limit and id will be placed on route parameters,
--- something like business?q=20&n=40,
--- q is the starting point, n is how many rows
--- taking from the most recently registered business
SELECT * FROM BUSINESS_T b
JOIN BUS_REPORT_T br ON
b.bus_id = br.bus_id
JOIN BUS_SHARE_T bs ON
b.bus_id = bs.bus_id
ORDER BY b.bus_id DESC
LIMIT n OFFSET q

--- the fields needed for this request:
--- bus_id, bus_name, bus_type, the logo, bs.bus_share_phase, br.current_fund
--- bus amount needed in current phase (8% & 32% )