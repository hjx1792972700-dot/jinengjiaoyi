<template>
	<div class="demand-page">
		<div class="demand-card">
			<div class="card-header">
				<div class="header-left">
					<div class="header-icon-wrap">
						<i class="el-icon-edit"></i>
					</div>
					<div class="header-text">
						<span class="header-title">{{ruleForm.id ? ('编辑' + typeLabel) : ('发布' + typeLabel)}}</span>
						<span class="header-sub">填写以下信息，发布你的{{typeLabel}}</span>
					</div>
				</div>
				<button class="back-btn" @click="back()">
					<i class="el-icon-arrow-left"></i> 返回
				</button>
			</div>

			<el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="0" label-position="top" class="demand-form">
				<div class="form-section">
					<div class="section-label"><i class="el-icon-document"></i> 基本信息</div>
					<el-form-item :label="typeLabel + '标题'" prop="xuqiubiaoti">
						<el-input v-model="ruleForm.xuqiubiaoti" :placeholder="isSkillType ? '例如：Python编程入门教学、吉他弹唱指导' : '例如：想学Python爬虫、求学日语入门'" clearable :readonly="ro.xuqiubiaoti" maxlength="50" show-word-limit></el-input>
					</el-form-item>
					<div class="inline-row">
						<el-form-item label="技能分类" prop="jinengfenlei">
							<el-select v-model="ruleForm.jinengfenlei" placeholder="选择分类" :disabled="ro.jinengfenlei" filterable>
								<el-option v-for="(item,index) in jinengfenleiOptions" :key="index" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item :label="isSkillType ? '技能定价' : '需求预算'">
							<el-input v-model="ruleForm.xuqiufeiyong" placeholder="留空则面议" type="number">
								<template #prepend>¥</template>
							</el-input>
						</el-form-item>
					</div>
					<el-form-item label="展示封面" class="cover-upload-item">
						<p class="tip-text">上传一张图片作为封面，让内容更有吸引力</p>
						<file-upload
							tip="点击上传封面图"
							action="file/upload"
							:limit="3"
							:multiple="true"
							:fileUrls="ruleForm.fengmian ? ruleForm.fengmian : ''"
							@change="fengmianUploadChange"
						></file-upload>
					</el-form-item>
				</div>
				<div class="form-section">
					<div class="section-label"><i class="el-icon-notebook"></i> 详细描述</div>
					<el-form-item :label="isSkillType ? '技能介绍' : '需求详情'" prop="xuqiuxiangqing" class="desc-textarea">
						<el-input type="textarea" v-model="ruleForm.xuqiuxiangqing" :placeholder="isSkillType ? '描述你的技能水平、教学方式、可提供的服务等' : '详细描述你的需求，以便他人更好地为你提供帮助'" :rows="6" maxlength="2000" show-word-limit resize="vertical"></el-input>
					</el-form-item>
				</div>
			</el-form>

			<input type="hidden" v-model="ruleForm.xuqiubianhao">
			<input type="hidden" v-model="ruleForm.yonghuzhanghao">
			<input type="hidden" v-model="ruleForm.yonghuxingming">
			<input type="hidden" v-model="ruleForm.shoujihao">
			<input type="hidden" v-model="ruleForm.xinyuzhishu">
			<input type="hidden" v-model="ruleForm.xuqiumiaoshu">

			<div class="card-footer">
				<button class="cancel-btn" @click="back()">取消</button>
				<button class="submit-btn" @click="onSubmit">
					<i class="el-icon-promotion"></i> {{ruleForm.id ? '保存修改' : ('发布' + typeLabel)}}
				</button>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	data() {
		return {
			baseUrl: '',
			userTableName: localStorage.getItem('UserTableName'),
			ruleForm: {
				xuqiubianhao: this.getUUID(),
				xuqiubiaoti: '',
				jinengfenlei: '',
				fengmian: '',
				fabushijian: '',
				xuqiufeiyong: null,
				xuqiumiaoshu: '',
				xuqiuxiangqing: '',
				yonghuzhanghao: '',
				yonghuxingming: '',
				shoujihao: '',
				xinyuzhishu: '',
			},
			ro: {
				xuqiubiaoti: false,
				jinengfenlei: false,
			},
			jinengfenleiOptions: [],
			rules: {
				xuqiubiaoti: [{ required: true, message: '请输入标题', trigger: 'blur' }],
				jinengfenlei: [{ required: true, message: '请选择分类', trigger: 'change' }],
				xuqiuxiangqing: [{ required: true, message: '请填写详情', trigger: 'blur' }],
			},
			centerType: false,
		};
	},
	computed: {
		isSkillType() {
			return this.ruleForm.leixing === '技能';
		},
		typeLabel() {
			return this.isSkillType ? '技能' : '需求';
		},
	},
	created() {
		if (this.$route.query.centerType) {
			this.centerType = true;
		}
		this.baseUrl = this.$config.baseUrl;
		this.ruleForm.fabushijian = this.getCurDate();
		this.loadUserInfo();
		this.loadCategories();
		let type = this.$route.query.type || '';
		if (type === 'edit' && this.$route.query.id) {
			this.loadDetail(this.$route.query.id);
		} else if (type === 'cross') {
			this.loadCrossData();
		}
	},
	methods: {
		getUUID() {
			return new Date().getTime();
		},
		getCurDate() {
			let d = new Date();
			let m = (d.getMonth() + 1 < 10 ? '0' : '') + (d.getMonth() + 1);
			let day = (d.getDate() < 10 ? '0' : '') + d.getDate();
			return d.getFullYear() + '-' + m + '-' + day;
		},
		loadUserInfo() {
			this.$http.get(this.userTableName + '/session').then(res => {
				if (res.data.code == 0) {
					let json = res.data.data;
					if (json.yonghuzhanghao) this.ruleForm.yonghuzhanghao = json.yonghuzhanghao;
					if (json.yonghuxingming) this.ruleForm.yonghuxingming = json.yonghuxingming;
					if (json.shoujihao) this.ruleForm.shoujihao = json.shoujihao;
					if (json.xinyuzhishu) this.ruleForm.xinyuzhishu = json.xinyuzhishu;
				}
			});
			this.$http.get('huiyuanchongzhi/vipStatus').then(res => {
				if (res.data.code === 0) {
					const d = res.data.data;
					if (!d.vip) {
						let user = JSON.parse(localStorage.getItem('sessionForm'));
						this.$http.get('jinengxuqiu/page', {
							params: { page: 1, limit: 1, yonghuzhanghao: user.yonghuzhanghao }
						}).then(r => {
							let hasPublished = r.data.code == 0 && r.data.data.total > 0;
							let typeText = this.isSkillType ? '技能' : '需求';
							let msg = (d.expired || hasPublished)
								? '您的VIP权限已到期，请先续费后再发布' + typeText
								: '只有VIP会员才可以发布' + typeText + '，请先开通会员';
							this.$alert(msg, '提示', {
								confirmButtonText: '返回',
								type: 'warning',
								callback: () => { this.back(); }
							});
						});
					}
				}
			});
		},
		loadCategories() {
			this.$http.get('option/jinengfenlei/jinengfenlei').then(res => {
				if (res.data.code == 0) {
					this.jinengfenleiOptions = res.data.data;
				}
			});
		},
		async loadDetail(id) {
			await this.$http.get(`jinengxuqiu/detail/${id}`).then(res => {
				if (res.data.code == 0) {
					this.ruleForm = res.data.data;
				}
			});
		},
		loadCrossData() {
			var obj = JSON.parse(localStorage.getItem('crossObj'));
			if (!obj) return;
			for (var o in obj) {
				if (this.ruleForm.hasOwnProperty(o)) {
					this.ruleForm[o] = obj[o];
					this.ro[o] = true;
				}
			}
		},
		fengmianUploadChange(fileUrls) {
			this.ruleForm.fengmian = fileUrls.replace(/^\/+/, '');
		},
		onSubmit() {
			this.$refs['ruleForm'].validate(async valid => {
				if (!valid) return;
				if (this.ruleForm.xuqiubianhao) {
					this.ruleForm.xuqiubianhao = String(this.ruleForm.xuqiubianhao);
				}
				if (!this.ruleForm.id) {
					this.ruleForm.leixing = '需求';
				}
				const url = this.ruleForm.id
					? 'jinengxuqiu/update'
					: (this.centerType ? 'jinengxuqiu/save' : 'jinengxuqiu/add');
				await this.$http.post(url, this.ruleForm).then(res => {
					if (res.data.code == 0) {
						this.$message({
							message: this.ruleForm.id ? '修改成功' : '发布成功，等待管理员审核',
							type: 'success',
							duration: 1500,
							onClose: () => { this.back(); }
						});
					} else {
						this.$message({ message: res.data.msg, type: 'error', duration: 1500 });
					}
				});
			});
		},
		back() {
			if (this.centerType) {
				this.$router.push({ path: '/main/center', query: { tab: 'jinengxuqiu' } });
			} else {
				this.$router.go(-1);
			}
		},
	}
};
</script>

