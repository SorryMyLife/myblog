<template>
  <div>
    <el-card>
      <el-col :span="4">
        <el-container>
          <el-header>
            <div @click="download_into_user(download_item.uid)">
              <img style="width: 40px; height: 40px;  border-radius:50%; " :src="download_item.icon"/>
            </div>
          </el-header>
          
        </el-container>
      </el-col>
      <el-col :span="20">
        <el-container>
          <el-header>
            <el-container>
              <el-aside>{{download_item.fname}}</el-aside>
              <el-main>{{fdate(download_item.createTime)}}</el-main>
            </el-container>
          </el-header>
          <el-main>
            <el-row :gutter="2">
              <el-col :span="12">
                <el-collapse accordion>
                  <el-collapse-item :title="download_item.text.substring(0,10)" name="1">
                      <article class="markdown-body" v-html="download_item.html"></article>
                   </el-collapse-item>
                </el-collapse>
              </el-col>
              <el-col :span="6">
                <el-button type="primary" icon="el-icon-bottom" circle @click="download_file(download_item.fid)"></el-button>
              </el-col>
              <el-col :span="6">
                <el-button type="primary" icon="el-icon-s-comment" circle @click="download_comment_click(download_item.fid)">{{download_item.commits}}</el-button>
              </el-col>
            </el-row>
            <el-row>
            <BlogSendMsg v-if="showList === true" :l_type="l_type" :articleid="download_item.fid"></BlogSendMsg>
            </el-row>
            <el-row>
            <BlogMsgList v-if="showList === true" :l_type="l_type" :article_id="download_item.fid"></BlogMsgList>
          </el-row>
          </el-main>
          
        </el-container>
      </el-col>
    </el-card>
  </div>
</template>

<script>
  import {formatDate} from '../../static/utils/date.js'
  import {httpget ,httppost} from '../../static/utils/request.js'
  import BlogSendMsg from '@/myblog/blog_sendmsg.vue'
  import BlogMsgList from '@/myblog/blog_list_msg.vue'
  export default {
    data() {
      return {
         comment_click_count: 0,
        showList: false,
        l_type: 1
      }
    },
    props: ['download_item'],
    methods:{
    download_comment_click(did){
      if(this.comment_click_count >0){
          this.comment_click_count=0
          this.showList = false
        }else{
          this.showList=true
          this.comment_click_count=this.comment_click_count+1
        }
    },
      download_into_user(uid){
        this.$router.push({
          path: '/User',
          params: uid
        })
      },
      fdate(d){
        return formatDate(d)
      },
      download_file(download_id){
        console.log(download_id)
        window.open("aa/blog/blog-file/download?id="+download_id, "_blank")
        
      },
    },
     components:{
        BlogSendMsg,BlogMsgList
      }
    }
</script>

<style>
</style>
