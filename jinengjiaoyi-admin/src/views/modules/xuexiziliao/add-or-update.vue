<template>
	<div class="zl-form-page">
		<el-form ref="ruleForm" :model="ruleForm" :rules="rules" label-width="0">

			<div class="zl-form-card">
				<!-- 标题栏 -->
				<div class="card-top-bar">
					<div class="top-bar-left">
						<i class="el-icon-document-add"></i>
						<span>{{ruleForm.id ? '编辑学习资料' : '发布学习资料'}}</span>
					</div>
					<div class="top-bar-right" v-if="type!='info'">
						<el-button class="btn-submit" @click="onSubmit"><i class="el-icon-check"></i> 保存发布</el-button>
						<el-button class="btn-cancel" @click="back()"><i class="el-icon-close"></i> 取消</el-button>
					</div>
					<div class="top-bar-right" v-else>
						<el-button class="btn-cancel" @click="back()"><i class="el-icon-back"></i> 返回</el-button>
					</div>
				</div>

				<div class="card-scroll-body">
					<!-- 基本信息 -->
					<div class="form-block">
						<div class="block-label"><span class="bl-dot"></span>基本信息</div>
						<div class="row-2">
							<el-form-item prop="ziliaobiaoti" class="col-span-2">
								<label class="f-label">资料标题</label>
								<el-input v-if="type!='info'" v-model="ruleForm.ziliaobiaoti" placeholder="请输入资料标题" clearable :readonly="ro.ziliaobiaoti"></el-input>
								<div v-else class="f-readonly">{{ruleForm.ziliaobiaoti || '-'}}</div>
							</el-form-item>
						</div>
						<div class="row-3">
							<el-form-item prop="ziliaoleixing">
								<label class="f-label">技能分类</label>
								<el-select v-if="type!='info'" :disabled="ro.ziliaoleixing" v-model="ruleForm.ziliaoleixing" placeholder="请选择" filterable style="width:100%">
									<el-option v-for="(item,index) in ziliaoleixingOptions" :key="index" :label="item" :value="item"></el-option>
								</el-select>
								<div v-else class="f-readonly">{{ruleForm.ziliaoleixing || '-'}}</div>
							</el-form-item>
							<el-form-item prop="jiage">
								<label class="f-label">资料价格 (元)</label>
								<el-input-number v-if="type!='info'" v-model="ruleForm.jiage" :min="0" :precision="0" :disabled="ro.jiage" style="width:100%"></el-input-number>
								<div v-else class="f-readonly">
									<span v-if="ruleForm.jiage > 0" style="color:#f59e0b;font-weight:700">¥{{ruleForm.jiage}}</span>
									<span v-else style="color:#10b981;font-weight:600">免费</span>
								</div>
							</el-form-item>
							<el-form-item prop="fabushijian">
								<label class="f-label">发布时间</label>
								<el-date-picker v-if="type!='info'" format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="ruleForm.fabushijian" type="date" :readonly="ro.fabushijian" placeholder="选择日期" style="width:100%"></el-date-picker>
								<div v-else class="f-readonly">{{ruleForm.fabushijian || '-'}}</div>
							</el-form-item>
						</div>
					</div>

					<!-- 媒体文件 -->
					<div class="form-block">
						<div class="block-label"><span class="bl-dot dot-purple"></span>媒体资源</div>
						<div class="media-row">
							<div class="media-cell">
								<div class="mc-head"><i class="el-icon-picture-outline"></i> 封面图片</div>
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
									<div v-else-if="ruleForm.ziliaofengmian" class="thumb-list">
										<img v-if="ruleForm.ziliaofengmian.substring(0,4)=='http'" :src="ruleForm.ziliaofengmian.split(',')[0]" @click="imgPreView(ruleForm.ziliaofengmian.split(',')[0])">
										<img v-else v-for="(item,index) in ruleForm.ziliaofengmian.split(',')" :key="index" :src="$base.url+item" @click="imgPreView($base.url+item)">
									</div>
									<span v-else class="mc-empty">暂无</span>
								</el-form-item>
							</div>
							<div class="media-cell">
								<div class="mc-head"><i class="el-icon-video-camera"></i> 视频资源</div>
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
									<el-button v-else-if="ruleForm.ziliaoshipin" size="mini" class="mc-link" @click="download($base.url+ruleForm.ziliaoshipin)"><i class="el-icon-video-play"></i> 预览</el-button>
									<span v-else class="mc-empty">暂无</span>
								</el-form-item>
							</div>
							<div class="media-cell">
								<div class="mc-head"><i class="el-icon-folder-opened"></i> 附件文件 <span class="mc-req">必填</span></div>
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
									<el-button v-else-if="ruleForm.ziliaofujian" size="mini" class="mc-link" @click="download($base.url+ruleForm.ziliaofujian)"><i class="el-icon-download"></i> 下载</el-button>
									<span v-else class="mc-empty">暂无</span>
								</el-form-item>
							</div>
						</div>
					</div>

					<!-- 资料详情 -->
					<div class="form-block last">
						<div class="block-label"><span class="bl-dot dot-green"></span>资料详情</div>
						<el-form-item prop="ziliaoxiangqing">
							<el-input v-if="type!='info'" type="textarea" v-model="ruleForm.ziliaoxiangqing" :rows="5" placeholder="请输入资料详细描述..." :readonly="ro.ziliaoxiangqing" resize="vertical"></el-input>
							<div v-else-if="ruleForm.ziliaoxiangqing" class="detail-html" v-html="ruleForm.ziliaoxiangqing"></div>
							<span v-else class="mc-empty">暂无详情</span>
						</el-form-item>
					</div>
				</div>
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
					ziliaobiaoti: [],
					ziliaoleixing: [],
					ziliaofengmian: [],
					ziliaoshipin: [],
					ziliaofujian: [
						{ required: true, message: '资料附件不能为空', trigger: 'blur' },
					],
					ziliaoxiangqing: [],
					fabushijian: [],
					clicktime: [],
					clicknum: [{ validator: validateIntNumber, trigger: 'blur' }],
					discussnum: [{ validator: validateIntNumber, trigger: 'blur' }],
					storeupnum: [{ validator: validateIntNumber, trigger: 'blur' }],
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
			download(file){
				window.open(`${file}`)
			},
			init(id,type) {
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
						if(o=='ziliaobiaoti'){ this.ruleForm.ziliaobiaoti = obj[o]; this.ro.ziliaobiaoti = true; continue; }
						if(o=='ziliaoleixing'){ this.ruleForm.ziliaoleixing = obj[o]; this.ro.ziliaoleixing = true; continue; }
						if(o=='ziliaofengmian'){ this.ruleForm.ziliaofengmian = obj[o]; this.ro.ziliaofengmian = true; continue; }
						if(o=='ziliaoshipin'){ this.ruleForm.ziliaoshipin = obj[o]; this.ro.ziliaoshipin = true; continue; }
						if(o=='ziliaofujian'){ this.ruleForm.ziliaofujian = obj[o]; this.ro.ziliaofujian = true; continue; }
						if(o=='ziliaoxiangqing'){ this.ruleForm.ziliaoxiangqing = obj[o]; this.ro.ziliaoxiangqing = true; continue; }
						if(o=='fabushijian'){ this.ruleForm.fabushijian = obj[o]; this.ro.fabushijian = true; continue; }
						if(o=='clicktime'){ this.ruleForm.clicktime = obj[o]; this.ro.clicktime = true; continue; }
						if(o=='clicknum'){ this.ruleForm.clicknum = obj[o]; this.ro.clicknum = true; continue; }
						if(o=='discussnum'){ this.ruleForm.discussnum = obj[o]; this.ro.discussnum = true; continue; }
						if(o=='storeupnum'){ this.ruleForm.storeupnum = obj[o]; this.ro.storeupnum = true; continue; }
					}
				}
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
			async info(id) {
				await this.$http({
					url: `xuexiziliao/info/${id}`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
						let reg=new RegExp('../../../upload','g')
						this.ruleForm.ziliaoxiangqing = this.ruleForm.ziliaoxiangqing.replace(reg,'../../../upload');
					} else {
						this.$message.error(data.msg);
					}
				});
			},
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
			getUUID () {
				return new Date().getTime();
			},
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
$cyan: #06b6d4;
$blue: #3b82f6;
$purple: #8b5cf6;
$green: #10b981;
$amber: #f59e0b;
$rose: #f43f5e;
$card: #1e293b;
$input-bg: rgba(255,255,255,0.04);
$border: rgba(255,255,255,0.06);
$text: #f1f5f9;
$text2: #94a3b8;
$text3: #64748b;

