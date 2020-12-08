package com.neuedu.controller.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台管理员
 */

@RestController
@RequestMapping(value = "/manage/user/")
public class UserManageController {

    @Autowired
    IUserService userService;

    /**
     * 登录
     */
    @RequestMapping(value = "login.do")
    public ServerResponse login(String username, String password,
                                HttpSession session) {
//        UserInfo userInfo = new UserInfo();
//        userInfo.setUsername(username);
//        userInfo.setPassword(password);
//
//        ServerResponse serverResponse = userService.login(userInfo);
//        if (serverResponse.isSuccess()) {
//            UserInfo userInfo1 = (UserInfo) serverResponse.getData();
//            if(userInfo1.getRole()==1){
//                return ServerResponse.createServerResponseByFail("用户无权限");
//            }
//            session.setAttribute(Const.CUTTENTUSER, userInfo1);
//        }
//        return serverResponse;
        return null;
    }

}