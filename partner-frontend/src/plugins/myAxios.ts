// Set config defaults when creating the instance
//自定义实例默认值
import axios from "axios";
import {useRoute, useRouter} from "vue-router";
import {showToast} from "vant";



const myAxios = axios.create({
    baseURL: '/api',
    proxy: {
        host: 'localhost',
        port: 4396
    }
});


//不知道要不要
// myAxios.defaults.withCredentials = true;



//拦截器
// 添加请求拦截器
myAxios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    console.log("请求发送了",config)
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});
const router = useRouter()

// 添加响应拦截器
myAxios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    //未登录跳转登录
    if (response?.data?.code == 40100) {
        showToast("请先登录")
        window.location.href = '#/user/login'
    } return response.data;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});

export default myAxios;
