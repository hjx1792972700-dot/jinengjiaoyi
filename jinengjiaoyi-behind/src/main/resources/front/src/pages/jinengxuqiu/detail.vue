<template>
	<div class="detail-page">
		<div class="detail-topbar">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a @click="backClick">{{item.name}}</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item3"><a href="javascript:void(0);">详情</a></el-breadcrumb-item>
			</el-breadcrumb>
			<el-button class="back-btn" size="small" @click="backClick">
				<template #icon><ArrowLeft /></template>
				返回
			</el-button>
		</div>

		<div class="detail-card">
			<template v-if="detail.leixing !== '需求'">
				<div class="card-img" v-if="detailBanner.length">
					<img v-if="detailBanner[currentImg] && detailBanner[currentImg].substr(0,4)=='http'" :src="detailBanner[currentImg]" class="main-image" />
					<img v-else :src="baseUrl + detailBanner[currentImg]" class="main-image" />
					<div class="img-thumbs" v-if="detailBanner.length > 1">
						<div class="thumb" v-for="(img, idx) in detailBanner" :key="idx"
							:class="{active: currentImg === idx}" @click="currentImg = idx">
							<img v-if="img.substr(0,4)=='http'" :src="img" />
							<img v-else :src="baseUrl + img" />
						</div>
					</div>
				</div>
				<div class="card-img card-img-empty" v-else>
					<el-icon class="empty-picture-icon"><Picture /></el-icon>
				</div>
			</template>

			<div class="card-info">
				<div class="title-row">
					<h2 class="detail-title">{{detail.xuqiubiaoti}}</h2>
					<span class="price-tag" v-if="detail.xuqiufeiyong"><el-icon><Coin /></el-icon> ¥{{detail.xuqiufeiyong}}</span>
					<span class="price-tag negotiate" v-else><el-icon><Coin /></el-icon> 面议</span>
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
					<span class="tag"><el-icon><Document /></el-icon> {{detail.xuqiubianhao || '--'}}</span>
					<span class="tag"><el-icon><CollectionTag /></el-icon> {{detail.jinengfenlei || '--'}}</span>
					<span class="tag"><el-icon><Calendar /></el-icon> {{detail.fabushijian || '--'}}</span>
					<span class="tag"><el-icon><User /></el-icon> {{detail.yonghuxingming || '--'}}</span>
					<span class="tag"><el-icon><Phone /></el-icon> {{detail.shoujihao || '--'}}</span>
					<span class="tag"><el-icon><Medal /></el-icon> 信誉 {{detail.xinyuzhishu || '--'}}</span>
					<span class="tag"><el-icon><View /></el-icon> {{detail.clicknum || '0'}}次浏览</span>
					<span class="tag"><el-icon><StarFilled /></el-icon> {{detail.totalscore || '0'}}分</span>
					<span class="tag status-tag pass" v-if="centerType && detail.sfsh=='是'"><el-icon><CircleCheck /></el-icon> 已通过</span>
					<span class="tag status-tag fail" v-if="centerType && detail.sfsh=='否'"><el-icon><CircleClose /></el-icon> 未通过</span>
					<span class="tag status-tag pending" v-if="centerType && (detail.sfsh=='待审核'||!detail.sfsh)"><el-icon><Clock /></el-icon> 待审核</span>
				</div>

				<div class="desc-line" v-if="detail.xuqiumiaoshu">
					<span class="desc-label">{{detail.leixing=='技能'?'技能描述':'需求描述'}}：</span>{{detail.xuqiumiaoshu}}
				</div>
				<div class="desc-line" v-if="centerType && detail.shhf">
					<span class="desc-label">审核回复：</span>{{detail.shhf}}
				</div>

				<div class="bottom-bar">
					<div class="interact-bar">
						<div v-if="!isThumbsupnum && !isCrazilynum" class="interact-btn" @click="thumbsupOrCrazily(21)">
							<i class="icon iconfont icon-zan07"></i><span>赞({{detail.thumbsupnum}})</span>
						</div>
						<div v-if="!isThumbsupnum && !isCrazilynum" class="interact-btn" @click="thumbsupOrCrazily(22)">
							<i class="icon iconfont icon-cai01"></i><span>踩({{detail.crazilynum}})</span>
						</div>
						<div v-if="isThumbsupnum" class="interact-btn active-zan" @click="cancelThumbsupOrCrazily(21)">
							<i class="icon iconfont icon-zan11"></i><span>已赞({{detail.thumbsupnum}})</span>
						</div>
						<div v-if="isCrazilynum" class="interact-btn active-cai" @click="cancelThumbsupOrCrazily(22)">
							<i class="icon iconfont icon-cai16"></i><span>已踩({{detail.crazilynum}})</span>
						</div>
					</div>
					<div class="action-bar">
						<el-button class="act-btn act-edit" size="mini" v-if="btnAuth('jinengxuqiu','修改')" @click="editClick">
							<template #icon><Edit /></template>
							修改
						</el-button>
						<el-button class="act-btn act-del" size="mini" v-if="btnAuth('jinengxuqiu','删除')" @click="delClick">
							<template #icon><Delete /></template>
							删除
						</el-button>
						<el-button class="act-btn act-chat" size="mini" v-if="btnAuth('jinengxuqiu','私聊')&&detail.id!=mid" @click="chatClick">
							<template #icon><ChatDotRound /></template>
							在线咨询
						</el-button>
						<el-button class="act-btn act-apply" size="mini"
							v-if="btnAuth('jinengxuqiu','申请')"
							@click="onAcross('jiaohuanshenqing','是','','xuqiuzhuangtai','已交换')">
							<template #icon><Promotion /></template>
							申请交换
						</el-button>
						<el-button class="act-btn act-audit" size="mini" v-if="btnAuth('jinengxuqiu','审核') && detail.sfsh!='是'" @click="onSH">
							<template #icon><CircleCheck /></template>
							审核
						</el-button>
						<el-button class="act-btn act-chapter" size="mini" v-if="btnAuth('jinengxuqiu','章节管理')" @click="allchapterClick()">
							<template #icon><Notebook /></template>
							章节
						</el-button>
						<el-button class="act-btn act-chapter" size="mini" v-if="btnAuth('jinengxuqiu','查看评论')" @click="discussClick()">
							<template #icon><ChatLineRound /></template>
							评论
						</el-button>
					</div>
				</div>
			</div>
		</div>

		<el-tabs id="tabsBox" class="detail-tabs" v-model="activeName" type="border-card" v-if="tabsNum>0">
			<el-tab-pane :label="detail.leixing=='技能'?'技能详情':'需求详情'" name="1">
				<div class="tab-scroll-area">
					<div class="ql-snow ql-editor tab-html-content" v-html="detail.xuqiuxiangqing"></div>
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
							<div class="rate-inline">
								<span class="rate-label"><el-icon><StarFilled /></el-icon> 评分</span>
								<el-rate
									v-model="form.score"
									:max='Number(5)'
									:allow-half='false'
									:low-threshold='Number(2)'
									:high-threshold='Number(4)'
									:show-text='true'
									:texts='["极差", "失望", "一般", "满意", "惊喜"]'
									text-color='#f59e0b'
									:colors='["#F7BA2A", "#F7BA2A", "#F7BA2A"]'
									void-color='#C6D1DE'
								></el-rate>
							</div>
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
						<div class="comment-item" v-for="item in infoList" :key="item.id" @mouseenter="discussEnter(item.id)" :id="'discuss' + item.id"
							@mouseleave="discussLeave">
							<div class="comment-top-flag" v-if="item.istop">
								<el-icon><Top /></el-icon>
							</div>
							<div class="comment-left">
								<el-image v-if="item.avatarurl" :src="baseUrl + item.avatarurl" class="comment-avatar"></el-image>
								<el-image v-if="!item.avatarurl" :src="defaultTouxiangUrl" class="comment-avatar"></el-image>
							</div>
							<div class="comment-right">
								<div class="comment-header">
									<span class="comment-name">{{item.nickname}}</span>
									<span class="comment-time">{{item.addtime}}</span>
								</div>
								<div class="ql-snow ql-editor comment-text" v-html="item.content"></div>
								<el-rate v-model="item.score" disabled :max='Number(5)' :colors='["#F7BA2A", "#F7BA2A", "#F7BA2A"]' void-color='#dcdfe6' class="comment-rate"></el-rate>
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
									<el-button class="del-comment" size="mini"
										v-if="showIndex==item.id&&userid==item.userid" @click="discussDel(item.id)">
										<template #icon><Delete /></template>
										删除
									</el-button>
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
						<img :src="mpic?baseUrl + mpic:defaultAvatorUrl" alt="" style="width: 30px;border-radius: 50%;height: 30px;margin: 0 0 0 10px;" />
					</div>
					<div v-else class="left-content">
						<img :src="fpic?baseUrl + fpic:defaultAvatorUrl" alt="" style="width: 30px;border-radius: 50%;height: 30px;margin: 0 10px 0 0;" />
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
						@click="toggleRecord"
						:class="{'voice-record-btn': true, 'voice-recording': isRecording}"
						style="margin: 0 0 0 6px;">
						<template #icon>
							<VideoPause v-if="isRecording" />
							<Microphone v-else />
						</template>
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
					<el-button v-if="showSaveType" size="small" type="primary" @click="saveGood">详情图发送</el-button>
				</div>
				</div>
			</template>
		</el-dialog>
	</div>
