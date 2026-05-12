






















<template>
	<div class="modern-form-page">
		<el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="0">

			<!-- Section 1: Basic Info -->
			<div class="form-section">
				<div class="section-header">
					<i class="el-icon-document"></i>
					<span>基本信息</span>
				</div>
				<div class="section-body grid-2">
					<el-form-item prop="ziliaobiaoti">
						<label class="field-label">资料标题</label>
						<el-input v-if="type!='info'" v-model="ruleForm.ziliaobiaoti" placeholder="请输入资料标题" clearable :readonly="ro.ziliaobiaoti"></el-input>
						<div v-else class="field-readonly">{{ruleForm.ziliaobiaoti || '-'}}</div>
					</el-form-item>
					<el-form-item prop="ziliaoleixing">
						<label class="field-label">技能分类</label>
						<el-select v-if="type!='info'" :disabled="ro.ziliaoleixing" v-model="ruleForm.ziliaoleixing" placeholder="请选择技能分类" filterable style="width:100%">
							<el-option v-for="(item,index) in ziliaoleixingOptions" :key="index" :label="item" :value="item"></el-option>
						</el-select>
						<div v-else class="field-readonly">{{ruleForm.ziliaoleixing || '-'}}</div>
					</el-form-item>
					<el-form-item prop="jiage">
						<label class="field-label">资料价格 (元)</label>
						<el-input-number v-if="type!='info'" v-model="ruleForm.jiage" :min="0" :precision="0" placeholder="0为免费" :disabled="ro.jiage" style="width:100%"></el-input-number>
						<div v-else class="field-readonly">
							<span v-if="ruleForm.jiage > 0" class="price-val">¥{{ruleForm.jiage}}</span>
							<span v-else class="free-val">免费</span>
						</div>
					</el-form-item>
					<el-form-item prop="fabushijian">
						<label class="field-label">发布时间</label>
						<el-date-picker v-if="type!='info'" format="YYYY-MM-DD" value-format="YYYY-MM-DD" v-model="ruleForm.fabushijian" type="date" :readonly="ro.fabushijian" placeholder="选择发布日期" style="width:100%"></el-date-picker>
						<div v-else class="field-readonly">{{ruleForm.fabushijian || '-'}}</div>
					</el-form-item>
				</div>
			</div>

			<!-- Section 2: Media -->
			<div class="form-section">
				<div class="section-header">
					<i class="el-icon-picture"></i>
					<span>媒体资源</span>
				</div>
				<div class="section-body grid-3">
					<div class="media-card">
						<div class="media-card-title"><i class="el-icon-camera"></i> 封面图片</div>
						<el-form-item prop="ziliaofengmian">
							<file-upload v-if="type!='info' && !ro.ziliaofengmian"
								tip="点击上传封面"
								action="file/upload"
								:limit="3"
								:disabled="ro.ziliaofengmian"
								:multiple="true"
								:fileUrls="ruleForm.ziliaofengmian?ruleForm.ziliaofengmian:''"
								@change="ziliaofengmianUploadChange"
							></file-upload>
							<div v-else-if="ruleForm.ziliaofengmian" class="preview-imgs">
								<img v-if="ruleForm.ziliaofengmian.substring(0,4)=='http'" :src="ruleForm.ziliaofengmian.split(',')[0]" @click="imgPreView(ruleForm.ziliaofengmian.split(',')[0])">
								<img v-else v-for="(item,index) in ruleForm.ziliaofengmian.split(',')" :key="index" :src="$base.url+item" @click="imgPreView($base.url+item)">
							</div>
							<div v-else class="media-empty">暂无封面</div>
						</el-form-item>
					</div>
					<div class="media-card">
						<div class="media-card-title"><i class="el-icon-video-camera"></i> 视频资源</div>
						<el-form-item prop="ziliaoshipin">
							<file-upload v-if="type!='info' && !ro.ziliaoshipin"
								tip="点击上传视频"
								action="file/upload"
								:limit="1"
								:type="2"
								:multiple="true"
								:disabled="ro.ziliaoshipin"
								:fileUrls="ruleForm.ziliaoshipin?ruleForm.ziliaoshipin:''"
								@change="ziliaoshipinUploadChange"
							></file-upload>
							<el-button v-else-if="ruleForm.ziliaoshipin" class="media-action-btn" @click="download($base.url+ruleForm.ziliaoshipin)"><i class="el-icon-video-play"></i> 预览视频</el-button>
							<div v-else class="media-empty">暂无视频</div>
						</el-form-item>
					</div>
					<div class="media-card">
						<div class="media-card-title"><i class="el-icon-folder"></i> 附件文件</div>
						<el-form-item prop="ziliaofujian">
							<file-upload v-if="type!='info' && !ro.ziliaofujian"
								tip="点击上传附件"
								action="file/upload"
								:limit="1"
								:type="2"
								:multiple="true"
								:disabled="ro.ziliaofujian"
								:fileUrls="ruleForm.ziliaofujian?ruleForm.ziliaofujian:''"
								@change="ziliaofujianUploadChange"
							></file-upload>
							<el-button v-else-if="ruleForm.ziliaofujian" class="media-action-btn" @click="download($base.url+ruleForm.ziliaofujian)"><i class="el-icon-download"></i> 下载附件</el-button>
							<div v-else class="media-empty">暂无附件</div>
						</el-form-item>
					</div>
				</div>
			</div>

			<!-- Section 3: Content -->
			<div class="form-section">
				<div class="section-header">
					<i class="el-icon-edit"></i>
					<span>资料详情</span>
				</div>
				<div class="section-body">
					<el-form-item prop="ziliaoxiangqing" class="full-width">
						<el-input v-if="type!='info'" type="textarea" v-model="ruleForm.ziliaoxiangqing" :rows="4" placeholder="请输入资料详细描述..." :readonly="ro.ziliaoxiangqing" resize="vertical"></el-input>
						<div v-else-if="ruleForm.ziliaoxiangqing" class="detail-preview" v-html="ruleForm.ziliaoxiangqing"></div>
						<div v-else class="media-empty">暂无详情</div>
					</el-form-item>
				</div>
			</div>

			<!-- Actions -->
			<div class="form-actions">
				<el-button v-if="type!='info'" class="action-submit" @click="onSubmit"><i class="el-icon-check"></i> 保存发布</el-button>
				<el-button v-if="type!='info'" class="action-cancel" @click="back()"><i class="el-icon-close"></i> 取消</el-button>
				<el-button v-if="type=='info'" class="action-cancel" @click="back()"><i class="el-icon-back"></i> 返回列表</el-button>
			</div>
		</el-form>
	</div>
