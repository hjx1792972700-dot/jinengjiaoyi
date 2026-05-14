import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
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

Vue.use(ElementUI)

Vue.prototype.$config = config
Vue.prototype.$validate = validate
Vue.prototype.$http = http
Vue.prototype.isAuth = isAuth
Vue.prototype.isBackAuth = isBackAuth
Vue.prototype.getCurDateTime = getCurDateTime
Vue.prototype.getAge = getAge
Vue.prototype.getCurDate = getCurDate

Vue.component('Breadcrumb', Breadcrumb)
Vue.component('file-upload', FileUpload)

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app')
