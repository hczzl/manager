package com.ruoyi.framework.util;

import org.springframework.beans.factory.annotation.Value;

import java.sql.*;

/**
 * @Author SuQZ
 * @Date 2019/11/21 17:13
 * @Version 1.0
 */
public class JDBCUtile {
    private static final String URL = " jdbc:mysql://120.24.149.172:3306/dep_manager?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "glch.213";
    /*private static final String URL = "jdbc:mysql://localhost:3306/dep_manager?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";*/

    private static final Connection CONN = getConnection();


    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void colse(Connection connection, PreparedStatement pstm, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id获取任务信息
     *
     * @param tid
     * @return
     * @throws SQLException
     */
    public static String getTaskTitleByTid(String tid) throws SQLException {
        String title = "";
        String sql = "SELECT task_title FROM task_table WHERE t_id= ?";
        PreparedStatement pstm = CONN.prepareStatement(sql);
        pstm.setString(1, tid);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            title = resultSet.getString("task_title");
        }
        colse(null, pstm, resultSet);
        return title;
    }

    /**
     * 根据用户id获取用户名
     *
     * @param userId
     * @return
     * @throws SQLException
     */
    public static String getUserNameByid(String userId) throws SQLException {
        String userName = "";
        String sql = "SELECT user_name FROM sys_user WHERE user_id= ?";
        PreparedStatement pstm = CONN.prepareStatement(sql);
        pstm.setString(1, userId);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            userName = resultSet.getString("user_name");
        }
        colse(null, pstm, resultSet);
        return userName;
    }

    /**
     * 根据审批流id获取任务标题
     *
     * @param currentId
     * @return
     * @throws SQLException
     */
    public static String getAdultTaskTitleByCurrentId(String currentId) throws SQLException {
        String title = "";
        String sql = "SELECT task_title FROM task_table WHERE t_id=(SELECT apply_id FROM audit_flow_current WHERE current_id= ?)";
        PreparedStatement pstm = CONN.prepareStatement(sql);
        pstm.setString(1, currentId);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            title = resultSet.getString("task_title");
        }
        //如果从任务中获取为空，那么从项目表中获取
        if ("".equals(title)) {
            sql = "SELECT title FROM project_table WHERE p_id=(SELECT apply_id FROM audit_flow_current WHERE current_id= ?)";
            pstm = CONN.prepareStatement(sql);
            pstm.setString(1, currentId);
            resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                title = resultSet.getString("title");
            }
        }
        colse(null, pstm, resultSet);
        return title;
    }

    /**
     * 根据项目id获取项目名
     *
     * @param pid
     * @return
     * @throws SQLException
     */
    public static String getProjectNameById(String pid) throws SQLException {
        String title = "";
        String sql = "SELECT title FROM project_table WHERE p_id=?";
        PreparedStatement pstm = CONN.prepareStatement(sql);
        pstm.setString(1, pid);
        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            title = resultSet.getString("title");
        }
        colse(null, pstm, resultSet);
        return title;
    }

}
