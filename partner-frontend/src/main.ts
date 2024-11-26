import {createApp} from 'vue'
import App from './App.vue'
import {Button, Icon, NavBar, showToast, Tabbar, TabbarItem} from "vant";

const app = createApp(App)
app.use(Button);
app.use(NavBar);
app.use(Icon);
app.use(Tabbar);
app.use(TabbarItem);
app.use(showToast)



app.mount('#app')
