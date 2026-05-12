<template>
	<div class="detail-page">
		<div class="detail-topbar">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index" :to="centerType ? '/main/center' : '/main/xuexiziliao'"><a>{{item.name}}</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item3"><a href="javascript:void(0);">详情</a></el-breadcrumb-item>
			</el-breadcrumb>
			<el-button class="back-btn" size="small" @click="backClick" :icon="ArrowLeft">返回</el-button>
		</div>

		<div class="detail-card">
			<div class="card-img" v-if="detailBanner.length">
				<img v-if="detailBanner[currentImg] && detailBanner[currentImg].substr(0,4)=='http'" :src="detailBanner[currentImg]" class="main-image" />
				<img v-else :src="baseUrl + detailBanner[currentImg]" class="main-image" />
				<div class="img-thumbs" v-if="detailBanner.length > 1">
					<div class="thumb" v-for="(img, idx) in detailBanner" :key="idx"
						:class="{ active: currentImg === idx }" @click="currentImg = idx">
						<img v-if="img.substr(0,4)=='http'" :src="img" />
						<img v-else :src="baseUrl + img" />
					</div>
				</div>
			</div>
			<div class="card-img card-img-empty" v-else>
				<el-icon><Picture /></el-icon>
			</div>

			<div class="card-info">
				<div class="title-row">
					<h2 class="detail-title">{{ detail.ziliaobiaoti || "—" }}</h2>
					<div class="collect-btn" @click="storeup(1)" v-show="!isStoreup">
						<el-icon><Star /></el-icon>
						<span>收藏({{detail.storeupnum}})</span>
					</div>
					<div class="collect-btn active" @click="storeup(-1)" v-show="isStoreup">
						<el-icon><StarFilled /></el-icon>
						<span>已收藏({{detail.storeupnum}})</span>
					</div>
				</div>

				<div class="meta-tags">
					<span class="tag"><el-icon><CollectionTag /></el-icon> {{detail.ziliaoleixing || "—"}}</span>
					<span class="tag"><el-icon><Calendar /></el-icon> {{detail.fabushijian || "—"}}</span>
					<span class="tag"><el-icon><View /></el-icon> {{detail.clicknum || 0}}次浏览</span>
					<span class="tag price-tag" v-if="detail.jiage > 0"><el-icon><Coin /></el-icon> &#165;{{detail.jiage}}</span>
					<span class="tag free-tag" v-else><el-icon><Present /></el-icon> 免费</span>
				</div>

				<div class="bottom-bar">
					<div class="action-bar">
						<el-button class="act-btn act-download" size="small" :icon="Download"
							:disabled="!detail.ziliaofujian" @click="download(detail.ziliaofujian)">下载附件</el-button>
						<el-button class="act-btn act-edit" size="small" v-if="btnAuth('xuexiziliao','修改')" @click="editClick" :icon="Edit">修改</el-button>
						<el-button class="act-btn act-del" size="small" v-if="btnAuth('xuexiziliao','删除')" @click="delClick" :icon="Delete">删除</el-button>
						<el-button class="act-btn act-chapter" size="small" @click="allchapterClick()" v-if="btnAuth('xuexiziliao','章节管理')" :icon="Reading">章节</el-button>
					</div>
				</div>
			</div>
		</div>

		<div class="detail-video" v-if="detail.ziliaoshipin">
			<div id="dplayer"></div>
		</div>

		<el-tabs id="tabsBox" class="detail-tabs" v-model="activeName" type="border-card" v-if="tabsNum>0">
			<el-tab-pane label="资料详情" name="1">
				<div class="tab-scroll-area">
					<div class="ql-snow ql-editor tab-html-content" v-html="detail.ziliaoxiangqing"></div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="评论" name="2">
				<div class="tab-scroll-area">
					<el-form class="comment-inline-form" :model="form" :rules="rules" ref="form">
						<div class="comment-input-row">
							<el-input
								type="textarea"
								v-model="form.content"
								:rows="2"
								placeholder="写下你的评论..."
								maxlength="500"
								show-word-limit
								resize="none"
								class="comment-textarea"
							></el-input>
						</div>
						<div class="comment-action-row">
							<div></div>
							<div class="comment-btns">
								<el-button class="submit-btn" size="small" @click="submitForm('form')"><el-icon><Promotion /></el-icon> 提交评论</el-button>
								<el-button class="reset-btn" size="small" @click="resetForm('form')">重置</el-button>
							</div>
						</div>
					</el-form>

					<div class="comment-section-title" v-if="infoList.length">
						<el-icon><ChatLineRound /></el-icon>
						<span>全部评论 ({{total}})</span>
					</div>

					<div v-if="infoList.length" class="comment-list">
						<div class="comment-item" v-for="item in infoList" :key="item.id"
							@mouseenter="discussEnter(item.id)" :id="'discuss' + item.id" @mouseleave="discussLeave">
							<div class="comment-top-flag" v-if="item.istop">
								<el-icon><Top /></el-icon>
							</div>
							<div class="comment-left">
								<el-image v-if="item.avatarurl" :src="baseUrl + item.avatarurl" class="comment-avatar"></el-image>
								<el-image v-else :src="defaultAvatar" class="comment-avatar"></el-image>
							</div>
							<div class="comment-right">
								<div class="comment-header">
									<span class="comment-name">{{item.nickname}}</span>
									<span class="comment-time">{{item.addtime}}</span>
								</div>
								<div class="ql-snow ql-editor comment-text" v-html="item.content"></div>
								<div class="comment-footer">
									<div class="comment-interact">
										<span v-if="!comcaiChange(item)" class="interact-item" :class="{active: comzanChange(item)}" @click="comzanClick(item)">
											<i class="icon iconfont" :class="comzanChange(item)?'icon-zan11':'icon-zan07'"></i>
											{{comzanChange(item)?'已赞':'赞'}} ({{item.thumbsupnum}})
										</span>
										<span v-if="!comzanChange(item)" class="interact-item" :class="{active: comcaiChange(item)}" @click="comcaiClick(item)">
											<i class="icon iconfont" :class="comcaiChange(item)?'icon-cai16':'icon-cai01'"></i>
											{{comcaiChange(item)?'已踩':'踩'}} ({{item.crazilynum}})
										</span>
									</div>
									<el-button class="del-comment" size="small" :icon="Delete"
										v-if="showIndex==item.id&&userid==item.userid" @click="discussDel(item.id)">删除</el-button>
								</div>
							</div>
							<div class="comment-reply-box" v-if="item.reply">
								<el-icon><ChatDotRound /></el-icon> 回复：<span class="ql-snow ql-editor" v-html="item.reply"></span>
							</div>
						</div>
					</div>

					<el-pagination
						background id="pagination" class="pagination"
						:page-size="pageSize" prev-text="<" next-text=">" :hide-on-single-page="true"
						:layout='["total","prev","pager","next","sizes","jumper"].join()'
						:total="total" @current-change="curChange" @prev-click="prevClick"
						@next-click="nextClick" @size-change="sizeChange"
					></el-pagination>
				</div>
			</el-tab-pane>
		</el-tabs>

		<el-dialog title="购买学习资料" v-model="purchaseDialogVisible" width="420px" :close-on-click-modal="false" class="purchase-dialog">
			<div class="purchase-info" v-if="!purchaseOrderNo">
				<div class="purchase-title">{{ detail.ziliaobiaoti }}</div>
				<div class="purchase-price">&#165;{{ detail.jiage }}</div>
				<div class="purchase-pay-method">
					<span>支付方式：</span>
					<el-radio-group v-model="payMethod">
						<el-radio label="支付宝">支付宝</el-radio>
						<el-radio label="微信">微信</el-radio>
					</el-radio-group>
				</div>
				<el-button type="primary" class="purchase-confirm-btn" :loading="purchaseLoading" @click="createPurchaseOrder">确认购买</el-button>
			</div>
			<div class="purchase-qr" v-else>
				<p class="qr-hint">请使用 {{ payMethod }} 扫码支付</p>
				<div class="qr-code-box">
					<img :src="'https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=' + encodeURIComponent(purchasePayUrl)" />
				</div>
				<div class="qr-amount">支付金额：<strong>&#165;{{ detail.jiage }}</strong></div>
				<el-button type="success" class="purchase-done-btn" :loading="confirmPayLoading" @click="confirmPurchasePay">我已完成支付</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import axios from 'axios'
	import DPlayer from 'dplayer';
	import defaultAvatar from '@/assets/touxiang.png'
	import {
		ArrowLeft,
		Picture,
		Star,
		StarFilled,
		CollectionTag,
		Calendar,
		View,
		Coin,
		Present,
		Download,
		Edit,
		Delete,
		Reading,
		Promotion,
		ChatLineRound,
		Top,
		ChatDotRound,
	} from '@element-plus/icons-vue'
	export default {
		components: {
			ArrowLeft,
			Picture,
			Star,
			StarFilled,
			CollectionTag,
			Calendar,
			View,
			Coin,
			Present,
			Download,
			Edit,
			Delete,
			Reading,
			Promotion,
			ChatLineRound,
			Top,
			ChatDotRound,
		},
		data() {
			return {
				defaultAvatar,
				tablename: 'xuexiziliao',
				baseUrl: '',
				breadcrumbItem: [{ name: '学习资料' }],
				title: '',
				detailBanner: [],
				userid: Number(localStorage.getItem('frontUserid')),
				id: 0,
				detail: {},
				tabsNum: 2,
				activeName: '1',
				form: {
					content: '',
					userid: Number(localStorage.getItem('frontUserid')),
					nickname: localStorage.getItem('displayName') || localStorage.getItem('username'),
					avatarurl: '',
				},
				showIndex: -1,
				infoList: [],
				rules: {
					content: [{ required: true, message: '请输入内容', trigger: 'blur' }],
				},
				total: 1,
				pageSize: 6,
				totalPage: 1,
				storeupParams: {
					name: '',
					picture: '',
					refid: 0,
					tablename: 'xuexiziliao',
					userid: Number(localStorage.getItem('frontUserid'))
				},
				isStoreup: false,
				storeupInfo: {},
				buynumber: 1,
				centerType: false,
				storeupType: false,
				sensitiveWordsArr: [],
				currentImg: 0,
				isVip: false,
				hasPurchased: false,
				purchaseDialogVisible: false,
				purchaseLoading: false,
				confirmPayLoading: false,
				purchaseOrderNo: '',
				purchasePayUrl: '',
				payMethod: '支付宝',
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
		computed: {
			username() {
				return localStorage.getItem('username')
			}
		},
		methods: {
			init() {
				this.id = this.$route.query.id
				this.baseUrl = this.$config.baseUrl;
				this.$http.get(this.tablename + '/detail/'  + this.id, {}).then(async res => {
					if (res.data.code == 0) {
						this.detail = res.data.data;
						this.getSensitiveWords()
						this.title = this.detail.ziliaobiaoti;
						if(this.detail.ziliaofengmian) {
							this.detailBanner = this.detail.ziliaofengmian.split(",w").length>1?[this.detail.ziliaofengmian]:this.detail.ziliaofengmian.split(',');
						}
						this.$forceUpdate();
						this.getDiscussList(1);
						if(localStorage.getItem('frontToken')){
							this.getStoreupStatus();
							this.checkPurchaseAccess();
						}
					}
				});
			},
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			storeup(type) {
				if (type == 1 && !this.isStoreup) {
					this.storeupParams.name = this.title;
					this.storeupParams.picture = this.detailBanner[0];
					this.storeupParams.refid = this.detail.id;
					this.storeupParams.type = String(type);
					this.$http.post('storeup/add', this.storeupParams).then(res => {
						if (res.data.code == 0) {
							this.isStoreup = true;
							this.detail.storeupnum++
							this.$http.post('xuexiziliao/update', this.detail).then(res => {});
							this.$message({ type: 'success', message: '收藏成功!', duration: 1500 });
						}
					});
				}
				if (type == -1 && this.isStoreup) {
					this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: '1', refid: this.detail.id, tablename: 'xuexiziliao', userid: Number(localStorage.getItem('frontUserid'))}}).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.isStoreup = true;
							this.storeupInfo = res.data.data.list[0];
							let delIds = [this.storeupInfo.id];
							this.$http.post('storeup/delete', delIds).then(res => {
								if (res.data.code == 0) {
									this.isStoreup = false;
									this.detail.storeupnum--
									this.$http.post('xuexiziliao/update', this.detail).then(res => {});
									this.$message({ type: 'success', message: '取消成功!', duration: 1500 });
								}
							});
						}
					});
				}
			},
			getStoreupStatus(){
				if(localStorage.getItem("frontToken")) {
					this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: '1', refid: this.detail.id, tablename: 'xuexiziliao', userid: Number(localStorage.getItem('frontUserid'))}}).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.isStoreup = true;
							this.storeupInfo = res.data.data.list[0];
						}
					});
				}
			},
			curChange(page) { this.getDiscussList(page); },
			prevClick(page) { this.getDiscussList(page); },
			nextClick(page) { this.getDiscussList(page); },
			sizeChange(size){ this.pageSize = size; this.getDiscussList(1); },
			backClick(){
				if(this.storeupType){
					history.back()
				}else if(this.centerType){
					this.$router.push({path: '/main/center', query: {tab: 'xuexiziliao'}});
				}else{
					this.$router.push({path: '/main/xuexiziliao'});
				}
			},
			download(file){
				if(!file) {
					this.$message({ type: 'error', message: '文件不存在', duration: 1500 });
					return;
				}
				if(!localStorage.getItem('frontToken')) {
					this.$message({ type: 'warning', message: '请先登录后再下载', duration: 1500 });
					return;
				}
				let price = this.detail.jiage || 0;
				if(price > 0 && !this.isVip && !this.hasPurchased) {
					this.purchaseOrderNo = '';
					this.purchasePayUrl = '';
					this.purchaseDialogVisible = true;
					return;
				}
			let arr = file.replace(new RegExp('upload/', "g"), "")
			let ext = arr.split('.').pop().toLowerCase()
			let mimeType = 'application/octet-stream'
			if(ext === 'pdf') mimeType = 'application/pdf'
			else if(ext === 'doc' || ext === 'docx') mimeType = 'application/msword'
			else if(ext === 'xls' || ext === 'xlsx') mimeType = 'application/vnd.ms-excel'
			else if(ext === 'zip') mimeType = 'application/zip'
			else if(ext === 'rar') mimeType = 'application/x-rar-compressed'
			let downloadUrl = this.baseUrl + 'file/download?fileName=' + encodeURIComponent(arr)
			axios.get(downloadUrl, {
				headers: { token: localStorage.getItem("frontToken") },
				responseType: "blob"
			}).then(({ data }) => {
				const blob = new Blob([data], { type: mimeType })
				const objectUrl = window.URL.createObjectURL(blob)
				const a = document.createElement('a')
				a.href = objectUrl
				a.download = arr
				document.body.appendChild(a)
				a.click()
				document.body.removeChild(a)
				window.URL.revokeObjectURL(objectUrl)
			}).catch(() => {
				let fallbackUrl = (location.href.split(this.$config.name).length>1 ? location.href.split(this.$config.name)[0] :'') + this.$config.name + 'file/download?fileName=' + encodeURIComponent(arr)
				axios.get(fallbackUrl, {
					headers: { token: localStorage.getItem("frontToken") },
					responseType: "blob"
				}).then(({ data }) => {
					const blob = new Blob([data], { type: mimeType })
					const objectUrl = window.URL.createObjectURL(blob)
					const a = document.createElement('a')
					a.href = objectUrl
					a.download = arr
					document.body.appendChild(a)
					a.click()
					document.body.removeChild(a)
					window.URL.revokeObjectURL(objectUrl)
				})
			})
			},
			checkPurchaseAccess() {
				this.$http.get('ziliaogoumai/checkPurchased', { params: { xuexiziliaoid: this.id } }).then(res => {
					if(res.data.code == 0) {
						let purchased = res.data.data;
						let reason = res.data.reason || '';
						if(reason === 'vip') {
							this.isVip = true;
							this.hasPurchased = true;
						} else {
							this.hasPurchased = purchased;
						}
					}
				}).catch(() => {});
			},
			createPurchaseOrder() {
				this.purchaseLoading = true;
				this.$http.post('ziliaogoumai/createOrder', {
					xuexiziliaoid: this.detail.id,
					payMethod: this.payMethod,
				}).then(res => {
					this.purchaseLoading = false;
					if(res.data.code == 0) {
						this.purchaseOrderNo = res.data.data.orderNo;
						this.purchasePayUrl = res.data.data.payUrl;
					} else {
						this.$message({ type: 'error', message: res.data.msg || '创建订单失败', duration: 2000 });
					}
				}).catch(() => {
					this.purchaseLoading = false;
					this.$message({ type: 'error', message: '网络错误', duration: 1500 });
				});
			},
			confirmPurchasePay() {
				this.confirmPayLoading = true;
				this.$http.post('ziliaogoumai/payCallback', { orderNo: this.purchaseOrderNo }).then(res => {
					this.confirmPayLoading = false;
					if(res.data.code == 0) {
						this.hasPurchased = true;
						this.purchaseDialogVisible = false;
						this.$message({ type: 'success', message: '购买成功！现在可以下载附件了', duration: 2000 });
					} else {
						this.$message({ type: 'error', message: res.data.msg || '支付确认失败', duration: 2000 });
					}
				}).catch(() => {
					this.confirmPayLoading = false;
					this.$message({ type: 'error', message: '网络错误', duration: 1500 });
				});
			},
			initDb(file) {
				if (!file) return
				let url = this.baseUrl + file
				let dp = new DPlayer({
					container: document.getElementById('dplayer'),
					screenshot: true,
					video: { url },
					control: { pictureInPicture: true },
					danmaku: true,
					apiBackend: {
						read: async (options) => {
							let initDanmuList = this.infoList.map((item,index) => ({
								text: item.content, time: 3 * index, type: 0
							}))
							options.success(initDanmuList);
						},
						send: (options) => {
							this.saveDanmaku(options.data)
							options.success([]);
						},
					},
				});
				this.dp = dp
			},
			saveDanmaku(content){ this.addComments(content.text,2) },
			getDiscussList(page, type=1) {
				this.$http.get('discussxuexiziliao/list', {
					params: { page, limit: this.pageSize, refid: this.detail.id, sort: 'istop', order: 'desc' }
				}).then(res => {
					if (res.data.code == 0) {
						for(let x in res.data.data.list) {
							res.data.data.list[x].content = res.data.data.list[x].content.replace(/img src/gi,"img style=\"width:30%;\" src");
						}
						this.infoList = res.data.data.list;
						this.total = res.data.data.total;
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						if(type==1) {
							this.initDb(this.detail.ziliaoshipin)
							if(this.$route.query.discussId&&this.$route.query.discussId!=0) {
								this.$nextTick(()=>{
									document.getElementById('discuss' + this.$route.query.discussId).scrollIntoView({ behavior: 'smooth' });
								})
							}
						}
					}
				});
			},
			comzanChange(row){
				if(row.tuserids){
					let arr = row.tuserids.split(',')
					for(let x in arr){ if(arr[x] == this.userid) return true }
				}
				return false
			},
			comzanClick(row){
				if(!this.userid) return false
				if(!this.comzanChange(row)){
					row.thumbsupnum++
					row.tuserids = row.tuserids ? row.tuserids + ',' + this.userid : String(this.userid)
					this.$http.post('discussxuexiziliao/update',row).then(()=> this.$message.success('点赞成功'))
				}else {
					row.thumbsupnum--
					let arr = row.tuserids.split(',')
					for(let x in arr){ if(arr[x] == this.userid) arr.splice(x,1) }
					row.tuserids = arr.join(',')
					this.$http.post('discussxuexiziliao/update',row).then(()=> this.$message.success('取消成功'))
				}
			},
			comcaiChange(row){
				if(row.cuserids){
					let arr = row.cuserids.split(',')
					for(let x in arr){ if(arr[x] == this.userid) return true }
				}
				return false
			},
			comcaiClick(row){
				if(!this.userid) return false
				if(!this.comcaiChange(row)){
					row.crazilynum++
					row.cuserids = row.cuserids ? row.cuserids + ',' + this.userid : String(this.userid)
					this.$http.post('discussxuexiziliao/update',row).then(()=> this.$message.success('点踩成功'))
				}else {
					row.crazilynum--
					let arr = row.cuserids.split(',')
					for(let x in arr){ if(arr[x] == this.userid) arr.splice(x,1) }
					row.cuserids = arr.join(',')
					this.$http.post('discussxuexiziliao/update',row).then(()=> this.$message.success('取消成功'))
				}
			},
			discussEnter(index){ this.showIndex = index },
			discussLeave(){ this.showIndex = -1 },
			discussDel(id){
				this.$confirm('是否删除此评论？').then(_ => {
					this.$http.post('discussxuexiziliao/delete',[id]).then(res=>{
						if(res.data&&res.data.code==0){
							this.addDiscussNum(1)
							this.$message({ type: 'success', message: '删除成功!', duration: 1500, onClose: () => { this.getDiscussList(1); } });
						}
					})
				}).catch(_ => {});
			},
			submitForm(formName) {
				for(var i=0; i<this.sensitiveWordsArr.length; i++){
					var reg = new RegExp(this.sensitiveWordsArr[i],"g");
					if (this.form.content.indexOf(this.sensitiveWordsArr[i]) > -1) {
						this.form.content = this.form.content.replace(reg,"**");
					}
				}
				this.$refs[formName].validate((valid) => { if (valid) this.addComments(); });
			},
			addComments(content=null, type=1) {
				let data = JSON.parse(JSON.stringify(this.form))
				data.refid = this.detail.id;
				data.avatarurl = localStorage.getItem('frontHeadportrait') || '';
				if(type==2) data.content = content
				this.$http.post('discussxuexiziliao/add', data).then(rs2 => {
					if (rs2.data.code == 0) {
						if(type==1) this.form.content = '';
						this.addDiscussNum(2)
						this.getDiscussList(1, type);
						this.$message({ type: 'success', message: '评论成功!', duration: 1500 });
					}
				});
			},
			resetForm(formName) { this.$refs[formName].resetFields(); },
			addDiscussNum(type){
				if(type==2) this.detail.discussnum++
				else if(type==1) this.detail.discussnum = this.detail.discussnum > 0 ? this.detail.discussnum - 1 : 0
				this.$http.post('xuexiziliao/update',this.detail).then(res=>{})
			},
			btnAuth(tableName,key){
				return this.centerType ? this.isBackAuth(tableName,key) : this.isAuth(tableName,key)
			},
			editClick(){
				this.$router.push(`/main/xuexiziliaoAdd?type=edit&&id=${this.detail.id}`);
			},
			async delClick(){
				await this.$confirm('是否删除此学习资料？').then(_ => {
					this.$http.post('xuexiziliao/delete', [this.detail.id]).then(async res => {
						if (res.data.code == 0) {
							this.$http.get('storeup/list',{params:{ page: 1, limit: 100, refid: this.detail.id, tablename: 'xuexiziliao' }}).then(async obj=>{
								if(obj.data&&obj.data.code==0){
									let arr = obj.data.data.list.map(i => i.id)
									if(arr.length) await this.$http.post('storeup/delete',arr)
									this.$message({ type: 'success', message: '删除成功!', duration: 1500, onClose: () => { history.back() } });
								}
							})
						}
					});
				}).catch(_ => {});
			},
			allchapterClick(){
				let params = { refid: this.detail.id }
				if(this.centerType) params.centerType = 1
				this.$router.push({path: '/main/chapterxuexiziliao', query: params});
			},
			discussClick(){
				let params = { refid: this.detail.id }
				if(this.centerType) params.centerType = 1
				this.$router.push({path: '/main/discussxuexiziliao', query: params});
			},
			getUUID() { return new Date().getTime(); },
		},
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$primary-light: #38bdf8;
$gold: #f59e0b;
$text-dark: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: #64748b;
$border: rgba(14,165,233,0.12);
$bg: #0c1222;
$white: #0f172a;
$radius: 14px;
$green: #22c55e;
$red: #fb7185;
$border-light: rgba(14,165,233,0.10);

