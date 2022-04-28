package id.holigo.services.holigoairlinesservice.web.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListAvailabilityDto {

    private List<AirlinesAvailabilityDto> departures;

    private List<AirlinesAvailabilityDto> returns;
}
