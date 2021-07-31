package org.myblog.blog.extend.type;

public enum ExcelExtractColumType {
    user(0),article(1),file(2),commit(3),role(4),roleuser(5),loginlist(6),textcode(7),slider(8);

    public static ExcelExtractColumType getByType(Integer t)
    {
        for (ExcelExtractColumType value : values()) {
            if(value.getType().equals(t)){
                return value;
            }
        }
        return null;
    }

    ExcelExtractColumType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    private Integer type;
}