<style lang="scss">
.demand-page .demand-card {
	.el-form-item__label {
		color: #475569 !important;
		font-weight: 600 !important;
		font-size: 13px !important;
		padding-bottom: 6px !important;
		line-height: 1.4 !important;
	}
	.el-input__inner {
		border-radius: 10px !important;
		height: 42px !important;
		line-height: 42px !important;
		font-size: 13px !important;
		background: #f8fafc !important;
		border: 1.5px solid #e2e8f0 !important;
		color: #1e293b !important;
		transition: all 0.25s !important;
		&::placeholder { color: #94a3b8 !important; }
		&:hover { border-color: #94a3b8 !important; }
		&:focus {
			border-color: #0ea5e9 !important;
			background: #fff !important;
			box-shadow: 0 0 0 3px rgba(14,165,233,0.1) !important;
		}
	}
	.el-input-group__prepend {
		background: linear-gradient(135deg,#eff6ff,#f0f9ff) !important;
		border: 1.5px solid #e2e8f0 !important;
		border-right: none !important;
		color: #0ea5e9 !important;
		font-weight: 700 !important;
		font-size: 15px !important;
		border-radius: 10px 0 0 10px !important;
		padding: 0 14px !important;
	}
	.el-input-group--prepend .el-input__inner {
		border-radius: 0 10px 10px 0 !important;
	}
	.el-select { width: 100% !important; }
	.el-select .el-input .el-select__caret { color: #94a3b8 !important; }
	.el-input__count-inner, .el-input__count {
		background: transparent !important;
		color: #94a3b8 !important;
		font-size: 11px !important;
	}
	.el-form-item.is-required:not(.is-no-asterisk) > .el-form-item__label::before {
		color: #ef4444 !important;
	}
}
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$accent: #7c3aed;
$text-dark: #1e293b;
$text-mid: #475569;
$text-light: #94a3b8;
$border: #e2e8f0;
$border-light: #f1f5f9;
$bg-page: #0f172a;
$bg-card: #ffffff;
$radius: 16px;

.demand-page {
	display: flex; justify-content: center; align-items: flex-start;
	padding: 32px 20px 60px; min-height: calc(100vh - 120px);
	background: linear-gradient(180deg, $bg-page 0%, #1e293b 100%);
}
.demand-card {
	width: 100%; max-width: 720px; background: $bg-card; border-radius: $radius;
	box-shadow: 0 20px 60px rgba(0,0,0,0.3), 0 1px 3px rgba(0,0,0,0.1);
	overflow: hidden; position: relative;
	animation: cardSlideUp 0.5s cubic-bezier(0.22, 1, 0.36, 1) both;
}
@keyframes cardSlideUp {
	from { opacity: 0; transform: translateY(30px) scale(0.98); }
	to { opacity: 1; transform: translateY(0) scale(1); }
}
.card-header {
	display: flex; align-items: center; justify-content: space-between;
	padding: 24px 28px; position: relative; overflow: hidden;
	background: linear-gradient(135deg, #0f172a 0%, #1e293b 60%, rgba(14,165,233,0.08) 100%);
	&::after {
		content: ''; position: absolute; bottom: 0; left: 0; right: 0; height: 3px;
		background: linear-gradient(90deg, $primary, $accent, $primary);
		background-size: 200% 100%;
		animation: shimmerBar 3s ease-in-out infinite;
	}
	&::before {
		content: ''; position: absolute; top: -40px; right: -40px; width: 120px; height: 120px;
		background: radial-gradient(circle, rgba(14,165,233,0.08) 0%, transparent 70%);
		border-radius: 50%;
	}
	.header-left { display: flex; align-items: center; gap: 14px; z-index: 1; }
	.header-icon-wrap {
		width: 44px; height: 44px; border-radius: 12px;
		background: linear-gradient(135deg, rgba(14,165,233,0.2), rgba(124,58,237,0.2));
		display: flex; align-items: center; justify-content: center;
		transition: transform 0.3s;
		.el-icon { font-size: 20px; color: $primary; }
		&:hover { transform: rotate(-8deg) scale(1.05); }
	}
	.header-text { display: flex; flex-direction: column; gap: 3px; }
	.header-title { font-size: 18px; font-weight: 700; color: #fff; letter-spacing: 0.5px; }
	.header-sub { font-size: 12px; color: rgba(255,255,255,0.5); }
}
@keyframes shimmerBar {
	0%, 100% { background-position: 0% 0; }
	50% { background-position: 100% 0; }
}
.back-btn {
	display: inline-flex; align-items: center; gap: 4px; padding: 8px 18px; border-radius: 20px;
	border: 1px solid rgba(255,255,255,0.15); background: rgba(255,255,255,0.06);
	color: rgba(255,255,255,0.7); font-size: 12px; font-weight: 500; cursor: pointer;
	transition: all 0.3s cubic-bezier(0.4,0,0.2,1); z-index: 1;
	&:hover { color: #fff; background: rgba(255,255,255,0.12); border-color: rgba(255,255,255,0.3); transform: translateX(-2px); }
	&:active { transform: scale(0.96); }
}
.demand-form {
	padding: 8px 28px 0;
	.form-section {
		padding: 20px 0 8px;
		animation: sectionFadeIn 0.4s ease-out both;
		&:nth-child(1) { animation-delay: 0.15s; }
		&:nth-child(2) { animation-delay: 0.3s; }
		&:not(:first-child) { border-top: 1.5px solid $border-light; margin-top: 4px; }
	}
	.section-label {
		font-size: 13px; font-weight: 700; color: $primary; margin-bottom: 16px;
		display: flex; align-items: center; gap: 6px;
		.el-icon { font-size: 15px; transition: transform 0.3s; }
		&:hover .el-icon { transform: scale(1.15); }
		&::after { content: ''; flex: 1; height: 1px; background: linear-gradient(90deg, rgba(14,165,233,0.3), transparent); margin-left: 10px; }
	}
	.inline-row { display: flex; gap: 20px; > .el-form-item { flex: 1; min-width: 0; } }
	::v-deep .el-form-item { margin-bottom: 20px; transition: opacity 0.3s; }
}
.cover-upload-item {
	.tip-text { font-size: 12px; color: $text-light; margin: 0 0 8px; }
	::v-deep .el-form-item__content { line-height: normal; }
	::v-deep .upload .upload-img,
	::v-deep .el-upload .el-icon-plus {
		border: 2px dashed $border; border-radius: 10px; color: $text-light;
		width: 100px; height: 100px; line-height: 100px; font-size: 24px;
		background: #f8fafc; transition: all 0.25s;
		&:hover { border-color: $primary; color: $primary; background: #eff6ff; }
	}
	::v-deep .upload-img { width: 100px !important; height: 100px !important; border-radius: 10px; object-fit: cover; }
	::v-deep .el-upload__tip { color: $text-light; font-size: 11px; }
}
@keyframes sectionFadeIn {
	from { opacity: 0; transform: translateY(12px); }
	to { opacity: 1; transform: translateY(0); }
}
.desc-textarea {
	::v-deep .el-textarea__inner {
		border-radius: 10px !important;
		font-size: 13px !important;
		line-height: 1.8 !important;
		padding: 14px 16px !important;
		background: #f8fafc !important;
		border: 1.5px solid $border !important;
		color: $text-dark !important;
		transition: all 0.3s !important;
		&::placeholder { color: $text-light !important; }
		&:hover { border-color: #94a3b8 !important; }
		&:focus {
			border-color: $primary !important;
			background: #fff !important;
			box-shadow: 0 0 0 3px rgba(14,165,233,0.1) !important;
		}
	}
	::v-deep .el-input__count { background: transparent !important; color: $text-light !important; font-size: 11px !important; }
}
.card-footer {
	display: flex; justify-content: flex-end; align-items: center; gap: 12px;
	padding: 20px 28px; border-top: 1.5px solid $border-light;
	background: linear-gradient(180deg, #fafbfc, #f5f7fa);
	animation: sectionFadeIn 0.4s ease-out 0.45s both;
}
.submit-btn {
	display: inline-flex; align-items: center; gap: 6px; padding: 11px 36px; border-radius: 22px;
	border: none; background: linear-gradient(135deg, $primary, $accent);
	background-size: 200% 200%;
	color: #fff; font-size: 14px; font-weight: 600; cursor: pointer;
	transition: all 0.3s cubic-bezier(0.4,0,0.2,1);
	box-shadow: 0 4px 14px rgba(14,165,233,0.25); letter-spacing: 0.3px;
	.el-icon { font-size: 15px; margin-right: 2px; vertical-align: middle; }
	&:hover {
		transform: translateY(-2px);
		box-shadow: 0 8px 24px rgba(14,165,233,0.35);
		background-position: 100% 0;
	}
	&:active { transform: translateY(0) scale(0.97); }
}
.cancel-btn {
	padding: 11px 24px; border-radius: 22px; border: 1.5px solid $border;
	background: #fff; color: $text-mid; font-size: 13px; font-weight: 500; cursor: pointer;
	transition: all 0.3s cubic-bezier(0.4,0,0.2,1);
	&:hover { color: $primary; border-color: rgba(14,165,233,0.3); background: rgba(14,165,233,0.04); }
	&:active { transform: scale(0.96); }
}
</style>
