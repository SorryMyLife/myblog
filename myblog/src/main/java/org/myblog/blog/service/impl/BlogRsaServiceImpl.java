package org.myblog.blog.service.impl;

import org.myblog.blog.entity.BlogRsa;
import org.myblog.blog.extend.utils.RSAUtils;
import org.myblog.blog.mapper.BlogRsaMapper;
import org.myblog.blog.service.BlogRsaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@Service
public class BlogRsaServiceImpl extends ServiceImpl<BlogRsaMapper, BlogRsa> implements BlogRsaService {

    RSAUtils rsaUtils = new RSAUtils(512);


    @Override
    public void truncate() {
        this.getBaseMapper().truncate();
    }

    @Override
    public String decrypt(String encrypt) {
        try {
            String privateKey = this.list().get(0).getPrivateKey();
            try {
                return rsaUtils.getDecrypt(encrypt, privateKey);
            }catch (Exception e){
                throw new RuntimeException("rsa解密失败");
            }
        }catch (Exception e){
            throw new RuntimeException("私钥不存在");
        }
    }

    @Override
    public String encrypt(String srcData) {
        if(this.count() < 1){
            BlogRsa blogRsa = new BlogRsa();
            String privateKey = rsaUtils.getPrivateKey();
            String publicKey = rsaUtils.getPublicKey();
            blogRsa.setPrivateKey(privateKey);
            blogRsa.setPublicKey(publicKey);
            this.save(blogRsa);
            return rsaUtils.getEncrypt(srcData,publicKey);
        }
        return rsaUtils.getEncrypt(srcData, this.list().get(0).getPublicKey());
    }

}
