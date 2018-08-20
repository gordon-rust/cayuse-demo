package com.echg.project.cayusedemo.service.impl;

import com.echg.project.cayusedemo.domain.Coordinates;
import com.echg.project.cayusedemo.domain.GoogleElevation;
import com.echg.project.cayusedemo.domain.GoogleTimeZone;
import com.echg.project.cayusedemo.domain.OpenWeather;
import com.echg.project.cayusedemo.service.LocationDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LocationDataServiceImpl implements LocationDataService {

    @Value("${openweather.api.key}")
    private String owKey;

    @Value("${google.api.key}")
    private String googleKey;

    @Value("${google.base.url}")
    private String googleBase;

    private static Logger log = LoggerFactory.getLogger(LocationDataServiceImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Async("asyncExecutor")
    @Override
    public CompletableFuture<OpenWeather> getTempAndCityForZip(String zipCode) {
        log.info("Fetch data from Open Weather API for zip " + zipCode);
        if(validateZipCode(zipCode)) {
            ResponseEntity<OpenWeather> openWeather = null;

            String url = "http://api.openweathermap.org/data/2.5/weather?zip=" + zipCode + ",us&APPID=" + owKey;
            try {
                openWeather = restTemplate.exchange(url, HttpMethod.GET, null, OpenWeather.class);
            } catch (HttpStatusCodeException ex) {
                if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                    throw new IllegalArgumentException(ex.getMessage());
                }
                throw new ResourceNotFoundException(ex.getMessage());
            }

            log.info("Fetch completed, {}", openWeather.getBody());
            return CompletableFuture.completedFuture(openWeather.getBody());
        }
        throw new IllegalArgumentException("The zip code entered was invalid!");
    }

    @Async("asyncExecutor")
    @Override
    public CompletableFuture<GoogleTimeZone> getTimeZoneForZip(Coordinates coordinates) {
        log.info("Fetch data from Google Timezone API for coordinates ");
        ResponseEntity<GoogleTimeZone> googleTimeZone = null;

        String url = googleBase + "timezone/json?location=" + coordinates.getLat() + "," + coordinates.getLon() +
                "&timestamp=" + Instant.now().getEpochSecond() + "&key=" + googleKey;
        try {
            googleTimeZone = restTemplate.exchange(url, HttpMethod.GET, null, GoogleTimeZone.class);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new IllegalArgumentException(ex.getMessage());
            }
            throw new ResourceNotFoundException(ex.getMessage());
        }

        log.info("Fetch completed, {}", googleTimeZone);
        return CompletableFuture.completedFuture(googleTimeZone.getBody());
    }

    @Async("asyncExecutor")
    @Override
    public CompletableFuture<GoogleElevation> getElevationForZip(Coordinates coordinates) {
        log.info("Fetch data from Google Elevation API for zip ");
        ResponseEntity<GoogleElevation> googleElevation = null;

        String url = googleBase + "elevation/json?locations=" + coordinates.getLat() + "," + coordinates.getLon() +
                "&timestamp=" + Instant.now().getEpochSecond() + "&key=" + googleKey;
        try {
            googleElevation = restTemplate.exchange(url, HttpMethod.GET, null, GoogleElevation.class);
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new IllegalArgumentException(ex.getMessage());
            }
            throw new ResourceNotFoundException(ex.getMessage());
        }

        log.info("Fetch completed, {}", googleElevation);
        return CompletableFuture.completedFuture(googleElevation.getBody());
    }

    /**
     * Validator method to check the zip code.
     * @param zipCode
     * @return
     */
    private boolean validateZipCode(String zipCode) {
        String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(zipCode);

        return matcher.matches();
    }
}
