<template>
  <div>
    <link href="https://cdn.bootcss.com/github-markdown-css/2.10.0/github-markdown.min.css" rel="stylesheet">
    <el-dialog :title="showArticleTtile" :visible.sync="showdialog" :width="width" :close-on-click-modal='false'
        :before-close="closedialog" center>
        <el-card>
          <article v-if="article_show ===true" class="markdown-body" v-html="text"></article>
          <el-tag v-if="tag_show ===true" v-for="(tag,index) in tags" :type="tag_type[Math.round(Math.random()*(tag_type.length-1))]" :key="index">{{tag}}</el-tag>
        </el-card>
     </el-dialog>

  </div>
</template>

<script>
  export default {
    data() {
      return {

      }

    },
    props: ['showArticleTtile','text','showdialog','article_show','tags','tag_type','tag_show','width'],
    methods:{
      closedialog() {
        this.$emit("getshowdialog", false)
      }
    },
    created() {

    }
  }
</script>

<style>
</style>
