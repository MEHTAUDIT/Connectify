package com.sdp.user_service.service;

import com.sdp.user_service.dto.LoginRequestDto;
import com.sdp.user_service.dto.SignupRequestDto;
import com.sdp.user_service.dto.UserDto;
import com.sdp.user_service.entity.User;
import com.sdp.user_service.exception.BadRequestException;
import com.sdp.user_service.exception.ResourceNotFoundException;
import com.sdp.user_service.repository.UserRepository;
import com.sdp.user_service.utills.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;

    public UserDto signup(SignupRequestDto signupRequestDto) {

        boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());

        if(exists){
            throw new BadRequestException("User with email "+signupRequestDto.getEmail()+" already exists");
        }
        
        User user = modelMapper.map(signupRequestDto, User.class);
        user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));
        User savedUser = userRepository.save(user);
        log.info("User with id {} signed up", savedUser.getId());
        return modelMapper.map(savedUser, UserDto.class);
    }

    public String login(LoginRequestDto loginRequestDto) {

        User user= (User) userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        if(PasswordUtil.checkPassword(loginRequestDto.getPassword(),user.getPassword())){


            return jwtService.generateAccessToken(user);
        }else{
            throw new BadRequestException("Invalid password");
        }
    }
}
