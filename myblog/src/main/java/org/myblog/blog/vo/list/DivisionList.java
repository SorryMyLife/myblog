package org.myblog.blog.vo.list;

import lombok.Data;

import java.util.List;

@Data
public class DivisionList {
    public String value,label;
    public List<DivisionList> children;
}
