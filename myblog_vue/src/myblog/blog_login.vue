<template>
  <div>
    <el-dialog
      title="用户登录"
      ref="dia"
      :visible.sync="user_login_dialogVisible"
      width="30%"
      :before-close="login_close"
    >
      <el-form
        :model="ruleForm"
        status-icon
        :rules="rules"
        ref="ruleForm"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="用户名" prop="id">
          <el-input
            placeholder="请输入用户名/用户ID/邮箱"
            v-model="ruleForm.id"
            autocomplete="on"
            clearable
          ></el-input>
        </el-form-item>
        <el-form-item label="用户密码" prop="password">
          <el-input
            placeholder="请输入密码"
            type="password"
            v-model="ruleForm.password"
            show-password
            autocomplete="on"
            clearable
          ></el-input>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="验证码" prop="code">
            <el-input
              placeholder="请输入验证码"
              v-model="ruleForm.code"
              clearable
            ></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <div v-if="code_img === ''">
              <el-button @click="getCode">获取验证码</el-button>
            </div>
            <div v-else ><img :src="code_img" @click="getCode" /></div>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button
            v-if="isLogin === true"
            type="primary"
            @click="login_account(ruleForm)"
            >登录</el-button
          >
          <el-button
            v-if="isLogin === false"
            type="primary"
            @click="reg_account(ruleForm)"
            >注册</el-button
          >
          <el-button @click="login_close">取消</el-button>
          <el-link type="primary" @click="change_aa" :underline="false">{{ttt}}</el-link>

        </el-form-item>
      </el-form>
      <blogSlider :showVerCode="showSlider" :uuid="ruleForm.id" :ccid="createCode(24)" @getshowdialog="getshowdialog"></blogSlider>
    </el-dialog>
  </div>
</template>

<script>
import { httpget, httppost } from "../../static/utils/request.js";
import blogSlider from "@/myblog/blog_slider.vue"
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入密码"));
      } else {
        if (this.ruleForm.password === "") {
          callback();
        }
      }
    };
    var validateAccount = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入账号"));
      } else {
        callback();
      }
    };
    return {
      ruleForm: {
        id: "",
        password: "",
        code: ""
      },
      rules: {
        password: [
          {
            validator: validatePass,
            trigger: "blur",
          },
        ],
        id: [
          {
            validator: validateAccount,
            trigger: "blur",
          },
        ],
      },
      isLogin: true,
      ttt: "切换到注册",
      ccc: 0,
      code_img: '',
      showSlider: false,

    };
  },
  props: ["user_login_dialogVisible"],
  methods: {
    getshowdialog(a){
      this.showSlider=false
    },
    createCode(codeLength)
    {
        var code = "";
        var selectChar=new Array(0,1,2,3,4,5,6,7,8,9,"A","B","C","D","E","F","G","H","I","G","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
        for(var i=0; i<codeLength; i++)
        {
            var charIndex=Math.floor(Math.random()*32);
            code+=selectChar[charIndex];
        }
        if(code.length!=codeLength)
        {
            this.createCode();
        }
        return code;
    },
    getCode(){
      let parm={
        cid: this.createCode(24),
        uid: this.ruleForm.id
      }
      let that=this
      httppost("aa/blog/blog-code/getCode",JSON.stringify(parm)).then(
        (res)=>{
          if(res.code ==0){
              that.code_img=res.data
            }else{
              that.$message.error(res.msg);
            }
        }
      )
    },
    change_aa() {
      if (this.ccc > 0) {
        this.isLogin = false;
        this.ttt = "切换到登录";
        this.$refs.dia.title === "用户登录";
        this.ccc = 0;
      } else {
        this.isLogin = true;
        this.ttt = "切换到注册";
        this.$refs.dia.title = "用户注册";
        this.ccc = this.ccc + 1;
      }

    },
    reg_account(ruleform) {
      this.showSlider=true;
      // console.log(ruleform)
    },

    login_close: function () {
      this.ruleForm.id = undefined;
      this.ruleForm.password = undefined;
      this.$emit("getLoginVisibleBack", false);
    },
    isPoneAvailable(poneInput) {
      var myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (!myreg.test(poneInput)) {
        return false;
      } else {
        return true;
      }
    },
    isTelAvailable(tel) {
      var myreg = /^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
      if (!myreg.test(tel)) {
        return false;
      } else {
        return true;
      }
    },
    isEmailAvailable(emailInput) {
      var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
      if (!myreg.test(emailInput)) {
        return false;
      } else {
        return true;
      }
    },
    login_account(ruleform) {
      let that = this;
      // console.log(ruleform)
      let parm={
        id: this.ruleForm.id,
        password: this.ruleForm.password,
        code: this.ruleForm.code,
        loginType: 0
      }
      if(this.isPoneAvailable(parm.id) || this.isTelAvailable(parm.id)){
        parm.loginType=2
      }
      if(this.isEmailAvailable(parm.id)){
        parm.loginType=1
      }

      httppost("aa/blog/blog-user/login", JSON.stringify(parm)).then(
        (res) => {
          if (res.code == 0) {
            that.$message({
              type: "success",
              message: "欢迎回来!",
            });
            setTimeout(() => {
                      that.$router.go(0);
                      that.login_close();
            }, 1000);
          } else {
            that.$message.error(res.msg);
            that.getCode();
          }
          if(res.code ==99){
              that.showSlider=true
            }
        }
      );
    },
  },
  mounted() {
    this.getCode();
  },
  watch: {
    showSlider(v,ov){
      let that = this;
      if(v===false){
        httppost("aa/blog/blog-user/reg", JSON.stringify(that.ruleForm)).then(
          (res) => {
            if (res.code == 0) {
              that.$message({
                type: "success",
                message: res.data,
              });
              setTimeout(() => {
                        that.$router.go(0);
                        that.login_close();
              }, 3000);

            } else {
              that.$message({
                type: "error",
                message: res.msg,
              });
              that.getCode();
            }

          }
        );
      }
    }

  },
  components:{
    blogSlider
  }
};
</script>

<style>
</style>
