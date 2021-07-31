<template>
<div>
  <el-row v-for="(article,index) in article_list" :key="index">
    <el-col :span="4">
      <BlogArticleUserIcon style="margin-top: 50px;" :article="article.user_info"></BlogArticleUserIcon>
      </el-col>
    <el-col :span="20">
      <BlogArticle :article="article.article"></BlogArticle>
    </el-col>
  </el-row>
</div>
</template>

<script>
  import BlogArticleUserIcon from '@/myblog/blog_article_user_icon.vue'
  import BlogArticle from '@/myblog/blog_article.vue'
  export default {
    data() {
      return {

      };
    },
    props: ['article_list'],
    methods:{

    },
    components:{
      BlogArticleUserIcon,
      BlogArticle
    },
    created() {

    }
    }
</script>

<style>
</style>
