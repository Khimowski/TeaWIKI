package com.zredtea.TeaWIKI.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zredtea.TeaWIKI.DTO.request.User.UserLoginDTO;
import com.zredtea.TeaWIKI.DTO.request.User.UserPasswordUpdateDTO;
import com.zredtea.TeaWIKI.DTO.request.User.UserRegisterDTO;
import com.zredtea.TeaWIKI.DTO.response.AuthDTO;
import com.zredtea.TeaWIKI.DTO.response.UserDTO;
import com.zredtea.TeaWIKI.common.exception.AuthException;
import com.zredtea.TeaWIKI.common.exception.BusinessException;
import com.zredtea.TeaWIKI.common.exception.ExceptionEnum;
import com.zredtea.TeaWIKI.common.exception.ServerException;
import com.zredtea.TeaWIKI.entity.User;
import com.zredtea.TeaWIKI.service.UserService;

import com.zredtea.TeaWIKI.mapper.UserMapper;
import com.zredtea.TeaWIKI.util.JWTUtil;
import com.zredtea.TeaWIKI.util.SaltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
                             implements UserService {

    private UserMapper userMapper;
    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public AuthDTO register(UserRegisterDTO dto) {
        UserMapper userMapper = getBaseMapper();
        User user = convertToEntity(dto);

        boolean success = super.save(user);
        if(!success) {
            throw new ServerException(ExceptionEnum.DATABASE_ERROR);
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());
        UserDTO userDTO = convertToDTO(user);
        return new AuthDTO(userDTO,token,jwtUtil.getExpiration());
    }

    @Override
    public AuthDTO login(UserLoginDTO dto) {
        UserMapper userMapper = getBaseMapper();
        User user = userMapper.selectByUsername(dto.getUsername());
        String passwordInput = dto.getPassword();
        passwordInput = SaltUtil.getPasswordCrypto(passwordInput, user.getSalt());
        if(!passwordInput.equals(user.getPassword())) {
            throw new BusinessException(ExceptionEnum.USER_PWD_WRONG);
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());
        UserDTO userDTO = convertToDTO(user);
        return new AuthDTO(userDTO,token,jwtUtil.getExpiration());
    }

    @Override
    public UserDTO logout(Integer userId) {
        if (userId == null) {
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }
        jwtUtil.invalidateToken(userId);
        return new UserDTO();
    }

    @Override
    public UserDTO getUserInfo(String username) {
        UserMapper userMapper = getBaseMapper();
        User user = userMapper.selectByUsername(username);
        return convertToDTO(user);
    }

    @Override
    public UserDTO getProfile(Integer userId) {
        return convertToDTO(userMapper.selectById(userId));
    }

    @Override
    public UserDTO updateNickname(Integer userId, String nickname) {
        UserMapper userMapper = getBaseMapper();
        User user = userMapper.selectById(userId);
        user.setNickname(nickname);
        boolean success = updateById(user);
        if(!success) {
            throw new ServerException(ExceptionEnum.DATABASE_ERROR);
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateAvatar(Integer userId, String avatar) {
        UserMapper userMapper = getBaseMapper();
        User user = userMapper.selectById(userId);
        user.setAvatar(avatar);
        int success = userMapper.updateById(user);
        if(success <= 0) {
            throw new ServerException(ExceptionEnum.DATABASE_ERROR);
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO updateDepartment(Integer userId, String department) {
        UserMapper userMapper = getBaseMapper();
        User user = userMapper.selectById(userId);
        user.setDepartment(department);
        boolean success = updateById(user);
        if(!success) {
            throw new ServerException(ExceptionEnum.DATABASE_ERROR);
        }
        return convertToDTO(user);
    }

    @Override
    public UserDTO updatePassword(Integer userId, UserPasswordUpdateDTO dto) {
        UserMapper userMapper = getBaseMapper();
        User user = userMapper.selectById(userId);
        String salt = user.getSalt();
        String oldPasswordInput = SaltUtil.getPasswordCrypto(dto.getOldPassword(), salt);
        String oldPassword = user.getPassword();
        if(!oldPasswordInput.equals(oldPassword)) {
            throw new BusinessException(ExceptionEnum.USER_OLD_WRONG);
        }

        String newSalt = SaltUtil.getSalt();
        String newPassword = SaltUtil.getPasswordCrypto(dto.getNewPassword(), newSalt);
        user.setPassword(newPassword);
        user.setSalt(newSalt);
        int success = userMapper.updateById(user);
        if(success <= 0) {
            throw new ServerException(ExceptionEnum.DATABASE_ERROR);
        }

        return convertToDTO(user);
    }

    @Override
    public Boolean isUserExist(Integer userid) {
        UserMapper userMapper = getBaseMapper();
        return userMapper.checkByUserId(userid);
    }

    @Override
    public Boolean isUserExist(String username) {
        UserMapper userMapper = getBaseMapper();
        return userMapper.checkByUsername(username);
    }

    @Override
    public String getUsernameByUserId(Integer userId) {
        return getById(userId).getUsername();
    }

    public User convertToEntity(UserRegisterDTO dto) {
        User user =  new User();
        String salt = SaltUtil.getSalt();

        user.setUsername(dto.getUsername());
        if(dto.getNickname() != null ) {
            user.setNickname(dto.getNickname());
        } else {
            user.setNickname(dto.getUsername());
        }

        user.setSalt(salt);
        user.setPassword(SaltUtil.getPasswordCrypto(dto.getPassword(), salt));
        return user;
    }

    public UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setNickname(user.getNickname());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setDepartment(user.getDepartment());
        userDTO.setCreatedAt(user.getCreatedAt());
        userDTO.setUpdatedAt(user.getUpdatedAt());
        return userDTO;
    }

    public List<UserDTO> convertToDTO(List<User> users){
        List<UserDTO> userDTOs = new ArrayList<>();
        for(User user : users) {
            userDTOs.add(convertToDTO(user));
        }
        return userDTOs;
    }
}
