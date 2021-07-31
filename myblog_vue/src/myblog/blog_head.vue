<template>
  <div ref="blogheaddiv">
    <el-row :gutter="20">
      <el-col :span="10">
        <el-tabs v-model="tabactiveID" type="card" @tab-click="tab_c">
          <el-tab-pane v-for="(menu,index) in head_menu_list" :key="index" :label="menu.menu_name"></el-tab-pane>
        </el-tabs>
      </el-col>
      <el-col :span="7">
        <el-input placeholder="请输入内容" v-model="search_data" clearable></el-input>
      </el-col>
      <el-col :span="2">
        <el-button @click="search_data_click">搜索</el-button>
      </el-col>
      <el-col :span="5">
       <div v-if="user_info.id!==undefined">
          <blogUserIcon :showUserIcon="true" :showUserName="false" :user_info="user_info"></blogUserIcon>
          <el-button  @click="loginout_click">注销</el-button>
        </div>
        <div v-else>
          <el-button  @click="login_click">登录/注册</el-button>
        </div>
      </el-col>
    </el-row>
    <blogLogin :user_login_dialogVisible="user_login_dialogVisible" @getLoginVisibleBack="getLoginVisibleBackValue"></blogLogin>
 <el-backtop  :bottom="100"></el-backtop>
  </div>

</template>

<script>
  import blogLogin from '@/myblog/blog_login.vue'
  import blogUserIcon from '@/myblog/blog_user_icon.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  import {clearAllCookie} from '../../static/utils/cookie.js'
  export default{
    data(){
      return {
        user_login_dialogVisible: false,
        user_info: [],
        search_data: '',
        tabactiveID: '',
        head_menu_list: []
      }
    },
    methods: {

      tab_c() {
        let menu = this.head_menu_list[this.tabactiveID]
        // console.log(menu.menu_name, menu.menu_url)
        this.$router.push({
          path: menu.menu_url
        })

      },
      search_data_click() {
        console.log(this.search_data)
      },
      show_menu_datas() {
        this.head_menu_list = [{
          menu_name: '首页',
          menu_url: '/'
        },{
          menu_name: '下载',
          menu_url: '/download'
        },{
          menu_name: "写文章",
          menu_url: '/Article/writeArticle'
        }, {
          menu_name: '消息',
          menu_url: '/message'
        }, {
          menu_name: '放松一下',
          menu_url: '/life'
        }]
      },


      getUserInfo(){
        let that=this
        httpget("aa/blog/blog-user/getUserInfo")
        .then((res)=>{
          if(res.code == 0){
            that.user_info=res.user_info
          }
        })
      },
      loginout_click(){
        let that=this
          httpget("aa/blog/blog-user/logout")
          .then((res)=>{
            that.$message(
                {
                  type: "success",
                  message: res.msg
                })
                clearAllCookie()
                 that.$router.go(0)
          })
      },
      login_click(){
        this.user_login_dialogVisible = true;
      },
      getLoginVisibleBackValue(ssss){
        // console.log(ssss)
        this.user_login_dialogVisible = ssss
      }
    },
    created() {
      this.show_menu_datas()
      this.getUserInfo()
    },
    mounted() {

    },
    components:{
      blogLogin,blogUserIcon
    }
  }
</script>

<style>
</style>
