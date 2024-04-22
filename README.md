# Adding a Span Processor to the OpenTelemetry Java instrumentation

This turns out to be harder than in Python or Node. You don't get to change code in initialization inside your app, because the OpenTelemetry agent is loaded separately from your app.

## Developing a Span Processor

Here's the process I followed:

I copied from https://github.com/open-telemetry/opentelemetry-java-instrumentation/tree/746a7a667c2f0655fafa65bda07aabdf65e3a3a3/examples/extension#readme

and then deleted most of it, but not TOO much.

Here is what I did to the code:

- deleted all the tests because they don't work
- deleted the spotless stuff from build.gradle because it got in the way

- deleted most of the examples; what I want is the SpanProcessor
- do NOT delete the DemoAutoConfigurationCustomizerProvider, you need that
- instead, delete most of the stuff that the AutoConfigurationCustomizerProvider was doing, so instead it provides only the span processor.

## Using the span processor

I tested this in conjunction with https://github.com/jessitron/pathetic-java-web-app

Here is what I did to use it:

- `./gradlew build`
- copy `/build/libs/*all.jar` (that is one jar) over to where my java app lives
- define OTEL_JAVAAGENT_EXTENSIONS to contain the name of that jar file (or path to it, if it isn't hanging out in the same dir where the app runs)
- define the rest of the otel env vars (see https://github.com/jessitron/pathetic-java-web-app/blob/main/run )
- run the app and see the 'custom' and 'random' fields on spans.

The otel debug output gives me no indication it is being loaded. The System.err.printlns in my span processor do not show either. They used to? I dunno. The attributes show up so I am satisfied.
