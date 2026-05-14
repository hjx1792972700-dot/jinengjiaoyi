<template>
	<div class="apply-detail-page">
		<div class="page-header">
			<div class="header-left">
				<el-breadcrumb :separator="'/'">
					<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
					<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a @click="backClick">{{item.name}}</a></el-breadcrumb-item>
					<el-breadcrumb-item class="item3"><a href="javascript:void(0);">申请详情</a></el-breadcrumb-item>
				</el-breadcrumb>
			</div>
			<div class="header-right">
				<span class="status-badge done" v-if="detail.sfsh=='已完成'"><el-icon><CircleCheck /></el-icon> 已完成</span>
				<span class="status-badge confirming" v-else-if="detail.sfsh=='待确认'"><el-icon><Select /></el-icon> 待确认</span>
				<span class="status-badge fail" v-else-if="detail.sfsh=='否'"><el-icon><CircleClose /></el-icon> 已拒绝</span>
				<span class="status-badge pending" v-else><el-icon><Clock /></el-icon> 待处理</span>
				<el-button class="back-btn" size="small" @click="backClick">
					<template #icon><ArrowLeft /></template>
					返回
				</el-button>
			</div>
		</div>

		<div class="exchange-flow">
			<div class="flow-card applicant-card">
				<div class="card-label"><el-icon><Promotion /></el-icon> 申请人</div>
				<div class="card-avatar">
					<span class="avatar-text">{{(detail.gongjirenxingming || '?').substring(0,1)}}</span>
				</div>
				<div class="card-name">{{detail.gongjirenxingming || '--'}}</div>
				<div class="card-tags">
					<span class="mini-tag"><el-icon><Phone /></el-icon> {{detail.lianxifangshi || '--'}}</span>
				</div>
			</div>

			<div class="flow-center">
				<div class="skill-item left-skill">
					<div class="skill-item-label">申请人提供</div>
					<div class="skill-item-name">{{detail.xuanzejinengbiaoti || '待选择'}}</div>
				</div>
				<div class="exchange-badge">
					<el-icon><Sort /></el-icon>
					<span>技能交换</span>
				</div>
				<div class="skill-item right-skill">
					<div class="skill-item-label">发布者提供</div>
					<div class="skill-item-name">{{detail.xuqiubiaoti || '--'}}</div>
				</div>
			</div>

			<div class="flow-card owner-card">
				<div class="card-label"><el-icon><Goods /></el-icon> 技能发布者</div>
				<div class="card-avatar owner">
					<span class="avatar-text">{{(detail.yonghuxingming || '?').substring(0,1)}}</span>
				</div>
				<div class="card-name">{{detail.yonghuxingming || '--'}}</div>
				<div class="card-tags">
					<span class="mini-tag"><el-icon><Phone /></el-icon> {{detail.shoujihao || '--'}}</span>
				</div>
			</div>
		</div>

		<div class="detail-bottom">
			<div class="exchange-detail-card" v-if="(detail.sfsh=='待确认' || detail.sfsh=='已完成') && detail.xuanzejinengbiaoti">
				<div class="exchange-detail-title">
					<el-icon><Sort /></el-icon> 交换信息
				</div>
				<div class="exchange-detail-body">
					<div class="exchange-side">
						<div class="exchange-side-label">{{detail.yonghuxingming}} 提供</div>
						<div class="exchange-side-skill">
							<div class="exchange-skill-cover" v-if="detailBanner.length">
								<img v-if="detailBanner[0] && detailBanner[0].substr(0,4)=='http'" :src="detailBanner[0]" />
								<img v-else :src="baseUrl + detailBanner[0]" />
							</div>
							<div class="exchange-skill-cover placeholder" v-else><el-icon><Box /></el-icon></div>
							<div class="exchange-skill-name">{{detail.xuqiubiaoti}}</div>
							<div class="exchange-skill-cat">{{detail.jinengfenlei}}</div>
						</div>
					</div>
					<div class="exchange-arrow">
						<el-icon><Sort /></el-icon>
					</div>
					<div class="exchange-side">
						<div class="exchange-side-label">{{detail.gongjirenxingming}} 提供</div>
						<div class="exchange-side-skill">
							<div class="exchange-skill-cover" v-if="selectedSkillBanner.length">
								<img v-if="selectedSkillBanner[0] && selectedSkillBanner[0].substr(0,4)=='http'" :src="selectedSkillBanner[0]" />
								<img v-else :src="baseUrl + selectedSkillBanner[0]" />
							</div>
							<div class="exchange-skill-cover placeholder" v-else><el-icon><Box /></el-icon></div>
							<div class="exchange-skill-name">{{detail.xuanzejinengbiaoti}}</div>
							<div class="exchange-skill-cat" v-if="selectedSkillCategory">{{selectedSkillCategory}}</div>
						</div>
					</div>
				</div>
			</div>

			<div class="confirm-action-card" v-if="isApplicant && detail.sfsh=='待确认'">
				<div class="confirm-hint">
					<el-icon><InfoFilled /></el-icon>
					<span>对方已选择你的技能 <strong>{{detail.xuanzejinengbiaoti}}</strong> 进行交换，请确认是否同意</span>
				</div>
				<div class="confirm-buttons">
					<button type="button" class="confirm-btn accept" @click="confirmExchange" :disabled="confirmLoading">
						<el-icon><Check /></el-icon> 确认交换
					</button>
					<button type="button" class="confirm-btn decline" @click="showDeclineDialog">
						<el-icon><Close /></el-icon> 拒绝交换
					</button>
				</div>
			</div>

			<div class="remark-card" v-if="detail.shenqingbeizhu">
				<div class="remark-icon"><el-icon><ChatLineSquare /></el-icon></div>
				<div class="remark-body">
					<div class="remark-title">申请备注</div>
					<div class="remark-text">{{detail.shenqingbeizhu}}</div>
				</div>
			</div>
			<div class="remark-card reply-card" v-if="detail.shhf">
				<div class="remark-icon reply"><el-icon><ChatDotRound /></el-icon></div>
				<div class="remark-body">
					<div class="remark-title">回复信息</div>
					<div class="remark-text">{{detail.shhf}}</div>
				</div>
			</div>

			<div class="bottom-row">
				<div class="time-info">
					<el-icon><Clock /></el-icon> 申请时间：{{detail.shenqingshijian || '--'}}
				</div>
				<div class="action-bar">
					<el-button class="act-btn act-edit" size="small" v-if="isApplicant && detail.sfsh=='待审核'" @click="editClick">
						<template #icon><Edit /></template>
						修改
					</el-button>
					<el-button class="act-btn act-del" size="small" v-if="isApplicant && (detail.sfsh=='待审核' || detail.sfsh=='否')" @click="delClick">
						<template #icon><Delete /></template>
						删除
					</el-button>
				</div>
			</div>
		</div>

		<el-dialog v-model="declineVisible" title="拒绝交换" width="440px" :close-on-click-modal="false" top="30vh">
			<div class="decline-hint">
				<p>确定拒绝与 <strong>{{detail.yonghuxingming}}</strong> 的技能交换？</p>
				<p class="decline-sub">你的技能：{{detail.xuanzejinengbiaoti}} / 对方技能：{{detail.xuqiubiaoti}}</p>
			</div>
			<el-input type="textarea" :rows="3" v-model="declineReason" placeholder="请输入拒绝原因（选填），对方将看到此信息"></el-input>
			<template #footer>
				<el-button size="small" @click="declineVisible = false">取消</el-button>
				<el-button type="danger" size="small" @click="submitDecline" :loading="confirmLoading">确认拒绝</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				tablename: 'jiaohuanshenqing',
				baseUrl: '',
				breadcrumbItem: [{ name: '交换申请' }],
				title: '',
				detailBanner: [],
				selectedSkillBanner: [],
				selectedSkillCategory: '',
				currentImg: 0,
				userid: Number(localStorage.getItem('frontUserid')),
				id: 0,
				detail: {},
				centerType: false,
				storeupType: false,
				declineVisible: false,
				declineReason: '',
				confirmLoading: false,
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
			username() { return localStorage.getItem('username') },
			isApplicant() { return this.detail.gongjirenzhanghao === this.username },
			isSkillOwner() { return this.detail.yonghuzhanghao === this.username },
		},
		methods: {
			init() {
				this.id = this.$route.query.id
				this.baseUrl = this.$config.baseUrl;
				this.$http.get(this.tablename + '/detail/' + this.id, {}).then(res => {
					if (res.data.code == 0) {
						this.detail = res.data.data;
						this.currentImg = 0;
						this.title = this.detail.xuqiubiaoti;
					if(this.detail.fengmian) {
						this.detailBanner = this.detail.fengmian.split(",w").length>1?[this.detail.fengmian]:this.detail.fengmian.split(',');
					}
					if(this.detail.xuanzejinengid) {
						this.loadSelectedSkillCover(this.detail.xuanzejinengid);
					}
					this.$forceUpdate();
					}
				});
			},
			loadSelectedSkillCover(skillId) {
				this.$http.get('jinengxuqiu/detail/' + skillId, {}).then(res => {
					if (res.data.code == 0 && res.data.data) {
						let data = res.data.data;
						if (data.fengmian) {
							let fm = data.fengmian;
							this.selectedSkillBanner = fm.split(",w").length > 1 ? [fm] : fm.split(',');
						}
						if (data.jinengfenlei) {
							this.selectedSkillCategory = data.jinengfenlei;
						}
						this.$forceUpdate();
					}
				});
			},
			async confirmExchange() {
				try {
					await this.$confirm('确认与对方完成技能交换？', '确认交换', {
						confirmButtonText: '确认', cancelButtonText: '取消', type: 'success'
					});
				} catch(e) { return; }
				this.confirmLoading = true;
				try {
					const res = await this.$http.post('jiaohuanshenqing/confirmExchange', { id: this.detail.id });
					if (res.data.code == 0) {
						this.$message({ type: 'success', message: '交换已确认！', duration: 1500 });
						this.init();
					} else {
						this.$message.error(res.data.msg || '操作失败');
					}
				} catch(e) {
					this.$message.error('网络异常，请重试');
				}
				this.confirmLoading = false;
			},
			showDeclineDialog() {
				this.declineReason = '';
				this.declineVisible = true;
			},
			async submitDecline() {
				this.confirmLoading = true;
				try {
					const res = await this.$http.post('jiaohuanshenqing/rejectExchange', {
						id: this.detail.id,
						shhf: this.declineReason
					});
					if (res.data.code == 0) {
						this.declineVisible = false;
						this.$message({ type: 'success', message: '已拒绝该交换', duration: 1500 });
						this.init();
					} else {
						this.$message.error(res.data.msg || '操作失败');
					}
				} catch(e) {
					this.$message.error('网络异常，请重试');
				}
				this.confirmLoading = false;
			},
			backClick(){
				if(this.storeupType){
					history.back()
				}else if(this.centerType){
					this.$router.push({path: '/main/center', query: {tab: 'jiaohuanshenqing'}});
				}else{
					this.$router.push({path: '/main/jiaohuanshenqing'});
				}
			},
			btnAuth(tableName,key){
				if(this.centerType){ return this.isBackAuth(tableName,key) }
				else{ return this.isAuth(tableName,key) }
			},
			editClick(){
				this.$router.push('/main/jiaohuanshenqingAdd?type=edit&&id=' + this.detail.id);
			},
			async delClick(){
				await this.$confirm('是否删除此交换申请？').then(_ => {
					this.$http.post('jiaohuanshenqing/delete', [this.detail.id]).then(res => {
						if (res.data.code == 0) {
							this.$message({ type: 'success', message: '删除成功!', duration: 1500, onClose: () => { history.back() } });
						}
					});
				}).catch(_ => {});
			},
		},
		components: {}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #38bdf8;
