<template>

  <div>
    <div v-if="showUserIcon===true">
      <div @click="inuserinfo(user_info.id)">
        <img :src="user_info.icon" style=" width:50px;height:50px; border-radius:50%; " />
       </div>
      <div v-if="showUserName===true" @click="inuserinfo(user_info.id)" >
        {{user_info.name}}
      </div>
    </div>
    <div v-else-if="showUserName===true" @click="inuserinfo(user_info.id)">
        {{user_info.name}}
    </div>
  </div>
</template>

<script>
  export default {
    data() {
      return {

      }
    },
    props: ['showUserIcon', 'showUserName', 'user_info'],
    methods: {
      inuserinfo(uid) {
        // console.log("user icon ::: ",uid)
        if(uid === undefined || uid === "" || uid === null){
          uid="user"
        }
        document.cookie="afff="+uid
        this.$router.push({
          path: '/User',
          params: uid
        })
        this.$router.go(0)
      },
    }
  }
</script>

<style>
</style>
