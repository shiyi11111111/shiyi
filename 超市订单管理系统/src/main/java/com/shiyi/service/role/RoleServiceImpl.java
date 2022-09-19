package com.shiyi.service.role;

import com.shiyi.dao.BaseDao;
import com.shiyi.dao.role.RoleDao;
import com.shiyi.dao.role.RoleDaoImpl;
import com.shiyi.pojo.Role;
import org.junit.Test;

import java.beans.beancontext.BeanContextChild;
import java.sql.Connection;
import java.util.List;

public class RoleServiceImpl implements RoleService{
    private RoleDao roleDao;
    public RoleServiceImpl(){
        roleDao = new RoleDaoImpl();
    }

    @Override
    public List<Role> getRoleList() {
        Connection connection = null;
        List<Role> roleList = null;

        try {
            connection = BaseDao.getConnection();
            roleList = roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return roleList;
    }

//    @Test
//    public void test(){
//        RoleServiceImpl roleService = new RoleServiceImpl();
//        List<Role> roleList = roleService.getRoleList();
//        for(Role temp: roleList){
//            System.out.println(temp.getRoleName());
//        }
//    }
}