.zl-form-page {
	width: 100%;
	height: 100%;
	display: flex;
	flex-direction: column;
}

.zl-form-card {
	display: flex;
	flex-direction: column;
	height: 100%;
	background: $card;
	border: 1px solid $border;
	border-radius: 14px;
	overflow: hidden;
}

/* ===== 顶部操作栏：固定在卡片顶部 ===== */
.card-top-bar {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 14px 22px;
	background: linear-gradient(135deg, rgba(6,182,212,0.06), rgba(59,130,246,0.04));
	border-bottom: 1px solid $border;
	flex-shrink: 0;
}
.top-bar-left {
	display: flex; align-items: center; gap: 10px;
	font-size: 15px; font-weight: 700; color: $text;
	i { font-size: 18px; color: $cyan; }
}
.top-bar-right { display: flex; gap: 8px; }

.btn-submit {
	border: none !important; padding: 0 22px !important; height: 34px !important;
	font-size: 13px !important; font-weight: 600; color: #fff !important;
	border-radius: 8px !important;
	background: linear-gradient(135deg, $cyan, $blue) !important;
	box-shadow: 0 2px 8px rgba(6,182,212,0.25);
	transition: all .25s;
	i { margin-right: 4px; }
	&:hover { box-shadow: 0 4px 14px rgba(6,182,212,0.35); transform: translateY(-1px); }
}
.btn-cancel {
	border: 1px solid $border !important; padding: 0 18px !important; height: 34px !important;
	font-size: 13px !important; font-weight: 600; color: $text2 !important;
	border-radius: 8px !important; background: transparent !important;
	transition: all .25s;
	i { margin-right: 4px; }
	&:hover { border-color: rgba(6,182,212,0.25) !important; color: $text !important; }
}

