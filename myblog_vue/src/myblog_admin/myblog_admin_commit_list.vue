<template>
  <div>
    <el-container>
      <el-header>
        <el-row :gutter="10">
          <el-col :span="4">
            <el-select clearable v-model="selectvalue" multiple placeholder="请选择">
              <el-option v-for="item in conditions" :key="item.value" :label="item.label" :value="item.value"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="16">
            <el-input placeholder="请输入内容" v-model="userlistsearchvalue" clearable>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button icon="el-icon-search" @click="search" circle></el-button>
            <el-button icon="el-icon-circle-plus" @click="addarticle" circle></el-button>
            <el-button icon="el-icon-remove" @click="delarticle" circle></el-button>
            <el-button icon="el-icon-tickets" @click="d_excel" circle></el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-table @cell-click="cell_click" @row-dblclick="article_table_row_click" :default-sort="{prop: 'user_info.id'}" ref="tabledataref" :data="tableData" tooltip-effect="dark"
          style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column fixed type="selection" width="55">
          </el-table-column>
          <el-table-column v-for="(c,index) in table_columns" :key="index" sortable :label="c.label" :width="c.width" :prop="c.prop" show-overflow-tooltip>
          </el-table-column>

          <el-table-column fixed="right" label="操作" width="120">
            <template slot-scope="scope">
              <el-button icon="el-icon-edit" @click.native.prevent="changeRowArticle(scope.$index, tableData)" type="text"
                size="small">
              </el-button>
              <el-button icon="el-icon-remove-outline" @click.native.prevent="delRowArticle(scope.$index, tableData)"
                type="text" size="small">
              </el-button>
            </template>
          </el-table-column>

        </el-table>
      </el-main>
      <el-footer>
        <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[5,10, 20, 30, 40]"
              :page-size="currentSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="tableData[0].total">
            </el-pagination>
      </el-footer>
    </el-container>
     <BlogAdminShowArticle :showArticleTtile="showArticleTtile" :tags="rowdata" :tag_type="tag_type" :text="rowdata" :tag_show="showTag" :article_show="showArticle" :showdialog="showArticleDialog" @getshowdialog="getshowdialogv"> </BlogAdminShowArticle>
    <BlogAdminExtractExcel  @getshowdialog="getshowdialogv" :ccctype="table_columns[0].ccctype" :showExcelDialog="showExtractExcel"></BlogAdminExtractExcel>
    <BlogAdminCommitEdit :addarticle="showAddarticle" :showcommit="showCommitME"  @getshowdialog="getshowdialogv" :edit_title="edit_title" :showdialog="showArticleEdit" :edit_article="rowdata" :tag_type="tag_type"></BlogAdminCommitEdit>
  </div>
</template>

