package com.neuedu.VO;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {

    private String userName;
    private String userEmail;
    private String userPhone;
    private String userAuthor;
    private String userPwd;
    private String userConfirmPwd;
    private String userinfoName;
    private String userinfoIdcard;
    private String userinfoSex;
    private Date userinfoBirth;
    private String userinfoAddr;

}
