<template>
	<div class="apply-page">
		<div class="page-header">
			<h2 class="page-title">
				<i class="el-icon-promotion"></i>
				提交交换申请
			</h2>
			<el-button class="back-btn" size="small" plain @click="back()">
				<template #icon><ArrowLeft /></template>
				返回
			</el-button>
		</div>

		<div class="apply-body">
			<div class="info-cards">
				<div class="info-card skill-card">
					<div class="card-badge">申请技能</div>
					<div class="card-cover" v-if="ruleForm.fengmian">
						<img v-if="ruleForm.fengmian.substring(0,4)=='http'" :src="ruleForm.fengmian.split(',')[0]" />
						<img v-else :src="baseUrl + ruleForm.fengmian.split(',')[0]" />
					</div>
					<div class="card-cover card-cover-empty" v-else>
						<el-icon class="empty-picture-icon"><Picture /></el-icon>
					</div>
					<div class="card-info">
						<div class="card-title">{{ruleForm.xuqiubiaoti || '—'}}</div>
						<div class="card-meta">
							<span class="meta-tag"><i class="el-icon-collection-tag"></i> {{ruleForm.jinengfenlei || '—'}}</span>
							<span class="meta-tag"><i class="el-icon-document"></i> {{ruleForm.xuqiubianhao || '—'}}</span>
						</div>
						<div class="card-row">
							<span><i class="el-icon-user"></i> {{ruleForm.yonghuxingming || '—'}}</span>
						</div>
						<div class="card-row">
							<span><i class="el-icon-phone"></i> {{ruleForm.shoujihao || '—'}}</span>
						</div>
					</div>
				</div>

				<div class="arrow-icon">
					<i class="el-icon-sort"></i>
				</div>

				<div class="info-card user-card">
					<div class="card-badge my">我的信息</div>
					<div class="card-avatar">
						<i class="el-icon-avatar"></i>
					</div>
					<div class="card-info">
						<div class="card-title">{{ruleForm.gongjirenxingming || '—'}}</div>
						<div class="card-meta">
							<span class="meta-tag"><i class="el-icon-avatar"></i> {{ruleForm.gongjirenxingming || '—'}}</span>
						</div>
						<div class="card-row">
							<span><i class="el-icon-phone"></i> {{ruleForm.lianxifangshi || '—'}}</span>
						</div>
					</div>
				</div>
			</div>

			<div class="remark-section">
				<div class="remark-label">
					<i class="el-icon-edit"></i> 申请备注
					<span class="remark-tip">向对方说明你的交换意向、可提供的技能等</span>
				</div>
				<el-input
					type="textarea"
					:rows="4"
					v-model="ruleForm.shenqingbeizhu"
					placeholder="例如：你好，我擅长吉他弹唱，希望能和你交换技能学习，互相进步！"
					maxlength="200"
					show-word-limit
					resize="none"
				></el-input>
			</div>

			<div class="apply-time">
				<i class="el-icon-time"></i> 申请时间：{{ruleForm.shenqingshijian}}
			</div>

			<div class="form-actions">
				<el-button class="btn-submit" type="primary" @click="onSubmit(null)">
					<template #icon><Promotion /></template>
					提交申请
				</el-button>
				<el-button class="btn-cancel" @click="back()">取消</el-button>
			</div>
		</div>

		<el-form ref="ruleForm" :model="ruleForm" style="display:none">
			<el-form-item prop="xuqiubianhao"><el-input v-model="ruleForm.xuqiubianhao"></el-input></el-form-item>
		</el-form>
	</div>
</template>

