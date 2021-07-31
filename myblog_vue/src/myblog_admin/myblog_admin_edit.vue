<template>
  <div>
    <el-dialog :title="edit_title" :visible.sync="showdialog" width="50%" :close-on-click-modal='false'
      :before-close="closedialog" center>
      <div>
        <el-form :model="edit_user" label-width="100px">

          <el-form-item label="头像" width="120" prop="user_info.icon">
           <el-upload
              class="avatar-uploader"
              ref="upload"
              action=""
              :http-request="uploadicon"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img  :src="edit_user.user_info.icon" class="avatar">
              
            </el-upload>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item v-if="edit_user.user_info.id !== '' && edit_user.user_info.id !== undefined" label="ID"
                width="120" prop="user_info.id" show-overflow-tooltip>
                <el-input :disabled="true" v-model="edit_user.user_info.id"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="名称" width="120" prop="user_info.name" show-overflow-tooltip>
                <el-input v-model="edit_user.user_info.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="性别" width="120" prop="user_info.gender">
            <!-- <el-input v-model="edit_user.user_info.gender"></el-input> -->
            <el-radio v-model="edit_user.user_info.gender" :label="0">男</el-radio>
            <el-radio v-model="edit_user.user_info.gender" :label="1">女</el-radio>
          </el-form-item>
          <el-form-item label="地址" width="120" prop="user_info.address" show-overflow-tooltip>
            <!-- <el-input v-model="edit_user.user_info.address"></el-input> -->
            <el-cascader
            v-model="edit_user.user_info.addr"
              placeholder="试试搜索：北京"
              :options="DivisionList"
              filterable></el-cascader>
          </el-form-item>
          <el-form-item label="个性签名" width="120" prop="user_info.autograph" show-overflow-tooltip>
            <el-input v-model="edit_user.user_info.autograph"></el-input>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="邮箱" width="120" prop="user_info.email" show-overflow-tooltip>
                <el-input v-model="edit_user.user_info.email"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="手机" width="120" prop="user_info.phone" show-overflow-tooltip>
                <el-input v-model="edit_user.user_info.phone"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="角色等级" width="120" prop="user_info.role_name" show-overflow-tooltip>
                <!-- <el-input v-model="edit_user.role_name"></el-input> -->
                <el-select v-model="edit_user.user_info.role_name" placeholder="请选择">
                  <el-option v-for="(item,index) in role_list" :key="index" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="登录类型" width="120" prop="user_info.loginType" show-overflow-tooltip>
                <!-- <el-input v-model="edit_user.user_info.login_type"></el-input> -->
                <el-select v-model="edit_user.user_info.loginType" placeholder="请选择">
                  <el-option v-for="(item,index) in login_type_list" :key="index" :label="item.name"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closedialog">退出</el-button>
        <el-button type="primary" @click="dialog_submit">确定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {httpget ,httppost} from '../../static/utils/request.js'

  export default {
    data() {
      return {
        role_list: [],
        login_type_list: [],
        DivisionList: [],
        imgChange: 0
      }
    },
    props: ['showdialog', 'edit_user', 'edit_title'],
    methods: {
      uploadicon(p){
        
      },
      handleAvatarSuccess(res, file) {
        var reader = new FileReader()
        reader.readAsDataURL(file[this.imgChange].raw)
        reader.onload = () => {
            // console.log('file 转 base64结果：' + reader.result)
            this.edit_user.user_info.icon=reader.result
            this.imgChange=this.imgChange+1
        }
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg/png';
        const isLt2M = file.size / 1024 / 1024 < 2;
        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG/PNG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },

      getDivisionList(){
        let that=this
        httpget("aa/blog/administrative-division-code/getDivision").then(
          (res)=>{
            if(res.code ==0){
              that.DivisionList=res.data
            }else{
              that.$message.error(res.msg);
            }
          }
        )
      },
      getLoginTypeList() {
        this.login_type_list = [{
            name: "用户ID",
            value: 0
          },
          {
            name: "邮箱",
            value: 1
          }, {
            name: "手机号",
            value: 2
          }
        ]
      },
      getRoleList() {
        let that=this
        httpget("aa/blog/blog-role/listrole").then(
          (res)=>{
            if(res.code ==0){
              that.role_list=res.data
            }else{
              this.$message.error(res.msg);
            }
          }
        )
        
      },
      dialog_submit() {
        console.log(this.edit_user)
        httppost("aa/blog/blog-admin-user/changeuser",JSON.stringify(this.edit_user.user_info)).then(
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
        
      },
      closedialog() {
        this.$emit("getshowdialog", false)
        this.$router.go(0)
      }
    },
    created() {
      this.getRoleList()
      this.getLoginTypeList()
      this.getDivisionList();

    }
  }
</script>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
