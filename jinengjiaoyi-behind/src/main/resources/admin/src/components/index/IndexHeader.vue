<template>
	<div class="navbar">
		<div class="title">
			<span class="title-name">{{this.$project.projectName}}</span>
		</div>
		<el-dropdown class="dropdown-box" @command="handleCommand" trigger="click">
			<div class="el-dropdown-link">
				<el-image v-if="avatar" :src="avatar?this.$base.url + avatar : require('@/assets/img/avator.png')" fit="cover"></el-image>
				<span class="label">欢迎</span>
				<span class="nickname">{{this.$storage.get('adminName')}}</span>
				<span class="icon iconfont icon-xiala"></span>
			</div>
			<template #dropdown>
			<el-dropdown-menu class="top-el-dropdown-menu">
				<el-dropdown-item class="item2" :command="'center'">
					<span class="icon iconfont icon-touxiang09"></span>
					个人中心
				</el-dropdown-item>
				<el-dropdown-item v-if="changeHasChat()" class="item6" :command="'chat'">
					<span class="icon iconfont icon-huifu18"></span>
					聊天记录				</el-dropdown-item>
				<el-dropdown-item v-if="this.$storage.get('sessionTable')!='users'" class="item3" :command="'front'">
					<span class="icon iconfont icon-fanhui19"></span>
					退出到前台
				</el-dropdown-item>
				<el-dropdown-item class="item4" :command="'logout'">
					<span class="icon iconfont icon-guanbi19"></span>
					退出登录
				</el-dropdown-item>
			</el-dropdown-menu>
			</template>
		</el-dropdown>
		<el-dialog v-model="recordVisible" :title="'聊天记录'" :append-to-body="true">
			<div class="z-box" :style='{"width":"70%","padding":"20px","margin":"0 auto"}'>
				<div class="section-content" v-for="item in recordList" :key="item.id" @click.stop="chatClick(item)">
					<div style="display: flex;align-items: center;">
						<img :src="item.picture?$base.url + item.picture:require('@/assets/img/avator.png')" style="width: 60px;border-radius: 50%;"
							alt="">
						<div style="margin: 0 0 0 10px;display: flex;flex-direction: column;align-items: flex-start;">
							<div :style='{"fontSize":"14px","color":"#000","flex":"1","fontWeight":"bold"}'
								class="item-style">{{item.name}}</div>
							<div :style='{"color":"#666","flex":"1","fontSize":"14px","lineHeight":"1.5","display":"flex","alignItems":"center"}'
								class="item-style">
								<div v-if="item.notreadnum" style="padding: 0 5px;height: 16px;border-radius: 50%;text-align: center;line-height: 16px;font-size: 12px;background: #f00;color:#fff;width: auto;margin: 0 2px 0 0">{{item.notreadnum}}</div>
								{{item.content.split('/').length&&item.content.split('/')[0]=='upload'?'[多媒体]':item.content}}
							</div>
						</div>
					</div>
					<div>
						<el-button @click.stop="chatDel(item)" size="small" type="danger">删除</el-button>
					</div>
				</div>
				<div class="noList" v-if="!recordList.length">
					暂无聊天记录				</div>
			</div>
		</el-dialog>
		<el-dialog v-model="chatVisible" @close="clearChat" :title="nowname" :append-to-body="true">
			<div class="chat-content" id="chat-content">
				<div v-bind:key="item.id" v-for="item in chatList">
					<div v-if="item.addtime" class="addtime">{{timeFormat(item.addtime)}}</div>
					<div v-if="item.uid==myid" class="right-content">
						<el-alert v-if="item.format==1" class="text-content" :title="item.content" :closable="false"
							type="warning"></el-alert>
						<el-image v-else-if="item.format==2" fit="cover" :src="item.content?$base.url + item.content:''" style="width: 100px;height: 100px;" :preview-src-list="[item.content?$base.url + item.content:'']"></el-image>
						<video v-else-if="item.format==3" :src="$base.url + item.content" style="width: 280px;" controls></video>
						<el-button v-else-if="item.format==4" type="primary" size="small" @click="download(item.content)">文件预览</el-button>
						<audio v-else-if="item.format==5" :src="$base.url + item.content" controls></audio>
						<img :src="avatar?$base.url + avatar:require('@/assets/img/avator.png')" alt="" style="width: 30px;border-radius: 50%;height: 30px;margin: 0 0 0 10px;" />
					</div>
					<div v-else class="left-content">
						<img :src="nowfpic?$base.url + nowfpic:require('@/assets/img/avator.png')" alt="" style="width: 30px;border-radius: 50%;height: 30px;margin: 0 10px 0 0;" />
						<el-alert v-if="item.format==1" class="text-content" :title="item.content" :closable="false"
							type="success"></el-alert>
						<el-image v-else-if="item.format==2" fit="cover" :src="item.content?$base.url + item.content:''" style="width: 100px;height: 100px;" :preview-src-list="[item.content?$base.url + item.content:'']"></el-image>
						<video v-else-if="item.format==3" :src="$base.url + item.content" style="width: 280px;" controls></video>
						<el-button v-else-if="item.format==4" type="primary" size="small" @click="download(item.content)">文件预览</el-button>
						<audio v-else-if="item.format==5" :src="$base.url + item.content" controls></audio>
					</div>
					<div class="clear-float"></div>
				</div>
			</div>
			<template #footer>
			<div class="dialog-footer">
				<div style="display: flex;align-items: center">
					<el-input @keydown.enter="addChat(null)" v-model="chatForm.content" placeholder="请输入内容" style="width: calc(100% - 180px);float: left;">
					</el-input>
					<el-button :disabled="chatForm.content?false:true" type="primary" @click="addChat(null)">发送</el-button>
					<el-button
						type="warning" 
						:icon="isRecording ? VideoPause : Microphone"
						@click="toggleRecord"
						:class="{'voice-record-btn': true, 'voice-recording': isRecording}"
						style="margin: 0 0 0 6px;">
						{{ isRecording ? `录音中 ${recordDuration}秒 (点击停止)` : '点击录音' }}
					</el-button>
					<el-upload style="display: inline-block;margin: 0 0 0 6px;" class="upload-demo" accept=".jpg,.png" :action="uploadUrl" :on-success="uploadSuccess"
						:show-file-list="false">
						<el-button type="success">上传图片</el-button>
					</el-upload>
					<el-upload class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess2" accept=".mp4"
						:show-file-list="false">
						<el-button type="success">上传视频</el-button>
					</el-upload>
					<el-upload class="upload-demo" :action="uploadUrl" :on-success="uploadSuccess3"
						:show-file-list="false">
						<el-button type="success">上传文件</el-button>
					</el-upload>
				</div>
			</div>
			</template>
		</el-dialog>
	</div>
