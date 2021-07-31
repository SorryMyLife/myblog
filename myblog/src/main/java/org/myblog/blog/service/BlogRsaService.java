package org.myblog.blog.service;

import org.myblog.blog.entity.BlogRsa;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
public interface BlogRsaService extends IService<BlogRsa> {

    public void truncate();

    public String decrypt(String encrypt);

    public String encrypt(String srcData);

}
