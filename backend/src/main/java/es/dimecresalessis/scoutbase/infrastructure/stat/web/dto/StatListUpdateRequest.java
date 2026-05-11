package es.dimecresalessis.scoutbase.infrastructure.stat.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatListUpdateRequest {

    @NotNull
    private List<StatUpdateRequest> stats;
}
