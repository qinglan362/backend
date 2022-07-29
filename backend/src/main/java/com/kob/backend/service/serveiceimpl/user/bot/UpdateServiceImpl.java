package com.kob.backend.service.serveiceimpl.user.bot;

import com.kob.backend.entity.Bot;
import com.kob.backend.entity.User;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.service.serveiceimpl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class UpdateServiceImpl implements UpdateService {

    @Resource
    private BotMapper botMapper;
    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication();
        UserDetailsImpl loginUser=(UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();
        Integer bot_id=Integer.parseInt(data.get("bot_id"));
        String content=data.get("content");
        String title=data.get("title");
        String description=data.get("description");
        Map<String,String> map=new HashMap<>();
        if(title==null||title.length()==0) {
            map.put("error_message","标题不能为空");
            return map;
        }
        if (title.length()>100){
            map.put("error_message","标题长度过长");
             return map;
        }
        if(description.length() == 0){
            description="这个用户很懒，什么描述也没有";
        }
        if (description.length()>300) {
             map.put("error_message","描述过长");
            return map;
        }
        if (content==null||content.length()==0){
            map.put("error_message","代码不能为空");
            return map;
        }
        if(content.length()>10000){
            map.put("error_message","代码长度过长");
           return map;
        }
        Bot bot=botMapper.selectById(bot_id);
        if(bot==null)
        {
            map.put("error_message","bot不存在");
            return map;
        }
        if(!bot.getUserId().equals(user.getId()))
        {
            map.put("error_message","无权修改");
            return map;
        }
        Bot new_bot=new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                content,
                bot.getRating(),
                bot.getCreatetime(),
                new Date()
        );
        botMapper.updateById(new_bot);
        map.put("error_message","successs");
        return map;
    }
}
