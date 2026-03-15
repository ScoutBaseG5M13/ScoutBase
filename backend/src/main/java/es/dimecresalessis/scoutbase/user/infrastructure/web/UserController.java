package es.dimecresalessis.scoutbase.user.infrastructure.web;

import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.shared.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.shared.routes.Routes;
import es.dimecresalessis.scoutbase.user.application.usecases.CreateUserUseCase;
import es.dimecresalessis.scoutbase.user.application.usecases.FindUserByIdUseCase;
import es.dimecresalessis.scoutbase.user.domain.exception.UserException;
import es.dimecresalessis.scoutbase.user.domain.model.User;
import es.dimecresalessis.scoutbase.user.infrastructure.web.dto.UserDto;
import es.dimecresalessis.scoutbase.user.infrastructure.web.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
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
    public ApiResponse<UserDto> findById(@PathVariable UUID id) {
        try {
            return handleResponse(
                    userMapper.toDto(
                            findUserByIdUseCase.execute(id)
                    )
            ).ok();
        } catch (NoSuchElementException ex) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, id.toString());
        }
    }

    @PostMapping("/create")
    public ApiResponse<UserDto> create(@RequestBody UserDto userDto) {
        User user = createUserUseCase.execute(userMapper.toDomain(userDto));
        return handleResponse(userMapper.toDto(user)).ok();
    }
}