<script>
  import BlogAdminShowArticle from '@/myblog_admin/myblog_admin_show_article_dialog.vue'
  import BlogAdminCommitEdit from '@/myblog_admin/myblog_admin_commit_edit.vue'
  import BlogAdminExtractExcel from '@/myblog_admin/myblog_admin_extract_excel.vue'
  import {httpget ,httppost} from '../../static/utils/request.js'
  import {
    formatDate
  } from '../../static/utils/date.js'
  export default {
    data() {
      return {
        currentPage: 1,
        currentSize: 5,
        userlistsearchvalue: '',
        selectvalue: [],
        conditions: [],
        tableData: [],
        multipleSelection: [],
        showCommitME: false,
        showArticleDialog: false,
        showArticle: false,
        showTag: false,
        showAddarticle: false,
        showArticleEdit: false,
        showExtractExcel: false,
        showArticleTtile: '',
        showArticleWidth: '',
        tag_type: ['','success','info','danger','warning'],
        table_columns: [],
        rowdata: '',
        edit_title: ''
      }
    },
    methods: {
      d_excel(){
        this.showExtractExcel=true;
      },
      handleSizeChange(val) {
        this.currentSize=val
        if( this.userlistsearchvalue != ""){
          this.search();
        }else{
          this.getTableData();
        }
        
      },
      handleCurrentChange(val) {
        this.currentPage=val
        if(this.userlistsearchvalue != ""){
          this.search();
        }else{
          this.getTableData();
        }
      },
      getTableColumns(){
        let that=this
        httpget("aa/blog/blog-admin-table-columns/getColumns?t=3").then(
          (res)=>{
            if(res.code ==0){
              that.table_columns=res.data
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      },
      cell_click(a,b){
        let that = this
        this.table_columns.forEach((tablefff)=>{
          if(tablefff.label === b.label && b.label === "评论内容"){
            that.rowdata = eval("a."+tablefff.prop)
            that.showArticleTtile=b.label
            that.showArticleDialog=true
            that.showArticle =true

          }
          if(tablefff.label === b.label && b.label === "拥有标签"){
            that.rowdata = eval("a."+tablefff.prop.replace(".length",""))
            that.showArticleTtile=b.label
            that.showTag = true
            that.showArticleDialog=true
          }
        })

      },
      article_table_row_click(a){
        console.log("article_table_row_click ::: ",a)
      },

      addarticle() {
        this.edit_title = "评论添加"
        this.rowdata={
              commit:{
                msg: "",
                createTime: undefined,
                chagneTime: undefined,
                roleName: undefined,
                changeRole: undefined,
                html: undefined,
                tags: undefined,
                tagsName: [],
                title: undefined,
                type: undefined,
                typeName: undefined,
                uid: undefined,
                uname:undefined
              },
              total: undefined
          }
        this.showArticleEdit=true
        this.showCommitME=true
        this.showAddarticle=true
        // console.log("adduser")
      },
      delRowArticle(a, b) {
        this.rowdata = this.$refs.tabledataref.tableData[a]
        console.log("delRowArticle :: ", a, this.rowdata)
        this.$confirm('是否要删除[ ' + this.rowdata.article.id + ' ] 文章评论?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let cid=[]
          cid.push(this.rowdata.commit.id)
          let parm={
            cids: cid.toString()
          }
          httppost("aa/blog/blog-commit/admin/del",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code ==0){
              this.$message({
                type: 'success',
                message: '删除' + this.rowdata.commit.id + '成功!'
              });
               this.$router.go(0);
            }else{
              this.$message.error(res.msg);
            }
            }
          )
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      delarticle() {
        let pp=[]
        var mup=this.multipleSelection;
        for(var i=0;i<mup.length;i++){
          pp.push(mup[i].commit.id)
        }
        let parm={
          cids: pp.toString()
        }
        httppost("aa/blog/blog-commit/admin/del",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code ==0){
              this.$message({
                type: 'success',
                message: '删除' + pp + '成功!'
              });
              this.$router.go(0);
            }else{
              this.$message.error(res.msg);
            }
            }
          )
      },
      getshowdialogv(sss) {
        this.showArticle=false
        this.showTag=false
        this.showArticleDialog=false
        this.showArticleEdit=false
        this.showCommitME=false
        this.showExtractExcel=false
      },
      changeRowArticle(a, b) {
        this.rowdata = this.$refs.tabledataref.tableData[a]
        // console.log("this.rowdata  ::: ",this.rowdata)
        this.edit_title='评论编辑 [ '+this.rowdata.commit.id + ' ]'
        this.showArticleEdit = true
        this.showCommitME=true
        // console.log(a, b, this.rowdata)
      },
      fdate(d) {
        return formatDate(d)
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      getTableData() {
        let parm={
          currpage: this.currentPage,
          pagesize: this.currentSize
        }
        httppost("aa/blog/blog-commit/admin/list",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
                this.tableData=res.data
              }else{
                this.$message.error(res.msg);
              }
          }
        )

      },
      search() {
        let parm={
          s_str: this.userlistsearchvalue,
          s_types: this.selectvalue.toString(),
          currpage: this.currentPage,
          pagesize: this.currentSize
        }

        httppost("aa/blog/blog-commit/admin/search",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
                this.tableData=res.data
              }else{
                this.$message.error(res.msg);
              }
          }
        )
      },
      getConditions() {
        this.conditions = [{
          value: 'ctitle',
          label: '标题',
          disabled: false
        },{
          value: 'cid',
          label: "评论ID"
        },{
          value: 'ctext',
          label: "评论内容"
        }
        ]
      }
    },
    created() {
      this.getConditions()
      this.getTableData()
      this.getTableColumns()
    },
    components: {
      BlogAdminShowArticle,
      BlogAdminCommitEdit,
      BlogAdminExtractExcel
    }
  }
</script>

<style>
</style>
