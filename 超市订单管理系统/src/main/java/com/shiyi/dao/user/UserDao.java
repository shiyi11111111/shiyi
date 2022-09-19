package com.shiyi.dao.user;

import com.shiyi.pojo.User;
import com.shiyi.util.Constants;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    public User getLoginUser(Connection connection,String userCode) throws SQLException;

    public int updatePwd(Connection connection,int id,String password)throws SQLException;

    //根据用户输入的名字或者角色id来查询计算用户数量
    public int getUserCount(Connection connection, String userName, int userRole)throws Exception;

    //通过用户输入的条件查询用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws  Exception;

    //增加用户信息
    public  int add(Connection connection,User user) throws Exception;

    //通过用户id删除用户信息
    public int deleteUserById(Connection connection, Integer delId)throws Exception;

    //通过userId查看当前用户信息
    public User getUserById(Connection connection, String id)throws Exception;
    //修改用户信息
    public int modify(Connection connection, User user)throws Exception;

}
