package org.myblog.blog.extend.type;

public enum  LoginType {
    uid(0,"用户ID"),email(1,"邮箱"),phone(2,"手机号");
    private Integer logincode;
    private String loginname;

    LoginType(Integer logincode, String loginname) {
        this.logincode = logincode;
        this.loginname = loginname;
    }

    public Integer getLogincode() {
        return logincode;
    }

    public void setLogincode(Integer logincode) {
        this.logincode = logincode;
    }

    public String getLoginname() {
        return loginname;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public static LoginType getByID(Integer id){
        for (LoginType loginType : values()) {
            if(loginType.getLogincode().equals(id)){
                return loginType;
            }
        }
        return  null;
    }
}
