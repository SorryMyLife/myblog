<template>
  <el-card :body-style="{ padding: '0px' }">
  
  <blogUserIcon :showUserIcon="true" :showUserName="true" :user_info="article"></blogUserIcon>
  <el-button v-if="isFans === false" type="primary" icon="el-icon-plus" @click="addFans" circle></el-button>
  <el-button v-if="isFans === true" type="primary" icon="el-icon-check" @click="delFans" circle></el-button>
  
  </el-card>
</template>

<script>
  import blogUserIcon from '@/myblog/blog_user_icon.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  export default {
    data() {
      return {
        isFans: false
      };
    },
    props: ['article'],

    methods:{
      isFansff(){
        let parm={
          uid: this.article.id
        }
        httppost("aa/blog/blog-fans/isFans",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ===0){
              this.isFans=true
            }
          }
        )
      },
      addFans(){
        let parm={
          uid: this.article.id
        }
        httppost("aa/blog/blog-fans/save",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ===0){
              this.isFans=true
              this.$router.go(0)
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      },
      delFans(){
        let parm={
          uid: this.article.id
        }
        httppost("aa/blog/blog-fans/delFans",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ===0){
              this.isFans=false
              this.$router.go(0)
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      }
    },
    created(){
      this.isFansff();
    },
    components:{
      blogUserIcon
    }
    }
</script>

<style>
</style>
