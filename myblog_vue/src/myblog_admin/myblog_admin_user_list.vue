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
            <el-button icon="el-icon-circle-plus" @click="adduser" circle></el-button>
            <el-button icon="el-icon-remove" @click="deluser" circle></el-button>
            <el-button icon="el-icon-tickets" @click="d_excel" circle></el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-table :default-sort="{prop: 'user_info.id'}" ref="tabledataref" :data="tableData" tooltip-effect="dark"
          style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column fixed type="selection" width="55">
          </el-table-column>
          <el-table-column sortable v-for="(tc,index) in table_columns" :key="index" :label="tc.label" width="120" :prop="tc.prop" show-overflow-tooltip>
          </el-table-column>

          <el-table-column fixed="right" label="操作" width="120">
            <template slot-scope="scope">
              <el-button icon="el-icon-edit" @click.native.prevent="changeRowUser(scope.$index, tableData)" type="text"
                size="small">
              </el-button>
              <el-button icon="el-icon-refresh-left" @click.native.prevent="changeRowUserPWD(scope.$index, tableData)"
                type="text" size="small">
              </el-button>
              <el-button icon="el-icon-remove-outline" @click.native.prevent="delRowUser(scope.$index, tableData)"
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
    <BloadAdminUserEdit :showdialog="showDialog" :edit_user="rowuser" :edit_title="edit_title"
      @getshowdialog="getshowdialogv">
    </BloadAdminUserEdit>
    <BlogAdminExtractExcel  @getshowdialog="getshowdialogv" :ccctype="table_columns[0].ccctype" :showExcelDialog="showExtractExcel"></BlogAdminExtractExcel>
    
    <BlogAdminChangePWD :showdialog="showChangePwdDialog" :edit_title="edit_title" :uid="rowuser"
      @getshowdialog="getshowdialogv"></BlogAdminChangePWD>
  </div>
</template>

<script>
  import BloadAdminUserEdit from '@/myblog_admin/myblog_admin_edit.vue'
  import BlogAdminChangePWD from '@/myblog_admin/myblog_admin_change_pwd.vue'
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
        showDialog: false,
        showExtractExcel: false,
        showChangePwdDialog: false,
        edit_title: "",
        rowuser: '',
        table_columns: []
      }
    },
    methods: {
      d_excel(){
        this.showExtractExcel=true;
      },

      getTableColumns(){
        let that=this
        httpget("aa/blog/blog-admin-table-columns/getColumns?t=0").then(
          (res)=>{
            if(res.code ==0){
              that.table_columns=res.data
            }else{
              this.$message.error(res.msg);
            }
          }
        )
      },

      handleSizeChange(val) {
        this.currentSize=val
        if(this.userlistsearchvalue!== ""){
          this.search();
        }else{
          this.getTableData();
        }
        
      },
      handleCurrentChange(val) {
        this.currentPage=val
        if(this.userlistsearchvalue!== ""){
          this.search();
        }else{
          this.getTableData();
        }
      },
      adduser() {
        this.edit_title = "用户新增"
        this.showDialog = true
        this.rowuser = {
          user_info: {
            id: "",
            name: "",
            gender: "",
            address: "",
            phone: "",
            email: "",
            createTime: '',
            createRole: "",
            changeTime: '',
            changeRole: "",
            loginType: '',
            role_name: ''
          },
          articles: '',
          role_name: "",
          files: '',
          tags: '',
          fans: '',
          commits: ''
        }
        console.log("adduser")
      },
      delRowUser(a, b) {
        this.rowuser = this.$refs.tabledataref.tableData[a]
        console.log("delRowUser :: ", a, this.rowuser)
        this.$confirm('是否要删除[ ' + this.rowuser.user_info.id + ' ]用户?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let pp=[]
          pp.push(this.rowuser.user_info.id)
          let parm={
            uids: pp.toString()
          }
          httppost("aa/blog/blog-admin-user/delUser",JSON.stringify(parm)).then(
            (res)=>{
              if(res.code ==0){
                this.$message({
                  type: 'success',
                  message: '删除' + this.rowuser.user_info.id + '成功!'
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
      deluser() {
        let pp=[]
        var mup=this.multipleSelection;
        for(var i=0;i<mup.length;i++){
          pp.push(mup[i].user_info.id)
        }
          let parm={
            uids: pp.toString()
          }
          httppost("aa/blog/blog-admin-user/delUser",JSON.stringify(parm)).then(
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
        // console.log("deluser  ::", mup)
      },
      getshowdialogv(sss) {
        // console.log(sss)
        this.showDialog = false;
        this.showChangePwdDialog = false;
        this.showExtractExcel=false;
      },
      changeRowUserPWD(a, b) {
        this.edit_title = "密码修改"
        this.showChangePwdDialog = true
        this.rowuser = this.$refs.tabledataref.tableData[a]
        // console.log("change row user pwd :: ", a, this.rowuser)
      },
      changeRowUser(a, b) {
        this.edit_title = "用户修改"
        this.showDialog = true
        this.rowuser = this.$refs.tabledataref.tableData[a]
        // console.log(a, b, this.rowuser)
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
          currentpage: this.currentPage,
          currentsize: this.currentSize
        }
        httppost("aa/blog/blog-admin-user/listuser",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
              that.tableData=res.data
              // console.log(res)
            }
            
          }
        )
        
      },
      search() {
        let parm={
          s_str: this.userlistsearchvalue,
          s_types: this.selectvalue.toString(),
          currentpage: this.currentPage,
          currentsize: this.currentSize
        }
        httppost("aa/blog/blog-admin-user/searchUser",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
                this.tableData=res.data
              }else{
                this.$message.error(res.msg);
              }
          }
        )
        // console.log(this.selectvalue, this.userlistsearchvalue)
        
      },
      getConditions() {
        this.conditions = [{
          value: 'uid',
          label: '用户ID'
        }, {
          value: 'uname',
          label: '用户名称',
          // disabled: true
        }, {
          value: 'atitle',
          label: '用户文章名称',
          disabled: true
        },{
          value: 'autograph',
          label: "用户签名"
        },{
          value: "email",
          label: "用户邮箱"
        },{
          value: "phone",
          label: "手机号码"
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
      BloadAdminUserEdit,
      BlogAdminChangePWD,
      BlogAdminExtractExcel
    }
  }
</script>

<style>
</style>