/* ===== 可滚动表单区域 ===== */
.card-scroll-body {
	flex: 1;
	overflow-y: auto;
	padding: 18px 22px 22px;
	&::-webkit-scrollbar { width: 5px; }
	&::-webkit-scrollbar-thumb { background: rgba(255,255,255,0.08); border-radius: 4px; }
}

/* ===== 表单块 ===== */
.form-block {
	margin-bottom: 20px;
	&.last { margin-bottom: 0; }
}
.block-label {
	display: flex; align-items: center; gap: 8px;
	font-size: 13px; font-weight: 700; color: $text; margin-bottom: 14px;
}
.bl-dot {
	width: 6px; height: 6px; border-radius: 2px; background: $cyan;
	&.dot-purple { background: $purple; }
	&.dot-green { background: $green; }
}

/* ===== 栅格 ===== */
.row-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; margin-bottom: 14px; }
.row-3 { display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 14px; }
.col-span-2 { grid-column: span 2; }

.f-label {
	display: block; font-size: 12px; font-weight: 600; color: $text3; margin-bottom: 6px;
}
.f-readonly {
	padding: 8px 12px; font-size: 13px; color: $text;
	background: $input-bg; border-radius: 8px; border: 1px solid $border;
	min-height: 36px; display: flex; align-items: center;
}

/* ===== 媒体三列 ===== */
.media-row {
	display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 12px;
}
.media-cell {
	background: rgba(255,255,255,0.015); border: 1px solid $border;
	border-radius: 10px; padding: 12px; min-height: 90px;
	transition: border-color .2s;
	&:hover { border-color: rgba(6,182,212,0.15); }
}
.mc-head {
	font-size: 11px; font-weight: 700; color: $text2; margin-bottom: 8px;
	letter-spacing: 0.3px;
	i { color: $cyan; margin-right: 4px; font-size: 13px; }
}
.mc-req {
	font-size: 10px; color: $rose; font-weight: 600;
	background: rgba(244,63,94,0.08); padding: 1px 6px;
	border-radius: 8px; margin-left: 4px; border: 1px solid rgba(244,63,94,0.12);
}
.mc-empty { font-size: 12px; color: $text3; }
.mc-link {
	border: 1px solid rgba(6,182,212,0.2) !important; border-radius: 6px !important;
	color: $cyan !important; background: rgba(6,182,212,0.04) !important;
	font-size: 12px !important; transition: all .2s;
	i { margin-right: 3px; }
	&:hover { background: rgba(6,182,212,0.1) !important; }
}
.thumb-list {
	display: flex; gap: 6px; flex-wrap: wrap;
	img {
		width: 54px; height: 54px; object-fit: cover; border-radius: 6px;
		border: 1px solid $border; cursor: pointer; transition: transform .2s;
		&:hover { transform: scale(1.06); }
	}
}

