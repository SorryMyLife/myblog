<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="4">
        <div v-if="user_info.id!==undefined">
             <blogUserIcon style="height: 10px; width: 10px;" :showUserIcon="true" :showUserName="false" :user_info="user_info"></blogUserIcon>
         </div>
         <div v-else>
           <el-button  @click="login_click">请登录</el-button>
         </div>
      </el-col>
      <el-col :span="16">
        <mavon-editor @imgAdd="imgAdd" ref="med" placeholder="请输入文章内容" @change="mechangeData" v-model="msgdata" />
      </el-col>
      <el-col :span="4">
        <el-button type="primary" icon="el-icon-s-promotion" circle @click="sendMsg"></el-button>
      </el-col>
    </el-row>

     <blogLogin :user_login_dialogVisible="user_login_dialogVisible" @getLoginVisibleBack="getLoginVisibleBackValue"></blogLogin>

  </div>

</template>

<script>
  import blogUserIcon from '@/myblog/blog_user_icon.vue'
  import blogLogin from '@/myblog/blog_login.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'

  export default {
    data() {
      return {
        user_info: '',
        user_login_dialogVisible: false,
        msgdata: '',
        mavon_edito_value: '',
        mavon_edito_html: ''
      };
    },
    props: ['articleid','l_type'],
    methods:{
      imgAdd(a,b){
        // console.log("img add ::: ",a,b)
        var formdata = new FormData();
        let that=this
        formdata.append("uid",this.user_info.id);
        formdata.append("file",b);
        httppost("aa/blog/blog-file/saveEditImg",formdata).then(
          (res)=>{
            if(res.code ==0){
                that.$refs.med.$img2Url(a, "aa"+res.data)
              }else{
                this.$message.error(res.msg);
              }
          }
        )

      },
      mechangeData(a, b) {
        this.mavon_edito_value = a
        this.mavon_edito_html=b
      },
      login_click(){
        this.user_login_dialogVisible = true;
      },
      getLoginVisibleBackValue(ssss){
        // console.log(ssss)
        this.user_login_dialogVisible = ssss
      },
      sendMsg(articleid){
        let parm={
          html: this.mavon_edito_html,
          msg: this.msgdata,
          type: this.l_type,
          id: this.articleid
        }
        let that=this
        httppost("aa/blog/blog-commit/save",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code == 0){
               that.$router.go(0)
            }else{
              that.$message(
                 {
                   type: "error",
                   message: res.msg
                 }
              );
          }}
        )
        // console.log("send msg :::: ",this.msgdata,this.articleid)
        this.msgdata = undefined
      },
      getUserInfo(){
        let that=this
        httpget("aa/blog/blog-user/getUserInfo")
        .then((res)=>{
          if(res.code == 0){
            that.user_info=res.user_info
          }
        })
        // console.log(document.cookie)
      }

    },
    created() {
      // console.log(this.articleid)
      this.getUserInfo()
    },
    components:{
      blogLogin,blogUserIcon
    }
   }
</script>

<style>
</style>
