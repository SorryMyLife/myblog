<template>
  <div>
    <el-dialog :title="edit_title" :visible.sync="showdialog" width="70%"
      :close-on-click-modal='false' :before-close="closedialog" center>
      <el-form :model="edit_article" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户ID" width="120" prop="commit.uid" show-overflow-tooltip>
              <el-input :disabled="true" v-model="edit_article.commit.uid"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名" width="120" prop="commit.uname" show-overflow-tooltip>
              <el-input :disabled="true" v-model="edit_article.commit.uname"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item v-if="addarticle===true" label="评论类型" width="120" prop="commit.type" show-overflow-tooltip>
              <el-radio v-model="edit_article.commit.type" :label="0">文章</el-radio>
              <el-radio v-model="edit_article.commit.type" :label="1">文件</el-radio>
        </el-form-item>
        <el-row>
          <el-col :span="12">
             <el-form-item v-if="addarticle===true" label="用户名" width="120" prop="commit.uname" show-overflow-tooltip>
              <!-- <el-input :disabled="true" v-model="edit_article.article.uname"></el-input> -->
              <el-select v-model="edit_article.commit.uid" clearable  filterable placeholder="请选择" @focus="u_focus" @change="u_select">
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
            <el-form-item v-if="addarticle === true" label="标题" width="120" prop="commit.title" show-overflow-tooltip>
          <!-- <el-input v-model="edit_article.article.title"></el-input> -->
          <el-select v-model="edit_article.commit.id" clearable  filterable placeholder="请选择" @focus="u_focus2" @change="u_select2">
                <el-option
                  v-for="(item,index) in u_a_list"
                  :key="index"
                  :label="item.title"
                  :value="item.id">
                </el-option>
              </el-select>
         </el-form-item>
          </el-col>
        </el-row>      
        <el-form-item label="标签" width="120" prop="tags" show-overflow-tooltip>
          <el-tag v-for="(tag,index) in edit_article.commit.tagsName" :key="index"
            :type="tag_type[Math.round(Math.random()*(tag_type.length-1))]" closable :disable-transitions="false"
            @close="tag_close(tag)">
            {{tag}}
          </el-tag>
          <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="small"
            @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+</el-button>
        </el-form-item>
        <el-form-item label="文章内容" width="120" prop="commit.title" show-overflow-tooltip>
          <mavon-editor v-if="showcommit === true" @imgAdd="imgAdd" ref="med" placeholder="请输入文章内容" @change="mechangeData" v-model="edit_article.commit.msg" />
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
        options: [],
        list: [],
        loading: false,
        mavon_edito_value: '',
        mavon_edito_html: '',
        inputVisible: false,
        inputValue: '',
        u_s_list: [],
        u_a_list: []
      }
    },
    props: ['showdialog','addarticle','showarticle','showcommit','edit_title', 'edit_article', 'tag_type'],
    methods: {
      u_select2(){
       var select_id=this.edit_article.commit.uid
       let that=this
       this.u_a_list.forEach(item=>{
         if(item.id === select_id){
           that.edit_article.commit.title=item.title
         }
       })
      },
      u_focus2(){
        this.getTitleList();
      },
      u_select(){
       var select_id=this.edit_article.commit.uid
       let that=this
       this.u_s_list.forEach(item=>{
         if(item.id === select_id){
           that.edit_article.commit.uname=item.name
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
    getTitleList(){
      let parm={
        c_type: this.edit_article.commit.type
      }
        httppost("aa/blog/blog-commit/admin/getTitleList",JSON.stringify(parm)).then(
          (res)=>{
            if(res.code ==0){
                this.u_a_list=res.data
              }else{
                this.$message.error(res.msg);
              }
          }
        )
      },
      imgAdd(a,b){
        // console.log("img add ::: ",a,b)
        var formdata = new FormData();
        let that=this
        formdata.append("uid",this.edit_article.commit.uid);
        formdata.append("file",b);
        httppost("aa/blog/blog-file/saveEditImg",formdata).then(
          (res)=>{
            if(res.code ==0){
                that.$refs.med.$img2Url(a, "aa"+res.data)
              }else{
                this.$message.error(res.msg);
              }
          }
        )
       
      },
      mechangeData(a, b) {
        this.mavon_edito_value = a
        this.mavon_edito_html=b
      },
      tag_close(tag) {
        let tags = this.edit_article.commit.tagsName;
        tags.splice(tags.indexOf(tag), 1);
      },
      dialog_submit() {
        this.edit_article.commit.msg = this.mavon_edito_value
        this.edit_article.commit.html=this.mavon_edito_html
        let that = this
        httppost("aa/blog/blog-commit/admin/change",JSON.stringify(that.edit_article.commit)).then(
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
        // console.log(that.edit_article)
        this.$emit("getshowdialog", false)
      },
      closedialog() {
        this.$emit("getshowdialog", false)
        this.$router.go(0);
      },
      showInput() {
        this.inputVisible = true;
        this.$nextTick(_ => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
      },

      handleInputConfirm() {
        let inputValue = this.inputValue;
        if (inputValue) {
          this.edit_article.commit.tagsName.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      }
    },
    created() {
      
    }
  }
</script>

<style>
</style>