</template>

<script>
	import { ElMessage, ElMessageBox, ElLoading } from 'element-plus';
	import { Microphone, VideoPause } from '@element-plus/icons-vue';
	import timeMethod from '@/components/common/timeMethod.js'
	import {
		WebsocketMixin
	} from '@/mixins/WebsocketMixin'
	export default {
		mixins: [WebsocketMixin],
		components: { Microphone, VideoPause },
		data() {
			return {
				dialogVisible: false,
				ruleForm: {},
				user: null,
				recordList: [],
				recordVisible: false,
				chatVisible: false,
				nowfid: 0,
				nowfpic:'',
				nowname: '',
				chatList: [],
				chatForm: {
					content: ''
				},
				uploadUrl: this.$base.url + 'file/upload',
				// 语音聊天相关
				isRecording: false,
				mediaRecorder: null,
				audioStream: null,
				audioChunks: [],
				recordDuration: 0,
				recordTimer: null,
				hasChatList: [
				],
				sensitiveWordsArr: [],
				topLogoUrl: '',
			};
		},
		created() {
			this.$http.get('config/info?name=bTopLogo',).then(rs=>{this.topLogoUrl = rs.data.data?rs.data.data.value:''})
			this.getSensitiveWords()
		},
		computed: {
			avatar(){
				return this.$storage.get('headportrait')?this.$storage.get('headportrait'):''
			},
			myid(){
				return this.$storage.get('userid')?this.$storage.get('userid'):''
			},
		},
		mounted() {
		},
		methods: {
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			changeHasChat(){
				let table = this.$storage.get('sessionTable')
				for(let x in this.hasChatList){
					if(this.hasChatList[x]==table){
						return true
					}
				}
				return false
			},
			handleCommand(name) {
				if (name == 'logout') {
					this.onLogout()
				} 
				else if (name == 'front') {
					this.onIndexTap()
				}
				else if (name == 'backUp'){
					this.backUp()
				}
				else if (name == 'chat'){
					this.getRecordList()
				}
				else {
					let router = this.$router
					name = '/'+name
					router.push(name)
				}
			},
			getRecordList() {
				this.$http.get('friend/page2', {
					params: {
						uid: this.$storage.get('userid'),
						type: 2
					}
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.recordList = res.data.data.list
						this.recordVisible = true
					}
				})
			},
			chatClick(row) {
				this.nowfid = row.fid
				this.nowfpic = row.picture
				this.nowname = row.name
				this.initWebSocket(this.nowfid)
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
						uid: this.$storage.get('userid'),
						fid: this.nowfid
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
			chatDel(row){
				ElMessageBox.confirm('是否删除聊天记录？').then(_ => {
					let arr = []
					let brr = []
					this.$http.get('chatmessage/list',{params: {
						page: 1,
						limit: 10000,
						uid: Number(this.$storage.get('userid')),
						fid: row.fid
					}}).then(rs=>{
						if(rs.data.data.list.length){
							for(let x in rs.data.data.list){
								arr.push(rs.data.data.list[x].id)
							}
						}
						this.$http.get('chatmessage/list',{params: {
							page: 1,
							limit: 10000,
							uid: row.fid,
							fid: Number(this.$storage.get('userid'))
						}}).then(rs2=>{
							if(rs2.data.data.list.length){
								for(let x in rs2.data.data.list){
									arr.push(rs2.data.data.list[x].id)
								}
							}
							this.$http.post('chatmessage/delete',arr).then(()=>{
								this.$http.get('friend/list',{params: {
									page: 1,
									limit: 10000,
									uid: Number(this.$storage.get('userid')),
									fid: row.fid,
									type: 0
								}}).then(rs3=>{
									if(rs3.data.data.list.length){
										for(let x in rs3.data.data.list){
											brr.push(rs3.data.data.list[x].id)
										}
									}
									this.$http.get('friend/list',{params: {
										page: 1,
										limit: 10000,
										uid: row.fid,
										fid: Number(this.$storage.get('userid')),
										type: 0
									}}).then(rs4=>{
										if(rs4.data.data.list.length){
											for(let x in rs4.data.data.list){
												brr.push(rs4.data.data.list[x].id)
											}
										}
										this.$http.get('friend/list',{params: {
											page: 1,
											limit: 10000,
											uid: Number(this.$storage.get('userid')),
											fid: row.fid,
											type: 2
										}}).then(rs5=>{
											if(rs5.data.data.list.length){
												for(let x in rs5.data.data.list){
													brr.push(rs5.data.data.list[x].id)
												}
											}
											this.$http.get('friend/list',{params: {
												page: 1,
												limit: 10000,
												uid: row.fid,
												fid: Number(this.$storage.get('userid')),
												type: 2
											}}).then(rs6=>{
												if(rs6.data.data.list.length){
													for(let x in rs6.data.data.list){
														brr.push(rs6.data.data.list[x].id)
													}
												}
												this.$http.post('friend/delete',brr).then(()=>{
													ElMessage.success('删除成功')
													this.getRecordList()
												})
											})
										})
									})
								})
							})
						})
					})
				}).catch(_ => {});
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
				this.chatList = []
				this.getRecordList()
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
						ElMessage.error('您的浏览器不支持录音功能')
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
						ElMessage.error('录音过程中发生错误')
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
							ElMessage.info('录音已达到最大时长，已自动停止')
						}
					}, 1000)
					
				} catch (error) {
					console.error('无法访问麦克风:', error)
					this.isRecording = false
					if (error.name === 'NotAllowedError' || error.name === 'PermissionDeniedError') {
						ElMessage.error('麦克风权限被拒绝，请在浏览器设置中允许访问麦克风')
					} else if (error.name === 'NotFoundError' || error.name === 'DevicesNotFoundError') {
						ElMessage.error('未检测到麦克风设备')
					} else {
						ElMessage.error('无法访问麦克风，请检查权限设置')
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
					ElMessage.warning('录音时间太短，请至少录音1秒')
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
					ElMessage.error('录音数据为空')
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
					ElMessage.error('录音文件过大，请缩短录音时间')
					this.audioChunks = []
					this.recordDuration = 0
					return
				}
				
				// 创建FormData上传
				const formData = new FormData()
				formData.append('file', audioBlob, `voice_${Date.now()}.${fileExtension}`)
				
				// 显示上传提示
				const loading = ElLoading.service({
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
						ElMessage.success('语音发送成功')
					} else {
						ElMessage.error(res.data.msg || '上传失败')
					}
				}).catch(error => {
					loading.close()
					console.error('上传失败:', error)
					ElMessage.error('语音上传失败，请重试')
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
				if(!ask) {
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
					uid: this.$storage.get('userid'),
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
			// 预览
			download(file){
				if(!file){
					return false
				}
				window.open((location.href.split(this.$base.name).length>1 ? location.href.split(this.$base.name)[0] + this.$base.name + '/' + file :this.$base.url + file))
			},
			async onLogout() {
				await this.$http.post(`${this.$storage.get('sessionTable')}/logout`).then(rs=>{
					this.$storage.clear()
					this.$store.dispatch('tagsView/delAllViews')
					window.location.href = this.$base.indexUrl
				})
			},
			onIndexTap(){
				localStorage.setItem("frontToken", localStorage.getItem("Token"));
				localStorage.setItem("frontRole", localStorage.getItem("role"));
				localStorage.setItem("frontSessionTable", localStorage.getItem("sessionTable"));
				localStorage.setItem("frontHeadportrait", localStorage.getItem("headportrait"));
				localStorage.setItem("UserTableName", localStorage.getItem("sessionTable"));
				localStorage.setItem("frontUserid", localStorage.getItem("userid"));
				localStorage.setItem("username", localStorage.getItem("adminName"));
				window.location.href = `${this.$base.indexUrl}`
			},
			backUp() {
				ElMessageBox.confirm('是否备份数据库?', '数据备份提示', {
					confirmButtonText: '是',
					cancelButtonText: '否',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: '/mysqldump',
						method: "get"
					}).then(({
						data
					}) => {
						if (data) {
							const binaryData = [];
							binaryData.push(data);
							const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
								type: 'application/pdf;chartset=UTF-8'
							}))
							const a = document.createElement('a')
							a.href = objectUrl
							a.download = 'mysql.dmp'
							// a.click()
							// 下面这个写法兼容火狐
							a.dispatchEvent(new MouseEvent('click', {
								bubbles: true,
								cancelable: true,
								view: window
							}))
							window.URL.revokeObjectURL(objectUrl)
							ElMessage.success('数据备份成功')
						} else {
							ElMessage.error(data && data.msg ? data.msg : '备份失败');
						}
					});
				});
			},
		}
	};
