package com.zredtea.TeaWIKI.controller;

import com.zredtea.TeaWIKI.DTO.Result;
import com.zredtea.TeaWIKI.DTO.request.User.UserLoginDTO;
import com.zredtea.TeaWIKI.DTO.request.User.UserPasswordUpdateDTO;
import com.zredtea.TeaWIKI.DTO.request.User.UserRegisterDTO;
import com.zredtea.TeaWIKI.DTO.response.AuthDTO;
import com.zredtea.TeaWIKI.DTO.response.UserDTO;
import com.zredtea.TeaWIKI.common.exception.BusinessException;
import com.zredtea.TeaWIKI.common.exception.ExceptionEnum;
import com.zredtea.TeaWIKI.costumer.annotation.CurrentUser;
import com.zredtea.TeaWIKI.service.UserService;
import com.zredtea.TeaWIKI.util.FileUploadUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Tag(name = "用户控制器", description = "用于管理用户的接口")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "注册新用户")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "注册成功")
    })
    @PostMapping("/register")
    public Result<AuthDTO> register(@RequestBody @Valid UserRegisterDTO dto) {
        if (userService.isUserExist(dto.getUsername())) {
            throw new BusinessException(ExceptionEnum.USER_HAS_EXIST);
        }
        AuthDTO result = userService.register(dto);
        return Result.success(result);
    }

    @Operation(summary = "已注册用户登录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登录成功")
    })
    @PostMapping("/login")
    public Result<AuthDTO> login(@RequestBody @Valid UserLoginDTO dto) {
        if (!userService.isUserExist(dto.getUsername())) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        AuthDTO result = userService.login(dto);
        return Result.success(result);
    }

    @Operation(summary = "已登录用户登出")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "登出成功")
    })
    @Parameters(value = {
            @Parameter(name = "用户ID", in = ParameterIn.COOKIE)
    })
    @PostMapping("/logout")
    public Result<UserDTO> logout(@CurrentUser Integer userId) {
        if (!userService.isUserExist(userId)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        UserDTO result = userService.logout(userId);
        return Result.success(result);
    }

    @Operation(summary = "查询用户信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功")
    })
    @Parameters(value = {
            @Parameter(name = "用户名", in = ParameterIn.PATH)
    })
    @GetMapping("/{username}")
    public Result<UserDTO> getUserInfo(@PathVariable String username) {
        if (!userService.isUserExist(username)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        UserDTO result = userService.getUserInfo(username);
        return Result.success(result);
    }

    @Operation(summary = "查询当前用户信息")
    @Parameters(value = {
            @Parameter(name = "用户ID", in = ParameterIn.COOKIE)
    })
    @GetMapping("/current")
    public Result<UserDTO> getCurrentUserInfo(@CurrentUser Integer userId) {
        if (!userService.isUserExist(userId)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        UserDTO result = userService.getUserInfo(userService.getUsernameByUserId(userId));
        return Result.success(result);
    }

    @Operation(summary = "修改用户昵称")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "修改成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @PutMapping("/profile/nickname")
    public Result<UserDTO> updateNickname(@CurrentUser Integer userId,
                                          @RequestBody Map<String, String> request) {
        String nickname = request.get("nickname");
        if (!userService.isUserExist(userId)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        UserDTO result = userService.updateNickname(userId, nickname);
        return Result.success(result);
    }

    @Operation(summary = "修改用户头像")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "修改成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @PutMapping("/profile/avatar")
    public Result<UserDTO> updateAvatar(@CurrentUser Integer userId,
                                        @RequestParam("avatar") MultipartFile avatar)
                                        throws IOException {
        if (!userService.isUserExist(userId)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        String avatarURL = FileUploadUtil.uploadAvatar(avatar);
        UserDTO result = userService.updateAvatar(userId, avatarURL);
        return Result.success(result);
    }

    @Operation(summary = "修改用户学院")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "修改成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @PutMapping("/profile/department")
    public Result<UserDTO> updateDepartment(@CurrentUser Integer userId,
                                            @RequestBody Map<String, String> request) {
        String department = request.get("department");
        if (!userService.isUserExist(userId)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        UserDTO result = userService.updateDepartment(userId, department);
        return Result.success(result);
    }

    @Operation(summary = "修改用户密码")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "修改成功")
    })
    @Parameters(value = {
            @Parameter(name = "userId", description = "用户ID", required = true, in = ParameterIn.COOKIE)
    })
    @PutMapping("/profile/password")
    public Result<UserDTO> updatePassword(@CurrentUser Integer userId,
                                          @RequestBody @Valid UserPasswordUpdateDTO dto) {
        if (!userService.isUserExist(userId)) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        if (!dto.getOldPassword().equals(dto.getNewPassword())) {
            UserDTO result = userService.updatePassword(userId, dto);
            return Result.success(result);
        } else {
            throw new BusinessException(ExceptionEnum.USER_OLD_WRONG);
        }
    }
}
