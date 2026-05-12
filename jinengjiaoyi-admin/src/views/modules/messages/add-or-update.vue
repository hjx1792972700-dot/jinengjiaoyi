














<template>
	<div class="msg-form-page">
		<el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="0">

			<!-- Section 1: User & Images -->
			<div class="msg-section">
				<div class="msg-section-header"><i class="el-icon-user"></i><span>留言信息</span></div>
				<div class="msg-section-body">
					<div class="msg-grid-2">
						<el-form-item prop="username">
							<label class="msg-label">用户名</label>
							<div class="msg-readonly">{{ruleForm.username || '-'}}</div>
						</el-form-item>
						<el-form-item prop="cpicture">
							<label class="msg-label">留言图片</label>
							<div v-if="type=='msg' || type=='info'">
								<div v-if="ruleForm.cpicture" class="msg-imgs">
									<img v-if="ruleForm.cpicture.substring(0,4)=='http'" :src="ruleForm.cpicture.split(',')[0]" @click="imgPreView(ruleForm.cpicture.split(',')[0])">
									<img v-else v-for="(item,index) in ruleForm.cpicture.split(',')" :key="index" :src="$base.url+item" @click="imgPreView($base.url+item)">
								</div>
								<div v-else class="msg-empty">无图片</div>
							</div>
							<file-upload v-else tip="点击上传留言图片" action="file/upload" :limit="3" :multiple="true" :disabled="ro.cpicture" :fileUrls="ruleForm.cpicture?ruleForm.cpicture:''" @change="cpictureUploadChange"></file-upload>
						</el-form-item>
					</div>
					<el-form-item prop="content">
						<label class="msg-label">留言内容</label>
						<div v-if="type=='msg' || type=='info'" class="msg-content-box" v-html="ruleForm.content"></div>
						<el-input v-else type="textarea" v-model="ruleForm.content" :rows="3" placeholder="请输入留言内容..." resize="vertical"></el-input>
					</el-form-item>
				</div>
			</div>

			<!-- Section 2: Reply -->
			<div class="msg-section">
				<div class="msg-section-header"><i class="el-icon-chat-dot-round"></i><span>回复管理</span></div>
				<div class="msg-section-body">
					<el-form-item prop="rpicture">
						<label class="msg-label">回复图片</label>
						<file-upload v-if="type!='info' && !ro.rpicture" tip="点击上传回复图片" action="file/upload" :limit="3" :disabled="ro.rpicture" :multiple="true" :fileUrls="ruleForm.rpicture?ruleForm.rpicture:''" @change="rpictureUploadChange"></file-upload>
						<div v-else-if="ruleForm.rpicture" class="msg-imgs">
							<img v-if="ruleForm.rpicture.substring(0,4)=='http'" :src="ruleForm.rpicture.split(',')[0]" @click="imgPreView(ruleForm.rpicture.split(',')[0])">
							<img v-else v-for="(item,index) in ruleForm.rpicture.split(',')" :key="index" :src="$base.url+item" @click="imgPreView($base.url+item)">
						</div>
						<div v-else class="msg-empty">无图片</div>
					</el-form-item>
					<el-form-item prop="reply">
						<label class="msg-label">回复内容</label>
						<el-input v-if="type!='info'" type="textarea" v-model="ruleForm.reply" :rows="3" placeholder="请输入回复内容..." resize="vertical"></el-input>
						<div v-else-if="ruleForm.reply" class="msg-content-box" v-html="ruleForm.reply"></div>
						<div v-else class="msg-empty">暂无回复</div>
					</el-form-item>
				</div>
			</div>

			<!-- Actions -->
			<div class="msg-actions">
				<el-button v-if="type!='info'" class="msg-btn-submit" @click="onSubmit"><i class="el-icon-check"></i> 提交回复</el-button>
				<el-button v-if="type!='info'" class="msg-btn-cancel" @click="back()"><i class="el-icon-close"></i> 取消</el-button>
				<el-button v-if="type=='info'" class="msg-btn-cancel" @click="back()"><i class="el-icon-back"></i> 返回列表</el-button>
			</div>
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
					userid : false,
					username : false,
					avatarurl : false,
					content : false,
					cpicture : false,
					reply : false,
					rpicture : false,
				},
			
				ruleForm: {
					userid: '',
					username: '',
					avatarurl: '',
					content: '',
					cpicture: '',
					reply: '',
					rpicture: '',
				},

				rules: {
					userid: [
						{ required: true, message: '留言人id不能为空', trigger: 'blur' },
					],
					username: [
					],
					avatarurl: [
					],
					content: [
						{ required: true, message: '留言内容不能为空', trigger: 'blur' },
					],
					cpicture: [
					],
					reply: [
					],
					rpicture: [
					],
				},
			};
		},
		props: ["parent"],
		computed: {
			sessionForm() {
				return JSON.parse(this.$storage.getObj('userForm'))
			},
			sessionTable() {
				return this.$storage.get('sessionTable')
			},



		},
		components: {},
		created() {
		},
		methods: {
			imgPreView(url){
				this.$parent.imgPreView(url)
			},
			// 下载
			download(file){
				window.open(`${file}`)
			},
			// 初始化
			init(id,type ) {
				if (id) {
					this.id = id;
					this.type = type;
				}
				if(this.type=='info'||this.type=='else'||this.type=='msg'){
					this.info(id);
				}else if(this.type=='logistics'){
					for(let x in this.ro) {
						this.ro[x] = true
					}
					this.logistics=false;
					this.info(id);
				}else if(this.type=='cross'){
					var obj = this.$storage.getObj('crossObj');
					for (var o in obj){
						if(o=='userid'){
							this.ruleForm.userid = obj[o];
							this.ro.userid = true;
							continue;
						}
						if(o=='username'){
							this.ruleForm.username = obj[o];
							this.ro.username = true;
							continue;
						}
						if(o=='avatarurl'){
							this.ruleForm.avatarurl = obj[o];
							this.ro.avatarurl = true;
							continue;
						}
						if(o=='content'){
							this.ruleForm.content = obj[o];
							this.ro.content = true;
							continue;
						}
						if(o=='cpicture'){
							this.ruleForm.cpicture = obj[o];
							this.ro.cpicture = true;
							continue;
						}
						if(o=='reply'){
							this.ruleForm.reply = obj[o];
							this.ro.reply = true;
							continue;
						}
						if(o=='rpicture'){
							this.ruleForm.rpicture = obj[o];
							this.ro.rpicture = true;
							continue;
						}
					}
				}

			
			},
			// 多级联动参数

			async info(id) {
				await this.$http({
					url: `messages/info/${id}`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
						//解决前台上传图片后台不显示的问题
						let reg=new RegExp('../../../upload','g')//g代表全部
					} else {
						this.$message.error(data.msg);
					}
				});
			},

			// 提交
			async onSubmit() {
					if(this.ruleForm.avatarurl!=null) {
						this.ruleForm.avatarurl = this.ruleForm.avatarurl.split(',').map(u => u.replace(/^\/+/, '')).join(',');
					}
					if(this.ruleForm.cpicture!=null) {
						this.ruleForm.cpicture = this.ruleForm.cpicture.split(',').map(u => u.replace(/^\/+/, '')).join(',');
					}
					if(this.ruleForm.rpicture!=null) {
						this.ruleForm.rpicture = this.ruleForm.rpicture.split(',').map(u => u.replace(/^\/+/, '')).join(',');
					}
					var objcross = this.$storage.getObj('crossObj');
					if(!this.ruleForm.id) {
						delete this.ruleForm.userid
					}
					await this.$refs["ruleForm"].validate(async valid => {
						if (valid) {
							if(this.type=='cross'){
								var statusColumnName = this.$storage.get('statusColumnName');
								var statusColumnValue = this.$storage.get('statusColumnValue');
								if(statusColumnName!='') {
									var obj = this.$storage.getObj('crossObj');
									if(statusColumnName && !statusColumnName.startsWith("[")) {
										for (var o in obj){
											if(o==statusColumnName){
												obj[o] = statusColumnValue;
											}
										}
										var table = this.$storage.get('crossTable');
										await this.$http({
											url: `${table}/update`,
											method: "post",
											data: obj
										}).then(({ data }) => {});
									}
								}
							}
							await this.$http({
								url: `messages/${!this.ruleForm.id ? "save" : "update"}`,
								method: "post",
								data: this.ruleForm
							}).then(async ({ data }) => {
								if (data && data.code === 0) {
									this.$message({
										message: "操作成功",
										type: "success",
										duration: 1500,
										onClose: () => {
											this.parent.showFlag = true;
											this.parent.addOrUpdateFlag = false;
											this.parent.messagesCrossAddOrUpdateFlag = false;
											this.parent.search();
										}
									});
								} else {
									this.$message.error(data.msg);
								}
							});
						}
					});
			},
			// 获取uuid
			getUUID () {
				return new Date().getTime();
			},
			// 返回
			back() {
				this.parent.showFlag = true;
				this.parent.addOrUpdateFlag = false;
				this.parent.messagesCrossAddOrUpdateFlag = false;
			},
			avatarurlUploadChange(fileUrls) {
				this.ruleForm.avatarurl = fileUrls;
			},
			cpictureUploadChange(fileUrls) {
				this.ruleForm.cpicture = fileUrls;
			},
			rpictureUploadChange(fileUrls) {
				this.ruleForm.rpicture = fileUrls;
			},
		}
	};
