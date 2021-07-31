<template>
  <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="currentPage4"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="10"
        :hide-on-single-page="showPaginationValue"
        background
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagesize">
      </el-pagination>

</template>

<script>
  export default {
      methods: {
        handleSizeChange(val) {
          console.log(`每页 ${val} 条`);
        },
        handleCurrentChange(val) {
          console.log(`当前页: ${val}`);
        }
      },
      props: ['pagesize'],
      data() {
        return {
          currentPage4: 1,
          showPaginationValue: false
        };
      },
      created() {
        this.showPaginationValue = this.pagesize > 10? false:true
      }
    }
</script>

<style>
</style>
