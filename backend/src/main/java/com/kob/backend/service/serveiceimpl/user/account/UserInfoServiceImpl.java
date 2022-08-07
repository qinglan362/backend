package com.kob.backend.service.serveiceimpl.user.account;

import com.kob.backend.entity.User;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.service.serveiceimpl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.UserInfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    UserMapper userMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();
        System.out.println(data);
        int age=Integer.parseInt(data.get("age"));
        String personalsignature=data.get("personalsignature");
        String hobby=data.get("hobby");
        Map<String,String> map=new HashMap<>();
        if(age<0){
            map.put("error_message","年龄不能小于0");
            return map;
        }
        if(age>120){
            map.put("error_message","年龄不能大于120");
            return map;
        }
        if(personalsignature.length()>100){
            map.put("error_message","个性签名不得超过100字");
            return map;
        }
        if(hobby.length()>100){
            map.put("error_message","爱好不得超过100字");
            return map;
        }
        User user1=userMapper.selectById(user.getId());
        if(user1==null)
        {
            map.put("error_message","用户不存在");
            return map;
        }
        User new_user=new User(
                user1.getId(),
                user1.getUsername(),
                user1.getPassword(),
                user1.getPhoto(),
                personalsignature,
                age,
                hobby
        );
        userMapper.updateById(new_user);
        map.put("error_message","success");
        return map;
    }
}
