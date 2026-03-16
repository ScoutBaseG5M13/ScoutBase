package es.dimecresalessis.scoutbase.infrastructure.user.web;

import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.application.user.CreateUserUseCase;
import es.dimecresalessis.scoutbase.application.user.FindUserByIdUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
import es.dimecresalessis.scoutbase.infrastructure.user.web.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

@RestController
@AllArgsConstructor
@ApiCommonResponses
@RequestMapping(Routes.API_ROOT + Routes.USERS)
public class UserController {

    private final UserMapper userMapper;
    private final CreateUserUseCase createUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDto> findById(@PathVariable UUID id) {
        return handleResponse(
                userMapper.toDto(
                        findUserByIdUseCase.execute(id)
                )
        ).ok();
    }


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<UserDto> create(@RequestBody UserDto userDto) {
        User user = createUserUseCase.execute(userMapper.toDomain(userDto));
        return handleResponse(userMapper.toDto(user)).ok();
    }
}