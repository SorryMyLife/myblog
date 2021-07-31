<template>
    <div>
        <el-dialog :title="out_title" :close-on-click-modal='false' :before-close="closeDia" :visible.sync="showExcelDialog" center>
        <div>
            <el-input
            placeholder="请输入需要导出的数据数量:比如输入1则导出1条数据"
            v-model="Line"
            clearable>
            </el-input>
        </div>
        <div>
            <el-radio-group v-model="radio" @change="r_change">
                <el-radio :label="1">Excel</el-radio>
                <el-radio :label="2">PDF</el-radio>
                <el-radio :label="3">Word</el-radio>
            </el-radio-group>
        </div>

        <div slot="footer" class="dialog-footer">
            <el-button @click="closeDia">取消</el-button>
            <el-button type="primary" @click="sss">确定</el-button>
        </div>
        </el-dialog>
    </div>
</template>
<script>
import {httpget ,httppost} from '../../static/utils/request.js'
export default {
    data(){
        return{
            Line: 1,
            out_title: "导出为Excel表格",
            radio: 1,
            http_url: ""
        }
    },
    props: ['ccctype','showExcelDialog'],
    methods: {
        r_change(a){
            switch(a){
                case 1:
                    this.out_title="导出为Excel表格"
                    this.http_url="aa/blog/extract/ExtractExcel?ccctype="
                    break;
                case 2:
                    this.out_title="导出为PDF文件"
                    this.http_url="aa/blog/extract/ExtractPDF?ccctype="
                    break;
                case 3:
                    this.out_title="导出为Word文件"
                    this.http_url="aa/blog/extract/ExtractWord?ccctype="
                    break;
            }
        },
        sss(){
            window.open(this.http_url+this.ccctype+"&line="+this.Line, "_blank")
            // console.log(this.ccctype,this.Line);
            this.closeDia();
        },

        closeDia(){
            this.$emit("getshowdialog", false)
        }

    }
}
</script>