package org.myblog.blog.extend.type;

public enum  searchMultType {
    uname("uname","name"),
    uid("uid","id"),
    rname("rname","name"),
    uaddr("uaddr","address"),
    loginType("loginType","login_type"),
    phone("phone","phone"),
    autograph("autograph","autograph"),
    email("email","email"),article_title("atitle","title"),article_id("aid","id"),
    article_text("atext","text"),article_html("ahtml","html"),file_name("fname","name"),
    file_id("fid","id"),file_text("ftext","text"),commit_title("ctitle","title"),
    commit_id("cid","id"),commit_text("ctext","msg"),role_title("rname","name"),
    role_id("rid","id"),role_user_uid("ruuid","uid"),role_user_rid("rurid","rid"),
    login_uid("luid","uid"),login_error_count("lec","login_error_count"),login_ip("ip","ip_addr"),
    code_uid("cuid","uid"),code_cid("ccid","cid"),code_code("ccode","code"),verif_uid("vuid","uid"),
    verif_x("vx","slider_x"),verif_cid("vcid","cid")

    ;

    public static searchMultType getByKey(String key){
        for (searchMultType value : values()) {
            if(value.getKey().equals(key)){
                return  value;
            }
        }
        return null;
    }

    public static String getByCloum(String key){
        return getByKey(key).getValue();
    }

    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    searchMultType(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
