<template>
	<div class="detail-page">
		<div class="detail-topbar">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2"><a @click="back()">我的技能</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item3"><a href="javascript:void(0);">{{ruleForm.id ? '编辑技能' : '发布技能'}}</a></el-breadcrumb-item>
			</el-breadcrumb>
			<el-button class="back-btn" size="small" @click="back()">
				<template #icon><ArrowLeft /></template>
				返回
			</el-button>
		</div>

		<el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-position="top" class="post-form">
			<div class="detail-card form-card">
				<div class="card-info">
					<div class="title-row">
						<h2 class="detail-title">{{ruleForm.id ? '编辑技能' : '发布技能'}}</h2>
						<div class="meta-tags">
							<span class="tag"><i class="el-icon-edit"></i> {{ruleForm.id ? '修改已有技能信息' : '发布一个新技能到技能市场'}}</span>
						</div>
					</div>
				</div>
			</div>

			<el-tabs class="detail-tabs" v-model="activeTab" type="border-card">
				<el-tab-pane label="基本信息" name="1">
					<div class="tab-scroll-area">
						<div class="tab-form-content">
							<el-form-item label="技能名称" prop="xuqiubiaoti">
								<el-input v-model="ruleForm.xuqiubiaoti" placeholder="例如：Python编程入门教学、吉他弹唱一对一指导" clearable maxlength="50" show-word-limit></el-input>
							</el-form-item>
							<div class="form-row">
								<el-form-item label="技能分类" prop="jinengfenlei" class="form-row-item">
									<el-select v-model="ruleForm.jinengfenlei" placeholder="选择技能分类" filterable>
										<el-option v-for="(item,index) in jinengfenleiOptions" :key="index" :label="item" :value="item"></el-option>
									</el-select>
								</el-form-item>
								<el-form-item label="技能定价" prop="xuqiufeiyong" class="form-row-item">
									<el-input v-model="ruleForm.xuqiufeiyong" placeholder="留空则显示为面议" type="number">
										<template #prepend>¥</template>
										<template #append>元</template>
									</el-input>
								</el-form-item>
							</div>
							<el-form-item label="展示封面" prop="fengmian" class="cover-upload">
								<p class="section-tip">上传技能相关图片，让你的技能更有吸引力</p>
								<file-upload
									tip="点击上传封面图"
									action="file/upload"
									:limit="3"
									:multiple="true"
									:fileUrls="ruleForm.fengmian ? ruleForm.fengmian : ''"
									@change="coverUploadChange"
								></file-upload>
							</el-form-item>
						</div>
					</div>
				</el-tab-pane>
				<el-tab-pane label="技能详细介绍" name="2">
					<div class="tab-scroll-area">
						<div class="tab-form-content">
							<p class="section-tip">详细描述你的技能水平、教学方式、可提供的服务等</p>
							<el-form-item prop="xuqiuxiangqing" class="desc-textarea">
								<el-input type="textarea" v-model="ruleForm.xuqiuxiangqing" placeholder="描述你的技能水平、教学方式、可提供的服务等" :rows="8" maxlength="2000" show-word-limit resize="vertical"></el-input>
							</el-form-item>
						</div>
					</div>
				</el-tab-pane>
			</el-tabs>

			<input type="hidden" v-model="ruleForm.xuqiubianhao">
			<input type="hidden" v-model="ruleForm.yonghuzhanghao">
			<input type="hidden" v-model="ruleForm.yonghuxingming">
			<input type="hidden" v-model="ruleForm.shoujihao">
			<input type="hidden" v-model="ruleForm.xinyuzhishu">

			<div class="form-actions">
				<el-button class="act-btn act-edit" size="small" @click="onSubmit">
					<template #icon><Promotion /></template>
					{{ruleForm.id ? '保存修改' : '发布技能'}}
				</el-button>
				<el-button class="act-btn act-cancel" size="small" @click="back()">
					<template #icon><i class="el-icon-close"></i></template>
					取消
				</el-button>
			</div>
		</el-form>
	</div>
</template>

