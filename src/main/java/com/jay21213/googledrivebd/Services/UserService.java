package com.jay21213.googledrivebd.Services;


import com.jay21213.googledrivebd.DTO.UserData;
import com.jay21213.googledrivebd.Entity.User;
import com.jay21213.googledrivebd.reprository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService  {

    @Autowired
    private UserRepository userRepository;


    public User getUser(String email){
       return userRepository.findByEmail(email);
    }
    public User getUserById(String id){
       return userRepository.findById(id).get();
    }

    public User saveUser (User user){
        return userRepository.save(user);
    }

    public UserData getUserData(String id){
        return userRepository.findUserData(id);
    }

}






