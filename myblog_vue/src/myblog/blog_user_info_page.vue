<template>
  <div>
    <el-container>
      <el-header>
        <div>
          <img style="height: 120px; width: 120px;  border-radius:50%; " :src="user_info.icon" />
        </div>
        <div>
          <h1>{{user_info.name}}</h1>
        </div>
        <div>
          <h6>{{user_info.autograph}}</h6>
        </div>
      </el-header>
      <el-main style="margin-top: 180px;">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-tabs type="card">
              <div>
                <el-table :data="left_fans" style="width: 100%; margin-top: 100px;">
                  <el-table-column prop="fans" label="粉丝" width="60">
                  </el-table-column>
                  <el-table-column prop="watchs" label="访问" width="60">
                  </el-table-column>
                  <el-table-column prop="commits" label="评论">
                  </el-table-column>
                </el-table>
              </div>
            </el-tabs>
          </el-col>
          <el-col :span="12">
            <el-tabs v-model="activeName" type="card">
              <el-tab-pane label="文章" name="first">
                <el-row v-for="(article,index) in articles" :key="index">
                  <BlogArticle :article="article"></BlogArticle>
                </el-row>
                <el-pagination
                  @size-change="article_handleSizeChange"
                  @current-change="article_handleCurrentChange"
                  :current-page="article_currentPage"
                  :page-sizes="[5,10, 20, 30, 40]"
                  :page-size="article_currentPageSize"
                  :hide-on-single-page="article_showPaginationValue"
                  background
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="a_total">
                </el-pagination>

              </el-tab-pane>
              <el-tab-pane label="文件" name="second">
                <el-row v-for="(download_item,index) in files" :key="index">
                  <BlogDownloadItem :download_item='download_item'></BlogDownloadItem>
                </el-row>
               
                <el-pagination
                  @size-change="file_handleSizeChange"
                  @current-change="file_handleCurrentChange"
                  :current-page="file_currentPage"
                  :page-sizes="[5,10, 20, 30, 40]"
                  :page-size="file_currentPageSize"
                  :hide-on-single-page="file_showPaginationValue"
                  background
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="f_total">
                </el-pagination>
              </el-tab-pane>
            </el-tabs>
             
          </el-col>
          <el-col :span="6">
            <el-tabs type="card">
              <div>
                  <BlogRightTag :articlevalue='user_info' :user_article_tag_list='tags'></BlogRightTag>
              </div>
              <div v-if="getUUID() != ''">
                <el-button @click="into_admin">进入管理界面</el-button>
              </div>
            </el-tabs>
          </el-col>
        </el-row>
      </el-main>

    </el-container>

  </div>


</template>

<script>
  import BlogDownloadItem from '@/myblog/blog_download_item.vue'
  import BlogArticle from '@/myblog/blog_article.vue'
  import BlogRightTag from '@/myblog/blog_right_tag.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  import {getUID,getCookie} from '../../static/utils/cookie.js'
  export default {
    data() {
      return {
        user_info: [],
        articles: [],
        tags: [],
        files: [],
        left_fans: [],
        activeName: "first",
        article_currentPage: 1,
        article_currentPageSize: 5,
        file_currentPage: 1,
        file_currentPageSize: 5,
        a_total: 0,
        f_total: 0,
        article_showPaginationValue: false,
        file_showPaginationValue: false
      };
    },
    methods: {
      
      getUUID(){
        var uu=getCookie("uid");
        return uu;
      },

       article_handleSizeChange(val) {
        // console.log(`每页 ${val} 条`);
        this.article_currentPageSize=val
        this.getArticles();
      },
      article_handleCurrentChange(val) {
        // console.log(`当前页: ${val}`);
        this.article_currentPage=val
         this.getArticles();
      },
      file_handleSizeChange(val) {
        // console.log(`每页 ${val} 条`);
        this.file_currentPageSize=val
        this.getFiles()
      },
      file_handleCurrentChange(val) {
        // console.log(`当前页: ${val}`);
        this.file_currentPage=val
        this.getFiles();
      },
      getArticles(){
        let parm={
          uid: getUID(this),
          currpage: this.article_currentPage,
          pagesize: this.article_currentPageSize
        }
        httppost("aa/blog/blog-article/list",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code == 0){
              this.articles=res.data
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      },
      getFiles(){
        let parm={
          uid: getUID(this),
          currentpage: this.file_currentPage,
          currentsize: this.file_currentPageSize
        }
        httppost("aa/blog/blog-file/getFileListByUID",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code == 0){
              this.files=res.data
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      },
      into_admin(){
        this.$router.push({
          path: '/admin'
        })
      },
      
      getUserInfo() {
        // console.log(this.$route.params)
        
        let parm={
          uid: getUID(this),
          currentpage: 1,
          currentsize: 5
        }
        httppost("aa/blog/blog-user/getUserPageInfo",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code == 0){
              this.user_info=res.data.user_info
              this.left_fans=[{
                fans: res.data.fans,
                commits: res.data.commits,
                watchs: res.data.watchs
              }]
              this.articles=res.data.articles
              this.tags=res.data.tags
              this.files=res.data.files
              this.a_total=res.data.article_total
              this.f_total=res.data.file_total
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      }

    },
    mounted(){
    },
    created() {
      this.getUserInfo()
      this.article_showPaginationValue = this.a_total > this.article_currentPageSize? false:true
      this.file_showPaginationValue = this.f_total > this.file_currentPageSize? false:true
    
    },
    components: {
      BlogDownloadItem,
      BlogArticle,
      BlogRightTag
    }
  }
</script>

<style>
</style>
