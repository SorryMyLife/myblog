<template>
    <div>
        <el-dialog
            title="请验证"
            :visible.sync="showVerCode"
            width="460px"
            :before-close="handleClose">
            <img :src="verCode.img1" @click="getVerCode"/>
            <div :style='"z-index:999;transform: translate("+value3+"px, "+verCode.y+"px);"'>
                <img :src="verCode.img2" @click="getVerCode"/>
            </div>
            <el-slider :max="mmm" @change="ssslid" v-model="value3" :show-tooltip="false"></el-slider>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="getVerCode">刷新验证码</el-button>
            </span>
            </el-dialog>

    </div>
</template>
<script>
import { httpget, httppost } from "../../static/utils/request.js";
export default {
    data(){
        return{
            verCode: {
                img1: '',
                img2: '',
                y: ''
            },
            value3: 0,
            mmm: 0
        }
    },
    props: ['showVerCode','uuid','ccid'],
    methods: {
        ssslid(){
            let parm={
                cid: this.ccid,
                uid: this.uuid,
                x:this.value3
            }
            let that=this
            httppost("aa/blog/blog-verif/Verif",JSON.stringify(parm)).then(
                (res)=>{
                    if(res.code ==0){
                        that.handleClose()
                    }else{
                        that.$message.error(res.msg);
                        that.getVerCode()
                    }
                }
            )
        },
        getVerCode(){
            let parm={
                cid: this.ccid,
                uid: this.uuid
            }
            let that=this
            httppost("aa/blog/blog-verif/getVerif",JSON.stringify(parm)).then(
                (res)=>{
                if(res.code ==0){
                    that.verCode.img1=res.data.img1
                    that.verCode.img2=res.data.img2
                    that.verCode.y=-res.data.h-4
                    that.value3=0
                    var img = new Image();
                    img.src=res.data.img1
                    img.onload = function(){
                        that.mmm=img.width
                    }
                    }else{
                        that.$message.error(res.msg);
                    }
                }
            )
            },
        handleClose(){
            this.$emit("getshowdialog", false)
        },


    },
    created() {
        this.getVerCode()
    }
}
</script>
