<script setup lang="ts">

import {useRouter} from "vue-router";
import {onMounted} from "vue";
import myAxios from "../plugins/myAxios.ts";
import {showToast} from "vant";
import { ref } from "vue";
import TeamCardList from "../components/TeamCardList.vue";
const router = useRouter()
const doJoinTeam = () =>{
  router.push({
    path: "team/add"
  })
}

const active = ref('public')
const searchText = ref('')

const onTabChange = (name) => {
  if (name === 'public') {
    listTeam(searchText.value,0)
  }else{
    listTeam(searchText.value,2)
  }
}

const teamList = ref([])
const listTeam = async (val = '',status = 0) => {
  const res = await myAxios.get("/team/list", {
    params: {
      searchText: val,
      pageNum: 1,
      status,
    }
  })
  if(res?.code === 0){
    teamList.value = res.data
  }else{
    showToast("error")
  }
}

//页面加载触发一次
onMounted(()=>{
  listTeam()
})


//搜索
const onSearch = (val)=>{
  listTeam(val)
}

</script>


<template>
  <div id ="teamPage">
    <van-search v-model="serchText" placeholder="搜索队伍" clearable="true" @search="onSearch"/>
    <van-tabs v-model:active="active" @change="onTabChange">
      <van-tab title="公开" name = "public" />
      <van-tab title="加密" name=  "private" />
    </van-tabs>
    <div style="margin-bottom: 16px" />
    <van-button class="add-button" type="primary" icon="plus" @click="doJoinTeam" />
    <team-card-list :teamList="teamList"></team-card-list>
    <van-empty v-if="!teamList || teamList.length < 1" description="搜索接口为空"  />
  </div>

</template>

<style scoped>

</style>