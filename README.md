
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