</template>
<script>
	import { 
		isIntNumer,
	} from "@/utils/validate";
	export default {
		data() {
			var validateIntNumber = (rule, value, callback) => {
				if(!value){
					callback();
				} else if (!isIntNumer(value)) {
					callback(new Error("请输入整数"));
				} else {
					callback();
				}
			};
			return {
				id: '',
				type: '',
			
			
				ro:{
					ziliaobiaoti : false,
					ziliaoleixing : false,
					jiage : false,
					ziliaofengmian : false,
					ziliaoshipin : false,
					ziliaofujian : false,
					ziliaoxiangqing : false,
					fabushijian : false,
					clicktime : false,
					clicknum : false,
					discussnum : false,
					storeupnum : false,
				},
			
				ruleForm: {
					ziliaobiaoti: '',
					ziliaoleixing: '',
					jiage: 0,
					ziliaofengmian: '',
					ziliaoshipin: '',
					ziliaofujian: '',
					ziliaoxiangqing: '',
					fabushijian: '',
					clicktime: '',
					clicknum: Number('0'),
					discussnum: Number('0'),
					storeupnum: Number('0'),
				},
				ziliaoleixingOptions: [],

				rules: {
					ziliaobiaoti: [
					],
					ziliaoleixing: [
					],
					ziliaofengmian: [
					],
					ziliaoshipin: [
						{ required: true, message: '资料视频不能为空', trigger: 'blur' },
					],
					ziliaofujian: [
						{ required: true, message: '资料附件不能为空', trigger: 'blur' },
					],
					ziliaoxiangqing: [
					],
					fabushijian: [
					],
					clicktime: [
					],
					clicknum: [
						{ validator: validateIntNumber, trigger: 'blur' },
					],
					discussnum: [
						{ validator: validateIntNumber, trigger: 'blur' },
					],
					storeupnum: [
						{ validator: validateIntNumber, trigger: 'blur' },
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
			this.ruleForm.fabushijian = this.getCurDate()
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
						if(o=='ziliaobiaoti'){
							this.ruleForm.ziliaobiaoti = obj[o];
							this.ro.ziliaobiaoti = true;
							continue;
						}
						if(o=='ziliaoleixing'){
							this.ruleForm.ziliaoleixing = obj[o];
							this.ro.ziliaoleixing = true;
							continue;
						}
						if(o=='ziliaofengmian'){
							this.ruleForm.ziliaofengmian = obj[o];
							this.ro.ziliaofengmian = true;
							continue;
						}
						if(o=='ziliaoshipin'){
							this.ruleForm.ziliaoshipin = obj[o];
							this.ro.ziliaoshipin = true;
							continue;
						}
						if(o=='ziliaofujian'){
							this.ruleForm.ziliaofujian = obj[o];
							this.ro.ziliaofujian = true;
							continue;
						}
						if(o=='ziliaoxiangqing'){
							this.ruleForm.ziliaoxiangqing = obj[o];
							this.ro.ziliaoxiangqing = true;
							continue;
						}
						if(o=='fabushijian'){
							this.ruleForm.fabushijian = obj[o];
							this.ro.fabushijian = true;
							continue;
						}
						if(o=='clicktime'){
							this.ruleForm.clicktime = obj[o];
							this.ro.clicktime = true;
							continue;
						}
						if(o=='clicknum'){
							this.ruleForm.clicknum = obj[o];
							this.ro.clicknum = true;
							continue;
						}
						if(o=='discussnum'){
							this.ruleForm.discussnum = obj[o];
							this.ro.discussnum = true;
							continue;
						}
						if(o=='storeupnum'){
							this.ruleForm.storeupnum = obj[o];
							this.ro.storeupnum = true;
							continue;
						}
					}
				}

				// 获取用户信息
				this.$http({
					url: `${this.sessionTable}/session`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						var json = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
				this.$http({
					url: `option/jinengfenlei/jinengfenlei`,
					method: "get",
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ziliaoleixingOptions = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
			
			},
			// 多级联动参数

			async info(id) {
				await this.$http({
					url: `xuexiziliao/info/${id}`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
						//解决前台上传图片后台不显示的问题
						let reg=new RegExp('../../../upload','g')//g代表全部
						this.ruleForm.ziliaoxiangqing = this.ruleForm.ziliaoxiangqing.replace(reg,'../../../upload');
					} else {
						this.$message.error(data.msg);
					}
				});
			},

			// 提交
			async onSubmit() {
					if(this.ruleForm.ziliaofengmian!=null) {
						this.ruleForm.ziliaofengmian = this.ruleForm.ziliaofengmian.split(',').map(u => u.replace(/^\/+/, '')).join(',');
					}
					if(this.ruleForm.ziliaoshipin!=null) {
						this.ruleForm.ziliaoshipin = this.ruleForm.ziliaoshipin.split(',').map(u => u.replace(/^\/+/, '')).join(',');
					}
					if(this.ruleForm.ziliaofujian!=null) {
						this.ruleForm.ziliaofujian = this.ruleForm.ziliaofujian.split(',').map(u => u.replace(/^\/+/, '')).join(',');
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
								url: `xuexiziliao/${!this.ruleForm.id ? "save" : "update"}`,
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
											this.parent.xuexiziliaoCrossAddOrUpdateFlag = false;
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
				this.parent.xuexiziliaoCrossAddOrUpdateFlag = false;
			},
			ziliaofengmianUploadChange(fileUrls) {
				this.ruleForm.ziliaofengmian = fileUrls;
			},
			ziliaoshipinUploadChange(fileUrls) {
				this.ruleForm.ziliaoshipin = fileUrls;
			},
			ziliaofujianUploadChange(fileUrls) {
				this.ruleForm.ziliaofujian = fileUrls;
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

.modern-form-page {
	padding: 20px 24px 32px;
	width: 100%;
	background: $bg-page;
}

.form-section {
	background: $bg-card;
	border: 1px solid $border;
	border-radius: 14px;
	margin-bottom: 16px;
	overflow: hidden;
	backdrop-filter: blur(12px);
	box-shadow: 0 2px 16px rgba(0,0,0,0.2);
}

	.section-header {
	display: flex;
	align-items: center;
	gap: 10px;
	padding: 12px 20px;
	border-bottom: 1px solid $border;
	background: rgba(14,165,233,0.04);
	.el-icon {
		font-size: 16px;
		color: $accent;
	}
	span {
		font-size: 14px;
		font-weight: 600;
		color: $text;
		letter-spacing: 0.5px;
	}
}

.section-body {
	padding: 16px 20px;
}

.grid-2 {
	display: grid;
	grid-template-columns: 1fr 1fr;
	gap: 14px 28px;
}

.grid-3 {
	display: grid;
	grid-template-columns: repeat(3, 1fr);
	gap: 14px;
}

.field-label {
	display: block;
	font-size: 13px;
	font-weight: 500;
	color: $text-dim;
	margin-bottom: 8px;
	letter-spacing: 0.3px;
}

.field-readonly {
	padding: 10px 14px;
	font-size: 14px;
	color: $text;
	background: rgba(255,255,255,0.03);
	border-radius: $radius;
	border: 1px solid transparent;
	min-height: 42px;
	display: flex;
	align-items: center;
}

.price-val {
	color: #f59e0b;
	font-weight: 600;
	font-size: 18px;
}

.free-val {
	color: #22c55e;
	font-weight: 500;
}

.modern-form-page ::v-deep .el-form-item {
	margin-bottom: 0;
}
.modern-form-page ::v-deep .el-form-item__label {
	display: none;
}
.modern-form-page ::v-deep .el-form-item__content {
	margin-left: 0 !important;
}

.modern-form-page .el-input ::v-deep .el-input__inner,
.modern-form-page .el-input-number ::v-deep .el-input__inner {
	border: 1px solid $border;
	border-radius: $radius;
	padding: 0 14px;
	outline: none;
	color: $text;
	background: $bg-input;
	width: 100%;
	font-size: 14px;
	height: 42px;
	transition: border-color 0.25s, box-shadow 0.25s;
	&:focus {
		border-color: $border-focus;
		box-shadow: 0 0 0 3px rgba(14,165,233,0.1);
	}
	&::placeholder { color: rgba(148,163,184,0.6); }
}
.modern-form-page .el-input-number {
	width: 100%;
	text-align: left;
	::v-deep .el-input__inner { text-align: left; }
	::v-deep .el-input-number__decrease,
	::v-deep .el-input-number__increase { display: none; }
}

.modern-form-page .el-select ::v-deep .el-input__inner {
	border: 1px solid $border;
	border-radius: $radius;
	padding: 0 14px;
	color: $text;
	background: $bg-input;
	font-size: 14px;
	height: 42px;
	transition: border-color 0.25s;
	&:focus { border-color: $border-focus; }
}

.modern-form-page .el-date-editor {
	width: 100% !important;
	::v-deep .el-input__inner {
		border: 1px solid $border;
		border-radius: $radius;
		padding: 0 30px;
		color: $text;
		background: $bg-input;
		font-size: 14px;
		height: 42px;
		transition: border-color 0.25s;
		&:focus { border-color: $border-focus; }
	}
	::v-deep .el-input__prefix i { color: $accent; }
}

.media-card {
	background: rgba(255,255,255,0.02);
	border: 1px solid $border;
	border-radius: 10px;
	padding: 14px;
	display: flex;
	flex-direction: column;
	min-height: 110px;
	transition: border-color 0.3s;
	&:hover { border-color: rgba(14,165,233,0.25); }
}

.media-card-title {
	font-size: 13px;
	font-weight: 600;
	color: $text-dim;
	margin-bottom: 12px;
	.el-icon {
		margin-right: 6px;
		color: $accent;
		vertical-align: middle;
	}
}

.media-empty {
	color: rgba(148,163,184,0.5);
	font-size: 13px;
	text-align: center;
	padding: 24px 0;
}

.media-action-btn {
	border: 1px solid rgba(14,165,233,0.25) !important;
	border-radius: 8px !important;
	color: $accent !important;
	background: rgba(14,165,233,0.06) !important;
	font-size: 13px !important;
	padding: 10px 20px !important;
	cursor: pointer;
	transition: all 0.25s;
	&:hover {
		background: rgba(14,165,233,0.12) !important;
		border-color: $accent !important;
	}
}

.preview-imgs {
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

.modern-form-page ::v-deep .el-upload--picture-card {
	background: transparent;
	border: 0;
	border-radius: 0;
	width: auto;
	height: auto;
	line-height: initial;
}
.modern-form-page ::v-deep .el-upload .el-icon-plus {
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
	&:hover {
		border-color: $accent;
		background: rgba(14,165,233,0.08);
	}
}
.modern-form-page ::v-deep .el-upload-list .el-upload-list__item {
	border: 1px solid $border;
	border-radius: 8px;
	width: 72px;
	height: 72px;
}
.modern-form-page ::v-deep .el-upload__tip {
	color: $text-dim;
	font-size: 12px;
	padding-top: 4px;
}
.modern-form-page ::v-deep .el-upload-dragger {
	color: $text-dim;
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	justify-content: center;
	font-size: 14px;
	border: 1px dashed rgba(14,165,233,0.2);
	border-radius: $radius;
	background: rgba(14,165,233,0.02);
	width: 100%;
	height: 90px;
	transition: all 0.25s;
	&:hover {
		border-color: $accent;
		background: rgba(14,165,233,0.05);
	}
}
.modern-form-page ::v-deep .el-upload-dragger .el-icon-upload {
	margin: 0;
	color: $accent;
	width: 100%;
	font-size: 36px;
	opacity: 0.5;
}
.modern-form-page ::v-deep .el-upload-dragger .el-upload__text {
	color: $text-dim;
	text-align: center;
	width: 100%;
	font-size: 12px;
	line-height: 1;
	em {
		font-style: normal;
		color: $accent;
	}
}

.full-width {
	width: 100%;
}

.modern-form-page .el-textarea ::v-deep .el-textarea__inner {
	border: 1px solid $border;
	border-radius: $radius;
	padding: 14px 16px;
	color: $text;
	background: $bg-input;
	width: 100%;
	font-size: 14px;
	line-height: 1.7;
	font-family: inherit;
	resize: vertical;
	min-height: 100px;
	transition: border-color 0.25s, box-shadow 0.25s;
	&:focus {
		border-color: $border-focus;
		box-shadow: 0 0 0 3px rgba(14,165,233,0.1);
	}
	&::placeholder { color: rgba(148,163,184,0.5); }
}

.detail-preview {
	padding: 16px 20px;
	color: $text;
	background: rgba(255,255,255,0.03);
	border: 1px solid $border;
	border-radius: $radius;
	font-size: 14px;
	line-height: 1.8;
	width: 100%;
	min-height: 100px;
	word-break: break-word;
}

.form-actions {
	display: flex;
	justify-content: center;
	gap: 16px;
	padding: 16px 0 8px;
	position: sticky;
	bottom: 0;
	background: $bg-page;
	z-index: 10;
	border-top: 1px solid $border;
	margin: 0 -24px;
	padding-left: 24px;
	padding-right: 24px;
}

.action-submit {
	border: none !important;
	cursor: pointer;
	padding: 0 40px !important;
	color: #fff !important;
	font-weight: 600;
	font-size: 15px !important;
	border-radius: 24px !important;
	background: linear-gradient(135deg, $accent, $accent2) !important;
	min-width: 160px;
	height: 46px !important;
	box-shadow: 0 2px 12px rgba(14,165,233,0.25);
	transition: all 0.3s;
	.el-icon { margin-right: 6px; vertical-align: middle; }
	&:hover {
		box-shadow: 0 6px 20px rgba(14,165,233,0.35);
		transform: translateY(-2px);
	}
}

.action-cancel {
	border: 1px solid $border !important;
	cursor: pointer;
	padding: 0 40px !important;
	color: $text-dim !important;
	font-weight: 600;
	font-size: 15px !important;
	border-radius: 24px !important;
	background: transparent !important;
	min-width: 160px;
	height: 46px !important;
	transition: all 0.3s;
	.el-icon { margin-right: 6px; vertical-align: middle; }
	&:hover {
		border-color: $accent !important;
		color: $text !important;
		background: rgba(14,165,233,0.04) !important;
	}
}

@media (max-width: 900px) {
	.grid-2 { grid-template-columns: 1fr; }
	.grid-3 { grid-template-columns: 1fr; }
	.modern-form-page { padding: 16px; }
}
</style>
