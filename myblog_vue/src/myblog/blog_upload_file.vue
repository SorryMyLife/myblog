<template>
    <div>
        <el-dialog
            title="提示"
            :visible.sync="isShow"
            width="60%"
            :before-close="closedialog">
            <div>
                <el-input
                    placeholder="请输入内容"
                    v-model="file_text"
                    clearable>
                </el-input>
            </div>
            <div>
                <el-tag
                    :key="tag"
                    v-for="tag in dynamicTags"
                    closable
                    :disable-transitions="false"
                    @close="handleClose(tag)">
                    {{tag}}
                </el-tag>
                <el-input
                    class="input-new-tag"
                    v-if="inputVisible"
                    v-model="inputValue"
                    ref="saveTagInput"
                    size="small"
                    width="100px"
                    @keyup.enter.native="handleInputConfirm"
                    @blur="handleInputConfirm"
                    >
                </el-input>
                <el-button v-else class="button-new-tag" size="small" @click="showInput">+</el-button>

            </div>
            <div>
                <el-upload
                class="upload-demo"
                drag
                ref="upload"
                :auto-upload="false"
                :http-request="uploadfiles"
                action=""
                multiple>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            </el-upload>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="closedialog">取消</el-button>
                <el-button type="primary" @click="upload_file">上传</el-button>
            </span>
        </el-dialog>
        
    </div>
</template>
<script>
import {httpget ,httppost} from '../../static/utils/request.js'
  
export default {
    data(){
        return{
            file_text: '',
            dynamicTags: [],
            inputVisible: false,
            inputValue: ''
        }
    },
    props: ['isShow'],
    methods:{
        uploadfiles(p){
            // console.log(p)
            let formData = new FormData()
            formData.append("text",this.file_text)
            formData.append("tags",this.dynamicTags.toString())
            formData.append("file" ,p.file)
            let that=this
            httppost("aa/blog/blog-file/save",formData).then(
                (res)=>{
                    if(res.code == 0){
                        // console.log(res)
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
        },
        upload_file(){
            this.$refs.upload.submit();
        },
        closedialog() {
        this.$emit("getupload", false)
        this.$router.go(0)
      },
        handleClose(tag) {
            this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
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
          this.dynamicTags.push(inputValue);
        }
        this.inputVisible = false;
        this.inputValue = '';
      }
    },
    created(){

    }
}
</script>