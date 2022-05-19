package com.creddit.credditchatserver.service;

import com.creddit.credditchatserver.entity.ChatRoom;
import com.creddit.credditchatserver.entity.User;
import com.creddit.credditchatserver.exception.user.UserException;
import com.creddit.credditchatserver.exception.user.UserExceptionType;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private RedisTemplate<String, User> userTemplate;

    public void createUser(User user) throws Exception{
        ValueOperations<String, User> userMaps = userTemplate.opsForValue();
        if(Boolean.TRUE.equals(userTemplate.hasKey(user.getId() + ":info"))){
            throw new UserException(UserExceptionType.ALREADY_EXIST_USER);
        }
        userMaps.set(user.getId()+":info",user);
    }
}
