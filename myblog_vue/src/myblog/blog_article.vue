<template>

 <el-card :body-style="{ padding: '0px' }" :id="article.id">
   <el-container>
     <el-main>
       <h1>{{article.title}}</h1>
       <article v-html="article.text.substring(0,10)"></article>
       <el-row style="margin-top: 10px; margin-bottom: 10px;" :gutter="10">
         <el-col style="font-size: 4px; padding-top: 10px;" :span="8">
           <h5>{{article.createTime}}</h5>
         </el-col>
         <el-col style="padding-top: 13px;" :span="4">
             <span class="el-icon-s-custom">
               {{article.watch}}
             </span>
         </el-col>
         <el-col :span="12">
           <el-button circle icon="el-icon-thumb" @click="article_like_click(article.id)">{{article.likes}}</el-button>
              <el-button circle icon="el-icon-s-comment" @click="article_comment_click(article.id)">{{article.commits}}</el-button>
              <el-button circle icon="el-icon-more-outline" @click="article_read_click(article.id)"></el-button>
         </el-col>

       </el-row>
        <el-row>
          <BlogSendMsg v-if="showList === true" :l_type="l_type" :articleid="article.id"></BlogSendMsg>
       </el-row>
       <el-row>
         <BlogMsgList v-if="showList === true" :l_type="l_type" :article_id="article.id"></BlogMsgList>
       </el-row>
      </el-main>


   </el-container>
 </el-card>
</template>

<script>
  import BlogSendMsg from '@/myblog/blog_sendmsg.vue'
  import BlogMsgList from '@/myblog/blog_list_msg.vue'
  import {formatDate} from '../../static/utils/date.js'
  import {httpget ,httppost} from '../../static/utils/request.js'

  export default {
    data() {
      return {
        comment_click_count: 0,
        showList: false,
        l_type: 0
      };
    },
    props: ['article'],
    methods: {
      fdate(d){
        return formatDate(d)
      },
    article_read_click(dd) {
      // console.log("blog article :: ",dd,this.article)
        this.$router.push({
          name: 'readArticle',
          params: this.article
        })
      },
      article_comment_click(dd) {
        console.log(dd)
        if(this.comment_click_count >0){
          this.comment_click_count=0
          this.showList = false
        }else{
          this.showList=true
          this.comment_click_count=this.comment_click_count+1
        }

      },
      article_like_click(dd) {
        let parm={
          id: dd
        }
        let that=this
        httppost("aa/blog/blog-like/save",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code !== 0){
              that.$message(
                {
                  type: "error",
                  message: res.msg
                }
          );
            }
          }
        )
        // console.log(dd)
        
      }
    },
    created() {

    },
      watch: {

      },
      components:{
        BlogSendMsg,BlogMsgList
      }
}
</script>

<style>
</style>