/* ===== 详情 ===== */
.detail-html {
	padding: 12px 14px; color: $text; background: $input-bg;
	border: 1px solid $border; border-radius: 8px;
	font-size: 13px; line-height: 1.8; min-height: 60px; word-break: break-word;
}

/* ===== Element UI 覆盖 ===== */
.zl-form-page ::v-deep .el-form-item { margin-bottom: 0; }
.zl-form-page ::v-deep .el-form-item__label { display: none; }
.zl-form-page ::v-deep .el-form-item__content { margin-left: 0 !important; }

.zl-form-page .el-input ::v-deep .el-input__inner,
.zl-form-page .el-input-number ::v-deep .el-input__inner {
	border: 1px solid $border; border-radius: 8px; padding: 0 12px;
	color: $text; background: $input-bg; font-size: 13px; height: 36px;
	transition: border-color .2s, box-shadow .2s;
	&:focus { border-color: $cyan; box-shadow: 0 0 0 2px rgba(6,182,212,0.08); }
	&::placeholder { color: rgba(148,163,184,0.4); }
}
.zl-form-page .el-input-number {
	width: 100%;
	::v-deep .el-input__inner { text-align: left; }
	::v-deep .el-input-number__decrease, ::v-deep .el-input-number__increase { display: none; }
}
.zl-form-page .el-select ::v-deep .el-input__inner {
	border: 1px solid $border; border-radius: 8px; padding: 0 12px;
	color: $text; background: $input-bg; font-size: 13px; height: 36px;
	&:focus { border-color: $cyan; }
}
.zl-form-page .el-date-editor {
	width: 100% !important;
	::v-deep .el-input__inner {
		border: 1px solid $border; border-radius: 8px; padding: 0 12px 0 30px;
		color: $text; background: $input-bg; font-size: 13px; height: 36px;
		&:focus { border-color: $cyan; }
	}
	::v-deep .el-input__prefix i { color: $cyan; }
}
.zl-form-page .el-textarea ::v-deep .el-textarea__inner {
	border: 1px solid $border; border-radius: 8px; padding: 10px 12px;
	color: $text; background: $input-bg; font-size: 13px; line-height: 1.7;
	font-family: inherit; resize: vertical;
	transition: border-color .2s, box-shadow .2s;
	&:focus { border-color: $cyan; box-shadow: 0 0 0 2px rgba(6,182,212,0.08); }
	&::placeholder { color: rgba(148,163,184,0.4); }
}
.zl-form-page ::v-deep .el-form-item.is-error .el-input__inner,
.zl-form-page ::v-deep .el-form-item.is-error .el-textarea__inner {
	border-color: $rose !important;
}
.zl-form-page ::v-deep .el-form-item__error { font-size: 11px; padding-top: 3px; }

.zl-form-page ::v-deep .el-upload--picture-card { background: transparent; border: 0; border-radius: 0; width: auto; height: auto; line-height: initial; }
.zl-form-page ::v-deep .el-upload .el-icon-plus {
	border: 1px dashed rgba(6,182,212,0.25); border-radius: 8px; color: $cyan;
	font-size: 18px; width: 54px; height: 54px; line-height: 54px; text-align: center;
	background: rgba(6,182,212,0.03); transition: all .2s;
	&:hover { border-color: $cyan; background: rgba(6,182,212,0.08); }
}
.zl-form-page ::v-deep .el-upload-list .el-upload-list__item { border: 1px solid $border; border-radius: 6px; width: 54px; height: 54px; }
.zl-form-page ::v-deep .el-upload__tip { color: $text3; font-size: 11px; padding-top: 2px; }
.zl-form-page ::v-deep .el-upload-dragger {
	color: $text2; display: flex; flex-wrap: wrap; align-items: center; justify-content: center;
	border: 1px dashed rgba(6,182,212,0.18); border-radius: 8px; background: transparent;
	width: 100%; height: 56px; transition: all .2s;
	&:hover { border-color: $cyan; background: rgba(6,182,212,0.04); }
}
.zl-form-page ::v-deep .el-upload-dragger .el-icon-upload { margin: 0; color: $cyan; width: 100%; font-size: 22px; opacity: 0.5; }
.zl-form-page ::v-deep .el-upload-dragger .el-upload__text { color: $text3; text-align: center; width: 100%; font-size: 11px; line-height: 1; em { font-style: normal; color: $cyan; } }

@media (max-width: 800px) {
	.row-3 { grid-template-columns: 1fr; }
	.media-row { grid-template-columns: 1fr; }
}
</style>
