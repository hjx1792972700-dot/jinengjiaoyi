<template>
	<div class="center-preview">
		<div class="center-particles">
			<span v-for="i in 12" :key="'p'+i" class="c-particle"
				:style="{
					left: (Math.sin(i*1.7)*40+50) + '%',
					top: (Math.cos(i*2.3)*30+50) + '%',
					animationDelay: (i * 0.25) + 's',
					animationDuration: (4 + i % 5) + 's',
					width: (2 + i%3) + 'px',
					height: (2 + i%3) + 'px',
				}"></span>
		</div>

		<div class="center-sidebar">
		<nav class="sidebar-nav">
			<!-- 账户管理 -->
			<div class="nav-section-label">账户管理</div>
			<div class="nav-item" :class="activeName==title2?'is-active':''" @click="handleClick(title2)">
				<el-icon class="nav-icon"><User /></el-icon><span>{{title2}}</span>
			</div>
			<div class="nav-item" :class="activeName=='修改密码'?'is-active':''" @click="handleClick('修改密码')">
				<el-icon class="nav-icon"><Lock /></el-icon><span>修改密码</span>
			</div>
		<template v-for="(item,index) in accountMenuItems">
			<div :key="'acc-'+index" class="nav-item" :class="activeName==item.child[0].menu?'is-active':''" v-if="hasBack(item.menu,item.child[0].tableName)" @mouseenter="centerTabEnter(item.child[0].tableName)" @mouseleave="centerTabEnter('')" @click="menuClick(item.child[0],item.child.length)">
				<el-icon class="nav-icon"><component :is="getMenuIcon(item.child[0].menu)" /></el-icon><span>{{item.child[0].menu}}</span>
			</div>
		</template>

		<!-- 技能交换 -->
		<div class="nav-section-label" v-if="exchangeMenuItems.length">技能交换</div>
		<template v-for="(item,index) in exchangeMenuItems">
			<div class="nav-item" :class="activeName=='我的申请'?'is-active':''" :key="'exc-myapply-'+index" @click="switchTo('我的申请','jiaohuanshenqing')" v-if="item.child[0] && item.child[0].tableName === 'jiaohuanshenqing' && hasBack(item.menu,item.child[0].tableName)">
				<el-icon class="nav-icon"><Promotion /></el-icon><span>我的申请</span>
				<div v-if="myApplyNotifyCount>0" class="nav-badge">{{myApplyNotifyCount}}</div>
			</div>
			<div class="nav-item" :class="activeName=='收到申请'?'is-active':''" :key="'exc-recv-'+index" @click="switchTo('收到申请','receivedApply')" v-if="item.child[0] && item.child[0].tableName === 'jiaohuanshenqing' && hasBack(item.menu,item.child[0].tableName)">
				<el-icon class="nav-icon"><ChatSquare /></el-icon><span>收到申请</span>
				<div v-if="pendingApplyCount>0" class="nav-badge">{{pendingApplyCount}}</div>
			</div>
			<div class="nav-item" :class="activeName==item.child[0].menu?'is-active':''" :key="'exc-'+index" @mouseenter="centerTabEnter(item.child[0].tableName)" @mouseleave="centerTabEnter('')" @click="menuClick(item.child[0],item.child.length)" v-if="item.child[0] && item.child[0].tableName !== 'jiaohuanshenqing' && hasBack(item.menu,item.child[0].tableName)">
				<el-icon class="nav-icon"><component :is="getMenuIcon(item.child[0].menu)" /></el-icon><span>{{item.child[0].menu}}</span>
			</div>
		</template>

			<!-- 消息 -->
			<div class="nav-section-label" v-if="changeHasChat()">消息</div>
			<div class="nav-item" :class="activeName=='聊天记录'?'is-active':''" @click="handleClick('聊天记录')" v-if="changeHasChat()">
				<el-icon class="nav-icon"><ChatDotRound /></el-icon><span>聊天记录</span>
				<div v-if="redNum>0" class="nav-badge">{{redNum}}</div>
			</div>

			<!-- 我的内容 -->
			<div class="nav-section-label">我的内容</div>
		<template v-for="(item,index) in myContentMenuItems">
			<div :key="'my-'+index" class="nav-item" :class="activeName==item.child[0].menu?'is-active':''" v-if="hasBack(item.menu,item.child[0].tableName)" @mouseenter="centerTabEnter(item.child[0].tableName)" @mouseleave="centerTabEnter('')" @click="menuClick(item.child[0],item.child.length)">
				<el-icon class="nav-icon"><component :is="getMenuIcon(item.child[0].menu)" /></el-icon><span>{{item.child[0].menu}}</span>
			</div>
		</template>
			<div class="nav-item" :class="activeName=='我的发布'?'is-active':''" @click="handleClick('我的发布')">
				<el-icon class="nav-icon"><EditPen /></el-icon><span>我的发布</span>
			</div>
		</nav>
		</div>

		<div class="center-main">
		<div class="center-content-box">
			<div class="content-section-header" v-if="activeName">
				<div class="csh-icon">
					<svg viewBox="0 0 24 24" width="18" height="18">
						<path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
					</svg>
				</div>
				<span class="csh-text">{{title || activeName}}</span>
				<div class="csh-line"></div>
			</div>
			<div class="center-content-view" v-show="activeName=='个人中心'">
					<el-form class="center-preview-pv" ref="sessionForm" :model="sessionForm" :rules="rules" label-width="80px">
						<el-form-item class="center-item" v-if="userTableName=='yonghu'" label="用户账号" prop="yonghuzhanghao">
							<el-input v-model="sessionForm.yonghuzhanghao" placeholder="用户账号" :disabled="ro.yonghuzhanghao"></el-input>
						</el-form-item>
						<el-form-item class="center-item" v-if="userTableName=='yonghu'" label="用户姓名" prop="yonghuxingming">
							<el-input v-model="sessionForm.yonghuxingming" placeholder="用户姓名" :disabled="ro.yonghuxingming"></el-input>
						</el-form-item>
						<el-form-item class="center-item" v-if="userTableName=='yonghu'" label="性别" prop="xingbie">
							<el-select filterable v-model="sessionForm.xingbie" placeholder="请选择性别" :disabled="ro.xingbie">
								<el-option v-for="(item, index) in dynamicProp.xingbie" :key="index" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item class="center-item" v-if="userTableName=='yonghu'" label="身份证" prop="idcard">
							<el-input @input="idcardChange" v-model="sessionForm.idcard" placeholder="身份证" :disabled="ro.idcard"></el-input>
						</el-form-item>
						<el-form-item class="center-item" v-if="userTableName=='yonghu'" label="年龄" prop="age">
							<el-input v-model="sessionForm.age" placeholder="年龄" :disabled="ro.age ||true"></el-input>
						</el-form-item>
						<el-form-item class="center-item" v-if="userTableName=='yonghu'" label="信誉指数" prop="xinyuzhishu">
							<el-select filterable v-model="sessionForm.xinyuzhishu" placeholder="请选择信誉指数" :disabled="ro.xinyuzhishu">
								<el-option v-for="(item, index) in dynamicProp.xinyuzhishu" :key="index" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item class="center-item" v-if="userTableName=='yonghu'" label="手机号" prop="shoujihao">
							<el-input v-model="sessionForm.shoujihao" placeholder="手机号" :disabled="ro.shoujihao"></el-input>
						</el-form-item>
						<el-form-item class="center-item center-item-full" v-if="userTableName=='yonghu'" label="头像" prop="touxiang">
							<file-upload
								tip="点击上传头像"
								action="file/upload"
								:limit="1"
								:multiple="true"
								:fileUrls="sessionForm.touxiang?sessionForm.touxiang:''"
								@change="yonghutouxiangHandleAvatarSuccess"
								></file-upload>
						</el-form-item>
						<el-form-item class="center-btn-item">
							<div class="updateBtn" type="primary" @click="onSubmit('sessionForm')">
								<span class="icon iconfont icon-xiugai17"></span>
								<span class="text">保存信息</span>
							</div>
							<div class="closeBtn" type="danger" @click="logout">
								<span class="icon iconfont icon-fanhui12"></span>
								<span class="text">退出登录</span>
							</div>
						</el-form-item>
					</el-form>
				</div>
				<div class="center-content-view" v-show="activeName=='修改密码'">
					<el-form v-if="psdType==1" class="center-preview-pv center-preview-pv-single" ref="passwordForm" :model="passwordForm" :rules="passwordRules" label-width="80px">
						<el-form-item class="center-item" label="原密码" prop="password">
							<el-input type="password" v-model="passwordForm.password" placeholder="原密码"></el-input>
						</el-form-item>
						<el-form-item class="center-item" label="新密码" prop="newpassword">
							<el-input type="password" v-model="passwordForm.newpassword" placeholder="新密码"></el-input>
						</el-form-item>
						<el-form-item class="center-item" label="确认密码" prop="repassword">
							<el-input type="password" v-model="passwordForm.repassword" placeholder="确认密码"></el-input>
						</el-form-item>
						<el-form-item class="center-btn-item">
							<div class="updateBtn" type="primary" @click="updatePassword">
								<span class="icon iconfont icon-xiugai17"></span>
								<span class="text">修改密码</span>
							</div>
						</el-form-item>
					</el-form>
				</div>
			<div class="center-content-view" v-show="activeName=='聊天记录'">
				<!-- 聊天列表 -->
				<div class="z-box" v-if="!chatVisible">
						<div class="section-content" v-for="item in recordList" :key="item.id" @click="chatClick(item)">
							<div class="chat-left">
								<div class="chat-avatar-wrap">
									<img :src="item.picture?baseUrl + item.picture:avatarDefault">
									<div class="chat-noread-dot" v-if="item.notreadnum > 0">{{item.notreadnum}}</div>
								</div>
								<div class="chat-info">
									<div class="chat-name">{{item.name}}</div>
									<div class="chat-text">
										{{item.content ? (item.content.split('/').length>1&&item.content.split('/')[0]=='upload'?'[多媒体]':item.content) : ''}}
									</div>
								</div>
							</div>
							<div class="chat-btn">
								<div class="friendBtn-unread" v-if="!item.notreadnum" @click.stop="markAsUnread(item)">标记未读</div>
								<div class="friendBtn-read" v-else @click.stop="markAsRead(item)">标记已读</div>
								<div class="friendBtn2" @click.stop="chatDel(item)">删除</div>
							</div>
						</div>
						<div class="noList" v-if="!recordList.length">暂无聊天记录</div>
					</div>
				<!-- 聊天对话 (内联视图) -->
				<div class="inline-chat-view" v-if="chatVisible">
					<div class="inline-chat-header">
						<div class="chat-back-btn" @click="clearChat"><i class="el-icon-arrow-left"></i> 返回</div>
						<div class="chat-partner-info">
							<img :src="nowfpic?baseUrl + nowfpic:avatarDefault" class="chat-partner-avatar">
							<span class="chat-partner-name">{{nowname}}</span>
						</div>
					</div>
					<div class="inline-chat-messages" id="chat-content" ref="chatContent">
						<div v-for="item in chatList" :key="item.id">
							<div v-if="item.addtime" class="addtime">{{timeFormat(item.addtime)}}</div>
							<div v-if="item.uid==sessionForm.id" class="right-content">
								<el-alert v-if="item.format==1" class="text-content" :title="item.content" :closable="false" type="warning"></el-alert>
								<el-image v-else-if="item.format==2" fit="cover" :src="item.content?baseUrl + item.content:''" style="width: 100px;height: 100px;border-radius:8px;" :preview-src-list="[item.content?baseUrl + item.content:'']"></el-image>
								<video v-else-if="item.format==3" :src="baseUrl + item.content" style="width: 280px;border-radius:8px;" controls></video>
								<el-button v-else-if="item.format==4" type="primary" size="mini" @click="chatDownload(item.content)">文件预览</el-button>
								<audio v-else-if="item.format==5" :src="baseUrl + item.content" controls></audio>
								<img :src="mypic?baseUrl + mypic:avatarDefault" class="msg-avatar" />
							</div>
							<div v-else class="left-content">
								<img :src="nowfpic?baseUrl + nowfpic:avatarDefault" class="msg-avatar" />
								<el-alert v-if="item.format==1" class="text-content" :title="item.content" :closable="false" type="success"></el-alert>
								<el-image v-else-if="item.format==2" fit="cover" :src="item.content?baseUrl + item.content:''" style="width: 100px;height: 100px;border-radius:8px;" :preview-src-list="[item.content?baseUrl + item.content:'']"></el-image>
								<video v-else-if="item.format==3" :src="baseUrl + item.content" style="width: 280px;border-radius:8px;" controls></video>
								<el-button v-else-if="item.format==4" type="primary" size="mini" @click="chatDownload(item.content)">文件预览</el-button>
								<audio v-else-if="item.format==5" :src="baseUrl + item.content" controls></audio>
							</div>
							<div class="clear-float"></div>
						</div>
					</div>
					<div class="inline-chat-input">
						<div class="chat-input-row">
							<el-input size="small" @keydown.enter="addChat(null)" v-model="chatForm.content" placeholder="请输入内容">
							</el-input>
							<el-button size="small" :disabled="!chatForm.content" type="primary" @click="addChat(null)">发送</el-button>
						</div>
						<div class="chat-input-actions">
							<el-button type="warning" size="mini" :icon="isRecording ? VideoPause : Microphone" @click="toggleRecord" :class="{'voice-record-btn': true, 'voice-recording': isRecording}">
								{{ isRecording ? `录音中 ${recordDuration}秒` : '录音' }}
							</el-button>
							<el-upload accept=".jpg,.png" style="display:inline-block;" class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess" :show-file-list="false">
								<el-button size="mini" type="success" icon="el-icon-picture">图片</el-button>
							</el-upload>
							<el-upload class="upload-demo" style="display:inline-block;" :action="uploadUrl" :on-success="uploadSuccess2" accept=".mp4" :show-file-list="false">
								<el-button size="mini" type="success" icon="el-icon-video-camera">视频</el-button>
							</el-upload>
							<el-upload class="upload-demo" style="display:inline-block;" :action="uploadUrl" :on-success="uploadSuccess3" :show-file-list="false">
								<el-button size="mini" type="success" icon="el-icon-folder-opened">文件</el-button>
							</el-upload>
						</div>
					</div>
				</div>
				</div>
				<div class="center-content-view" v-if="activeMenuTable=='huiyuanchongzhi'">
					<huiyuanchongzhi-list :embedded="true"></huiyuanchongzhi-list>
				</div>
				<div class="center-content-view" v-if="activeMenuTable=='jinengxuqiu'">
					<my-need-list :embedded="true"></my-need-list>
				</div>
			<div class="center-content-view" v-if="activeMenuTable=='jiaohuanshenqing'">
				<jiaohuanshenqing-list :embedded="true"></jiaohuanshenqing-list>
			</div>
			<div class="center-content-view" v-if="activeMenuTable=='receivedApply'">
				<received-apply-list :embedded="true" @count-changed="onApplyCountChanged"></received-apply-list>
			</div>
				<div class="center-content-view" v-if="activeMenuTable=='jiaohuanjilu'">
					<jiaohuanjilu-list :embedded="true"></jiaohuanjilu-list>
				</div>
				<div class="center-content-view" v-if="activeMenuTable=='pingjiafankui'">
					<pingjiafankui-list :embedded="true"></pingjiafankui-list>
				</div>
				<div class="center-content-view" v-if="activeMenuTable=='storeup'">
					<storeup-list :embedded="true" :key="'storeup-'+storeupTypeKey"></storeup-list>
				</div>
				<div class="center-content-view" v-if="activeMenuTable=='myForumList'">
					<div class="publish-tabs">
						<div class="pub-tab" :class="{active: publishTab==='skill'}" @click="publishTab='skill'">
							<i class="el-icon-histogram"></i> 我的技能
						</div>
						<div class="pub-tab" :class="{active: publishTab==='forum'}" @click="publishTab='forum'">
							<i class="el-icon-edit"></i> 我的帖子
						</div>
						<div class="pub-actions">
							<el-button v-if="publishTab==='skill'" size="small" type="primary" class="pub-add-btn" @click="$router.push('/main/skillAdd')">
								<i class="el-icon-plus"></i> 发布技能
							</el-button>
						</div>
					</div>
					<my-forum-list v-show="publishTab==='forum'" :embedded="true"></my-forum-list>
					<my-skill-list v-show="publishTab==='skill'" :embedded="true"></my-skill-list>
				</div>
			</div>
		</div>

	</div>
