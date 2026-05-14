<template>
	<div class="addEdit-block">
		<!-- 查看模式：交互流程视图 -->
		<template v-if="type=='info'">
			<div class="detail-view">
				<div class="detail-header">
					<div class="detail-title">
						<i class="el-icon-sort"></i>
						<span>交换申请详情</span>
					</div>
					<div class="detail-header-right">
						<span class="status-tag" :class="statusClass">{{statusText}}</span>
						<el-button class="back-btn" size="small" @click="back()">
							<i class="el-icon-arrow-left"></i> 返回
						</el-button>
					</div>
				</div>

				<div class="flow-section">
					<div class="flow-card">
						<div class="card-badge applicant-badge">申请人</div>
						<div class="card-avatar applicant-avatar">
							{{(ruleForm.gongjirenxingming || '?').substring(0,1)}}
						</div>
						<div class="card-name">{{ruleForm.gongjirenxingming || '--'}}</div>
						<div class="card-account">{{ruleForm.gongjirenzhanghao || '--'}}</div>
						<div class="card-meta"><i class="el-icon-phone"></i> {{ruleForm.lianxifangshi || '--'}}</div>
					</div>

					<div class="flow-middle">
						<div class="skill-box left-box">
							<div class="skill-label">申请人提供</div>
							<div class="skill-name">{{ruleForm.xuanzejinengbiaoti || '待选择'}}</div>
						</div>
						<div class="exchange-icon">
							<i class="el-icon-sort"></i>
							<span>技能交换</span>
						</div>
						<div class="skill-box right-box">
							<div class="skill-label">发布者提供</div>
							<div class="skill-name">{{ruleForm.xuqiubiaoti || '--'}}</div>
						</div>
					</div>

					<div class="flow-card">
						<div class="card-badge owner-badge">需求发布者</div>
						<div class="card-avatar owner-avatar">
							{{(ruleForm.yonghuxingming || '?').substring(0,1)}}
						</div>
						<div class="card-name">{{ruleForm.yonghuxingming || '--'}}</div>
						<div class="card-account">{{ruleForm.yonghuzhanghao || '--'}}</div>
						<div class="card-meta"><i class="el-icon-phone"></i> {{ruleForm.shoujihao || '--'}}</div>
					</div>
				</div>

				<div class="info-grid">
					<div class="info-item">
						<div class="info-label">需求编号</div>
						<div class="info-value">{{ruleForm.xuqiubianhao || '--'}}</div>
					</div>
					<div class="info-item">
						<div class="info-label">技能分类</div>
						<div class="info-value">{{ruleForm.jinengfenlei || '--'}}</div>
					</div>
					<div class="info-item">
						<div class="info-label">需求费用</div>
						<div class="info-value">{{ruleForm.xuqiufeiyong ? ruleForm.xuqiufeiyong + ' 元' : '无'}}</div>
					</div>
					<div class="info-item">
						<div class="info-label">申请时间</div>
						<div class="info-value">{{ruleForm.shenqingshijian || '--'}}</div>
					</div>
					<div class="info-item" v-if="ruleForm.fengmian">
						<div class="info-label">封面</div>
						<div class="info-value">
							<img v-if="ruleForm.fengmian.substring(0,4)=='http'" :src="ruleForm.fengmian.split(',')[0]" class="cover-img" @click="imgPreView(ruleForm.fengmian.split(',')[0])">
							<template v-else>
								<img v-for="(item,index) in ruleForm.fengmian.split(',')" :key="index" :src="$base.url+item" class="cover-img" @click="imgPreView($base.url+item)">
							</template>
						</div>
					</div>
				</div>

				<div class="timeline-section" v-if="ruleForm.shenqingbeizhu || ruleForm.shhf">
					<div class="timeline-title"><i class="el-icon-chat-line-round"></i> 交互记录</div>
					<div class="timeline">
						<div class="timeline-item" v-if="ruleForm.shenqingbeizhu">
							<div class="tl-dot applicant-dot"></div>
							<div class="tl-content">
								<div class="tl-header">
									<span class="tl-author">{{ruleForm.gongjirenxingming}}</span>
									<span class="tl-role applicant-role">申请人</span>
									<span class="tl-time" v-if="ruleForm.shenqingshijian">{{ruleForm.shenqingshijian}}</span>
								</div>
								<div class="tl-body">{{ruleForm.shenqingbeizhu}}</div>
							</div>
						</div>
						<div class="timeline-item" v-if="ruleForm.shhf">
							<div class="tl-dot reply-dot"></div>
							<div class="tl-content">
								<div class="tl-header">
									<span class="tl-author">{{replyAuthor}}</span>
									<span class="tl-role reply-role">{{replyRoleText}}</span>
								</div>
								<div class="tl-body">{{ruleForm.shhf}}</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</template>

		<!-- 编辑模式（保留原有表单，理论上不再使用） -->
		<el-form
			v-else
			class="add-update-preview"
			ref="ruleForm"
			:model="ruleForm"
			:rules="rules"
			label-width="100%"
		>
			<template>
				<el-form-item class="input" label="需求编号" prop="xuqiubianhao">
					<el-input v-model="ruleForm.xuqiubianhao" placeholder="需求编号" clearable :readonly="ro.xuqiubianhao"></el-input>
				</el-form-item>
				<el-form-item class="input" label="需求标题" prop="xuqiubiaoti">
					<el-input v-model="ruleForm.xuqiubiaoti" placeholder="需求标题" clearable :readonly="ro.xuqiubiaoti"></el-input>
				</el-form-item>
				<el-form-item class="input" label="技能分类" prop="jinengfenlei">
					<el-input v-model="ruleForm.jinengfenlei" placeholder="技能分类" clearable :readonly="ro.jinengfenlei"></el-input>
				</el-form-item>
			</template>
			<el-form-item class="textarea" label="申请备注" prop="shenqingbeizhu">
				<el-input style="min-width: 200px; max-width: 600px;" type="textarea" :rows="8" placeholder="申请备注" v-model="ruleForm.shenqingbeizhu"></el-input>
			</el-form-item>
			<el-form-item class="btn">
				<el-button class="btn3" type="success" @click="onSubmit">确定</el-button>
				<el-button class="btn4" type="success" @click="back()">取消</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>
