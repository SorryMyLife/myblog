<template>
  <div>
    <el-container>
      <el-header style="margin-bottom: 100px;">
        <el-row>
          <el-col :span="6">
            <blogUserIcon :showUserIcon="true" :showUserName="true" :user_info="user_article_info[0]"></blogUserIcon>
          </el-col>
        </el-row>
        <el-row>
          <el-table :data="user_article_info" style="width: 100%">
            <el-table-column prop="articles" label="文章" width="60">
            </el-table-column>
            <el-table-column prop="fans" label="粉丝" width="60">
            </el-table-column>
            <el-table-column prop="watchs" label="访问" width="60">
            </el-table-column>
            <el-table-column prop="commits" label="评论">
            </el-table-column>
          </el-table>
        </el-row>
      </el-header>
      <el-main style="margin-top: 60px;">
        <el-row>
          <el-col :span="16">
            <el-input placeholder="请输入内容" v-model="articleSearchData" clearable>
            </el-input>
          </el-col>
          <el-col :span="2">
            <el-button @click="searchArticle">搜索</el-button>
          </el-col>
        </el-row>
      </el-main>

      <el-main>
        <el-card  style="margin-top: 0px;">
          <span
            style="margin: 0; padding: 0;  font-weight: normal; box-sizing: border-box; -moz-box-sizing: border-box; -webkit-box-sizing: border-box">
            {{user_article_info[0].name}} 的其他文章</span>
          <ul v-for="(hot_article,index) in user_hot_article_list" :key="index">
            《<div @click="intoReadArticle(hot_article.id)" style="color:#ff0000">
              {{hot_article.title}}
            </div>》
            <span style="color: #2C3E50; pointer-events: none;" class="el-icon-s-custom">
              {{hot_article.watch}}</span>
          </ul>
        </el-card>
      </el-main>

    </el-container>
  </div>
</template>

<script>
  import blogUserIcon from '@/myblog/blog_user_icon.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  import {getUID} from '../../static/utils/cookie.js'
  export default {
    data() {
      return {
        articleSearchData: ''
      };
    },
    props: ['articlevalue','user_article_info','user_hot_article_list'],
    methods: {
      intoReadArticle(a_id){
        let rr={
          aid: a_id
        }
        this.$router.push({
          name: 'readArticle',
          params: rr
        })
        // console.log("div clicked :: ",a_id)
      },
      searchArticle() {
        let parm={
          uid: getUID(this),
          currpage: 1,
          pagesize: 5,
          search_str: this.articleSearchData
        }
        let that=this
        httppost("aa/blog/blog-article/searchOnUser",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
              that.user_hot_article_list=res.data;
            }else{
              that.$message(
                {
                  type: "error",
                  message: res.msg
                }
            );
            }
          }
        )
        
      },
    },
    created() {

    },
    watch: {
      $route(){
				console.log("touteee")
			}
    },
    components:{
      blogUserIcon
    }

 }
</script>

<style>
</style>
