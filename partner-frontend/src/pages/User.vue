<script setup lang="ts">
import {useRouter} from "vue-router";


const user = {
  id: 1,
  userName: 'jsz',
  userAccount: '12344',
  userAvatar: 'String',
  userGender: '男',
  userProfile: '213213',
  userRole: 'admin',
  createTime: new Date(),
};

const router = useRouter()
const toEdit = (editKey:string, editName:string, currentValue:string) => {
  router.push({
    path:'/user/edit',
    query:{
      editKey,
      editName,
      currentValue,
    }
  })
}
</script>

<template>
  <van-cell-group inset>
    <van-cell title="名称" is-link to="/user/edit" :value="user.userName" @click="toEdit('userName','昵称',user.userName)"/>
    <van-cell title="账号" is-link to="/user/edit" :value="user.userAccount" />
    <van-cell title="头像" is-link to="/user/edit" :value="user.userAvatar" />
    <van-cell title="性别" is-link to="/user/edit" :value="user.userGender" @click="toEdit('userGender','性别',user.userGender)"/>
    <van-cell title="注册时间" :value="user.createTime.toISOString()" />
  </van-cell-group>
</template>



<style scoped>

</style>