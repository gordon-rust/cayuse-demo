package com.echg.project.cayusedemo.domain;

import com.echg.project.cayusedemo.annotations.Precision;
import com.echg.project.cayusedemo.serialization.DoubleSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Collection;

@Data @Accessors
public class OpenWeather {
    private Coordinates coord;
    private Collection<Weather> weather;
    private String base;
    private Atmospherics main;
    private Integer visibility;
    private WindData wind;
    private CloudData clouds;
    private RainData rain;
    private SnowData snow;
    private Instant dt;
    private SystemData sys;
    private Integer id;
    private String name;
    private Integer cod;

    @Data @Accessors
    public static final class Weather {
        private Integer id;
        private String main;
        private String description;
        private String icon;
    }

    @Data @Accessors
    public static final class Atmospherics {
        private Double temp;

        private Integer pressure;
        private Integer humidity;

        @JsonSerialize(using = DoubleSerializer.class)
        @Precision(precision = 2)
        private Double temp_min;

        @JsonSerialize(using = DoubleSerializer.class)
        @Precision(precision = 2)
        private Double temp_max;

        @JsonSerialize(using = DoubleSerializer.class)
        @Precision(precision = 2)
        public Double getTemp(){
            return (((temp - 273) * 9/5) + 32);
        }
    }

    @Data @Accessors
    public static final class WindData {
        @JsonSerialize(using = DoubleSerializer.class)
        @Precision(precision = 2)
        private Double speed;

        private Integer deg;

        @JsonSerialize(using = DoubleSerializer.class)
        @Precision(precision = 2)
        private Double gust;
    }

    @Data @Accessors
    public static final class CloudData {
        private Integer all;
    }

    @Data @Accessors
    public static final class RainData {
        @JsonProperty(value = "3h")
        private Double rainPastThreeHours;
    }

    @Data @Accessors
    public static final class SnowData {
        @JsonProperty(value = "3h")
        private Double snowPastThreeHours;
    }

    @Data @Accessors
    public static final class SystemData {
        private Integer type;
        private Integer id;
        private Double message;
        private String country;
        private Instant sunrise;
        private Instant sunset;
    }
}
