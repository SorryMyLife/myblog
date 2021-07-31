<template>
  <el-container style="margin-top: 1px; margin-bottom: 1px;" :id="articlevalue.id">
    <el-main>
      <el-row :gutter="20">
        <el-col :offset="2" :span="5">
          <BlogLeftUser :articlevalue='articlevalue' :user_article_info='user_article_info' :user_hot_article_list='user_hot_article_list'></BlogLeftUser>
        </el-col>
        <el-col :span="8">
          <div>
            <h1>{{tname}}</h1>
          </div>
          <el-row v-for="(article,index) in article_list" :key="index">
            <el-col>
              <BlogArticle :article="article.article"></BlogArticle>
            </el-col>
          </el-row>
          <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[5,10, 20, 30, 40]"
            :page-size="currentPageSize"
            :hide-on-single-page="showPaginationValue"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="article_list[0].total">
          </el-pagination>

        </el-col>
        <el-col :span="4">
         <BlogRightTag :articlevalue='articlevalue' :user_article_tag_list='user_article_tag_list'></BlogRightTag>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script>
  import BlogLeftUser from '@/myblog/blog_left_user.vue'
  import BlogRightTag from '@/myblog/blog_right_tag.vue'
  import BlogArticle from '@/myblog/blog_article.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  import {getUID} from '../../static/utils/cookie.js'
  export default {
    data() {
      return {
        currentPage: 1,
        currentPageSize: 5,
        showPaginationValue: false,
        article_list: [],
        user_article_tag_list: [],
        user_hot_article_list: [],
        articlevalue: '',
        user_article_info: [],
        tid: 0,
        tname: ''
      };
    },

    methods: {
       handleSizeChange(val) {
        // console.log(`每页 ${val} 条`);
        this.currentPageSize=val
        this.getRouterParms();       
      },
      handleCurrentChange(val) {
        // console.log(`当前页: ${val}`);
        this.currentPage=val
        this.getRouterParms()
      },
      getRouterParms() {
        this.articlevalue=this.$route.params.articlevalue
        this.tname=this.$route.params.tagname
        // console.log("category ::: ",this.$route.params)
        let parm={
          uid: getUID(this),
          tag_id: this.tid==0 ?this.$route.params.tagid:this.tid,
          currentpage: this.currentPage,
          currentsize: this.currentPageSize
        }
        this.tid=parm.tag_id
        let that=this
        httppost("aa/blog/blog-tag/intoTag",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
              that.user_article_info=[]
              that.user_article_info.push(res.data.user_info)
              that.article_list=res.data.articlelist
              that.user_article_tag_list=res.data.tags
              that.user_hot_article_list=res.data.article
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
    watch: {
      $route(val,oldval){
        console.log("route ::; ",val,oldval)
      }
    },
    components: {
      BlogLeftUser,BlogRightTag,BlogArticle
    }
  };
</script>

<style>
</style>