<script>
	export default {
		data() {
			return {
				id: '',
				type: '',
				ro:{
					xuqiubianhao: false, yonghuzhanghao: false, yonghuxingming: false,
					shoujihao: false, xuqiubiaoti: false, jinengfenlei: false,
					xuqiufeiyong: false, fengmian: false, shenqingshijian: false,
					shenqingbeizhu: false, gongjirenzhanghao: false, gongjirenxingming: false,
					lianxifangshi: false, xinyu: false, sfsh: false, shhf: false,
				},
				ruleForm: {
					xuqiubianhao: '', yonghuzhanghao: '', yonghuxingming: '',
					shoujihao: '', xuqiubiaoti: '', jinengfenlei: '',
					xuqiufeiyong: null, fengmian: '', shenqingshijian: '',
					shenqingbeizhu: '', gongjirenzhanghao: '', gongjirenxingming: '',
					lianxifangshi: '', xinyu: '', sfsh: '待审核', shhf: '',
					xuanzejinengbiaoti: '',
				},
				rules: {
					xuqiubianhao: [], yonghuzhanghao: [], yonghuxingming: [],
					shoujihao: [], xuqiubiaoti: [], jinengfenlei: [],
					xuqiufeiyong: [], fengmian: [], shenqingshijian: [],
					shenqingbeizhu: [], gongjirenzhanghao: [], gongjirenxingming: [],
					lianxifangshi: [], xinyu: [], sfsh: [], shhf: [],
				},
			};
		},
		props: ["parent"],
		computed: {
			sessionForm() { return JSON.parse(this.$storage.getObj('userForm')) },
			sessionTable() { return this.$storage.get('sessionTable') },
			statusText() {
				const map = { '待审核': '待审核', '待确认': '待确认', '已完成': '已完成', '否': '已拒绝', '是': '已通过' };
				return map[this.ruleForm.sfsh] || this.ruleForm.sfsh || '--';
			},
			statusClass() {
				const map = { '待审核': 'status-pending', '待确认': 'status-confirming', '已完成': 'status-done', '否': 'status-fail', '是': 'status-pass' };
				return map[this.ruleForm.sfsh] || '';
			},
			replyAuthor() {
				if (this.ruleForm.xuanzejinengbiaoti) {
					return this.ruleForm.gongjirenxingming;
				}
				return this.ruleForm.yonghuxingming;
			},
			replyRoleText() {
				if (this.ruleForm.xuanzejinengbiaoti) {
					return '申请人';
				}
				return '发布者';
			},
		},
		components: {},
		created() {
			this.ruleForm.shenqingshijian = this.getCurDateTime()
		},
		methods: {
			imgPreView(url) {
				this.$parent.imgPreView(url)
			},
			download(file) { window.open(`${file}`) },
			init(id, type) {
				if (id) {
					this.id = id;
					this.type = type;
				}
				if (this.type == 'info' || this.type == 'else' || this.type == 'msg') {
					this.info(id);
				} else if (this.type == 'cross') {
					var obj = this.$storage.getObj('crossObj');
					for (var o in obj) {
						if (this.ro.hasOwnProperty(o)) {
							this.ruleForm[o] = obj[o];
							this.ro[o] = true;
						}
					}
				}
				this.$http({
					url: `${this.sessionTable}/session`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						var json = data.data;
						if (((json.yonghuzhanghao != '' && json.yonghuzhanghao) || json.yonghuzhanghao == 0) && this.sessionTable != "users") {
							this.ruleForm.gongjirenzhanghao = json.yonghuzhanghao;
							this.ro.gongjirenzhanghao = true;
						}
						if (((json.yonghuxingming != '' && json.yonghuxingming) || json.yonghuxingming == 0) && this.sessionTable != "users") {
							this.ruleForm.gongjirenxingming = json.yonghuxingming;
							this.ro.gongjirenxingming = true;
						}
						if (((json.shoujihao != '' && json.shoujihao) || json.shoujihao == 0) && this.sessionTable != "users") {
							this.ruleForm.lianxifangshi = json.shoujihao;
							this.ro.lianxifangshi = true;
						}
						if (((json.xinyuzhishu != '' && json.xinyuzhishu) || json.xinyuzhishu == 0) && this.sessionTable != "users") {
							this.ruleForm.xinyu = json.xinyuzhishu;
							this.ro.xinyu = true;
						}
					} else {
						this.$message.error(data.msg);
					}
				});
			},
			async info(id) {
				await this.$http({
					url: `jiaohuanshenqing/info/${id}`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
			},
			async onSubmit() {
				if (this.ruleForm.fengmian != null) {
					this.ruleForm.fengmian = this.ruleForm.fengmian.split(',').map(u => u.replace(/^\/+/, '')).join(',');
				}
				if (!this.ruleForm.id) { delete this.ruleForm.userid; }
				await this.$refs["ruleForm"].validate(async valid => {
					if (valid) {
						if (this.type == 'cross') {
							var statusColumnName = this.$storage.get('statusColumnName');
							var statusColumnValue = this.$storage.get('statusColumnValue');
							if (statusColumnName != '') {
								var obj = this.$storage.getObj('crossObj');
								if (statusColumnName && !statusColumnName.startsWith("[")) {
									for (var o in obj) {
										if (o == statusColumnName) { obj[o] = statusColumnValue; }
									}
									var table = this.$storage.get('crossTable');
									await this.$http({ url: `${table}/update`, method: "post", data: obj }).then(({ data }) => {});
								}
							}
						}
						await this.$http({
							url: `jiaohuanshenqing/${!this.ruleForm.id ? "save" : "update"}`,
							method: "post",
							data: this.ruleForm
						}).then(async ({ data }) => {
							if (data && data.code === 0) {
								this.$message({
									message: "操作成功", type: "success", duration: 1500,
									onClose: () => {
										this.parent.showFlag = true;
										this.parent.addOrUpdateFlag = false;
										this.parent.jiaohuanshenqingCrossAddOrUpdateFlag = false;
										this.parent.search();
									}
								});
							} else { this.$message.error(data.msg); }
						});
					}
				});
			},
			getUUID() { return new Date().getTime(); },
			back() {
				this.parent.showFlag = true;
				this.parent.addOrUpdateFlag = false;
				this.parent.jiaohuanshenqingCrossAddOrUpdateFlag = false;
			},
			fengmianUploadChange(fileUrls) { this.ruleForm.fengmian = fileUrls; },
		}
	};
</script>
<style lang="scss" scoped>
$primary: #38bdf8;
$purple: #a78bfa;
$green: #34d399;
$red: #fb7185;
$gold: #fbbf24;
$bg: #0f172a;
$card-bg: #1e293b;
$border: rgba(14,165,233,0.15);
$text: #f1f5f9;
$text-sub: #cbd5e1;
$text-dim: #94a3b8;

.addEdit-block {
	padding: 0;
	background: none;
	width: 100%;
	min-height: 100vh;
}

.detail-view {
	max-width: 860px;
	margin: 0 auto;
	padding: 20px;
}

.detail-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 24px;
}
.detail-title {
	font-size: 18px;
	font-weight: 700;
	color: $text;
	display: flex;
	align-items: center;
	gap: 8px;
	.el-icon { color: $primary; font-size: 20px; }
}
.detail-header-right {
	display: flex;
	align-items: center;
	gap: 10px;
}
.status-tag {
	display: inline-block;
	padding: 4px 14px;
	border-radius: 20px;
	font-size: 13px;
	font-weight: 600;
	&.status-pending { background: rgba(245,158,11,0.15); color: $gold; border: 1px solid rgba(245,158,11,0.3); }
	&.status-confirming { background: rgba(14,165,233,0.15); color: $primary; border: 1px solid rgba(14,165,233,0.3); }
	&.status-done { background: rgba(52,211,153,0.15); color: $green; border: 1px solid rgba(52,211,153,0.3); }
	&.status-pass { background: rgba(52,211,153,0.15); color: $green; border: 1px solid rgba(52,211,153,0.3); }
	&.status-fail { background: rgba(251,113,133,0.15); color: $red; border: 1px solid rgba(251,113,133,0.3); }
}
.back-btn {
	border-radius: 20px;
	border: 1px solid $border;
	background: transparent;
	color: $text-sub;
	font-size: 13px;
	padding: 6px 16px;
	cursor: pointer;
	&:hover { color: $primary; border-color: $primary; }
}

