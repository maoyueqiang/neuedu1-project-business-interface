package com.neuedu.controller.portal;


import com.neuedu.VO.UserVO;
import com.neuedu.common.Const;
import com.neuedu.common.ServerResponse;
import com.neuedu.entity.User;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/user/")
@CrossOrigin
public class UserController {

    @Autowired
    IUserService userService;

    /**
     * 登录
     */
    @RequestMapping(value = "login.do")
    public ServerResponse login(String userName, String userPwd,
                                HttpSession session){
        User user = new User();
        user.setUserName(userName);
        user.setUserPwd(userPwd);
        ServerResponse serverResponse = userService.login(user);
        if(serverResponse.isSuccess()){
            User user1 = (User) serverResponse.getData();
            session.setAttribute(Const.CUTTENTUSER,user1);
        }
        return serverResponse;
    }


    /**
     * 注册
     */
    @RequestMapping(value = "register.do")
    public ServerResponse register(UserVO userVO, HttpSession session){
        ServerResponse serverResponse = userService.register(userVO);
        return serverResponse;
    }


//    /**
//     * 根据用户名查询密保问题
//     */
//    @RequestMapping(value = "forget_getQuestion.do")
//    public ServerResponse forget_getQuestion(String username){
//        ServerResponse serverResponse = userService.forget_getQuestion(username);
//
//        return serverResponse;
//    }
//    /**
//     * 验证问题答案是否正确
//     */
//    @RequestMapping(value = "forget_checkAnswer.do")
//    public ServerResponse forget_checkAnswer(String username,String question,String answer){
//        ServerResponse serverResponse = userService.forget_checkAnswer(username,question,answer);
//
//        return serverResponse;
//    }
//    /**
//     * 未登录重置密码
//     */
//    @RequestMapping(value = "forget_resetPassword.do")
//    public ServerResponse forget_resetPassword(String username,String password,String forgetToken){
//        ServerResponse serverResponse = userService.forget_resetPassword(username,password,forgetToken);
//
//        return serverResponse;
//    }
//
//
//    /**
//     * 检查用户名或邮箱是否有效
//     */
//    @RequestMapping(value = "check_valid.do")
//    public ServerResponse check_valid(String str,String type){
//        ServerResponse serverResponse = userService.check_valid(str,type);
//
//        return serverResponse;
//    }
//
//    /**
//     * 检查登录用户信息
//     */
//    @RequestMapping(value = "getuser.do")
//    public ServerResponse getuser(HttpSession session){
//        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CUTTENTUSER);
//
//        userInfo.setPassword("");
//        userInfo.setQuestion("");
//        userInfo.setAnswer("");
//        userInfo.setRole(null);
//        userInfo.setIp(null);
//        return ServerResponse.createServerResponseBySuccess(userInfo);
//    }
//
//    /**
//     * 登录状态重置密码
//     */
//    @RequestMapping(value = "login_resetpassword.do")
//    public ServerResponse login_resetpassword(HttpSession session,String oldPassword,String newPassword){
//        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CUTTENTUSER);
//
//        ServerResponse serverResponse = userService.login_resetpassword(userInfo.getUsername(),oldPassword,newPassword);
//        if(serverResponse.isSuccess()){
//            UserInfo userInfo1 = userService.findUserById(userInfo.getId());
//            session.setAttribute(Const.CUTTENTUSER,userInfo1);
//        }
//        return serverResponse;
//    }
//
//    /**
//     * 登录状态更新个人信息
//     */
//    @RequestMapping(value = "login_updateuser.do")
//    public ServerResponse login_updateuser(HttpSession session,UserInfo user){
//        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CUTTENTUSER);
//
//        user.setId(userInfo.getId());
//        ServerResponse serverResponse = userService.login_updateuser(user);
//        if(serverResponse.isSuccess()){
//            UserInfo userInfo1 = userService.findUserById(userInfo.getId());
//            session.setAttribute(Const.CUTTENTUSER,userInfo1);
//        }
//        return serverResponse;
//    }
//
//    /**
//     * 获取登录用户详细信息
//     */
//    @RequestMapping(value = "login_getuser.do")
//    public ServerResponse login_getuser(HttpSession session){
//        UserInfo userInfo=(UserInfo) session.getAttribute(Const.CUTTENTUSER);
//
//        userInfo.setPassword("");
//        return ServerResponse.createServerResponseBySuccess(userInfo);
//    }
//
//    /**
//     * 退出登录
//     */
//    @RequestMapping(value = "logout.do")
//    public ServerResponse logout(HttpSession session){
//        session.removeAttribute(Const.CUTTENTUSER);
//        UserInfo userInfo=(UserInfo)session.getAttribute(Const.CUTTENTUSER);
//        if(userInfo==null){
//            return ServerResponse.createServerResponseBySuccess();
//        }
//        else{
//            return ServerResponse.createServerResponseByFail("服务端异常");
//        }
//    }
//
//

}
