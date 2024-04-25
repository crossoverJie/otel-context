package com.otel.extensions;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.internal.StringUtils;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.ContextKey;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import java.util.Collections;
import java.util.List;

public class DemoPropagator implements TextMapPropagator {
    private static final String FIELD = "X-propagation";
    public static final String UPSTREAM_NAME = "upstream_name";
    public static final ContextKey<String> PROPAGATION_UPSTREAM_NAME =
            ContextKey.named(UPSTREAM_NAME);


    @Override
    public List<String> fields() {
        return Collections.singletonList(FIELD);
    }

    @Override
    public <C> void inject(Context context, C carrier, TextMapSetter<C> setter) {
        System.out.println("===========BeginInject===========");
        String upstreamName = context.get(PROPAGATION_UPSTREAM_NAME);
        System.out.println("DemoPropagator=====Inject context: " + context);
        if (StringUtils.isNullOrEmpty(upstreamName)) {
            upstreamName = Baggage.current().getEntryValue(UPSTREAM_NAME);
        }
        System.out.println("DemoPropagator=====inject: " + upstreamName);
        System.out.println("DemoPropagator=====carrier: " + carrier);

        Baggage.current().forEach((k, v) -> {
            System.out.println("1DemoPropagator=====k: " + k);
            System.out.println("1DemoPropagator=====v: " + v);
        });
        setter.set(carrier, FIELD, String.valueOf(upstreamName));
        System.out.println("===========EndInject===========");

    }

    @Override
    public <C> Context extract(Context context, C carrier, TextMapGetter<C> getter) {
        System.out.println("===========BeginExtract===========");
        String propagationStart = getter.get(carrier, FIELD);
        System.out.println("DemoPropagator=====extract: " + propagationStart);
        System.out.println("===========EndExtract===========");
        if (propagationStart != null) {
            return context.with(PROPAGATION_UPSTREAM_NAME, propagationStart);
        } else {
            return context;
        }

    }
}