</template>

<script>
	import axios from 'axios'
	import { ElLoading } from 'element-plus'
	import touxiangPng from '@/assets/touxiang.png'
	import avatorPng from '@/assets/avator.png'
	import timeMethod from '@/common/timeMethod'
	import {
		WebsocketMixin
	} from '@/mixins/WebsocketMixin'
	export default {
		mixins: [WebsocketMixin],
		//数据集合
		data() {
			return {
				tablename: 'jinengxuqiu',
				baseUrl: '',
				breadcrumbItem: [
					{
						name: '技能需求'
					}
				],
				fromSkill: false,
				title: '',
				detailBanner: [],
				currentImg: 0,
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
					score: 0,
				},
				showIndex: -1,
				infoList: [],
				colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
				rules: {
					content: [
						{ required: true, message: '请输入内容', trigger: 'blur' }
					],
					score: [
						{ required: true, message: '请选择评分', trigger: 'blur' }
					],
				},
				total: 1,
				pageSize: 6,
				totalPage: 1,
				storeupParams: {
					name: '',
					picture: '',
					refid: 0,
					tablename: 'jinengxuqiu',
					userid: Number(localStorage.getItem('frontUserid'))
				},
				isStoreup: false,
				storeupInfo: {},
				isCrazilynum: false,
				isThumbsupnum: false,
				thumbsupOrCrazilyInfo: {},
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
				ftableName: 'jinengxuqiu',
				mpic: localStorage.getItem('frontHeadportrait'),
				// 语音聊天相关
				isRecording: false,
				mediaRecorder: null,
				audioStream: null,
				audioChunks: [],
				recordDuration: 0,
				recordTimer: null,
				showSaveType: true,
				sensitiveWordsArr: [],
				defaultTouxiangUrl: touxiangPng,
				defaultAvatorUrl: avatorPng,
			}
		},
		created() {
			if(this.$route.query.centerType&&this.$route.query.centerType!=0) {
				this.centerType = true
			}
			if(this.$route.query.fromSkill&&this.$route.query.fromSkill!=0) {
				this.fromSkill = true
			}
			if(this.$route.query.storeupType&&this.$route.query.storeupType!=0) {
				this.storeupType = true
			}
			this.init();
		},
	mounted() {
		window.scrollTo(0, 0);
		const body = document.querySelector('.body-containers');
		if (body) {
			body.scrollTop = 0;
			body.style.overflow = '';
		}
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
						this.currentImg = 0;
						this.getSensitiveWords()
						this.title = this.detail.xuqiubiaoti;
						if (this.centerType) {
							this.breadcrumbItem = [{ name: this.fromSkill ? '我的技能' : '我的需求' }];
						} else {
							this.breadcrumbItem = [{ name: '技能市场' }];
						}
						if(this.detail.fengmian) {
							this.detailBanner = this.detail.fengmian.split(",w").length>1?[this.detail.fengmian]:this.detail.fengmian.split(',');
						}
						if(res.data.data.touxiang){
							this.fpic = res.data.data.touxiang.split(',')[0]
						}else if(res.data.data.headportrait){
							this.fpic = res.data.data.headportrait.split(',')[0]
						}
						this.$forceUpdate();
						this.getDiscussList(1);
						if(localStorage.getItem('frontToken')){
							this.getStoreupStatus();
							this.getThumbsupOrCrazilyStatus();
						}

					}
				});
			},
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			async onAcross(acrossTable,crossOptAudit,crossOptPay,statusColumnName,tips,statusColumnValue,type=1){
				if(acrossTable == 'jiaohuanshenqing') {
					let username = localStorage.getItem('username')
					if(this.detail.yonghuzhanghao == username) {
						this.$message.error('这是你自己已拥有的技能，快去技能市场跟别人交换更中意的技能吧～');
						return false
					}
					try {
						let dupRes = await this.$http.get('jiaohuanshenqing/checkDuplicate', {
							params: { xuqiubianhao: this.detail.xuqiubianhao, gongjirenzhanghao: username }
						});
						if(dupRes.data.code == 0 && dupRes.data.data === true) {
							this.$message.error('您已对该技能提交过申请，请勿重复提交');
							return false
						}
					} catch(e) {}
				}
				if(crossOptAudit=='是'&&this.detail.sfsh!='是') {
					this.$message({
						message: "请审核通过后再操作",
						type: "error",
						duration: 1500,
						onClose: () => {
						}
					});
					return
				}
				localStorage.setItem('crossTable',`jinengxuqiu`);
				localStorage.setItem('crossObj', JSON.stringify(this.detail));
				localStorage.setItem('statusColumnName',statusColumnName);
				localStorage.setItem('statusColumnValue',statusColumnValue);
				localStorage.setItem('tips',tips);
				if(statusColumnName!=''&&!statusColumnName.startsWith("[")) {
					var obj = JSON.parse(localStorage.getItem('crossObj'));
					for (var o in obj){
						if(o==statusColumnName && obj[o]==statusColumnValue){
							this.$message({
								type: 'error',
								message: tips,
								duration: 1500
							});
							return
						}
					}
				}
				this.$router.push({path: '/main/' + acrossTable + 'Add', query: {type: 'cross'}});
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
							this.$http.post('jinengxuqiu/update', this.detail).then(res => {});
							this.$message({
								type: 'success',
								message: '收藏成功!',
								duration: 1500,
							});
						}
					});
				}
				if (type == -1 && this.isStoreup) {
					this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: '1', refid: this.detail.id, tablename: 'jinengxuqiu', userid: Number(localStorage.getItem('frontUserid'))}}).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.isStoreup = true;
							this.storeupInfo = res.data.data.list[0];
							let delIds = new Array();
							delIds.push(this.storeupInfo.id);
							this.$http.post('storeup/delete', delIds).then(res => {
								if (res.data.code == 0) {
									this.isStoreup = false;
									this.detail.storeupnum--
									this.$http.post('jinengxuqiu/update', this.detail).then(res => {});
									this.$message({
										type: 'success',
										message: '取消成功!',
										duration: 1500,
									});
								}
							});
						}
					});
				}
			},
			getStoreupStatus(){
				if(localStorage.getItem("frontToken")) {
					this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: '1', refid: this.detail.id, tablename: 'jinengxuqiu', userid: Number(localStorage.getItem('frontUserid'))}}).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.isStoreup = true;
							this.storeupInfo = res.data.data.list[0];
						}
					});
				}
			},
			async thumbsupOrCrazily(type) {
				this.storeupParams.name = this.title;
				this.storeupParams.picture = this.detailBanner[0];
				this.storeupParams.refid = this.detail.id;
				this.storeupParams.type = String(type);
				await this.$http.post('storeup/add', this.storeupParams).then(res => {
					if (res.data.code == 0) {
						let detail = JSON.parse(JSON.stringify(this.detail))
						if (type == 21) detail.thumbsupnum = Number(detail.thumbsupnum) + 1;
						if (type == 22) detail.crazilynum = Number(detail.crazilynum) + 1;
						this.$http.post('jinengxuqiu/update', detail).then(res => {
							this.detail = detail
						});
						this.getThumbsupOrCrazilyStatus();
						this.$message({
							type: 'success',
							message: '操作成功!',
							duration: 1500,
						});
						
					}
				});
				
			},
			async cancelThumbsupOrCrazily(type) {
				let delIds = new Array();
				delIds.push(this.thumbsupOrCrazilyInfo.id);
				await this.$http.post('storeup/delete', delIds).then(res => {
					if (res.data.code == 0) {
						let detail = JSON.parse(JSON.stringify(this.detail))
						if (type == 21) detail.thumbsupnum -= 1;
						if (type == 22) detail.crazilynum -= 1;
						this.$http.post('jinengxuqiu/update', detail).then(res => {
							this.detail = detail
						});
						this.isThumbsupnum = false;
						this.isCrazilynum = false;
						this.$message({
							type: 'success',
							message: '取消成功!',
							duration: 1500,
						});
					}
				});
				
			},
			getThumbsupOrCrazilyStatus() {
				if(localStorage.getItem("frontToken")) {
					this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: '21', refid: this.detail.id, tablename: 'jinengxuqiu', userid: Number(localStorage.getItem('frontUserid'))}}).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.isThumbsupnum = true;
							this.thumbsupOrCrazilyInfo = res.data.data.list[0];
						}
					});
					this.$http.get('storeup/list', {params: {page: 1, limit: 1, type: '22', refid: this.detail.id, tablename: 'jinengxuqiu', userid: Number(localStorage.getItem('frontUserid'))}}).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.isCrazilynum = true;
							this.thumbsupOrCrazilyInfo = res.data.data.list[0];
						}
					});
				}
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
				}else if(this.centerType){
					if(this.fromSkill){
						this.$router.push({path: '/main/center', query: {tab: 'myPublish', publishTab: 'skill'}});
					} else {
						this.$router.push({path: '/main/center', query: {tab: 'jinengxuqiu'}});
					}
				}else{
					this.$router.push({path: '/main/jinengxuqiu'});
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
			getDiscussList(page,type=1) {
				this.$http.get('discussjinengxuqiu/list', {
					params: {
						page, 
						limit: this.pageSize, 
						refid: this.detail.id,
						sort: 'istop',
						order: 'desc',
					}
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
							if(this.$route.query.discussId&&this.$route.query.discussId!=0) {
								this.$nextTick(()=>{
									document.getElementById('discuss' + this.$route.query.discussId).scrollIntoView({
										behavior: 'smooth'
									});
								})
							}
						}
					}
				});
			},
			comzanChange(row){
				if(row.tuserids){
					let arr = row.tuserids.split(',')
					for(let x in arr){
						if(arr[x] == this.userid){
							return true
						}
					}
				}
				return false
			},
			comzanClick(row){
				if(!this.userid){
					return false
				}
				if(!this.comzanChange(row)){
					row.thumbsupnum++
					if(row.tuserids){
						row.tuserids = row.tuserids + ',' + this.userid
					}else {
						row.tuserids = String(this.userid)
					}
					this.$http.post('discussjinengxuqiu/update',row).then(rs=>{
						this.$message.success('点赞成功')
					})
				}else {
					row.thumbsupnum--
					let arr = row.tuserids.split(',')
					for(let x in arr){
						if(arr[x] == this.userid){
							arr.splice(x,1)
						}
					}
					row.tuserids = arr.join(',')
					this.$http.post('discussjinengxuqiu/update',row).then(rs=>{
						this.$message.success('取消成功')
					})
				}
			},
			comcaiChange(row){
				if(row.cuserids){
					let arr = row.cuserids.split(',')
					for(let x in arr){
						if(arr[x] == this.userid){
							return true
						}
					}
				}
				return false
			},
			comcaiClick(row){
				if(!this.userid){
					return false
				}
				if(!this.comcaiChange(row)){
					row.crazilynum++
					if(row.cuserids){
						row.cuserids = row.cuserids + ',' + this.userid
					}else {
						row.cuserids = String(this.userid)
					}
					this.$http.post('discussjinengxuqiu/update',row).then(rs=>{
						this.$message.success('点踩成功')
					})
				}else {
					row.crazilynum--
					let arr = row.cuserids.split(',')
					for(let x in arr){
						if(arr[x] == this.userid){
							arr.splice(x,1)
						}
					}
					row.cuserids = arr.join(',')
					this.$http.post('discussjinengxuqiu/update',row).then(rs=>{
						this.$message.success('取消成功')
					})
				}
			},
			discussEnter(index){
				this.showIndex = index
			},
			discussLeave(){
				this.showIndex = -1
			},
			discussDel(id){
				this.$confirm('是否删除此评论？').then(_ => {
					this.$http.post('discussjinengxuqiu/delete',[id]).then(res=>{
						if(res.data&&res.data.code==0){
							this.addDiscussNum(1)
							this.$message({
								type: 'success',
								message: '删除成功!',
								duration: 1500,
								onClose: () => {
									this.getDiscussList(1);
								}
							});
						}
					})
				}).catch(_ => {});
			},
			submitForm(formName) {
				for(var i=0; i<this.sensitiveWordsArr.length; i++){
					//全局替换
					var reg = new RegExp(this.sensitiveWordsArr[i],"g");
					//判断内容中是否包括敏感词
					if (this.form.content.indexOf(this.sensitiveWordsArr[i]) > -1) {
						// 将敏感词替换为 **
						this.form.content = this.form.content.replace(reg,"**");
					}
				}
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.$http.get('discussjinengxuqiu/page',{params: {
							page: 1,
							limit: 1,
							refid: this.detail.id,
							userid: Number(localStorage.getItem('frontUserid'))
						}}).then(rs=>{
							if(rs.data.data.list.length){
								this.$message({
									type: 'error',
									message: '每个用户只能评论一次!',
									duration: 1500
								});
								return false
							}
							this.addComments()
						})
					} else {
						return false;
					}
				});
			},
			addComments(content=null,type=1) {
				let data = JSON.parse(JSON.stringify(this.form))
				data.refid = this.detail.id;
				data.avatarurl = localStorage.getItem('frontHeadportrait')?localStorage.getItem('frontHeadportrait'):'';
				if(type==2) {
					data.content = content
				}
				this.$http.post('discussjinengxuqiu/add', data).then(rs2 => {
					if (rs2.data.code == 0) {
						if(type==1) {
							this.form.content = '';
							this.form.score = 0
						}
						this.addDiscussNum(2)
						this.getDiscussList(1,type);
						this.$message({
							type: 'success',
							message: '评论成功!',
							duration: 1500,
						});
					}
				});
			},
			resetForm(formName) {
				this.$refs[formName].resetFields();
			},
			addDiscussNum(type){
				if(type==2){
					this.detail.discussnum++
				}else if(type==1){
					if(this.detail.discussnum!=0){
						this.detail.discussnum--
					}else{
						this.detail.discussnum = 0
					}
				}
				this.$http.get('discussjinengxuqiu/list',{params: {
					page: 1,
					limit: 10000,
					refid: this.detail.id,
				}}).then(rs=>{
					let score = 0
					for(let x in rs.data.data.list){
						score += Number(rs.data.data.list[x].score)
					}
					this.detail.totalscore = rs.data.data.list.length?Number((score / Number(rs.data.data.list.length)).toFixed(2)):0
					this.$http.post('jinengxuqiu/update',this.detail).then(res=>{})
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
				if (this.detail.leixing === '技能' && this.detail.sfsh === '是') {
					this.$alert('您的该技能已进入技能市场，如有变动请联系管理员！！', '提示', {
						confirmButtonText: '我知道了',
						type: 'warning'
					});
					return;
				}
				this.$router.push(`/main/jinengxuqiuAdd?type=edit&&id=${this.detail.id}`);
			},
			// 删除
			async delClick(){
				if (this.detail.leixing === '技能' && this.detail.sfsh === '是') {
					this.$alert('您的该技能已进入技能市场，如有变动请联系管理员！！', '提示', {
						confirmButtonText: '我知道了',
						type: 'warning'
					});
					return;
				}
				await this.$confirm('是否删除此技能需求？') .then(_ => {
					this.$http.post('jinengxuqiu/delete', [this.detail.id]).then(async res => {
						if (res.data.code == 0) {
							this.$http.get('storeup/list',{params:{
								page: 1,
								limit: 100,
								refid: this.detail.id,
								tablename: 'jinengxuqiu',
							}}).then(async obj=>{
								if(obj.data&&obj.data.code==0){
									let arr = []
									for(let x in obj.data.data.list){
										arr.push(obj.data.data.list[x].id)
									}
									if(arr.length){
										await this.$http.post('storeup/delete',arr).then(()=>{})
									}
									this.$message({
										type: 'success',
										message: '删除成功!',
										duration: 1500,
										onClose: () => {
											history.back()
										}
									});
								}
							})
						}
					});
				}).catch(_ => {});
			},
		
			chatClick() {
				let user = JSON.parse(localStorage.getItem('sessionForm'))
				let chatUser = ''
				let chatUserName = ''
				let chatUserName2 = ''
				if(!chatUser||localStorage.getItem('frontSessionTable') == chatUser) {
					chatUserName2 = ''
					chatUser = 'yonghu'
					chatUserName = 'yonghuzhanghao'
				}
				if(localStorage.getItem('frontSessionTable') == chatUser) {
					if(this.detail[chatUserName] == user[chatUserName]){
						this.$message.error('不能给自己发信息')
						return false
					}
				}
				let params = {}
				this.ftableName = chatUser
				params[chatUserName2?chatUserName2:chatUserName] = this.detail[chatUserName]
				this.$http.get(`${chatUser}/query`, {
					params: params
				}).then(res => {
					if (res.data && res.data.code == 0) {
						this.fid = res.data.data.id
						this.initWebSocket(this.fid)
						this.fname = res.data.data.yonghuxingming || res.data.data[chatUserName2?chatUserName2:chatUserName]
						if (res.data.data.touxiang) {
							this.fpic = res.data.data.touxiang.split(',')[0]
						} else if (res.data.data.headportrait) {
							this.fpic = res.data.data.headportrait.split(',')[0]
						}
						this.getChatList()
						this.chatVisible = true
					}
				})
			},
			saveGood(){
				this.showSaveType = false
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
						this.$http.post('chatmessage/add', {
							uid: Number(localStorage.getItem('frontUserid')),
							fid: this.fid,
							content: this.detailBanner[0],
							format: 2
						}).then(res2 => {
							this.websocketSend(this.detailBanner[0])
							this.chatForm = {
								content: ''
							}
							this.getChatList()
						})
					}
				})
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
				this.$router.push({path: '/main/chapterjinengxuqiu', query: params});
			},
			discussClick (){
				let params = {
					refid: this.detail.id
				}
				if(this.centerType){
					params.centerType = 1
				}
				this.$router.push({path: '/main/discussjinengxuqiu', query: params});
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
$primary: #0ea5e9;
$primary-light: #38bdf8;
$purple: #38bdf8;
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
	.empty-picture-icon { font-size: 36px; color: $text-dim; }
}

