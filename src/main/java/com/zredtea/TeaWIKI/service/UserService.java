package com.zredtea.TeaWIKI.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.zredtea.TeaWIKI.DTO.request.User.UserLoginDTO;
import com.zredtea.TeaWIKI.DTO.request.User.UserPasswordUpdateDTO;
import com.zredtea.TeaWIKI.DTO.request.User.UserRegisterDTO;
import com.zredtea.TeaWIKI.DTO.response.AuthDTO;
import com.zredtea.TeaWIKI.DTO.response.UserDTO;
import com.zredtea.TeaWIKI.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    AuthDTO register(UserRegisterDTO dto);

    AuthDTO login(UserLoginDTO dto);

    UserDTO logout(Integer userId);

    UserDTO getUserInfo(String username);

    UserDTO getProfile(Integer userId);

    UserDTO updateNickname(Integer userId, String nickname);

    UserDTO updateAvatar(Integer userId, String avatar);

    UserDTO updateDepartment(Integer userId, String department);

    UserDTO updatePassword(Integer userId, UserPasswordUpdateDTO dto);

    Boolean isUserExist(Integer userId);

    Boolean isUserExist(String username);

    String getUsernameByUserId(Integer userId);
//
//    User convertToEntity(UserRegisterDTO dto);
//
//    UserDTO convertToDTO(User user);
//
//    List<UserDTO> convertToDTO(List<User> users);
}
