<template>
	<div>
		<div class="breadcrumb-preview">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a @click="backClick">{{item.name}}</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item3"><a href="javascript:void(0);">详情</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="back_box">
			<el-button class="backBtn" size="mini" @click="backClick">
				<span class="icon iconfont icon-fanhui01"></span>
				<span class="text">返回</span>
			</el-button>
		</div>
		<div class="detail-preview">
			<div class="attr">
				<div class="info">
					<div class="title-item">
						<div class="detail-title">
							{{detail.yonghuxingming}}
						</div>
					</div>
					<div class="item">
						<div class="lable">性别</div>
						<div class="text "  >{{detail.xingbie}}</div>
					</div>
					<div class="item">
						<div class="lable">身份证</div>
						<div class="text "  >{{detail.idcard}}</div>
					</div>
					<div class="item">
						<div class="lable">年龄</div>
						<div class="text "  >{{detail.age}}</div>
					</div>
					<div class="item">
						<div class="lable">信誉指数</div>
						<div class="text "  >{{detail.xinyuzhishu}}</div>
					</div>
					<div class="item">
						<div class="lable">手机号</div>
						<div class="text "  >{{detail.shoujihao}}</div>
					</div>
					<div class="item" v-if="centerType">
						<div class="lable">审核状态</div>
						<div class="text">{{detail.sfsh=='是'?'通过':(detail.sfsh=='否'?'不通过':'待审核')}}</div>
					</div>
					<div class="item" v-if="centerType">
						<div class="lable">审核回复</div>
						<div class="text">{{detail.shhf}}</div>
					</div>
					<div class="btn_box">
						<el-button class="editBtn" v-if="btnAuth('yonghu','修改')&&detail.sfsh!='是'&&detail.sfsh!='否' " @click="editClick">修改</el-button>
						<el-button class="delBtn" v-if="btnAuth('yonghu','删除')" @click="delClick">删除</el-button>
						<el-button class="chatBtn" v-if="btnAuth('yonghu','私聊')&&detail.id!=mid" @click="chatClick">私聊</el-button>
						<el-button class="auditBtn" v-if="btnAuth('yonghu','审核') && (detail.sfsh=='待审核'||!detail.sfsh)" @click="onSH" type="warning">审核</el-button>
						<el-button class="editBtn" @click="allchapterClick()" type="warning" v-if="btnAuth('yonghu','章节管理')">章节管理</el-button>
					</div>
				</div>
			</div>
		
			<div class="detail-swpier2" v-if="detailBanner.length">
				<div class="swiper21">
					<div class="swiper-container mySwiper21">
						<div class="swiper-wrapper">
							<div class="swiper-slide" v-for="item in detailBanner" :key="item.id">
								<div class="swiper-item">
									<img v-if="item.substr(0,4)=='http'" :src="item" class="image">
									<img v-else :src="baseUrl + item" class="image">
								</div>
							</div>
						</div>
						<div class="swiper-button-prev">
							<span class="icon iconfont icon-jiantou39"></span>
						</div>
						<div class="swiper-button-next">
							<span class="icon iconfont icon-jiantou18"></span>
						</div>
					</div>
				</div>
				<div class="swiper22">
					<div class="swiper-container mySwiper22">
						<div class="swiper-wrapper">
							<div class="swiper-slide" v-for="item in detailBanner" :key="item.id">
								<div class="swiper-item">
									<img v-if="item.substr(0,4)=='http'" :src="item" class="image">
									<img v-else :src="baseUrl + item" class="image">
								</div>
							</div>
						</div>
						<div class="swiper-button-prev">
							<span class="icon iconfont icon-jiantou39"></span>
						</div>
						<div class="swiper-button-next">
							<span class="icon iconfont icon-jiantou18"></span>
						</div>
					</div>
				</div>
			</div>



			<el-tabs id="tabsBox" class="detail-tabs" v-model="activeName" type="border-card" v-if="tabsNum>0" >
			</el-tabs>
			<el-dialog title="审核" v-model="sfshVisiable" width="50%">
				<el-form ref="shForm" :model="shForm" label-width="80px">
					<el-form-item label="审核状态">
						<el-select v-model="shForm.sfsh" placeholder="审核状态">
							<el-option label="通过" value="是"></el-option>
							<el-option label="不通过" value="否"></el-option>
							<el-option label="待审核" value="待审核"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item label="内容">
						<el-input type="textarea" :rows="8" v-model="shForm.shhf"></el-input>
					</el-form-item>
				</el-form>
				<template #footer>
					<div class="dialog-footer">
						<el-button @click="sfshVisiable = false">取 消</el-button>
						<el-button type="primary" @click="shHandler">确 定</el-button>
					</div>
				</template>
			</el-dialog>

		</div>
		<el-dialog v-model="chatVisible" @close="clearChat" :title="fname">
			<div class="chat-content" id="chat-content">
				<div v-bind:key="item.id" v-for="item in chatList">
					<div v-if="item.addtime" class="addtime">{{timeFormat(item.addtime)}}</div>
					<div v-if="item.uid==mid" class="right-content">
						<el-alert v-if="item.format==1" class="text-content" :title="item.content" :closable="false"
							type="warning"></el-alert>
						<el-image v-else-if="item.format==2" fit="cover" :src="item.content?baseUrl + item.content:''" style="width: 100px;height: 100px;" :preview-src-list="[item.content?baseUrl + item.content:'']"></el-image>
						<video v-else-if="item.format==3" :src="baseUrl + item.content" style="width: 280px;" controls></video>
						<el-button v-else-if="item.format==4" type="primary" size="mini" @click="chatDownload(item.content)">文件预览</el-button>
						<audio v-else-if="item.format==5" :src="baseUrl + item.content" controls></audio>
						<img :src="mpic?baseUrl + mpic:defaultAvatar" alt="" style="width: 30px;border-radius: 50%;height: 30px;margin: 0 0 0 10px;" />
					</div>
					<div v-else class="left-content">
						<img :src="fpic?baseUrl + fpic:defaultAvatar" alt="" style="width: 30px;border-radius: 50%;height: 30px;margin: 0 10px 0 0;" />
						<el-alert v-if="item.format==1" class="text-content" :title="item.content" :closable="false"
							type="success"></el-alert>
						<el-image v-else-if="item.format==2" fit="cover" :src="item.content?baseUrl + item.content:''" style="width: 100px;height: 100px;" :preview-src-list="[item.content?baseUrl + item.content:'']"></el-image>
						<video v-else-if="item.format==3" :src="baseUrl + item.content" style="width: 280px;" controls></video>
						<audio v-else-if="item.format==5" :src="baseUrl + item.content" controls></audio>
						<el-button v-else-if="item.format==4" type="primary" size="mini" @click="chatDownload(item.content)">文件预览</el-button>
					</div>
					<div class="clear-float"></div>
				</div>
			</div>
			<template #footer>
				<div class="dialog-footer">
				<div style="display: flex;align-items: center">
					<el-input size="small" @keydown.enter="addChat(null)" v-model="chatForm.content" placeholder="请输入内容" style="width: calc(100% - 240px);float: left;">
					</el-input>
					<el-button size="small" :disabled="chatForm.content?false:true" type="primary" @click="addChat(null)">发送</el-button>
					<el-button
						type="warning" 
						size="small"
						:icon="isRecording ? VideoPause : Microphone"
						@click="toggleRecord"
						:class="{'voice-record-btn': true, 'voice-recording': isRecording}"
						style="margin: 0 0 0 6px;">
						{{ isRecording ? `录音中 ${recordDuration}秒 (点击停止)` : '点击录音' }}
					</el-button>
					<el-upload style="display: inline-block;margin: 0 0 0 6px;" class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess" accept=".jpg,.png"
						:show-file-list="false">
						<el-button size="small" type="success">上传图片</el-button>
					</el-upload>
					<el-upload style="display: inline-block;margin: 0 0 0 6px;" class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess2" accept=".mp4"
						:show-file-list="false">
						<el-button size="small" type="success">上传视频</el-button>
					</el-upload>
					<el-upload style="display: inline-block;margin: 0 0 0 6px;" class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess3"
						:show-file-list="false">
						<el-button size="small" type="success">上传文件</el-button>
					</el-upload>
				</div>
				</div>
			</template>
		</el-dialog>
	</div>
