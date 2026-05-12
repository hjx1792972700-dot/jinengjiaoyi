import { createApp } from 'vue'
import App from '@/App.vue'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import '@/assets/css/element-variables.scss'
import '@/assets/css/style.scss'
import '@/assets/css/admin-dark.scss'
import router from '@/router/router-static.js'
import BreadCrumbs from '@/components/common/BreadCrumbs.vue'
import * as echarts from 'echarts'
import 'echarts-wordcloud'
import 'echarts/theme/macarons.js'
import http from '@/utils/http.js'
import base from '@/utils/base'
import {
	isAuth,
	getCurDate,
	getCurDateTime,
	getAge,
} from '@/utils/utils'
import storage from '@/utils/storage'
import FileUpload from '@/components/common/FileUpload.vue'
import Editor from '@/components/common/Editor.vue'
import api from '@/utils/api'
import * as validate from '@/utils/validate.js'

const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component(key, component)
}

app.config.globalProperties.$getBgName = base.getBgName()
app.config.globalProperties.$getUsersName = base.getUsersName()
app.config.globalProperties.$validate = validate
app.config.globalProperties.$http = http
app.config.globalProperties.$echarts = echarts
app.config.globalProperties.$base = base.get()
app.config.globalProperties.$project = base.getProjectName()
app.config.globalProperties.$storage = storage
app.config.globalProperties.$api = api
app.config.globalProperties.getAge = getAge
app.config.globalProperties.isAuth = isAuth
app.config.globalProperties.getCurDateTime = getCurDateTime
app.config.globalProperties.getCurDate = getCurDate

app.use(ElementPlus, { size: 'default', zIndex: 3000 })
app.use(router)
app.use(store)

app.component('bread-crumbs', BreadCrumbs)
app.component('file-upload', FileUpload)
app.component('editor', Editor)

app.mixin({
	mounted() {
		if (this.$data && Object.prototype.hasOwnProperty.call(this.$data, 'pageSize') && typeof this.getDataList === 'function') {
			this.$nextTick(() => {
				this._calcAutoPageSize()
				window.addEventListener('resize', this._onResizeCalc)
			})
		}
	},
	beforeUnmount() {
		if (this._onResizeCalc) {
			window.removeEventListener('resize', this._onResizeCalc)
		}
	},
	methods: {
		_onResizeCalc() {
			clearTimeout(this._resizeTimer)
			this._resizeTimer = setTimeout(() => this._calcAutoPageSize(), 300)
		},
		_calcAutoPageSize() {
			const table = this.$el && this.$el.querySelector && this.$el.querySelector('.el-table')
			if (!table) return
			const tableTop = table.getBoundingClientRect().top
			const vh = window.innerHeight
			const rowH = 48
			const reserve = 80
			const available = vh - tableTop - reserve
			const tableH = Math.max(240, available)
			table.style.height = tableH + 'px'
			table.style.overflow = 'hidden'
			const headerH = 40
			const bodyH = tableH - headerH
			const count = Math.max(3, Math.floor(bodyH / rowH))
			if (count !== this.pageSize) {
				this.pageSize = count
				this.pageIndex = 1
				this.getDataList()
			}
		}
	}
})

app.mount('#app')
