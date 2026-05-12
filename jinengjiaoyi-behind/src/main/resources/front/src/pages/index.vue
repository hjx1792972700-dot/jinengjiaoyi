<template>
	<div class="main-containers">
		<!-- 技能交易主题浮动装饰 -->
		<div class="theme-deco-dots" aria-hidden="true">
			<div class="dot"></div>
			<div class="dot"></div>
			<div class="dot"></div>
			<div class="dot"></div>
			<div class="dot"></div>
		</div>
		<div class="body-containers">
			<div class="top-container">
				<!-- info -->
				<div class="top_title">
					<svg class="top-logo-icon" viewBox="0 0 28 28" width="28" height="28">
						<circle cx="14" cy="14" r="12" fill="none" stroke="rgba(14,165,233,0.5)" stroke-width="1.5"/>
						<path d="M9 14 L12 11 L16 17 L19 14" fill="none" stroke="#0ea5e9" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
						<circle cx="9" cy="14" r="2" fill="#7c3aed"/>
						<circle cx="19" cy="14" r="2" fill="#0ea5e9"/>
					</svg>
					<span @click="goMenu('/main/home')">基于协同过滤的技能交易系统的设计与实现</span>
				</div>
				<div class="top_tel"></div>
			

				<el-dropdown v-if="Token" class="dropdown-box" @command="handleCommand" trigger="click">
					<div class="el-dropdown-link">
						<img class="top_avatar2" v-show="headportrait" :src="headportrait?baseUrl + headportrait:avatarDefault">
						<span class="top_label2">欢迎</span>
						<span class="top_nickname2">{{username}}</span>
						<span class="icon iconfont icon-xiala"></span>
					</div>
					<template #dropdown>
					<el-dropdown-menu class="top-el-dropdown-menu">
						<el-dropdown-item v-show="notAdmin" class="user-item" :command="'user'">
							<span class="icon iconfont icon-touxiang09"></span>
							个人中心
						</el-dropdown-item>
						<el-dropdown-item class="register-item" :command="'register'">
							<span class="icon iconfont icon-shanchu16"></span>
							退出
						</el-dropdown-item>
					</el-dropdown-menu>
					</template>
				</el-dropdown>
				<div v-else class="el-dropdown-link">
					<div class="login-item" @click="toLogin">
						<span class="icon iconfont icon-touxiang03"></span>
						登录
					</div>
				</div>
			</div>


			<div class="menu-preview">
				<div class="menu-list">
					<div class="menu-home" :class="activeMenu=='/main/home'?'menu-active':''" @click="goMenu('/main/home')">
						<div class="title">
							<span class="icon iconfont icon-home19"></span>
							<div class="text">首页</div>
						</div>
					</div>
					<div  class="menu-item" v-for="(item,index) in menuList" :key="index" @mouseenter="menuShowClick4(index)" @mouseleave="menuShowClick4(-1)" :class="activeMenu==item.url?'menu-active':''" @click.stop="goMenu(item.url)">
						<div class="title">
							<el-icon class="menu-nav-icon"><component :is="iconArr[index]" /></el-icon>
							<div class="text">{{item.name}}</div>
						</div>
						<transition name="el-zoom-in-top">
							<div v-if="showType4==index&&item.hasCate" class="menu-child-list">
								<div class="child-item" v-for="(items,indexs) in item.cateList" :key="indexs" @click.stop="cateClick(item.url,items)">{{items}}</div>
							</div>
						</transition>
					</div>
					<div class="menu-item" @click="goChat" v-if="Token">
						<div class="title">
							<span class="icon iconfont icon-xiaoxi-zhihui"></span>
							<div class="text">
								AI智能问答
							</div>
						</div>
					</div>
				</div>
			</div>

			<router-view id="scrollView"></router-view>
			
		<div class="bottom-preview">
			<div class="footer">
				<div class="footer-deco">
					<svg class="footer-icon" viewBox="0 0 24 24" width="18" height="18">
						<path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-1 17.93c-3.95-.49-7-3.85-7-7.93 0-.62.08-1.21.21-1.79L9 15v1c0 1.1.9 2 2 2v1.93zm6.9-2.54c-.26-.81-1-1.39-1.9-1.39h-1v-3c0-.55-.45-1-1-1H8v-2h2c.55 0 1-.45 1-1V7h2c1.1 0 2-.9 2-2v-.41c2.93 1.19 5 4.06 5 7.41 0 2.08-.8 3.97-2.1 5.39z" fill="rgba(14,165,233,0.4)"/>
					</svg>
					<span class="footer-slogan">技能互通 · 价值共享 · 协同成长</span>
					<svg class="footer-icon" viewBox="0 0 24 24" width="18" height="18">
						<path d="M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z" fill="rgba(124,58,237,0.4)"/>
					</svg>
				</div>
				<div v-html="bottomContent"></div>
			</div>
		</div>
		</div>
		
		<el-dialog v-model="chatFormVisible" width="680px" :before-close="chatClose" class="ai-chat-dialog" :show-close="false" append-to-body>
			<div class="ai-chat-wrapper">
				<div class="ai-chat-header">
					<div class="ai-header-left">
						<div class="ai-logo-pulse"></div>
						<div class="ai-header-info">
							<span class="ai-header-title">AI 智能问答</span>
							<span class="ai-header-status"><i class="ai-status-dot"></i>在线</span>
						</div>
					</div>
					<div class="ai-header-actions">
						<el-button size="mini" class="ai-switch-btn" @click="askChange">{{askType==1?'人工服务':'智能回复'}}</el-button>
						<el-icon class="ai-close-btn" @click="chatClose"><Close /></el-icon>
					</div>
				</div>

				<div class="ai-chat-body" id="chat-content">
				<div class="ai-welcome" v-if="chatList.length===0">
					<div class="ai-welcome-icon">
						<svg viewBox="0 0 60 60" width="48" height="48"><circle cx="30" cy="30" r="28" fill="none" stroke="rgba(0,212,255,0.3)" stroke-width="1.5"/><circle cx="30" cy="30" r="20" fill="none" stroke="rgba(0,212,255,0.5)" stroke-width="1"/><circle cx="22" cy="26" r="3" fill="#00d4ff"/><circle cx="38" cy="26" r="3" fill="#7c3aed"/><path d="M22 36 Q30 42 38 36" fill="none" stroke="#00d4ff" stroke-width="2" stroke-linecap="round"/></svg>
					</div>
					<p class="ai-welcome-text">你好！我是 AI 助手，有什么可以帮你的？</p>
					<p class="ai-suggest-title">你可以试着问我：</p>
					<div class="ai-suggest-list">
						<span class="ai-suggest-tag" v-for="(q, qi) in suggestQuestions" :key="qi" @click="quickAsk(q)">{{q}}</span>
					</div>
				</div>
				<div class="ai-suggest-bar" v-if="chatList.length > 0 && !aiLoading">
					<span class="ai-suggest-bar-label">猜你想问：</span>
					<span class="ai-suggest-mini" v-for="(q, qi) in suggestQuestionsShort" :key="qi" @click="quickAsk(q)">{{q}}</span>
				</div>
					<div v-bind:key="item.id" v-for="item in chatList" class="ai-msg-group">
						<div v-if="item.addtime" class="ai-msg-time">{{timeFormat(item.addtime)}}</div>
						<div v-if="item.ask" class="ai-msg ai-msg-user">
							<div class="ai-bubble ai-bubble-user">
								<span v-if="item.type==1">{{item.ask}}</span>
								<el-image v-else-if="item.type==2" :src="baseUrl + item.ask" style="width: 150px;height: 150px;border-radius:8px;" fit="cover" :preview-src-list="[baseUrl + item.ask]"></el-image>
								<video v-else-if="item.type==3" :src="baseUrl + item.ask" style="width: 240px;border-radius:8px;" controls></video>
								<el-button v-else-if="item.type==4" size="mini" @click="download(item.ask)">文件预览</el-button>
							</div>
							<img class="ai-avatar" :src="item.uimage?(baseUrl + item.uimage.split(',')[0]):serviceImg" />
						</div>
						<div v-else class="ai-msg ai-msg-bot">
							<img class="ai-avatar ai-avatar-bot" :src="item.uimage?(baseUrl + item.uimage.split(',')[0]):aiImg" />
							<div class="ai-bubble ai-bubble-bot">
								<span v-if="item.type==1">{{item.reply}}</span>
								<el-image v-else-if="item.type==2" :src="baseUrl + item.reply" style="width: 150px;height: 150px;border-radius:8px;" fit="cover" :preview-src-list="[baseUrl + item.reply]"></el-image>
								<video v-else-if="item.type==3" :src="baseUrl + item.reply" style="width: 240px;border-radius:8px;" controls></video>
								<el-button v-else-if="item.type==4" size="mini" @click="download(item.reply)">文件预览</el-button>
							</div>
						</div>
					</div>
					<div v-if="aiLoading" class="ai-msg ai-msg-bot">
						<img class="ai-avatar ai-avatar-bot" :src="aiImg" />
						<div class="ai-bubble ai-bubble-bot ai-typing">
							<span class="ai-dot"></span><span class="ai-dot"></span><span class="ai-dot"></span>
						</div>
					</div>
				</div>

				<div class="ai-chat-footer">
					<div v-if="askShow" class="ai-upload-bar">
						<el-upload :action="uploadUrl" :on-success="uploadSuccess" accept=".jpg,.png" :show-file-list="false">
							<el-button size="mini" class="ai-upload-btn"><el-icon><Picture /></el-icon> 图片</el-button>
						</el-upload>
						<el-upload :action="uploadUrl" :on-success="uploadSuccess2" accept=".mp4" :show-file-list="false">
							<el-button size="mini" class="ai-upload-btn"><el-icon><VideoCamera /></el-icon> 视频</el-button>
						</el-upload>
						<el-upload :action="uploadUrl" :on-success="uploadSuccess3" :show-file-list="false">
							<el-button size="mini" class="ai-upload-btn"><el-icon><FolderOpened /></el-icon> 文件</el-button>
						</el-upload>
					</div>
					<div class="ai-input-row">
						<el-icon class="ai-plus-icon" @click="askShow = !askShow"><CirclePlus /></el-icon>
						<input class="ai-input" v-model="form.ask" placeholder="输入你的问题..." @keydown.enter="addChat(null)" />
						<button class="ai-send-btn" @click="addChat(null)"><el-icon><Promotion /></el-icon></button>
						<div style="position: relative;" v-if="askType==2">
						<span @click="showEmoji=!showEmoji" class="icon iconfont icon-gerenzhongxin-zhihui" style="font-size: 24px;color: rgba(255,255,255,0.5);cursor: pointer;"></span>
						<div v-if="showEmoji" class="simple-emoji-picker" style="position: absolute;bottom: 40px;left: -100px;background:#fff;border-radius:8px;padding:8px;box-shadow:0 2px 12px rgba(0,0,0,0.15);display:flex;flex-wrap:wrap;width:280px;max-height:200px;overflow-y:auto;z-index:9999;">
							<span v-for="(em, ei) in emojiList" :key="ei" @click="addEmoji({native:em})" style="cursor:pointer;font-size:22px;padding:4px;display:inline-block;">{{em}}</span>
						</div>
						</div>
					</div>
				</div>
			</div>
		</el-dialog>
		<div class="audioAnimation-box" :class="showType?'audioAnimation-box1':''" v-if="audio.length"
			:style="{'transform':audio[audioIndex].lrc&&audio[audioIndex].lrc!=''?'rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 98px, 0px)':'rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 68px, 0px)'}"
			style="width: 100%;position: fixed;bottom: 0;left: 0;z-index: 99999;" @mouseover="showmouseover">
			<div @click="suoClick"
				style="position: absolute;top: -20px;right: 40%;background: #fff;border-radius: 50% 50% 0 0;font-size: 0;width: 30px;height: 30px;display: flex;justify-content: center;align-items: center;cursor: pointer;">
				<img v-if="suoType" style="width: 20px;height: 20px;" src="../assets/suo.png">
				<img v-else style="width: 20px;height: 20px;" src="../assets/jiesuo.png">
			</div>
			<audio ref="aplayer" :src="audio[audioIndex] ? audio[audioIndex].url : ''" controls style="width:100%;height:40px;"></audio>
		</div>
	</div>
