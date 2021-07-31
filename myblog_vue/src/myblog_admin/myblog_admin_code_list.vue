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
            <el-button icon="el-icon-remove" @click="delfile" circle></el-button>
            <el-button icon="el-icon-tickets" @click="d_excel" circle></el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-table
          :default-sort="{prop: 'user_info.id'}" ref="tabledataref" :data="tableData" tooltip-effect="dark"
          style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column fixed type="selection" width="55">
          </el-table-column>
          <el-table-column v-for="(c,index) in table_columns" :key="index" sortable :label="c.label" :width="c.width"
            :prop="c.prop" show-overflow-tooltip>
          </el-table-column>

          <el-table-column fixed="right" label="操作" width="120">
            <template slot-scope="scope">
              <el-button icon="el-icon-remove-outline" @click.native.prevent="delRowFile(scope.$index, tableData)"
                type="text" size="small">
              </el-button>
            </template>
          </el-table-column>

        </el-table>
      </el-main>
      <el-footer>
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
          :page-sizes="[5,10, 20, 30, 40]" :page-size="currentSize" layout="total, sizes, prev, pager, next, jumper"
          :total="tableData[0].total">
        </el-pagination>
      </el-footer>
    </el-container>
    <BlogAdminExtractExcel  @getshowdialog="getshowdialogv" :ccctype="table_columns[0].ccctype" :showExcelDialog="showExtractExcel"></BlogAdminExtractExcel>
  </div>
</template>

<script>
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
        showExtractExcel: false,
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
        if(this.userlistsearchvalue!= ""){
          this.search();
        }else{
          this.getTableData();
        }

      },
      handleCurrentChange(val) {
        this.currentPage=val
        if(this.userlistsearchvalue!= ""){
          this.search();
        }else{
          this.getTableData();
        }
      },
      getTableColumns() {
        let that=this
        httpget("aa/blog/blog-admin-table-columns/getColumns?t=7").then(
          (res)=>{
            if(res.code ==0){
              that.table_columns=res.data
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      },
      getshowdialogv(sss) {
        this.showExtractExcel=false
      },

      delRowFile(a, b) {
        this.rowdata = this.$refs.tabledataref.tableData[a]
        // console.log("delRowArticle :: ", a, this.rowdata)
        this.$confirm('是否要删除[ ' + this.rowdata.clist.uid + ' ] ?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let fid=[]
          fid.push(this.rowdata.clist.uid+"----"+this.rowdata.clist.cid)
          let parm={
            codeids: fid.toString()
          }
          httppost("aa/blog/blog-code/admin/del",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code ==0){
              this.$message({
                type: 'success',
                message: '删除' + this.rowdata.clist.uid + '成功!'
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
      delfile() {
        let pp=[]
        var mup=this.multipleSelection;
        for(var i=0;i<mup.length;i++){
          pp.push(mup[i].clist.uid+"----"+mup[i].clist.cid)
        }
        let parm={
          codeids: pp.toString()
        }
        httppost("aa/blog/blog-code/admin/del",JSON.stringify(parm)).then(
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

      fdate(d) {
        return formatDate(d)
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      getTableData() {
        let that=this
        let parm={
          currpage: this.currentPage,
          pagesize: this.currentSize
        }
        httppost("aa/blog/blog-code/admin/list",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
              that.tableData=res.data
              // console.log(res)
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
        httppost("aa/blog/blog-code/admin/search",JSON.stringify(parm)).then(
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
            value: 'cuid',
            label: '用户id',
            disabled: false
          },  {
            value: 'ccid',
            label: '特征码'
          },
          {
            value: 'ccode',
            label: "验证码"
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
      BlogAdminExtractExcel
    }
  }
</script>

<style>
</style>
