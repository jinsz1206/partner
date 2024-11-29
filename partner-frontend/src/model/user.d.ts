import app from "../App.vue";

type UserType = {
    id:number;
    userName:String;
    userAccount:String;
    userAvatar?:String;
    userGender:String;
    userProfile:String;
    userRole:String;
    tags:string[];
    createTime:Date;
};

export default UserType;