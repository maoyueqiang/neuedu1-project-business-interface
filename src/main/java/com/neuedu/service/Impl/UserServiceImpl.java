package com.neuedu.service.Impl;

import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserInfoMapper;
import com.neuedu.pojo.UserInfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import com.neuedu.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    UserInfoMapper userInfoMapper;


    @Override
    //登录
    public ServerResponse login(UserInfo userInfo){

        //参数非空校验
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }
        if(userInfo.getUsername()==null){
            return ServerResponse.createServerResponseByFail("用户名不能为空");
        }
        if(userInfo.getPassword()==null){
            return ServerResponse.createServerResponseByFail("密码不能为空");
        }

        //用户名是否存在
        int count = userInfoMapper.existUsername(userInfo.getUsername());
        if(count==0){
            return ServerResponse.createServerResponseByFail("用户名不存在");
        }

        //根据用户名和密码获得用户
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        UserInfo userInfo1= userInfoMapper.findByUsernameAndPassword(userInfo);
        if(userInfo1==null){
            return ServerResponse.createServerResponseByFail("密码错误");
        }

        userInfo1.setPassword("");
        return ServerResponse.createServerResponseBySuccess(userInfo1);
    }

    @Override
    //注册
    public ServerResponse register(UserInfo userInfo) {
        //参数非空校验
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }
        if(userInfo.getUsername()==null){
            return ServerResponse.createServerResponseByFail("用户名不能为空");
        }
        if(userInfo.getPassword()==null){
            return ServerResponse.createServerResponseByFail("密码不能为空");
        }
        if(userInfo.getQuestion()==null){
            return ServerResponse.createServerResponseByFail("问题不能为空");
        }
        if(userInfo.getAnswer()==null){
            return ServerResponse.createServerResponseByFail("问题答案不能为空");
        }
        if(userInfo.getEmail()==null){
            return ServerResponse.createServerResponseByFail("邮箱不能为空");
        }
        if(userInfo.getPhone()==null){
            return ServerResponse.createServerResponseByFail("手机号不能为空");
        }
        if(userInfo.getIp()==null){
            return ServerResponse.createServerResponseByFail("ip不能为空");
        }

        //校验用户名
        int count=userInfoMapper.existUsername(userInfo.getUsername());
        if(count>0){
            return ServerResponse.createServerResponseByFail("用户名已存在");
        }

        //校验邮箱
        count=userInfoMapper.existEmail(userInfo.getEmail());
        if(count>0){
            return ServerResponse.createServerResponseByFail("邮箱已存在");
        }

        //注册
        userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
        userInfo.setPassword(MD5Utils.getMD5Code(userInfo.getPassword()));
        userInfo.setImage("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=310329945,3969783838&fm=26&gp=0.jpg");
        count = userInfoMapper.insert(userInfo);
        if(count==0){
            return ServerResponse.createServerResponseByFail("注册失败");
        }
        return ServerResponse.createServerResponseByFail("注册成功");
    }

    @Override
    //找到密保问题
    public ServerResponse forget_getQuestion(String username) {

        //非空判断
        if(username==null){
            return ServerResponse.createServerResponseByFail("用户名不能为空");
        }

        //校验username
        int count = userInfoMapper.existUsername(username);
        if(count==0){
            return ServerResponse.createServerResponseByFail("用户名不存在，请重新输入");
        }

        //查找密保问题
        String question = userInfoMapper.findQuestionByUsername(username);
        if(question==null){
            return ServerResponse.createServerResponseByFail("无密保问题");
        }

        return ServerResponse.createServerResponseBySuccess(question);
    }

    @Override
    //验证密保答案
    public ServerResponse forget_checkAnswer(String username, String question, String answer) {
        //非空校验
        if(username==null){
            return ServerResponse.createServerResponseByFail("用户名不能为空");
        }
        if(question==null){
            return ServerResponse.createServerResponseByFail("密保问题不能为空");
        }
        if(answer==null){
            return ServerResponse.createServerResponseByFail("密保答案不能为空");
        }

        //查询正确
        int count = userInfoMapper.checkAnswer(username,question,answer);
        if(count==0){
            return ServerResponse.createServerResponseByFail("答案错误");
        }

        //服务端生成token
        String forgetToken = UUID.randomUUID().toString();
        //guava cache
        TokenCache.set(username,forgetToken);



        return ServerResponse.createServerResponseBySuccess(forgetToken);
    }

    @Override
    public ServerResponse forget_resetPassword(String username, String password, String forgetToken) {
        //非空校验
        if(username==null){
            return ServerResponse.createServerResponseByFail("用户名不能为空");
        }
        if(password==null){
            return ServerResponse.createServerResponseByFail("密码不能为空");
        }
        if(forgetToken==null){
            return ServerResponse.createServerResponseByFail("token不能为空");
        }

        //校验用户
        String token = TokenCache.get(username);
        if(token==null){
            return ServerResponse.createServerResponseByFail("token过期，请重新获取token");
        }
        if(!token.equals(forgetToken)){
            return ServerResponse.createServerResponseByFail("无效的token");
        }

        //重置密码
        int count = userInfoMapper.resetPassword(username,MD5Utils.getMD5Code(password));
        if(count==0){
            return ServerResponse.createServerResponseByFail("密码修改失败");
        }

        return ServerResponse.createServerResponseBySuccess();
    }

    @Override
    //注册前校验用户名或邮箱是否有效
    public ServerResponse check_valid(String str, String type) {
        //非空校验
        if(str==null){
            return ServerResponse.createServerResponseByFail("用户名或邮箱不能为空");
        }
        if(type==null){
            return ServerResponse.createServerResponseByFail("校验类型不能为空");
        }

        //校验用户名
        if(type.equals("username")){
            int count = userInfoMapper.checkUsername(str);
            if(count>0){
                return ServerResponse.createServerResponseByFail("用户名已存在");
            }
            return ServerResponse.createServerResponseBySuccess();
        }
        //校验邮箱
        else if(type.equals("email")){
            int count = userInfoMapper.checkEmail(str);
            if(count>0){
                return ServerResponse.createServerResponseByFail("邮箱已存在");
            }
            return ServerResponse.createServerResponseBySuccess();
        }
        else{
            return ServerResponse.createServerResponseByFail("参数错误");
        }

    }

    @Override
    //登陆状态修改密码
    public ServerResponse login_resetpassword(String username,String oldPassword, String newPassword) {
        //非空校验
        if(oldPassword==null){
            return ServerResponse.createServerResponseByFail("旧密码不能为空");
        }
        if(newPassword==null){
            return ServerResponse.createServerResponseByFail("新密码不能为空");
        }

        //找到用户
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(MD5Utils.getMD5Code(oldPassword));
        UserInfo userInfo1=userInfoMapper.findByUsernameAndPassword(userInfo);

        if(userInfo1==null){
            return ServerResponse.createServerResponseByFail("旧密码错误");
        }
        //修改密码
        userInfo1.setPassword(MD5Utils.getMD5Code(newPassword));
        int count = userInfoMapper.updateByPrimaryKey(userInfo1);
        if(count>0){
            return ServerResponse.createServerResponseBySuccess("密码修改成功");
        }
        return ServerResponse.createServerResponseByFail("密码修改失败");

    }

    @Override
    //登陆状态修改个人信息
    public ServerResponse login_updateuser(UserInfo userInfo) {
        //非空校验
        if(userInfo==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }

        //修改信息
        int count = userInfoMapper.updateByPrimaryKey(userInfo);
        if(count>0){
            return ServerResponse.createServerResponseBySuccess("用户信息修改成功");
        }
        return ServerResponse.createServerResponseByFail("用户信息修改失败");

    }

//    @Override
//    public List<UserInfo> findAll(){
//        return userInfoMapper.selectAll();
//    }
//
//    public int updateUser(UserInfo userInfo){
//        return userInfoMapper.updateByPrimaryKey(userInfo);
//    }
//
    @Override
    public UserInfo findUserById(int id){
        return userInfoMapper.selectByPrimaryKey(id);
    }
//
//    @Override
//    public int addUserInfo(UserInfo userInfo){
//        return userInfoMapper.insert(userInfo);
//    }
//
//    @Override
//    public int deleteUserInfo(int id){
//        return userInfoMapper.deleteByPrimaryKey(id);
//    }
}
