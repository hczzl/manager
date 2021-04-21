package com.ruoyi.web.service.item;

import java.util.List;
import java.util.Map;
import com.ruoyi.web.domain.MsgEvtInfo;
import com.ruoyi.web.util.pageutils.PageEntity;

/**
 * @author zhongzhilong
 * @date 2020/9/15 0015
 */
public interface MsgEvtInfoService {

    /**
     * 根据类型查消息
     *
     * @param msgEvtInfo
     * @return
     */
    List<MsgEvtInfo> selectMsgListByType(MsgEvtInfo msgEvtInfo);

    /**
     * list
     *
     * @param msgEvtInfo
     * @return
     */
    List<MsgEvtInfo> selectMsgList(MsgEvtInfo msgEvtInfo);

    /**
     * 更新消息
     *
     * @param msgEvtInfo
     * @return
     */
    int update(MsgEvtInfo msgEvtInfo);

    /**
     * 添加审批提醒信息
     *
     * @param msgEvtInfo
     * @return
     */
    int insertMessageInfo(MsgEvtInfo msgEvtInfo);

    /**
     * 更新消息表
     *
     * @param msgEvtInfo
     * @return
     */
    int updateMessageInfo(MsgEvtInfo msgEvtInfo);

    /**
     * 根据查找消息表，决定是否还继续将审批提醒信息插入消息表
     *
     * @param msgEvtInfo
     * @return
     */
    List<MsgEvtInfo> selectOneMsgList(MsgEvtInfo msgEvtInfo);

    /**
     * 查找关于审批的消息
     *
     * @param msgEvtInfo
     * @return
     */
    List<MsgEvtInfo> selectMsgForApproval(MsgEvtInfo msgEvtInfo);

    /**
     * 从数据库中删除数据
     *
     * @param msgEvtInfo
     * @return
     */
    int deleteForMsg(MsgEvtInfo msgEvtInfo);


    /**
     * 查询某一个用户还有多少多少条未读信息
     *
     * @param msgEvtInfo
     * @return
     */
    int selectCountReadMark(MsgEvtInfo msgEvtInfo);

    /**
     * 添加消息
     *
     * @param map
     */
    void save(Map<String, Object> map);

    /**
     * 根据id查询消息
     *
     * @param msgEvtInfo
     * @return
     */
    MsgEvtInfo selectMsgById(MsgEvtInfo msgEvtInfo);

    /**
     * 查找对应的填写提醒
     *
     * @param msgEvtInfo
     * @return
     */
    List<MsgEvtInfo> selectMsgListByType1(MsgEvtInfo msgEvtInfo);

    /**
     * 更新用户的所有未读状态
     *
     * @param msgEvtInfo
     * @return
     */
    int updateReadMark(MsgEvtInfo msgEvtInfo);

    /**
     * 查找对应的审批未通过的提醒
     *
     * @param msgEvtInfo
     * @return
     */
    List<MsgEvtInfo> selectTypeForTen(MsgEvtInfo msgEvtInfo);

    /**
     * 根据id更新消息
     *
     * @param msgEvtInfo
     * @return
     */
    int updateInfoById(MsgEvtInfo msgEvtInfo);

    /**
     * 修改消息表
     *
     * @param msgEvtInfo
     * @return
     */
    int updateMessageInfo2(MsgEvtInfo msgEvtInfo);

    /**
     * 获得消息列表
     *
     * @param msgEvtInfo
     * @return
     */
    List<MsgEvtInfo> selectMsgLists(MsgEvtInfo msgEvtInfo);

    /**
     * 分页列表
     *
     * @param msgEvtInfo
     * @return
     */
    PageEntity selectMsgEvtList(MsgEvtInfo msgEvtInfo);
}
