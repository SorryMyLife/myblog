<template>
  <div>

  <el-dialog :title="edit_title" :visible.sync="showdialog" width="50%" :close-on-click-modal='false'
      :before-close="closedialog" center>
      <div>
        <el-form :model="edit_pwd" label-width="100px">
          <el-form-item label="原密码" width="120" prop="oldpwd">
            <el-input show-password v-model="edit_pwd.oldpwd"></el-input>
           </el-form-item>
          <el-form-item label="新密码" width="120" prop="newpwd1" >
            <el-input show-password v-model="edit_pwd.newpwd1"></el-input>
          </el-form-item>
          <el-form-item label="再确认" width="120" prop="newpwd2" show-overflow-tooltip>
            <el-input show-password v-model="edit_pwd.newpwd2"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closedialog">退出</el-button>
        <el-button type="danger" @click="change_pwd">确定修改</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {httpget ,httppost} from '../../static/utils/request.js'
  export default {
    data() {
      return {
        edit_pwd: {
          oldpwd: undefined,
          newpwd1: undefined,
          newpwd2: undefined
        }
      }
    },
    props: ['showdialog', 'edit_title','uid'],
    methods:{
      closedialog() {
        this.$emit("getshowdialog", false)
        this.$router.go(0);
      },
      change_pwd(){
        let parm={
          uid: this.uid.user_info.id,
          oldpwd: this.edit_pwd.oldpwd,
          newpwd1: this.edit_pwd.newpwd1,
          newpwd2: this.edit_pwd.newpwd2
        }
        let that=this
        httppost("aa/blog/blog-admin-user/changepwd",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
              that.$message(
                {
                    type: "success",
                    message: "密码修改成功"
                }
            );
            }else{
              that.$message.error(res.msg);
            }
          }
        )
        // console.log("change ",this.uid,this.edit_pwd)
        this.closedialog();
      },
    },
    created() {

    }
    }
</script>

<style>
</style>
