






















<template>
	<div class="addEdit-block">
		<el-form
			class="add-update-preview"
			ref="ruleForm"
			:model="ruleForm"
			:rules="rules"
			label-width="100%"
		>
			<template >
				<el-form-item class="input" v-if="type!='info'"  label="用户账号" prop="yonghuzhanghao" >
					<el-input v-model="ruleForm.yonghuzhanghao" placeholder="用户账号" clearable  :readonly="ro.yonghuzhanghao"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="用户账号" prop="yonghuzhanghao" >
					<el-input v-model="ruleForm.yonghuzhanghao" placeholder="用户账号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="用户姓名" prop="yonghuxingming" >
					<el-input v-model="ruleForm.yonghuxingming" placeholder="用户姓名" clearable  :readonly="ro.yonghuxingming"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="用户姓名" prop="yonghuxingming" >
					<el-input v-model="ruleForm.yonghuxingming" placeholder="用户姓名" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="密码" prop="mima" >
					<el-input v-model="ruleForm.mima" placeholder="密码" clearable  :readonly="ro.mima"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="密码" prop="mima" >
					<el-input v-model="ruleForm.mima" placeholder="密码" readonly></el-input>
				</el-form-item>
				<el-form-item class="select" v-if="type!='info'"  label="性别" prop="xingbie" >
					<el-select :disabled="ro.xingbie" v-model="ruleForm.xingbie" placeholder="请选择性别"  filterable>
						<el-option
							v-for="(item,index) in xingbieOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item v-else class="input" label="性别" prop="xingbie" >
					<el-input v-model="ruleForm.xingbie"
						placeholder="性别" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="身份证" prop="idcard" >
					<el-input @input="idcardChange" v-model="ruleForm.idcard" placeholder="身份证" clearable :readonly="ro.idcard"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="身份证" prop="idcard" >
					<el-input v-model="ruleForm.idcard" placeholder="身份证" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="年龄" prop="age" >
					<el-input v-model="ruleForm.age" placeholder="年龄" clearable :readonly="ro.age ||true"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="年龄" prop="age" >
					<el-input v-model="ruleForm.age" placeholder="年龄" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="信誉指数" prop="xinyuzhishu" >
					<el-input-number v-model="ruleForm.xinyuzhishu" :disabled="ro.xinyuzhishu" :min="0" :max="100" :step="5" controls-position="right" style="width:100%"></el-input-number>
				</el-form-item>
				<el-form-item v-else class="input" label="信誉指数" prop="xinyuzhishu" >
					<el-input :value="ruleForm.xinyuzhishu" placeholder="信誉指数" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="手机号" prop="shoujihao" >
					<el-input v-model="ruleForm.shoujihao" placeholder="手机号" clearable  :readonly="ro.shoujihao"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="手机号" prop="shoujihao" >
					<el-input v-model="ruleForm.shoujihao" placeholder="手机号" readonly></el-input>
				</el-form-item>
				<el-form-item class="select" v-if="type!='info'" label="VIP会员" prop="vip">
				<el-select v-model="ruleForm.vip" placeholder="请选择">
					<el-option label="是" value="是"></el-option>
					<el-option label="否" value="否"></el-option>
				</el-select>
			</el-form-item>
			<el-form-item v-else class="input" label="VIP会员" prop="vip">
				<el-input v-model="ruleForm.vip" placeholder="VIP会员" readonly></el-input>
			</el-form-item>
			<el-form-item class="date" v-if="type!='info'" label="VIP到期时间" prop="vipExpire">
				<el-date-picker
					format="YYYY 年 MM 月 DD 日"
					value-format="YYYY-MM-DD"
					v-model="ruleForm.vipExpire"
					type="date"
					placeholder="VIP到期时间"
				></el-date-picker>
			</el-form-item>
			<el-form-item v-else class="input" label="VIP到期时间" prop="vipExpire">
				<el-input v-model="ruleForm.vipExpire" placeholder="VIP到期时间" readonly></el-input>
			</el-form-item>
			<el-form-item class="upload" v-if="type!='info' && !ro.touxiang" label="头像" prop="touxiang" >
					<file-upload
						tip="点击上传头像"
						action="file/upload"
						:limit="3"
						:disabled="ro.touxiang"
						:multiple="true"
						:fileUrls="ruleForm.touxiang?ruleForm.touxiang:''"
						@change="touxiangUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item class="upload" v-else-if="ruleForm.touxiang" label="头像" prop="touxiang" >
					<img v-if="ruleForm.touxiang.substring(0,4)=='http'&&ruleForm.touxiang.split(',w').length>1" class="upload-img" style="margin-right:20px;" v-bind:key="index" :src="ruleForm.touxiang" width="100" height="100" @click="imgPreView(ruleForm.tupian)">
					<img v-else-if="ruleForm.touxiang.substring(0,4)=='http'" class="upload-img" style="margin-right:20px;" v-bind:key="index" :src="ruleForm.touxiang.split(',')[0]" width="100" height="100" @click="imgPreView(ruleForm.tupian.split(',')[0])">
					<img v-else class="upload-img" style="margin-right:20px;" v-bind:key="index" v-for="(item,index) in ruleForm.touxiang.split(',')" :src="$base.url+item" width="100" height="100" @click="imgPreView($base.url+item)">
				</el-form-item>
			</template>
			<el-form-item class="btn">
				<el-button class="btn3"  v-if="type!='info'" type="success" @click="onSubmit">
					<span class="icon iconfont icon-queren15"></span>
					确定 
				</el-button>
				<el-button class="btn4" v-if="type!='info'" type="success" @click="back()">
					<span class="icon iconfont icon-guanbi2"></span>
					取消
				</el-button>
				<el-button class="btn5" v-if="type=='info'" type="success" @click="back()">
					<span class="icon iconfont icon-fanhui13"></span>
					返回
				</el-button>
			</el-form-item>
		</el-form>
    

	</div>
