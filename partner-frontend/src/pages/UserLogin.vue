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


    <div style="margin: 20px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
    <div style="margin: 20px;">
      <button round @click="forget" style="border: none; background-color: transparent; color: #1989fa; padding: 0; font-size:12px; float: left;">
        忘记密码
      </button>

      <button round @click="toRegister" style="border: none; background-color: transparent; color: #1989fa; padding: 0; font-size:12px; float: right;">
        注册账号
      </button>
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

const toRegister = () => {
  router.replace('/user/register')
}
const forget = () => {
  window.location.href = 'https://github.com/jinsz1206/partner';
}

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