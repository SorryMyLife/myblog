<template>
  <div>
    <el-container>
      <el-header>
        <el-row :gutter="10">
          <el-col :span="18">
            <el-input
              placeholder="请输入内容"
              v-model="search_download_file_str"
              clearable>
            </el-input>
            </el-col>
          <el-col :span="3">
            <el-button icon="el-icon-search" circle @click="search_download_file"></el-button>
          </el-col>
          <el-col :span="3">
            <el-button icon="el-icon-folder-add" circle @click="upload_file"></el-button>
          </el-col>
        </el-row>
        <BlogUploadFile :isShow="show_upload" @getupload="getupload"></BlogUploadFile>
      </el-header>
      <el-main>
        <el-row v-for="(download_item,index) in list_download_files" :key="index">
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
      </el-main>
    </el-container>
  </div>
</template>

<script>
  import BlogDownloadItem from '@/myblog/blog_download_item.vue'
  import BlogUploadFile from '@/myblog/blog_upload_file.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  export default {
    data() {
      return {
        search_download_file_str: '',
        list_download_files: [],
        show_upload: false,
        file_currentPage: 1,
        file_currentPageSize: 5,
        file_showPaginationValue: false,
        f_total: 0
      }
    },
    methods: {
      file_handleSizeChange(val) {
        // console.log(`每页 ${val} 条`);
        this.file_currentPageSize=val
        this.getDownloadFilesList()
      },
      file_handleCurrentChange(val) {
        // console.log(`当前页: ${val}`);
        this.file_currentPage=val
        this.getDownloadFilesList();
      },
      getupload(){
        this.show_upload=false
      },
      upload_file(){
        this.show_upload=true
      },
      search_download_file(){
        console.log("search_download_file ::: ",this.search_download_file_str)
      },
      getDownloadFilesList(){
        let that=this
        let parm={
          uid: '',
          currentpage: this.file_currentPage,
          currentsize: this.file_currentPageSize
        }
        httppost("aa/blog/blog-file/getFileList",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code == 0){
              that.list_download_files=res.data
              that.f_total=res.total
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      }

    },
    created() {
this.getDownloadFilesList()
    },
    components:{
      BlogDownloadItem,BlogUploadFile
    }
  }
</script>

<style>
</style>
