myblog是一个博客项目，囊括了博客与后台

技术：
	后端：
		1、使用springboot+mybatis-plus构建模板
		2、使用poi、itextpdf用于生成excel、word、pdf文件
		3、使用封装后的枚举类型来效验用户角色
		4、通过HttpServletResponse类返回文件流用于客户端下载
	前端：
		1、使用vue2+element ui编写页面
		2、封装axios组件来访问后端接口

功能：
	博客系统：支持图文发表、文件上传与下载、评论查看与发送、消息查看、文章专题、首页文章、用户主页查看、点击用户头像、点赞博文、阅读博文、管理自己博文
	admin后台：
		1、支持导出所有数据为excel、pdf、word
		2、支持用户权限调整、信息修改、删除、新增、检索
		3、支持文章、文件、评论增删改查操作
		4、可查询用户登录ip地址、登录名称、验证情况
		5、统计用户登录信息
		6、使用级联查询选择用户地区
	安全验证：
		1、图文验证码
			1、默认采用四位长度随机数字+字母组合生成
			2、通过java自带awt图像库将随机生成的字符串写入到图片上
			3、前端发送过来的验证码统一在后台以小写形式保存至数据库
		2、滑块验证码
			1、随机截取固定大小的图块用于生成滑块背景图
			2、随机截取图块位置中的一部分用于生成滑块样式
			3、保存随机滑块x轴坐标到数据库用于效验前端是否通过验证
		3、密码采用16位md5加密生成
		4、服务端返回的cookie信息采用封装后的rsa加密算法生成