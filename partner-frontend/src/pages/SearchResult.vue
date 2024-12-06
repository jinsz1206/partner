<template>
  <user-card-list :user-list="userList" />
  <van-empty v-if="!userList || userList.length < 1" description="搜索接口为空"  />
</template>

<script setup >
import {onMounted, ref} from "vue";
import {useRoute} from "vue-router";

import myAxios from "../plugins/myAxios.ts";

import qs from 'qs'
import UserCardList from "../components/UserCardList.vue";

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

const userList = ref();


onMounted( async () =>{
  // 为给定 ID 的 user 创建请求
  const userListData = await myAxios.get('/user/search/tags',{
    withCredentials: false,
    params: {
      tagNameList: tags
    },

    //序列化
    paramsSerializer: {
      serialize: params => qs.stringify(params, { indices: false}),
    }
  })
      .then(function (response) {
        console.log('/user/search/tags succeed',response);
        showToast('请求成功');
        return response?.data;
      })
      .catch(function (error) {
        console.log('/user/search/tags error',error);
        showToast('请求失败');
      });
  if (userListData){
    userListData.forEach(user =>{
      if (user.tags){
        user.tags = JSON.parse(user.tags);
      }
    })
    userList.value = userListData;
  }
})



</script>

<style scoped>

</style>