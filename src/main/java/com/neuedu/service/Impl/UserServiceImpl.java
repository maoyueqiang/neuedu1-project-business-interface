package com.neuedu.service.Impl;

import com.neuedu.VO.UserVO;
import com.neuedu.common.ServerResponse;
import com.neuedu.dao.UserMapper;
import com.neuedu.dao.UserinfoMapper;
import com.neuedu.entity.User;
import com.neuedu.entity.Userinfo;
import com.neuedu.service.IUserService;
import com.neuedu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserinfoMapper userInfoMapper;


    @Override
    //登录
    public ServerResponse login(User user){

        //参数非空校验
        if(user==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }
        if(user.getUserName()==null || user.getUserName().equals("")){
            return ServerResponse.createServerResponseByFail("用户名不能为空");
        }
        if(user.getUserPwd()==null || user.getUserPwd().equals("")){
            return ServerResponse.createServerResponseByFail("密码不能为空");
        }

        //用户名是否存在
        int count = userMapper.existUserNameOrEmail(user.getUserName());
        if(count==0){
            return ServerResponse.createServerResponseByFail("用户名不存在");
        }

        //根据用户名和密码获得用户
        user.setUserPwd(MD5Utils.getMD5Code(user.getUserPwd()));
        User user1= userMapper.findByUsernameAndPassword(user);
        if(user1==null){
            return ServerResponse.createServerResponseByFail("密码错误");
        }

        user1.setUserPwd("");
        return ServerResponse.createServerResponseBySuccess(user1);
    }

    @Override
    @Transactional
    //注册
    public ServerResponse register(UserVO userVO) {
        if(userVO==null){
            return ServerResponse.createServerResponseByFail("参数不能为空");
        }
        if(userVO.getUserName()==null || userVO.getUserName().equals("")){
            return ServerResponse.createServerResponseByFail("用户名不能为空");
        }
        //校验用户名
        int count=userMapper.existUserName(userVO.getUserName());
        if(count>0){
            return ServerResponse.createServerResponseByFail("用户名已存在");
        }
        if(userVO.getUserEmail()==null || userVO.getUserEmail().equals("")){
            return ServerResponse.createServerResponseByFail("邮箱不能为空");
        }
        //校验邮箱
        count=0;
        count = userMapper.existUserEmail(userVO.getUserEmail());
        if(count>0){
            return ServerResponse.createServerResponseByFail("邮箱已存在");
        }
        if(userVO.getUserAuthor()==null || userVO.getUserAuthor().equals("")){
            return ServerResponse.createServerResponseByFail("权限不能为空");
        }
        if(userVO.getUserPwd()==null || userVO.getUserPwd().equals("")){
            return ServerResponse.createServerResponseByFail("密码不能为空");
        }
        if(userVO.getUserConfirmPwd()==null || userVO.getUserConfirmPwd().equals("")){
            return ServerResponse.createServerResponseByFail("确认密码不能为空");
        }
        if(!userVO.getUserPwd().equals(userVO.getUserConfirmPwd())){
            return ServerResponse.createServerResponseByFail("两次密码输入不一致");
        }
        if(userVO.getUserinfoIdcard()==null || userVO.getUserinfoIdcard().equals("")){
            return ServerResponse.createServerResponseByFail("身份证号不能为空");
        }
        //校验身份证号
        count=0;
        count = userInfoMapper.existUserinfoIdcard(userVO.getUserinfoIdcard());
        if(count>0){
            return ServerResponse.createServerResponseByFail("身份证号已存在");
        }

        //添加用户信息
        Userinfo userInfo = new Userinfo();
        userInfo.setUserinfoName(userVO.getUserinfoName());
        userInfo.setUserinfoIdcard(userVO.getUserinfoIdcard());
        userInfo.setUserinfoSex(userVO.getUserinfoSex());
        userInfo.setUserinfoBirth(userVO.getUserinfoBirth());
        userInfo.setUserinfoAddr(userVO.getUserinfoAddr());
        count=0;
        count=userInfoMapper.insert(userInfo);
        if(count<=0){
            return ServerResponse.createServerResponseByFail("注册失败");
        }
        Userinfo userInfoTemp = userInfoMapper.selectUserInfoByIdCard(userInfo.getUserinfoIdcard());

        //添加用户
        User user = new User();
        user.setUserinfoId(userInfoTemp.getUserinfoId());
        user.setUserName(userVO.getUserName());
        user.setUserEmail(userVO.getUserEmail());
        user.setUserPhone(userVO.getUserPhone());
        user.setUserPwd(MD5Utils.getMD5Code(userVO.getUserPwd()));
        user.setUserAuthor(userVO.getUserAuthor());
        count=0;
        count=userMapper.insert(user);
        if(count<=0){
            return ServerResponse.createServerResponseByFail("注册失败");
        }

        return ServerResponse.createServerResponseBySuccess("注册成功");
    }

