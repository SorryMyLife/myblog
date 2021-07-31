<template>
  <div>
    <el-container>

      <el-container>
        <el-aside width="200px"></el-aside>
        <el-main >
          <div>
            <el-row :gutter="20">
              <el-col :span="18">
              <el-input placeholder="请输入内容" v-model="search_data" clearable></el-input>
            </el-col>
            <el-col :span="2">
              <el-button @click="search_data_click">搜索</el-button>
            </el-col>

            </el-row>
            
          </div>
          <BlogHotArticleList :article_list="blog_article_list"></BlogHotArticleList>
        </el-main>
        <el-aside width="200px"></el-aside>
      </el-container>

      <el-footer>
         <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[5,10, 20, 30, 40]"
            :page-size="currentPageSize"
            :hide-on-single-page="showPaginationValue"
            background
            layout="total, sizes, prev, pager, next, jumper"
            :total="blog_article_list[0].total">
          </el-pagination>

      </el-footer>
    </el-container>
  </div>
</template>

<script>

  import BlogHotArticleList from '@/myblog/blog_hot_article_list.vue'
  import BlogPagination from '@/myblog/blog_pagination.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  export default {
    data() {
      return {
        blog_article_list: [],
        currentPage: 1,
        currentPageSize: 5,
        showPaginationValue: false,
        search_data: ''
      }
    },
    methods: {
      search_data_click(){
        let that=this
        let parm={
          currpage: that.currentPage,
          pagesize: that.currentPageSize,
          search_str: this.search_data
        }
        httppost("aa/blog/blog-article/searchOnPage",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code == 0){

                this.blog_article_list = res.data
                // console.log(this.blog_article_list)
                // that.$router.go(0)
              }else{
                that.$message(
                   {
                     type: "error",
                     message: "文章获取失败"
                   }
                );
            }
            }
        )
      },
      handleSizeChange(val) {
        // console.log(`每页 ${val} 条`);
        this.currentPageSize=val
        if(this.search_data === undefined || this.search_data ===""){
          this.show_article_datas()
        }else{
          this.search_data_click()
        }
        
      },
      handleCurrentChange(val) {
        // console.log(`当前页: ${val}`);
        this.currentPage=val
         if(this.search_data === undefined || this.search_data ===""){
          this.show_article_datas()
        }else{
          this.search_data_click()
        }
      },
      show_article_datas() {
        let that=this
        let parm={
          currpage: that.currentPage,
          pagesize: that.currentPageSize
        }
        httppost("aa/blog/blog-article/list",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code == 0){

                this.blog_article_list = res.data
                // console.log(this.blog_article_list)
                // that.$router.go(0)
              }else{
                that.$message(
                   {
                     type: "error",
                     message: "文章获取失败"
                   }
                );
            }
            }
        )
        
      },

    },
    created() {
      this.show_article_datas()
      this.showPaginationValue = this.blog_article_list[0].total > this.currentPageSize? false:true
    },
    mounted() {

    },
    components:{
      BlogHotArticleList,BlogPagination
    }
  }
</script>
