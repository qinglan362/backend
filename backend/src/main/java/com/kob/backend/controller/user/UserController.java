package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {
    @Resource
    UserMapper userMapper;

    @RequestMapping("/user/all/")
    public List<User> getAll(){
        return userMapper.selectList(null);
    }

    @RequestMapping("/user/{userId}/")
    public User getUser(@PathVariable int userId){
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",userId);
        return userMapper.selectOne(queryWrapper);
    }

    @RequestMapping("/user/add/{userId}/{username}/{password}/")
    public String addUser(@PathVariable int userId,
                          @PathVariable String username,
                          @PathVariable String password){
        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String encodedPassword=passwordEncoder.encode(password);
        User user=new User(userId,username,encodedPassword);
        userMapper.insert(user);
        return "Yes";
    }

    @RequestMapping("/user/delete/{userId}/")
    public String deleteUser(@PathVariable int userId) {
      userMapper.deleteById(userId);
      return  "Yes";
    }


}
