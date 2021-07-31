package org.myblog.blog.extend;

import java.util.HashMap;

public class MsgResult extends HashMap {

    public static MsgResult add(ResultStatus resultStatus){
        MsgResult msgResult = new MsgResult();
        msgResult.add("code",resultStatus.getStatus());
        msgResult.add("msg",resultStatus.getMsg());
        return  msgResult;
    }

    public MsgResult add(Object key , Object data){
        super.put(key,data);
        return  this;
    }

    public MsgResult put(Object key,Object data){
        return  add(key,data);
    }

}
