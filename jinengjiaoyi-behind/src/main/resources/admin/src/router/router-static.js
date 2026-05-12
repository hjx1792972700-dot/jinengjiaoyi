import { createRouter, createWebHashHistory } from 'vue-router'

import Index from '@/views/index.vue'
import Home from '@/views/home.vue'
import NotFound from '@/views/404.vue'
import UpdatePassword from '@/views/update-password.vue'
import pay from '@/views/pay.vue'
import center from '@/views/center.vue'
import yonghu from '@/views/modules/yonghu/list.vue'
import huiyuanchongzhi from '@/views/modules/huiyuanchongzhi/list.vue'
import jinengfenlei from '@/views/modules/jinengfenlei/list.vue'
import jinengxuqiu from '@/views/modules/jinengxuqiu/list.vue'
import jiaohuanshenqing from '@/views/modules/jiaohuanshenqing/list.vue'
import jiaohuanjilu from '@/views/modules/jiaohuanjilu/list.vue'
import pingjiafankui from '@/views/modules/pingjiafankui/list.vue'
import ziliaoleixing from '@/views/modules/ziliaoleixing/list.vue'
import xuexiziliao from '@/views/modules/xuexiziliao/list.vue'
import news from '@/views/modules/news/list.vue'
import newstype from '@/views/modules/newstype/list.vue'
import forum from '@/views/modules/forum/list.vue'
import forumtype from '@/views/modules/forumtype/list.vue'
import chatmessage from '@/views/modules/chatmessage/list.vue'
import friend from '@/views/modules/friend/list.vue'
import chat from '@/views/modules/chat/list.vue'
import chathelper from '@/views/modules/chathelper/list.vue'
import syslog from '@/views/modules/syslog/list.vue'
import statisticsDashboard from '@/views/modules/statistics/index.vue'
import businessManagement from '@/views/modules/business/index.vue'
import systemConfig from '@/views/modules/system/index.vue'
import userManagement from '@/views/modules/usermgmt/index.vue'
import messages from '@/views/modules/messages/list.vue'
import sensitivewords from '@/views/modules/sensitivewords/list.vue'
import users from '@/views/modules/users/list.vue'
import discussjinengxuqiu from '@/views/modules/discussjinengxuqiu/list.vue'
import discussxuexiziliao from '@/views/modules/discussxuexiziliao/list.vue'
import storeup from '@/views/modules/storeup/list.vue'

export const routes = [{
	path: '/',
	name: '系统布局',
	component: Index,
	children: [{
		path: '',
		name: '系统首页',
		component: Home,
		meta: {icon:'', title:'center', affix: true}
	}, {
		path: '/updatePassword',
		name: '修改密码',
		component: UpdatePassword,
		meta: {icon:'', title:'updatePassword'}
	}, {
		path: '/pay',
		name: '支付',
		component: pay,
		meta: {icon:'', title:'pay'}
	}, {
		path: '/center',
		name: '个人信息',
		component: center,
		meta: {icon:'', title:'center'}
	}
	,{ path: '/yonghu', name: '用户', component: yonghu }
	,{ path: '/yonghustat', name: '用户统计', component: yonghu }
	,{ path: '/huiyuanchongzhi', name: '会员充值', component: huiyuanchongzhi }
	,{ path: '/jinengfenlei', name: '技能分类', component: jinengfenlei }
	,{ path: '/jinengxuqiu', name: '技能市场', component: jinengxuqiu }
	,{ path: '/jinengxuqiustat', name: '技能市场统计', component: jinengxuqiu }
	,{ path: '/jiaohuanshenqing', name: '交换申请', component: jiaohuanshenqing }
	,{ path: '/jiaohuanjilu', name: '交换记录', component: jiaohuanjilu }
	,{ path: '/jiaohuanjilustat', name: '交换记录统计', component: jiaohuanjilu }
	,{ path: '/pingjiafankui', name: '评价反馈', component: pingjiafankui }
	,{ path: '/pingjiafankuistat', name: '评价反馈统计', component: pingjiafankui }
	,{ path: '/ziliaoleixing', name: '资料类型', component: ziliaoleixing }
	,{ path: '/xuexiziliao', name: '学习资料', component: xuexiziliao }
	,{ path: '/xuexiziliaostat', name: '学习资料统计', component: xuexiziliao }
	,{ path: '/news', name: '公告资讯', component: news }
	,{ path: '/newstype', name: '资讯分类', component: newstype }
	,{ path: '/forum', name: '技能社区', component: forum }
	,{ path: '/forumtype', name: '论坛类型', component: forumtype }
	,{ path: '/chatmessage', name: '消息表', component: chatmessage }
	,{ path: '/friend', name: '好友表', component: friend }
	,{ path: '/chat', name: 'AI智能问答', component: chat }
	,{ path: '/chathelper', name: '聊天助手表', component: chathelper }
	,{ path: '/statisticsDashboard', name: '数据统计总览', component: statisticsDashboard }
	,{ path: '/businessManagement', name: '业务管理', component: businessManagement }
	,{ path: '/systemConfig', name: '系统配置', component: systemConfig }
	,{ path: '/userManagement', name: '用户管理', component: userManagement }
	,{ path: '/syslog', name: '系统日志', component: syslog }
	,{ path: '/messages', name: '留言反馈', component: messages }
	,{ path: '/sensitivewords', name: '敏感词', component: sensitivewords }
	,{ path: '/users', name: '管理员', component: users }
	,{ path: '/storeup', name: '收藏管理', component: storeup }
	,{ path: '/discussjinengxuqiu', name: '技能市场评论', component: discussjinengxuqiu }
	,{ path: '/discussxuexiziliao', name: '学习资料评论', component: discussxuexiziliao }
	]
},
{
	path: '/:pathMatch(.*)*',
	component: NotFound
}
]

const router = createRouter({
	history: createWebHashHistory(),
	routes,
})

import storage from '@/utils/storage'
import base from '@/utils/base'

router.beforeEach((to, from, next) => {
	if (!storage.get('Token')) {
		window.location.href = base.get().indexUrl
	} else {
		next()
	}
})

export default router
