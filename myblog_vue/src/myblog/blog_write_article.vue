<template>
  <div>
    <el-container>
      <el-header>
        <el-row>
          <el-col :span="20">
            <el-input placeholder="请输入文章标题" v-model="article_title" clearable>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button @click="article_submit">提交</el-button>
          </el-col>
        </el-row>
      </el-header>

      <el-main>
        <el-tag :key="tag" v-for="tag in dynamicTags" closable :disable-transitions="false" @close="handleClose(tag)">
          {{tag}}
        </el-tag>
        <el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput" size="small"
          @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
        </el-input>
        <el-button v-else class="button-new-tag" size="small" @click="showInput">+</el-button>
      </el-main>
      <el-footer>
        <mavon-editor style="height: 600px;" ref="med" placeholder="请输入文章内容" @imgAdd="imgAdd" @change="mechangeData" v-model="article_value" />
      </el-footer>
    </el-container>


  </div>
</template>

<script>
  import {httpget ,httppost} from '../../static/utils/request.js'
  export default {
    data() {
      return {
        article_title: '',
        article_value: '',
        mavon_edito_value: '',
        mavon_edito_html: '',
        dynamicTags: [],
        inputVisible: false,
        inputValue: ''
      };
    },
    methods: {
      imgAdd(a,b){
        // console.log("img add ::: ",a,b)
        var formdata = new FormData();
        let that=this
        formdata.append("uid","");
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
      },

      article_clean(){
        this.$refs.med.d_render=undefined
        this.article_value=undefined
        this.article_title=undefined
        this.dynamicTags=undefined
      },
      article_submit() {

        let article={
          title: this.article_title,
          text: this.mavon_edito_value,
          html: this.mavon_edito_html,
          tags: this.dynamicTags.toString()
        }
        let that=this
        httppost("aa/blog/blog-article/save",JSON.stringify(article)).then(
        (res)=>{
          if(res.code == 0){
            that.$message(
               {
                 type: "success",
                 message: "文章提交成功"
               }
            );
            that.$router.go(0)
          }else{
            that.$message.error(res.msg);
          }
        }
        );
        // console.log(article)
      }

    },
    created() {

    },
    mounted() {

    }
  };
</script>

<style>
</style>
