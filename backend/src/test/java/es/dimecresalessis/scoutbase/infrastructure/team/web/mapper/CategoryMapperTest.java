package es.dimecresalessis.scoutbase.infrastructure.team.web.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.CategoryEnumDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private CategoryMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CategoryMapper.class);
    }

    @Test
    void domainToDTO_ShouldMapCategoryAndSubcategoriesCorrectly() {
        // Given
        // Assuming CategoryEnum has a getCategoryName() and getSubcategories() method
        // based on the mapping source "categoryName" and "subcategories"
        CategoryEnum domain = CategoryEnum.JUVENIL;

        // When
        CategoryEnumDTO dto = mapper.domainToDTO(domain);

        // Then
        assertNotNull(dto);
        assertEquals(domain.getCategoryName(), dto.getName());

        List<String> expectedSubcategoryNames = domain.getSubcategories().stream()
                .map(SubcategoryEnum::getSubcategoryName)
                .toList();

        assertIterableEquals(expectedSubcategoryNames, dto.getSubcategories());
    }

    @Test
    void toSubcategoryDTOList_ShouldMapSubcategoryEnumsToStringList() {
        // Given
        List<SubcategoryEnum> subcategories = List.of(SubcategoryEnum.SUB16, SubcategoryEnum.SUB_SUPERIOR);

        // When
        List<String> result = mapper.toSubcategoryDTOList(subcategories);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("SUB-16", result.get(0));
        assertEquals("SUB17-19", result.get(1));
    }

    @Test
    void domainToDTO_ShouldReturnNull_WhenDomainIsNull() {
        assertNull(mapper.domainToDTO(null));
    }

    @Test
    void toSubcategoryDTOList_ShouldHandleEmptyList() {
        List<String> result = mapper.toSubcategoryDTOList(List.of());
        assertTrue(result.isEmpty());
    }
}