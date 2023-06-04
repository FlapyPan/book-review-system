import './assets/style.css'
import 'element-plus/es/components/message/style/css'
import 'element-plus/es/components/message-box/style/css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'

createApp(App)
  .use(createPinia())
  .use(router)
  .mount('#app')
