

<script setup>
  import { ref } from 'vue';

  import Index from "../pages/index.vue";
  import Team from "../pages/Team.vue";
  const onClickLeft = () => alter('左边');
  const onClickRight = () => alter('右边');
  const active = ref("index");
  const onChange = (index) =>showToast(`标签 ${index}`);
</script>





<template>


  <van-nav-bar
      title="标题"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
  >
    <template #right>
      <van-icon name="search" size="18" />
    </template>
    </van-nav-bar>
  <div id="content">
    <template v-if="active === 'index'">
      <Index />
    </template>
    <template v-if="active === 'team'">
      <Team />
    </template>


  </div>

  <van-tabbar v-model="active" @change="onChange">
    <van-tabbar-item icon="home-o" name="index">主页</van-tabbar-item>
    <van-tabbar-item icon="search" name="team">队伍</van-tabbar-item>
    <van-tabbar-item icon="friends-o" name="user">个人</van-tabbar-item>
  </van-tabbar>

</template>









<style scoped>

</style>