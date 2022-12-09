package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties
public class RetrossFlightDto implements Serializable {

    @JsonProperty(value = "FlightNo")
    private String flightNumber;

    @JsonProperty(value = "STD")
    private String std;

    @JsonProperty(value = "STA")
    private String sta;

    @JsonProperty(value = "Transit")
    private String transit;

    @JsonProperty(value = "ETD")
    private String etd;

    @JsonProperty(value = "ETA")
    private String eta;

    @JsonProperty(value = "Durasi")
    private String duration;
}