<script>
export default {
	data() {
		return {
			baseUrl: '',
			userTableName: localStorage.getItem('UserTableName'),
			isVip: false,
			activeTab: '1',
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
			jinengfenleiOptions: [],
			rules: {
				xuqiubiaoti: [{ required: true, message: '请输入技能名称', trigger: 'blur' }],
				jinengfenlei: [{ required: true, message: '请选择技能分类', trigger: 'change' }],
				xuqiuxiangqing: [{ required: true, message: '请填写技能介绍', trigger: 'blur' }],
			},
		};
	},
	created() {
		this.baseUrl = this.$config.baseUrl;
		this.ruleForm.fabushijian = this.getCurDate();
		this.loadUserInfo();
		this.loadCategories();
		if (this.$route.query.type === 'edit' && this.$route.query.id) {
			this.loadDetail(this.$route.query.id);
		}
	},
	methods: {
		getUUID() {
			return new Date().getTime();
		},
		loadUserInfo() {
			this.$http.get(this.userTableName + '/session').then(res => {
				if (res.data.code == 0) {
					let json = res.data.data;
					if (json.yonghuzhanghao) this.ruleForm.yonghuzhanghao = json.yonghuzhanghao;
					if (json.yonghuxingming) this.ruleForm.yonghuxingming = json.yonghuxingming;
					if (json.shoujihao) this.ruleForm.shoujihao = json.shoujihao;
					if (json.xinyuzhishu) this.ruleForm.xinyuzhishu = json.xinyuzhishu;
					this.isVip = json.vip === '是';
					if (!this.isVip && !this.$route.query.id) {
						this.$http.get('huiyuanchongzhi/vipStatus').then(vipRes => {
							let vipExpired = vipRes.data.code === 0 && vipRes.data.data.expired;
							this.$http.get('jinengxuqiu/page', {
								params: { page: 1, limit: 1, yonghuzhanghao: json.yonghuzhanghao }
							}).then(r => {
								let hasPublished = r.data.code == 0 && r.data.data.total > 0;
								let msg = (vipExpired || hasPublished)
									? '您的VIP权限已到期，请先续费后再发布技能'
									: '只有VIP会员才可以发布技能，请先开通会员';
								this.$alert(msg, '提示', {
									confirmButtonText: '返回',
									type: 'warning',
									callback: () => { this.back(); }
								});
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
		coverUploadChange(fileUrls) {
			this.ruleForm.fengmian = fileUrls.replace(/^\/+/, '');
		},
		onSubmit() {
			this.$refs['ruleForm'].validate(async valid => {
				if (!valid) return;
				if (this.ruleForm.xuqiubianhao) {
					this.ruleForm.xuqiubianhao = String(this.ruleForm.xuqiubianhao);
				}
				if (!this.ruleForm.id) {
					this.ruleForm.leixing = '技能';
					this.ruleForm.sfsh = '待审核';
				}
				const url = this.ruleForm.id ? 'jinengxuqiu/update' : 'jinengxuqiu/add';
				await this.$http.post(url, this.ruleForm).then(res => {
					if (res.data.code == 0) {
						this.$message({
							message: this.ruleForm.id ? '修改成功' : '发布成功，等待管理员审核',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.$router.push({ path: '/main/center', query: { tab: 'myPublish' } });
							}
						});
					} else {
						this.$message({ message: res.data.msg, type: 'error', duration: 1500 });
					}
				});
			});
		},
		back() {
			this.$router.push({ path: '/main/center', query: { tab: 'myPublish' } });
		},
	}
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$cyan: #00d4ff;
$purple: #7c3aed;
$gold: #f59e0b;
$text-dark: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: #64748b;
$border: rgba(0,212,255,0.12);
$bg: #0c1222;
$white: #0f172a;
$radius: 12px;

.detail-page {
	height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
	overflow: hidden;
	padding: 8px 20px 0;
	max-width: 1200px;
	margin: 0 auto;
	width: 100%;
}

.detail-topbar {
	display: flex; align-items: center; justify-content: space-between;
	margin-bottom: 6px; flex-shrink: 0;
	.back-btn {
		border-radius: 20px; border: 1px solid $border; color: $text-sub; font-size: 12px; padding: 4px 12px;
		&:hover { color: $primary; border-color: $primary; background: rgba(14,165,233,0.04); }
	}
}

.detail-card {
	display: flex; gap: 12px; background: $white; border-radius: $radius;
	box-shadow: 0 4px 20px rgba(0,0,0,0.3); padding: 10px; border: 1px solid $border;
	flex-shrink: 0; align-items: stretch;
}

.form-card.detail-card {
	padding: 14px 18px;
}

.card-info { flex: 1; display: flex; flex-direction: column; min-width: 0; justify-content: space-between; }

.title-row {
	display: flex; align-items: center; gap: 8px; flex-wrap: wrap;
	.detail-title { font-size: 16px; color: $text-dark; font-weight: 700; margin: 0; }
}

.meta-tags {
	display: flex; flex-wrap: wrap; gap: 4px;
	.tag {
		font-size: 11px; color: $text-sub; background: rgba(255,255,255,0.04); padding: 2px 8px;
		border-radius: 4px; border: 1px solid $border;
		display: inline-flex; align-items: center; gap: 3px; white-space: nowrap;
		.el-icon { font-size: 11px; color: $text-dim; }
	}
}

.detail-tabs {
	flex: 1; min-height: 0; margin-top: 6px;
	border: none !important; box-shadow: none !important; background: transparent !important;
	display: flex; flex-direction: column;
	::v-deep .el-tabs--border-card { border: none; box-shadow: none; background: transparent; }
	::v-deep .el-tabs__header {
		padding: 0; margin: 0; background: transparent; border: none; border-bottom: 1px solid $border; flex-shrink: 0;
	}
	::v-deep .el-tabs__header .el-tabs__nav { border: none; }
	::v-deep .el-tabs__header .el-tabs__item {
		border: none !important; padding: 0 4px 6px; margin: 0 18px 0 0;
		color: $text-sub; font-weight: 500; font-size: 13px; background: none !important;
		height: auto; position: relative;
	}
	::v-deep .el-tabs__header .el-tabs__item::after {
		content: ""; position: absolute; left: 0; right: 0; bottom: 0;
		height: 2px; background: transparent; border-radius: 1px; transition: background 0.2s;
	}
	::v-deep .el-tabs__header .el-tabs__item:hover { color: $primary; }
	::v-deep .el-tabs__header .el-tabs__item.is-active { color: $text-dark; font-weight: 600; }
	::v-deep .el-tabs__header .el-tabs__item.is-active::after { background: $primary; }
	::v-deep .el-tabs__content { flex: 1; min-height: 0; padding: 0; background: transparent; overflow: hidden;
		display: flex; flex-direction: column;
	}
	::v-deep .el-tab-pane { height: 100%; display: flex; flex-direction: column; }
	::v-deep .el-tabs__nav-wrap::after { display: none; }
}

.tab-scroll-area {
	flex: 1; min-height: 0; overflow-y: auto; padding: 6px 0;
	&::-webkit-scrollbar { width: 4px; }
	&::-webkit-scrollbar-thumb { background: rgba(0,212,255,0.15); border-radius: 4px; }
}

.tab-form-content {
	background: $white; border-radius: $radius; border: 1px solid $border;
	box-shadow: 0 4px 20px rgba(0,0,0,0.3); padding: 18px 22px;
	color: $text-dark;
}

.section-tip {
	font-size: 12px; color: $text-dim; margin: 0 0 12px 0;
}

.form-row {
	display: flex; gap: 16px;
	.form-row-item { flex: 1; min-width: 0; }
}

.post-form {
	display: flex; flex-direction: column; flex: 1; min-height: 0;
	::v-deep .el-form-item { margin-bottom: 16px; }
	::v-deep .el-form-item__label {
		font-size: 13px; color: $text-sub; font-weight: 500; padding-bottom: 4px; line-height: 1.4;
	}
	::v-deep .el-input__inner {
		background: rgba(255,255,255,0.03); border: 1px solid $border; border-radius: 8px;
		height: 38px; font-size: 13px; color: $text-dark;
		transition: border-color 0.2s, box-shadow 0.2s;
		&:focus { border-color: $primary; box-shadow: 0 0 0 3px rgba(14,165,233,0.08); }
		&::placeholder { color: $text-dim; }
	}
	::v-deep .el-select { width: 100%; }
	::v-deep .el-input-group__prepend,
	::v-deep .el-input-group__append {
		background: rgba(255,255,255,0.03); border-color: $border; color: $primary; font-weight: 600;
	}
	::v-deep .el-input__count-inner { background: transparent !important; color: $text-dim; }
	::v-deep .el-input__count { background: transparent !important; color: $text-dim; }
}

.cover-upload {
	::v-deep .el-form-item__content { line-height: normal; }
	::v-deep .upload .upload-img,
	::v-deep .el-upload .el-icon-plus {
		border: 2px dashed $border; border-radius: 8px; color: $text-dim;
		width: 120px; height: 120px; line-height: 120px; font-size: 28px;
		background: rgba(255,255,255,0.03); transition: all 0.25s;
		&:hover { border-color: $primary; color: $primary; background: rgba(14,165,233,0.04); }
	}
	::v-deep .upload-img { width: 120px !important; height: 120px !important; border-radius: 8px; object-fit: cover; }
	::v-deep .el-upload__tip { color: $text-dim; font-size: 11px; }
}

.desc-textarea {
	::v-deep .el-textarea__inner {
		background: rgba(255,255,255,0.03) !important;
		border: 1px solid $border !important;
		border-radius: 8px !important;
		color: $text-dark !important;
		font-size: 13px !important;
		line-height: 1.7 !important;
		padding: 12px 14px !important;
		transition: border-color 0.2s, box-shadow 0.2s !important;
		&::placeholder { color: $text-dim !important; }
		&:focus {
			border-color: $primary !important;
			box-shadow: 0 0 0 3px rgba(14,165,233,0.08) !important;
		}
	}
	::v-deep .el-input__count { background: transparent !important; color: $text-dim !important; }
}

.form-actions {
	display: flex; justify-content: flex-end; gap: 8px;
	margin-top: 8px; padding-top: 8px; flex-shrink: 0;
	.act-btn { border-radius: 6px; font-size: 12px; padding: 6px 16px; font-weight: 500; transition: all 0.3s; }
	.act-edit { background: linear-gradient(135deg, $primary, $purple); border: none; color: #fff;
		&:hover { box-shadow: 0 3px 12px rgba(14,165,233,0.25); }
	}
	.act-cancel { background: $white; border: 1px solid $border; color: $text-sub;
		&:hover { color: $primary; border-color: $primary; background: rgba(14,165,233,0.04); }
	}
}

@media (max-width: 768px) {
	.form-row { flex-direction: column; gap: 0; }
}
</style>
