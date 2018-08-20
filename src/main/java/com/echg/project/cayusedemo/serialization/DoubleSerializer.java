package com.echg.project.cayusedemo.serialization;

import com.echg.project.cayusedemo.annotations.Precision;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DoubleSerializer extends JsonSerializer<Double> implements ContextualSerializer {
    private int precision = 0;

    public DoubleSerializer (int precision) {
        this.precision = precision;
    }

    public DoubleSerializer () {

    }

    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        if (precision == 0) {
            gen.writeNumber(value.doubleValue());
        } else {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(precision, RoundingMode.HALF_UP);
            gen.writeNumber(bd.doubleValue());
        }

    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Precision precision = property.getAnnotation(Precision.class);
        if (precision != null) {
            return new DoubleSerializer (precision.precision());
        }
        return this;
    }
}
