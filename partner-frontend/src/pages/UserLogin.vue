<script setup lang="ts">
import { ref } from 'vue';

const value = ref('');



</script>

<template>
  <!-- 可以使用 CellGroup 作为容器 -->
  <van-cell-group inset>
    <van-field v-model="value" label="文本" placeholder="请输入用户名" />
  </van-cell-group>

</template>

<style scoped>

</style>