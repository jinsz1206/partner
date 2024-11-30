<template>
  <van-card
      v-for="user in userList"
      :desc="user.profile"
      :title="`${user.username} (${user.planetCode})`"
      :thumb="user.avatarUrl"
  >
    <template #tags>
      <van-tag plain type="danger" v-for="tag in tags" style="margin-right: 8px; margin-top: 8px" >
        {{tag}}
      </van-tag>
    </template>
    <template #footer>
      <van-button size="mini">联系我</van-button>
    </template>
  </van-card>
</template>

<script setup >
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {Toast} from "vant";

import myAxios from "../plugins/myAxios.js";

import qs from 'qs'

const route = useRoute();
const {tags} = route.query;

const mockUser = ref({
  id: 931,
  username: '沙鱼',
  userAccount: 'shayu',
  profile: '一条咸鱼',
  gender: 0,
  phone: '123456789101',
  email: 'shayu-yusha@qq.com',
  planetCode: '931',
  avatarUrl: 'https://xingqiu-tuchuang-1256524210.cos.ap-shanghai.myqcloud.com/shayu931/shayu.png',
  tags: ['java', 'emo', '打工中', 'emo', '打工中'],
  createTime: new Date(),
})

const userList = ref({mockUser});


onMounted( () =>{
  // 为给定 ID 的 user 创建请求
  myAxios.get('/user/search/tags',{
    withCredentials: false,
    params: {
      tagNameList: tags
    },
    //用鱼皮的这个我的头像不会显示。
    // paramsSerializer: params =>{
    //   return qs.stringify(params,{indices: false})
    // }

    //序列化
    paramsSerializer: {
      serialize: params => qs.stringify(params, { indices: false}),
    }

  })
      .then(function (response) {
        console.log('/user/search/tags succeed',response);
        Toast.success('请求成功');
      })
      .catch(function (error) {
        console.log('/user/search/tags error',error);
        Toast.fail('请求失败');
      });

})



</script>

<style scoped>

</style>