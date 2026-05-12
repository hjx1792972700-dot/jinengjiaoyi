import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './router/router'
import store from './store'
import '@/assets/css/iconfont.css'
import config from './config/config'
import validate from './common/validate'
import http from './utils/http'
import {
	isAuth,
	getCurDateTime,
	getCurDate,
	isBackAuth,
	getAge,
} from './common/system'
import App from './App.vue'
import Breadcrumb from '@/components/Breadcrumb.vue'
import FileUpload from '@/components/FileUpload.vue'
import Editor from '@/components/Editor.vue'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(key, component)
}

app.config.globalProperties.$config = config
app.config.globalProperties.$validate = validate
app.config.globalProperties.$http = http
app.config.globalProperties.isAuth = isAuth
app.config.globalProperties.isBackAuth = isBackAuth
app.config.globalProperties.getCurDateTime = getCurDateTime
app.config.globalProperties.getAge = getAge
app.config.globalProperties.getCurDate = getCurDate

app.use(router)
app.use(store)
app.use(ElementPlus)

app.component('Breadcrumb', Breadcrumb)
app.component('file-upload', FileUpload)
app.component('editor', Editor)

app.mount('#app')
