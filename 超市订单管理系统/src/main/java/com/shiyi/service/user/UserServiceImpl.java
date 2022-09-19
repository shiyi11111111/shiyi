package com.shiyi.service.user;

import com.shiyi.dao.BaseDao;
import com.shiyi.dao.user.UserDao;
import com.shiyi.dao.user.UserDaoImpl;
import com.shiyi.pojo.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService{

    //引入Dao层
    private UserDao userDao;
    public UserServiceImpl(){
        userDao = new UserDaoImpl();
    }

    @Override
    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;


        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }

        if(null != user){
            if(!user.getUserPassword().equals(password)){
                user = null;
            }
        }
        return user;
    }

    //根据用户id修改密码
    //通过返回的参数flag判断是否修改成功
    public boolean updatePwd(int id, String password) {
        boolean flag = false;
        Connection connection = null;
        try{
            connection = BaseDao.getConnection();
            if(userDao.updatePwd(connection,id,password) > 0)
                flag = true;
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public int getUserCount(String queryUserName, int queryUserRole) {
        int count=0;
        Connection connection=null;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection, queryUserName, queryUserRole);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection, null, null);
        }
        return count;
    }

    @Override
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;
        System.out.println("queryUserName ---- > " + queryUserName);
        System.out.println("queryUserRole ---- > " + queryUserRole);
        System.out.println("currentPageNo ---- > " + currentPageNo);
        System.out.println("pageSize ---- > " + pageSize);
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName,queryUserRole,currentPageNo,pageSize);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }

    @Override
    public Boolean add(User user) {
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();//获得连接
            connection.setAutoCommit(false);//开启JDBC事务管理
            int updateRows = userDao.add(connection,user);
            connection.commit();
            if(updateRows > 0){
                flag = true;
                System.out.println("add success!");
            }else{
                System.out.println("add failed!");
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {
                System.out.println("rollback==================");
                connection.rollback();//失败就回滚
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }finally{
            //在service层进行connection连接的关闭
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Override
    public Boolean modify(User user) throws Exception {
        Boolean flag=false;
        Connection connection=null;
        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务
            int updateNum = userDao.modify(connection, user);//执行修改sql
            connection.commit();//提交事务
            if(updateNum>0){
                flag=true;
                System.out.println("修改用户成功");
            }else{
                System.out.println("修改用户失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //若抛出异常，则说明修改失败需要回滚
            System.out.println("修改失败，回滚事务");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    @Override
    public User selectUserCodeExist(String userCode) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }

    @Override
    public boolean deleteUserById(Integer delId) {
        Boolean flag=false;
        Connection connection=null;
        connection=BaseDao.getConnection();
        try {
            connection.setAutoCommit(false);
            int deleteNum=userDao.deleteUserById(connection,delId);
            connection.commit();
            if(deleteNum>0)flag=true;
        } catch (Exception e) {
            try {
                connection.rollback();//失败就回滚
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }

    public User getUserById(String id) {
        User user = new User();
        Connection connection=null;
        try {
            connection=BaseDao.getConnection();
            connection.setAutoCommit(false);
            user = userDao.getUserById(connection,id);
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                connection.rollback();//失败就回滚
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return  user;
    }
}
