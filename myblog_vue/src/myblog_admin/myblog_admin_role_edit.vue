<template>
  <div>
    <el-dialog :title="edit_title" :visible.sync="showdialog" width="70%"
      :close-on-click-modal='false' :before-close="closedialog" center>
      <el-form :model="edit_role" label-width="100px">
        <el-row v-if="showuserrole ===true">
          <el-col :span="12">
            <el-form-item label="用户ID" width="120" prop="roleuser.uid" show-overflow-tooltip>
              <el-input :disabled="true" v-model="edit_role.roleuser.uid"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名" width="120" prop="roleuser.uname" show-overflow-tooltip>
              <el-input :disabled="true" v-model="edit_role.roleuser.uname"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="showuserrole ===true">
          <el-col :span="12">
             <el-form-item label="用户" width="120" prop="roleuser.uname" show-overflow-tooltip>
              <el-select v-model="edit_role.roleuser.uid" clearable  filterable placeholder="请选择" @focus="u_focus" @change="u_select">
                  <el-option
                    v-for="(item,index) in u_s_list"
                    :key="index"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="角色" width="120" prop="roleuser.rname" show-overflow-tooltip>
              <el-select v-model="edit_role.roleuser.rid" clearable  filterable placeholder="请选择" @focus="u_focus2" @change="u_select2">
                  <el-option
                    v-for="(item,index) in u_r_list"
                    :key="index"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
         </el-form-item>
          </el-col>
        </el-row>
         <el-form-item v-if="showrole === true" label="角色ID" width="120" prop="role.id" show-overflow-tooltip>
           <el-input v-model="edit_role.role.id"></el-input>
         </el-form-item>
         <el-form-item v-if="showrole === true" label="角色名称" width="120" prop="role.name" show-overflow-tooltip>
           <el-input  v-model="edit_role.role.name"></el-input>
         </el-form-item>



        <el-form-item>
          <el-button type="info" @click="closedialog">取消</el-button>
          <el-button type="danger" @click="dialog_submit">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {httpget ,httppost} from '../../static/utils/request.js'
  export default {
    data() {
      return {
        loading: false,
        u_s_list: [],
        u_r_list: []
      }
    },
    props: ['showdialog','showuserrole','showrole','edit_title', 'edit_role'],
    methods: {
       u_select2(){
       var select_id=this.edit_role.roleuser.uid
       let that=this
       this.u_s_list.forEach(item=>{
         if(item.id === select_id){
           that.edit_role.roleuser.uname=item.name
         }
       })
      },
      u_focus2(){
        this.getRoleList();
      },
      getRoleList(){
        httpget("aa/blog/blog-role/getRoleList").then(
          (res)=>{
            if(res.code ==0){
                this.u_r_list=res.data
              }else{
                this.$message.error(res.msg);
              }
          }
        )
      },
       u_select(){
       var select_id=this.edit_role.roleuser.uid
       let that=this
       this.u_s_list.forEach(item=>{
         if(item.id === select_id){
           that.edit_role.roleuser.uname=item.name
         }
       })
      },
      u_focus(){
        this.getUserList();
      },
      getUserList(){
        httpget("aa/blog/blog-admin-user/getUserList").then(
          (res)=>{
            if(res.code ==0){
                this.u_s_list=res.data
              }else{
                this.$message.error(res.msg);
              }
          }
        )
      },

      dialog_submit() {
        let that = this
        if(that.edit_role.role === "" || that.edit_role.role === undefined){
          httppost("aa/blog/blog-role/admin/changeUser",JSON.stringify(that.edit_role.roleuser)).then(
          (res)=>{
             if(res.code ==0){
              this.$message({
                  message: '修改成功!',
                  type: 'success'
                });
                this.closedialog()
            }else{
              this.$message.error(res.msg);
            }
          }
        )
        }else{
          httppost("aa/blog/blog-role/admin/change",JSON.stringify(that.edit_role.role)).then(
          (res)=>{
             if(res.code ==0){
              this.$message({
                  message: '修改成功!',
                  type: 'success'
                });
                this.closedialog()
            }else{
              this.$message.error(res.msg);
            }
          }
        )
        }
        

        this.$emit("getshowdialog", false)
      },
      closedialog() {
        this.$emit("getshowdialog", false)
        this.$router.go(0);
      }
    },
    created() {
      
    }
  }
</script>

<style>
</style>
