package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.vo.SysUserVo;
import org.springframework.stereotype.Component;

/**
 * 用户表 数据层
 *
 * @author ruoyi
 */
@Component
public interface SysUserMapper {
    /**
     * 根据条件分页查询用户列表
     *
     * @param sysUser 用户信息
     * @return 用户信息集合信息
     */
    List<SysUser> selectUserList(SysUser sysUser);

    List<SysUser> getUserLists(SysUser sysUser);

    /**
     * 获得职级id和部门id
     */
    List<SysUser> selectDetptIdAndPostId(SysUser user);
    //获得所有部门下的所有人

    List<SysUser> selectAllDetptUser(SysUser user);

    /**
     * 根据条件分页查询未已配用户角色列表
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
     */
    int deleteUserByIds(Long[] ids);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int updateUser(SysUser user);

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    int insertUser(SysUser user);

    /**
     * 校验用户名称是否唯一
     *
     * @param loginName 登录名称
     * @return 结果
     */
    int checkLoginNameUnique(String loginName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    SysUser checkPhoneUnique(String phonenumber);

    /**
     * 校验email是否唯一
     *
     * @param email 用户邮箱
     * @return 结果
     */
    SysUser checkEmailUnique(String email);

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
     * 查询所有用户的信息
     */
    List<SysUser> selectAllUserInfo();

    SysUser selectUserByUserId(SysUser sysUser);

    /**
     * 根据名字模糊查询获取用户信息
     *
     * @return
     */
    List<SysUser> selectUserByName(String userName);

    /**
     * 根据用户Id获取用户信息
     */
    SysUser selectUserInfoById(Integer userId);

    /**
     * 根据用户Id获取用户部门名称
     */
    String selectDeptNameByUserId(Integer userId);

    List<SysUser> selectId(SysUser sysUser);

    List<SysUser> selectInfosByWxId(SysUser sysUser);

    int updateWxId(SysUser sysUser);

    List<SysUser> selectmanagerId(SysUser sysUser);

    SysUserVo selectUserInfos(Long userId);

    SysUser selectUserInfo(Long userId);

    void addInitPassWord(SysUser sysUser);

    String selectAllInitPassWordById(SysUser sysUser);

    List<SysUser> selectAllInitPassWord();

    /**
     * 根据条件获取用户信息
     *
     * @param sysUser
     */
    List<SysUser> selectAllUserInfoByterm(SysUser sysUser);

    /**
     * 获取所有的userId
     *
     * @return
     */
    List<String> queryUserIds();

    List<String> selectNameList(String[] userIds);

    List<SysUserVo> selectUserListByUserId(String[] userId);

    /**
     * 获取任务统计模块的用户信息
     *
     * @param user
     * @return
     */
    List<SysUser> selectStatisticUserList(SysUser user);

}