</script>
<style lang="scss" scoped>
$accent: #0ea5e9;
$accent2: #7c3aed;
$bg-page: #0b1120;
$bg-card: rgba(15,23,42,0.85);
$bg-input: rgba(255,255,255,0.06);
$border: rgba(100,160,220,0.12);
$border-focus: #0ea5e9;
$text: #e2e8f0;
$text-dim: #94a3b8;
$radius: 8px;

.msg-form-page {
	padding: 20px 24px 32px;
	width: 100%;
	background: $bg-page;
}

.msg-section {
	background: $bg-card;
	border: 1px solid $border;
	border-radius: 14px;
	margin-bottom: 16px;
	overflow: hidden;
	box-shadow: 0 2px 16px rgba(0,0,0,0.2);
}

.msg-section-header {
	display: flex;
	align-items: center;
	gap: 10px;
	padding: 12px 20px;
	border-bottom: 1px solid $border;
	background: rgba(14,165,233,0.04);
	i { font-size: 16px; color: $accent; }
	span { font-size: 14px; font-weight: 600; color: $text; letter-spacing: 0.5px; }
}

.msg-section-body {
	padding: 16px 20px;
}

.msg-grid-2 {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 14px 28px;
}

.msg-label {
	display: block;
	font-size: 13px;
	font-weight: 500;
	color: $text-dim;
	margin-bottom: 8px;
}

