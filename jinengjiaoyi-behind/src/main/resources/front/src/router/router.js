import Vue from 'vue'
import VueRouter from 'vue-router'
import { Message } from 'element-ui'

const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
	return originalPush.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

import Index from '../pages/index.vue'
import Home from '../pages/home/home.vue'
import Portal from '../pages/portal/portal.vue'
import Register from '../pages/register/register.vue'
import Center from '../pages/center/center.vue'
import Messages from '../pages/messages/list.vue'
import Storeup from '../pages/storeup/list.vue'
import payList from '../pages/pay.vue'

import yonghuList from '../pages/yonghu/list.vue'
import yonghuDetail from '../pages/yonghu/detail.vue'
import yonghuAdd from '../pages/yonghu/add.vue'
import huiyuanchongzhiList from '../pages/huiyuanchongzhi/list.vue'
import huiyuanchongzhiDetail from '../pages/huiyuanchongzhi/detail.vue'
import huiyuanchongzhiAdd from '../pages/huiyuanchongzhi/add.vue'
import jinengfenleiList from '../pages/jinengfenlei/list.vue'
import jinengfenleiDetail from '../pages/jinengfenlei/detail.vue'
import jinengfenleiAdd from '../pages/jinengfenlei/add.vue'
import jinengxuqiuList from '../pages/jinengxuqiu/list.vue'
import jinengxuqiuDetail from '../pages/jinengxuqiu/detail.vue'
import jinengxuqiuAdd from '../pages/jinengxuqiu/add.vue'
import skillAdd from '../pages/jinengxuqiu/skillAdd.vue'
import jiaohuanshenqingList from '../pages/jiaohuanshenqing/list.vue'
import jiaohuanshenqingDetail from '../pages/jiaohuanshenqing/detail.vue'
import jiaohuanshenqingAdd from '../pages/jiaohuanshenqing/add.vue'
import jiaohuanjiluList from '../pages/jiaohuanjilu/list.vue'
import jiaohuanjiluDetail from '../pages/jiaohuanjilu/detail.vue'
import jiaohuanjiluAdd from '../pages/jiaohuanjilu/add.vue'

import ziliaoleixingList from '../pages/ziliaoleixing/list.vue'
import ziliaoleixingDetail from '../pages/ziliaoleixing/detail.vue'
import ziliaoleixingAdd from '../pages/ziliaoleixing/add.vue'
import xuexiziliaoList from '../pages/xuexiziliao/list.vue'
import xuexiziliaoDetail from '../pages/xuexiziliao/detail.vue'
import xuexiziliaoAdd from '../pages/xuexiziliao/add.vue'
import News from '../pages/news/news-list.vue'
import NewsDetail from '../pages/news/news-detail.vue'
import newstypeList from '../pages/newstype/list.vue'
import newstypeDetail from '../pages/newstype/detail.vue'
import newstypeAdd from '../pages/newstype/add.vue'
import Forum from '../pages/forum/list.vue'
import ForumAdd from '../pages/forum/add.vue'
import ForumDetail from '../pages/forum/detail.vue'
import MyForumList from '../pages/forum/myForumList.vue'
import forumtypeList from '../pages/forumtype/list.vue'
import forumtypeDetail from '../pages/forumtype/detail.vue'
import forumtypeAdd from '../pages/forumtype/add.vue'
import chatmessageList from '../pages/chatmessage/list.vue'
import chatmessageDetail from '../pages/chatmessage/detail.vue'
import chatmessageAdd from '../pages/chatmessage/add.vue'
import friendList from '../pages/friend/list.vue'
import friendDetail from '../pages/friend/detail.vue'
import friendAdd from '../pages/friend/add.vue'
import chathelperList from '../pages/chathelper/list.vue'
import chathelperDetail from '../pages/chathelper/detail.vue'
import chathelperAdd from '../pages/chathelper/add.vue'
import sensitivewordsList from '../pages/sensitivewords/list.vue'
import sensitivewordsDetail from '../pages/sensitivewords/detail.vue'
import sensitivewordsAdd from '../pages/sensitivewords/add.vue'
import discussjinengxuqiuList from '../pages/discussjinengxuqiu/list.vue'
import discussjinengxuqiuDetail from '../pages/discussjinengxuqiu/detail.vue'
import discussjinengxuqiuAdd from '../pages/discussjinengxuqiu/add.vue'
import discussxuexiziliaoList from '../pages/discussxuexiziliao/list.vue'
import discussxuexiziliaoDetail from '../pages/discussxuexiziliao/detail.vue'
import discussxuexiziliaoAdd from '../pages/discussxuexiziliao/add.vue'

