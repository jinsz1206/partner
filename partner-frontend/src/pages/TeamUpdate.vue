<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios.ts";
import {showToast} from "vant";

const router = useRouter();
const route = useRoute();

const addTeamData = ref({});


const id = route.query.teamId;
//获取之前的队伍信息
onMounted(async () => {
  if (id <= 0){
    showToast("加载队伍失败，请重试");
    return;
  }
  const res = await  myAxios.get("/team/get",{
    params: {
      id,
    }
  });
  if (res?.code === 0){
    addTeamData.value = res.data;
  }else {
    showToast("加载队伍失败，请重试")
  }
})

// 展示日期选择器
//todo  日期有时候传递为空 状态为空

const pickerValue = ref(['2025','12','16'])
const result = ref('');
const showPicker = ref(false);
const onConfirm = ({ selectedValues }) => {
  result.value = selectedValues.join('/');
  showPicker.value = false;
}


function formatDate(array) {
  const [year, month, day] = array;
  return `${year}-${month}-${day}`;
}

//提交
const onSubmit = async () => {
  const postData = {
    ...addTeamData.value,
    status: Number(addTeamData.value.status),
    expireTime:new Date(`${formatDate(addTeamData.value.expireTime)}T00:00:00`),

  }
  //todo 前端数据校验
  const res = await myAxios.post("/team/update",postData);
  if (res?.code === 0 && res.data){
    showToast("更新成功");
    router.push({
      path:'/team',
      replace:true,
    });
  }else {
    showToast("更新失败")
  }
}


</script>


<template>
  <div id="teamPage">
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
            v-model="addTeamData.name"
            name="name"
            label="队伍名"
            placeholder="请输入队伍名"
            :rules="[{ required: true, message: '请输入队伍名' }]"
        />
        <van-field
            v-model="addTeamData.description"
            rows="4"
            autosize
            label="队伍描述"
            type="textarea"
            placeholder="请输入队伍描述"
        />
        <van-field
            v-model="result"
            is-link
            readonly
            name="datePicker"
            label="时间选择"
            placeholder="点击选择时间"
            @click="showPicker = true"
        />
        <van-popup v-model:show="showPicker" destroy-on-close position="bottom">
          <van-date-picker
              v-model="addTeamData.expireTime"
              :model-value="pickerValue"
              @confirm="onConfirm"
              @cancel="showPicker = false"
          />
        </van-popup>

        <van-field name="stepper" label="最大人数">
          <template #input>
            <van-stepper v-model="addTeamData.maxNum" max="10" min="3"/>
          </template>
        </van-field>
        <van-field name="radio" label="队伍状态">
          <template #input>
            <van-radio-group v-model="addTeamData.status" direction="horizontal">
              <van-radio name="0">公开</van-radio>
              <van-radio name="1">私有</van-radio>
              <van-radio name="2">加密</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
            v-if="Number(addTeamData.status) === 2"
            v-model="addTeamData.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入队伍密码"
            :rules="[{ required: true, message: '请填写密码' }]"
        />
      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          提交
        </van-button>
      </div>
    </van-form>
  </div>

</template>

<style scoped>

</style>