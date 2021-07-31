package org.myblog.blog;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.myblog.blog.extend.utils.BlogHttpUtils;
import org.myblog.blog.service.BlogAdminTableColumnsService;
import org.myblog.blog.service.BlogAdminUserService;
import org.myblog.blog.service.BlogRoleService;
import org.myblog.blog.service.BlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Provider;

@SpringBootTest
@MapperScan("org.myblog.blog.mapper")
class BlogApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("hsalfjaslkdjf");
    }
//
//
//
//    @Test
//    void gentor(){
//        // 代码生成器
//        AutoGenerator mpg = new AutoGenerator();
//
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        String projectPath = System.getProperty("user.dir");
//        gc.setOutputDir(projectPath + "/src/main/java");
//        gc.setAuthor("test");
//        gc.setOpen(false);
//        gc.setServiceName("%sService");  //去掉接口上的I
//        // gc.setSwagger2(true); 实体属性 Swagger2 注解
//        mpg.setGlobalConfig(gc);
//
//        // 数据源配置
//        DataSourceConfig dsc = new DataSourceConfig();
//        dsc.setUrl("jdbc:mysql://127.0.0.1:3307/my_blog?characterEncoding=utf8&useSSL=false&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false");
//        // dsc.setSchemaName("public");
//        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
//        dsc.setUsername("root");
//        dsc.setPassword("123");
//        mpg.setDataSource(dsc);
//
//        // 包配置
//        PackageConfig pc = new PackageConfig();
//        pc.setModuleName("blog");
//        pc.setParent("org.myblog");
//        mpg.setPackageInfo(pc);
//
//
//
//        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig();
//
//        // 配置自定义输出模板
//        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
//        // templateConfig.setEntity("templates/entity2.java");
//        // templateConfig.setService();
//        // templateConfig.setController();
//
//        templateConfig.setXml(null);
//        mpg.setTemplate(templateConfig);
//
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//
//        strategy.setEntityLombokModel(true);
//        strategy.setRestControllerStyle(true);
//
//        // 写于父类中的公共字段
////        strategy.setSuperEntityColumns("id");
////        strategy.setInclude(new String[]{"administrative_division_code","blog_article","blog_commit",
////        "blog_fans"
////        ,"blog_file","blog_like","blog_role","blog_user"});
//        strategy.setInclude(new String[]{"blog_verif"});
//        strategy.setControllerMappingHyphenStyle(true);
////        strategy.setTablePrefix(pc.getModuleName() + "_");
//        mpg.setStrategy(strategy);
//        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
//        mpg.execute();
//    }
}
