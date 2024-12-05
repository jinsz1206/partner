import {setCurrentUser} from "../states/user.ts";
import MyAxios from "../plugins/myAxios.ts";
import {showToast} from "vant";


export const getCurrentUser = async() =>{
    const res = await MyAxios.get("/user/current");
    if(res.code === 0){
        showToast("cheng")
        setCurrentUser(res.data);
        return res.data;
    }else {
        showToast("fail");
    }
}