.msg-readonly {
	padding: 10px 14px;
	font-size: 14px;
	color: $text;
	background: rgba(255,255,255,0.03);
	border-radius: $radius;
	border: 1px solid transparent;
	min-height: 40px;
	display: flex;
	align-items: center;
}

.msg-content-box {
	padding: 12px 16px;
	color: $text;
	background: rgba(255,255,255,0.03);
	border: 1px solid $border;
	border-radius: $radius;
	font-size: 14px;
	line-height: 1.7;
	width: 100%;
	min-height: 60px;
	word-break: break-word;
}

.msg-imgs {
	display: flex;
	gap: 8px;
	flex-wrap: wrap;
	img {
		width: 80px;
		height: 80px;
		object-fit: cover;
		border-radius: 8px;
		border: 1px solid $border;
		cursor: pointer;
		transition: transform 0.2s;
		&:hover { transform: scale(1.05); }
	}
}

.msg-empty {
	color: rgba(148,163,184,0.5);
	font-size: 13px;
	padding: 8px 0;
}

.msg-form-page ::v-deep .el-form-item {
	margin-bottom: 14px;
}
.msg-form-page ::v-deep .el-form-item__label {
	display: none;
}
.msg-form-page ::v-deep .el-form-item__content {
	margin-left: 0 !important;
}

