package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetrossDepartureDto {
    @JsonProperty(value = "Flights")
    List<RetrossFlightDto> flights;

    @JsonProperty(value = "Fares")
    List<JsonNode> fares;
}
