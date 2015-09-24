# Pinpoint Profiler Plugin Sample
You can extend Pinoint's profiling avility by writing a Pinpoint profiler plugin.
This sample project shows how to write it.

# Projects 
Pinpoint profiler plugin sample is consist of these 3 modules

* plugin-sample-target: target library
* plugin-sample-plugin: sample plugin
* plugin-sample-agent: agent distribution with sample plugin

Before jumping into code, you'd better read [basic concepts] of the pinpoint.


# Services
A Pinpoint profiler plugin have to provide implementations of [ProfilerPlugin](https://github.com/naver/pinpoint/blob/master/bootstrap-core/src/main/java/com/navercorp/pinpoint/bootstrap/plugin/ProfilerPlugin.java) and [TraceMetadataProvider](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/TraceMetadataProvider.java)
`ProfilerPlugin` is used by Pinpoint Agent only while `TraceMetadataProvider` is used by Pinpoint Agent, Collector and Web.

Pinpoint loads these implementations by Java's [ServiceLoader](https://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html). So an plugin JAR must contains two provider-configuration files.

* META-INF/services/com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin
* META-INF/services/com.navercorp.pinpoint.common.trace.TraceMetadataProvider 

Each file should contains fully qualified names of the implementation classes.


### TraceMetadataProvider
A TraceMetadataProvider adds [ServiceType](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/ServiceType.java)s and [AnnotationKey](https://github.com/naver/pinpoint/blob/master/commons/src/main/java/com/navercorp/pinpoint/common/trace/AnnotationKey.java)s to Pinpoint.

Both ServiceType and AnnotationKey's code value must be unique. If you're writing a private plugin, you can use code values reserved for private usage. Pinpoint will not assign these values to anything. Otherwise you have to contact Pinpoint dev team to allocate codes for the plugin. 

* ServiceType codes for private use
  * Server: 1900 ~ 1999
  * Client: 9900 ~ 9999
  * Others: 10000 ~ 10999

* AnnotaionKey codes for private use
  * 900 ~ 999


### ProfilerPlugin
A ProfilerPlugin adds [PinpointClassFileTransformer](https://github.com/naver/pinpoint/blob/master/bootstrap-core/src/main/java/com/navercorp/pinpoint/bootstrap/instrument/transformer/PinpointClassFileTransformer.java)s to Pinpoint.

A PinpointClassFileTransformer transforms a target class by adding interceptors, getters and/or fields. You can find example codes in plugin-sample-plugin project.


# Test
