<script setup lang="ts">
import { ref } from 'vue';
import { showToast } from 'vant';

// 占位
const value = ref(' ');

const onSearch = (val) => showToast(val);
const onCancel = () => showToast('取消');


//标签列表
const tagList = [
  {
    text: '浙江',
    children: [
      { text: '杭州', id: 1 },
      { text: '温州', id: 2 },
      { text: '宁波', id: 3, disabled: true },
    ],
  },
  {
    text: '江苏',
    children: [
      { text: '南京', id: 4 },
      { text: '无锡', id: 5 },
      { text: '徐州', id: 6 },
    ],
  },
  { text: '福建', disabled: true },
];

//已选择的标签
const activeIds = ref([]);
const activeIndex = ref(0);

//关闭标签
const  doClose = (tag) =>{
  activeIds.value = activeIds.value.filter(item =>{
    return item !== tag;
  })

}


</script>

<template>
  <form action="/">
    <van-search
        v-model="value"
        show-action
        placeholder="请输入搜索标签"
        @search="onSearch"
        @cancel="onCancel"
    />
  </form>

  <van-divider dashed>已选</van-divider>
  <div v-if="tagList.length === 0">....</div>
  <van-tag v-for="tag in activeIds" :show="true" closeable size="large" type="primary" @close="doClose(tag)">
    {{ tag }}
  </van-tag>




  <van-divider dashed>文本</van-divider>
  <van-tree-select
      v-model:active-id="activeIds"
      v-model:main-active-index="activeIndex"
      :items="tagList"
  />




</template>




<style scoped>

</style>