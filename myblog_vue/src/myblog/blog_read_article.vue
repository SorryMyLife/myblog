<template>
  <el-container style="margin-top: 1px; margin-bottom: 1px;" :id="articlevalue.article_id">
    <el-main>
      <el-row :gutter="20">
        <el-col :offset="2" :span="5">
          <BlogLeftUser :articlevalue='articlevalue' :user_article_info='user_article_info'
            :user_hot_article_list='user_hot_article_list'></BlogLeftUser>
        </el-col>
        <el-col :span="8">
          <el-card width="500px">
            <el-container>
              <el-header>
                <h1>{{title}}</h1>
              </el-header>
              <el-main>
                <article v-html="html"></article>
              </el-main>
            </el-container>
          </el-card>
        </el-col>
        <el-col :span="4">
          <BlogRightTag :articlevalue='user_article_info' :user_article_tag_list='user_article_tag_list'></BlogRightTag>
        </el-col>
      </el-row>
      <BlogSendMsg :articleid="articlevalue.id===undefined?articlevalue.aid:articlevalue.id" :l_type="l_type"></BlogSendMsg>
      <BlogMsgList :article_id="articlevalue.id===undefined?articlevalue.aid:articlevalue.id" :l_type="l_type"></BlogMsgList>
    </el-main>
  </el-container>
</template>

<script>
  import BlogLeftUser from '@/myblog/blog_left_user.vue'
  import BlogRightTag from '@/myblog/blog_right_tag.vue'
  import BlogSendMsg from '@/myblog/blog_sendmsg.vue'
  import BlogMsgList from '@/myblog/blog_list_msg.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'

  export default {
    data() {
      return {

        user_article_tag_list: [],
        user_hot_article_list: [],
        l_type: 0,
        articleSearchData: '',
        articlevalue: '',
        defaultData: "preview",
        user_article_info: [],
        text: '',
        title: '',
        html: ''
      };
    },
    methods: {
      getRouterParms() {
        this.articlevalue = this.$route.params
        // console.log("read article ::: ",this.articlevalue)
        let parm={
          aid: this.articlevalue.id===undefined?this.articlevalue.aid:this.articlevalue.id
        }
        let that=this
        httppost("aa/blog/blog-article/read",JSON.stringify(parm)).then(
        (res)=>{
          if(res.code == 0){
            that.user_article_info=[]
            that.user_article_info.push(res.data.user_info)
            that.user_hot_article_list=res.data.article
            that.user_article_tag_list=res.data.tags
            that.text=res.data.text
            that.title=res.data.title
            that.html=res.data.html
            document.cookie="afff="+res.data.user_info.id
            // console.log(document.cookie)
          }
        }
        )
      },

    },
    created() {
      this.getRouterParms()
    },
    mounted() {

    },
    components: {
      BlogLeftUser,
      BlogRightTag,
      BlogSendMsg,
      BlogMsgList
    }
  };
</script>

<style>
</style>
