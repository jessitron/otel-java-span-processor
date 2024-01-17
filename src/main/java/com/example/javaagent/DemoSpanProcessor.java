/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package com.example.javaagent;

import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;

/**
 * See <a
 * href="https://github.com/open-telemetry/opentelemetry-specification/blob/master/specification/trace/sdk.md#span-processor">
 * OpenTelemetry Specification</a> for more information about {@link SpanProcessor}.
 *
 * @see DemoAutoConfigurationCustomizerProvider
 */
public class DemoSpanProcessor implements SpanProcessor {

  @Override
  public void onStart(Context parentContext, ReadWriteSpan span) {
    addGlobalThreadCountToSpan(span);
  }

  private void addGlobalThreadCountToSpan(ReadWriteSpan span) {
    // Get the current thread's thread group
    ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
    span.setAttribute("thread.group.name", rootGroup.getName());
    span.setAttribute("thread.group.active.count", rootGroup.activeCount());
    ThreadGroup parentGroup;
    while ((parentGroup = rootGroup.getParent()) != null) {
        rootGroup = parentGroup;
    }
    // Active count of all threads 
    span.setAttribute("thread.root.active.count", rootGroup.activeCount());
    // System.out.println("Number of active threads: " + activeThreads);
  }

  @Override
  public boolean isStartRequired() {
    return true;
  }

  @Override
  public void onEnd(ReadableSpan span) {}

  @Override
  public boolean isEndRequired() {
    return false;
  }

  @Override
  public CompletableResultCode shutdown() {
    return CompletableResultCode.ofSuccess();
  }

  @Override
  public CompletableResultCode forceFlush() {
    return CompletableResultCode.ofSuccess();
  }
}
