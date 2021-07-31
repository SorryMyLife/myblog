<template>
  <div >
    <el-row :gutter="10">
      <el-col :span="4">
        <el-menu
              default-active="2"
              class="el-menu-vertical-demo"
              @open="handleOpen"
              @close="handleClose"
              @select="nv_open"
              router
              unique-opened
              background-color="#545c64"
              text-color="#fff"
              active-text-color="#ffd04b">
              <el-submenu index="1">
                <template slot="title">
                  <i class="el-icon-setting"></i>
                  <span>用户管理</span>
                </template>
                <el-menu-item-group>
                   <el-menu-item index="/admin/userlist">用户修改</el-menu-item>
                   <el-menu-item index="/admin/userarticlelist">文章修改</el-menu-item>
                   <el-menu-item index="/admin/userfilelist">文件修改</el-menu-item>
                   <el-menu-item index="/admin/usercommitlist">评论修改</el-menu-item>
                 </el-menu-item-group>
              </el-submenu>
              <el-submenu index="2">
                <template slot="title">
                  <i class="el-icon-setting"></i>
                  <span>角色管理</span>
                </template>
                <el-menu-item-group>
                   <el-menu-item index="/admin/rolelist">角色修改</el-menu-item>
                   <el-menu-item index="/admin/userrole">用户角色</el-menu-item>
                </el-menu-item-group>
              </el-submenu>
              <el-submenu index="3">
                <template slot="title">
                  <i class="el-icon-setting"></i>
                  <span>登录情况</span>
                </template>
                <el-menu-item-group>
                   <el-menu-item index="/admin/loginlist">用户登录</el-menu-item>
                   <el-menu-item index="/admin/codelist">文本验证</el-menu-item>
                   <el-menu-item index="/admin/verifylist">滑块验证</el-menu-item>
                </el-menu-item-group>
              </el-submenu>
            </el-menu>
      </el-col>
      <el-col :span="20">
        <router-view></router-view>
      </el-col>


    </el-row>
  </div>
</template>

<script>
  export default {
      methods: {
        nv_open(key, keyPath){
          console.log(key, keyPath);
        },
        handleOpen(key, keyPath) {
          console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
          console.log(key, keyPath);
        }
      }
    }
</script>

<style>
</style>
