<template>
  <div>
    <el-dialog :title="edit_title" :visible.sync="showdialog" width="70%"
      :close-on-click-modal='false' :before-close="closedialog" center>
      <el-form :model="edit_file" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户ID" width="120" prop="file.uid" show-overflow-tooltip>
              <el-input :disabled="true" v-model="edit_file.file.uid"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户名" width="120" prop="file.uname" show-overflow-tooltip>
              <el-input :disabled="true" v-model="edit_file.file.uname"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item v-if="addarticle===true" label="用户名" width="120" prop="file.uname" show-overflow-tooltip>
              <!-- <el-input :disabled="true" v-model="edit_article.article.uname"></el-input> -->
              <el-select v-model="edit_file.file.uid" clearable  filterable placeholder="请选择" @focus="u_focus" @change="u_select">
                <el-option
                  v-for="(item,index) in u_s_list"
                  :key="index"
                  :label="item.name"
                  :value="item.id">
                </el-option>
              </el-select>
        </el-form-item>
        <el-form-item label="文件名" width="120" prop="file.name" show-overflow-tooltip>
          <el-input v-model="edit_file.file.name"></el-input>
        </el-form-item>
        <el-form-item label="文件路径" width="120" prop="file.dlink" show-overflow-tooltip>
          <el-row>
            <el-col :span="20">
              <el-input v-model="edit_file.file.dlink"></el-input>

            </el-col>
            <el-col :span="4">
              <el-button icon="el-icon-folder-add" @click="slocaldir" circle></el-button>
            </el-col>
          </el-row>

        </el-form-item>
        <el-form-item label="标签" width="120" prop="file.tagsName" show-overflow-tooltip>
          <el-tag v-for="(tag,index) in edit_file.file.tagsName" :key="index"
            :type="tag_type[Math.round(Math.random()*(tag_type.length-1))]" closable :disable-transitions="false"
            @close="tag_close(tag)">
            {{tag}}
          </el-tag>
          <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="small"
            @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+</el-button>
        </el-form-item>
        <el-form-item label="文章内容" width="120" prop="file.title" show-overflow-tooltip>
          <mavon-editor @imgAdd="imgAdd" ref="med" placeholder="请输入文章内容" @change="mechangeData" v-model="edit_file.file.html" />
        </el-form-item>
        <el-form-item>
          <el-button type="info" @click="closedialog">取消</el-button>
          <el-button type="danger" @click="dialog_submit">提交</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <input type="file" id="filepicker" style="display: none;" name="fileList" @click="aa" />
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
         u_s_list: []
      }
    },
    props: ['showdialog','edit_title', 'edit_file', 'tag_type','addarticle'],
    methods: {
      u_select(){
       var select_id=this.edit_file.file.uid
       let that=this
       this.u_s_list.forEach(item=>{
         if(item.id === select_id){
           that.edit_file.file.uname=item.name
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
      imgAdd(a,b){
        // console.log("img add ::: ",a,b)
        var formdata = new FormData();
        let that=this
        formdata.append("uid",this.edit_file.file.uid);
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
      aa(e){
        let that = this
        document.getElementById("filepicker").addEventListener("change", function(event) {
          var formData = new FormData()
          formData.append("file" ,event.target.files[0])
             httppost("aa/blog/blog-file/admin/uploadfile",formData).then(
                (res)=>{
                    if(res.code == 0){
                        // console.log(res)
                        that.edit_file.file.dlink=res.data
                         that.$message(
                            {
                                type: "success",
                                message: "文件上传成功"
                            }
                        );
                        
                        // this.closedialog();
                    }else{
                        that.$message(
                            {
                                type: "error",
                                message: res.msg
                            }
                        );
                    }
                }
            )
        }, false);
      },
      slocaldir(){
          document.getElementById("filepicker").click()
      },
      mechangeData(a, b) {
        this.mavon_edito_value = a
        this.mavon_edito_html=b
        // console.log(a,b)
      },
      tag_close(tag) {
        let tags = this.edit_file.file.tagsName;
        tags.splice(tags.indexOf(tag), 1);
      },
      dialog_submit() {
        this.edit_file.file.text = this.mavon_edito_value
        this.edit_file.file.html = this.mavon_edito_html
        let that = this
      
        httppost("aa/blog/blog-file/admin/change",JSON.stringify(that.edit_file.file)).then(
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
          this.edit_file.file.tagsName.push(inputValue);
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
