
import Index from "../pages/index.vue";
import Team from "../pages/Team.vue";
import User from "../pages/User.vue";
import Search from "../pages/Search.vue";
import UserEdit from "../pages/UserEdit.vue";
import SearchResult from "../pages/SearchResult.vue";
import UserLogin from "../pages/UserLogin.vue";
import TeamAdd from "../pages/TeamAdd.vue";
import TeamUpdate from "../pages/TeamUpdate.vue";
import TeamCreated from "../pages/TeamCreated.vue";
import TeamJoined from "../pages/TeamJoined.vue";
import UserUpdate from "../pages/UserUpdate.vue";
import UserRegister from "../pages/UserRegister.vue";



const routes = [
    {path: "/", component: Index},
    {path: "/team",component: Team,title:'寻找队伍'},
    {path: "/user", component: User,title:'个人信息'},
    {path: "/search", component: Search ,title: '寻找伙伴'},
    {path: "/user/edit", component: UserEdit ,title: '编辑'},
    {path: "/user/list", component: SearchResult ,title: '用户列表'},
    {path: "/user/login", component: UserLogin ,title: '登录'},
    {path: "/team/add", component: TeamAdd ,title: '创建队伍'},
    {path: "/team/update", component: TeamUpdate ,title: '用户更新'},
    {path: "/team/created", component: TeamCreated ,title: '创建队伍'},
    {path: "/team/joined", component: TeamJoined ,title: '加入队伍'},
    {path: "/user/update", component: UserUpdate ,title: '队伍更新'},
    {path: "/user/register", component: UserRegister ,title: '注册'},
]

export default routes;