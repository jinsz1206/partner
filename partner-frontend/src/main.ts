import {createApp} from 'vue'
import App from './App.vue'
import {Button, Card, Icon, NavBar, Search, showToast, Tabbar, TabbarItem, Tag} from "vant";
import * as VueRouter from 'vue-router'
import routes from "./config/route.ts";


const app = createApp(App)
app.use(Button);
app.use(NavBar);
app.use(Icon);
app.use(Tabbar);
app.use(TabbarItem);
app.use(showToast);
app.use(Search);
app.use(Tag);
app.use(Card);

const router = VueRouter.createRouter({
    history : VueRouter.createWebHashHistory(),
    routes,
})

app.use(router);




app.mount('#app')