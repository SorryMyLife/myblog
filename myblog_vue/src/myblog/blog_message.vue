<template>
  <div>
    <el-container>

      <el-header>
        <el-row :gutter="1">
          <el-col :span="6">
            <el-tabs type="card"></el-tabs>
          </el-col>
          <el-col :span="12">
            <el-tabs v-model="activeName" type="card" @tab-click="msg_tabs_click">
              <el-tab-pane label="文章" name="wz">
                <BlogMessageItem :isArticleItem="true" :msg_list="article_msg_list" :acname="activeName" :total="total"></BlogMessageItem>
              </el-tab-pane>
              <el-tab-pane label="文件" name="wj">
                <BlogMessageItem :isDownloadItem="true" :msg_list="download_msg_list" :acname="activeName" :total="total"></BlogMessageItem>
              </el-tab-pane>
              <el-tab-pane label="点赞" name="dz">
                <BlogMessageItem :isLikeItem="true" :msg_list="like_msg_list" :acname="activeName" :total="total"></BlogMessageItem>
              </el-tab-pane>
              <el-tab-pane label="粉丝" name="fs">
                <BlogMessageItem :isFansItem="true" :msg_list="fans_msg_list" :acname="activeName" :total="total"></BlogMessageItem>
              </el-tab-pane>
            </el-tabs>
          </el-col>
          <el-col :span="6">
            <el-tabs type="card"></el-tabs>
          </el-col>
        </el-row>
      </el-header>
    </el-container>
  </div>
</template>

<script>
  import {
    formatDate
  } from '../../static/utils/date.js'
  import BlogMessageItem from '@/myblog/blog_message_item.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  export default {
    data() {
      return {
        user_msg_list: [],
        user_info: [],
        activeName: 'wz',
        currentpage: 1,
        currentsize: 5,
        article_msg_list: [],
        download_msg_list: [],
        like_msg_list: [],
        fans_msg_list: [],
        total: 0
      };
    },

    methods: {
      msg_tabs_click(tab,event){
        let tab_name=tab.name
        let parm={
          m_type: 0,
          currentpage: this.currentpage,
          currentsize: this.currentsize
        }
        let that=this
        if(tab_name === "wz"){
          httppost("aa/blog/blog-commit/getUserMessage",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code == 0){
               that.article_msg_list=res.data
               that.total=res.total
               
              }else{
                 that.$message.error(res.msg);
              }
            }
          )
        }
        if(tab_name === "wj"){
          parm.m_type=1
          httppost("aa/blog/blog-commit/getUserMessage",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code == 0){
               that.download_msg_list=res.data
               that.total=res.total
              }else{
                 that.$message.error(res.msg);
              }
            }
          )
        }
        if(tab_name === "dz"){
          parm.m_type=2
          httppost("aa/blog/blog-commit/getUserMessage",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code == 0){
               that.like_msg_list=res.data
               that.total=res.total
              }else{
                 that.$message.error(res.msg);
              }
            }
          )
        }
        if(tab_name === "fs"){
          parm.m_type=3
          httppost("aa/blog/blog-commit/getUserMessage",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code == 0){
               that.fans_msg_list=res.data
               that.total=res.total
              }else{
                 that.$message.error(res.msg);
              }
            }
          )
        }
      }
    },
    components: {
      BlogMessageItem
    },
    created() {
     
    }
  }
</script>

<style>
</style>
