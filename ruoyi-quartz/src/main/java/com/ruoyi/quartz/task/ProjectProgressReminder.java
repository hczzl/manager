package com.ruoyi.quartz.task;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.TaskProject;
import com.ruoyi.web.domain.MsgEvtInfo;
import com.ruoyi.web.domain.SysProjectTable;
import com.ruoyi.web.service.item.MsgEvtInfoService;
import com.ruoyi.web.service.item.ProduceTypeService;
import com.ruoyi.web.service.item.SysProjectTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Suqz
 * @version 1.0
 * @date 2020/4/16 10:12
 */
@Component("projectProgressReminder")
public class ProjectProgressReminder {
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ProduceTypeService produceTypeService;
    @Value("${server.port}")
    String serverPortNum;
    public void execute() {
        Map<Integer,Integer> projectRateMap= (Map<Integer, Integer>) redisTemplate.opsForValue().get(serverPortNum+"projectRateMap")!=null?(Map<Integer, Integer>) redisTemplate.opsForValue().get(serverPortNum+"projectRateMap"):new HashMap<>();
        Map<Integer,Integer> projectCountMap=(Map<Integer, Integer>) redisTemplate.opsForValue().get(serverPortNum+"projectCountMap")!=null?(Map<Integer, Integer>) redisTemplate.opsForValue().get(serverPortNum+"projectCountMap"):new HashMap<>();
        List<SysProjectTable> projectTableList=JSON.parseArray(JSON.toJSONString(sysProjectTableService.selectProjectInPlanList(new TaskProject())),SysProjectTable.class);
        if (projectRateMap.size()==0||projectCountMap.size()==0){
            projectTableList.forEach(item->{
                projectRateMap.put(item.getpId(),item.getPlanRate());
                projectCountMap.put(item.getpId(),1);
            });
        }

        //???????????????????????????????????????????????????+1???????????????????????????1
        projectTableList.forEach(item->{
            Integer rate = projectRateMap.get(item.getpId());
            if (rate.equals(item.getPlanRate())){
                Integer count = projectCountMap.get(item.getpId());
                projectCountMap.put(item.getpId(),count+1);
            }else {
                projectRateMap.put(item.getpId(),item.getPlanRate());
                projectCountMap.put(item.getpId(),1);
            }
        });
        redisTemplate.opsForValue().set(serverPortNum+"projectRateMap",projectRateMap);
        redisTemplate.opsForValue().set(serverPortNum+"projectCountMap",projectCountMap);

        //????????????7?????????????????????????????????????????????????????????
        projectCountMap.forEach((pid,count)->{
            if (count>7){
                //??????
                insertMessage(pid);
            }
        });
    }

    void insertMessage(Integer pid){
        //????????????id
        List<Integer> msgUserIdByPid = getMsgUserIdByPid(pid);
        msgUserIdByPid.forEach(uid->{
            MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
            msgEvtInfo.setType(12);
            msgEvtInfo.setEventId(pid);
            msgEvtInfo.setUserId(uid);
            msgEvtInfo.setCreateTime(new Date());
            msgEvtInfoService.insertMessageInfo(msgEvtInfo);
        });
    }

    List<Integer> getMsgUserIdByPid(Integer pid){
        SysProjectTable sysProjectTable = sysProjectTableService.selectProjectById(Long.parseLong(pid.toString()));
        List<Integer> userList=new ArrayList<>();
        //?????????
        Integer chargepeopleId = sysProjectTable.getChargepeopleId();
        if (chargepeopleId!=null){
            userList.add(chargepeopleId);
        }
        //???????????????
        Integer techniquePeople = sysProjectTable.getTechniquePeople();
        if(techniquePeople!=null){
            userList.add(techniquePeople);
        }
        //????????????
        Integer id = sysProjectTable.getProducetypeid();
        String projectManager = produceTypeService.getprojectmanagerbyid(id);
        if (!MyUtils.isEmpty(projectManager)){
            String[] users = projectManager.split(",");
            for (int i = 0; i < users.length; i++) {
                if(!MyUtils.isEmpty(users[i])){
                    userList.add(Integer.parseInt(users[i]));
                }
            }
        }
        //??????
        SysUser sysUser = new SysUser();
        sysUser.setDeptId(Long.parseLong("107"));
        List<SysUser> sysUsers = sysUserService.selectAllUserInfoByterm(sysUser);
        sysUsers.forEach(item->{
            userList.add(Integer.parseInt(item.getUserId().toString()));
        });
        //?????????????????????????????????????????????
        userList.add(245);
        userList.add(119);
        //??????
        return userList.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