</template>

<script>
	import Swiper from "swiper";
	
	import timeMethod from '@/common/timeMethod'
	import avatarDefault from '@/assets/avator.png'
	import serviceImg from '@/assets/service.png'
	import aiImg from '@/assets/AI.png'
	
	import {
		WebsocketMixin
	} from '@/mixins/WebsocketMixin'
	import { Close, Picture, VideoCamera, FolderOpened, CirclePlus, Promotion } from '@element-plus/icons-vue'
export default {
	components:{
		Close,
		Picture,
		VideoCamera,
		FolderOpened,
		CirclePlus,
		Promotion,
	},
	mixins: [WebsocketMixin],
	data() {
		return {
			activeIndex: '0',
			baseUrl: '',
			carouselList: [],
			carouselForm: {
				inHome: true,
				inOther: true,
			},
			mySwiper3Timer: null,
			menuList: [],
			askType: 1, //1为智能回复 2为人工服务
			chatFormVisible: false,
			chatList: [],
			headers: {
				Token: localStorage.getItem('frontToken')
			},
			uploadUrl: this.$config.baseUrl + 'file/upload',
			askShow: false,
			aiLoading: false,
			showEmoji: false,
			emojiList: ['😀','😁','😂','🤣','😃','😄','😅','😆','😉','😊','😋','😎','😍','😘','🥰','😗','😙','😚','🙂','🤗','🤩','🤔','🤨','😐','😑','😶','🙄','😏','😣','😥','😮','🤐','😯','😪','😫','😴','😌','😛','😜','😝','🤤','😒','😓','😔','😕','🙃','🤑','😲','🙁','😖','😞','😟','😤','😢','😭','😦','😧','😨','😩','🤯','😬','😰','😱','🥵','🥶','😳','🤪','😵','😡','😠','🤬','😷','🤒','🤕','🤢','🤮','🥴','😇','🥳','🥺','🤠','🤡','🤥','🤫','🤭','👋','👍','👎','👏','🙏','🤝','💪','❤️','🔥','⭐','🎉','💯'],
		form: {
			ask: '',
		},
		suggestQuestions: [
			'如何注册账号？',
			'怎么发布技能需求？',
			'技能交换的流程是什么？',
			'信誉指数有什么用？',
			'如何联系管理员？',
			'可以同时发布多个技能需求吗？',
			'怎么修改个人信息？',
			'平台收费吗？',
		],
		suggestQuestionsShort: [
			'怎么发布技能？',
			'交换流程是什么？',
			'如何提高信誉？',
			'平台收费吗？',
		],
			headportrait: localStorage.getItem('frontHeadportrait')?localStorage.getItem('frontHeadportrait'):'',
			Token: localStorage.getItem('frontToken'),
			username: localStorage.getItem('displayName') || localStorage.getItem('username'),
			notAdmin: localStorage.getItem('frontSessionTable')!='"users"',
			iconArr: [
				'Histogram',
				'Reading',
				'Bell',
				'ChatDotRound',
				'EditPen',
				'QuestionFilled',
				'PictureRounded',
				'CameraFilled',
				'VideoCameraFilled',
				'VideoCamera',
				'Connection',
				'List',
				'Platform',
				'UserFilled',
				'Operation',
				'Promotion',
				'Finished',
				'Ticket',
				'Management',
				'Open',
				'Shop',
				'Sell',
				'Flag',
				'ChatLineRound',
				'Coin',
				'Medal',
				'Aim',
				'DataLine',
				'CircleCheck',
			],
			bottomContent: '',
			musicType: false,
			showTimer: null,
			showType: false,
			suoType:false,
			showType4: -1,
			indexBgUrl: '',
			avatarDefault,
			serviceImg,
			aiImg,
		}
	},
	async created() {
		this.$http.get('config/info?name=fTopLogo',).then(rs=>{this.indexLogoUrl = rs.data.data?rs.data.data.value:''})
		this.baseUrl = this.$config.baseUrl;
		this.menuList = this.$config.indexNav;
		this.getCarousel();
		if(localStorage.getItem('frontToken') && localStorage.getItem('frontToken')!=null) {
			this.getSession()
		}
		this.cateList = this.$config.cateList
		if(this.cateList.length){
			for(let x in this.menuList){
				for(let i in this.cateList){
					if(this.menuList[x].name==this.cateList[i].name){
						await this.$http.get(`option/${this.cateList[i].refTable}/${this.cateList[i].refColumn}`).then(rs=>{
							this.menuList[x].cateList = rs.data.data
							this.menuList[x].hasCate = true
						})
					}
				}
			}
		}
	},
	mounted() {
		this.activeIndex = localStorage.getItem('keyPath') || '0';
		this.musicType = localStorage.getItem('musicType') ? true : false;


		// banner
		setTimeout(()=>{
			this.mySwiper3Timer = new Swiper(".mySwiper3", {"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"pagination":{"el":".swiper-pagination","clickable":true},"slidesPerView":3,"speed":300,"autoplay":{"delay":2500,"disableOnInteraction":false},"effect":"fade"})
		}, 500)

	},
	computed: {
		activeMenu() {
			const route = this.$route
			const {
				meta,
				path
			} = route
			// if st path, the sidebar will highlight the path you sete
			if (meta.activeMenu) {
				return meta.activeMenu
			}
			return path
		},
		audioIndex: {
			get() {
				return this.$store.state.app.audioIndex
			},
			set(val) {
				return this.$store.state.app.audioIndex = val
			}
		},
		audio: {
			get() {
				return this.$store.state.app.audio
			},
			set(val) {
				return this.$store.state.app.audio = val
			}
		},
	},
	watch: {
		$route(newValue) {
			let that = this
			let url = window.location.href
			let arr = url.split('#')
			for (let x in this.menuList) {
				if (newValue.path == this.menuList[x].url) {
					this.activeIndex = x
				}
			}
			this.Token = localStorage.getItem('frontToken')
			if(arr[1]!='/main/home'){
				var element = document.getElementById('scrollView');
				var distance = element.offsetTop;
				window.scrollTo( 0, distance )
			}else{
				setTimeout(()=>{
					window.scrollTo( 0, 0 )
				},100)
			}
		},
		headportrait(){
			this.$forceUpdate()
		},
		audio(newValue) {
			this.audioIndex = this.$store.state.app.audioIndex
		},
		audioIndex() {
			this.showmouseover()
			this.$nextTick(() => {
				if (this.$refs.aplayer) this.$refs.aplayer.play()
			})
			this.$forceUpdate()
		},
	},
	methods: {
		cateClick(url,fenlei){
			this.$router.push(url + '?homeFenlei=' + fenlei);
		},
		preHttp(str) {
			return str && str.substr(0,4)=='http';
		},

		async getSession() {
			await this.$http.get(`${localStorage.getItem('UserTableName')}/session`).then(async res => {
				if (res.data.code == 0) {
					localStorage.setItem('sessionForm',JSON.stringify(res.data.data))
					localStorage.setItem('frontUserid', res.data.data.id);
					if(res.data.data.yonghuxingming) {
						localStorage.setItem('displayName', res.data.data.yonghuxingming);
						this.username = res.data.data.yonghuxingming;
					}
					if(res.data.data.vip) {
						localStorage.setItem('vip', res.data.data.vip);
					}
					if(res.data.data.touxiang) {
						this.headportrait = res.data.data.touxiang
						localStorage.setItem('frontHeadportrait', res.data.data.touxiang);
					} else if(res.data.data.headportrait) {
						this.headportrait = res.data.data.headportrait
						localStorage.setItem('frontHeadportrait', res.data.data.headportrait);
					}
				}
			});
		},
		handleSelect(keyPath) {
			if (keyPath) {
				localStorage.setItem('keyPath', keyPath)
			}
		},
		toLogin() {
		  this.$router.push('/index');
		},
		async logout() {
			await this.$http.post(`${localStorage.getItem('frontSessionTable')}/logout`).then(rs=>{
				localStorage.clear();
				this.$router.push('/index');
				this.activeIndex = '0'
				localStorage.setItem('keyPath', this.activeIndex)
				this.Token = ''
				this.$forceUpdate()
				this.$message({
					message: '登出成功',
					type: 'success',
					duration: 1000,
				});
			})
		},
		getCarousel() {
			this.$http.get('config/list', {params: {type: 1,limit: 100,type: 1}}).then(res => {
				if (res.data.code == 0) {
					this.carouselList = res.data.data.list;
				}
			});
		},
		// 轮播图跳转
		carouselClick(url) {
			if (url) {
				if (url.indexOf('https') != -1) {
					window.open(url)
				} else {
					this.$router.push(url)
				}
			}
		},
		carouselChange(){
			let url = window.location.href
			let arr = url.split('#')
			return (this.carouselForm.inHome&&arr[1]=='/main/home')||(this.carouselForm.inOther&&arr[1]!='/main/home')
		},
		chatTimeChange() {
			let chatList = JSON.parse(JSON.stringify(this.chatList)).reverse()
			if(!chatList.length) {
				return true
			}
			if(chatList[0].reply&&chatList[0].reply=="您好，有什么可以帮您？") {
				return false
			}
			let lastTime = new Date().getTime();
			const currentTime = new Date(chatList[0].addtime).getTime();
			const timeDiff = (currentTime - lastTime) / 1000 / 60; // 转换为分钟
			if (timeDiff < 3) {
				return false
			}
			return true
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
		addEmoji(e) {
			this.form.ask += e.native;
			this.showEmoji = false
		},
		async getChatList() {
			await this.$http.get('chat/list', {params: { userid: Number(localStorage.getItem('frontUserid')), sort: 'addtime', order: 'asc',limit: 1000 }}).then(res => {
				if (res.data.code == 0) {
					this.chatList = this.formatMessages(res.data.data.list);
					let div = document.getElementsByClassName('chat-content')[0]
					setTimeout(() => {
						if (div){
							div.scrollTop = div.scrollHeight
						}
					}, 0)
				}
			});
		},
		quickAsk(question) {
		this.form.ask = question;
		this.addChat(null);
	},
	addChat(ask=null,type=1) {
			let params = JSON.parse(JSON.stringify(this.form))
			if(params.ask==''&&ask==null){
				this.$message.error('内容不能为空')
				return false
			}
			if(ask){
				params.ask = ask
			}
			params.type = type
			params.uimage = localStorage.getItem('frontHeadportrait')
			params.uname = localStorage.getItem('username')
			params.userid = Number(localStorage.getItem('frontUserid'))
			this.$http.post('chat/add', params).then(res => {
				if (res.data.code == 0) {
					if (this.askType == 1 && ask == null) {
						let ask2 = JSON.parse(JSON.stringify(this.form.ask))
						this.$nextTick(function() {
							setTimeout(() => {
								this.getChathelper(ask2)
							}, 1000) // 要加点延迟, 不然有可能不生效
						})
					}
					if(this.askType==2){
						this.websocketSend(ask?ask:params.ask)
					}
					this.form.ask = '';
					this.getChatList();
				}
			});
		},
		chatClose() {
			if(this.askType==2){
				this.websocketOnclose();
			}
			this.chatFormVisible = false;
		},
		websocketOnmessage:function(e) {
			this.getChatList()
		},
		async goChat() {
			if(!localStorage.getItem('frontToken')) {
				this.toLogin();
				return;
			}
			await this.getChatList();
			this.askType = 1
			if(this.chatTimeChange()) {
				this.saveChathelper("您好，有什么可以帮您？");
			}
			this.chatFormVisible = true;
		},
		uploadSuccess(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,2)
			}
		},
		uploadSuccess2(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,3)
			}
		},
		uploadSuccess3(res) {
			if (res.code == 0) {
				this.askShow = !this.askShow;
				this.addChat('upload/' + res.file,4)
			}
		},
		download(url){
			if(!url){
				return false
			}
			window.open((location.href.split(this.$config.name).length>1 ? location.href.split(this.$config.name)[0] + this.$config.name + '/' + url :this.$config.baseUrl + url))
		},
		getChathelper(ask) {
			this.$http.get('chathelper/page', {
				params: {
					ask: `%${ask}%`,
					limit: 100
				}
			}).then(res => {
				if (res.data.code == 0) {
					if (res.data.data.list.length) {
						let arr = this.getRandomNumber(res.data.data.list)
						this.saveChathelper(arr.reply)
					} else {
						this.getChathelper2(ask)
					}
				}
			});
		},
	getChathelper2(ask) {
		this.aiLoading = true
		let div = document.getElementById('chat-content')
		setTimeout(() => {
			if (div) div.scrollTop = div.scrollHeight
		}, 100)
		this.$http.post('baidu/askai', {
			ask: `${ask}`,
		}).then(res => {
			this.aiLoading = false
			if (res.data.code == 0) {
				this.saveChathelper(res.data.data);
			} else {
				this.saveChathelper('抱歉，AI服务暂时不可用，请稍后再试。您也可以点击上方推荐问题获取帮助，或切换到"人工服务"联系管理员。');
			}
		}).catch(() => {
			this.aiLoading = false
			this.saveChathelper('抱歉，网络连接异常，请稍后再试。');
		});
	},
		getRandomNumber(arr) {
			var randomIndex = Math.floor(Math.random() * arr.length);
			return arr[randomIndex];
		},
		saveChathelper(reply) {
			this.$http.post('chat/save', {
				reply: reply,
				userid: Number(localStorage.getItem('frontUserid')),
				type: 1
			}).then(res => {
				if (res.data.code == 0) {
					this.form.ask = '';
					this.getChatList();
				}
			});
		},
		askChange() {
			this.askShow = !this.askShow;
			this.askType = this.askType == 1 ? 2 : 1
			if(this.askType==1) {
				if(this.chatTimeChange()) {
					this.saveChathelper("您好，有什么可以帮您？");
				}
				
				this.websocketOnclose();
			} else if(this.askType==2){
				if(this.chatTimeChange()) {
					this.saveChathelper('您好，在线客服很高兴为您服务！')
				}
				this.initWebSocket(1)
			}
		},
		menuShowClick4(index){
			this.showType4 = index
		},
	goMenu(path) {
		this.$router.push(path);
		this.$nextTick(() => {
			window.scrollTo(0, 0);
			const body = document.querySelector('.body-containers');
			if (body) body.scrollTop = 0;
		});
	},
		handleCommand(name){
			if(name == 'register') {
				this.logout()
			}
			else if (name == 'service') {
				this.goChat()
			}
			else if (name == 'user'){
				this.goMenu('/main/center')
			}
			else if (name == 'login'){
				this.toLogin()
			}
		},
		suoClick(){
			this.suoType = !this.suoType
			if(this.suoType){
				clearTimeout(this.showTimer)
			}else{
				this.showmouseover()
			}
		},
		showmouseover() {
			if(this.suoType){
				return false
			}
			let that = this
			clearTimeout(this.showTimer)
			this.showType = true
			this.showTimer = setTimeout(() => {
				if (that.$refs.aplayer) that.$refs.aplayer.showList = false;
				that.showType = false

			}, 6000)
		},
		timeChange(e){
			let user = JSON.parse(localStorage.getItem('sessionForm'))
			if(e.target.currentTime>30&&this.audio[this.audioIndex].isfree==0&&(!user||!user.vip||(user.vip&&user.vip!='是'))){
				if (this.$refs.aplayer) this.$refs.aplayer.pause()
				if(this.audioIndex==this.audio.length - 1){
					this.$store.state.app.audioIndex = 0
				}else{
					this.$store.state.app.audioIndex++
				}
			}
		},
		playing(e){
			for(let x in this.audio) {
				if(this.$refs.aplayer && this.$refs.aplayer.currentMusic && this.audio[x].id == this.$refs.aplayer.currentMusic.id) {
					this.$store.state.app.audioIndex = Number(x)
					break
				}
			}
		},
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$cyan: #0ea5e9;
$purple: #7c3aed;
$accent: #0284c7;
$bg-page: #f0f4f8;
$bg-header: #0f172a;
$bg-nav: #1e293b;
$bg-card: #ffffff;
$border-light: rgba(14,165,233,0.12);
$text-dark: #1e293b;
$text-body: #475569;
$text-sub: #94a3b8;

.top-el-dropdown-menu {
	border: 1px solid #e2e8f0;
	border-radius: 12px;
	padding: 8px 0;
	box-shadow: 0 8px 24px rgba(0,0,0,0.1);
	margin: 12px 0;
	background: #fff;
	.service-item, .user-item, .register-item {
		border: 0;
		padding: 0 14px;
		margin: 0 6px;
		color: $text-dark;
		background: none;
		width: auto;
		font-size: 14px;
		line-height: 36px;
		height: 36px;
		border-radius: 8px;
		transition: all 0.2s;
		.icon { color: inherit; font-size: 14px; }
	}
	.service-item:hover, .user-item:hover, .register-item:hover {
		color: $cyan;
		background: rgba(14,165,233,0.06);
	}
}

.main-containers {
	.body-containers {
		padding: 0;
		margin: 0;
		background: $bg-page;
		min-height: 100vh;
		position: relative;

		.top-container {
			padding: 0 10%;
			z-index: 1002;
			color: #fff;
			display: flex;
			font-size: 15px;
			top: 0;
			left: 0;
			background: $bg-header;
			width: 100%;
			justify-content: flex-end;
			align-items: center;
			position: sticky;
			height: 56px;
			box-shadow: 0 2px 16px rgba(0,0,0,0.15);

			.top_title {
				margin: 0 auto 0 0;
				display: flex;
				align-items: center;
				gap: 10px;
				.top-logo-icon {
					flex-shrink: 0;
					filter: drop-shadow(0 0 6px rgba(14,165,233,0.3));
					animation: logoSpin 12s linear infinite;
				}
				@keyframes logoSpin {
					0% { transform: rotate(0deg); }
					100% { transform: rotate(360deg); }
				}
				span {
					padding: 0;
					background: linear-gradient(90deg, #fff 0%, $cyan 70%, $purple 100%);
					-webkit-background-clip: text;
					-webkit-text-fill-color: transparent;
					background-clip: text;
					font-weight: 700;
					font-size: 22px;
					line-height: 56px;
					letter-spacing: 2px;
					cursor: pointer;
				}
			}
			.top_tel {
				margin: 0 10px;
				color: inherit;
				font-size: inherit;
			}
			.dropdown-box {
				color: inherit;
				display: flex;
				font-size: inherit;
				.el-dropdown-link {
					display: flex;
					align-items: center;
					cursor: pointer;
					.top_avatar2 {
						border-radius: 50%;
						margin: 0 10px;
						object-fit: cover;
						display: inline-block;
						width: 34px;
						height: 34px;
						border: 2px solid rgba(14,165,233,0.4);
					}
					.top_label2 {
						color: rgba(255,255,255,0.6);
						font-size: 13px;
						line-height: 32px;
					}
					.top_nickname2 {
						color: $cyan;
						font-size: 14px;
						font-weight: 500;
						line-height: 32px;
						margin-left: 4px;
					}
					.icon {
						margin: 0 0 0 5px;
						color: rgba(255,255,255,0.5);
						font-size: 12px;
					}
					.login-item {
						border: 1px solid rgba(14,165,233,0.4);
						padding: 0 16px;
						margin: 0;
						color: $cyan;
						background: rgba(14,165,233,0.08);
						font-size: 14px;
						line-height: 32px;
						height: 34px;
						border-radius: 17px;
						transition: all 0.3s;
						display: flex;
						align-items: center;
						gap: 4px;
						.icon { margin: 0; color: inherit; font-size: 15px; }
					}
					.login-item:hover {
						cursor: pointer;
						background: rgba(14,165,233,0.2);
						border-color: $cyan;
						box-shadow: 0 0 12px rgba(14,165,233,0.2);
					}
				}
			}
		}

		.menu-preview {
			border-radius: 0;
			padding: 0;
			margin: 0;
			z-index: 100;
			color: rgba(255,255,255,0.85);
			background: $bg-nav;
			width: 100%;
			font-size: 15px;
			position: sticky;
			top: 56px;
			box-shadow: 0 1px 8px rgba(0,0,0,0.08);

			&::after {
				content: '';
				position: absolute;
				bottom: 0; left: 0; right: 0;
				height: 1px;
				background: linear-gradient(90deg, transparent 0%, rgba(14,165,233,0.3) 30%, rgba(124,58,237,0.3) 70%, transparent 100%);
			}

			.menu-list {
				padding: 0 10%;
				color: rgba(255,255,255,0.85);
				display: flex;
				justify-content: center;
				position: relative;

				.menu-home, .menu-item {
					cursor: pointer;
					line-height: 46px;
					height: 46px;
					position: relative;

					.title {
						cursor: pointer;
						padding: 0 24px;
						margin: 0;
						display: flex;
						align-items: center;
						border: none;
						transition: all 0.3s;
						.menu-nav-icon {
							margin: 0 6px 0 0;
							color: rgba(255,255,255,0.4);
							font-size: 16px;
							vertical-align: middle;
						}
						.icon {
							padding: 0;
							margin: 0 6px 0 0;
							color: rgba(255,255,255,0.4);
							display: inline-flex;
							font-size: 16px;
							align-items: center;
						}
						.text {
							padding: 0;
							color: inherit;
							white-space: nowrap;
							font-size: inherit;
							transition: color 0.3s;
						}
					}

					&::after {
						content: '';
						position: absolute;
						bottom: 0;
						left: 50%;
						width: 0;
						height: 2px;
						background: linear-gradient(90deg, $cyan, $purple);
						border-radius: 1px;
						transition: all 0.3s;
						transform: translateX(-50%);
					}

					&:hover {
						.title { color: $cyan; }
						&::after { width: 60%; }
					}

					&.menu-active {
						.title { color: #fff; font-weight: 600; }
						&::after { width: 80%; box-shadow: 0 0 8px rgba(14,165,233,0.4); }
					}
				}

				.menu-item {
					.menu-child-list {
						z-index: 9999;
						flex-direction: column;
						background: $bg-nav;
						border: 1px solid rgba(255,255,255,0.08);
						border-radius: 12px;
						display: flex;
						width: 200px;
						justify-content: flex-start;
						position: absolute;
						flex-wrap: wrap;
						overflow: hidden;
						box-shadow: 0 8px 24px rgba(0,0,0,0.2);
						margin-top: -1px;

						.child-item {
							cursor: pointer;
							padding: 0 20px;
							color: rgba(255,255,255,0.75);
							width: 100% !important;
							font-size: 14px;
							line-height: 40px;
							transition: all 0.2s;
						}
						.child-item:hover {
							color: #fff;
							background: rgba(14,165,233,0.15);
						}
					}
				}
			}
		}

		.bottom-preview {
			width: 100%;
			height: auto;
			.footer {
				padding: 20px 10% 16px;
				color: rgba(255,255,255,0.5);
				display: flex;
				flex-direction: column;
				font-size: 13px;
				min-height: 80px;
				overflow: hidden;
				align-content: center;
				background: $bg-header;
				width: 100%;
				justify-content: center;
				align-items: center;
				text-align: center;
				letter-spacing: 1px;
				position: relative;

				&::before {
					content: '';
					position: absolute;
					top: 0; left: 0; right: 0;
					height: 2px;
					background: linear-gradient(90deg, transparent, $cyan, $purple, transparent);
				}

				.footer-deco {
					display: flex;
					align-items: center;
					gap: 10px;
					margin-bottom: 8px;

					.footer-slogan {
						font-size: 12px;
						color: rgba(255,255,255,0.35);
						letter-spacing: 3px;
						font-weight: 300;
					}
					.footer-icon {
						opacity: 0.6;
					}
				}
			}
		}
	}
}

::v-deep .ai-chat-dialog {
	border-radius: 16px !important;
	overflow: hidden;
	background: #0b1120 !important;
	border: 1px solid rgba(0,212,255,0.12);
	box-shadow: 0 20px 60px rgba(0,0,0,0.5), 0 0 40px rgba(0,212,255,0.08);
	.el-dialog__header { display: none; }
	.el-dialog__body { padding: 0 !important; }
	.el-dialog__footer { display: none; }
}

.ai-chat-wrapper {
	display: flex;
	flex-direction: column;
	height: 600px;
}

.ai-chat-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 14px 20px;
	background: linear-gradient(135deg, rgba(0,212,255,0.08), rgba(124,58,237,0.06));
	border-bottom: 1px solid rgba(0,212,255,0.1);

	.ai-header-left {
		display: flex; align-items: center; gap: 12px;
	}
	.ai-logo-pulse {
		width: 36px; height: 36px; border-radius: 50%;
		background: linear-gradient(135deg, #00d4ff, #7c3aed);
		animation: ai-pulse 2s ease-in-out infinite;
		position: relative;
		&::after {
			content: 'AI'; position: absolute; inset: 0;
			display: flex; align-items: center; justify-content: center;
			color: #fff; font-size: 13px; font-weight: 700;
		}
	}
	@keyframes ai-pulse {
		0%, 100% { box-shadow: 0 0 0 0 rgba(0,212,255,0.3); }
		50% { box-shadow: 0 0 0 8px rgba(0,212,255,0); }
	}
	.ai-header-info { display: flex; flex-direction: column; }
	.ai-header-title { font-size: 15px; font-weight: 600; color: #e2e8f0; letter-spacing: 1px; }
	.ai-header-status { font-size: 11px; color: rgba(255,255,255,0.4); display: flex; align-items: center; gap: 4px; }
	.ai-status-dot { width: 6px; height: 6px; border-radius: 50%; background: #22c55e; display: inline-block; }
	.ai-header-actions { display: flex; align-items: center; gap: 10px; }
	.ai-switch-btn {
		background: rgba(0,212,255,0.1) !important; border: 1px solid rgba(0,212,255,0.2) !important;
		color: #00d4ff !important; border-radius: 12px !important; font-size: 11px !important;
		&:hover { background: rgba(0,212,255,0.2) !important; }
	}
	.ai-close-btn { font-size: 18px; color: rgba(255,255,255,0.3); cursor: pointer; &:hover { color: #fff; } }
	.ai-close-btn.el-icon { vertical-align: middle; }
}

.ai-chat-body {
	flex: 1; overflow-y: auto; padding: 16px 20px;
	background-image: linear-gradient(rgba(0,212,255,0.02) 1px, transparent 1px), linear-gradient(90deg, rgba(0,212,255,0.02) 1px, transparent 1px);
	background-size: 40px 40px;

	&::-webkit-scrollbar { width: 4px; }
	&::-webkit-scrollbar-thumb { background: rgba(0,212,255,0.15); border-radius: 4px; }
}

.ai-welcome {
	display: flex; flex-direction: column; align-items: center; justify-content: center;
	padding: 30px 20px 10px; opacity: 0.9;
	.ai-welcome-icon { margin-bottom: 12px; }
	.ai-welcome-text { color: rgba(255,255,255,0.5); font-size: 14px; margin-bottom: 6px; }
}
.ai-suggest-title {
	color: rgba(255,255,255,0.35); font-size: 12px; margin-bottom: 10px;
}
.ai-suggest-list {
	display: flex; flex-wrap: wrap; gap: 8px; justify-content: center; max-width: 480px;
}
.ai-suggest-tag {
	display: inline-block; padding: 6px 14px; border-radius: 16px; font-size: 13px;
	background: rgba(0,212,255,0.08); border: 1px solid rgba(0,212,255,0.2);
	color: #7dd3fc; cursor: pointer; transition: all 0.2s;
	&:hover { background: rgba(0,212,255,0.18); border-color: rgba(0,212,255,0.5); color: #fff; transform: translateY(-1px); }
}
.ai-suggest-bar {
	display: flex; align-items: center; gap: 6px; padding: 6px 16px; flex-wrap: wrap;
	border-bottom: 1px solid rgba(255,255,255,0.04);
}
.ai-suggest-bar-label {
	font-size: 11px; color: rgba(255,255,255,0.25); white-space: nowrap;
}
.ai-suggest-mini {
	padding: 3px 10px; border-radius: 12px; font-size: 11px;
	background: rgba(124,58,237,0.1); border: 1px solid rgba(124,58,237,0.2);
	color: #c4b5fd; cursor: pointer; transition: all 0.2s;
	&:hover { background: rgba(124,58,237,0.25); color: #fff; }
}

.ai-msg-group { margin-bottom: 4px; }

.ai-msg-time {
	text-align: center; font-size: 10px; color: rgba(255,255,255,0.2);
	padding: 6px 0; letter-spacing: 0.5px;
}

.ai-msg { display: flex; align-items: flex-start; gap: 8px; margin-bottom: 12px; }
.ai-msg-user { flex-direction: row-reverse; }

.ai-avatar { width: 32px; height: 32px; border-radius: 50%; flex-shrink: 0; object-fit: cover; }
.ai-avatar-bot { box-shadow: 0 0 10px rgba(0,212,255,0.2); border: 1px solid rgba(0,212,255,0.2); }

.ai-bubble {
	max-width: 75%; padding: 10px 14px; border-radius: 14px;
	font-size: 13px; line-height: 1.6; word-break: break-word; white-space: pre-line;
}
.ai-bubble-user {
	background: linear-gradient(135deg, rgba(124,58,237,0.6), rgba(124,58,237,0.4));
	color: #e2e8f0; border-bottom-right-radius: 4px;
}
.ai-bubble-bot {
	background: rgba(255,255,255,0.06); color: #c8d6e5;
	border: 1px solid rgba(0,212,255,0.08); border-bottom-left-radius: 4px;
}

.ai-typing {
	display: flex; align-items: center; gap: 4px; padding: 12px 18px;
	.ai-dot {
		width: 6px; height: 6px; border-radius: 50%; background: #00d4ff;
		animation: ai-bounce 1.2s infinite;
		&:nth-child(2) { animation-delay: 0.2s; }
		&:nth-child(3) { animation-delay: 0.4s; }
	}
	@keyframes ai-bounce {
		0%, 80%, 100% { opacity: 0.3; transform: scale(0.8); }
		40% { opacity: 1; transform: scale(1.2); }
	}
}

.ai-chat-footer {
	border-top: 1px solid rgba(0,212,255,0.08);
	background: rgba(0,0,0,0.3);
	padding: 10px 16px 14px;

	.ai-upload-bar {
		display: flex; gap: 6px; margin-bottom: 8px;
		.ai-upload-btn {
			background: rgba(0,212,255,0.08) !important; border: 1px solid rgba(0,212,255,0.15) !important;
			color: rgba(0,212,255,0.7) !important; border-radius: 8px !important; font-size: 11px !important;
			.el-icon { margin-right: 3px; vertical-align: middle; }
			&:hover { background: rgba(0,212,255,0.15) !important; }
		}
	}

	.ai-input-row {
		display: flex; align-items: center; gap: 8px;

		.ai-plus-icon {
			font-size: 24px; color: rgba(255,255,255,0.25); cursor: pointer;
			transition: color 0.2s; flex-shrink: 0;
			&:hover { color: #00d4ff; }
			&.el-icon { font-size: 24px; }
		}

		.ai-input {
			flex: 1; height: 38px; border: 1px solid rgba(0,212,255,0.12);
			border-radius: 20px; padding: 0 16px;
			background: rgba(255,255,255,0.04); color: #e2e8f0;
			font-size: 13px; outline: none; transition: all 0.3s;
			&:focus { border-color: rgba(0,212,255,0.35); background: rgba(0,212,255,0.04); box-shadow: 0 0 12px rgba(0,212,255,0.06); }
			&::placeholder { color: rgba(255,255,255,0.2); }
		}

		.ai-send-btn {
			width: 38px; height: 38px; border-radius: 50%; border: none;
			background: linear-gradient(135deg, #00d4ff, #7c3aed); color: #fff;
			font-size: 16px; cursor: pointer; display: flex; align-items: center;
			justify-content: center; flex-shrink: 0; transition: all 0.25s;
			&:hover { transform: scale(1.08); box-shadow: 0 4px 16px rgba(0,212,255,0.3); }
			.el-icon { font-size: 16px; }
		}
	}
}

.audioAnimation-box {
	transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 68px, 0px);
	-webkit-perspective: 1000px;
	perspective: 1000px;
	transition: 0.3s;
}

.audioAnimation-box1 {
	transform: rotate(0deg) scale(1) skew(0deg, 0deg) translate3d(0px, 0, 0px) !important;
}
</style>
