package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.SysUserPost;
import com.ruoyi.system.domain.vo.SysUserVo;

/**
 * 用户 业务层
 *
 * @author ruoyi
 */
public interface ISysUserService {
    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUserList(SysUser user);

    /**
     * 获得职级id和部门id
     *
     * @param user
     * @return
     */
    List<SysUser> selectDetptIdAndPostId(SysUser user);

    /**
     * 获得所有部门下的所有人
     *
     * @param user
     * @return
     */

    List<SysUser> selectAllDetptUser(SysUser user);

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectAllocatedList(SysUser user);

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUnallocatedList(SysUser user);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByLoginName(String userName);

    /**
     * 通过手机号码查询用户
     *
     * @param phoneNumber 手机号码
     * @return 用户对象信息
     */
    SysUser selectUserByPhoneNumber(String phoneNumber);

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    SysUser selectUserByEmail(String email);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    SysUser selectUserById(Long userId);

    /**
     * @param sysUser
     * @return
     */
    SysUser selectUserByUserId(SysUser sysUser);

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    int deleteUserById(Long userId);

    /**
     * 批量删除用户信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     * @throws Exception 异常
     */
    int deleteUserByIds(String ids) throws Exception;

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(SysUser user);

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);

    /**
     * 修改用户详细信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUserInfo(SysUser user);

    /**
     * 修改用户密码信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int resetUserPwd(SysUser user);

    /**
     * 校验用户名称是否唯一
     *
     * @param loginName 登录名称
     * @return 结果
     */
    String checkLoginNameUnique(String loginName);

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkPhoneUnique(SysUser user);

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    String checkEmailUnique(SysUser user);

    /**
     * 根据用户ID查询用户所属角色组
     *
     * @param userId 用户ID
     * @return 结果
     */
    String selectUserRoleGroup(Long userId);

    /**
     * 根据用户ID查询用户所属岗位组
     *
     * @param userId 用户ID
     * @return 结果
     */
    String selectUserPostGroup(Long userId);

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName);

    /**
     * 用户状态修改
     *
     * @param user 用户信息
     * @return 结果
     */
    int changeStatus(SysUser user);

    /**
     * 获取用户名字
     *
     * @param uid
     * @return
     */
    String getName(long uid);

    /**
     * 根据用户名获取用户id
     *
     * @param userName
     * @return
     */
    Long getUid(String userName);


    /**
     * 根据用户id查询对应的postid
     *
     * @param sysUserPost
     * @return
     */
    List<SysUserPost> selectPostByUserId(SysUserPost sysUserPost);

    /**
     * 查询所有用户的信息
     */
    List<SysUser> selectAllUserInfo();

    /**
     * 根据名字模糊查询获取用户信息
     *
     * @return
     */
    List<SysUser> selectUserByName(String userName);

    /**
     * 根据用户Id获取用户信息
     *
     * @param userId
     * @return
     */
    SysUser selectUserInfoById(Integer userId);

    /**
     * 根据用户Id获取用户部门名称
     *
     * @param userId
     * @return
     */
    String selectDeptNameByUserId(Integer userId);

    /**
     * @param sysUser
     * @return
     */
    List<SysUser> getUserLists(SysUser sysUser);

    /**
     * 获取到所有的user_id
     *
     * @param sysUser
     * @return
     */
    List<SysUser> selectId(SysUser sysUser);

    /**
     * 根据微信id获取用户信息，wx_id是唯一的
     *
     * @param sysUser
     * @return
     */
    List<SysUser> selectInfosByWxId(SysUser sysUser);

    /**
     * 插入微信id
     *
     * @param sysUser
     * @return
     */
    int updateWxId(SysUser sysUser);

    /**
     * @param sysUser
     * @return
     */
    List<SysUser> selectmanagerId(SysUser sysUser);

    /**
     * @param userId
     * @return
     */
    SysUserVo selectUserInfos(Long userId);

    /**
     * @param userId
     * @return
     */
    SysUser selectUserInfo(Long userId);

    /**
     * @param sysUser
     */
    void addInitPassWord(SysUser sysUser);

    /**
     * @param sysUser
     * @return
     */
    String selectAllInitPassWordById(SysUser sysUser);

    /**
     * @param sysUser
     * @return
     */
    List<SysUser> selectAllUserInfoByterm(SysUser sysUser);

    /**
     * @param userId
     * @return
     */
    Boolean superAdmin(Long userId);

    /**
     * 获取任务统计模块的用户信息
     *
     * @param user
     * @return
     */
    List<SysUser> selectStatisticUserList(SysUser user);
}