</template>

<script>
	import config from '@/config/config';
	import menu from '@/config/menu';
	import timeMethod from '@/common/timeMethod'
	import avatarDefault from '@/assets/avator.png'
	import {
		WebsocketMixin
	} from '@/mixins/WebsocketMixin'
	import HuiyuanchongzhiList from '../huiyuanchongzhi/list.vue'
	import MyNeedList from '../jinengxuqiu/myNeedList.vue'
	import JiaohuanshenqingList from '../jiaohuanshenqing/list.vue'
	import ReceivedApplyList from '../jiaohuanshenqing/receivedApply.vue'
	import JiaohuanjiluList from '../jiaohuanjilu/list.vue'
	import PingjiafankuiList from '../pingjiafankui/list.vue'
	import StoreupList from '../storeup/list.vue'
	import MyForumList from '../forum/myForumList.vue'
	import MySkillList from '../jinengxuqiu/mySkillList.vue'
	export default {
		mixins: [WebsocketMixin],
		components: {
			HuiyuanchongzhiList,
			MyNeedList,
			JiaohuanshenqingList,
		ReceivedApplyList,
			JiaohuanjiluList,
			PingjiafankuiList,
			StoreupList,
			MyForumList,
			MySkillList,
		},
		//数据集合
		data() {
			return {
				title: '个人中心',
				title2: '个人中心',
				showActive: '',
				activeName: '个人中心',
				baseUrl: config.baseUrl,
				sessionForm: {},
				ro: {},
				passwordForm: {},
				psdType: '1',
				passwordRules: {
					password: [
						{
							required: true,
							message: "密码不能为空",
							trigger: "blur"
						}
					],
					newpassword: [
						{
							required: true,
							message: "新密码不能为空",
							trigger: "blur"
						}
					],
					repassword: [
						{
							required: true,
							message: "确认密码不能为空",
							trigger: "blur"
						}
					]
				},
				rules: {},
				menuList: [],
				disabled: false,
				uploadUrl: config.baseUrl + 'file/upload',
				imageUrl: '',
				headers: {Token: localStorage.getItem('frontToken')},
				userTableName: localStorage.getItem('UserTableName'),
				dynamicProp: {},
				redNum: 0,
			pendingApplyCount: 0,
				recordList: [],
				chatVisible: false,
				nowfid: 0,
				nowfpic:'',
				nowname: '',
				mypic: localStorage.getItem('frontHeadportrait'),
				// 语音聊天相关
				isRecording: false,
				mediaRecorder: null,
				audioStream: null,
				audioChunks: [],
				recordDuration: 0,
				recordTimer: null,
				chatList: [],
				chatForm: {
					content: ''
				},
	hasChatList: [
			'yonghu',
		],
		sensitiveWordsArr: [],
		activeMenuTable: '',
		publishTab: 'skill',
				storeupTypeKey: 0,
				myApplyNotifyCount: 0,
				avatarDefault,
			}
		},
	computed: {
		accountMenuItems() {
			return this.menuList.filter(item =>
				item.child[0] && item.child[0].tableName === 'huiyuanchongzhi'
			)
		},
		exchangeMenuItems() {
			return this.menuList.filter(item => {
				if (!item.child[0]) return false
				const tn = item.child[0].tableName
				return ['jinengxuqiu', 'jiaohuanshenqing', 'jiaohuanjilu', 'pingjiafankui'].includes(tn)
			})
		},
		myContentMenuItems() {
			return this.menuList.filter(item =>
				item.child[0] && item.child[0].tableName === 'storeup'
			)
		},
	},
		async created() {
			let menus = menu.list()
			for(let x in menus){
				if(menus[x].tableName == this.userTableName){
					for(let i in menus[x].backMenu){
						if(menus[x].backMenu[i].child&&menus[x].backMenu[i].child.length&&menus[x].backMenu[i].child[0].tableName.indexOf('exam')!=-1){
							menus[x].backMenu.splice(i,1)
						}
					}
					this.menuList = menus[x].backMenu
				}
			}
			if ('yonghu' == this.userTableName) {
				this.sessionForm.yonghuzhanghao = null
				this.sessionForm.yonghuxingming = null
				this.sessionForm.mima = null
				this.sessionForm.xingbie = null
				this.sessionForm.idcard = null
				this.sessionForm.age = null
				this.sessionForm.xinyuzhishu = null
				this.sessionForm.shoujihao = null
				this.sessionForm.touxiang = null
				this.sessionForm.sfsh = null
				this.sessionForm.shhf = null
			}

			if ('yonghu' == this.userTableName) {
				if (this.rules['yonghuzhanghao']){
					this.rules['yonghuzhanghao'].push({ required: true, message: '请输入用户账号', trigger: 'blur' })
				}else{
					this.rules['yonghuzhanghao'] = [{ required: true, message: '请输入用户账号', trigger: 'blur' }]
				}
				if (this.rules['yonghuxingming']){
					this.rules['yonghuxingming'].push({ required: true, message: '请输入用户姓名', trigger: 'blur' })
				}else{
					this.rules['yonghuxingming'] = [{ required: true, message: '请输入用户姓名', trigger: 'blur' }]
				}
				if (this.rules['mima']){
					this.rules['mima'].push({ required: true, message: '请输入密码', trigger: 'blur' })
				}else{
					this.rules['mima'] = [{ required: true, message: '请输入密码', trigger: 'blur' }]
				}
				this.rules['idcard'] = [{ required: false, validator: this.$validate.isIdCard, trigger: 'blur' }]
				this.rules['age'] = [{ required: false, validator: this.$validate.isIntNumer, trigger: 'blur' }]
				this.ro = {
					yonghuzhanghao: true,
					yonghuxingming: false,
					mima: false,
					xingbie: false,
					idcard: false,
					age: false,
					xinyuzhishu: true,
					shoujihao: false,
					touxiang: false,
					sfsh: false,
					shhf: false,
				}
			}

		this.init();
		await this.$http.get(`${localStorage.getItem('UserTableName')}/session`).then(async res => {
			if (res.data.code == 0) {
				this.sessionForm = res.data.data
			}
		});
	if(this.changeHasChat()) {
		this.getRecordList2()
	}
	this.loadPendingApplyCount()
	this.loadMyApplyNotifyCount()
	this.$nextTick(() => { this.cleanupStaleOverlays() })

		if (this.$route.query.tab) {
			this.$nextTick(() => {
				const tabName = this.$route.query.tab;
			const menuNameMap = {
				huiyuanchongzhi: '会员充值',
				jinengxuqiu: '我的需求',
				jiaohuanjilu: '交换记录',
				pingjiafankui: '评价反馈',
				xuexiziliao: '学习资料',
				storeup: '收藏',
			};
			if (tabName === 'password') {
				this.activeName = '修改密码';
				this.activeMenuTable = '';
				this.title = '修改密码';
				this.passwordForm = { password: '', newpassword: '', repassword: '' };
				this.psdType = '1';
			} else if (tabName === 'chat') {
				this.activeName = '聊天记录';
				this.activeMenuTable = '';
				this.title = '聊天记录';
				this.getRecordList();
			} else if (tabName === 'myPublish') {
				this.activeName = '我的发布';
				this.activeMenuTable = 'myForumList';
				this.title = '我的发布';
				if (this.$route.query.publishTab) {
					this.publishTab = this.$route.query.publishTab;
				}
			} else if (tabName === 'jiaohuanshenqing') {
				this.activeName = '我的申请';
				this.activeMenuTable = 'jiaohuanshenqing';
				this.title = '我的申请';
			} else if (tabName === 'receivedApply') {
				this.activeName = '收到申请';
				this.activeMenuTable = 'receivedApply';
				this.title = '收到申请';
			} else if (menuNameMap[tabName]) {
				this.activeName = menuNameMap[tabName];
				this.activeMenuTable = tabName;
				this.title = menuNameMap[tabName];
			}
			});
		}
	},
	mounted() {
		window.scrollTo(0, 0);
		const footer = document.querySelector('.bottom-preview');
		if (footer) footer.style.display = 'none';
		const body = document.querySelector('.body-containers');
		if (body) {
			body.scrollTop = 0;
			body.style.overflow = 'hidden';
			body.style.height = '100vh';
		}
	},
	beforeDestroy() {
		const footer = document.querySelector('.bottom-preview');
		if (footer) footer.style.display = '';
		const body = document.querySelector('.body-containers');
		if (body) {
			body.style.overflow = '';
			body.style.height = '';
		}
	},
		//方法集合
		methods: {
		init() {
			if ('yonghu' == this.userTableName) {
				this.dynamicProp.xingbie = '男,女'.split(',');
				this.dynamicProp.xinyuzhishu = '⭐,⭐⭐,⭐⭐⭐,⭐⭐⭐⭐,⭐⭐⭐⭐⭐'.split(',');
			}
			this.getSensitiveWords()
		},
		cleanupStaleOverlays() {
			document.querySelectorAll('.v-modal').forEach(el => {
				if (el && el.parentNode) el.parentNode.removeChild(el)
			})
			document.querySelectorAll('.el-message-box__wrapper').forEach(el => {
				if (el && el.parentNode) el.parentNode.removeChild(el)
			})
			document.body.classList.remove('el-popup-parent--hidden')
		},
		loadPendingApplyCount() {
			this.$http.get('jiaohuanshenqing/receivedApply', {
				params: { page: 1, limit: 1, sfsh: '待审核' }
			}).then(res => {
				if (res.data && res.data.code === 0) this.pendingApplyCount = res.data.data.total || 0
			}).catch(() => {})
		},
		onApplyCountChanged() {
			this.loadPendingApplyCount();
			this.loadMyApplyNotifyCount();
		},
		loadMyApplyNotifyCount() {
			let count = 0;
			let done = 0;
			const check = () => { done++; if (done >= 2) this.myApplyNotifyCount = count; };
			this.$http.get('jiaohuanshenqing/myApply', {
				params: { page: 1, limit: 1, sfsh: '待确认' }
			}).then(res => {
				if (res.data && res.data.code === 0) count += res.data.data.total || 0;
				check();
			}).catch(() => check());
			this.$http.get('jiaohuanshenqing/myApply', {
				params: { page: 1, limit: 1, sfsh: '否' }
			}).then(res => {
				if (res.data && res.data.code === 0) count += res.data.data.total || 0;
				check();
			}).catch(() => check());
		},
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			setSession(){
				localStorage.setItem('sessionForm',JSON.stringify(this.sessionForm))
			},
			onSubmit(formName) {
			if(`yonghu` == this.userTableName && this.sessionForm.touxiang!=null){
				this.sessionForm.touxiang = this.sessionForm.touxiang.replace(/^\/+/, '');
			}
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.$http.post(this.userTableName + '/update', this.sessionForm).then(res => {
							if (res.data.code == 0) {
								this.setSession()
								this.$message({
									message: '更新成功',
									type: 'success',
									duration: 1500
								});
							}
						});
					} else {
						return false;
					}
				});
			},
			yonghutouxiangHandleAvatarSuccess(fileUrls) {
				this.sessionForm.touxiang = fileUrls;
			},
			handleClick(name) {
				this.cleanupStaleOverlays();
				this.activeMenuTable = '';
				switch(name) {
					case '个人中心':
						this.activeName = name
						this.updateTabQuery(null)
						break;
					case '修改密码':
						this.activeName = name
						this.passwordForm = {
							password: '',
							newpassword: '',
							repassword: '',
						}
						this.psdType = '1'
						this.$forceUpdate()
						this.updateTabQuery('password')
						break;
					case '聊天记录':
						this.activeName = name
						this.getRecordList()
						this.updateTabQuery('chat')
						break;
					case '我的发布':
						this.activeName = name
						this.activeMenuTable = 'myForumList'
						this.updateTabQuery('myPublish')
						break;
				}

				this.title = name;
			},
			changeHasChat(){
				let table = localStorage.getItem('frontSessionTable')
				for(let x in this.hasChatList){
					if(this.hasChatList[x]==table){
						return true
					}
				}
				return false
			},
			refreshChatData() {
				this.$http.get('friend/page2', {
					params: {
						uid: Number(localStorage.getItem('frontUserid')),
						type: 2
					}
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.recordList = res.data.data.list
						let num = 0
						for(let x in res.data.data.list) {
							num += Number(res.data.data.list[x].notreadnum)
						}
						this.redNum = num
					}
				})
			},
			getRecordList() {
				this.refreshChatData()
			},
			getRecordList2() {
				this.refreshChatData()
			},
			chatClick(row) {
				this.nowfid = row.fid
				this.nowfpic = row.picture
				this.nowname = row.name
				this.chatVisible = true
				this.getChatList()
				try { this.initWebSocket(this.nowfid) } catch(e) {}
			},
			markAsUnread(item) {
				this.$http.post('chatmessage/markUnread', {
					uid: item.fid,
					fid: Number(localStorage.getItem('frontUserid'))
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.refreshChatData()
					}
				})
			},
			markAsRead(item) {
				this.$http.post('chatmessage/markRead', {
					uid: item.fid,
					fid: Number(localStorage.getItem('frontUserid'))
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.refreshChatData()
					}
				})
			},
			chatDel(row){
				this.$confirm('是否删除与 ' + row.name + ' 的聊天记录？', '确认删除', { type: 'warning' }).then(_ => {
					let uid = Number(localStorage.getItem('frontUserid'))
					let arr = []
					let brr = []
					Promise.all([
						this.$http.get('chatmessage/list', { params: { page: 1, limit: 10000, uid: uid, fid: row.fid } }),
						this.$http.get('chatmessage/list', { params: { page: 1, limit: 10000, uid: row.fid, fid: uid } }),
					]).then(([rs1, rs2]) => {
						if(rs1.data.data.list.length) arr.push(...rs1.data.data.list.map(i => i.id))
						if(rs2.data.data.list.length) arr.push(...rs2.data.data.list.map(i => i.id))
						let deleteChat = arr.length ? this.$http.post('chatmessage/delete', arr) : Promise.resolve()
						return deleteChat
					}).then(() => {
						return Promise.all([
							this.$http.get('friend/list', { params: { page: 1, limit: 10000, uid: row.fid, fid: uid, type: 0 } }),
							this.$http.get('friend/list', { params: { page: 1, limit: 10000, fid: row.fid, uid: uid, type: 0 } }),
							this.$http.get('friend/list', { params: { page: 1, limit: 10000, fid: row.fid, uid: uid, type: 2 } }),
							this.$http.get('friend/list', { params: { page: 1, limit: 10000, uid: row.fid, fid: uid, type: 2 } }),
						])
					}).then(results => {
						results.forEach(rs => {
							if(rs.data.data.list.length) brr.push(...rs.data.data.list.map(i => i.id))
						})
						if(brr.length) return this.$http.post('friend/delete', brr)
					}).then(() => {
						this.$message.success('删除成功')
						this.refreshChatData()
					})
				}).catch(_ => {});
			},
			websocketOnopen: function() {
				
			},
			websocketOnmessage:function(e) {
				this.getChatList()
			},
			getChatList() {
				this.$http.get('chatmessage/mlist', {
					params: {
						page: 1,
						limit: 1000,
						uid: Number(localStorage.getItem('frontUserid')),
						fid: this.nowfid
					}
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.chatList = this.formatMessages(res.data.data.list)
						this.$nextTick(() => {
							let div = this.$refs.chatContent || document.getElementById('chat-content')
							if (div) div.scrollTop = div.scrollHeight
						})
					}
				})
			},
			formatMessages(messages) {
				let lastTime = null;
				messages.forEach((message, index) => {
					const currentTime = new Date(message.addtime).getTime();
					if (lastTime !== null) {
						const timeDiff = (currentTime - lastTime) / 1000 / 60; // 转换为分钟
						if (timeDiff < 3) {
							message.addtime = ''; // 如果小于3分钟，不显示时间
						}
					}
					lastTime = currentTime;
				});
				return messages;
			},
			timeFormat(time) {
				const Time = timeMethod.getTime(time).split("T");
				//当前消息日期属于周
				const week = timeMethod.getDateToWeek(time);
				//当前日期0时
				const nti = timeMethod.setTimeZero(timeMethod.getNowTime());
				//消息日期当天0时
				const mnti = timeMethod.setTimeZero(timeMethod.getTime(time));
				//计算日期差值
				const diffDate = timeMethod.calculateTime(nti, mnti);
				//本周一日期0时 （后面+1是去除当天时间）
				const fwnti = timeMethod.setTimeZero(timeMethod.countDateStr(-timeMethod.getDateToWeek(timeMethod
					.getNowTime()).weekID + 1));
				//计算周日期差值
				const diffWeek = timeMethod.calculateTime(mnti, fwnti);

				if (diffDate === 0) { //消息发送日期减去当天日期如果等于0则是当天时间
					return Time[1].slice(0, 5);
				} else if (diffDate < 172800000) { //当前日期减去消息发送日期小于2天（172800000ms）则是昨天-  一天最大差值前天凌晨00:00:00到今天晚上23:59:59
					return "昨天 " + Time[1].slice(0, 5);
				} else if (diffWeek >= 0) { //消息日期减去本周一日期大于0则是本周
					return week.weekName;
				} else { //其他时间则是日期
					return Time[0].slice(5, 10);
				}
			},
			clearChat(){
				if(this.websock) {
					try { this.websocketOnclose(); } catch(e) {}
				}
				this.chatVisible = false
				this.chatList = []
				this.refreshChatData()
			},
			// 切换录音状态（点击开始/停止）
			toggleRecord() {
				if (this.isRecording) {
					// 如果正在录音，则停止
					this.stopRecord()
				} else {
					// 如果未录音，则开始
					this.startRecord()
				}
			},
			// 开始录音
			async startRecord() {
				if (this.isRecording) return
				
				try {
					// 请求麦克风权限
					this.audioStream = await navigator.mediaDevices.getUserMedia({ audio: true })
					
					// 检查浏览器是否支持 MediaRecorder
					if (!window.MediaRecorder) {
						this.$message.error('您的浏览器不支持录音功能')
						return
					}
					
					// 获取支持的 MIME 类型
					let mimeType = 'audio/webm'
					if (!MediaRecorder.isTypeSupported('audio/webm')) {
						if (MediaRecorder.isTypeSupported('audio/mp4')) {
							mimeType = 'audio/mp4'
						} else if (MediaRecorder.isTypeSupported('audio/ogg')) {
							mimeType = 'audio/ogg'
						} else {
							mimeType = '' // 使用浏览器默认格式
						}
					}
					
					// 初始化录音器
					const options = mimeType ? { mimeType: mimeType } : {}
					this.mediaRecorder = new MediaRecorder(this.audioStream, options)
					this.audioChunks = []
					
					// 监听数据可用事件（每100ms收集一次数据）
					this.mediaRecorder.ondataavailable = (event) => {
						if (event.data && event.data.size > 0) {
							this.audioChunks.push(event.data)
						}
					}
					
					// 监听录音停止事件
					this.mediaRecorder.onstop = () => {
						// 停止所有音频轨道
						if (this.audioStream) {
							this.audioStream.getTracks().forEach(track => track.stop())
							this.audioStream = null
						}
					}
					
					// 监听错误事件
					this.mediaRecorder.onerror = (event) => {
						console.error('录音错误:', event.error)
						this.$message.error('录音过程中发生错误')
						this.stopRecord()
					}
					
					// 开始录音，每100ms收集一次数据
					this.mediaRecorder.start(100)
					this.isRecording = true
					this.recordDuration = 0
					
					// 开始计时
					this.recordTimer = setInterval(() => {
						this.recordDuration++
						// 最长录音60秒
						if (this.recordDuration >= 60) {
							this.stopRecord()
							this.$message.info('录音已达到最大时长，已自动停止')
						}
					}, 1000)
					
				} catch (error) {
					console.error('无法访问麦克风:', error)
					this.isRecording = false
					if (error.name === 'NotAllowedError' || error.name === 'PermissionDeniedError') {
						this.$message.error('麦克风权限被拒绝，请在浏览器设置中允许访问麦克风')
					} else if (error.name === 'NotFoundError' || error.name === 'DevicesNotFoundError') {
						this.$message.error('未检测到麦克风设备')
					} else {
						this.$message.error('无法访问麦克风，请检查权限设置')
					}
				}
			},
			// 停止录音
			stopRecord() {
				if (!this.isRecording) return
				
				// 清除计时器
				if (this.recordTimer) {
					clearInterval(this.recordTimer)
					this.recordTimer = null
				}
				
				// 停止录音
				if (this.mediaRecorder && this.mediaRecorder.state !== 'inactive') {
					try {
						this.mediaRecorder.stop()
					} catch (e) {
						console.error('停止录音失败:', e)
					}
				}
				
				this.isRecording = false
				
				// 如果录音时长太短，不发送
				if (this.recordDuration < 1) {
					this.$message.warning('录音时间太短，请至少录音1秒')
					this.recordDuration = 0
					this.audioChunks = []
					return
				}
				
				// 等待录音数据收集完成
				setTimeout(() => {
					this.uploadAudio()
				}, 200)
			},
			// 上传录音
			uploadAudio() {
				if (this.audioChunks.length === 0) {
					this.$message.error('录音数据为空')
					return
				}
				
				// 确定文件扩展名
				let fileExtension = 'webm'
				let mimeType = 'audio/webm'
				if (this.mediaRecorder && this.mediaRecorder.mimeType) {
					mimeType = this.mediaRecorder.mimeType
					if (mimeType.includes('mp4')) {
						fileExtension = 'mp4'
					} else if (mimeType.includes('ogg')) {
						fileExtension = 'ogg'
					} else if (mimeType.includes('wav')) {
						fileExtension = 'wav'
					}
				}
				
				// 合并音频数据
				const audioBlob = new Blob(this.audioChunks, { type: mimeType })
				
				// 检查文件大小（限制为10MB）
				if (audioBlob.size > 10 * 1024 * 1024) {
					this.$message.error('录音文件过大，请缩短录音时间')
					this.audioChunks = []
					this.recordDuration = 0
					return
				}
				
				// 创建FormData上传
				const formData = new FormData()
				formData.append('file', audioBlob, `voice_${Date.now()}.${fileExtension}`)
				
				// 显示上传提示
				const loading = this.$loading({
					lock: true,
					text: '正在上传语音...',
					background: 'rgba(0, 0, 0, 0.7)'
				})
				
				// 上传文件
				this.$http.post(this.uploadUrl, formData, {
					headers: {
						'Content-Type': 'multipart/form-data'
					}
				}).then(res => {
					loading.close()
					if (res.data && res.data.code == 0) {
						// 上传成功，发送语音消息
						this.addChat('upload/' + res.data.file, 5)
						this.$message.success('语音发送成功')
					} else {
						this.$message.error(res.data.msg || '上传失败')
					}
				}).catch(error => {
					loading.close()
					console.error('上传失败:', error)
					this.$message.error('语音上传失败，请重试')
				}).finally(() => {
					// 重置状态
					this.audioChunks = []
					this.recordDuration = 0
					this.mediaRecorder = null
				})
			},
			uploadSuccess(res) {
				if (res.code == 0) {
					this.addChat('upload/' + res.file,2);
				}
			},
			uploadSuccess2(res) {
				if (res.code == 0) {
					this.addChat('upload/' + res.file,3);
				}
			},
			uploadSuccess3(res) {
				if (res.code == 0) {
					this.addChat('upload/' + res.file,4);
				}
			},
			addChat(ask=null,type=1) {
				if(!ask){
					for(var i=0; i<this.sensitiveWordsArr.length; i++){
						//全局替换
						var reg = new RegExp(this.sensitiveWordsArr[i],"g");
						//判断内容中是否包括敏感词
						if (this.chatForm.content.indexOf(this.sensitiveWordsArr[i]) > -1) {
							// 将敏感词替换为 **
							this.chatForm.content = this.chatForm.content.replace(reg,"**");
						}
					}
				}
				this.$http.post('chatmessage/add', {
					uid: Number(localStorage.getItem('frontUserid')),
					fid: this.nowfid,
					content: ask?ask:this.chatForm.content,
					format: type
				}).then(res2 => {
					this.websocketSend(ask?ask:this.chatForm.content)
					this.chatForm = {
						content: ''
					}
					this.getChatList()
				})
			},
			chatDownload(url){
				if(!url){
					return false
				}
				window.open((location.href.split(this.$config.name).length>1 ? location.href.split(this.$config.name)[0] + this.$config.name + '/' + url :this.$config.baseUrl + url))
			},
			idcardChange(e) {
				if(this.$validate.isIdCard2(e)) {
					this.sessionForm.age = this.getAge(e)
				}else {
					this.sessionForm.age = ''
				}
			},
			async updatePassword(){
				this.$refs["passwordForm"].validate(async valid => {
					if (valid) {
						var password = "";
						if (this.sessionForm.mima) {
							password = this.sessionForm.mima;
						} else if (this.sessionForm.password) {
							password = this.sessionForm.password;
						}
						if (this.userTableName == 'yonghu') {
						}
						if (this.passwordForm.password != password) {
							this.$message.error("原密码错误");
							return;
						}
						if (this.passwordForm.newpassword != this.passwordForm.repassword) {
							this.$message.error("两次密码输入不一致");
							return;
						}
						if (this.passwordForm.newpassword == this.passwordForm.password) {
							this.$message.error("新密码与原密码相同！");
							return;
						}
						this.sessionForm.password = this.passwordForm.newpassword;
						this.sessionForm.mima = this.passwordForm.newpassword;
						this.$http.post(`${this.userTableName}/update`,this.sessionForm).then(({data})=>{
							if (data && data.code === 0) {
								this.$message({
									message: "修改密码成功,下次登录系统生效",
									type: "success",
									duration: 1500,
									onClose: () => {
									}
								});
								this.setSession()
							} else {
								this.$message.error(data.msg);
							}
						});
					}
				})
			},
			logout() {
				localStorage.clear();
				this.$router.push('/main/home');
				this.activeIndex = '0'
				localStorage.setItem('keyPath', this.activeIndex)
				this.$forceUpdate()
				this.$message({
					message: '登出成功',
					type: 'success',
					duration: 1500,
				});
			},
			hasBack(name,tablename){
				if(name.indexOf('章节')!=-1&&tablename.substring(0,7)=='chapter') {
					return false
				}
				return true
			},
			menuClick(row,length=1) {
				this.cleanupStaleOverlays();
				if(length==1) {
					this.activeName = row.menu;
					if(row.tableName=='storeup') {
						localStorage.setItem('storeupType', row.menuJump);
						this.storeupTypeKey++;
						this.activeMenuTable = 'storeup';
					} else {
						this.activeMenuTable = row.tableName;
					}
					this.title = row.menu;
					this.updateTabQuery(row.tableName);
				}
			},
			switchTo(name, table) {
				this.cleanupStaleOverlays();
				this.activeName = name;
				this.activeMenuTable = table;
				this.title = name;
				this.updateTabQuery(table);
			},
			updateTabQuery(tab) {
				let query = {};
				if (tab) query.tab = tab;
				let current = this.$route.query.tab || null;
				if (current !== tab) {
					this.$router.replace({ path: '/main/center', query }).catch(() => {});
				}
			},
		centerTabEnter(name){
			this.showActive = name?('show' + name):''
		},
		getMenuIcon(menuName) {
			const iconMap = {
				'会员充值': 'Wallet',
				'我的需求': 'Histogram',
				'交换申请': 'CircleCheck',
				'交换记录': 'List',
				'评价反馈': 'Star',
				'我的评论': 'ChatLineRound',
				'我的点赞': 'Pointer',
				'我的收藏': 'Collection',
			}
			return iconMap[menuName] || 'Grid'
		},
	}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$cyan: #00d4ff;
