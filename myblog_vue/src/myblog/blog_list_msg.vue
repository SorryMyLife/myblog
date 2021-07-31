<template>

  <div>
    <dl v-for="(user_msg,index) in msg_list" :key="index">
      <dd>
       <el-row>
         <el-col :offset="2" :span="4">

           <blogUserIcon :showUserIcon="true" :showUserName="true" :user_info="user_msg.user_info"></blogUserIcon>
         </el-col>
         <el-col style="margin-top: 40px;" :span="10">
            回复: 
            <article class="markdown-body" v-html="user_msg.html"></article>
               
         </el-col>
        <el-col :span="6">
          {{user_msg.create_time}}
        </el-col>
       </el-row>
      </dd>
    </dl>
     <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-sizes="[5,10, 20, 30, 40]"
        :page-size="currentPageSize"
        :hide-on-single-page="showPaginationValue"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="msg_list[0].total">
      </el-pagination>
  </div>

</template>

<script>
  import blogUserIcon from '@/myblog/blog_user_icon.vue'
  import BlogPagination from '@/myblog/blog_pagination.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'

  import {
    formatDate
  } from '../../static/utils/date.js'
  export default {
    data() {
      return {
        msg_list: [],
        currentPage: 1,
        currentPageSize: 5,
        showPaginationValue: false
      }
    },
    props: ['article_id','l_type'],
    methods: {
      handleSizeChange(val) {
        this.currentPageSize=val
        this.getMsgList(this.article_id)
        // console.log(`每页 ${val} 条`);
      },
      handleCurrentChange(val) {
        this.currentPage=val
        this.getMsgList(this.article_id)
        // console.log(`当前页: ${val}`);
      },

      fdate(d) {
        return formatDate(d)
      },
      getMsgList(article_id) {
        let that=this
        let parm={
          id: this.article_id,
          type: this.l_type,
          currentpage: this.currentPage,
          currentsize: this.currentPageSize
        }
        httppost("aa/blog/blog-commit/list",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code == 0){
              that.msg_list=res.data
            }
          }
        )
      }
    },
    created() {
      this.getMsgList(this.article_id)
    },
    components:{
      blogUserIcon,BlogPagination
    }
  }
</script>

<style>
</style>
