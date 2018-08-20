package com.echg.project.cayusedemo.domain;

import com.echg.project.cayusedemo.annotations.Precision;
import com.echg.project.cayusedemo.serialization.DoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;

@Data @Accessors
public class GoogleElevation {
    private Collection<ElevationResults> results;
    private String status;

    @Data @Accessors
    public static final class ElevationResults {
        @JsonSerialize(using = DoubleSerializer.class)
        @Precision(precision = 2)
        private Double elevation;

        @JsonSerialize(using = DoubleSerializer.class)
        @Precision(precision = 2)
        private Double resolution;

        private Coordinates location;
    }
}