$purple: #7dd3fc;
$green: #34d399;
$gold: #fbbf24;
$red: #fb7185;
$text-dark: #f1f5f9;
$text-sub: #cbd5e1;
$text-dim: #94a3b8;
$border: rgba(14,165,233,0.15);
$bg: #0c1222;
$white: #0f172a;
$radius: 14px;

.apply-detail-page {
	max-width: 900px;
	margin: 0 auto;
	padding: 16px 20px;
	min-height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
	background: $bg;
	border-radius: 12px;
}

.page-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 16px;
	flex-shrink: 0;
	.header-left { flex: 1; }
	.header-right {
		display: flex; align-items: center; gap: 10px;
	}
	.status-badge {
		display: inline-flex; align-items: center; gap: 4px;
		padding: 5px 14px; border-radius: 20px; font-size: 13px; font-weight: 600;
		.el-icon { font-size: 14px; }
		&.done { background: rgba(52,211,153,0.12); color: #34d399; border: 1px solid rgba(52,211,153,0.25); }
		&.confirming { background: rgba(14,165,233,0.12); color: #0ea5e9; border: 1px solid rgba(14,165,233,0.25); }
		&.fail { background: rgba(251,113,133,0.12); color: #fb7185; border: 1px solid rgba(251,113,133,0.25); }
		&.pending { background: rgba(245,158,11,0.12); color: #fbbf24; border: 1px solid rgba(245,158,11,0.25); }
	}
	.back-btn {
		border-radius: 20px; border: 1px solid $border; color: $text-sub; font-size: 12px; padding: 5px 14px;
		&:hover { color: $primary; border-color: $primary; background: rgba(14,165,233,0.04); }
	}
}

.exchange-flow {
	display: flex;
	align-items: stretch;
	gap: 0;
	flex-shrink: 0;
}

.flow-card {
	flex: 1;
	min-width: 0;
	background: $white;
	border-radius: $radius;
	border: 1px solid $border;
	box-shadow: 0 2px 12px rgba(0,0,0,0.04);
	padding: 20px 16px;
	display: flex;
	flex-direction: column;
	align-items: center;
	text-align: center;
	position: relative;
}

.card-label {
	position: absolute;
	top: 0;
	left: 50%;
	transform: translateX(-50%);
	padding: 3px 14px;
	border-radius: 0 0 8px 8px;
	font-size: 11px;
	font-weight: 700;
	letter-spacing: 0.5px;
	white-space: nowrap;
	.el-icon { margin-right: 3px; vertical-align: middle; }
}
.applicant-card .card-label {
	background: linear-gradient(135deg, $primary, #6d5cfc);
	color: #fff;
}
.owner-card .card-label {
	background: linear-gradient(135deg, $purple, #8b6ffa);
	color: #fff;
}

.card-avatar {
	width: 56px;
	height: 56px;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin: 16px 0 10px;
	background: linear-gradient(135deg, #0ea5e9, #6d5cfc);
	.avatar-text {
		font-size: 24px;
		font-weight: 700;
		color: #ffffff;
		line-height: 1;
	}
	&.owner {
		background: linear-gradient(135deg, #7c3aed, #38bdf8);
	}
}

.card-name {
	font-size: 16px;
	font-weight: 700;
	color: #ffffff;
	margin-bottom: 2px;
}

.card-account {
	font-size: 12px;
	color: $text-sub;
	margin-bottom: 10px;
}

.card-tags {
	display: flex;
	flex-wrap: wrap;
	gap: 6px;
	justify-content: center;
	.mini-tag {
		font-size: 12px;
		color: #e2e8f0;
		background: $bg;
		padding: 4px 10px;
		border-radius: 4px;
		border: 1px solid rgba(14,165,233,0.12);
		display: inline-flex;
		align-items: center;
		gap: 3px;
		.el-icon { font-size: 11px; color: $text-sub; }
	}
}

.flow-center {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 0 10px;
	flex-shrink: 0;
	width: 220px;
	align-self: center;
	gap: 10px;
}

.skill-item {
	width: 100%;
	padding: 10px 14px;
	border-radius: 10px;
	text-align: center;
	.skill-item-label {
		font-size: 11px;
		color: $text-sub;
		font-weight: 600;
		letter-spacing: 0.5px;
		margin-bottom: 4px;
	}
	.skill-item-name {
		font-size: 13px;
		font-weight: 700;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	&.left-skill {
		background: rgba(14,165,233,0.08);
		border: 1px solid rgba(14,165,233,0.2);
		.skill-item-name { color: #7dd3fc; }
	}
	&.right-skill {
		background: rgba(167,139,250,0.08);
		border: 1px solid rgba(167,139,250,0.2);
		.skill-item-name { color: #c4b5fd; }
	}
}

.exchange-badge {
	display: flex;
	align-items: center;
	gap: 6px;
	padding: 5px 14px;
	border-radius: 20px;
	background: linear-gradient(135deg, rgba(52,211,153,0.1), rgba(14,165,233,0.08));
	border: 1px solid rgba(52,211,153,0.2);
	white-space: nowrap;
	.el-icon { font-size: 14px; color: $green; }
	span { font-size: 11px; font-weight: 600; color: $green; }
}

.detail-bottom {
	margin-top: 14px;
	flex: 1;
	min-height: 0;
	display: flex;
	flex-direction: column;
	gap: 10px;
	overflow-y: auto;
}

.exchange-detail-card {
	background: $white;
	border-radius: $radius;
	border: 1px solid rgba(14,165,233,0.2);
	padding: 16px;
	box-shadow: 0 2px 12px rgba(14,165,233,0.06);
}
.exchange-detail-title {
	font-size: 15px;
	font-weight: 700;
	color: #38bdf8;
	margin-bottom: 14px;
	display: flex;
	align-items: center;
	gap: 6px;
	.el-icon { font-size: 16px; }
}
.exchange-detail-body {
	display: flex;
	align-items: center;
	gap: 12px;
}
.exchange-side {
	flex: 1;
	min-width: 0;
	text-align: center;
}
.exchange-side-label {
	font-size: 13px;
	color: #e2e8f0;
	margin-bottom: 8px;
	font-weight: 600;
}
.exchange-side-skill {
	background: $bg;
	border-radius: 10px;
	border: 1px solid $border;
	padding: 12px 10px;
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 6px;
}
.exchange-skill-cover {
	width: 48px;
	height: 48px;
	border-radius: 8px;
	overflow: hidden;
	flex-shrink: 0;
	img { width: 100%; height: 100%; object-fit: cover; display: block; }
	&.placeholder {
		background: linear-gradient(135deg, rgba(14,165,233,0.08), rgba(167,139,250,0.08));
		display: flex;
		align-items: center;
		justify-content: center;
		.el-icon { font-size: 22px; color: $text-dim; }
	}
}
.exchange-skill-name {
	font-size: 14px;
	font-weight: 600;
	color: #ffffff;
	overflow: hidden;
	text-overflow: ellipsis;
	white-space: nowrap;
	max-width: 100%;
}
.exchange-skill-cat {
	font-size: 11px;
	color: $text-sub;
}
.exchange-arrow {
	flex-shrink: 0;
	width: 40px;
	height: 40px;
	border-radius: 50%;
	background: linear-gradient(135deg, $primary, $purple);
	display: flex;
	align-items: center;
	justify-content: center;
	.el-icon { font-size: 20px; color: #fff; }
}

.confirm-action-card {
	background: linear-gradient(135deg, rgba(14,165,233,0.15), rgba(167,139,250,0.1));
	border-radius: $radius;
	border: 1px solid rgba(14,165,233,0.25);
	padding: 14px 16px;
}
.confirm-hint {
	display: flex;
	align-items: flex-start;
	gap: 8px;
	margin-bottom: 12px;
	font-size: 14px;
	color: #f1f5f9;
	line-height: 1.5;
	.el-icon { font-size: 16px; color: #38bdf8; flex-shrink: 0; margin-top: 2px; }
	strong { color: #38bdf8; }
}
.confirm-buttons {
	display: flex;
	gap: 10px;
}
.confirm-btn {
	flex: 1;
	padding: 10px 0;
	border-radius: 8px;
	font-size: 14px;
	font-weight: 600;
	cursor: pointer;
	border: none;
	display: inline-flex;
	align-items: center;
	justify-content: center;
	gap: 6px;
	transition: all 0.3s;
	&.accept {
		background: linear-gradient(135deg, #22c55e, #059669);
		color: #fff;
		box-shadow: 0 2px 10px rgba(52,211,153,0.25);
		&:hover { box-shadow: 0 4px 16px rgba(52,211,153,0.35); transform: translateY(-1px); }
	}
	&.decline {
		background: $white;
		color: $red;
		border: 1px solid rgba(251,113,133,0.3);
		&:hover { background: rgba(251,113,133,0.06); border-color: $red; }
	}
	&:disabled {
		opacity: 0.6;
		cursor: not-allowed;
		transform: none !important;
	}
}

.decline-hint {
	margin-bottom: 12px;
	p { font-size: 14px; color: #f1f5f9; line-height: 1.5; margin: 0 0 4px; }
	.decline-sub { font-size: 13px; color: #cbd5e1; }
	strong { color: #38bdf8; }
}

.remark-card {
	display: flex;
	gap: 12px;
	align-items: flex-start;
	background: $white;
	border-radius: 10px;
	border: 1px solid $border;
	padding: 12px 16px;
	box-shadow: 0 1px 6px rgba(0,0,0,0.03);
	.remark-icon {
		width: 36px;
		height: 36px;
		border-radius: 8px;
		background: linear-gradient(135deg, rgba(14,165,233,0.08), rgba(14,165,233,0.15));
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		.el-icon { font-size: 16px; color: $primary; }
		&.reply {
			background: linear-gradient(135deg, rgba(167,139,250,0.08), rgba(167,139,250,0.15));
			.el-icon { color: $purple; }
		}
	}
	.remark-body { flex: 1; min-width: 0; }
	.remark-title { font-size: 12px; color: $text-sub; margin-bottom: 2px; font-weight: 600; }
	.remark-text { font-size: 14px; color: #f1f5f9; line-height: 1.6; }
}

.bottom-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-top: auto;
	padding-top: 10px;
}

.time-info {
	font-size: 13px;
	color: $text-sub;
	display: flex;
	align-items: center;
	gap: 4px;
	.el-icon { font-size: 13px; vertical-align: middle; }
}

.action-bar {
	display: flex;
	gap: 8px;
	flex-wrap: wrap;
	.act-btn { border-radius: 8px; font-size: 12px; padding: 7px 14px; font-weight: 500; transition: all 0.3s; }
	.act-edit { background: linear-gradient(135deg, $primary, $purple); border: none; color: #fff; &:hover { box-shadow: 0 3px 12px rgba(14,165,233,0.25); } }
	.act-del { background: $white; border: 1px solid #fca5a5; color: $red; &:hover { background: #fef2f2; border-color: $red; } }
}
</style>
