package com.otel.extensions;

import com.google.auto.service.AutoService;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigurablePropagatorProvider;

@AutoService(ConfigurablePropagatorProvider.class)
public class DemoPropagatorProvider implements ConfigurablePropagatorProvider {
    @Override
    public TextMapPropagator getPropagator(ConfigProperties config) {
        return new DemoPropagator();
    }

    @Override
    public String getName() {
        return "demo";
    }
}