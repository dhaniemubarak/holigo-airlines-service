package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RetrossFinalFareDto implements Serializable {

    @JsonProperty("departure")
    List<RetrossFinalFareDepartureDto> departures;

    @JsonProperty(value = "return")
    List<RetrossFinalFareDepartureDto> returns;
}
