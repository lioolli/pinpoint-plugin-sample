# Pinpoint Profiler Plugin Sample
You can extend Pinoint's profiling avility by writing a Pinpoint profiler plugin.
This sample project shows how to write it.

# Projects 
Pinpoint profiler plugin sample is consist of these 3 modules

* plugin-sample-target: target library
* plugin-sample-plugin: sample plugin
* plugin-sample-agent: agent distribution with sample plugin

Before jumping into code, you'd better read [basic concepts] of the pinpoint.


# Plugin Services
A Pinpoint profiler plugin have to provide implementations of [ProfilerPlugin](https://github.com/naver/pinpoint/blob/master/bootstrap-core/src/main/java/com/navercorp/pinpoint/bootstrap/plugin/ProfilerPlugin.java) and [TraceMetadataProvider](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/TraceMetadataProvider.java)
`ProfilerPlugin` is used by Pinpoint Agent only while `TraceMetadataProvider` is used by Pinpoint Agent, Collector and Web.

Pinpoint loads these implementations by Java's [ServiceLoader](https://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html). So you have to write provider-configuration files.

* META-INF/services/com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin
* META-INF/services/com.navercorp.pinpoint.common.trace.TraceMetadataProvider 

Each file should contains fully qualified names of implementation classes.


### TraceMetadataProvider
A TraceMetadataProvider adds [ServiceType](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/ServiceType.java) and [AnnotationKey](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/AnnotationKey.java) to Pinpoint.

Both ServiceType and AnnotationKey's code value must be unique. If you're writing a private plugin, You can use sandbox code values which is reserved for private usage. Pinpoint will not assign these sandbox values to anything. Otherwise you have to contact Pinpoint dev team to allocate codes for the plugin. 