<script>
export default {
	data() {
		return {
			id: '',
			baseUrl: '',
			ro: {
				xuqiubianhao: false, yonghuzhanghao: false, yonghuxingming: false,
				shoujihao: false, xuqiubiaoti: false, jinengfenlei: false,
				fengmian: false, shenqingshijian: false, shenqingbeizhu: false,
				gongjirenzhanghao: false, gongjirenxingming: false,
				lianxifangshi: false, xinyu: false, sfsh: false, shhf: false,
			},
			type: '',
			userTableName: localStorage.getItem('UserTableName'),
			ruleForm: {
				xuqiubianhao: '', yonghuzhanghao: '', yonghuxingming: '',
				shoujihao: '', xuqiubiaoti: '', jinengfenlei: '',
				fengmian: '', shenqingshijian: '', shenqingbeizhu: '',
				gongjirenzhanghao: '', gongjirenxingming: '',
				lianxifangshi: '', xinyu: '',
			},
			centerType: false,
		};
	},
	created() {
		if (this.$route.query.centerType) this.centerType = true;
		let type = this.$route.query.type || '';
		this.init(type);
		this.baseUrl = this.$config.baseUrl;
		this.ruleForm.shenqingshijian = this.getCurDateTime();
	},
	methods: {
		getCurDateTime() {
			let d = new Date();
			let pad = n => n < 10 ? '0' + n : n;
			return d.getFullYear() + '-' + pad(d.getMonth()+1) + '-' + pad(d.getDate()) + ' '
				+ pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds());
		},
		init(type) {
			this.type = type;
			if (type === 'cross') {
				let obj = JSON.parse(localStorage.getItem('crossObj'));
				if (obj) {
					for (let o in obj) {
						if (this.ruleForm.hasOwnProperty(o)) {
							this.ruleForm[o] = (o === 'fengmian' && obj[o]) ? obj[o].split(',')[0] : obj[o];
							this.ro[o] = true;
						}
					}
				}
			} else if (type === 'edit') {
				this.info();
			}
			this.$http.get(this.userTableName + '/session').then(res => {
				if (res.data.code == 0) {
					let json = res.data.data;
					if (json.yonghuzhanghao) { this.ruleForm.gongjirenzhanghao = json.yonghuzhanghao; this.ro.gongjirenzhanghao = true; }
					if (json.yonghuxingming) { this.ruleForm.gongjirenxingming = json.yonghuxingming; this.ro.gongjirenxingming = true; }
					if (json.shoujihao) { this.ruleForm.lianxifangshi = json.shoujihao; this.ro.lianxifangshi = true; }
					if (json.xinyuzhishu) { this.ruleForm.xinyu = json.xinyuzhishu; this.ro.xinyu = true; }
				}
			});
			if (localStorage.getItem('raffleType')) {
				localStorage.removeItem('raffleType');
				setTimeout(() => { this.onSubmit(null); }, 300);
			}
		},
		async info() {
			await this.$http.get(`jiaohuanshenqing/detail/${this.$route.query.id}`).then(res => {
				if (res.data.code == 0) this.ruleForm = res.data.data;
			});
		},
		async onSubmit() {
			if (!this.ruleForm.id) delete this.ruleForm.userid;
			if (this.type === 'cross') {
				let statusColumnName = localStorage.getItem('statusColumnName');
				let statusColumnValue = localStorage.getItem('statusColumnValue');
				if (statusColumnName && statusColumnName !== '') {
					let obj = JSON.parse(localStorage.getItem('crossObj'));
					if (!statusColumnName.startsWith('[')) {
						for (let o in obj) { if (o === statusColumnName) obj[o] = statusColumnValue; }
						let table = localStorage.getItem('crossTable');
						await this.$http.post(table + '/update', obj);
					}
				}
			}
			let url = this.ruleForm.id ? 'jiaohuanshenqing/update' : (this.centerType ? 'jiaohuanshenqing/save' : 'jiaohuanshenqing/add');
			await this.$http.post(url, this.ruleForm).then(res => {
				if (res.data.code == 0) {
					this.$message({ message: '申请提交成功！', type: 'success', duration: 1500, onClose: () => { this.$router.go(-1); } });
				} else {
					this.$message({ message: res.data.msg, type: 'error', duration: 1500 });
				}
			});
		},
		back() { this.$router.go(-1); },
	}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$purple: #7c3aed;
$white: #0f172a;
$text-dark: #e2e8f0;
$text-body: #94a3b8;
$text-sub: #64748b;
$border: rgba(0,212,255,0.12);
$radius: 12px;

.apply-page {
	max-width: 780px;
	margin: 0 auto;
	padding: 20px 20px 32px;
}

.page-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 20px;
	padding-bottom: 14px;
	border-bottom: 1px solid $border;
	.page-title {
		font-size: 20px;
		font-weight: 700;
		color: $text-dark;
		margin: 0;
		display: flex;
		align-items: center;
		gap: 8px;
		.el-icon { color: $primary; font-size: 22px; }
	}
	.back-btn {
		border-color: $border !important;
		color: $text-sub !important;
		background: $white !important;
		border-radius: 20px;
		padding: 7px 18px;
		font-size: 13px;
		&:hover { color: $primary !important; border-color: $primary !important; }
	}
}

