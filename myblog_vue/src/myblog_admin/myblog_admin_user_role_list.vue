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
            <el-button icon="el-icon-circle-plus" @click="adduserrole" circle></el-button>
            <el-button icon="el-icon-remove" @click="deluserrole" circle></el-button>
            <el-button icon="el-icon-tickets" @click="d_excel" circle></el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-table  :default-sort="{prop: 'user_info.id'}" ref="tabledataref" :data="tableData" tooltip-effect="dark"
          style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column fixed type="selection" width="55">
          </el-table-column>
          <el-table-column v-for="(c,index) in table_columns" :key="index" sortable :label="c.label" :width="c.width"
            :prop="c.prop" show-overflow-tooltip>
          </el-table-column>

          <el-table-column fixed="right" label="操作" width="120">
            <template slot-scope="scope">
              <el-button icon="el-icon-edit" @click.native.prevent="changeRowRole(scope.$index, tableData)" type="text"
                size="small">
              </el-button>
              <el-button icon="el-icon-remove-outline" @click.native.prevent="delRowRole(scope.$index, tableData)"
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
    <BlogAdminRoleEdit :showuserrole="showUserRole" @getshowdialog="getshowdialogv" :edit_role="rowdata" :showdialog="showRoleEdit" :edit_title="edit_title"></BlogAdminRoleEdit>
  </div>
</template>

<script>
  import {httpget ,httppost} from '../../static/utils/request.js'
  import BlogAdminExtractExcel from '@/myblog_admin/myblog_admin_extract_excel.vue'
  import BlogAdminRoleEdit from '@/myblog_admin/myblog_admin_role_edit.vue'
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
        showRoleEdit: false,
        showUserRole: false,
        showExtractExcel: false,
        multipleSelection: [],
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
      getTableColumns() {
        let that=this
        httpget("aa/blog/blog-admin-table-columns/getColumns?t=5").then(
          (res)=>{
            if(res.code ==0){
              that.table_columns=res.data
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      },

      adduserrole() {
        this.edit_title = "用户角色添加"
        this.rowdata = {
          roleuser: {
            rid: undefined,
            rname: undefined,
            uid: undefined,
            uname: undefined,
            createTime: undefined,
            createRole: undefined,
            changeTime: undefined,
            changeRole: undefined
          },
          total: undefined
        }
       
        // this.showUserRole=true
        // this.showRoleEdit = true
        // console.log("adduser")
      },
      delRowRole(a, b) {
        this.rowdata = this.$refs.tabledataref.tableData[a]
        this.$confirm('是否要删除[ ' + this.rowdata.roleuser.uname + ' ] 用户的权限?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let aid=[]
          aid.push(this.rowdata.roleuser.uid)
          let parm={
            ruids: aid.toString()
          }
          httppost("aa/blog/blog-role/admin/delUser",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code ==0){
              this.$message({
                type: 'success',
                message: '删除' + this.rowdata.roleuser.uname + ' 的权限成功!'
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
      deluserrole() {
        let pp=[]
        var mup=this.multipleSelection;
        for(var i=0;i<mup.length;i++){
          pp.push(mup[i].roleuser.uid)
        }
        let parm={
          ruids: pp.toString()
        }
        httppost("aa/blog/blog-role/admin/delUser",JSON.stringify(parm)).then(
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
        this.showUserRole=false
        this.showRoleEdit = false
        this.showExtractExcel=false
      },
      changeRowRole(a, b) {
        this.rowdata = this.$refs.tabledataref.tableData[a]
        this.edit_title = '用户角色编辑 [ ' + this.rowdata.roleuser.uname + ' ]'
        this.showUserRole=true
        this.showRoleEdit = true
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
        httppost("aa/blog/blog-role/admin/listUser",JSON.stringify(parm)).then(
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
        httppost("aa/blog/blog-role/admin/searchUser",JSON.stringify(parm)).then(
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
            value: 'ruuid',
            label: '用户ID'
          }, {
            value: 'rurid',
            label: '角色ID',
            // disabled: true
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
      BlogAdminRoleEdit,
      BlogAdminExtractExcel
    }
  }
</script>

<style>
</style>