</template>
<script>
	import { 
		isIntNumer,
		checkIdCard,
	} from "@/utils/validate";
	export default {
		data() {
			var validateIdCard = (rule, value, callback) => {
				if(!value){
					callback();
				} else if (!checkIdCard(value)) {
					callback(new Error("请输入正确的身份证号码"));
				} else {
					callback();
				}
			};
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
					yonghuzhanghao : false,
					yonghuxingming : false,
					mima : false,
					xingbie : false,
					idcard : false,
					age : false,
					xinyuzhishu : false,
					shoujihao : false,
					touxiang : false,
					sfsh : false,
					shhf : false,
				},
			
				ruleForm: {
					yonghuzhanghao: '',
					yonghuxingming: '',
					mima: '',
					xingbie: '',
					idcard: '',
					age: '',
					xinyuzhishu: 80,
					shoujihao: '',
					touxiang: '',
					sfsh: '待审核',
					shhf: '',
				},
				xingbieOptions: [],

				rules: {
					yonghuzhanghao: [
						{ required: true, message: '用户账号不能为空', trigger: 'blur' },
					],
					yonghuxingming: [
						{ required: true, message: '用户姓名不能为空', trigger: 'blur' },
					],
					mima: [
						{ required: true, message: '密码不能为空', trigger: 'blur' },
					],
					xingbie: [
					],
					idcard: [
						{ validator: validateIdCard, trigger: 'blur' },
					],
					age: [
						{ validator: validateIntNumber, trigger: 'blur' },
					],
					xinyuzhishu: [
					],
					shoujihao: [
					],
					touxiang: [
					],
					sfsh: [
					],
					shhf: [
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
						if(o=='mima'){
							this.ruleForm.mima = obj[o];
							this.ro.mima = true;
							continue;
						}
						if(o=='xingbie'){
							this.ruleForm.xingbie = obj[o];
							this.ro.xingbie = true;
							continue;
						}
						if(o=='idcard'){
							this.ruleForm.idcard = obj[o];
							this.ro.idcard = true;
							continue;
						}
						if(o=='age'){
							this.ruleForm.age = obj[o];
							this.ro.age = true;
							continue;
						}
						if(o=='xinyuzhishu'){
							let v = parseInt(obj[o])
							this.ruleForm.xinyuzhishu = isNaN(v) ? 80 : v;
							this.ro.xinyuzhishu = true;
							continue;
						}
						if(o=='shoujihao'){
							this.ruleForm.shoujihao = obj[o];
							this.ro.shoujihao = true;
							continue;
						}
						if(o=='touxiang'){
							this.ruleForm.touxiang = obj[o];
							this.ro.touxiang = true;
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
						if(this.sessionTable!="users") {
							this.ro.xinyuzhishu = true;
						}
					} else {
						this.$message.error(data.msg);
					}
				});
				this.xingbieOptions = "男,女".split(',')
			
			},
			// 多级联动参数

			async info(id) {
				await this.$http({
					url: `yonghu/info/${id}`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.ruleForm = data.data;
						if (this.ruleForm.xinyuzhishu != null) {
							let v = parseInt(this.ruleForm.xinyuzhishu)
							this.ruleForm.xinyuzhishu = isNaN(v) ? 80 : v
						}
						let reg=new RegExp('../../../upload','g')
					} else {
						this.$message.error(data.msg);
					}
				});
			},

			// 提交
			async onSubmit() {
				if(this.ruleForm.touxiang!=null) {
					this.ruleForm.touxiang = this.ruleForm.touxiang.split(',').map(u => u.replace(/^\/+/, '')).join(',');
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
								url: `yonghu/${!this.ruleForm.id ? "save" : "update"}`,
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
											this.parent.yonghuCrossAddOrUpdateFlag = false;
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
				this.parent.yonghuCrossAddOrUpdateFlag = false;
			},
			touxiangUploadChange(fileUrls) {
				this.ruleForm.touxiang = fileUrls;
			},
			idcardChange(e) {
				if(checkIdCard(e)) {
					this.ruleForm.age = this.getAge(e)
				}else {
					this.ruleForm.age = ''
				}
			},
		}
	};