.apply-body {
	background: $white;
	border-radius: $radius;
	border: 1px solid $border;
	padding: 24px 28px;
	box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.info-cards {
	display: flex;
	align-items: stretch;
	gap: 0;
	margin-bottom: 20px;
}

.arrow-icon {
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 0 16px;
	flex-shrink: 0;
	.el-icon {
		font-size: 28px;
		color: $primary;
		transform: rotate(90deg);
		opacity: 0.5;
	}
}

.info-card {
	flex: 1;
	min-width: 0;
	border-radius: 10px;
	padding: 16px;
	position: relative;
	display: flex;
	gap: 14px;
	align-items: flex-start;

	&.skill-card {
		background: linear-gradient(135deg, rgba(14,165,233,0.04), rgba(14,165,233,0.08));
		border: 1px solid rgba(14,165,233,0.15);
	}
	&.user-card {
		background: linear-gradient(135deg, rgba(124,58,237,0.04), rgba(124,58,237,0.08));
		border: 1px solid rgba(124,58,237,0.15);
	}
}

.card-badge {
	position: absolute;
	top: -1px;
	right: 12px;
	background: $primary;
	color: #fff;
	font-size: 11px;
	font-weight: 600;
	padding: 2px 10px;
	border-radius: 0 0 6px 6px;
	letter-spacing: 0.5px;
	&.my { background: $purple; }
}

.card-cover {
	width: 80px;
	height: 80px;
	border-radius: 8px;
	overflow: hidden;
	flex-shrink: 0;
	img {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}
	&.card-cover-empty {
		background: #f1f5f9;
		display: flex;
		align-items: center;
		justify-content: center;
		.empty-picture-icon { font-size: 28px; color: $text-sub; }
	}
}

.card-avatar {
	width: 80px;
	height: 80px;
	border-radius: 50%;
	flex-shrink: 0;
	background: linear-gradient(135deg, rgba(124,58,237,0.1), rgba(124,58,237,0.2));
	display: flex;
	align-items: center;
	justify-content: center;
	.el-icon { font-size: 36px; color: $purple; }
}

.card-info {
	flex: 1;
	min-width: 0;
	.card-title {
		font-size: 15px;
		font-weight: 600;
		color: $text-dark;
		margin-bottom: 6px;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.card-meta {
		display: flex;
		flex-wrap: wrap;
		gap: 6px;
		margin-bottom: 6px;
		.meta-tag {
			font-size: 11px;
			color: $text-sub;
			background: #f1f5f9;
			padding: 2px 8px;
			border-radius: 4px;
			.el-icon { margin-right: 2px; vertical-align: middle; }
		}
	}
	.card-row {
		font-size: 13px;
		color: $text-body;
		line-height: 1.8;
		.el-icon { color: $text-sub; margin-right: 4px; width: 14px; vertical-align: middle; }
	}
}

.remark-section {
	margin-bottom: 16px;
	.remark-label {
		font-size: 14px;
		font-weight: 600;
		color: $text-dark;
		margin-bottom: 8px;
		display: flex;
		align-items: center;
		gap: 6px;
		.el-icon { color: $primary; }
		.remark-tip {
			font-size: 12px;
			color: $text-sub;
			font-weight: 400;
			margin-left: 8px;
		}
	}
	::v-deep .el-textarea__inner {
		border: 1px solid $border;
		border-radius: 8px;
		font-size: 14px;
		color: $text-dark;
		padding: 12px 14px;
		transition: border-color 0.2s, box-shadow 0.2s;
		&:focus {
			border-color: $primary;
			box-shadow: 0 0 0 3px rgba(14,165,233,0.1);
		}
		&::placeholder { color: #c0c4cc; }
	}
	::v-deep .el-input__count {
		background: transparent;
		color: $text-sub;
		font-size: 12px;
	}
}

.apply-time {
	text-align: right;
	font-size: 12px;
	color: $text-sub;
	margin-bottom: 16px;
	.el-icon { margin-right: 4px; vertical-align: middle; }
}

.form-actions {
	display: flex;
	justify-content: center;
	gap: 14px;
	.btn-submit {
		background: linear-gradient(135deg, $primary, $purple) !important;
		border: none !important;
		border-radius: 24px;
		padding: 10px 40px;
		font-size: 15px;
		font-weight: 600;
		min-width: 150px;
		letter-spacing: 1px;
		color: #fff !important;
		box-shadow: 0 4px 16px rgba(14,165,233,0.25);
		transition: all 0.25s;
		&:hover {
			transform: translateY(-2px);
			box-shadow: 0 8px 24px rgba(14,165,233,0.3);
		}
	}
	.btn-cancel {
		border-radius: 24px;
		padding: 10px 28px;
		font-size: 14px;
		border: 1px solid $border !important;
		color: $text-body !important;
		background: $white !important;
		&:hover { color: $primary !important; border-color: $primary !important; }
	}
}
</style>
