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

			<!-- 滚动进度条 -->
		<div class="scroll-progress-bar" :style="{width: scrollProgress + '%'}"></div>

		<transition name="page-fade" mode="out-in">
			<router-view id="scrollView" :key="$route.path"></router-view>
		</transition>

		<!-- 回到顶部按钮 -->
		<transition name="fade-up">
			<div class="back-to-top" v-show="showBackTop" @click="scrollToTop">
				<i class="el-icon-arrow-up"></i>
			</div>
		</transition>
			
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
		
		<el-dialog :visible.sync="chatFormVisible" width="780px" :before-close="chatClose" class="ai-chat-dialog" :show-close="false" append-to-body>
			<div class="ai-chat-wrapper">
				<div class="ai-chat-header">
					<div class="ai-header-left">
						<div class="ai-logo-pulse"></div>
						<div class="ai-header-info">
							<span class="ai-header-title">AI 智能问答</span>
							<span class="ai-header-status"><i class="ai-status-dot"></i>{{askType==1?'智能助手在线':'人工客服在线'}}</span>
						</div>
					</div>
					<div class="ai-header-actions">
						<i class="el-icon-close ai-close-btn" @click="chatClose"></i>
					</div>
				</div>

				<div class="ai-chat-body" id="chat-content">
				<div class="ai-welcome" v-if="chatList.length===0">
					<div class="ai-welcome-icon">
						<svg viewBox="0 0 80 80" width="64" height="64"><defs><linearGradient id="ai-grad" x1="0%" y1="0%" x2="100%" y2="100%"><stop offset="0%" stop-color="#00d4ff"/><stop offset="100%" stop-color="#7c3aed"/></linearGradient></defs><circle cx="40" cy="40" r="36" fill="none" stroke="url(#ai-grad)" stroke-width="2" opacity="0.4"/><circle cx="40" cy="40" r="26" fill="none" stroke="url(#ai-grad)" stroke-width="1.5" opacity="0.6"/><circle cx="30" cy="34" r="4" fill="#00d4ff"/><circle cx="50" cy="34" r="4" fill="#7c3aed"/><path d="M28 48 Q40 56 52 48" fill="none" stroke="url(#ai-grad)" stroke-width="2.5" stroke-linecap="round"/></svg>
					</div>
					<p class="ai-welcome-text">你好！我是 AI 智能助手</p>
					<p class="ai-welcome-sub">可以回答您关于技能交易平台的各种问题</p>
					<p class="ai-suggest-title">热门问题，点击直接提问：</p>
					<div class="ai-suggest-list">
						<span class="ai-suggest-tag" v-for="(q, qi) in suggestQuestions" :key="qi" @click="quickAsk(q)">
							<i class="el-icon-chat-dot-round"></i> {{q}}
						</span>
					</div>
				</div>
				<div class="ai-suggest-bar" v-if="chatList.length > 0 && !aiLoading">
					<span class="ai-suggest-bar-label">💡 猜你想问：</span>
					<span class="ai-suggest-mini" v-for="(q, qi) in suggestQuestionsShort" :key="qi" @click="quickAsk(q)">{{q}}</span>
				</div>
					<div v-bind:key="item.id" v-for="item in chatList" class="ai-msg-group">
						<div v-if="item.addtime" class="ai-msg-time">
							<span class="ai-time-badge">{{timeFormat(item.addtime)}}</span>
						</div>
						<div v-if="item.ask" class="ai-msg ai-msg-user">
							<div class="ai-bubble ai-bubble-user">
								<span v-if="item.type==1">{{item.ask}}</span>
								<el-image v-else-if="item.type==2" :src="baseUrl + item.ask" style="width: 180px;height: 180px;border-radius:10px;" fit="cover" :preview-src-list="[baseUrl + item.ask]"></el-image>
								<video v-else-if="item.type==3" :src="baseUrl + item.ask" style="width: 260px;border-radius:10px;" controls></video>
								<el-button v-else-if="item.type==4" size="mini" @click="download(item.ask)"><i class="el-icon-document"></i> 文件预览</el-button>
							</div>
							<img class="ai-avatar" :src="item.uimage?(baseUrl + item.uimage.split(',')[0]):serviceImg" />
						</div>
						<div v-else class="ai-msg ai-msg-bot">
							<img class="ai-avatar ai-avatar-bot" :src="item.uimage?(baseUrl + item.uimage.split(',')[0]):aiImg" />
							<div class="ai-bubble ai-bubble-bot">
								<span v-if="item.type==1">{{item.reply}}</span>
								<el-image v-else-if="item.type==2" :src="baseUrl + item.reply" style="width: 180px;height: 180px;border-radius:10px;" fit="cover" :preview-src-list="[baseUrl + item.reply]"></el-image>
								<video v-else-if="item.type==3" :src="baseUrl + item.reply" style="width: 260px;border-radius:10px;" controls></video>
								<el-button v-else-if="item.type==4" size="mini" @click="download(item.reply)"><i class="el-icon-document"></i> 文件预览</el-button>
							</div>
						</div>
					</div>
					<div v-if="aiLoading" class="ai-msg ai-msg-bot">
						<img class="ai-avatar ai-avatar-bot" :src="aiImg" />
						<div class="ai-bubble ai-bubble-bot ai-typing">
							<span class="ai-typing-text">AI 正在思考</span>
							<span class="ai-dot"></span><span class="ai-dot"></span><span class="ai-dot"></span>
						</div>
					</div>
				</div>

				<div class="ai-chat-footer">
					<div v-if="askShow" class="ai-upload-bar">
						<el-upload :action="uploadUrl" :on-success="uploadSuccess" accept=".jpg,.png" :show-file-list="false">
							<el-button size="mini" class="ai-upload-btn"><i class="el-icon-picture"></i> 图片</el-button>
						</el-upload>
						<el-upload :action="uploadUrl" :on-success="uploadSuccess2" accept=".mp4" :show-file-list="false">
							<el-button size="mini" class="ai-upload-btn"><i class="el-icon-video-camera"></i> 视频</el-button>
						</el-upload>
						<el-upload :action="uploadUrl" :on-success="uploadSuccess3" :show-file-list="false">
							<el-button size="mini" class="ai-upload-btn"><i class="el-icon-folder-opened"></i> 文件</el-button>
						</el-upload>
					</div>
					<div class="ai-input-row">
						<i class="el-icon-circle-plus-outline ai-plus-icon" @click="askShow = !askShow"></i>
						<input class="ai-input" v-model="form.ask" placeholder="请输入您的问题，按 Enter 发送..." @keydown.enter="addChat(null)" />
						<button class="ai-send-btn" :class="{'ai-send-active': form.ask.trim()}" @click="addChat(null)" :title="form.ask.trim()?'发送消息':'请先输入内容'">
							<i class="el-icon-s-promotion"></i>
						</button>
						<div style="position: relative;" v-if="askType==2">
						<span @click="showEmoji=!showEmoji" class="ai-emoji-trigger">😊</span>
						<div v-if="showEmoji" class="ai-emoji-picker">
							<span v-for="(em, ei) in emojiList" :key="ei" @click="addEmoji({native:em})" class="ai-emoji-item">{{em}}</span>
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
	export default {
	components:{
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
			scrollProgress: 0,
			showBackTop: false,
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

		window.addEventListener('scroll', this.onPageScroll, { passive: true });

		setTimeout(()=>{
			this.mySwiper3Timer = new Swiper(".mySwiper3", {"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"pagination":{"el":".swiper-pagination","clickable":true},"slidesPerView":3,"speed":300,"autoplay":{"delay":2500,"disableOnInteraction":false},"effect":"fade"})
		}, 500)

	},
	beforeDestroy() {
		window.removeEventListener('scroll', this.onPageScroll);
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
			if (res.data.code == 0 && res.data.data) {
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
			if (res.data.code == 0 && res.data.data && res.data.data.trim()) {
				this.saveChathelper(res.data.data);
			} else {
				this.saveChathelper(this.getLocalAnswer(ask));
			}
		}).catch(() => {
			this.aiLoading = false
			this.saveChathelper(this.getLocalAnswer(ask));
		});
	},
	getLocalAnswer(ask) {
		const qa = [
			{ keys: ['注册', '账号', '开户'], answer: '注册账号非常简单：\n1. 点击页面右上角的"注册"按钮\n2. 填写用户名、密码和基本信息\n3. 完成注册后即可登录使用\n\n如有问题可切换到"人工服务"联系管理员。' },
			{ keys: ['发布', '技能', '需求'], answer: '发布技能需求的步骤：\n1. 登录账号后，进入"技能市场"页面\n2. 点击"发布技能"按钮\n3. 填写技能名称、详细描述、分类等信息\n4. 上传相关图片（可选）\n5. 提交后等待审核通过即可展示\n\n成为VIP会员后，您发布的技能将获得优先推荐展示。' },
			{ keys: ['交换', '流程', '交易', '怎么换'], answer: '技能交换流程：\n1. 在技能市场浏览或搜索你感兴趣的技能\n2. 点击进入技能详情页，了解对方技能信息\n3. 点击"发起交换"按钮，选择你要用来交换的技能\n4. 双方确认后即可开始线上或线下交流学习\n5. 交换完成后双方互相评价\n\n平台采用协同过滤算法，会智能推荐最适合您的技能。' },
			{ keys: ['信誉', '指数', '评分', '信用'], answer: '关于信誉指数：\n• 信誉指数反映您在平台上的交易信誉度\n• 完成技能交换后获得的好评会提高信誉指数\n• 信誉指数越高，您的技能在推荐列表中排名越靠前\n• 违规操作或获得差评会降低信誉指数\n\n建议：认真完成每次技能交换，积极获取好评来提升信誉。' },
			{ keys: ['联系', '管理员', '客服', '投诉'], answer: '联系管理员的方式：\n1. 点击当前对话框上方的"人工服务"按钮\n2. 在留言反馈页面发布留言，管理员会回复\n3. 通过公告资讯页面查看管理员联系方式\n\n工作时间内管理员会尽快回复您的消息。' },
			{ keys: ['多个', '同时', '几个'], answer: '可以同时发布多个技能！\n• 每位用户可以发布多个不同类别的技能\n• 建议每个技能描述尽量详细，方便其他用户了解\n• VIP会员发布的技能数量不受限制\n• 普通用户也可以发布多个技能' },
			{ keys: ['修改', '个人', '信息', '资料', '头像'], answer: '修改个人信息的步骤：\n1. 点击页面右上角的头像或用户名\n2. 选择"个人中心"\n3. 在个人信息页面点击"编辑"按钮\n4. 修改需要更新的信息（头像、昵称、简介等）\n5. 点击"保存"完成修改' },
			{ keys: ['收费', '免费', '费用', '价格', '多少钱'], answer: '平台收费说明：\n• 基础功能（浏览、搜索技能）免费使用\n• 发起技能交换的基本功能免费\n• VIP会员享有优先推荐、VIP标识等特权\n• VIP会员可在"会员充值"页面了解详细价格\n\n平台致力于提供公平、透明的技能交易环境。' },
			{ keys: ['VIP', '会员', '充值', '特权'], answer: 'VIP会员特权包括：\n🚀 发布技能 - 发布个人技能到市场\n⭐ 优先推荐 - 技能在首页优先展示\n👑 VIP标识 - 专属VIP身份标识\n🤖 智能匹配 - AI智能匹配合适技能\n\n前往"会员充值"页面了解更多详情。' },
			{ keys: ['推荐', '协同过滤', '算法', '匹配'], answer: '平台采用协同过滤推荐算法：\n• 根据您的技能标签和历史交换记录\n• 分析与您相似的用户的偏好\n• 智能推荐最匹配您需求的技能\n• 推荐结果会随着使用逐步优化\n\n使用越多，推荐越精准！' },
			{ keys: ['学习', '资料', '教程', '资源'], answer: '关于学习资料：\n• 平台提供"学习资料"板块\n• 用户可以分享和浏览学习资源\n• 涵盖编程、设计、语言等多个类别\n• 支持在线预览和下载\n\n建议多关注您感兴趣领域的学习资料。' },
			{ keys: ['社区', '论坛', '讨论', '帖子'], answer: '技能社区功能：\n• 在"技能社区"板块可以发帖讨论\n• 分享技能学习心得和经验\n• 与其他用户互动交流\n• 获取行业最新动态\n\n积极参与社区讨论有助于提升您的影响力。' },
			{ keys: ['安全', '隐私', '保护'], answer: '平台安全保障：\n• 用户个人信息严格加密存储\n• 交易过程有完善的评价和举报机制\n• 管理员定期审核内容，杜绝违规信息\n• 如遇到问题可随时联系管理员处理\n\n您的隐私和安全是我们的首要考虑。' },
		];
		const lowerAsk = ask.toLowerCase();
		for (const item of qa) {
			if (item.keys.some(k => lowerAsk.includes(k))) {
				return item.answer;
			}
		}
		return '感谢您的提问！关于"' + ask + '"，以下是一些建议：\n\n1. 您可以在技能市场中搜索相关内容\n2. 浏览学习资料板块获取更多信息\n3. 在技能社区中发帖询问其他用户\n4. 切换到"人工服务"联系管理员获取帮助\n\n如需了解更多，请点击上方的推荐问题。';
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
		onPageScroll() {
			const scrollTop = window.pageYOffset || document.documentElement.scrollTop;
			const docHeight = document.documentElement.scrollHeight - window.innerHeight;
			this.scrollProgress = docHeight > 0 ? (scrollTop / docHeight) * 100 : 0;
			this.showBackTop = scrollTop > 400;
		},
		scrollToTop() {
			window.scrollTo({ top: 0, behavior: 'smooth' });
		},
		menuShowClick4(index){
			this.showType4 = index
		},
	goMenu(path) {
		this.$router.push(path).catch(() => {});
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
					transition: filter 0.3s;
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
					transition: all 0.3s;
				}
				&:hover {
					.top-logo-icon { filter: drop-shadow(0 0 12px rgba(14,165,233,0.6)); }
					span { letter-spacing: 3px; }
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
						transition: all 0.3s cubic-bezier(0.4,0,0.2,1);
						border-radius: 8px;
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
						.title {
							color: $cyan;
							background: rgba(14,165,233,0.06);
							.menu-nav-icon, .icon { color: $cyan; opacity: 0.8; }
						}
						&::after { width: 60%; }
					}

					&.menu-active {
						.title {
							color: #fff;
							font-weight: 600;
							background: rgba(14,165,233,0.1);
							.menu-nav-icon, .icon { color: $cyan; opacity: 1; }
						}
						&::after { width: 80%; box-shadow: 0 0 8px rgba(14,165,233,0.4); }
					}

					&:active .title {
						transform: scale(0.97);
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

/* 路由切换过渡 */
.page-fade-enter-active {
	animation: pageFadeIn 0.35s ease-out;
}
.page-fade-leave-active {
	animation: pageFadeOut 0.2s ease-in;
}
@keyframes pageFadeIn {
	from { opacity: 0; transform: translateY(12px); }
	to { opacity: 1; transform: translateY(0); }
}
@keyframes pageFadeOut {
	from { opacity: 1; transform: translateY(0); }
	to { opacity: 0; transform: translateY(-8px); }
}

/* 滚动进度条 */
.scroll-progress-bar {
	position: fixed;
	top: 0;
	left: 0;
	height: 3px;
	background: linear-gradient(90deg, $cyan, $purple, #e040fb);
	z-index: 9999;
	transition: width 0.15s ease-out;
	border-radius: 0 2px 2px 0;
	box-shadow: 0 0 10px rgba(14,165,233,0.5);
}

/* 回到顶部 */
.back-to-top {
	position: fixed;
	bottom: 40px;
	right: 32px;
	width: 44px;
	height: 44px;
	border-radius: 50%;
	background: linear-gradient(135deg, $cyan, $purple);
	color: #fff;
	display: flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	z-index: 999;
	box-shadow: 0 4px 20px rgba(14,165,233,0.35);
	transition: all 0.3s cubic-bezier(0.4,0,0.2,1);
	font-size: 20px;
	&:hover {
		transform: translateY(-3px) scale(1.08);
		box-shadow: 0 8px 28px rgba(14,165,233,0.45);
	}
	&:active {
		transform: translateY(-1px) scale(0.95);
	}
}
.fade-up-enter-active { transition: all 0.35s cubic-bezier(0.4,0,0.2,1); }
.fade-up-leave-active { transition: all 0.25s ease-in; }
.fade-up-enter, .fade-up-leave-to {
	opacity: 0;
	transform: translateY(16px);
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

<style lang="scss">
.ai-chat-dialog {
	.el-dialog {
		border-radius: 20px !important;
		overflow: hidden;
		background: #111827 !important;
		border: 1.5px solid rgba(99,102,241,0.3) !important;
		box-shadow: 0 25px 60px rgba(0,0,0,0.5), 0 0 40px rgba(99,102,241,0.08) !important;
	}
	.el-dialog__header { display: none !important; }
	.el-dialog__body { padding: 0 !important; background: #111827 !important; }
	.el-dialog__footer { display: none !important; }
}

.ai-chat-wrapper {
	display: flex;
	flex-direction: column;
	height: 680px;
	background: #111827;
}

.ai-chat-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 16px 24px;
	background: linear-gradient(135deg, #1e1b4b, #312e81) !important;
	border-bottom: 2px solid rgba(99,102,241,0.3);

	.ai-header-left {
		display: flex; align-items: center; gap: 14px;
	}
	.ai-logo-pulse {
		width: 44px; height: 44px; border-radius: 50%;
		background: linear-gradient(135deg, #6366f1, #8b5cf6);
		animation: ai-pulse 2s ease-in-out infinite;
		position: relative;
		box-shadow: 0 4px 20px rgba(99,102,241,0.4);
		&::after {
			content: 'AI'; position: absolute; inset: 0;
			display: flex; align-items: center; justify-content: center;
			color: #fff; font-size: 16px; font-weight: 800; letter-spacing: 1px;
		}
	}
	@keyframes ai-pulse {
		0%, 100% { box-shadow: 0 4px 20px rgba(99,102,241,0.4); }
		50% { box-shadow: 0 4px 20px rgba(99,102,241,0.4), 0 0 0 12px rgba(99,102,241,0); }
	}
	.ai-header-info { display: flex; flex-direction: column; gap: 3px; }
	.ai-header-title { font-size: 18px; font-weight: 700; color: #ffffff; letter-spacing: 1px; }
	.ai-header-status { font-size: 13px; color: rgba(255,255,255,0.7); display: flex; align-items: center; gap: 6px; }
	.ai-status-dot {
		width: 8px; height: 8px; border-radius: 50%; background: #22c55e; display: inline-block;
		box-shadow: 0 0 8px #22c55e; animation: ai-dot-glow 2s ease-in-out infinite;
	}
	@keyframes ai-dot-glow {
		0%, 100% { box-shadow: 0 0 4px #22c55e; }
		50% { box-shadow: 0 0 12px #22c55e, 0 0 24px rgba(34,197,94,0.4); }
	}
	.ai-header-actions { display: flex; align-items: center; gap: 12px; }
	.ai-switch-btn {
		background: rgba(255,255,255,0.15) !important; border: 1.5px solid rgba(255,255,255,0.25) !important;
		color: #fff !important; border-radius: 18px !important; font-size: 13px !important; padding: 7px 16px !important;
		font-weight: 500 !important;
		i { margin-right: 4px; }
		&:hover { background: rgba(255,255,255,0.25) !important; transform: translateY(-1px); }
	}
	.ai-close-btn {
		font-size: 22px; color: rgba(255,255,255,0.5); cursor: pointer; transition: all 0.2s;
		&:hover { color: #fff; transform: rotate(90deg); }
	}
}

.ai-chat-body {
	flex: 1; overflow-y: auto; padding: 20px 24px;
	background: #111827 !important;

	&::-webkit-scrollbar { width: 6px; }
	&::-webkit-scrollbar-track { background: transparent; }
	&::-webkit-scrollbar-thumb { background: rgba(99,102,241,0.3); border-radius: 10px; }
	&::-webkit-scrollbar-thumb:hover { background: rgba(99,102,241,0.5); }
}

.ai-welcome {
	display: flex; flex-direction: column; align-items: center; justify-content: center;
	padding: 40px 20px 20px; animation: ai-fade-in 0.5s ease;
	.ai-welcome-icon { margin-bottom: 16px; filter: drop-shadow(0 4px 20px rgba(99,102,241,0.4)); }
	.ai-welcome-text { color: #ffffff !important; font-size: 22px; font-weight: 700; margin-bottom: 6px; }
	.ai-welcome-sub { color: #a5b4fc !important; font-size: 15px; margin-bottom: 28px; }
}
@keyframes ai-fade-in {
	from { opacity: 0; transform: translateY(10px); }
	to { opacity: 1; transform: translateY(0); }
}
.ai-suggest-title {
	color: #c7d2fe !important; font-size: 14px; margin-bottom: 16px; font-weight: 500;
}
.ai-suggest-list {
	display: flex; flex-wrap: wrap; gap: 10px; justify-content: center; max-width: 600px;
}
.ai-suggest-tag {
	display: inline-flex; align-items: center; gap: 6px; padding: 10px 20px; border-radius: 22px; font-size: 14px;
	background: rgba(99,102,241,0.15) !important; border: 1.5px solid rgba(99,102,241,0.35) !important;
	color: #c7d2fe !important; cursor: pointer; transition: all 0.25s; font-weight: 500;
	i { font-size: 14px; opacity: 0.8; }
	&:hover { background: rgba(99,102,241,0.3) !important; border-color: rgba(99,102,241,0.6) !important; color: #fff !important; transform: translateY(-2px); box-shadow: 0 6px 20px rgba(99,102,241,0.25); }
}
.ai-suggest-bar {
	display: flex; align-items: center; gap: 8px; padding: 10px 0; flex-wrap: wrap;
	margin-bottom: 10px; border-bottom: 1px solid rgba(255,255,255,0.08);
}
.ai-suggest-bar-label {
	font-size: 13px; color: #a5b4fc !important; white-space: nowrap; font-weight: 500;
}
.ai-suggest-mini {
	padding: 5px 14px; border-radius: 14px; font-size: 13px;
	background: rgba(139,92,246,0.15) !important; border: 1px solid rgba(139,92,246,0.3) !important;
	color: #c4b5fd !important; cursor: pointer; transition: all 0.2s; font-weight: 500;
	&:hover { background: rgba(139,92,246,0.35) !important; color: #fff !important; transform: translateY(-1px); }
}

.ai-msg-group { margin-bottom: 6px; animation: ai-msg-in 0.35s ease; }
@keyframes ai-msg-in {
	from { opacity: 0; transform: translateY(10px); }
	to { opacity: 1; transform: translateY(0); }
}

.ai-msg-time {
	text-align: center; padding: 10px 0;
	.ai-time-badge {
		display: inline-block; font-size: 12px; color: #94a3b8 !important;
		background: rgba(255,255,255,0.06); padding: 3px 14px; border-radius: 12px;
		letter-spacing: 0.5px;
	}
}

.ai-msg { display: flex; align-items: flex-end; gap: 12px; margin-bottom: 16px; }
.ai-msg-user { flex-direction: row-reverse; }

.ai-avatar {
	width: 40px; height: 40px; border-radius: 50%; flex-shrink: 0; object-fit: cover;
	border: 2.5px solid rgba(255,255,255,0.15);
}
.ai-avatar-bot {
	box-shadow: 0 0 20px rgba(99,102,241,0.3);
	border: 2.5px solid rgba(99,102,241,0.4) !important;
}

.ai-bubble {
	max-width: 72%; padding: 14px 20px; border-radius: 20px;
	font-size: 15px !important; line-height: 1.75; word-break: break-word; white-space: pre-line;
}
.ai-bubble-user {
	background: linear-gradient(135deg, #7c3aed, #6d28d9) !important;
	color: #ffffff !important; border-bottom-right-radius: 6px;
	box-shadow: 0 4px 20px rgba(124,58,237,0.35);
	font-weight: 500;
}
.ai-bubble-bot {
	background: #1e293b !important; color: #e2e8f0 !important;
	border: 1.5px solid rgba(99,102,241,0.2) !important; border-bottom-left-radius: 6px;
	box-shadow: 0 2px 16px rgba(0,0,0,0.25);
	font-weight: 400;
}

.ai-typing {
	display: flex; align-items: center; gap: 6px; padding: 14px 20px;
	.ai-typing-text {
		font-size: 14px; color: #a5b4fc !important; margin-right: 8px; font-weight: 500;
	}
	.ai-dot {
		width: 8px; height: 8px; border-radius: 50%; background: #6366f1 !important;
		animation: ai-bounce 1.4s infinite;
		&:nth-child(3) { animation-delay: 0.2s; }
		&:nth-child(4) { animation-delay: 0.4s; }
	}
	@keyframes ai-bounce {
		0%, 80%, 100% { opacity: 0.2; transform: scale(0.6); }
		40% { opacity: 1; transform: scale(1.4); }
	}
}

.ai-chat-footer {
	border-top: 2px solid rgba(99,102,241,0.2);
	background: #0f172a !important;
	padding: 14px 20px 18px;

	.ai-upload-bar {
		display: flex; gap: 8px; margin-bottom: 12px; padding: 8px 0;
		border-bottom: 1px solid rgba(255,255,255,0.06);
		.ai-upload-btn {
			background: rgba(99,102,241,0.15) !important; border: 1.5px solid rgba(99,102,241,0.3) !important;
			color: #a5b4fc !important; border-radius: 12px !important; font-size: 13px !important;
			padding: 7px 16px !important; font-weight: 500 !important;
			.el-icon { margin-right: 4px; }
			&:hover { background: rgba(99,102,241,0.3) !important; color: #fff !important; }
		}
	}

	.ai-input-row {
		display: flex; align-items: center; gap: 10px;

		.ai-plus-icon {
			font-size: 28px; color: rgba(255,255,255,0.4); cursor: pointer;
			transition: all 0.25s; flex-shrink: 0;
			&:hover { color: #818cf8; transform: rotate(90deg); }
		}

		.ai-input {
			flex: 1; height: 46px; border: 2px solid rgba(99,102,241,0.2);
			border-radius: 23px; padding: 0 20px;
			background: rgba(255,255,255,0.07) !important; color: #ffffff !important;
			font-size: 15px !important; outline: none; transition: all 0.3s;
			&:focus { border-color: rgba(99,102,241,0.5); background: rgba(99,102,241,0.08) !important; box-shadow: 0 0 24px rgba(99,102,241,0.12); }
			&::placeholder { color: rgba(255,255,255,0.35) !important; font-size: 14px; }
		}

		.ai-send-btn {
			width: 46px; height: 46px; border-radius: 50%; border: none;
			background: rgba(255,255,255,0.1); color: rgba(255,255,255,0.35);
			font-size: 20px; cursor: pointer; display: flex; align-items: center;
			justify-content: center; flex-shrink: 0; transition: all 0.3s;
			&.ai-send-active {
				background: linear-gradient(135deg, #6366f1, #8b5cf6) !important; color: #fff !important;
				box-shadow: 0 4px 24px rgba(99,102,241,0.4);
				&:hover { transform: scale(1.1); box-shadow: 0 6px 28px rgba(99,102,241,0.5); }
			}
		}

		.ai-emoji-trigger {
			font-size: 28px; cursor: pointer; transition: transform 0.2s; user-select: none;
			&:hover { transform: scale(1.2); }
		}

		.ai-emoji-picker {
			position: absolute; bottom: 48px; left: -120px;
			background: #1e293b !important; border: 2px solid rgba(99,102,241,0.3) !important;
			border-radius: 16px; padding: 12px;
			box-shadow: 0 8px 40px rgba(0,0,0,0.5);
			display: flex; flex-wrap: wrap; width: 310px; max-height: 230px; overflow-y: auto; z-index: 9999;
			&::-webkit-scrollbar { width: 5px; }
			&::-webkit-scrollbar-thumb { background: rgba(99,102,241,0.3); border-radius: 5px; }
		}

		.ai-emoji-item {
			cursor: pointer; font-size: 26px; padding: 5px 6px; display: inline-block;
			border-radius: 8px; transition: all 0.15s;
			&:hover { background: rgba(99,102,241,0.2); transform: scale(1.2); }
		}
	}
}
</style>