.card-info { flex: 1; display: flex; flex-direction: column; min-width: 0; justify-content: space-between; }

.title-row {
	display: flex; align-items: center; gap: 10px; margin-bottom: 8px;
	.detail-title { font-size: 18px; color: $text-dark; font-weight: 700; margin: 0; flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
	.price-tag {
		font-size: 16px; font-weight: 700; color: $red; white-space: nowrap;
		.el-icon { margin-right: 2px; vertical-align: middle; color: $gold; }
		&.negotiate { color: $text-sub; font-weight: 500; font-size: 13px; }
	}
	.collect-btn {
		display: flex; align-items: center; gap: 3px; cursor: pointer; padding: 5px 14px;
		border-radius: 18px; border: 1.5px solid $border; color: $text-sub; font-size: 12px;
		transition: all 0.25s; white-space: nowrap; flex-shrink: 0; background: $white;
		.el-icon { font-size: 13px; }
		&:hover { color: $gold; border-color: $gold; background: #fffbeb; }
		&.active { background: linear-gradient(135deg, $gold, #f97316); border-color: $gold; color: #fff;
			.el-icon { color: #fff; }
			&:hover { box-shadow: 0 3px 12px rgba(245,158,11,0.3); }
		}
	}
}

.meta-tags {
	display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 8px;
	.tag {
		font-size: 12px; color: $text-sub; background: $border-light; padding: 4px 10px;
		border-radius: 6px; display: inline-flex; align-items: center; gap: 4px; white-space: nowrap;
		.el-icon { font-size: 12px; color: $text-sub; }
	}
	.status-tag {
		font-weight: 600;
		&.pass { background: #f0fdf4; color: #16a34a; .el-icon { color: #16a34a; } }
		&.fail { background: #fef2f2; color: #dc2626; .el-icon { color: #dc2626; } }
		&.pending { background: #fffbeb; color: #d97706; .el-icon { color: #d97706; } }
	}
}

.desc-line {
	font-size: 13px; color: $text-sub; line-height: 1.6; margin-bottom: 4px;
	overflow: hidden; text-overflow: ellipsis; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
	.desc-label { font-weight: 600; color: $text-dark; }
}

.bottom-bar {
	display: flex; align-items: center; justify-content: space-between; gap: 10px;
	margin-top: 8px; padding-top: 10px; border-top: 1.5px solid $border-light;
}

.interact-bar {
	display: flex; gap: 6px; flex-shrink: 0;
	.interact-btn {
		display: flex; align-items: center; gap: 3px; cursor: pointer; padding: 5px 14px;
		border-radius: 18px; border: 1.5px solid $border; color: $text-sub; font-size: 12px;
		transition: all 0.25s; background: $white;
		.icon { font-size: 12px; }
		&:hover { color: $primary; border-color: $primary; background: rgba(14,165,233,0.12); }
	}
	.active-zan {
		background: linear-gradient(135deg, $primary, #5b21b6); border-color: $primary; color: #fff;
		.icon { color: #fff; }
		&:hover { box-shadow: 0 3px 12px rgba(14,165,233,0.3); }
	}
	.active-cai {
		background: $text-sub; border-color: $text-sub; color: #fff;
		.icon { color: #fff; }
	}
}

.action-bar {
	display: flex; gap: 6px; flex-wrap: wrap; justify-content: flex-end;
	.act-btn { border-radius: 8px; font-size: 12px; padding: 6px 14px; font-weight: 500; transition: all 0.3s; }
	.act-edit { background: linear-gradient(135deg, $primary, $purple); border: none; color: #fff; &:hover { box-shadow: 0 4px 14px rgba(14,165,233,0.3); transform: translateY(-1px); } }
	.act-del { background: #fff; border: 1.5px solid #fca5a5; color: #dc2626; &:hover { background: #fef2f2; } }
	.act-chat { background: linear-gradient(135deg, $green, #059669); border: none; color: #fff; &:hover { box-shadow: 0 4px 14px rgba(34,197,94,0.3); transform: translateY(-1px); } }
	.act-apply { background: linear-gradient(135deg, $gold, #ea580c); border: none; color: #fff; &:hover { box-shadow: 0 4px 14px rgba(245,158,11,0.3); transform: translateY(-1px); } }
	.act-audit { background: #fff; border: 1.5px solid #fde68a; color: #d97706; &:hover { background: #fffbeb; } }
	.act-chapter { background: $white; border: 1.5px solid $border; color: $text-sub; &:hover { color: $primary; border-color: $primary; background: rgba(14,165,233,0.12); } }
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
	::v-deep .el-tabs__header .el-tabs__item.is-active::after { background: linear-gradient(90deg, $primary, $purple); }
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
	.rate-inline {
		display: flex; align-items: center; gap: 8px;
		.rate-label {
			font-size: 13px; font-weight: 600; color: $text-dark; white-space: nowrap;
			.el-icon { color: $gold; margin-right: 2px; vertical-align: middle; }
		}
	}
	.comment-btns { display: flex; gap: 8px; }
}

.submit-btn {
	background: linear-gradient(135deg, $primary, $purple) !important;
	border: none !important; color: #fff !important;
	border-radius: 10px !important; padding: 8px 20px !important;
	font-size: 13px !important; font-weight: 600 !important;
	transition: all 0.3s !important;
	.el-icon { margin-right: 4px; vertical-align: middle; }
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
	> .el-icon { font-size: 17px; color: $primary; }
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
		.comment-rate { padding: 4px 0 0; }
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
		> .el-icon { color: $primary; margin-top: 2px; flex-shrink: 0; }
		::v-deep .ql-editor { padding: 0; display: inline; }
	}
}

.el-rate { padding: 2px 0; line-height: 20px; }

.pagination { margin-top: 14px; display: flex; justify-content: center; }
::v-deep #pagination .el-pager li.active,
::v-deep #pagination .el-pager li:hover { color: $primary; }

.chat-content {
	padding-bottom: 10px; width: 100%; margin-bottom: 10px;
	max-height: 300px; height: 300px; overflow-y: scroll;
	border: 1px solid $border; background: $white; border-radius: 10px;
	.addtime { width: 100%; text-align: center; font-size: 12px; color: $text-sub; padding: 6px 0; }
	.left-content { float: left; margin-bottom: 8px; padding: 8px; max-width: 80%; display: flex; align-items: center; }
	.right-content { float: right; margin-bottom: 8px; padding: 8px; max-width: 80%; display: flex; align-items: center; }
}
.clear-float { clear: both; }

@media (max-width: 768px) {
	.detail-card { flex-direction: column; padding: 14px; }
	.card-img { width: 100%; min-width: 0; .main-image { height: 160px; } }
}
</style>
