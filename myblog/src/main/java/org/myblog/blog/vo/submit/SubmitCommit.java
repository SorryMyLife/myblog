package org.myblog.blog.vo.submit;

import lombok.Data;

@Data
public class SubmitCommit {
    public String msg,html;
    public Integer type,id;
}