.flow-section {
	display: flex;
	align-items: stretch;
	gap: 0;
	margin-bottom: 20px;
}

.flow-card {
	flex: 1;
	min-width: 0;
	background: $card-bg;
	border-radius: 14px;
	border: 1px solid $border;
	padding: 28px 16px 18px;
	display: flex;
	flex-direction: column;
	align-items: center;
	text-align: center;
	position: relative;
}
.card-badge {
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
	&.applicant-badge {
		background: linear-gradient(135deg, $primary, #6d5cfc);
		color: #fff;
	}
	&.owner-badge {
		background: linear-gradient(135deg, #7dd3fc, #8b6ffa);
		color: #fff;
	}
}
.card-avatar {
	width: 52px;
	height: 52px;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 22px;
	font-weight: 700;
	color: #fff;
	margin-bottom: 8px;
	&.applicant-avatar { background: linear-gradient(135deg, #0ea5e9, #6d5cfc); }
	&.owner-avatar { background: linear-gradient(135deg, #7c3aed, #38bdf8); }
}
.card-name {
	font-size: 15px;
	font-weight: 700;
	color: #fff;
	margin-bottom: 2px;
}
.card-account {
	font-size: 12px;
	color: $text-dim;
	margin-bottom: 6px;
}
.card-meta {
	font-size: 12px;
	color: $text-sub;
	background: $bg;
	padding: 4px 10px;
	border-radius: 4px;
	border: 1px solid rgba(14,165,233,0.12);
	.el-icon { font-size: 11px; margin-right: 3px; }
}

.flow-middle {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 0 10px;
	flex-shrink: 0;
	width: 200px;
	gap: 10px;
}
.skill-box {
	width: 100%;
	padding: 10px 12px;
	border-radius: 10px;
	text-align: center;
	.skill-label {
		font-size: 11px;
		color: $text-sub;
		font-weight: 600;
		letter-spacing: 0.5px;
		margin-bottom: 3px;
	}
	.skill-name {
		font-size: 13px;
		font-weight: 700;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	&.left-box {
		background: rgba(14,165,233,0.08);
		border: 1px solid rgba(14,165,233,0.2);
		.skill-name { color: #7dd3fc; }
	}
	&.right-box {
		background: rgba(167,139,250,0.08);
		border: 1px solid rgba(167,139,250,0.2);
		.skill-name { color: #c4b5fd; }
	}
}
.exchange-icon {
	display: flex;
	align-items: center;
	gap: 5px;
	padding: 4px 12px;
	border-radius: 16px;
	background: linear-gradient(135deg, rgba(52,211,153,0.1), rgba(14,165,233,0.08));
	border: 1px solid rgba(52,211,153,0.2);
	.el-icon { font-size: 13px; color: $green; }
	span { font-size: 11px; font-weight: 600; color: $green; }
}

.info-grid {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 12px;
	margin-bottom: 20px;
}
.info-item {
	background: $card-bg;
	border-radius: 10px;
	border: 1px solid $border;
	padding: 12px 16px;
}
.info-label {
	font-size: 12px;
	color: $text-dim;
	margin-bottom: 4px;
	font-weight: 600;
}
.info-value {
	font-size: 14px;
	color: $text;
	font-weight: 500;
}
.cover-img {
	width: 64px;
	height: 64px;
	object-fit: cover;
	border-radius: 8px;
	cursor: pointer;
	margin-right: 8px;
	margin-top: 4px;
	border: 1px solid $border;
}

.timeline-section {
	background: $card-bg;
	border-radius: 14px;
	border: 1px solid $border;
	padding: 18px 20px;
}
.timeline-title {
	font-size: 15px;
	font-weight: 700;
	color: $primary;
	margin-bottom: 16px;
	display: flex;
	align-items: center;
	gap: 6px;
	.el-icon { font-size: 16px; }
}
.timeline {
	position: relative;
	padding-left: 20px;
	&::before {
		content: '';
		position: absolute;
		left: 6px;
		top: 6px;
		bottom: 6px;
		width: 2px;
		background: rgba(14,165,233,0.15);
		border-radius: 1px;
	}
}
.timeline-item {
	position: relative;
	padding-bottom: 20px;
	&:last-child { padding-bottom: 0; }
}
.tl-dot {
	position: absolute;
	left: -17px;
	top: 6px;
	width: 10px;
	height: 10px;
	border-radius: 50%;
	&.applicant-dot { background: $primary; box-shadow: 0 0 0 3px rgba(56,189,248,0.2); }
	&.reply-dot { background: $purple; box-shadow: 0 0 0 3px rgba(167,139,250,0.2); }
}
.tl-content {
	background: $bg;
	border-radius: 10px;
	border: 1px solid $border;
	padding: 12px 16px;
}
.tl-header {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 6px;
	flex-wrap: wrap;
}
.tl-author {
	font-size: 14px;
	font-weight: 700;
	color: $text;
}
.tl-role {
	font-size: 11px;
	font-weight: 600;
	padding: 1px 8px;
	border-radius: 10px;
	&.applicant-role { background: rgba(14,165,233,0.15); color: $primary; }
	&.reply-role { background: rgba(167,139,250,0.15); color: $purple; }
}
.tl-time {
	font-size: 12px;
	color: $text-dim;
	margin-left: auto;
}
.tl-body {
	font-size: 14px;
	color: $text-sub;
	line-height: 1.6;
}

.add-update-preview {
	border: 1px solid $border;
	border-radius: 14px;
	padding: 30px;
	background: $card-bg;
	display: flex;
	justify-content: space-between;
	flex-wrap: wrap;
}
.add-update-preview ::v-deep .el-form-item {
	margin: 10px;
	flex-direction: column;
	display: flex;
	width: calc(50% - 40px);
	flex-wrap: wrap;
}
.add-update-preview .el-form-item ::v-deep .el-form-item__label {
	padding: 0;
	color: $text;
	font-weight: 500;
	width: 100%;
	font-size: 14px;
	line-height: 30px;
	text-align: left;
	height: 30px;
}
.add-update-preview .el-form-item ::v-deep .el-form-item__content {
	margin-left: auto !important;
	margin: auto !important;
	flex: 1;
	display: flex;
	width: 100%;
	justify-content: flex-start;
	align-items: flex-start;
	flex-wrap: wrap;
}
.add-update-preview .el-input ::v-deep .el-input__inner {
	border: 1px solid rgba(14,165,233,0.25);
	border-radius: 8px;
	padding: 0 12px;
	color: $text;
	background: $bg;
	width: 100%;
	font-size: 14px;
	height: 40px;
}
.add-update-preview .el-textarea ::v-deep .el-textarea__inner {
	border: 1px solid rgba(14,165,233,0.25);
	border-radius: 8px;
	padding: 10px 12px;
	color: $text;
	background: $bg;
	font-size: 14px;
}
.add-update-preview ::v-deep .el-form-item.btn {
	padding: 0 30px;
	margin: 20px auto 0 auto;
	background: none;
	display: flex;
	width: auto;
	justify-content: center;
	.btn3, .btn4 {
		border: none;
		cursor: pointer;
		padding: 0 24px;
		margin: 4px;
		color: #fff;
		font-weight: bold;
		font-size: 14px;
		border-radius: 8px;
		min-width: 120px;
		height: 40px;
	}
	.btn3 { background: linear-gradient(135deg, $primary, #6d5cfc); }
	.btn4 { background: $card-bg; color: $text-sub; border: 1px solid $border; }
}
</style>
