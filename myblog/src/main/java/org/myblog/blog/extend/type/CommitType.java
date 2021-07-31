package org.myblog.blog.extend.type;

public enum  CommitType {
    article(0,"文章"),file(1,"文件"),
    like(2,"点赞收藏"),fans(3,"粉丝");

    public Integer getCommitcode() {
        return commitcode;
    }

    public void setCommitcode(Integer commitcode) {
        this.commitcode = commitcode;
    }

    public String getCommitname() {
        return commitname;
    }

    public void setCommitname(String commitname) {
        this.commitname = commitname;
    }

    private  Integer commitcode;
    private  String commitname;

    CommitType(Integer commitcode, String commitname) {
        this.commitcode = commitcode;
        this.commitname = commitname;
    }
    public static CommitType getByKEY(String key){
        for (CommitType value : values()) {
            if(value.getCommitname().equals(key)){
                return value;
            }
        }
        return null;
    }

    public static CommitType getByID(Integer id){
        for (CommitType value : values()) {
            if(value.getCommitcode().equals(id)){
                return value;
            }
        }
        return null;
    }

}
