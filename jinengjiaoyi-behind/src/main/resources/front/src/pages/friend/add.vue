
















<template>
	<div class="add-update-preview">
		<el-form
			class="add-update-form"
			ref="ruleForm"
			:model="ruleForm"
			:rules="rules"
			label-width="200px"
			>
			<el-form-item class="add-item" label="用户ID" prop="uid">
				<el-input v-model="ruleForm.uid" 
					placeholder="用户ID" clearable :readonly="ro.uid"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="好友用户ID" prop="fid">
				<el-input v-model="ruleForm.fid" 
					placeholder="好友用户ID" clearable :readonly="ro.fid"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="名称" prop="name">
				<el-input v-model="ruleForm.name" 
					placeholder="名称" clearable :readonly="ro.name"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="图片" v-if="type!='cross' || (type=='cross' && !ro.picture)" prop="picture">
				<file-upload
					tip="点击上传图片"
					action="file/upload"
					:limit="3"
					:multiple="true"
					:disabled="ro.picture"
					:fileUrls="ruleForm.picture?ruleForm.picture:''"
					@change="pictureUploadChange"
					></file-upload>
			</el-form-item>
			<el-form-item class="add-item" v-else label="图片" prop="picture">
				<img v-if="ruleForm.picture.substring(0,4)=='http'" class="upload-img" v-bind:key="index" :src="ruleForm.picture.split(',')[0]">
				<img v-else class="upload-img" v-bind:key="index" v-for="(item,index) in ruleForm.picture.split(',')" :src="baseUrl+item">
			</el-form-item>
			<el-form-item class="add-item" label="角色" prop="role">
				<el-input v-model="ruleForm.role" 
					placeholder="角色" clearable :readonly="ro.role"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="表名" prop="tablename">
				<el-input v-model="ruleForm.tablename" 
					placeholder="表名" clearable :readonly="ro.tablename"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="别名" prop="alias">
				<el-input v-model="ruleForm.alias" 
					placeholder="别名" clearable :readonly="ro.alias"></el-input>
			</el-form-item>
			<el-form-item class="add-item" label="类型(0:好友申请，1:好友，2:消息)" prop="type">
				<el-input v-model.number="ruleForm.type" 
					placeholder="类型(0:好友申请，1:好友，2:消息)" clearable :readonly="ro.type"></el-input>
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
					uid : false,
					fid : false,
					name : false,
					picture : false,
					role : false,
					tablename : false,
					alias : false,
					type : false,
				},
				type: '',
				userTableName: localStorage.getItem('UserTableName'),
				ruleForm: {
					uid: '',
					fid: '',
					name: '',
					picture: '',
					role: '',
					tablename: '',
					alias: '',
					type: '',
				},

				rules: {
					uid: [
						{ required: true, message: '用户ID不能为空', trigger: 'blur' },
					],
					fid: [
						{ required: true, message: '好友用户ID不能为空', trigger: 'blur' },
					],
					name: [
						{ required: true, message: '名称不能为空', trigger: 'blur' },
					],
					picture: [
						{ required: true, message: '图片不能为空', trigger: 'blur' },
					],
					role: [
					],
					tablename: [
					],
					alias: [
					],
					type: [
						{ validator: this.$validate.isIntNumer, trigger: 'blur' },
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
						if(o=='uid'){
							this.ruleForm.uid = obj[o];
							this.ro.uid = true;
							continue;
						}
						if(o=='fid'){
							this.ruleForm.fid = obj[o];
							this.ro.fid = true;
							continue;
						}
						if(o=='name'){
							this.ruleForm.name = obj[o];
							this.ro.name = true;
							continue;
						}
						if(o=='picture'){
							this.ruleForm.picture = obj[o]?obj[o].split(",")[0]:'';
							this.ro.picture = true;
							continue;
						}
						if(o=='role'){
							this.ruleForm.role = obj[o];
							this.ro.role = true;
							continue;
						}
						if(o=='tablename'){
							this.ruleForm.tablename = obj[o];
							this.ro.tablename = true;
							continue;
						}
						if(o=='alias'){
							this.ruleForm.alias = obj[o];
							this.ro.alias = true;
							continue;
						}
						if(o=='type'){
							this.ruleForm.type = obj[o];
							this.ro.type = true;
							continue;
						}
					}
				}else if(type=='edit'){
					this.info()
				}

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
				await this.$http.get(`friend/detail/${this.$route.query.id}`, {emulateJSON: true}).then(res => {
					if (res.data.code == 0) {
						this.ruleForm = res.data.data;
					}
				});
			},
			// 提交
			async onSubmit(subMitType=null) {
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
							}
						}

						await this.$http.post(`friend/${this.ruleForm.id?'update':this.centerType?'save':'add'}`, this.ruleForm).then(async res => {
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
			pictureUploadChange(fileUrls) {
				this.ruleForm.picture = fileUrls.replace(/^\/+/, "");
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
