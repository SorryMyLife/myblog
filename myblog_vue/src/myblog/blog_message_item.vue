<template>

  <div>
    <el-row :gutter="20" v-for="(msg,index) in msg_list" :key="index">
     <el-col :span="4">
       <img style="height: 60px; width: 60px;" :src="msg.icon" />

     </el-col>
     <el-col :span="14">
       <div @click="user_info_click(msg.uid)">
         <h4 style="margin-top: 6px;">
           {{msg.name}}
         </h4>
       </div>

       <div v-if="isFansItem ==true">
         <h4>关注了你</h4>
       </div>

       <div v-if="isLikeItem ==true">
         <h4>点赞了你的博文</h4>
       </div>

       <h6 v-if="isArticleItem == true || isDownloadItem == true" style="margin-top: -15px;">
        回复: {{msg.msg}}
       </h6>

       <div v-if="isArticleItem == true || isLikeItem ==true" @click="article_read_click(msg.aid)">
         <h1 style="margin-top: -15px;">
           《{{msg.title}}》
         </h1>
       </div>

       <div v-if="isDownloadItem == true" @click="download_item_click(msg.fid)">
         <h1 style="margin-top: -15px;">
           《{{msg.name}}》
         </h1>
       </div>
     </el-col>
     <el-col :span="6">
       <h4>
         {{msg.createTime}}
       </h4>
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
          :total="total">
        </el-pagination>
  </div>

</template>

<script>
import {formatDate} from '../../static/utils/date.js'
import {httpget ,httppost} from '../../static/utils/request.js'
  export default {
    data() {
      return {
        currentPage: 1,
        currentPageSize: 5,
        showPaginationValue: false,
      }
    },
    props: ['msg_list' , 'isArticleItem','isDownloadItem','isLikeItem','isFansItem','acname','total'],
    methods:{

      handleSizeChange(val) {
        // console.log(`每页 ${val} 条`);
        this.currentPageSize=val
        this.getmsg()
      },
      handleCurrentChange(val) {
        // console.log(`当前页: ${val}`);
        this.file_currentPage=val
        this.getmsg();
      },
      getmsg(){
        let tab_name=this.acname
        let parm={
          m_type: 0,
          currentpage: this.file_currentPage,
          currentsize: this.currentPageSize
        }
        let that=this
        if(tab_name === "wz"){
          parm.m_type=0
        }
        if(tab_name === "wj"){
          parm.m_type=1
        }
        if(tab_name === "dz"){
          parm.m_type=2
        }
        if(tab_name === "fs"){
          parm.m_type=3
        }
        httppost("aa/blog/blog-commit/getUserMessage",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code == 0){
               that.msg_list=res.data
               that.total=res.total
              }else{
                 that.$message.error(res.msg);
              }
            }
          )
      },
      download_item_click(did){
        console.log("ditem ::: ",did)
      },
      user_info_click(user_Id){
        document.cookie="afff="+user_Id
        this.$router.push({
          path: 'User',
          params: {"uid":user_Id}
        })
      },
      article_read_click(article_id){
        this.$router.push({
          name: 'readArticle',
          params: {"aid":article_id}
        })
      },
      fdate(d){
        return formatDate(d)
      },
    },
    created(){
      
    }
    }
</script>

<style>
</style>