const routes = [
	{
		path: '/',
		redirect: '/index'
	},
	{
		path: '/index',
		component: Portal
	},
	{
		path: '/main',
		component: Index,
		children: [
			{ path: 'home', component: Home },
			{ path: 'center', component: Center },
			{ path: 'pay', component: payList },
			{ path: 'messages', component: Messages },
			{ path: 'storeup', component: Storeup },
			{ path: 'yonghu', component: yonghuList },
			{ path: 'yonghuDetail', component: yonghuDetail },
			{ path: 'yonghuAdd', component: yonghuAdd },
			{ path: 'huiyuanchongzhi', component: huiyuanchongzhiList },
			{ path: 'huiyuanchongzhiDetail', component: huiyuanchongzhiDetail },
			{ path: 'huiyuanchongzhiAdd', component: huiyuanchongzhiAdd },
			{ path: 'jinengfenlei', component: jinengfenleiList },
			{ path: 'jinengfenleiDetail', component: jinengfenleiDetail },
			{ path: 'jinengfenleiAdd', component: jinengfenleiAdd },
			{ path: 'jinengxuqiu', component: jinengxuqiuList },
			{ path: 'jinengxuqiuDetail', component: jinengxuqiuDetail },
			{ path: 'jinengxuqiuAdd', component: jinengxuqiuAdd },
			{ path: 'skillAdd', component: skillAdd },
			{ path: 'jiaohuanshenqing', component: jiaohuanshenqingList },
			{ path: 'jiaohuanshenqingDetail', component: jiaohuanshenqingDetail },
			{ path: 'jiaohuanshenqingAdd', component: jiaohuanshenqingAdd },
			{ path: 'jiaohuanjilu', component: jiaohuanjiluList },
			{ path: 'jiaohuanjiluDetail', component: jiaohuanjiluDetail },
			{ path: 'jiaohuanjiluAdd', component: jiaohuanjiluAdd },

			{ path: 'ziliaoleixing', component: ziliaoleixingList },
			{ path: 'ziliaoleixingDetail', component: ziliaoleixingDetail },
			{ path: 'ziliaoleixingAdd', component: ziliaoleixingAdd },
			{ path: 'xuexiziliao', component: xuexiziliaoList },
			{ path: 'xuexiziliaoDetail', component: xuexiziliaoDetail },
			{ path: 'xuexiziliaoAdd', component: xuexiziliaoAdd },
			{ path: 'news', component: News },
			{ path: 'newsDetail', component: NewsDetail },
			{ path: 'newstype', component: newstypeList },
			{ path: 'newstypeDetail', component: newstypeDetail },
			{ path: 'newstypeAdd', component: newstypeAdd },
			{ path: 'forum', component: Forum },
			{ path: 'forumAdd', component: ForumAdd },
			{ path: 'forumDetail', component: ForumDetail },
			{ path: 'myForumList', component: MyForumList },
			{ path: 'forumtype', component: forumtypeList },
			{ path: 'forumtypeDetail', component: forumtypeDetail },
			{ path: 'forumtypeAdd', component: forumtypeAdd },
			{ path: 'chatmessage', component: chatmessageList },
			{ path: 'chatmessageDetail', component: chatmessageDetail },
			{ path: 'chatmessageAdd', component: chatmessageAdd },
			{ path: 'friend', component: friendList },
			{ path: 'friendDetail', component: friendDetail },
			{ path: 'friendAdd', component: friendAdd },
			{ path: 'chathelper', component: chathelperList },
			{ path: 'chathelperDetail', component: chathelperDetail },
			{ path: 'chathelperAdd', component: chathelperAdd },
			{ path: 'sensitivewords', component: sensitivewordsList },
			{ path: 'sensitivewordsDetail', component: sensitivewordsDetail },
			{ path: 'sensitivewordsAdd', component: sensitivewordsAdd },
			{ path: 'discussjinengxuqiu', component: discussjinengxuqiuList },
			{ path: 'discussjinengxuqiuDetail', component: discussjinengxuqiuDetail },
			{ path: 'discussjinengxuqiuAdd', component: discussjinengxuqiuAdd },
			{ path: 'discussxuexiziliao', component: discussxuexiziliaoList },
			{ path: 'discussxuexiziliaoDetail', component: discussxuexiziliaoDetail },
			{ path: 'discussxuexiziliaoAdd', component: discussxuexiziliaoAdd },
		]
	},
	{
		path: '/login',
		redirect: '/index'
	},
	{
		path: '/register',
		component: Register
	},
]

const router = new VueRouter({
	mode: 'hash',
	routes,
})

router.beforeEach((to, from, next) => {
	const token = localStorage.getItem('frontToken')
	const openPaths = ['/index', '/register']
	if (openPaths.includes(to.path)) {
		next()
	} else if (!token) {
		Message.warning({ message: '请先登录后再访问', duration: 2500, showClose: true })
		next('/index')
	} else {
		next()
	}
})

export default router
