package com.echg.project.cayusedemo.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class GoogleTimeZone {
    private Integer dstOffset;
    private Integer rawOffset;
    private String timeZoneId;
    private String timeZoneName;
    private String status;
    private String errorMessage;
}