</template>

<script>
	import axios from 'axios'
	import { Loading, Microphone, VideoPause } from '@element-plus/icons-vue'
	import defaultAvatar from '@/assets/avator.png'
	import Swiper from "swiper";
	import timeMethod from '@/common/timeMethod'
	import {
		WebsocketMixin
	} from '@/mixins/WebsocketMixin'
	export default {
		components: { Loading, Microphone, VideoPause },
		mixins: [WebsocketMixin],
		//数据集合
		data() {
			return {
				defaultAvatar,
				tablename: 'yonghu',
				baseUrl: '',
				breadcrumbItem: [
					{
						name: '用户'
					}
				],
				title: '',
				detailBanner: [],
				userid: Number(localStorage.getItem('frontUserid')),
				id: 0,
				detail: {},
				tabsNum: 0,
				activeName: '1',
				buynumber: 1,
				sfshVisiable: false,
				shForm: {
					sfsh: '是',
					shhf: ''
				},
				centerType: false,
				storeupType: false,
				mid: Number(localStorage.getItem('frontUserid')),
				chatVisible: false,
				chatList: [],
				chatForm: {
					content: ''
				},
				uploadUrl: this.$config.baseUrl + 'file/upload',
				fpic: '',
				fname: '',
				fid: '',
				ftableName: 'yonghu',
				mpic: localStorage.getItem('frontHeadportrait'),
				// 语音聊天相关
				isRecording: false,
				mediaRecorder: null,
				audioStream: null,
				audioChunks: [],
				recordDuration: 0,
				recordTimer: null,
				sensitiveWordsArr: [],
			}
		},
		created() {
			if(this.$route.query.centerType&&this.$route.query.centerType!=0) {
				this.centerType = true
			}
			if(this.$route.query.storeupType&&this.$route.query.storeupType!=0) {
				this.storeupType = true
			}
			this.init();
		},
		mounted() {
			setTimeout(()=>{
				let mySwiper22 = new Swiper(".mySwiper22", {"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"freeMode":true,"watchSlidesVisibility":true,"watchSlidesProgress":true,"loopedSlides":6,"slidesPerView":3,"spaceBetween":10})
				let option21 = {...{"navigation":{"nextEl":".swiper-button-next","prevEl":".swiper-button-prev"},"loopedSlides":5,"spaceBetween":10}}
				option21.thumbs = {
					swiper: mySwiper22
				}
				new Swiper(".mySwiper21", option21)
			},100)
		},
		computed: {
			username() {
				return localStorage.getItem('username')
			}
		},
		//方法集合
		methods: {
			init() {
				this.id = this.$route.query.id
				this.baseUrl = this.$config.baseUrl;
				this.$http.get(this.tablename + '/detail/'  + this.id, {}).then(async res => {
					if (res.data.code == 0) {
						this.detail = res.data.data;
						// 获取数据库敏感词
						this.getSensitiveWords()
						this.title = this.detail.yonghuxingming;
						if(this.detail.touxiang) {
							this.detailBanner = this.detail.touxiang.split(",w").length>1?[this.detail.touxiang]:this.detail.touxiang.split(',');
						}
						if(res.data.data.touxiang){
							this.fpic = res.data.data.touxiang.split(',')[0]
						}else if(res.data.data.headportrait){
							this.fpic = res.data.data.headportrait.split(',')[0]
						}
						if(res.data.data.yonghuxingming){
							this.fname = res.data.data.yonghuxingming
						} else if(res.data.data.yonghuzhanghao){
							this.fname = res.data.data.yonghuzhanghao
						}
						this.fid = res.data.data.id
						this.$forceUpdate();
						if(localStorage.getItem('frontToken')){
						}

					}
				});
			},
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			onSH() {
				this.sfshVisiable = true;
			},
			shHandler(){
				this.$confirm(`确定操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(async () => {
					this.detail.sfsh = this.shForm.sfsh;
					this.detail.shhf = this.shForm.shhf;
					await this.$http.post(`${this.tablename}/update`, this.detail).then(async res => {
						if (res.data.code == 0) {
							this.sfshVisiable = false;
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
							});
						}
					});
				});
			},
			curChange(page) {
				this.getDiscussList(page);
			},
			prevClick(page) {
				this.getDiscussList(page);
			},
			nextClick(page) {
				this.getDiscussList(page);
			},
			sizeChange(size){
				this.pageSize = size
				this.getDiscussList(1);
			},
			// 返回按钮
			backClick(){
				if(this.storeupType){
					history.back()
				}else{
					let params = {}
					if(this.centerType){
						params.centerType = 1
					}
					this.$router.push({path: '/main/yonghu', query: params});
				}
			},
			// 下载
			download(file ){
				if(!file) {
					this.$message({
						type: 'error',
						message: '文件不存在',
						duration: 1500,
					});
					return;
				}
				let arr = file.replace(new RegExp('upload/', "g"), "")
				axios.get(this.baseUrl + 'file/download?fileName=' + arr, {
					headers: {
						token: localStorage.getItem("frontToken")
					},
					responseType: "blob"
				}).then(({
					data
				}) => {
					const binaryData = [];
					binaryData.push(data);
					const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
						type: 'application/pdf;chartset=UTF-8'
					}))
					const a = document.createElement('a')
					a.href = objectUrl
					a.download = arr
					// a.click()
					// 下面这个写法兼容火狐
					a.dispatchEvent(new MouseEvent('click', {
						bubbles: true,
						cancelable: true,
						view: window
					}))
					window.URL.revokeObjectURL(data)
				},err=>{
					axios.get((location.href.split(this.$config.name).length>1 ? location.href.split(this.$config.name)[0] :'') + this.$config.name + 'file/download?fileName=' + arr, {
						headers: {
							token: localStorage.getItem("frontToken")
						},
						responseType: "blob"
					}).then(({
						data
					}) => {
						const binaryData = [];
						binaryData.push(data);
						const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
							type: 'application/pdf;chartset=UTF-8'
						}))
						const a = document.createElement('a')
						a.href = objectUrl
						a.download = arr
						// a.click()
						// 下面这个写法兼容火狐
						a.dispatchEvent(new MouseEvent('click', {
							bubbles: true,
							cancelable: true,
							view: window
						}))
						window.URL.revokeObjectURL(data)
					})
				})
			},


			// 权限判断
			btnAuth(tableName,key){
				if(this.centerType){
					return this.isBackAuth(tableName,key)
				}else{
					return this.isAuth(tableName,key)
				}
			},
			// 修改
			editClick(){
				this.$router.push(`/main/yonghuAdd?type=edit&&id=${this.detail.id}`);
			},
			// 删除
			async delClick(){
				await this.$confirm('是否删除此用户？') .then(_ => {
					this.$http.post('yonghu/delete', [this.detail.id]).then(async res => {
						if (res.data.code == 0) {
							this.$message({
								type: 'success',
								message: '删除成功!',
								duration: 1500,
								onClose: () => {
									history.back()
								}
							});
						}
					});
				}).catch(_ => {});
			},
			chatClick() {
				this.initWebSocket(this.fid)
				this.getChatList()
				this.chatVisible = true
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
						fid: this.fid
					}
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.chatList = this.formatMessages(res.data.data.list)
						let div = document.getElementsByClassName('chat-content')[0]
						setTimeout(() => {
							if (div)
								div.scrollTop = div.scrollHeight
						}, 0)
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
				this.websocketOnclose();
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
					spinner: Loading,
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
					this.addChat('upload/' + res.file,2)
				}
			},
			uploadSuccess2(res) {
				if (res.code == 0) {
					this.addChat('upload/' + res.file,3)
				}
			},
			uploadSuccess3(res) {
				if (res.code == 0) {
					this.addChat('upload/' + res.file,4)
				}
			},
			addChat(ask=null,type=1) {
				this.$http.get('friend/page', {
					params: {
						uid: Number(localStorage.getItem('frontUserid')),
						fid: this.fid,
					}
				}).then(obj => {
					if (obj.data && obj.data.code == 0) {
						if (!obj.data.data.list.length) {
							this.$http.post('friend/add', {
								uid: Number(localStorage.getItem('frontUserid')),
								fid: this.fid,
								name: this.fname,
								picture: this.fpic,
								type: 2,
								tablename: this.ftableName,
							}).then(res => {
								this.$http.post('friend/add', {
									uid: this.fid,
									fid: Number(localStorage.getItem('frontUserid')),
									type: 2,
									tablename: localStorage.getItem('frontSessionTable'),
									name: localStorage.getItem('username'),
									picture: this.mpic,
								}).then(res1 => {
			
								})
							})
						}
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
							fid: this.fid,
							content: ask?ask:this.chatForm.content,
							format: type
						}).then(res2 => {
							this.websocketSend(ask?ask:this.chatForm.content)
							this.chatForm = {
								content: ''
							}
							this.getChatList()
						})
					}
				})
			},
			chatDownload(url){
				if(!url){
					return false
				}
				window.open((location.href.split(this.$config.name).length>1 ? location.href.split(this.$config.name)[0] + this.$config.name + '/' + url :this.$config.baseUrl + url))
			},
			allchapterClick (){
				let params = {
					refid: this.detail.id
				}
				if(this.centerType){
					params.centerType = 1
				}
				this.$router.push({path: '/main/chapteryonghu', query: params});
			},
			// 获取uuid
			getUUID() {
				return new Date().getTime();
			},
		},
		components: {
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.detail-preview {
		padding: 0 10%;
		margin: 10px auto;
		display: flex;
		width: 100%;
		position: relative;
		flex-wrap: wrap;
		.attr {
			padding: 0px;
			margin: 0 0 20px;
			background: none;
			flex: 1;
			display: flex;
			position: relative;
			order: 2;
			.info {
				border-radius: 8px;
				padding: 10px;
				margin: 0;
				background: #fff;
				flex: 1;
				.title-item {
					padding: 10px;
					margin: 0 0 0px 0;
					background: none;
					display: flex;
					border-color: rgba(149, 119, 65,0.3);
					border-width: 0 0 1px;
					justify-content: space-between;
					align-items: center;
					border-style: solid;
					.detail-title {
						padding: 0;
						color: #333;
						font-weight: 600;
						font-size: 18px;
						border-color: #009899;
						border-width: 0 0 0 0px;
						line-height: 1;
						border-style: solid;
					}
				}
				.item {
					padding: 5px 10px;
					margin: 0 0 0px 0;
					background: none;
					display: flex;
					border-color: rgba(149, 119, 65,0.3);
					border-width: 0 0 1px;
					justify-content: spaceBetween;
					border-style: solid;
					.lable {
						padding: 0 10px 0 0;
						color: #333;
						white-space: nowrap;
						font-weight: 500;
						width: auto;
						font-size: 16px;
						line-height: 24px;
						text-align: right;
						height: auto;
					}
					.count-down {
						padding: 0 10px;
						color: #666;
						word-break: break-all;
						flex: 1;
						font-size: 14px;
						line-height: 24px;
						height: auto;
					}
					.text {
						padding: 0 10px;
						color: #666;
						word-break: break-all;
						flex: 1;
						font-size: 14px;
						line-height: 24px;
						height: auto;
					}
					.bold {
						font-weight: bold;
					}
					.link {
						cursor: pointer;
						text-decoration: underline;
					}
				}
				.item-price {
					padding: 10px;
					margin: 0 0 10px 0;
					background: #f5f5f5;
					display: flex;
					justify-content: spaceBetween;
					.lable {
						padding: 0 10px;
						color: #999;
						width: 102px;
						font-size: 14px;
						line-height: 40px;
						text-align: right;
						height: 40px;
					}
					.text {
						padding: 0 10px;
						color: #666;
						flex: 1;
						font-size: 14px;
						line-height: 40px;
						height: 40px;
					}
				}
				.btn_box {
					padding: 10px 0;
					display: flex;
					flex-wrap: wrap;
				}
				.editBtn {
					border: 1px solid #0ea5e9;
					cursor: pointer;
					border-radius: 4px;
					padding: 0 10px;
					margin: 0 0 0 10px;
					outline: none;
					color: #fff;
					background: #0ea5e9;
					width: auto;
					font-size: 14px;
					line-height: 40px;
					height: 40px;
				}
				.editBtn:hover {
					opacity: 0.7;
				}
				.delBtn {
					border: 1px solid #0ea5e9;
					cursor: pointer;
					border-radius: 4px;
					padding: 0 10px;
					margin: 0 0 0 10px;
					outline: none;
					color: #fff;
					background: #0ea5e9;
					width: auto;
					font-size: 14px;
					line-height: 40px;
					height: 40px;
				}
				.delBtn:hover {
					opacity: 0.7;
				}
				.chatBtn {
					border: 1px solid #0ea5e9;
					cursor: pointer;
					border-radius: 4px;
					padding: 0 10px;
					margin: 0 0 0 10px;
					outline: none;
					color: #fff;
					background: #0ea5e9;
					width: auto;
					font-size: 14px;
					line-height: 40px;
					height: 40px;
				}
				.chatBtn:hover {
					opacity: 0.7;
				}
				.auditBtn {
					border: 1px solid #0ea5e9;
					cursor: pointer;
					border-radius: 4px;
					padding: 0 10px;
					margin: 0 0 0 10px;
					outline: none;
					color: #fff;
					background: #0ea5e9;
					width: auto;
					font-size: 14px;
					line-height: 40px;
					height: 40px;
				}
				.auditBtn:hover {
					opacity: 0.7;
				}
			}
		}
		.detail-swpier2 {
			border-radius: 80px;
			padding: 80px;
			margin: 0 0 20px 0;
			overflow: hidden;
			background: #F6F6F6;
			width: 100%;
			height: auto;
			order: 1;
			.swiper21 {
				width: 100%;
				height: auto;
				.swiper-button-prev:after {
					display:none;
				}
				.swiper-button-next:after {
					display:none;
				}
				.swiper-item {
					width: 100%;
					height: auto;
					img {
						border: 1px solid #eee;
						background: #fff;
						object-fit: contain;
						width: 100%;
						height: 600px;
					}
				}
				.swiper-button-prev {
					margin: -12px 0 0;
					top: 50%;
					width: 24px;
					height: 24px;
					.icon {
						color: #fff;
						width: 24px;
						font-size: 24px;
						height: 24px;
					}
				}
				.swiper-button-next {
					margin: -12px 0 0;
					top: 50%;
					width: 24px;
					height: 24px;
					.icon {
						color: #fff;
						width: 24px;
						font-size: 24px;
						height: 24px;
					}
				}
			}
			.swiper22 {
				width: 100%;
				height: auto;
				
				.swiper-button-prev:after {
					display:none;
				}
				.swiper-button-next:after {
					display:none;
				}
				
				.swiper-item {
					width: 100%;
					opacity: 0.4;
					height: auto;
					img {
						border: 1px solid #eee;
						background: #fff;
						object-fit: contain;
						width: 100%;
						border-width: 0 1px 1px;
						height: 100px;
					}
				}
				.swiper-slide.swiper-slide-thumb-active div {
					opacity: 1;
				}
				.swiper-button-prev {
					margin: -7px 0 0;
					top: 50%;
					width: 14px;
					height: 14px;
					.icon {
						color: #fff;
						width: 14px;
						font-size: 14px;
						height: 14px;
					}
				}
				.swiper-button-next {
					margin: -7px 0 0;
					top: 50%;
					width: 14px;
					height: 14px;
					.icon {
						color: #fff;
						width: 14px;
						font-size: 14px;
						height: 14px;
					}
				}
			}
		}
		.detail-tabs {
			border: 0px solid #DCDFE6;
			box-shadow: none;
			padding: 0;
			margin: 20px auto;
			background: none;
			width: 100%;
			order: 5;
			& ::v-deep  .el-tabs__header .el-tabs__nav-wrap {
				margin-bottom: 0;
			}
			::v-deep  .el-tabs__header {
				padding: 8px 20px;
				background: none;
				display: block;
				width: 100%;
				border-color: #ddd;
				border-width: 0 0 1px;
				line-height: 1.5;
				border-style: solid;
				text-align: center;
			}
			
			::v-deep  .el-tabs__header .el-tabs__item {
				border: 0;
				padding: 0 20px;
				margin: 0 30px 0 0;
				color: #000;
				font-weight: 500;
				display: inline-block;
				font-size: 18px;
				border-color: #707070;
				line-height: 40px;
				background: none;
				border-width: 0 0 0 1px;
				position: relative;
				border-style: solid;
				list-style: none;
				height: 40px;
			}
			
			::v-deep  .el-tabs__header .el-tabs__item:hover {
				color: #000;
				background: url(http://codegen.caihongy.cn/20251210/7b0aa80845f149919c14496896e4ac8b.png) no-repeat left center / 4px auto;
				border-color: #fff;
			}
			
			::v-deep  .el-tabs__header .el-tabs__item.is-active {
				padding: 0 20px;
				margin: 0 30px 0 0;
				color: #000;
				background: url(http://codegen.caihongy.cn/20251210/7b0aa80845f149919c14496896e4ac8b.png) no-repeat left center / 4px auto;
				font-size: 18px;
				border-color: #fff;
				border-width: 0;
				border-style: solid;
			}
			
			::v-deep  .el-tabs__content {
				padding: 15px 5px;
				color: #333;
				background: #fff;
				width: 100%;
				font-size: 16px;
			}
		}
	}
	.chat-content {
		padding-bottom: 20px;
		width: 100%;
		margin-bottom: 10px;
		max-height: 300px;
		height: 300px;
		overflow-y: scroll;
		border: 1px solid #eeeeee;
		background: #fff;
	
		.addtime {
			width: 100%;
			text-align: center;
			font-size: 12px;
		}
		
		.left-content {
			float: left;
			margin-bottom: 10px;
			padding: 10px;
			max-width: 80%;
			display: flex;
			align-items: center;
		}
	
		.right-content {
			float: right;
			margin-bottom: 10px;
			padding: 10px;
			max-width: 80%;
			display: flex;
			align-items: center;
		}
	}
	
	.clear-float {
		clear: both;
	}
</style>
