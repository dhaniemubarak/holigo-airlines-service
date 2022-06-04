package id.holigo.services.holigoairlinesservice.web.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionDto implements Serializable {

    private JsonNode baggage;

    private JsonNode meal;

    private JsonNode medical;

    private JsonNode seat;

}
