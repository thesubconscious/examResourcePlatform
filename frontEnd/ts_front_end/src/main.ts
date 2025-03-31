import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import i18n from './services/i18n'
import {authManager} from "@/services/AuthManager";
import './iconFormat.css'

const app = createApp(App)

app.use(router)
app.use(i18n)
// app.component('font-awesome-icon', FontAwesomeIcon);

app.mount('#app')
authManager.startPolling()
