<template>
	<div class="forum-add-page">
		<div class="page-header">
			<h2 class="page-title">{{isEdit ? '编辑帖子' : '发布新帖'}}</h2>
			<el-button class="back-btn" size="small" plain @click="back()">
				<el-icon><ArrowLeft /></el-icon> 返回
			</el-button>
		</div>

		<el-form class="post-form" :model="form" :rules="rules" ref="form" label-position="top">
			<div class="form-card">
				<div class="form-section">
					<div class="section-label">基本信息</div>
					<el-form-item label="标题" prop="title">
						<el-input v-model="form.title" placeholder="输入一个吸引人的标题" maxlength="50" show-word-limit></el-input>
					</el-form-item>
					<div class="form-row">
						<el-form-item label="分类" prop="typename" class="form-row-item">
							<el-select v-model="form.typename" placeholder="选择帖子分类">
								<el-option
									v-for="(item,index) in categoryList"
									:key="index"
									:label="item.typename"
									:value="item.typename">
								</el-option>
							</el-select>
						</el-form-item>
						<el-form-item label="类型" prop="isdone" class="form-row-item">
							<el-radio-group v-model="form.isdone">
								<el-radio label="开放">公开</el-radio>
								<el-radio label="关闭">私人</el-radio>
							</el-radio-group>
						</el-form-item>
						<el-form-item label="匿名发布" prop="isanon" class="form-row-item">
							<el-radio-group v-model="form.isanon">
								<el-radio :label="0">否</el-radio>
								<el-radio :label="1">是</el-radio>
							</el-radio-group>
						</el-form-item>
					</div>
				</div>

				<div class="form-section">
					<div class="section-label">封面图片</div>
					<el-form-item prop="cover" class="cover-upload">
						<file-upload
							tip="点击上传封面图"
							action="file/upload"
							:limit="3"
							:multiple="true"
							:fileUrls="form.cover?form.cover:''"
							@change="coverUploadChange"
						></file-upload>
					</el-form-item>
				</div>

			<div class="form-section">
				<div class="section-label">帖子内容</div>
				<el-form-item prop="content">
					<el-input
						type="textarea"
						:rows="8"
						v-model="form.content"
						placeholder="请输入内容...">
					</el-input>
				</el-form-item>
			</div>
			</div>

			<div class="form-actions">
				<el-button class="btn-submit" type="primary" @click="submitForm('form')">
					{{isEdit ? '保存修改' : '发布帖子'}}
				</el-button>
				<el-button class="btn-reset" @click="back()">取消</el-button>
			</div>
		</el-form>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				form: {
					title: '',
					isdone: '开放',
					content: '',
					parentid: 0,
					userid: Number(localStorage.getItem('frontUserid')),
					username: localStorage.getItem('username'),
					toptime: '',
					cover: '',
					isanon: 0,
				},
				isEdit: false,
				centerType: false,
				rules: {
					title: [
						{ required: true, message: '请输入标题', trigger: 'blur' }
					],
					cover: [
						{ required: true, message: '请上传封面图', trigger: 'blur' }
					],
					content: [
						{ required: true, message: '请输入内容', trigger: 'blur' }
					],
					typename: [
						{ required: true, message: '请选择分类', trigger: 'blur' }
					]
				},
				categoryList: [],
				sensitiveWordsArr: [],
			}
		},
		created() {
			if(this.$route.query.centerType){
				this.centerType = true
			}
			this.getCategoryList()
			if (this.$route.query.id != undefined) {
				this.isEdit = true;
				this.$http.get('forum/detail/' + this.$route.query.id,{}).then(res=>{
					if(res.data.code==0){
						this.form = res.data.data
					}
				})
			}
			this.getSensitiveWords()
		},
		methods: {
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			getCategoryList(){
				this.$http.get('forumtype/list', {params: {limit: 100}}).then(res => {
					if (res.data.code == 0) {
						this.categoryList = res.data.data.list
					}
				});
			},
			back(){
				if (this.centerType) {
					this.$router.push({ path: '/main/center', query: { tab: 'forum' } });
				} else {
					this.$router.go(-1);
				}
			},
			onEditorReady(editor) {
				editor.root.setAttribute('data-placeholder', "请输入内容...");
			},
			coverUploadChange(fileUrls) {
				this.form.cover = fileUrls.replace(/^\/+/, "");
			},
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						if(!this.isEdit){
							this.form.toptime = this.getCurDateTime()
						}
						if(this.form.cover==''){
							this.$message.error('请上传封面图')
							return false
						}
						for(var i=0; i<this.sensitiveWordsArr.length; i++){
							var reg = new RegExp(this.sensitiveWordsArr[i],"g");
							if (this.form.content.indexOf(this.sensitiveWordsArr[i]) > -1) {
								this.form.content = this.form.content.replace(reg,"**");
							}
						}
						this.$http.post(`forum/${this.isEdit ? 'update' : 'add'}`, this.form).then(res => {
							if (res.data.code === 0) {
								this.$message({
									message: this.isEdit ? '修改成功' : '发布成功',
									type: 'success',
									duration: 2000,
									onClose: () => {
										if (this.centerType) {
											this.$router.push({ path: '/main/center', query: { tab: 'forum' } });
										} else {
											this.$router.push('/main/myForumList');
										}
									}
								});
							} else {
								this.$message.error(res.data.msg);
							}
						});
					} else {
						return false;
					}
				});
			},
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$cyan: #00d4ff;
$purple: #7c3aed;
$bg-deep: #0c1222;
$bg-card: rgba(15,23,42,0.85);
$border-glow: rgba(0,212,255,0.12);
$text-bright: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: rgba(255,255,255,0.35);

