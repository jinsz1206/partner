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


const teamList = ref([])
const listTeam = async (val = '') => {
  const res = await myAxios.get("/team/list/myCreated", {
    params: {
      searchText: val,
      pageNum: 1,
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
    <van-button type="primary" @click = "doJoinTeam">创建队伍</van-button>
    <team-card-list :teamList="teamList"></team-card-list>
    <van-empty v-if="!teamList || teamList.length < 1" description="搜索接口为空"  />
  </div>

</template>

<style scoped>

</style>