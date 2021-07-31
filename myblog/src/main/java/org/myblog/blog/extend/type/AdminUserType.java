package org.myblog.blog.extend.type;

public enum  AdminUserType {
    root("root",0),system("system",1),admin("admin",2),admin2("admin2",3),user("user",4);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private String name;
    private Integer id;

    AdminUserType(String name, Integer id) {
        this.name = name;
        this.id = id;
    }
}
