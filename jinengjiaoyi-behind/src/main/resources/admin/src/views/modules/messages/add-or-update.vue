














<template>
	<div class="msg-form-page">
		<el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="0">

			<!-- 留言卡片 -->
			<div class="msg-card msg-user-card">
				<div class="card-header">
					<div class="user-avatar">{{(ruleForm.username||'?').substring(0,1)}}</div>
					<div class="user-info">
						<div class="user-name">{{ruleForm.username || '未知用户'}}</div>
						<div class="user-label">留言用户</div>
					</div>
				</div>
				<div class="card-body">
					<div class="msg-bubble">
						<div v-if="type=='msg' || type=='info'" v-html="ruleForm.content"></div>
						<el-input v-else type="textarea" v-model="ruleForm.content" :rows="3" placeholder="请输入留言内容..." resize="vertical"></el-input>
					</div>
					<div class="msg-images" v-if="type=='msg' || type=='info'">
						<template v-if="ruleForm.cpicture">
							<img v-if="ruleForm.cpicture.substring(0,4)=='http'" :src="ruleForm.cpicture.split(',')[0]" @click="imgPreView(ruleForm.cpicture.split(',')[0])">
							<img v-else v-for="(item,index) in ruleForm.cpicture.split(',')" :key="index" :src="$base.url+item" @click="imgPreView($base.url+item)">
						</template>
					</div>
					<div class="msg-images" v-else>
						<file-upload tip="点击上传留言图片" action="file/upload" :limit="3" :multiple="true" :disabled="ro.cpicture" :fileUrls="ruleForm.cpicture?ruleForm.cpicture:''" @change="cpictureUploadChange"></file-upload>
					</div>
				</div>
			</div>

			<!-- 回复卡片 -->
			<div class="msg-card msg-reply-card">
				<div class="card-header reply-header">
					<div class="reply-icon"><i class="el-icon-chat-dot-round"></i></div>
					<div class="reply-title">管理员回复</div>
				</div>
				<div class="card-body">
					<div class="reply-input-area">
						<el-form-item prop="reply" style="margin-bottom:0;">
							<el-input v-if="type!='info'" type="textarea" v-model="ruleForm.reply" :rows="4" placeholder="请输入回复内容..." resize="vertical"></el-input>
							<div v-else-if="ruleForm.reply" class="reply-bubble" v-html="ruleForm.reply"></div>
							<div v-else class="reply-empty">暂无回复</div>
						</el-form-item>
					</div>
					<div class="reply-upload" v-if="type!='info'">
						<label class="upload-label"><i class="el-icon-picture-outline"></i> 回复图片（可选）</label>
						<file-upload tip="点击上传" action="file/upload" :limit="3" :disabled="ro.rpicture" :multiple="true" :fileUrls="ruleForm.rpicture?ruleForm.rpicture:''" @change="rpictureUploadChange"></file-upload>
					</div>
					<div class="msg-images" v-else-if="ruleForm.rpicture">
						<img v-if="ruleForm.rpicture.substring(0,4)=='http'" :src="ruleForm.rpicture.split(',')[0]" @click="imgPreView(ruleForm.rpicture.split(',')[0])">
						<img v-else v-for="(item,index) in ruleForm.rpicture.split(',')" :key="index" :src="$base.url+item" @click="imgPreView($base.url+item)">
					</div>
				</div>
			</div>

			<!-- 操作栏 -->
			<div class="msg-actions">
				<el-button v-if="type!='info'" class="msg-btn-submit" @click="onSubmit"><i class="el-icon-check"></i> 提交回复</el-button>
				<el-button class="msg-btn-cancel" @click="back()"><i class="el-icon-arrow-left"></i> 返回</el-button>
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
$green: #34d399;
$bg-page: transparent;
$bg-card: #1e293b;
$bg-input: rgba(255,255,255,0.04);
$border: rgba(14,165,233,0.12);
$text: #f1f5f9;
$text-dim: #94a3b8;
$radius: 12px;

.msg-form-page {
	padding: 0;
	max-width: 720px;
	margin: 0 auto;
}

.msg-card {
	background: $bg-card;
	border: 1px solid $border;
	border-radius: $radius;
	margin-bottom: 16px;
	overflow: hidden;
}

.msg-user-card {
	.card-header {
		display: flex; align-items: center; gap: 14px; padding: 16px 20px;
		border-bottom: 1px solid rgba(255,255,255,0.04);
	}
	.user-avatar {
		width: 44px; height: 44px; border-radius: 50%; background: linear-gradient(135deg, $accent, $accent2);
		display: flex; align-items: center; justify-content: center;
		font-size: 18px; font-weight: 700; color: #fff; flex-shrink: 0;
	}
	.user-info { display: flex; flex-direction: column; }
	.user-name { font-size: 15px; font-weight: 700; color: $text; }
	.user-label { font-size: 12px; color: $text-dim; margin-top: 2px; }
}

.card-body { padding: 16px 20px; }

.msg-bubble {
	background: rgba(14,165,233,0.06); border: 1px solid rgba(14,165,233,0.1);
	border-radius: 4px 12px 12px 12px; padding: 14px 18px;
	font-size: 14px; color: $text; line-height: 1.8; word-break: break-word;
	position: relative;
}

