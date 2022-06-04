package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrossFinalFareDepartureDto implements Serializable {

    @JsonProperty(value = "Flights")
    List<RetrossFlightDto> flights;

    @JsonProperty(value = "Fares")
    List<RetrossFareDto> fares;

    @JsonProperty(value = "Additions")
    AdditionDto addition;

}
