package com.sdp.user_service.service;

import com.sdp.user_service.dto.UserDto;
import com.sdp.user_service.entity.User;
import com.sdp.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    public UserDto getUserById(Long userId){

        User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        return modelMapper.map(user, UserDto.class);
    }

    //check use was exists or not by id

    public boolean checkUserExists(Long userId){
        return userRepository.existsById(userId);
    }
}
