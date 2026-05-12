
































<template>
	<div class="add-update-preview">
		<el-form
			class="add-update-form"
			ref="ruleForm"
			:model="ruleForm"
			:rules="rules"
			label-width="200px"
			>
			<el-form-item class="add-item" label="用户账号" prop="yonghuzhanghao">
				<el-input v-model="ruleForm.yonghuzhanghao" 
					placeholder="用户账号" clearable :readonly="ro.yonghuzhanghao"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="用户姓名" prop="yonghuxingming">
				<el-input v-model="ruleForm.yonghuxingming" 
					placeholder="用户姓名" clearable :readonly="ro.yonghuxingming"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="手机号" prop="shoujihao">
				<el-input v-model="ruleForm.shoujihao" 
					placeholder="手机号" clearable :readonly="ro.shoujihao"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="标题" prop="biaoti">
				<el-input v-model="ruleForm.biaoti" 
					placeholder="标题" clearable :readonly="ro.biaoti"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="技能分类" prop="jinengfenlei">
				<el-input v-model="ruleForm.jinengfenlei" 
					placeholder="技能分类" clearable :readonly="ro.jinengfenlei"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="供给图片" v-if="type!='cross' || (type=='cross' && !ro.gongjitupian)" prop="gongjitupian">
				<file-upload
					tip="点击上传供给图片"
					action="file/upload"
					:limit="3"
					:multiple="true"
					:disabled="ro.gongjitupian"
					:fileUrls="ruleForm.gongjitupian?ruleForm.gongjitupian:''"
					@change="gongjitupianUploadChange"
					></file-upload>
			</el-form-item>
			<el-form-item class="add-item" v-else label="供给图片" prop="gongjitupian">
				<img v-if="ruleForm.gongjitupian.substring(0,4)=='http'" class="upload-img" v-bind:key="index" :src="ruleForm.gongjitupian.split(',')[0]">
				<img v-else class="upload-img" v-bind:key="index" v-for="(item,index) in ruleForm.gongjitupian.split(',')" :src="baseUrl+item">
			</el-form-item>
			<el-form-item class="add-item"  label="交换技能" prop="jiaohuanjineng">
				<el-select v-model="ruleForm.jiaohuanjineng" placeholder="请选择交换技能" :disabled="ro.jiaohuanjineng"  filterable>
					<el-option
						v-for="(item,index) in jiaohuanjinengOptions"
						:key="index"
						:label="item"
						:value="item">
					</el-option>
				</el-select>
			</el-form-item>
			<el-form-item class="add-item" label="供给视频" v-if="type!='cross' || (type=='cross' && !ro.gongjishipin)" prop="gongjishipin">
				<file-upload
					tip="点击上传供给视频"
					action="file/upload"
					:limit="1"
					:type="2"
					:multiple="true"
					:disabled="ro.gongjishipin"
					:fileUrls="ruleForm.gongjishipin?ruleForm.gongjishipin:''"
					@change="gongjishipinUploadChange"
					></file-upload>
			</el-form-item>
			<el-form-item class="add-item" v-else label="供给视频" prop="gongjishipin">
				<div class="viewBtn" v-if="ruleForm.gongjishipin" @click="download(baseUrl + ruleForm.gongjishipin )" type="success">预览</div>
				<div class="unviewBtn" v-else disabled>暂无</div>
			</el-form-item>
			<el-form-item class="add-item" label="供给附件" prop="gongjifujian">
				<file-upload
					tip="点击上传供给附件"
					action="file/upload"
					:limit="1"
					:type="3"
					:multiple="true"
					:disabled="ro.gongjifujian"
					:fileUrls="ruleForm.gongjifujian?ruleForm.gongjifujian:''"
					@change="gongjifujianUploadChange"
					></file-upload>
			</el-form-item>
			<el-form-item class="add-item" label="交换时间" prop="jiaohuanshijian">
				<el-date-picker
					:disabled="ro.jiaohuanshijian"
					format="yyyy 年 MM 月 dd 日"
					value-format="yyyy-MM-dd"
					v-model="ruleForm.jiaohuanshijian" 
					type="date"
					placeholder="交换时间">
				</el-date-picker> 
			</el-form-item>
			<el-form-item class="add-item" label="供给人账号" prop="gongjirenzhanghao">
				<el-input v-model="ruleForm.gongjirenzhanghao" 
					placeholder="供给人账号" clearable :readonly="ro.gongjirenzhanghao"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="供给人姓名" prop="gongjirenxingming">
				<el-input v-model="ruleForm.gongjirenxingming" 
					placeholder="供给人姓名" clearable :readonly="ro.gongjirenxingming"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="联系方式" prop="lianxifangshi">
				<el-input v-model="ruleForm.lianxifangshi" 
					placeholder="联系方式" clearable :readonly="ro.lianxifangshi"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="供给内容" prop="gongjineirong">
				<el-input
					type="textarea"
					:rows="8"
					:disabled="ro.gongjineirong"
					placeholder="供给内容"
					v-model="ruleForm.gongjineirong">
					</el-input>
			</el-form-item>

			<el-form-item class="add-btn-item">
				<el-button class="submitBtn"  type="primary" @click="onSubmit(null)">
					<span class="icon iconfont icon-xiugai17"></span>
					<span class="text">提交信息</span>
				</el-button>
				<el-button class="closeBtn" @click="back()">
					<span class="icon iconfont icon-shanchu8"></span>
					<span class="text">取消</span>
				</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
				baseUrl: '',
				ro:{
					yonghuzhanghao : false,
					yonghuxingming : false,
					shoujihao : false,
					biaoti : false,
					jinengfenlei : false,
					gongjitupian : false,
					jiaohuanjineng : false,
					gongjishipin : false,
					gongjifujian : false,
					jiaohuanshijian : false,
					gongjineirong : false,
					gongjirenzhanghao : false,
					gongjirenxingming : false,
					lianxifangshi : false,
					crossuserid : false,
					crossrefid : false,
				},
				type: '',
				userTableName: localStorage.getItem('UserTableName'),
				ruleForm: {
					yonghuzhanghao: '',
					yonghuxingming: '',
					shoujihao: '',
					biaoti: '',
					jinengfenlei: '',
					gongjitupian: '',
					jiaohuanjineng: '',
					gongjishipin: '',
					gongjifujian: '',
					jiaohuanshijian: '',
					gongjineirong: '',
					gongjirenzhanghao: '',
					gongjirenxingming: '',
					lianxifangshi: '',
					crossuserid: '',
					crossrefid: '',
				},
				jiaohuanjinengOptions: [],

				rules: {
					yonghuzhanghao: [
					],
					yonghuxingming: [
					],
					shoujihao: [
					],
					biaoti: [
					],
					jinengfenlei: [
					],
					gongjitupian: [
					],
					jiaohuanjineng: [
					],
					gongjishipin: [
					],
					gongjifujian: [
					],
					jiaohuanshijian: [
					],
					gongjineirong: [
					],
					gongjirenzhanghao: [
					],
					gongjirenxingming: [
					],
					lianxifangshi: [
					],
					crossuserid: [
					],
					crossrefid: [
					],
				},
				centerType: false,
			};
		},
		computed: {
			sessionForm() {
				return JSON.parse(localStorage.getItem('sessionForm'))
			},



		},
		components: {
		},
		created() {
			if(this.$route.query.centerType){
				this.centerType = true
			}
			//this.bg();
			let type = this.$route.query.type ? this.$route.query.type : '';
			this.init(type);
			this.baseUrl = this.$config.baseUrl;
			this.ruleForm.jiaohuanshijian = this.getCurDate()
		},
		methods: {
			getMakeZero(s) {
				return s < 10 ? '0' + s : s;
			},
			// 下载
			download(file ){
				window.open(`${file}`)
			},
			// 初始化
			init(type) {
				this.type = type;
				if(type=='cross'){
					var obj = JSON.parse(localStorage.getItem('crossObj'));
					for (var o in obj){
						if(o=='yonghuzhanghao'){
							this.ruleForm.yonghuzhanghao = obj[o];
							this.ro.yonghuzhanghao = true;
							continue;
						}
						if(o=='yonghuxingming'){
							this.ruleForm.yonghuxingming = obj[o];
							this.ro.yonghuxingming = true;
							continue;
						}
						if(o=='shoujihao'){
							this.ruleForm.shoujihao = obj[o];
							this.ro.shoujihao = true;
							continue;
						}
						if(o=='biaoti'){
							this.ruleForm.biaoti = obj[o];
							this.ro.biaoti = true;
							continue;
						}
						if(o=='jinengfenlei'){
							this.ruleForm.jinengfenlei = obj[o];
							this.ro.jinengfenlei = true;
							continue;
						}
						if(o=='gongjitupian'){
							this.ruleForm.gongjitupian = obj[o]?obj[o].split(",")[0]:'';
							this.ro.gongjitupian = true;
							continue;
						}
						if(o=='jiaohuanjineng'){
							this.ruleForm.jiaohuanjineng = obj[o];
							this.ro.jiaohuanjineng = true;
							continue;
						}
						if(o=='gongjishipin'){
							this.ruleForm.gongjishipin = obj[o];
							this.ro.gongjishipin = true;
							continue;
						}
						if(o=='gongjifujian'){
							this.ruleForm.gongjifujian = obj[o];
							this.ro.gongjifujian = true;
							continue;
						}
						if(o=='jiaohuanshijian'){
							this.ruleForm.jiaohuanshijian = obj[o];
							this.ro.jiaohuanshijian = true;
							continue;
						}
						if(o=='gongjineirong'){
							this.ruleForm.gongjineirong = obj[o];
							this.ro.gongjineirong = true;
							continue;
						}
						if(o=='gongjirenzhanghao'){
							this.ruleForm.gongjirenzhanghao = obj[o];
							this.ro.gongjirenzhanghao = true;
							continue;
						}
						if(o=='gongjirenxingming'){
							this.ruleForm.gongjirenxingming = obj[o];
							this.ro.gongjirenxingming = true;
							continue;
						}
						if(o=='lianxifangshi'){
							this.ruleForm.lianxifangshi = obj[o];
							this.ro.lianxifangshi = true;
							continue;
						}
						if(o=='crossuserid'){
							this.ruleForm.crossuserid = obj[o];
							this.ro.crossuserid = true;
							continue;
						}
						if(o=='crossrefid'){
							this.ruleForm.crossrefid = obj[o];
							this.ro.crossrefid = true;
							continue;
						}
					}
				}else if(type=='edit'){
					this.info()
				}
				// 获取用户信息
				this.$http.get(this.userTableName + '/session', {emulateJSON: true}).then(res => {
					if (res.data.code == 0) {
						var json = res.data.data;
						if((json.yonghuzhanghao!=''&&json.yonghuzhanghao) || json.yonghuzhanghao==0){
							this.ruleForm.gongjirenzhanghao = json.yonghuzhanghao;
							this.ro.gongjirenzhanghao = true;
						}
						if((json.yonghuxingming!=''&&json.yonghuxingming) || json.yonghuxingming==0){
							this.ruleForm.gongjirenxingming = json.yonghuxingming;
							this.ro.gongjirenxingming = true;
						}
						if((json.shoujihao!=''&&json.shoujihao) || json.shoujihao==0){
							this.ruleForm.lianxifangshi = json.shoujihao;
							this.ro.lianxifangshi = true;
						}
					}
				});
				this.$http.get('option/jinengfenlei/jinengfenlei', {}).then(res => {
					if (res.data.code == 0) {
						this.jiaohuanjinengOptions = res.data.data;
					}
				});

				if (localStorage.getItem('raffleType') && localStorage.getItem('raffleType') != null) {
					localStorage.removeItem('raffleType')
					setTimeout(() => {
						this.onSubmit(null)
					}, 300)
				}
			},

			// 多级联动参数
			// 多级联动参数
			async info() {
				await this.$http.get(`jiaohuanjilu/detail/${this.$route.query.id}`, {emulateJSON: true}).then(res => {
					if (res.data.code == 0) {
						this.ruleForm = res.data.data;
					}
				});
			},
			// 提交
			async onSubmit(subMitType=null) {
				//更新跨表属性
				var crossuserid;
				var crossrefid;
				var crossoptnum;
				var finishNum = 0;
				if(!this.ruleForm.id) {
					delete this.ruleForm.userid
				}
				await this.$refs["ruleForm"].validate(async valid => {
					if(valid) {
						if(this.type=='cross'){
							var statusColumnName = localStorage.getItem('statusColumnName');
							var statusColumnValue = localStorage.getItem('statusColumnValue');
							if(statusColumnName && statusColumnName!='') {
								var obj = JSON.parse(localStorage.getItem('crossObj'));
								if(!statusColumnName.startsWith("[")) {
									for (var o in obj){
										if(o==statusColumnName){
											obj[o] = statusColumnValue;
										}
									}
									var table = localStorage.getItem('crossTable');
									await this.$http.post(table+'/update', obj).then(res => {});
								}
								else {
									crossuserid=Number(localStorage.getItem('frontUserid'));
									crossrefid=obj['id'];
									crossoptnum=localStorage.getItem('statusColumnName');
									crossoptnum=crossoptnum.replace(/\[/,"").replace(/\]/,"");
								}
							}
							if(crossrefid && crossuserid) {
								this.ruleForm.crossuserid=crossuserid;
								this.ruleForm.crossrefid=crossrefid;
								var params = {
									page: 1,
									limit: 10,
									crossuserid:crossuserid,
									crossrefid:crossrefid,
								}
								await this.$http.get('jiaohuanjilu/list', {
									params: params
								}).then(res => {
									finishNum = res.data.data.total
								});
							}
							if(finishNum>=crossoptnum) {
								this.$message({
									message: localStorage.getItem('tips'),
									type: 'error',
									duration: 1500,
								});
								return false;
							}
						}

						await this.$http.post(`jiaohuanjilu/${this.ruleForm.id?'update':this.centerType?'save':'add'}`, this.ruleForm).then(async res => {
							if (res.data.code == 0) {
								await this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.$router.go(-1);
										
									}
								});
							} else {
								this.$message({
									message: res.data.msg,
									type: 'error',
									duration: 1500
								});
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
				this.$router.go(-1);
			},
			gongjitupianUploadChange(fileUrls) {
				this.ruleForm.gongjitupian = fileUrls.replace(/^\/+/, "");
			},
			gongjishipinUploadChange(fileUrls) {
				this.ruleForm.gongjishipin = fileUrls.replace(/^\/+/, "");
			},
			gongjifujianUploadChange(fileUrls) {
				this.ruleForm.gongjifujian = fileUrls.replace(/^\/+/, "");
			},
		}
	};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
	.add-update-preview {
		padding: 20px 10% 40px;
		margin: 10px auto;
		background: none;
		width: 100%;
		position: relative;
		.add-update-form {
			border: 0px solid #eee;
			border-radius: 10px;
			padding: 40px 20% 20px 10%;
			background: none;
			width: 100%;
			position: relative;
			.add-item.el-form-item {
				border: 1px solid #707070;
				padding: 0;
				margin: 0 0 24px;
				background: none;
				::v-deep  .el-form-item__label {
					padding: 0 10px 0 0;
					color: #333;
					white-space: nowrap;
					font-weight: 500;
					width: 200px;
					font-size: 16px;
					line-height: 40px;
					text-align: right;
				}
				::v-deep  .el-form-item__content {
					margin-left: 200px;
				}
				.el-input {
					width: 100%;
				}
				.el-input ::v-deep  .el-input__inner {
					border: 0 solid #000;
					border-radius: 0px;
					padding: 0 12px;
					box-shadow: none;
					outline: none;
					color: #666;
					width: 100%;
					font-size: 16px;
					height: 40px;
				}
				.el-input ::v-deep  .el-input__inner[readonly="readonly"] {
					border: 0;
					cursor: not-allowed;
					border-radius: 4px;
					padding: 0 12px;
					box-shadow: none;
					outline: none;
					color: #999;
					width: 100%;
					font-size: 16px;
					height: 40px;
				}
				.el-input-number ::v-deep  .el-input__inner {
					text-align: left;
					border: 0 solid #000;
					border-radius: 0px;
					padding: 0 12px;
					box-shadow: none;
					outline: none;
					color: #666;
					width: 100%;
					font-size: 16px;
					height: 40px;
				}
				.el-input-number ::v-deep  .is-disabled .el-input__inner {
					text-align: left;
					border: 0;
					cursor: not-allowed;
					border-radius: 4px;
					padding: 0 12px;
					box-shadow: none;
					outline: none;
					color: #999;
					width: 100%;
					font-size: 16px;
					height: 40px;
				}
				.el-input-number ::v-deep  .el-input-number__decrease {
					display: none;
				}
				.el-input-number ::v-deep  .el-input-number__increase {
					display: none;
				}
				.el-select {
					width: 100%;
				}
				.el-select ::v-deep  .el-input__inner {
					border: 0 solid #000;
					border-radius: 0px;
					padding: 0 10px;
					box-shadow: none;
					outline: none;
					color: rgba(64, 158, 255, 1);
					width: 100%;
					font-size: 14px;
					height: 40px;
				}
				.el-select ::v-deep  .is-disabled .el-input__inner {
					border: 0;
					cursor: not-allowed;
					border-radius: 4px;
					padding: 0 10px;
					box-shadow: none;
					outline: none;
					color: #999;
					background: #eee;
					width: 100%;
					font-size: 14px;
					height: 40px;
				}
				.el-date-editor {
					width: 100%;
				}
				.el-date-editor ::v-deep  .el-input__inner {
					border: 0 solid #000;
					border-radius: 0px;
					padding: 0 10px 0 30px;
					box-shadow: none;
					outline: none;
					color: #666;
					width: 100%;
					font-size: 16px;
					height: 40px;
				}
				.el-date-editor ::v-deep  .el-input__inner[readonly="readonly"] {
					border: 0;
					cursor: not-allowed;
					border-radius: 4px;
					padding: 0 10px 0 30px;
					box-shadow: none;
					outline: none;
					color: #999;
					background: #eee;
					width: 100%;
					font-size: 16px;
					height: 40px;
				}
				::v-deep  .el-upload--picture-card {
					background: transparent;
					border: 0;
					border-radius: 0;
					width: auto;
					height: auto;
					line-height: initial;
					vertical-align: middle;
				}
				::v-deep  .upload .upload-img {
					border: 1px solid #eee;
					cursor: pointer;
					border-radius: 0px;
					color: #999;
					width: 80px;
					font-size: 26px;
					line-height: 80px;
					text-align: center;
					height: 80px;
				}
				::v-deep  .el-upload-list .el-upload-list__item {
					border: 1px solid #eee;
					cursor: pointer;
					border-radius: 0px;
					color: #999;
					width: 80px;
					font-size: 26px;
					line-height: 80px;
					text-align: center;
					height: 80px;
					font-size: 14px;
					line-height: 1.8;
				}
				::v-deep  .el-upload .el-icon-plus {
					border: 1px solid #eee;
					cursor: pointer;
					border-radius: 0px;
					color: #999;
					width: 80px;
					font-size: 26px;
					line-height: 80px;
					text-align: center;
					height: 80px;
				}
				::v-deep  .el-upload__tip {
					color: #666;
					font-size: 16px;
				}
				.el-textarea ::v-deep  .el-textarea__inner {
					border: 0 solid #000;
					border-radius: 0px;
					padding: 12px;
					box-shadow: none;
					outline: none;
					color: #666;
					width: 100%;
					font-size: 16px;
					height: auto;
				}
				.el-textarea ::v-deep  .el-textarea__inner[readonly="readonly"] {
					border: 0;
					cursor: not-allowed;
					border-radius: 4px;
					padding: 12px;
					box-shadow: none;
					outline: none;
					color: #999;
					width: 100%;
					font-size: 16px;
					height: auto;
				}
				::v-deep  .el-input__inner::placeholder {
					color: #123;
					font-size: 16px;
				}
				::v-deep  textarea::placeholder {
					color: #123;
					font-size: 16px;
				}
				.editor {
					background-color: #fff;
					border-radius: 0;
					padding: 0;
					box-shadow: none;
					margin: 0;
					width: 100%;
					border-color: #ccc;
					border-width: 0;
					border-style: solid;
					height: auto;
				}
				.editor ::v-deep .ql-toolbar {
					border: 1px solid #eee;
					background: none;
					border-width: 1px 1px 0;
				}
				.editor ::v-deep .ql-container {
					border: 1px solid #eee;
					background: none;
					min-height: 180px;
				}
				.editor ::v-deep .ql-container .ql-blank::before {
					color: #999;
				}
				.upload-img {
					object-fit: cover;
					width: 120px;
					height: 120px;
				}
				.viewBtn {
					border: 0;
					cursor: pointer;
					padding: 0 20px;
					margin: 0;
					color: #fff;
					display: inline-block;
					font-size: 14px;
					line-height: 34px;
					border-radius: 4px;
					outline: none;
					background: #0ea5e9;
					width: auto;
					height: 34px;
				}
				.viewBtn:hover {
					opacity: 0.7;
				}
				.unviewBtn {
					border: 0;
					cursor: pointer;
					padding: 0 20px;
					margin: 0;
					color: #666;
					display: inline-block;
					font-size: 14px;
					line-height: 34px;
					border-radius: 4px;
					outline: none;
					background: #ddd;
					width: auto;
					height: 34px;
				}
				.unviewBtn:hover {
					opacity: 0.8;
				}
			}
			.add-btn-item {
				padding: 0;
				margin: 20px auto;
				width: 100%;
				text-align: center;
				.submitBtn {
					border: 1px solid #0ea5e9;
					cursor: pointer;
					border-radius: 20px;
					padding: 0 15px;
					margin: 0 20px 0 0;
					outline: none;
					background: #0ea5e9;
					display: inline-block;
					width: auto;
					font-size: 14px;
					line-height: 40px;
					height: 40px;
					.icon {
						color: #fff;
					}
					.text {
						color: #fff;
						font-size: 16px;
					}
				}
				.submitBtn:hover {
					opacity: 0.7;
					.icon {
						color: #000;
					}
					.text {
						color: #000;
					}
				}
				.closeBtn {
					border: 1px solid #000;
					cursor: pointer;
					border-radius: 20px;
					padding: 0 15px;
					margin: 0 20px 0 0;
					outline: none;
					background: none;
					display: inline-block;
					width: auto;
					font-size: 14px;
					line-height: 40px;
					height: 40px;
					.icon {
						color: #000;
						font-size: 18px;
					}
					.text {
						color: #000;
						font-size: 16px;
					}
				}
				.closeBtn:hover {
					opacity: 0.7;
					.icon {
					}
					.text {
					}
				}
			}
		}
	}
	.el-date-editor.el-input {
		width: auto;
	}
</style>
