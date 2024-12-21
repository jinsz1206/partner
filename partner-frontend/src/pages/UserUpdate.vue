<script setup lang="ts">
import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import {showToast} from "vant";
import {getCurrentUser} from "../services/user.ts";



const user = ref();

onMounted(async () => {
  const res = await getCurrentUser();
  if (res) {
    user.value = res;
    user.value.formattedCreateTime = new Date(user.value.createTime).toLocaleDateString('zh-CN');
    showToast('success');

  }else {
    showToast('error');
  }
})

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
  <template v-if="user">
    <van-cell title="昵称" is-link to="/user/edit" :value="user.userName" @click="toEdit('userName','昵称',user.userName)"/>
    <van-cell title="账户" :value="user.userAccount" />
    <van-cell title="头像" is-link to="/user/edit" >
      <img style="height: 48px" :src="user.userAvatar">
    </van-cell>
    <van-cell title="性别" is-link to="/user/edit" :value="user.userGender" @click="toEdit('userGender','性别',user.userGender)" />
    <van-cell title="注册时间" :value="user.formattedCreateTime"  />
  </template>
</template>



<style scoped>

</style>