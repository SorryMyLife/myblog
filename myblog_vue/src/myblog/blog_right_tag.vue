<template>
  <el-card>
    <div>
      <h2>
        {{articlevalue.name}}的标题专栏
      </h2>
      <el-tag v-for="tag in user_article_tag_list" :key="tag.id" :id="tag.id"
       effect="plain" @click="article_tag_click(tag)">{{tag.name}}</el-tag>
    </div>
  </el-card>
</template>

<script>
  export default {
     data() {
       return {

       };
     },
     props: ['articlevalue','user_article_tag_list'],
     methods: {
       article_tag_click(tag) {
        //  console.log("tag_id ::: ",tag_id)
        // console.log("tag ::: ",this.articlevalue)
         this.$router.push({
           name: 'category',
           params: {"tagid":tag.id,"tagname":tag.name,articlevalue: this.articlevalue,taglist: this.user_article_tag_list}
         })
         
       }
     },
     created() {

     }

  }
</script>

<style>
</style>
