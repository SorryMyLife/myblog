package org.myblog.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.codec.binary.Base64;
import org.myblog.blog.entity.BlogAdminUser;
import org.myblog.blog.entity.BlogCode;
import org.myblog.blog.entity.BlogLogin;
import org.myblog.blog.entity.BlogUser;
import org.myblog.blog.extend.type.AdminUserType;
import org.myblog.blog.extend.type.searchMultType;
import org.myblog.blog.mapper.BlogCodeMapper;
import org.myblog.blog.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.myblog.blog.vo.admin.AdminCodeList;
import org.myblog.blog.vo.admin.AdminLoginList;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author test
 * @since 2021-07-17
 */
@Service
public class BlogCodeServiceImpl extends ServiceImpl<BlogCodeMapper, BlogCode> implements BlogCodeService {

    @Autowired
    BlogAdminUserService blogAdminUserService;

    @Autowired
    BlogRsaService blogRsaService;

    @Autowired
    BlogUserService blogUserService;

    @Autowired
    BlogRoleService blogRoleService;

    @Override
    public void truncate() {
        this.getBaseMapper().truncate();
    }

    @Override
    public String[] getCode(int codelen, int codeheight) {
        int len = codelen;
        int height = codeheight;
        int width = 10* (len * 2);
        BufferedImage buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 创建一个随机数生成器类。
        Random random = new Random();
        // 设定图像背景色(因为是做背景，所以偏淡)
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font("Times New Roman", Font.HANGING_BASELINE, 28);      // 设置字体。
        g.setFont(font);
        // 画边框。
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到。
        //g.setColor(Color.GRAY);
        g.setColor(getRandColor(160,200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        // 设置备选验证码:包括"a-z"和数字"0-9"
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int size = base.length();
        // 随机产生4位数字的验证码。
        for (int i = 0; i < len; i++) {
            // 得到随机产生的验证码数字。
            int start = random.nextInt(size);
            String strRand = base.substring(start, start + 1);
            // 用随机产生的颜色将验证码绘制到图像中。
            // 生成随机颜色(因为是做前景，所以偏深) //g.setColor(getRandColor(1, 100)); 81
            //调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
            g.drawString(strRand, 15 * i + 6, 24);
            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }

        g.dispose();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(buffImg, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] encodeBase64 = Base64.encodeBase64(outputStream.toByteArray());
        String outbase64 = "data:image/png;base64,";

        String base64str = outbase64+new String(encodeBase64);
        String codestr = randomCode.toString();
        String data[] = {
                codestr,base64str
        };
        System.out.println(codestr + " --- " + base64str);
        return  data;
    }

    @Override
    public BlogCode getBlogCode(String uid, String code) {
        QueryWrapper<BlogCode> blogCodeQueryWrapper = new QueryWrapper<>();
        blogCodeQueryWrapper.lambda().eq(BlogCode::getUid,uid).eq(BlogCode::getCode,code);
        return this.getOne(blogCodeQueryWrapper);
    }

    @Override
    public List<HashMap<String, Object>> listAdminCodeList(String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if (adminUser != null && adminUser.getRid() < AdminUserType.user.getId()) {
            return getAdminCodes(valueOf,valueOf1);
        }
        return null;
    }

    @Override
    public List<HashMap<String, Object>> getAdminCodes(Integer valueOf, Integer valueOf1) {
        Page<BlogCode> blogCodePage = new Page<>(valueOf, valueOf1);
        Page<BlogCode> page = this.page(blogCodePage);
        return getAdminCodes(page);
    }

    @Override
    public List<HashMap<String, Object>> getAdminCodes(Page<BlogCode> page) {
        List<HashMap<String, Object>> collect = page.getRecords().stream().map(blogCode -> {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("clist", blogCode);
            hashMap.put("total", page.getTotal());
            return hashMap;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public boolean adminCodeChange(BlogCode blogCode, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            if(blogCode.getUid() != null){
                return this.update(blogCode,new QueryWrapper<BlogCode>().eq("uid",blogCode.getUid()).eq("cid",blogCode.getCid()));
            }else{
                return this.save(blogCode);
            }
        }
        return false;
    }

    private void removeAdminByUID(String u,String cid) {
        QueryWrapper<BlogCode> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(BlogCode::getUid,u).eq(BlogCode::getCid,cid);
        this.remove(wrapper);
    }

    @Override
    public boolean adminCodeDelete(String codeids, String cookieUid, LocalDateTime nowTime) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            String[] split = codeids.split(",");
            if (split != null) {
                for (String u : split) {
                    String[] split1 = u.split("----");
                    removeAdminByUID(split1[0], split1[1]);
                }
                return true;
            }else{
                try {
                    String[] split1 = codeids.split("----");
                    removeAdminByUID(split1[0], split1[1]);
                    return true;
                }catch (Exception e){
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public List<HashMap<String, Object>> searchMultAdminCode(String s_str, String s_types, String cookieUid, Integer valueOf, Integer valueOf1) {
        String decrypt = blogRsaService.decrypt(cookieUid);
        BlogAdminUser adminUser = blogAdminUserService.getAdminUser(Integer.valueOf(decrypt));
        if(adminUser != null && adminUser.getRid() < AdminUserType.user.getId()){
            if(s_types.equals("") && s_str.equals("")){
                return listAdminCodeList(cookieUid, valueOf, valueOf1);
            }
            Page<BlogCode> blogCodePage = new Page<>(valueOf, valueOf1);
            QueryWrapper<BlogCode> wrapper = new QueryWrapper<>();
            String[] split = s_types.split(",");
            if(split != null){
                for (String s : split) {
                    String byCloum = searchMultType.getByCloum(s);
                    if(byCloum!=null){
                        wrapper.or().like(byCloum,s_str);
                    }
                }
            }else{
                String byCloum = searchMultType.getByCloum(s_types);
                if(byCloum!=null){
                    wrapper.like(byCloum,s_str);
                }
            }
            Page<BlogCode> page = this.page(blogCodePage, wrapper);
            return getAdminCodes(page);
        }
        return null;
    }

    public Color getRandColor(int fc, int bc) {// 给定范围获得随机颜色
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