.forum-add-page {
	max-width: 820px;
	margin: 0 auto;
	padding: 20px 24px 40px;
	min-height: calc(100vh - 120px);
	background: linear-gradient(180deg, $bg-deep 0%, #101828 100%);
}

.page-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 18px;

	.page-title {
		font-size: 20px;
		font-weight: 700;
		margin: 0;
		background: linear-gradient(90deg, $text-bright, $cyan);
		-webkit-background-clip: text;
		-webkit-text-fill-color: transparent;
	}

	.back-btn {
		border-color: $border-glow !important;
		color: $text-sub !important;
		background: rgba(255,255,255,0.03) !important;
		border-radius: 20px;
		padding: 6px 16px;
		.el-icon { vertical-align: middle; margin-right: 4px; }
		&:hover {
			color: $cyan !important;
			border-color: rgba(0,212,255,0.3) !important;
			background: rgba(0,212,255,0.06) !important;
		}
	}
}

.form-card {
	background: $bg-card;
	backdrop-filter: blur(20px);
	border-radius: 14px;
	border: 1px solid $border-glow;
	padding: 24px 28px;
	box-shadow: 0 8px 32px rgba(0,0,0,0.3);
}

.form-section {
	margin-bottom: 22px;
	&:last-child { margin-bottom: 0; }

	.section-label {
		font-size: 14px;
		font-weight: 600;
		color: $text-bright;
		margin-bottom: 12px;
		padding-left: 12px;
		border-left: 3px solid $cyan;
		letter-spacing: 0.5px;
	}
}

.form-row {
	display: flex;
	gap: 20px;
	.form-row-item { flex: 1; }
}

.post-form {
	::v-deep .el-form-item {
		margin-bottom: 16px;
	}
	::v-deep .el-form-item__label {
		font-size: 13px;
		color: $text-sub;
		padding-bottom: 4px;
		line-height: 1.4;
	}
	::v-deep .el-input__inner {
		background: rgba(255,255,255,0.04);
		border: 1px solid rgba(255,255,255,0.08);
		border-radius: 10px;
		height: 40px;
		font-size: 14px;
		color: $text-bright;
		transition: all 0.3s;
		&:focus {
			border-color: rgba(0,212,255,0.4);
			background: rgba(0,212,255,0.04);
			box-shadow: 0 0 12px rgba(0,212,255,0.08);
		}
		&::placeholder { color: $text-dim; }
	}
	::v-deep .el-select {
		width: 100%;
		.el-input__inner { cursor: pointer; }
	}
	::v-deep .el-input__count-inner,
	::v-deep .el-input__count {
		background: transparent !important;
		color: $text-dim;
	}
	::v-deep .el-radio {
		margin-right: 24px;
		color: $text-sub;
	}
	::v-deep .el-radio__input.is-checked .el-radio__inner {
		border-color: $cyan;
		background: $cyan;
	}
	::v-deep .el-radio__input.is-checked + .el-radio__label {
		color: $cyan;
	}
	::v-deep .el-radio__inner {
		background: rgba(255,255,255,0.04);
		border-color: rgba(255,255,255,0.15);
	}
	::v-deep .el-radio__label {
		color: $text-sub;
	}
}

