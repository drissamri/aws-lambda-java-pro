
- Build: `./mvnw clean package`
- Deploy: `sh bin/deploy.sh`
- Load test: `sh bin/load.sh`

```
filter @type = "REPORT"
| stats
 count(*) as invocations,
 pct(@duration, 50) as p50, pct(@duration, 99) as p99, pct(@duration, 100) as p100,
 pct(@initDuration+@duration, 50) as initP50, pct(@initDuration+@duration, 99) as initP99, pct(@initDuration+@duration, 100) as initP100
 group by @log, ispresent(@initDuration) as coldstart
 | sort by @log, coldstart
```
p99: 1,7s

```  
{
  "functionName": "favorites-api-AddFavoriteLambda-QWiDINNwGiM2",
  "memorySize": 2048,
  "coldStarts": 82,
  "min": 1593.53,
  "p25": 1683.06,
  "median": 1719.18,
  "p75": 1739.2,
  "p95": 1776.18,
  "max": 1792.71,
  "stddev": 43.4565
}
```