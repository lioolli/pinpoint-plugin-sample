# Pinpoint Profiler Plugin Sample
You can extend Pinoint's profiling avility by writing a Pinpoint profiler plugin.
This sample project shows how to write it.

Before jumping into code, you'd better read [basic concepts] of the pinpoint.

# Plugin Structure
A Pinpoint profiler plugin have to provide an implementation of [ProfilerPlugin](https://github.com/naver/pinpoint/blob/master/bootstrap-core/src/main/java/com/navercorp/pinpoint/bootstrap/plugin/ProfilerPlugin.java) and [TraceMetadataProvider](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/TraceMetadataProvider.java)
`ProfilerPlugin` is used by Pinpoint Agent only while `TraceMetadataProvider` is used by Pinpoint Agent, Collector and Web.

Pinpoint loads these implementations by Java's [ServiceLoader](https://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html). So you have to write provider-configuration files.

* META-INF/services/com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin
* META-INF/services/com.navercorp.pinpoint.common.trace.TraceMetadataProvider 

Each file should contains fully qualified name of implementation class.

