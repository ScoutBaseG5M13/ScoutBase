package es.dimecresalessis.scoutbase.infrastructure.team.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryEnumDTO {

    private String name;

    private List<String> subcategories;
}
