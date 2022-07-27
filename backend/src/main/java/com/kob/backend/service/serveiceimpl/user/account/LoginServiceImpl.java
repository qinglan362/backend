package com.kob.backend.service.serveiceimpl.user.account;

import com.kob.backend.entity.User;
import com.kob.backend.service.serveiceimpl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(username,password);

       Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl loginUser=(UserDetailsImpl) authenticate.getPrincipal();
        User user=loginUser.getUser();

        String jwt= JwtUtil.createJWT(user.getId().toString());

        Map<String,String> map=new HashMap<>();
        map.put("error_message","success");
        map.put("token",jwt);
        return map;
    }
}
