<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="userAccout"
          name="userAccout"
          label="账户"
          placeholder="账户"
          :rules="[{ required: true, message: '请填写账户' }]"
      />
      <van-field
          v-model="userPassword"
          type="password"
          name="userPassword"
          label="密码"
          placeholder="密码"
          :rules="[{ required: true, message: '请填写密码' }]"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>
</template>


<script setup lang="ts">

import {useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {showToast} from "vant";
import MyAxios from "../plugins/myAxios.ts";

const router = useRouter();

const userAccout = ref('');
const userPassword = ref('');

const user = ref();

const onSubmit = async () => {
  const res = await myAxios.post('/user/login',{
    userAccount: userAccout.value,
    userPassword: userPassword.value,
  })
  console.log(res,'用户登录');
  if (res.code == 0 && res.data){
    user.value = res.data;
    showToast('获取用户');
    router.replace('/')
  } else {
    showToast('登录失败')
    router.replace('/user/login')
  }
};


</script>

<style scoped>

</style>