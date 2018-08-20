package com.echg.project.cayusedemo.controller;

import com.echg.project.cayusedemo.domain.GoogleElevation;
import com.echg.project.cayusedemo.domain.GoogleTimeZone;
import com.echg.project.cayusedemo.domain.LocationData;
import com.echg.project.cayusedemo.domain.OpenWeather;
import com.echg.project.cayusedemo.service.LocationDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 *
 */
@RestController
@RequestMapping("/weather")
public class LocationDataController {

    private static Logger log = LoggerFactory.getLogger(LocationDataController.class);
    private LocationDataService locationDataService;

    @Autowired
    public LocationDataController(LocationDataService locationDataService){
        this.locationDataService = locationDataService;
    }

    /**
     * Single endpoint to process the request and package the result from three asynchronous calls.
     * @param zipCode
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @GetMapping(path = "/location")
    public LocationData getWeatherDataFromZip(@RequestParam String zipCode) throws ExecutionException, InterruptedException {
        log.info("Initializing Async with zip " + zipCode);

        CompletableFuture<OpenWeather> openWeather = this.locationDataService.getTempAndCityForZip(zipCode);
        CompletableFuture<GoogleTimeZone> timeZone = this.locationDataService.getTimeZoneForZip(openWeather.get().getCoord());
        CompletableFuture<GoogleElevation> elevation = this.locationDataService.getElevationForZip(openWeather.get().getCoord());

        return LocationData.buildFrom(openWeather.get(),elevation.get(),timeZone.get());
    }

}