.msg-form-page .el-textarea ::v-deep .el-textarea__inner {
	border: 1px solid $border;
	border-radius: $radius;
	padding: 12px 16px;
	color: $text;
	background: $bg-input;
	width: 100%;
	font-size: 14px;
	line-height: 1.7;
	font-family: inherit;
	resize: vertical;
	min-height: 80px;
	transition: border-color 0.25s, box-shadow 0.25s;
	&:focus {
		border-color: $border-focus;
		box-shadow: 0 0 0 3px rgba(14,165,233,0.1);
	}
	&::placeholder { color: rgba(148,163,184,0.5); }
}

.msg-form-page ::v-deep .el-upload--picture-card {
	background: transparent;
	border: 0;
	border-radius: 0;
	width: auto;
	height: auto;
	line-height: initial;
}
.msg-form-page ::v-deep .el-upload .el-icon-plus {
	border: 1px dashed rgba(14,165,233,0.25);
	border-radius: 8px;
	color: $accent;
	font-size: 24px;
	width: 72px;
	height: 72px;
	line-height: 72px;
	text-align: center;
	background: rgba(14,165,233,0.03);
	transition: all 0.25s;
	&:hover { border-color: $accent; background: rgba(14,165,233,0.08); }
}
.msg-form-page ::v-deep .el-upload-list .el-upload-list__item {
	border: 1px solid $border;
	border-radius: 8px;
	width: 72px;
	height: 72px;
}
.msg-form-page ::v-deep .el-upload__tip {
	color: $text-dim;
	font-size: 12px;
	padding-top: 4px;
}
.msg-form-page ::v-deep .el-upload-dragger {
	color: $text-dim;
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	justify-content: center;
	border: 1px dashed rgba(14,165,233,0.2);
	border-radius: $radius;
	background: rgba(14,165,233,0.02);
	width: 100%;
	height: 80px;
	transition: all 0.25s;
	&:hover { border-color: $accent; background: rgba(14,165,233,0.05); }
}
.msg-form-page ::v-deep .el-upload-dragger .el-icon-upload {
	margin: 0;
	color: $accent;
	width: 100%;
	font-size: 32px;
	opacity: 0.5;
}
.msg-form-page ::v-deep .el-upload-dragger .el-upload__text {
	color: $text-dim;
	text-align: center;
	width: 100%;
	font-size: 12px;
	line-height: 1;
	em { font-style: normal; color: $accent; }
}

.msg-actions {
	display: flex;
	justify-content: center;
	gap: 16px;
	padding: 12px 0 8px;
	position: sticky;
	bottom: 0;
	background: $bg-page;
	z-index: 10;
	border-top: 1px solid $border;
	margin: 0 -24px;
	padding-left: 24px;
	padding-right: 24px;
}

.msg-btn-submit {
	border: none !important;
	padding: 0 40px !important;
	color: #fff !important;
	font-weight: 600;
	font-size: 15px !important;
	border-radius: 24px !important;
	background: linear-gradient(135deg, $accent, $accent2) !important;
	min-width: 140px;
	height: 44px !important;
	box-shadow: 0 2px 12px rgba(14,165,233,0.25);
	transition: all 0.3s;
	i { margin-right: 6px; }
	&:hover { box-shadow: 0 6px 20px rgba(14,165,233,0.35); transform: translateY(-2px); }
}

.msg-btn-cancel {
	border: 1px solid $border !important;
	padding: 0 40px !important;
	color: $text-dim !important;
	font-weight: 600;
	font-size: 15px !important;
	border-radius: 24px !important;
	background: transparent !important;
	min-width: 140px;
	height: 44px !important;
	transition: all 0.3s;
	i { margin-right: 6px; }
	&:hover { border-color: $accent !important; color: $text !important; background: rgba(14,165,233,0.04) !important; }
}
</style>
