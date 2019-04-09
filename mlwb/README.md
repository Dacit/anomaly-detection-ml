# Machine learnign workbench
Split into four parts:
* API
* Data types
* Data access
* Implementation

To use it, build + publish project into mvn repository. Load from maven repo in your Code, then import the following:
```scala
import de.qaware.mlwb.api._
import de.qaware.mlwb.impl.sparksolr.MetricsServiceImpl
```

Connect to solr and spark like this:
```scala
val service = new MetricsServiceImpl.Factory(yourSparkSqlContext).getInstance("zkhost:port", "collection")
```

And fetch data like this:
```scala
service.getMetrics(new QueryMetricContext.Builder().build())
```

The rest of the docu can be found in the API javadoc.