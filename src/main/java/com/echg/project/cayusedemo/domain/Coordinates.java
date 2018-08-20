package com.echg.project.cayusedemo.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public final class Coordinates {
    @JsonAlias({"lng","lon"})
    private Double lon;
    private Double lat;
}
