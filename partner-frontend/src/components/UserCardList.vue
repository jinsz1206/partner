<script setup lang="ts">
import UserType from "../model/user";
interface UserCardListProps {
  loading: boolean;
  userList:UserType[];
}
const props = withDefaults(defineProps<UserCardListProps>(), {
  loading: true,
  userList: [] as UserType[],
})

</script>

<template>
  <van-skeleton title avatar :row="3" :loading="loading" >
    <van-card
        v-for="user in props.userList"
        :desc="user.userProfile"
        :title="user.userName"
        :thumb="user.userAvatar"
    >
      <template #tags>
        <van-tag plain type="danger" v-for="tag in user.tags" style="margin-right: 8px; margin-top: 8px" >
          {{tag}}
        </van-tag>
      </template>
      <template #footer>
        <van-button size="mini">联系我</van-button>
      </template>
    </van-card>
  </van-skeleton>
</template>

<style scoped>

</style>