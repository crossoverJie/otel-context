package com.otel.extensions;

import static com.otel.extensions.DemoPropagator.PROPAGATION_UPSTREAM_NAME;
import static com.otel.extensions.DemoPropagator.UPSTREAM_NAME;
import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.internal.StringUtils;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import java.util.Random;

public class DemoSpanProcessor implements SpanProcessor {


    @Override
    public void onStart(Context parentContext, ReadWriteSpan span) {
    /*
    The sole purpose of this attribute is to introduce runtime dependency on some external library.
    We need this to demonstrate how extension can use them.
     */
        System.out.println("===========BeginSpan===========");
        String value = Baggage.current().getEntryValue(UPSTREAM_NAME);
        System.out.println("SpanProcessor=====value: " + value);
        value = Baggage.fromContext(parentContext).getEntryValue(UPSTREAM_NAME);
        System.out.println("SpanProcessor=====value=fromContext: " + value);


        System.out.println(String.format("SpanProcessor=====context: %s", parentContext));
        System.out.println("SpanProcessor=====span: " + span);
        System.out.println("SpanProcessor=====span_name: " + span.getName());

        Random random = new Random();
        int randomNumber = random.nextInt(11);
        span.setAttribute("random", randomNumber);
        span.setAttribute("custom", "demo");
        String upstreamName = parentContext.get(PROPAGATION_UPSTREAM_NAME);
        System.out.println("SpanProcessor=====upstreamName: " + upstreamName);
        if (!StringUtils.isNullOrEmpty(upstreamName)) {
            span.setAttribute(UPSTREAM_NAME, upstreamName);
        }

        Baggage.current().toBuilder()
                    .put(UPSTREAM_NAME, span.getName())
                    .build()
                    .storeInContext(Context.current()).makeCurrent();
        System.out.println("SpanProcessor=====set baggage: " + span.getName());


        System.out.println("===========EndSpan===========");
    }

    @Override
    public boolean isStartRequired() {
        return true;
    }

    @Override
    public void onEnd(ReadableSpan span) {

    }

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
