<template>
  <div
      id="teamCardList"
  >
    <van-card
        v-for="team in props.teamList"
        :desc="team.description"
        thumb="https://th.bing.com/th/id/OIP.xAI5AEBeQpMF1kAvUYkjKAHaEo"
        :title="`${team.name} `"
    >
      <template #tags>
        <van-tag plain type="danger" style="margin-right: 8px; margin-top: 8px" >
          {{
            teamStatusEnum[team.status]
          }}
        </van-tag>
      </template>
      <template #bottom>
        <div>
          {{'队伍人数:' + team.hasJoinNum + '/' + team.maxNum}}
        </div>
        <div>
          {{'过期时间:' + team.expireTime}}
        </div>
        <div>
          {{'创建时间:' + team.createTime}}
        </div>
      </template>
      <template #footer>
        <van-button size="mini" type="primary" v-if="team.createUser?.id !== currentUser?.id && !team.hasJoin" plain
                    @click="preJoinTeam(team)">
          加入队伍
        </van-button>
        <van-button v-if="team.createUser?.id == currentUser?.id" size="mini" plain type="primary"
                    @click="doUpdateTeam(team.id)">更新队伍
        </van-button>
        <van-button v-if="team.createUser?.id != currentUser?.id && team.hasJoin" size="mini" plain type="primary"
                    @click="doQuitTeam(team.id)">退出队伍
        </van-button>
        <van-button v-if="team.createUser?.id == currentUser?.id" size="mini" plain type="primary"
                    @click="doDeleteTeamTeam(team.id)">解散队伍
        </van-button>
      </template>
    </van-card>
  </div>
  <van-dialog v-model:show="showPasswordDialog" title="请输入密码" show-cancel-button @confirm ="doJoinTeam"
              @cancel = "doJoinCancel">
    <van-field v-model="password" placeholder="请输入密码" type="password"/>
  </van-dialog>
 </template>

<script setup lang="ts">
import {TeamType} from "../model/team";
import {teamStatusEnum} from "../constants/team";
import myAxios from "../plugins/myAxios";
import { showToast} from "vant";
import {getCurrentUser} from "../services/user.ts";
import {onMounted,ref} from "vue";
import {useRouter} from "vue-router";
const router = useRouter();

const showPasswordDialog = ref(false);
const password = ref('');
const joinTeamId = ref(0);




interface TeamCardListProps{
  teamList: TeamType[];
}


const props = withDefaults(defineProps<TeamCardListProps>(),{
  //@ts-ignore
  teamList: [] as TeamType[],
});



//获取用户
const currentUser = ref();
onMounted(async () => {
  currentUser.value = await getCurrentUser();
})


const preJoinTeam = (team:TeamType) =>{
  joinTeamId.value = team.id;
  if (team.status === 0){
    doJoinTeam()
  }else {
    showPasswordDialog.value = true;
  }
}

//队伍列表加入队伍
/**
 * 加入队伍
 * @param id
 */
const doJoinTeam = async() =>{
  if (!joinTeamId.value){
    return;
  }
  const res = await myAxios.post("/team/join",{
    teamId : joinTeamId.value,
    password : password.value,
  });
  if (res?.code === 0){
    showToast("加入成功")
  }else {
    showToast("加入失败" + (res.description ? `， ${res.description} `:''));
  }
}

const doJoinCancel = () =>{
  joinTeamId.value = 0;
  password.value = '';
}


//更新队伍
const doUpdateTeam = (id: number) =>{
  console.log("更新队伍")
  router.push({
    path: "/team/update",
    query: {
      teamId : id
    }
  })
}




/**
 * 退出队伍
 * @param id
 */
const doQuitTeam = async(id: number) =>{
  const res = await myAxios.post("/team/quit",{
    teamId : id
  });
  if (res?.code === 0){
    showToast("退出成功")
  }else {
    showToast("退出失败" + (res.description ? `， ${res.description} `:''));
  }
}

/**
 * 解散队伍
 * @param id
 */
const doDeleteTeamTeam = async(id: number) =>{
  const res = await myAxios.post("/team/delete",{
     id
  });
  if (res?.code === 0){
    showToast("解散成功")
  }else {
    showToast("解散失败" + (res.description ? `， ${res.description} `:''));
}}


</script>

<style scoped>
#teamCardList :deep(.van-image__img){
  height: 128px;
  object-fit: unset;
}
</style>