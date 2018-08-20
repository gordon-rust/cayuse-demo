package com.echg.project.cayusedemo.service;

import com.echg.project.cayusedemo.domain.Coordinates;
import com.echg.project.cayusedemo.domain.GoogleElevation;
import com.echg.project.cayusedemo.domain.GoogleTimeZone;
import com.echg.project.cayusedemo.domain.OpenWeather;

import java.util.concurrent.CompletableFuture;

public interface LocationDataService {
    /**
     * Service to interface with the Open Weather api and return data by zip code.
     * @param zipCode
     * @return
     */
    CompletableFuture<OpenWeather> getTempAndCityForZip(String zipCode);

    /**
     * Service to interface with the Google Timezone api and return data by coordinates.
     * @param coordinates
     * @return
     */
    CompletableFuture<GoogleTimeZone> getTimeZoneForZip(Coordinates coordinates);

    /**
     * Service to interface with the Google Elevation api and return data by coordinates.
     * @param coordinates
     * @return
     */
    CompletableFuture<GoogleElevation> getElevationForZip(Coordinates coordinates);
}