.cover-upload {
	::v-deep .el-upload--picture-card {
		background: transparent;
		border: 0;
		border-radius: 0;
		width: auto;
		height: auto;
		line-height: initial;
	}
	::v-deep .upload .upload-img,
	::v-deep .el-upload .el-icon-plus {
		border: 2px dashed rgba(0,212,255,0.2);
		border-radius: 10px;
		color: $text-dim;
		width: 110px;
		height: 110px;
		font-size: 28px;
		line-height: 110px;
		text-align: center;
		background: rgba(0,212,255,0.03);
		transition: all 0.3s;
		cursor: pointer;
		&:hover {
			border-color: $cyan;
			color: $cyan;
			background: rgba(0,212,255,0.06);
			box-shadow: 0 0 16px rgba(0,212,255,0.1);
		}
	}
	::v-deep .el-upload-list .el-upload-list__item {
		border: 1px solid rgba(255,255,255,0.08);
		border-radius: 10px;
		width: 110px;
		height: 110px;
	}
	::v-deep .el-upload__tip {
		color: $text-dim;
	}
}

.editor-wrapper {
	::v-deep .el-form-item__content { line-height: normal; }
	::v-deep .editor {
		margin: 0;
		.ql-toolbar {
			border-radius: 10px 10px 0 0;
			border-color: rgba(255,255,255,0.08);
			background: rgba(255,255,255,0.03);
			padding: 6px 10px;
			.ql-stroke { stroke: $text-sub; }
			.ql-fill { fill: $text-sub; }
			.ql-picker-label { color: $text-sub; }
			button:hover .ql-stroke { stroke: $cyan; }
			button:hover .ql-fill { fill: $cyan; }
			button.ql-active .ql-stroke { stroke: $cyan; }
			button.ql-active .ql-fill { fill: $cyan; }
		}
		.ql-container {
			border-radius: 0 0 10px 10px;
			border-color: rgba(255,255,255,0.08);
			background: rgba(255,255,255,0.02);
			min-height: 200px;
			font-size: 14px;
			color: $text-bright;
		}
		.ql-editor {
			min-height: 200px;
			padding: 10px 14px;
			color: $text-bright;
		}
		.ql-editor.ql-blank::before {
			color: $text-dim;
			font-style: normal;
		}
	}
}

.form-actions {
	display: flex;
	justify-content: center;
	gap: 16px;
	margin-top: 20px;

	.btn-submit {
		background: linear-gradient(135deg, $cyan, $purple) !important;
		border: none !important;
		border-radius: 24px;
		padding: 10px 40px;
		font-size: 15px;
		font-weight: 600;
		min-width: 140px;
		letter-spacing: 1px;
		color: #fff !important;
		box-shadow: 0 4px 16px rgba(0,212,255,0.25);
		transition: all 0.3s;
		&:hover {
			transform: translateY(-2px);
			box-shadow: 0 8px 24px rgba(0,212,255,0.35);
		}
	}
	.btn-reset {
		border-radius: 24px;
		padding: 10px 28px;
		font-size: 14px;
		border: 1px solid rgba(255,255,255,0.1) !important;
		color: $text-sub !important;
		background: transparent !important;
		&:hover {
			color: $cyan !important;
			border-color: rgba(0,212,255,0.3) !important;
			background: rgba(0,212,255,0.04) !important;
		}
	}
}
</style>
