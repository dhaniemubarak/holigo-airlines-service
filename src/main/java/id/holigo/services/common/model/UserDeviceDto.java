package id.holigo.services.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeviceDto {
    private Long id;

    private Long version;

    private DeviceTypeEnum deviceType;

    private String deviceId;

    private Double latitude;

    private Double longitude;

    private String manufacturer;

    private String model;

    private String osVersion;
}