</script>


<style lang="scss" scoped>
	$accent: #00b4d8;
	$surface: #1e293b;
	$text: #e2e8f0;
	$text2: #94a3b8;
	.navbar {
		display: flex;
		align-items: center;
		width: 100%;
		.title {
			padding: 0;
			flex: 1;
			display: flex;
			align-items: center;
			.title-name {
				font-weight: 700;
				font-size: 16px;
				color: $text;
				background: none;
				-webkit-text-fill-color: $text;
				letter-spacing: 0.5px;
			}
		}
		.dropdown-box {
			border-radius: 8px;
			color: #fff;
			background: linear-gradient(135deg, rgba(0,180,216,0.15), rgba(167,139,250,0.1));
			border: 1px solid rgba(0,180,216,0.2);
			display: flex;
			font-size: 13px;
			.el-dropdown-link {
				border-radius: 8px;
				padding: 0 14px;
				display: flex;
				align-items: center;
				height: 36px;
				cursor: pointer;
				.el-image {
					border-radius: 50%;
					margin: 0 8px 0 0;
					width: 26px;
					height: 26px;
					border: 2px solid rgba(0,180,216,0.3);
				}
				.label { color: $text2; font-size: 12px; }
				.nickname { color: $text; font-size: 13px; font-weight: 600; margin: 0 0 0 3px; }
				.iconfont { margin: 0 0 0 5px; color: $accent; font-size: 10px; }
			}
		}
	}
	.top-el-dropdown-menu {
		border: 1px solid rgba(255,255,255,0.08);
		border-radius: 10px;
		padding: 6px 0;
		box-shadow: 0 10px 40px rgba(0,0,0,0.5);
		margin: 8px 0;
		background: #1e293b;
		li.el-dropdown-menu__item {
			cursor: pointer;
			padding: 0 20px;
			margin: 0;
			color: #94a3b8;
			background: transparent;
			font-size: 13px;
			line-height: 38px;
			transition: all 0.2s;
			.iconfont { margin: 0 8px 0 0; color: $accent; font-size: 13px; }
			&:hover { background: rgba(0,180,216,0.08); color: #e2e8f0; }
		}
	}
	.section-content {
		cursor: pointer;
		padding: 20px;
		box-shadow: 0px 4px 10px 0px rgba(0, 0, 0, 0.3);
		margin: 0 0 20px;
		color: #333;
		background: #fff;
		display: flex;
		width: 100%;
		border-color: #efefef;
		border-width: 0;
		align-items: center;
		border-style: solid;
		position: relative;
		justify-content: space-between;
	}
	
	.section-content:hover {
		color: #fff;
		background: #DF847F10;
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
	.noList {
		color: #9e9e9e;
		width: 100%;
		text-align: center;
		padding: 60px 0;
	}
</style>
