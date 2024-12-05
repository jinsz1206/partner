<script setup lang="ts">
import { ref } from 'vue';
import {useRoute, useRouter} from "vue-router";
import {getCurrentUser} from "../states/user.ts";
import {showToast} from "vant";
import myAxios from "../plugins/myAxios.ts";

const router = useRouter()
const route = useRoute();
const editUser = ref({
  editKey:route.query.editKey,
  currentValue:route.query.currentValue,
  editName:route.query.editName,
})

const onSubmit = async () => {
  //获取用户
  const currentUser = await getCurrentUser()
  if (!currentUser) {
    showToast('用户未登录')
    return
  }
  const res = await myAxios.post('/user/update', {
    'id': currentUser.id,
    [editUser.value.editKey]:editUser.value.currentValue
  })
  console.log(res,'更新成功')
  if (res.code === 0 && res.data > 0) {
    showToast('修改成功');
    router.back();
  }else {
    showToast('error');
  }
}

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