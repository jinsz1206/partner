<script setup lang="ts">
import { ref } from 'vue';
import {useRoute} from "vue-router";

const route = useRoute();
const editUser = ref({
  editKey:route.query.editKey,
  currentValue:route.query.currentValue,
  editName:route.query.editName,
})

const onSubmit = (values:string) => {
  //todo提交到后端
  console.log('submit', values);
};

</script>



<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="editUser.currentValue"
          :name="editUser.editKey"
          :label="editUser.editName"
          :placeholder="`请输入${editUser.editName}`"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>


</template>



<style scoped>

</style>