//    @Override
//    //找到密保问题
//    public ServerResponse forget_getQuestion(String username) {
//
//        //非空判断
//        if(username==null){
//            return ServerResponse.createServerResponseByFail("用户名不能为空");
//        }
//
//        //校验username
//        int count = userInfoMapper.existUsername(username);
//        if(count==0){
//            return ServerResponse.createServerResponseByFail("用户名不存在，请重新输入");
//        }
//
//        //查找密保问题
//        String question = userInfoMapper.findQuestionByUsername(username);
//        if(question==null){
//            return ServerResponse.createServerResponseByFail("无密保问题");
//        }
//
//        return ServerResponse.createServerResponseBySuccess(question);
//    }
//
//    @Override
//    //验证密保答案
//    public ServerResponse forget_checkAnswer(String username, String question, String answer) {
//        //非空校验
//        if(username==null){
//            return ServerResponse.createServerResponseByFail("用户名不能为空");
//        }
//        if(question==null){
//            return ServerResponse.createServerResponseByFail("密保问题不能为空");
//        }
//        if(answer==null){
//            return ServerResponse.createServerResponseByFail("密保答案不能为空");
//        }
//
//        //查询正确
//        int count = userInfoMapper.checkAnswer(username,question,answer);
//        if(count==0){
//            return ServerResponse.createServerResponseByFail("答案错误");
//        }
//
//        //服务端生成token
//        String forgetToken = UUID.randomUUID().toString();
//        //guava cache
//        TokenCache.set(username,forgetToken);
//
//
//
//        return ServerResponse.createServerResponseBySuccess(forgetToken);
//    }
//
//    @Override
//    public ServerResponse forget_resetPassword(String username, String password, String forgetToken) {
//        //非空校验
//        if(username==null){
//            return ServerResponse.createServerResponseByFail("用户名不能为空");
//        }
//        if(password==null){
//            return ServerResponse.createServerResponseByFail("密码不能为空");
//        }
//        if(forgetToken==null){
//            return ServerResponse.createServerResponseByFail("token不能为空");
//        }
//
//        //校验用户
//        String token = TokenCache.get(username);
//        if(token==null){
//            return ServerResponse.createServerResponseByFail("token过期，请重新获取token");
//        }
//        if(!token.equals(forgetToken)){
//            return ServerResponse.createServerResponseByFail("无效的token");
//        }
//
//        //重置密码
//        int count = userInfoMapper.resetPassword(username,MD5Utils.getMD5Code(password));
//        if(count==0){
//            return ServerResponse.createServerResponseByFail("密码修改失败");
//        }
//
//        return ServerResponse.createServerResponseBySuccess();
//    }
//
//    @Override
//    //注册前校验用户名或邮箱是否有效
//    public ServerResponse check_valid(String str, String type) {
//        //非空校验
//        if(str==null){
//            return ServerResponse.createServerResponseByFail("用户名或邮箱不能为空");
//        }
//        if(type==null){
//            return ServerResponse.createServerResponseByFail("校验类型不能为空");
//        }
//
//        //校验用户名
//        if(type.equals("username")){
//            int count = userInfoMapper.checkUsername(str);
//            if(count>0){
//                return ServerResponse.createServerResponseByFail("用户名已存在");
//            }
//            return ServerResponse.createServerResponseBySuccess();
//        }
//        //校验邮箱
//        else if(type.equals("email")){
//            int count = userInfoMapper.checkEmail(str);
//            if(count>0){
//                return ServerResponse.createServerResponseByFail("邮箱已存在");
//            }
//            return ServerResponse.createServerResponseBySuccess();
//        }
//        else{
//            return ServerResponse.createServerResponseByFail("参数错误");
//        }
//
//    }
//
//    @Override
//    //登陆状态修改密码
//    public ServerResponse login_resetpassword(String username,String oldPassword, String newPassword) {
//        //非空校验
//        if(oldPassword==null){
//            return ServerResponse.createServerResponseByFail("旧密码不能为空");
//        }
//        if(newPassword==null){
//            return ServerResponse.createServerResponseByFail("新密码不能为空");
//        }
//
//        //找到用户
//        UserInfo userInfo=new UserInfo();
//        userInfo.setUsername(username);
//        userInfo.setPassword(MD5Utils.getMD5Code(oldPassword));
//        UserInfo userInfo1=userInfoMapper.findByUsernameAndPassword(userInfo);
//
//        if(userInfo1==null){
//            return ServerResponse.createServerResponseByFail("旧密码错误");
//        }
//        //修改密码
//        userInfo1.setPassword(MD5Utils.getMD5Code(newPassword));
//        int count = userInfoMapper.updateByPrimaryKey(userInfo1);
//        if(count>0){
//            return ServerResponse.createServerResponseBySuccess("密码修改成功");
//        }
//        return ServerResponse.createServerResponseByFail("密码修改失败");
//
//    }
//
//    @Override
//    //登陆状态修改个人信息
//    public ServerResponse login_updateuser(UserInfo userInfo) {
//        //非空校验
//        if(userInfo==null){
//            return ServerResponse.createServerResponseByFail("参数不能为空");
//        }
//
//        //修改信息
//        int count = userInfoMapper.updateByPrimaryKey(userInfo);
//        if(count>0){
//            return ServerResponse.createServerResponseBySuccess("用户信息修改成功");
//        }
//        return ServerResponse.createServerResponseByFail("用户信息修改失败");
//
//    }

//    @Override
//    public List<UserInfo> findAll(){
//        return userInfoMapper.selectAll();
//    }
//
//    public int updateUser(UserInfo userInfo){
//        return userInfoMapper.updateByPrimaryKey(userInfo);
//    }
//
//    @Override
//    public UserInfo findUserById(int id){
//        return userInfoMapper.selectByPrimaryKey(id);
//    }
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
