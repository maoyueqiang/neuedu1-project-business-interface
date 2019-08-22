package com.neuedu.service;

import com.neuedu.common.ServerResponse;
import com.neuedu.pojo.UserInfo;

import java.util.List;

public interface IUserService {
    public ServerResponse login(UserInfo userInfo);

    public ServerResponse register(UserInfo userInfo);

    public ServerResponse forget_getQuestion(String username);

    public ServerResponse forget_checkAnswer(String username,String question,String answer);

    public ServerResponse forget_resetPassword(String username,String password,String forgetToken);

    public ServerResponse check_valid(String str,String type);

    public ServerResponse login_resetpassword(String username,String oldPassword,String newPassword);

    public ServerResponse login_updateuser(UserInfo userInfo);


    //    public List<UserInfo> findAll();
//
//    public int updateUser(UserInfo userInfo);
//
    public UserInfo findUserById(int id);
//
//    public int addUserInfo(UserInfo userInfo);
//
//    public int deleteUserInfo(int id);
}