$purple: #7c3aed;
$cyan-dim: rgba(0,212,255,0.15);
$purple-dim: rgba(124,58,237,0.12);
$bg-deep: #0c1222;
$bg-card: rgba(15,23,42,0.85);
$bg-glass: rgba(30,41,59,0.6);
$border-glow: rgba(0,212,255,0.12);
$text-bright: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: rgba(255,255,255,0.35);
$radius: 16px;
$sidebar-w: 220px;

.center-preview {
	padding: 16px 3% 0;
	margin: 0 auto;
	background: linear-gradient(180deg, $bg-deep 0%, #101828 100%);
	display: flex;
	width: 100%;
	gap: 16px;
	align-items: stretch;
	position: relative;
	height: calc(100vh - 102px);
	overflow: hidden;
	box-sizing: border-box;
}

.center-particles {
	position: absolute;
	inset: 0;
	pointer-events: none;
	overflow: hidden;
	z-index: 0;
}
.c-particle {
	position: absolute;
	border-radius: 50%;
	background: $cyan;
	opacity: 0;
	animation: particleFloat 5s ease-in-out infinite;
	box-shadow: 0 0 6px rgba(0,212,255,0.4);
}
@keyframes particleFloat {
	0%, 100% { opacity: 0; transform: translateY(0) scale(0.8); }
	50% { opacity: 0.6; transform: translateY(-30px) scale(1.2); }
}

.center-sidebar {
	width: $sidebar-w;
	min-width: $sidebar-w;
	z-index: 2;
	border-radius: $radius;
	background: $bg-card;
	border: 1px solid $border-glow;
	display: flex;
	flex-direction: column;
	overflow: hidden;

	.sidebar-nav {
		padding: 6px 0;
		flex: 1;
		overflow-y: auto;
		&::-webkit-scrollbar { width: 3px; }
		&::-webkit-scrollbar-thumb { background: rgba(0,212,255,0.12); border-radius: 3px; }

		.nav-section-label {
			padding: 12px 16px 4px;
			font-size: 10px;
			font-weight: 600;
			color: rgba(0,212,255,0.45);
			text-transform: uppercase;
			letter-spacing: 1.5px;
			white-space: nowrap;
			&:not(:first-child) {
				margin-top: 4px;
				border-top: 1px solid rgba(255,255,255,0.04);
				padding-top: 10px;
			}
		}

		.nav-item {
			display: flex;
			align-items: center;
			gap: 10px;
			padding: 0 16px;
			height: 36px;
			color: $text-sub;
			font-size: 13px;
			font-weight: 500;
			cursor: pointer;
			transition: all 0.25s;
			position: relative;
			white-space: nowrap;

			.nav-icon {
				font-size: 15px;
				opacity: 0.5;
				transition: all 0.25s;
				width: 18px;
				text-align: center;
			}

			.nav-badge {
				margin-left: auto;
				background: linear-gradient(135deg, #ef4444, #f97316);
				color: #fff;
				border-radius: 10px;
				font-size: 10px;
				min-width: 18px;
				height: 18px;
				line-height: 18px;
				text-align: center;
				padding: 0 5px;
				box-shadow: 0 0 6px rgba(239,68,68,0.3);
			}

			&:hover {
				color: $cyan;
				background: rgba(0,212,255,0.04);
				.nav-icon { opacity: 1; color: $cyan; }
			}

			&.is-active {
				color: #fff;
				background: rgba(0,212,255,0.08);
				.nav-icon { opacity: 1; color: $cyan; }
				&::before {
					content: '';
					position: absolute;
					left: 0;
					top: 8px;
					bottom: 8px;
					width: 3px;
					border-radius: 0 2px 2px 0;
					background: linear-gradient(180deg, $cyan, $purple);
					box-shadow: 0 0 8px rgba(0,212,255,0.4);
				}
			}

			.sidebar-submenu {
				position: absolute;
				left: 100%;
				top: 0;
				min-width: 140px;
				padding: 6px 0;
				background: #1e293b;
				border: 1px solid $border-glow;
				border-radius: 10px;
				box-shadow: 0 8px 24px rgba(0,0,0,0.4);
				z-index: 999;

				.submenu-item {
					padding: 8px 16px;
					font-size: 13px;
					color: $text-sub;
					transition: all 0.2s;
					&:hover {
						color: $cyan;
						background: $cyan-dim;
					}
				}
			}
		}
	}
}

.center-main {
	flex: 1;
	min-width: 0;
	z-index: 1;
	border-radius: $radius;
	background: $bg-card;
	border: 1px solid $border-glow;
	display: flex;
	flex-direction: column;
	overflow: hidden;

	.center-content-box {
		padding: 16px 24px 16px;
		overflow-y: auto;
		overflow-x: hidden;
		background: none;
		width: 100%;
		flex: 1;
		&::-webkit-scrollbar { width: 4px; }
		&::-webkit-scrollbar-thumb { background: rgba(0,212,255,0.12); border-radius: 4px; }

		.content-section-header {
			display: flex;
			align-items: center;
			gap: 10px;
			margin-bottom: 14px;
			padding-bottom: 10px;
			border-bottom: 1px solid rgba(255,255,255,0.04);
			flex-shrink: 0;

			.csh-icon {
				width: 30px; height: 30px;
				border-radius: 8px;
				background: linear-gradient(135deg, $cyan-dim, $purple-dim);
				display: flex;
				align-items: center;
				justify-content: center;
				color: $cyan;
				flex-shrink: 0;
			}
			.csh-text {
				font-size: 16px;
				font-weight: 600;
				color: $text-bright;
				letter-spacing: 1px;
			}
			.csh-line {
				flex: 1;
				height: 1px;
				background: linear-gradient(90deg, rgba(0,212,255,0.15), transparent);
			}
		}
	}
	.center-content-view {
		width: 100%;
	}

}

.publish-tabs {
	display: flex;
	align-items: center;
	gap: 4px;
	margin-bottom: 16px;
	border-bottom: 1px solid rgba(71, 85, 105, 0.15);
	padding-bottom: 10px;

	.pub-tab {
		padding: 7px 20px;
		font-size: 13px;
		color: #94a3b8;
		cursor: pointer;
		border-radius: 16px;
		transition: all 0.25s;
		font-weight: 500;
		.el-icon { margin-right: 4px; vertical-align: middle; }
		&:hover { color: #e2e8f0; background: rgba(255,255,255,0.04); }
		&.active {
			color: #fff;
			background: linear-gradient(135deg, #0ea5e9, #7c3aed);
			box-shadow: 0 2px 8px rgba(14, 165, 233, 0.25);
		}
	}
	.pub-actions {
		margin-left: auto;
		.pub-add-btn {
			border-radius: 16px;
			font-size: 12px;
			padding: 6px 16px;
			background: linear-gradient(135deg, #0ea5e9, #7c3aed) !important;
			border: none !important;
			color: #fff !important;
			&:hover {
				transform: translateY(-1px);
				box-shadow: 0 4px 12px rgba(14, 165, 233, 0.3);
			}
		}
	}
}

.center-preview-pv {
		max-width: 600px;
		display: grid;
		grid-template-columns: 1fr 1fr;
		gap: 0 24px;

		.center-item.el-form-item {
			padding: 0;
			margin: 0 0 14px;
			background: none;

			::v-deep .el-form-item__label {
				padding: 0 10px 0 0;
				color: $text-sub;
				white-space: nowrap;
				font-weight: 500;
				width: 80px;
				font-size: 13px;
				line-height: 36px;
				text-align: right;
			}
			.el-form-item__content {
				margin-left: 80px;
			}
			.el-input {
				width: 100%;
			}
			.el-input ::v-deep .el-input__inner {
				border: 1px solid rgba(255,255,255,0.08);
				border-radius: 8px;
				padding: 0 12px;
				color: $text-bright;
				background: rgba(255,255,255,0.04);
				width: 100%;
				font-size: 13px;
				height: 36px;
				transition: all 0.3s;
				&:focus {
					border-color: rgba(0,212,255,0.4);
					background: rgba(0,212,255,0.04);
					box-shadow: 0 0 12px rgba(0,212,255,0.08);
				}
			}
			.el-input ::v-deep .el-input__inner[readonly="readonly"],
			.el-input ::v-deep .is-disabled .el-input__inner {
				border-color: transparent;
				background: rgba(255,255,255,0.02);
				color: $text-dim;
				cursor: not-allowed;
			}
			.el-select {
				width: 100%;
			}
			.el-select ::v-deep .el-input__inner {
				border: 1px solid rgba(255,255,255,0.08);
				border-radius: 8px;
				padding: 0 10px;
				color: $text-bright;
				background: rgba(255,255,255,0.04);
				width: 100%;
				font-size: 13px;
				height: 36px;
			}
			.el-select ::v-deep .is-disabled .el-input__inner {
				border-color: transparent;
				background: rgba(255,255,255,0.02);
				color: $text-dim;
			}
			.el-date-editor {
				width: 100%;
			}
			.el-date-editor ::v-deep .el-input__inner {
				border: 1px solid rgba(255,255,255,0.08);
				border-radius: 8px;
				padding: 0 10px 0 30px;
				color: $text-bright;
				background: rgba(255,255,255,0.04);
				font-size: 13px;
				height: 36px;
			}
			::v-deep .el-upload--picture-card {
				background: transparent;
				border: 0;
				border-radius: 0;
				width: auto;
				height: auto;
				line-height: initial;
				vertical-align: middle;
			}
			::v-deep .upload .upload-img,
			::v-deep .el-upload .el-icon-plus {
				border: 2px dashed rgba(0,212,255,0.2);
				cursor: pointer;
				border-radius: 10px;
				color: $text-dim;
				width: 64px;
				font-size: 22px;
				line-height: 64px;
				text-align: center;
				height: 64px;
				transition: all 0.3s;
				background: rgba(0,212,255,0.03);
				&:hover {
					border-color: $cyan;
					color: $cyan;
					background: rgba(0,212,255,0.06);
					box-shadow: 0 0 16px rgba(0,212,255,0.1);
				}
			}
			::v-deep .el-upload-list .el-upload-list__item {
				border: 1px solid rgba(255,255,255,0.08);
				border-radius: 10px;
				width: 64px;
				height: 64px;
			}
			::v-deep .el-upload__tip {
				color: $text-dim;
				font-size: 11px;
			}
		}

		&.center-preview-pv-single {
			grid-template-columns: 1fr;
			max-width: 360px;
		}

		.center-item-full.el-form-item {
			grid-column: 1 / -1;
		}

		.center-btn-item {
			grid-column: 1 / -1;
			padding: 0;
			margin: 10px auto 0;
			background: none;
			width: 100%;

			.updateBtn {
				cursor: pointer;
				border-radius: 20px;
				padding: 0 28px;
				margin: 0 10px 0 0;
				background: linear-gradient(135deg, $cyan, $purple);
				display: inline-flex;
				align-items: center;
				gap: 6px;
				font-size: 13px;
				line-height: 36px;
				height: 36px;
				transition: all 0.3s;
				box-shadow: 0 4px 16px rgba(0,212,255,0.2);
				.icon { color: #fff; }
				.text { color: #fff; font-weight: 500; }
			}
			.updateBtn:hover {
				transform: translateY(-2px);
				box-shadow: 0 8px 24px rgba(0,212,255,0.3);
			}
			.closeBtn {
				cursor: pointer;
				border-radius: 20px;
				padding: 0 22px;
				margin: 0 10px 0 0;
				background: none;
				border: 1px solid rgba(255,255,255,0.1);
				display: inline-flex;
				align-items: center;
				gap: 6px;
				font-size: 13px;
				line-height: 34px;
				height: 36px;
				transition: all 0.3s;
				.icon { color: $text-sub; }
				.text { color: $text-sub; }
			}
			.closeBtn:hover {
				border-color: rgba(239,68,68,0.5);
				background: rgba(239,68,68,0.06);
				.icon { color: #ef4444; }
				.text { color: #ef4444; }
			}
		}
	}

.z-box {
	padding: 0;
	margin: 0 auto;
	width: 100%;

	.section-content {
		cursor: pointer;
		padding: 16px 20px;
		margin: 0 0 10px;
		color: $text-bright;
		display: flex;
		background: rgba(255,255,255,0.03);
		border: 1px solid rgba(255,255,255,0.05);
		width: 100%;
		justify-content: space-between;
		align-items: center;
		border-radius: 12px;
		transition: all 0.3s;

		.chat-left {
			display: flex;
			align-items: center;
			flex: 1;
			min-width: 0;
			.chat-info {
				margin: 0 0 0 14px;
				.chat-name {
					color: $text-bright;
					font-weight: 600;
					font-size: 14px;
					margin-bottom: 4px;
				}
				.chat-text {
					color: $text-sub;
					font-size: 12px;
					line-height: 1.5;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
					max-width: 300px;
				}
			}
		}
		.chat-avatar-wrap {
			position: relative;
			flex-shrink: 0;
			.chat-noread-dot {
				position: absolute;
				top: -4px;
				right: -4px;
				min-width: 18px;
				height: 18px;
				line-height: 18px;
				text-align: center;
				font-size: 10px;
				font-weight: 700;
				color: #fff;
				background: #ef4444;
				border-radius: 9px;
				padding: 0 4px;
				box-shadow: 0 0 6px rgba(239,68,68,0.5);
			}
			img {
				width: 44px;
				height: 44px;
				border-radius: 50%;
				object-fit: cover;
				border: 2px solid rgba(0,212,255,0.15);
			}
		}
		.chat-btn {
			display: flex;
			gap: 6px;
			align-items: center;
			.friendBtn-unread, .friendBtn-read {
				padding: 0 12px;
				color: $text-sub;
				background: none;
				border: 1px solid rgba(255,255,255,0.1);
				font-size: 11px;
				line-height: 26px;
				text-align: center;
				height: 28px;
				border-radius: 14px;
				transition: all 0.3s;
				cursor: pointer;
				white-space: nowrap;
			}
			.friendBtn-unread:hover {
				color: #f59e0b;
				border-color: rgba(245,158,11,0.5);
				background: rgba(245,158,11,0.06);
			}
			.friendBtn-read:hover {
				color: #22c55e;
				border-color: rgba(34,197,94,0.5);
				background: rgba(34,197,94,0.06);
			}
			.friendBtn2 {
				padding: 0 12px;
				color: $text-sub;
				background: none;
				border: 1px solid rgba(255,255,255,0.1);
				font-size: 11px;
				line-height: 26px;
				text-align: center;
				height: 28px;
				border-radius: 14px;
				transition: all 0.3s;
				cursor: pointer;
				white-space: nowrap;
			}
			.friendBtn2:hover {
				color: #ef4444;
				border-color: rgba(239,68,68,0.5);
				background: rgba(239,68,68,0.06);
			}
		}
	}
	.section-content:hover {
		background: $cyan-dim;
		border-color: rgba(0,212,255,0.15);
		transform: translateX(4px);
	}
}


.clear-float {
	clear: both;
}

.inline-chat-view {
	display: flex;
	flex-direction: column;
	height: calc(100vh - 200px);
	background: rgba(15,23,42,0.5);
	border: 1px solid $border-glow;
	border-radius: 12px;
	overflow: hidden;
}

.inline-chat-header {
	display: flex;
	align-items: center;
	padding: 12px 16px;
	background: rgba(0,0,0,0.2);
	border-bottom: 1px solid $border-glow;
	gap: 12px;

	.chat-back-btn {
		color: $cyan;
		cursor: pointer;
		font-size: 13px;
		font-weight: 600;
		display: flex;
		align-items: center;
		gap: 4px;
		padding: 4px 12px;
		border-radius: 6px;
		transition: all 0.2s;
		&:hover { background: rgba(0,212,255,0.08); }
		.el-icon { font-size: 14px; }
	}

	.chat-partner-info {
		display: flex;
		align-items: center;
		gap: 10px;
		.chat-partner-avatar {
			width: 32px;
			height: 32px;
			border-radius: 50%;
			object-fit: cover;
			border: 2px solid rgba(0,212,255,0.15);
		}
		.chat-partner-name {
			font-size: 15px;
			font-weight: 600;
			color: $text-bright;
		}
	}
}

.inline-chat-messages {
	flex: 1;
	overflow-y: auto;
	padding: 16px;
	background: #0b1120;
	background-image: linear-gradient(rgba(0,212,255,0.02) 1px, transparent 1px),
		linear-gradient(90deg, rgba(0,212,255,0.02) 1px, transparent 1px);
	background-size: 40px 40px;

	&::-webkit-scrollbar { width: 4px; }
	&::-webkit-scrollbar-thumb { background: rgba(0,212,255,0.15); border-radius: 4px; }

	.addtime {
		text-align: center;
		font-size: 11px;
		color: $text-dim;
		padding: 8px 0;
	}

	.msg-avatar {
		width: 32px;
		height: 32px;
		border-radius: 50%;
		object-fit: cover;
		flex-shrink: 0;
	}

	.left-content {
		display: flex;
		align-items: flex-start;
		gap: 8px;
		margin-bottom: 12px;
		max-width: 80%;
	}

	.right-content {
		display: flex;
		align-items: flex-start;
		justify-content: flex-end;
		gap: 8px;
		margin-bottom: 12px;
		margin-left: auto;
		max-width: 80%;
	}

	::v-deep .text-content {
		border-radius: 12px !important;
		.el-alert__title { font-size: 13px; line-height: 1.5; }
	}
	::v-deep .el-alert--warning {
		background: rgba(0,212,255,0.1) !important;
		border: 1px solid rgba(0,212,255,0.2) !important;
		.el-alert__title { color: $text-bright !important; }
	}
	::v-deep .el-alert--success {
		background: rgba(34,197,94,0.08) !important;
		border: 1px solid rgba(34,197,94,0.15) !important;
		.el-alert__title { color: $text-bright !important; }
	}
}

.inline-chat-input {
	padding: 12px 16px;
	background: rgba(0,0,0,0.2);
	border-top: 1px solid $border-glow;

	.chat-input-row {
		display: flex;
		gap: 8px;
		margin-bottom: 8px;
		::v-deep .el-input__inner {
			background: rgba(255,255,255,0.05) !important;
			border: 1px solid rgba(255,255,255,0.1) !important;
			color: $text-bright !important;
			border-radius: 8px;
			&:focus { border-color: $cyan !important; }
		}
	}

	.chat-input-actions {
		display: flex;
		gap: 6px;
		flex-wrap: wrap;
	}
}

.noList {
	color: $text-dim;
	width: 100%;
	text-align: center;
	padding: 60px 0;
	font-size: 14px;
}

/* 嵌入式子组件深色主题适配 */
.center-content-view {
	::v-deep .section-title {
		color: $text-bright !important;
		background: none !important;
		font-size: 18px !important;
		padding-top: 0 !important;
		margin: 0 0 16px !important;
		position: relative;
		padding-left: 14px !important;

		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 3px;
			height: 20px;
			background: linear-gradient(180deg, $cyan, $purple);
			border-radius: 2px;
		}
	}

	::v-deep .storeup-page {
		padding: 0 !important;
	}

	::v-deep .list-preview {
		padding: 0 !important;
		max-width: 100% !important;
	}

	::v-deep .list-form-pv {
		background: rgba(255,255,255,0.03) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		border-radius: 12px !important;
		box-shadow: none !important;
		width: 100%;
		padding: 18px !important;
		margin-bottom: 20px !important;

		.lable, .label {
			color: $text-sub !important;
			font-size: 13px;
		}
	}

	::v-deep .list5 {
		gap: 14px !important;
	}

	::v-deep .list5 .list-item {
		background: rgba(255,255,255,0.03) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		border-radius: 12px !important;
		box-shadow: none !important;
		overflow: hidden;
		transition: all 0.3s;
	}
	::v-deep .list5 .list-item:hover {
		border-color: rgba(0,212,255,0.2) !important;
		background: rgba(0,212,255,0.04) !important;
		transform: translateY(-3px);
		box-shadow: 0 8px 24px rgba(0,212,255,0.08) !important;
	}
	::v-deep .list5 .list-item .infoBox {
		background: transparent !important;
	}
	::v-deep .list5 .list-item .infoBox .name {
		color: $text-bright !important;
	}
	::v-deep .list5 .list-item .bottomInfo {
		border-top-color: rgba(255,255,255,0.06) !important;
	}
	::v-deep .list5 .list-item .bottomInfo span,
	::v-deep .list5 .list-item .bottomInfo .label,
	::v-deep .list5 .list-item .bottomInfo .text {
		color: $text-sub !important;
	}
	::v-deep .list5 .list-item .bottomInfo .icon {
		color: $text-dim !important;
	}

	::v-deep .el-table {
		background: transparent !important;
		color: $text-bright;

		&::before { background: rgba(255,255,255,0.06) !important; }

		tr { background: transparent !important; }
		th.el-table__cell {
			background: rgba(0,212,255,0.06) !important;
			color: $text-bright !important;
			border-bottom: 1px solid rgba(255,255,255,0.08) !important;
			font-weight: 600;
			font-size: 13px;
		}
		td.el-table__cell {
			border-bottom: 1px solid rgba(255,255,255,0.04) !important;
			color: $text-sub !important;
			font-size: 13px;
		}
		.el-table__row:hover > td.el-table__cell {
			background: rgba(0,212,255,0.04) !important;
		}
		.el-table__empty-block {
			background: transparent !important;
		}
		.el-table__empty-text {
			color: $text-dim !important;
		}
		.el-table__body-wrapper {
			background: transparent !important;
		}
		.cell { color: inherit; }
	}

	::v-deep .el-table--border {
		border: 1px solid rgba(255,255,255,0.06) !important;
		border-radius: 10px;
		overflow: hidden;

		td.el-table__cell, th.el-table__cell {
			border-right: 1px solid rgba(255,255,255,0.04) !important;
		}
		&::after { background: rgba(255,255,255,0.06) !important; }
	}

	::v-deep .el-card {
		background: rgba(255,255,255,0.04) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		border-radius: 12px !important;
		color: $text-bright;
		box-shadow: none !important;
		transition: all 0.3s;

		&:hover {
			border-color: rgba(0,212,255,0.2) !important;
			background: rgba(0,212,255,0.06) !important;
			transform: translateY(-3px);
			box-shadow: 0 8px 24px rgba(0,212,255,0.08) !important;
		}

		.el-card__body { padding: 0; }
	}

	::v-deep .storeup-card-body {
		color: $text-bright !important;
		padding: 12px 14px !important;
		font-weight: 500;
	}

	::v-deep .storeup-del-btn {
		border-radius: 16px !important;
		background: rgba(239,68,68,0.1) !important;
		border: 1px solid rgba(239,68,68,0.2) !important;
		color: #ef4444 !important;

		&:hover {
			background: rgba(239,68,68,0.2) !important;
		}
	}

	::v-deep .storeup-card-actions {
		padding: 0 12px 12px !important;
	}

	::v-deep .el-form-item__label {
		color: $text-sub !important;
	}
	::v-deep .el-input__inner {
		background: rgba(255,255,255,0.04) !important;
		border: 1px solid rgba(255,255,255,0.08) !important;
		color: $text-bright !important;
		border-radius: 8px !important;

		&:focus {
			border-color: rgba(0,212,255,0.3) !important;
		}
		&::placeholder {
			color: rgba(255,255,255,0.25) !important;
		}
	}
	::v-deep .el-select .el-input__inner {
		background: rgba(255,255,255,0.04) !important;
		border: 1px solid rgba(255,255,255,0.08) !important;
		color: $text-bright !important;
	}
	::v-deep .el-button--primary {
		background: linear-gradient(135deg, $cyan, $purple) !important;
		border: none !important;
		border-radius: 20px !important;
		color: #fff !important;
		box-shadow: 0 2px 8px rgba(0,212,255,0.2);
		transition: all 0.3s;

		&:hover {
			box-shadow: 0 4px 16px rgba(0,212,255,0.3);
			transform: translateY(-1px);
		}
	}
	::v-deep .el-button--success {
		background: linear-gradient(135deg, #22c55e, #059669) !important;
		border: none !important;
		border-radius: 20px !important;
		color: #fff !important;
		box-shadow: 0 2px 8px rgba(34,197,94,0.2);
	}
	::v-deep .el-button--danger {
		background: rgba(239,68,68,0.15) !important;
		border: 1px solid rgba(239,68,68,0.3) !important;
		color: #ef4444 !important;
		border-radius: 16px !important;
	}
	::v-deep .el-button--default {
		background: rgba(255,255,255,0.04) !important;
		border: 1px solid rgba(255,255,255,0.1) !important;
		color: $text-sub !important;
		border-radius: 16px !important;
	}

	::v-deep .el-pagination {
		.btn-prev, .btn-next {
			background: rgba(255,255,255,0.04) !important;
			color: $text-sub !important;
			border-radius: 6px !important;
		}
		.el-pager li {
			background: rgba(255,255,255,0.04) !important;
			color: $text-sub !important;
			border-radius: 6px !important;
			border: none !important;

			&.active {
				background: linear-gradient(135deg, $cyan, $purple) !important;
				color: #fff !important;
			}
		}
		.el-pagination__total { color: $text-dim !important; }
		.el-pagination__jump {
			color: $text-dim !important;
			.el-input__inner {
				background: rgba(255,255,255,0.04) !important;
				border: 1px solid rgba(255,255,255,0.08) !important;
				color: $text-bright !important;
			}
		}
		.el-pagination__sizes .el-input__inner {
			background: rgba(255,255,255,0.04) !important;
			border: 1px solid rgba(255,255,255,0.08) !important;
			color: $text-bright !important;
		}
	}

	::v-deep .el-rate__icon { font-size: 16px; }

	::v-deep .el-dialog {
		background: #0f172a !important;
		border: 1px solid rgba(0,212,255,0.12);
		border-radius: 14px !important;
		box-shadow: 0 20px 60px rgba(0,0,0,0.5);

		.el-dialog__header {
			background: rgba(0,0,0,0.3);
			border-bottom: 1px solid rgba(255,255,255,0.06);
			.el-dialog__title { color: $text-bright; }
			.el-dialog__headerbtn .el-dialog__close {
				color: $text-sub;
				&:hover { color: $cyan; }
			}
		}
		.el-dialog__body {
			background: #0b1120;
			color: $text-bright;
		}
		.el-dialog__footer {
			background: rgba(0,0,0,0.2);
			border-top: 1px solid rgba(255,255,255,0.06);
		}
	}

	::v-deep .page-theme-banner {
		display: none;
	}

	::v-deep .category-1 {
		.item.active {
			background: linear-gradient(135deg, $cyan, $purple) !important;
			color: #fff !important;
		}
		.item {
			color: $text-sub !important;
			background: rgba(255,255,255,0.03) !important;
			border: 1px solid rgba(255,255,255,0.06) !important;
		}
		.item:hover {
			color: $cyan !important;
			background: rgba(0,212,255,0.06) !important;
		}
	}

	::v-deep .list-form-pv > .list-item {
		.lable, .label {
			color: $text-sub !important;
		}
	}

	::v-deep .searchBtn {
		background: linear-gradient(135deg, $cyan, $purple) !important;
		border: none !important;
		color: #fff !important;
	}

	::v-deep .comment-cell-ellipsis {
		padding: 0 !important;
		min-height: auto !important;
		color: $text-sub !important;
	}

	::v-deep .my-forum-wrap {
		background: transparent !important;
	}
}


</style>