.detail-page {
	height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
	overflow: hidden;
	padding: 12px 24px 0;
	max-width: 1100px;
	margin: 0 auto;
	width: 100%;
	background: $bg;
}

.detail-topbar {
	display: flex; align-items: center; justify-content: space-between;
	margin-bottom: 10px; flex-shrink: 0;
	::v-deep .el-breadcrumb__inner, ::v-deep .el-breadcrumb__inner a { color: $text-dim !important; font-size: 13px; }
	::v-deep .el-breadcrumb__separator { color: rgba(255,255,255,0.3) !important; }
	.back-btn {
		border-radius: 20px; border: 1px solid rgba(255,255,255,0.15); color: rgba(255,255,255,0.6);
		background: rgba(255,255,255,0.05); font-size: 12px; padding: 5px 14px;
		&:hover { color: #fff; border-color: rgba(255,255,255,0.3); background: rgba(255,255,255,0.1); }
	}
}

.detail-card {
	display: flex; gap: 20px; background: $white; border-radius: $radius;
	box-shadow: 0 8px 30px rgba(0,0,0,0.15); padding: 20px; flex-shrink: 0; align-items: stretch;
}

.card-img {
	width: 180px; min-width: 180px; flex-shrink: 0;
	border-radius: 10px; overflow: hidden; background: $border-light;
	display: flex; flex-direction: column;
	.main-image { width: 100%; height: 140px; object-fit: cover; display: block; }
	.img-thumbs {
		display: flex; gap: 4px; padding: 6px 3px 3px; overflow-x: auto; background: $border-light;
		.thumb {
			width: 32px; height: 24px; border-radius: 4px; overflow: hidden; cursor: pointer;
			border: 2px solid transparent; flex-shrink: 0; opacity: 0.5; transition: all 0.2s;
			&.active { border-color: $primary; opacity: 1; }
			&:hover { opacity: 1; }
			img { width: 100%; height: 100%; object-fit: cover; display: block; }
		}
	}
}
.card-img-empty {
	align-items: center; justify-content: center;
	i { font-size: 36px; color: $text-dim; }
}

.card-info { flex: 1; display: flex; flex-direction: column; min-width: 0; justify-content: space-between; }

.title-row {
	display: flex; align-items: center; gap: 10px; margin-bottom: 8px;
	.detail-title { font-size: 18px; color: $text-dark; font-weight: 700; margin: 0; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
	.collect-btn {
		display: flex; align-items: center; gap: 3px; cursor: pointer; padding: 5px 14px;
		border-radius: 18px; border: 1.5px solid $border; color: $text-sub; font-size: 12px;
		transition: all 0.25s; white-space: nowrap; flex-shrink: 0; background: $white;
		i { font-size: 13px; }
		&:hover { color: $gold; border-color: $gold; }
		&.active { background: linear-gradient(135deg, $gold, #f97316); border-color: $gold; color: #fff;
			i { color: #fff; }
			&:hover { box-shadow: 0 3px 12px rgba(245,158,11,0.3); }
		}
	}
}

.meta-tags {
	display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 8px;
	.tag {
		font-size: 12px; color: $text-sub; background: $border-light; padding: 4px 10px;
		border-radius: 6px; display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;
		i { font-size: 12px; color: $text-sub; }
	}
}

.bottom-bar {
	display: flex; align-items: center; justify-content: flex-end; gap: 10px;
	margin-top: 8px; padding-top: 10px; border-top: 1.5px solid $border-light;
}

.action-bar {
	display: flex; gap: 6px; flex-wrap: wrap; justify-content: flex-end;
	.act-btn { border-radius: 8px; font-size: 12px; padding: 6px 14px; font-weight: 500; transition: all 0.3s; }
	.act-download { background: linear-gradient(135deg, $green, #059669); border: none; color: #fff; &:hover { box-shadow: 0 4px 14px rgba(34,197,94,0.3); transform: translateY(-1px); } }
	.act-edit { background: linear-gradient(135deg, $primary, $primary-light); border: none; color: #fff; &:hover { box-shadow: 0 4px 14px rgba(14,165,233,0.3); transform: translateY(-1px); } }
	.act-del { background: #fff; border: 1.5px solid #fca5a5; color: #dc2626; &:hover { background: #fef2f2; } }
	.act-chapter { background: $white; border: 1.5px solid $border; color: $text-sub; &:hover { color: $primary; border-color: $primary; background: rgba(14,165,233,0.12); } }
}

.detail-video {
	margin-top: 10px; border-radius: $radius; overflow: hidden;
	max-width: 100%; flex-shrink: 0;
}

.detail-tabs {
	flex: 1; min-height: 0; margin-top: 10px;
	border: none !important; box-shadow: none !important; background: transparent !important;
	display: flex; flex-direction: column;
	::v-deep .el-tabs--border-card { border: none; box-shadow: none; background: transparent; }
	::v-deep .el-tabs__header {
		padding: 0; margin: 0; background: $white; border: none;
		border-bottom: 2px solid $border-light; flex-shrink: 0; border-radius: $radius $radius 0 0;
		padding: 0 20px;
	}
	::v-deep .el-tabs__header .el-tabs__nav { border: none; }
	::v-deep .el-tabs__header .el-tabs__item {
		border: none !important; padding: 12px 6px; margin: 0 20px 0 0;
		color: $text-sub; font-weight: 500; font-size: 14px; background: none !important;
		height: auto; position: relative;
	}
	::v-deep .el-tabs__header .el-tabs__item::after {
		content: ""; position: absolute; left: 0; right: 0; bottom: -1px;
		height: 2.5px; background: transparent; border-radius: 2px; transition: background 0.25s;
	}
	::v-deep .el-tabs__header .el-tabs__item:hover { color: $primary; }
	::v-deep .el-tabs__header .el-tabs__item.is-active { color: $text-dark; font-weight: 700; }
	::v-deep .el-tabs__header .el-tabs__item.is-active::after { background: linear-gradient(90deg, $primary, $primary-light); }
	::v-deep .el-tabs__content {
		flex: 1; min-height: 0; padding: 0; background: $white; overflow: hidden;
		display: flex; flex-direction: column; border-radius: 0 0 $radius $radius;
	}
	::v-deep .el-tab-pane { height: 100%; display: flex; flex-direction: column; }
	::v-deep .el-tabs__nav-wrap::after { display: none; }
}

.tab-scroll-area {
	flex: 1; min-height: 0; overflow-y: auto; padding: 20px;
	&::-webkit-scrollbar { width: 4px; }
	&::-webkit-scrollbar-thumb { background: rgba(14,165,233,0.2); border-radius: 4px; }
}

.tab-html-content {
	background: $white; border-radius: 10px; padding: 0;
	color: $text-dark; font-size: 14px; line-height: 1.8;
}

.comment-inline-form {
	background: $border-light; border-radius: 10px;
	padding: 14px 16px; margin-bottom: 16px;
	::v-deep .el-form-item { margin-bottom: 0; }
	.comment-textarea {
		::v-deep .el-textarea__inner {
			border: 1.5px solid $border; border-radius: 10px; font-size: 13px; color: $text-dark;
			background: $white !important;
			padding: 12px 14px; transition: border-color 0.2s, box-shadow 0.2s;
			&:focus { border-color: $primary; box-shadow: 0 0 0 3px rgba(14,165,233,0.08); }
			&::placeholder { color: $text-dim; }
		}
		::v-deep .el-input__count { background: transparent; color: $text-sub; font-size: 11px; }
	}
	.comment-action-row {
		display: flex; align-items: center; justify-content: space-between; margin-top: 10px;
	}
	.comment-btns { display: flex; gap: 8px; }
}

.submit-btn {
	background: linear-gradient(135deg, $primary, $primary-light) !important;
	border: none !important; color: #fff !important;
	border-radius: 10px !important; padding: 8px 20px !important;
	font-size: 13px !important; font-weight: 600 !important;
	transition: all 0.3s !important;
	i { margin-right: 4px; }
	&:hover { box-shadow: 0 4px 14px rgba(14,165,233,0.3); transform: translateY(-1px); }
}

.reset-btn {
	background: $white !important; border: 1.5px solid $border !important; color: $text-sub !important;
	border-radius: 10px !important; padding: 8px 18px !important;
	font-size: 13px !important; font-weight: 500 !important;
	transition: all 0.25s !important;
	&:hover { color: $primary !important; border-color: $primary !important; background: rgba(14,165,233,0.12) !important; }
}

.comment-section-title {
	display: flex; align-items: center; gap: 8px; margin-bottom: 14px;
	font-size: 15px; font-weight: 700; color: $text-dark;
	i { font-size: 17px; color: $primary; }
	&::after {
		content: ""; flex: 1; height: 1px;
		background: linear-gradient(90deg, $border, transparent); margin-left: 10px;
	}
}

.comment-list {
	display: flex; flex-direction: column; gap: 0;
	.comment-item {
		display: flex; gap: 14px; padding: 16px 0; flex-wrap: wrap;
		border-bottom: 1px solid $border-light; position: relative;
		&:first-child { padding-top: 0; }
		&:last-child { border-bottom: none; padding-bottom: 0; }
		&:hover { .del-comment { opacity: 1; } }
	}
	.comment-top-flag {
		position: absolute; top: 16px; right: 0; color: $gold; font-size: 15px;
	}
	.comment-left {
		flex-shrink: 0;
		.comment-avatar {
			width: 40px; height: 40px; border-radius: 50%;
			border: 2px solid $border-light; box-shadow: 0 2px 8px rgba(0,0,0,0.06);
			::v-deep img { width: 100%; height: 100%; object-fit: cover; border-radius: 50%; }
		}
	}
	.comment-right {
		flex: 1; min-width: 0;
		.comment-header {
			display: flex; align-items: center; justify-content: space-between; margin-bottom: 4px;
		}
		.comment-name { font-weight: 600; font-size: 13px; color: $text-dark; }
		.comment-time { font-size: 11px; color: $text-sub; }
		.comment-text {
			padding: 0 !important; margin: 0; font-size: 13px; color: $text-sub; line-height: 1.7;
		}
	}
	.comment-footer {
		display: flex; align-items: center; justify-content: space-between; margin-top: 8px;
		.comment-interact { display: flex; gap: 16px; }
		.interact-item {
			font-size: 12px; color: $text-sub; cursor: pointer;
			display: flex; align-items: center; gap: 4px; transition: color 0.2s;
			i { font-size: 13px; }
			&:hover { color: $primary; }
			&.active { color: $primary; font-weight: 600; }
		}
		.del-comment {
			opacity: 0; transition: opacity 0.2s;
			border-radius: 14px; border: 1px solid #fca5a5; color: #dc2626; background: $white;
			font-size: 11px; padding: 3px 10px;
			&:hover { background: #fef2f2; }
		}
	}
	.comment-reply-box {
		width: 100%; margin-top: 8px; margin-left: 54px; padding: 10px 14px;
		background: rgba(14,165,233,0.12); border-radius: 8px; border-left: 3px solid $primary;
		font-size: 12px; color: $text-sub; display: flex; align-items: flex-start; gap: 6px;
		i { color: $primary; margin-top: 2px; flex-shrink: 0; }
		::v-deep .ql-editor { padding: 0; display: inline; }
	}
}

.pagination { margin-top: 14px; display: flex; justify-content: center; }
::v-deep #pagination .el-pager li.active,
::v-deep #pagination .el-pager li:hover { color: $primary; }

@media (max-width: 768px) {
	.detail-card { flex-direction: column; padding: 14px; }
	.card-img { width: 100%; min-width: 0; .main-image { height: 160px; } }
}

.price-tag {
	background: linear-gradient(135deg, #f59e0b, #ef4444) !important;
	color: #fff !important;
	font-weight: 700;
}
.free-tag {
	background: linear-gradient(135deg, #10b981, #059669) !important;
	color: #fff !important;
	font-weight: 600;
}

.purchase-dialog {
	.purchase-info, .purchase-qr {
		text-align: center;
		padding: 10px 0;
	}
	.purchase-title {
		font-size: 16px;
		font-weight: 600;
		color: #333;
		margin-bottom: 12px;
	}
	.purchase-price {
		font-size: 32px;
		font-weight: 700;
		color: #ef4444;
		margin-bottom: 18px;
	}
	.purchase-pay-method {
		margin-bottom: 20px;
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 8px;
	}
	.purchase-confirm-btn {
		width: 100%;
		height: 42px;
		font-size: 15px;
		border-radius: 8px;
	}
	.qr-hint {
		color: #666;
		margin-bottom: 12px;
	}
	.qr-code-box {
		margin: 0 auto 16px;
		width: 180px;
		height: 180px;
		border: 1px solid #eee;
		border-radius: 8px;
		overflow: hidden;
		img { width: 100%; height: 100%; }
	}
	.qr-amount {
		font-size: 15px;
		color: #333;
		margin-bottom: 16px;
		strong { color: #ef4444; font-size: 18px; }
	}
	.purchase-done-btn {
		width: 100%;
		height: 42px;
		font-size: 15px;
		border-radius: 8px;
	}
}
</style>