.msg-images {
	display: flex; gap: 8px; flex-wrap: wrap; margin-top: 12px;
	img {
		width: 72px; height: 72px; object-fit: cover; border-radius: 8px;
		border: 1px solid $border; cursor: pointer; transition: all .2s;
		&:hover { transform: scale(1.08); box-shadow: 0 4px 12px rgba(0,0,0,0.3); }
	}
}

.msg-reply-card {
	border-color: rgba(52,211,153,0.15);
	.reply-header {
		display: flex; align-items: center; gap: 10px; padding: 14px 20px;
		border-bottom: 1px solid rgba(255,255,255,0.04);
		background: rgba(52,211,153,0.03);
	}
	.reply-icon {
		width: 32px; height: 32px; border-radius: 8px;
		background: linear-gradient(135deg, rgba(52,211,153,0.12), rgba(52,211,153,0.06));
		display: flex; align-items: center; justify-content: center;
		i { font-size: 16px; color: $green; }
	}
	.reply-title { font-size: 14px; font-weight: 700; color: $text; }
}

.reply-input-area { margin-bottom: 12px; }

.reply-bubble {
	background: rgba(52,211,153,0.06); border: 1px solid rgba(52,211,153,0.12);
	border-radius: 12px 4px 12px 12px; padding: 14px 18px;
	font-size: 14px; color: $text; line-height: 1.8; word-break: break-word;
}

.reply-empty { color: rgba(148,163,184,0.4); font-size: 13px; padding: 10px 0; font-style: italic; }

.reply-upload {
	.upload-label { display: flex; align-items: center; gap: 4px; font-size: 12px; color: $text-dim; margin-bottom: 8px; font-weight: 500; }
}

.msg-form-page ::v-deep .el-form-item { margin-bottom: 0; }
.msg-form-page ::v-deep .el-form-item__label { display: none; }
.msg-form-page ::v-deep .el-form-item__content { margin-left: 0 !important; }

.msg-form-page .el-textarea ::v-deep .el-textarea__inner {
	border: 1px solid $border; border-radius: 10px; padding: 14px 16px;
	color: $text; background: $bg-input; width: 100%;
	font-size: 14px; line-height: 1.7; font-family: inherit; resize: vertical; min-height: 100px;
	transition: border-color .25s, box-shadow .25s;
	&:focus { border-color: $green; box-shadow: 0 0 0 3px rgba(52,211,153,0.08); }
	&::placeholder { color: rgba(148,163,184,0.4); }
}

.msg-form-page ::v-deep .el-upload--picture-card { background: transparent; border: 0; border-radius: 0; width: auto; height: auto; line-height: initial; }
.msg-form-page ::v-deep .el-upload .el-icon-plus {
	border: 1px dashed rgba(52,211,153,0.3); border-radius: 8px; color: $green;
	font-size: 20px; width: 56px; height: 56px; line-height: 56px; text-align: center;
	background: rgba(52,211,153,0.03); transition: all .25s;
	&:hover { border-color: $green; background: rgba(52,211,153,0.08); }
}
.msg-form-page ::v-deep .el-upload-list .el-upload-list__item { border: 1px solid $border; border-radius: 8px; width: 56px; height: 56px; }
.msg-form-page ::v-deep .el-upload__tip { color: $text-dim; font-size: 11px; padding-top: 2px; }
.msg-form-page ::v-deep .el-upload-dragger {
	color: $text-dim; display: flex; flex-wrap: wrap; align-items: center; justify-content: center;
	border: 1px dashed rgba(52,211,153,0.2); border-radius: 8px; background: rgba(52,211,153,0.02);
	width: 100%; height: 64px; transition: all .25s;
	&:hover { border-color: $green; background: rgba(52,211,153,0.05); }
}
.msg-form-page ::v-deep .el-upload-dragger .el-icon-upload { margin: 0; color: $green; width: 100%; font-size: 28px; opacity: 0.5; }
.msg-form-page ::v-deep .el-upload-dragger .el-upload__text { color: $text-dim; text-align: center; width: 100%; font-size: 11px; line-height: 1; em { font-style: normal; color: $green; } }

.msg-actions {
	display: flex; justify-content: center; gap: 12px; padding: 16px 0 0;
}

.msg-btn-submit {
	border: none !important; padding: 0 36px !important; color: #fff !important;
	font-weight: 600; font-size: 14px !important; border-radius: 22px !important;
	background: linear-gradient(135deg, $accent, $accent2) !important;
	min-width: 130px; height: 40px !important;
	box-shadow: 0 2px 12px rgba(14,165,233,0.2); transition: all .3s;
	i { margin-right: 5px; }
	&:hover { box-shadow: 0 6px 20px rgba(14,165,233,0.3); transform: translateY(-1px); }
}

.msg-btn-cancel {
	border: 1px solid $border !important; padding: 0 36px !important;
	color: $text-dim !important; font-weight: 600; font-size: 14px !important;
	border-radius: 22px !important; background: transparent !important;
	min-width: 100px; height: 40px !important; transition: all .3s;
	i { margin-right: 5px; }
	&:hover { border-color: $accent !important; color: $text !important; }
}
</style>
