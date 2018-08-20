package com.echg.project.cayusedemo.domain;

import com.echg.project.cayusedemo.annotations.Precision;
import com.echg.project.cayusedemo.serialization.DoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.experimental.Accessors;

import java.text.DecimalFormat;
import java.util.Optional;

@Data
@Accessors
public class LocationData {
    private String cityName;
    private String timezone;

    @JsonSerialize(using = DoubleSerializer.class)
    @Precision(precision = 2)
    private Double temperature;

    @JsonSerialize(using = DoubleSerializer.class)
    @Precision(precision = 2)
    private Double elevation;

    private String message;

    public static LocationData buildFrom(OpenWeather weather, GoogleElevation elevation, GoogleTimeZone timeZone){
        Optional<GoogleElevation.ElevationResults> elevationResults = elevation.getResults().stream().findFirst();

        LocationData data = new LocationData();
        data.setCityName(weather.getName());
        data.setElevation(elevationResults.get().getElevation());
        data.setTemperature(weather.getMain().getTemp());
        data.setTimezone(timeZone.getTimeZoneName());
        data.setMessage(new StringBuilder("The temperature in ")
                .append(data.getCityName())
                .append(" currently is ")
                .append(new DecimalFormat("#.00").format(data.getTemperature()))
                .append(" degrees fahrenheit. ")
                .append(data.getCityName())
                .append(" is in the ")
                .append(data.getTimezone())
                .append(" Zone and has an elevation of ")
                .append(new DecimalFormat("#.00").format(data.getElevation()))
                .append(" meters above sea level.").toString());

        return data;
    }
}