</script>
<style lang="scss" scoped>
	.addEdit-block {
		padding: 0 30px 30px 0;
		background: none;
		width: 100%;
		min-height: 100vh;
	}
	.add-update-preview {
		border: 1px solid #FFFFFF;
		border-radius: 24px;
		padding: 50px 30px;
		background: linear-gradient( 180deg, #FFF1E8 0%, #FFFFFF 100%);
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
	}
	.amap-wrapper {
		width: 100%;
		height: 500px;
	}
	
	.search-box {
		position: absolute;
	}
	
	.el-date-editor.el-input {
		width: auto;
	}
	.add-update-preview ::v-deep .el-form-item {
		margin: 10px;
		flex-direction: column;
		display: flex;
		width: calc(50% - 40px);
		flex-wrap: wrap;
	}
	.add-update-preview .el-form-item ::v-deep .el-form-item__label {
		padding: 0 0px;
		color: #000000;
		white-space: nowrap;
		background: none;
		font-weight: 400;
		width: 100%;
		font-size: 16px;
		line-height: 30px;
		text-align: left;
		height: 30px;
	}
	
	.add-update-preview .el-form-item ::v-deep .el-form-item__content {
		margin-left: auto !important;
		margin: auto !important;
		flex: 1;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
	.add-update-preview ::v-deep .el-form-item.editorBox {
		margin: 10px 0px 10px 0 ;
		display: flex;
		width: 100%;
		flex-wrap: wrap;
	}
	.add-update-preview .el-form-item.editorBox ::v-deep .el-form-item__label {
		padding: 0 0px;
		color: #000000;
		white-space: nowrap;
		font-weight: 400;
		width: 100%;
		font-size: 16px;
		line-height: 30px;
		text-align: left;
		height: 30px;
	}
	
	.add-update-preview .el-form-item.editorBox ::v-deep .el-form-item__content {
		margin-left: auto !important;
		padding: 0;
		margin: auto !important;
		flex: 1;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
	.add-update-preview ::v-deep.el-form-item.editorBox .editor {
		border: 1px solid #875F41;
		border-radius: 4px;
		color: #875F41;
		max-width: 100% !important;
		align-content: flex-start;
		flex: 1;
		background: none;
		display: flex;
		align-items: flex-start;
		flex-wrap: wrap;
		height: auto;
	}
	.add-update-preview ::v-deep.el-form-item.editorBox .editor .ql-toolbar {
		border: 0px solid #555555;
		background: none;
		width: 100%;
		border-width: 0;
	}
	.add-update-preview ::v-deep.el-form-item.editorBox .editor .ql-container {
		border: 0px solid #555555;
		background: none;
		width: 100%;
		min-height: 200px;
	}
	.add-update-preview ::v-deep.el-form-item.editorBox .editor .ql-container .ql-blank::before {
		color: #999;
	}
	
	.add-update-preview ::v-deep .el-form-item.textBox {
		margin: 10px  0;
		flex-direction: column;
		display: flex;
		width: calc(100% - 0px);
		flex-wrap: wrap;
	}
	.add-update-preview .el-form-item.textBox ::v-deep .el-form-item__label {
		padding: 0 0px;
		color: #000000;
		white-space: nowrap;
		background: none;
		font-weight: 400;
		width: 100%;
		font-size: 16px;
		line-height: 30px;
		text-align: left;
		height: 30px;
	}
	
	.add-update-preview .el-form-item.textBox ::v-deep .el-form-item__content {
		margin-left: auto !important;
		padding: 0;
		margin: auto !important;
		flex: 1;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
	.add-update-preview ::v-deep.el-form-item.textBox span.text {
		border: 1px solid #875F41;
		border-radius: 4px;
		padding: 6px 12px;
		outline: none;
		color: #875F41;
		background: #fff;
		flex: 1;
		width: 100%;
		font-size: 16px;
		height: auto;
	}
	
	.add-update-preview .el-input {
		width: 100%;
	}
	.add-update-preview .el-input ::v-deep .el-input__inner {
		border:  1px solid #875F41;
		border-radius: 4px;
		padding: 0 12px;
		outline: none;
		color: #875F41;
		background: #FFFFFF;
		flex: 1;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-input ::v-deep .el-input__inner[readonly="readonly"] {
		border:  0px solid #875F41;
		border-radius: 4px;
		padding: 0 0px;
		outline: none;
		color: #875F41;
		flex: 1;
		background: none;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-input-number {
		text-align: left;
		width: 100%;
	}
	.add-update-preview .el-input-number ::v-deep .el-input__inner {
		text-align: left;
		border:  1px solid #875F41;
		border-radius: 4px;
		padding: 0 12px;
		outline: none;
		color: #875F41;
		background: #FFFFFF;
		flex: 1;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-input-number ::v-deep .is-disabled .el-input__inner {
		text-align: left;
		border:  0px solid #875F41;
		border-radius: 4px;
		padding: 0 0px;
		outline: none;
		color: #875F41;
		flex: 1;
		background: none;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-input-number ::v-deep .el-input-number__decrease {
		display: none;
	}
	.add-update-preview .el-input-number ::v-deep .el-input-number__increase {
		display: none;
	}
	.add-update-preview .el-select {
		width: 100%;
	}
	.add-update-preview .el-select ::v-deep .el-input__inner {
		border:  1px solid #875F41;
		border-radius: 4px;
		padding: 0 12px;
		outline: none;
		color:  #875F41;
		flex: 1;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-select ::v-deep .is-disabled .el-input__inner {
		border: 0px solid  #875F41;
		border-radius: 4px;
		padding: 0 0px;
		outline: none;
		color:  #875F41;
		flex: 1;
		background: none;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-date-editor {
		width: 100%;
	}
	.add-update-preview .el-date-editor ::v-deep .el-input__inner {
		border:  1px solid  #875F41;
		border-radius: 4px;
		padding: 0 30px;
		outline: none;
		color:  #875F41;
		flex: 1;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .el-date-editor ::v-deep .el-input__inner[readonly="readonly"] {
		border: 0px solid  #875F41;
		border-radius: 4px;
		padding: 0 30px;
		outline: none;
		color:  #875F41;
		flex: 1;
		background: none;
		width: 100%;
		font-size: 16px;
		height: 50px;
	}
	.add-update-preview .viewBtn {
		border: 0px solid #555555;
		cursor: pointer;
		padding: 0 30px;
		margin: 0;
		color: #FFFFFF;
		font-weight: 600;
		font-size: 14px;
		line-height: 50px;
		border-radius: 8px  8px  8px  8px;
		outline: none;
		background: #875F41;
		width: auto;
		text-align: center;
		height: 50px;
		.iconfont {
			margin: 0 2px;
			color: #fff;
			display: none;
			font-size: 14px;
			height: 40px;
		}
	}
	.add-update-preview .viewBtn:hover {
		opacity: 0.8;
	}
	.add-update-preview .downBtn {
		border: 0px solid #555555;
		cursor: pointer;
		padding: 0 30px;
		margin: 0;
		color: #FFFFFF;
		font-weight: 600;
		font-size: 14px;
		line-height: 50px;
		border-radius: 8px  8px  8px  8px;
		outline: none;
		background: #875F41;
		width: auto;
		text-align: center;
		height: 50px;
		.iconfont {
			margin: 0 2px;
			color: #fff;
			display: none;
			font-size: 14px;
			height: 40px;
		}
	}
	.add-update-preview .downBtn:hover {
		opacity: 0.8;
	}
	.add-update-preview .unBtn {
		border: 0px solid #555555;
		cursor: pointer;
		padding: 0 30px;
		margin: 0;
		color: #666;
		font-weight: 600;
		font-size: 14px;
		line-height: 50px;
		border-radius: 8px  8px  8px  8px;
		outline: none;
		background: #875F4130;
		width: auto;
		text-align: center;
		height: 50px;
		.iconfont {
			margin: 0 2px;
			color: #fff;
			display: none;
			font-size: 14px;
			height: 40px;
		}
	}
	.add-update-preview .unBtn:hover {
		opacity: 0.8;
	}
	.add-update-preview ::v-deep .el-upload--picture-card {
		background: transparent;
		border: 0;
		border-radius: 0;
		width: auto;
		height: auto;
		line-height: initial;
		vertical-align: middle;
	}
	
	.add-update-preview ::v-deep .upload .upload-img {
		border:  1px solid  #875F41;
		cursor: pointer;
		border-radius: 4px;
		color:  #875F41;
		font-weight: 600;
		width: 80px;
		font-size: 30px;
		line-height: 80px;
		text-align: center;
		height: 80px;
	}
	
	.add-update-preview ::v-deep .el-upload-list .el-upload-list__item {
		border:  1px solid  #875F41;
		cursor: pointer;
		border-radius: 4px;
		color:  #875F41;
		font-weight: 600;
		width: 80px;
		font-size: 30px;
		line-height: 80px;
		text-align: center;
		height: 80px;
	}
	
	.add-update-preview ::v-deep .el-upload .el-icon-plus {
		border:  1px solid  #875F41;
		cursor: pointer;
		border-radius: 4px;
		color:  #875F41;
		font-weight: 600;
		width: 80px;
		font-size: 30px;
		line-height: 80px;
		text-align: center;
		height: 80px;
	}
	.add-update-preview ::v-deep .el-upload__tip {
		padding: 0 10px;
		color: #000000;
		font-size: 15px;
	}
	.add-update-preview ::v-deep .el-form-item.fileupload {
		margin: 10px  ;
		flex-direction: column;
		display: flex;
		width: calc(50% - 40px);
		flex-wrap: wrap;
	}
	.add-update-preview .el-form-item.fileupload ::v-deep .el-form-item__label {
		padding: 0 0px;
		color: #000000;
		white-space: nowrap;
		font-weight: 400;
		width: 100%;
		font-size: 16px;
		line-height: 30px ;
		text-align: left;
		height: 30px ;
	}
	
	.add-update-preview .el-form-item.fileupload ::v-deep .el-form-item__content {
		margin-left: auto !important;
		margin: auto !important;
		flex: 1;
		display: flex;
		width: 100%;
		justify-content: flex-start;
		align-items: flex-start;
		flex-wrap: wrap;
	}
	.add-update-preview .el-form-item.fileupload ::v-deep .el-upload-dragger {
		color: #875F41;
		display: flex;
		font-size: 16px;
		border-color: #875F41;
		flex-wrap: wrap;
		border-radius: 4px;
		outline: none;
		background: none;
		width: 100%;
		border-width:  1px;
		align-items: center;
		border-style: solid;
		height: 100px;
	}
	.add-update-preview .el-form-item.fileupload .el-upload-dragger ::v-deep .el-icon-upload {
		margin: 0;
		color: #206cb480;
		width: 100%;
		font-size: 66px;
	}
	.add-update-preview .el-form-item.fileupload .el-upload-dragger ::v-deep .el-upload__text {
		color: #606266;
		textalign: center;
		width: 100%;
		fontsize: 14px;
		line-height: 1;
	}
	.add-update-preview .el-form-item.fileupload .el-upload-dragger ::v-deep .el-upload__text em {
		fontstyle: normal;
		color: #409EFF;
	}
	
	.add-update-preview .el-textarea ::v-deep .el-textarea__inner {
		border-radius: 4px;
		padding: 12px;
		outline: none;
		color: #875F41;
		background: none;
		width: 100%;
		font-size: 16px;
		border-color: #875F41;
		border-width: 1px;
		border-style: solid;
		height: auto;
	}
	.add-update-preview .el-textarea ::v-deep .el-textarea__inner[readonly="readonly"] {
		border-radius: 4px;
		padding: 12px 0;
		outline: none;
		color: #875F41;
		background: none;
		width: 100%;
		font-size: 16px;
		border-color: #875F41;
		border-width: 0px;
		border-style: solid;
		height: auto;
	}
	.add-update-preview ::v-deep .el-form-item.btn {
		padding: 0 30px;
		margin: 30px  auto 0 auto;
		background: none;
		display: flex;
		width: auto;
		justify-content: center;
		flex-wrap: wrap;
		.btn1 {
			border: 0px solid #206cb4;
			cursor: pointer;
			padding: 0 24px;
			margin: 4px;
			color: #fff;
			font-weight: bold;
			font-size: 16px;
			border-radius: 4px;
			outline: none;
			background:  linear-gradient( 180deg, #FBAE7D 0%, #875F41 100%);
			width: auto;
			min-width: 176px;
			height: 47px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 16px;
				height: 40px;
			}
		}
		.btn1:hover {
			opacity: 0.8;
		}
		.btn2 {
			border: 0px solid #206cb4;
			cursor: pointer;
			padding: 0 24px;
			margin: 4px;
			color: #fff;
			font-weight: bold;
			font-size: 16px;
			border-radius: 4px;
			outline: none;
			background:  linear-gradient( 180deg, #FBAE7D 0%, #875F41 100%);
			width: auto;
			min-width: 176px;
			height: 47px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 16px;
				height: 40px;
			}
		}
		.btn2:hover {
			opacity: 0.8;
		}
		.btn3 {
			border: 0px solid #206cb4;
			cursor: pointer;
			padding: 0 24px;
			margin: 4px;
			color: #fff;
			font-weight: bold;
			font-size: 16px;
			border-radius: 4px;
			outline: none;
			background: linear-gradient( 180deg, #FBAE7D 0%, #875F41 100%);
			width: auto;
			min-width: 176px;
			height: 47px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 16px;
				height: 40px;
			}
		}
		.btn3:hover {
			opacity: 0.8;
		}
		.btn4 {
			border: 0px solid #206cb4;
			cursor: pointer;
			padding: 0 24px;
			margin: 4px;
			color: #fff;
			font-weight: bold;
			font-size: 16px;
			border-radius: 4px;
			outline: none;
			background: linear-gradient( 180deg, #FBAE7D 0%, #875F41 100%);
			width: auto;
			min-width: 176px;
			height: 47px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 16px;
				height: 40px;
			}
		}
		.btn4:hover {
			opacity: 0.8;
		}
		.btn5 {
			border: 0px solid #206cb4;
			cursor: pointer;
			padding: 0 24px;
			margin: 4px;
			color: #fff;
			font-weight: bold;
			font-size: 16px;
			border-radius: 4px;
			outline: none;
			background: linear-gradient( 180deg, #FBAE7D 0%, #875F41 100%);
			width: auto;
			min-width: 176px;
			height: 47px;
			.iconfont {
				margin: 0 2px;
				color: #fff;
				display: none;
				font-size: 16px;
				height: 40px;
			}
		}
		.btn5:hover {
			opacity: 0.8;
		}
	}
	.add-update-preview .el-form-item.btn ::v-deep .el-form-item__label {
		padding: 0 0px;
		color: #000000;
		white-space: nowrap;
		background: none;
		font-weight: 400;
		width: 100%;
		font-size: 16px;
		line-height: 30px;
		text-align: left;
		height: 30px;
	}
	
	.add-update-preview .el-form-item.btn ::v-deep .el-form-item__content {
	}
</style>
