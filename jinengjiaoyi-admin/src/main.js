import Vue from 'vue'
import App from '@/App.vue'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
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

Vue.use(ElementUI, { size: 'medium', zIndex: 3000 })

Vue.prototype.$getBgName = base.getBgName()
Vue.prototype.$getUsersName = base.getUsersName()
Vue.prototype.$validate = validate
Vue.prototype.$http = http
Vue.prototype.$echarts = echarts
Vue.prototype.$base = base.get()
Vue.prototype.$project = base.getProjectName()
Vue.prototype.$storage = storage
Vue.prototype.$api = api
Vue.prototype.getAge = getAge
Vue.prototype.isAuth = isAuth
Vue.prototype.getCurDateTime = getCurDateTime
Vue.prototype.getCurDate = getCurDate

Vue.component('bread-crumbs', BreadCrumbs)
Vue.component('file-upload', FileUpload)
Vue.component('editor', Editor)

const elPlusIconMap = {
	Search: 'el-icon-search',
	Refresh: 'el-icon-refresh',
	RefreshRight: 'el-icon-refresh-right',
	Plus: 'el-icon-plus',
	Delete: 'el-icon-delete',
	Edit: 'el-icon-edit',
	EditPen: 'el-icon-edit',
	Check: 'el-icon-check',
	Close: 'el-icon-close',
	View: 'el-icon-view',
	Sort: 'el-icon-sort',
	Download: 'el-icon-download',
	Upload: 'el-icon-upload',
	Loading: 'el-icon-loading',
	Warning: 'el-icon-warning',
	CircleCheck: 'el-icon-circle-check',
	Microphone: 'el-icon-microphone',
	VideoPause: 'el-icon-video-pause',
	VideoPlay: 'el-icon-video-play',
	VideoCamera: 'el-icon-video-camera',
	Camera: 'el-icon-camera',
	Picture: 'el-icon-picture-outline',
	Document: 'el-icon-document',
	Folder: 'el-icon-folder',
	Cpu: 'el-icon-s-platform',
	Clock: 'el-icon-time',
	ChatDotRound: 'el-icon-chat-dot-round',
	User: 'el-icon-user',
	UserFilled: 'el-icon-user-solid',
	Back: 'el-icon-back',
	Grid: 'el-icon-menu',
	List: 'el-icon-tickets',
	Reading: 'el-icon-reading',
	Promotion: 'el-icon-s-promotion',
	Wallet: 'el-icon-wallet',
	Tickets: 'el-icon-tickets',
	DocumentChecked: 'el-icon-document-checked',
	Menu: 'el-icon-menu',
	Notebook: 'el-icon-notebook-2',
	ChatDotSquare: 'el-icon-chat-dot-square',
	Message: 'el-icon-message',
}
Object.entries(elPlusIconMap).forEach(([name, cls]) => {
	Vue.component(name, {
		functional: true,
		render(h) { return h('i', { class: cls }) }
	})
})

Vue.mixin({
	mounted() {
		if (this.$data && Object.prototype.hasOwnProperty.call(this.$data, 'pageSize') && typeof this.getDataList === 'function') {
			this.$nextTick(() => {
				this._calcAutoPageSize()
				window.addEventListener('resize', this._onResizeCalc)
			})
		}
	},
	beforeDestroy() {
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

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app')
