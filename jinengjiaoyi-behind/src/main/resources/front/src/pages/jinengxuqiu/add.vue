<template>
	<div class="demand-page">
		<div class="demand-card">
			<div class="card-header">
				<div class="header-left">
					<div class="header-icon-wrap">
						<el-icon><Edit /></el-icon>
					</div>
					<div class="header-text">
						<span class="header-title">{{ruleForm.id ? '编辑需求' : '发布需求'}}</span>
						<span class="header-sub">填写以下信息，发布你的技能需求</span>
					</div>
				</div>
				<button class="back-btn" @click="back()">
					<el-icon><ArrowLeft /></el-icon> 返回
				</button>
			</div>

			<el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="0" label-position="top" class="demand-form">
				<div class="form-section">
					<div class="section-label"><el-icon><Document /></el-icon> 基本信息</div>
					<el-form-item label="需求标题" prop="xuqiubiaoti">
						<el-input v-model="ruleForm.xuqiubiaoti" placeholder="例如：想学Python爬虫、求学日语入门" clearable :readonly="ro.xuqiubiaoti" maxlength="50" show-word-limit></el-input>
					</el-form-item>
					<div class="inline-row">
						<el-form-item label="技能分类" prop="jinengfenlei">
							<el-select v-model="ruleForm.jinengfenlei" placeholder="选择分类" :disabled="ro.jinengfenlei" filterable>
								<el-option v-for="(item,index) in jinengfenleiOptions" :key="index" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="需求预算">
							<el-input v-model="ruleForm.xuqiufeiyong" placeholder="留空则面议" type="number">
								<template #prepend>¥</template>
							</el-input>
						</el-form-item>
					</div>
				</div>
				<div class="form-section">
					<div class="section-label"><el-icon><Notebook /></el-icon> 详细描述</div>
					<el-form-item label="需求详情" prop="xuqiuxiangqing" class="editor-item">
						<editor v-model="ruleForm.xuqiuxiangqing" class="editor" myQuillEditor="xuqiuxiangqing" action="file/upload"></editor>
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
					<el-icon><Promotion /></el-icon> {{ruleForm.id ? '保存修改' : '立即发布'}}
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
				xuqiubiaoti: [{ required: true, message: '请输入需求标题', trigger: 'blur' }],
				jinengfenlei: [{ required: true, message: '请选择需求分类', trigger: 'change' }],
				xuqiuxiangqing: [{ required: true, message: '请填写需求描述', trigger: 'blur' }],
			},
			centerType: false,
		};
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
							let msg = (d.expired || hasPublished)
								? '您的VIP权限已到期，请先续费后再发布需求'
								: '只有VIP会员才可以发布需求，请先开通会员';
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
				this.ruleForm.fengmian = '';
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
}
.card-header {
	display: flex; align-items: center; justify-content: space-between;
	padding: 24px 28px; background: linear-gradient(135deg, #0f172a, #1e293b); position: relative;
	&::after { content: ''; position: absolute; bottom: 0; left: 0; right: 0; height: 3px; background: linear-gradient(90deg, $primary, $accent); }
	.header-left { display: flex; align-items: center; gap: 14px; }
	.header-icon-wrap {
		width: 44px; height: 44px; border-radius: 12px;
		background: linear-gradient(135deg, rgba(14,165,233,0.2), rgba(124,58,237,0.2));
		display: flex; align-items: center; justify-content: center;
		.el-icon { font-size: 20px; color: $primary; }
	}
	.header-text { display: flex; flex-direction: column; gap: 3px; }
	.header-title { font-size: 18px; font-weight: 700; color: #fff; }
	.header-sub { font-size: 12px; color: rgba(255,255,255,0.5); }
}
.back-btn {
	display: inline-flex; align-items: center; gap: 4px; padding: 8px 18px; border-radius: 20px;
	border: 1px solid rgba(255,255,255,0.15); background: rgba(255,255,255,0.06);
	color: rgba(255,255,255,0.7); font-size: 12px; font-weight: 500; cursor: pointer; transition: all 0.25s;
	&:hover { color: #fff; background: rgba(255,255,255,0.1); border-color: rgba(255,255,255,0.25); }
}
.demand-form {
	padding: 8px 28px 0;
	.form-section {
		padding: 20px 0 8px;
		&:not(:first-child) { border-top: 1.5px solid $border-light; margin-top: 4px; }
	}
	.section-label {
		font-size: 13px; font-weight: 700; color: $primary; margin-bottom: 16px;
		display: flex; align-items: center; gap: 6px;
		.el-icon { font-size: 15px; }
		&::after { content: ''; flex: 1; height: 1px; background: linear-gradient(90deg, $border, transparent); margin-left: 10px; }
	}
	.inline-row { display: flex; gap: 20px; > .el-form-item { flex: 1; min-width: 0; } }
	::v-deep .el-form-item { margin-bottom: 20px; }
}
.editor-item {
	::v-deep .el-form-item__content { line-height: normal; }
	::v-deep .editor {
		margin: 0; border-radius: 10px; overflow: hidden;
		border: 1.5px solid $border; transition: all 0.25s; background: #fff;
		&:focus-within { border-color: $primary; box-shadow: 0 0 0 3px rgba(14,165,233,0.1); }
	}
}
.card-footer {
	display: flex; justify-content: flex-end; align-items: center; gap: 12px;
	padding: 20px 28px; border-top: 1.5px solid $border-light; background: #fafbfc;
}
.submit-btn {
	display: inline-flex; align-items: center; gap: 6px; padding: 11px 36px; border-radius: 22px;
	border: none; background: linear-gradient(135deg, $primary, $accent);
	color: #fff; font-size: 14px; font-weight: 600; cursor: pointer;
	transition: all 0.3s; box-shadow: 0 4px 14px rgba(14,165,233,0.25); letter-spacing: 0.3px;
	.el-icon { font-size: 15px; margin-right: 2px; vertical-align: middle; }
	&:hover { transform: translateY(-2px); box-shadow: 0 8px 24px rgba(14,165,233,0.35); }
	&:active { transform: translateY(0); }
}
.cancel-btn {
	padding: 11px 24px; border-radius: 22px; border: 1.5px solid $border;
	background: #fff; color: $text-mid; font-size: 13px; font-weight: 500; cursor: pointer; transition: all 0.25s;
	&:hover { color: #ef4444; border-color: #fca5a5; background: #fef2f2; }
}
</style>
