<template>
	<div :style='{"minHeight":"100vh","width":"100%","padding":"0 30px 0 0","background":"none"}'>
		<el-form
			:style='{"border":"1px solid #FFFFFF","padding":"50px 30px","borderRadius":"24px","flexWrap":"wrap","background":"linear-gradient( 180deg, #FFF1E8 0%, #FFFFFF 100%)","display":"flex","justifyContent":"space-between"}'
			class="add-update-preview"
			ref="ruleForm"
			:model="ruleForm"
			label-width="100%"
		>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}'   v-if="flag=='yonghu'"  label="用户账号" prop="yonghuzhanghao">
					<el-input v-model="ruleForm.yonghuzhanghao" :readonly="ro.yonghuzhanghao" placeholder="用户账号" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}'   v-if="flag=='yonghu'"  label="用户姓名" prop="yonghuxingming">
					<el-input v-model="ruleForm.yonghuxingming" :readonly="ro.yonghuxingming" placeholder="用户姓名" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}' v-if="flag=='yonghu'"  label="性别" prop="xingbie">
					<el-select filterable v-model="ruleForm.xingbie" :disabled="ro.xingbie" placeholder="请选择性别">
						<el-option
							v-for="(item,index) in yonghuxingbieOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}'   v-if="flag=='yonghu'"  label="身份证" prop="idcard">
					<el-input @input="idcardChange" v-model="ruleForm.idcard" :readonly="ro.idcard" placeholder="身份证" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}'   v-if="flag=='yonghu'"  label="年龄" prop="age">
					<el-input v-model="ruleForm.age" :readonly="ro.age||true" placeholder="年龄" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}' v-if="flag=='yonghu'"  label="信誉指数" prop="xinyuzhishu">
					<el-select filterable v-model="ruleForm.xinyuzhishu" :disabled="ro.xinyuzhishu" placeholder="请选择信誉指数">
						<el-option
							v-for="(item,index) in yonghuxinyuzhishuOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}'   v-if="flag=='yonghu'"  label="手机号" prop="shoujihao">
					<el-input v-model="ruleForm.shoujihao" :readonly="ro.shoujihao" placeholder="手机号" clearable></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}' v-if="flag=='yonghu'" label="头像" prop="touxiang">
					<file-upload
						tip="点击上传头像"
						action="file/upload"
						:limit="1"
						:multiple="false"
						:fileUrls="ruleForm.touxiang?ruleForm.touxiang:''"
						@change="yonghutouxiangUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}' v-if="flag=='users'" label="用户名" prop="username">
					<el-input v-model="ruleForm.username" placeholder="用户名"></el-input>
				</el-form-item>
				<el-form-item :style='{"width":"calc(50% - 40px)","margin":"10px","flexWrap":"wrap","flexDirection":"column","display":"flex"}' v-if="flag=='users'" label="头像" prop="image">
					<file-upload
						tip="点击上传头像"
						action="file/upload"
						:limit="1"
						:multiple="false"
						:fileUrls="ruleForm.image?ruleForm.image:''"
						@change="usersimageUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item :style='{"padding":"0 30px","margin":"30px  auto 0 auto","flexWrap":"wrap","background":"none","display":"flex","width":"auto","justifyContent":"center"}'>
					<el-button class="btn3" type="primary" @click="onUpdateHandler">
						<span class="icon iconfont icon-queren15"></span>
						确定 
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
			return {
				ruleForm: {},
				ro: {},
				flag: '',
				usersFlag: false,
				yonghuxingbieOptions: [],
				yonghuxinyuzhishuOptions: [],
			};
		},
		mounted() {
			var table = this.$storage.get("sessionTable");
			this.flag = table;
			this.$http({
				url: `${this.$storage.get("sessionTable")}/session`,
				method: "get"
			}).then(({ data }) => {
				if (data && data.code === 0) {
					if(table == 'yonghu') {
						this.ro = {
							yonghuzhanghao: false,
							yonghuxingming: false,
							mima: false,
							xingbie: false,
							idcard: false,
							age: false,
							xinyuzhishu: true,
							shoujihao: false,
							touxiang: false,
							sfsh: false,
							shhf: false,
						}
					}

					this.ruleForm = data.data;
				} else {
					this.$message.error(data.msg);
				}
			});
			this.yonghuxingbieOptions = "男,女".split(',')
			this.yonghuxinyuzhishuOptions = "⭐,⭐⭐,⭐⭐⭐,⭐⭐⭐⭐,⭐⭐⭐⭐⭐".split(',')
		},
		methods: {
			yonghutouxiangUploadChange(fileUrls) {
				this.ruleForm.touxiang = fileUrls;
			},
			usersimageUploadChange(fileUrls) {
				this.ruleForm.image = fileUrls;
			},
			onUpdateHandler() {
				if((!this.ruleForm.yonghuzhanghao)&& 'yonghu'==this.flag){
					this.$message.error('用户账号不能为空');
					return
				}
				if((!this.ruleForm.yonghuxingming)&& 'yonghu'==this.flag){
					this.$message.error('用户姓名不能为空');
					return
				}
				if((!this.ruleForm.mima)&& 'yonghu'==this.flag){
					this.$message.error('密码不能为空');
					return
				}
				if('yonghu' ==this.flag && this.ruleForm.idcard&&(!checkIdCard(this.ruleForm.idcard))){
					this.$message.error(`身份证应输入身份证格式`);
					return
				}
				if('yonghu' ==this.flag && this.ruleForm.age&&(!isIntNumer(this.ruleForm.age))){
					this.$message.error(`年龄应输入整数`);
					return
				}
				if(this.ruleForm.touxiang!=null) {
					this.ruleForm.touxiang = this.ruleForm.touxiang.split(',').map(u => u.replace(/^\/+/, '')).join(',');
				}
				if('users'==this.flag && this.ruleForm.username.trim().length<1) {
					this.$message.error(`用户名不能为空`);
					return	
				}
				if(this.flag=='users'){
					this.ruleForm.image = this.ruleForm.image.split(',').map(u => u.replace(/^\/+/, '')).join(',')
				}
				this.$http({
					url: `${this.$storage.get("sessionTable")}/update`,
					method: "post",
					data: this.ruleForm
				}).then(({ data }) => {
					if (data && data.code === 0) {
						if(this.flag=='users'){
							this.$storage.set('headportrait',this.ruleForm.image)
						}else {
							if(this.flag == 'yonghu') {
								this.$storage.set('headportrait',this.ruleForm.touxiang)
							}
						}
						ElMessage({
							message: "修改信息成功",
							type: "success",
							duration: 1500,
							onClose: () => {
								window.location.reload();
							}
						});
					} else {
						this.$message.error(data.msg);
					}
				});
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
	.el-date-editor.el-input {
		width: auto;
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
	
	.add-update-preview ::v-deep .el-upload--picture-card {
		background: transparent;
		border: 0;
		border-radius: 0;
		width: auto;
		height: auto;
		line-height: initial;
		vertical-align: middle;
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
	
	.add-update-preview .btn3 {
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
	
	.add-update-preview .btn3:hover {
				opacity: 0.8;
			}
	.editor>.avatar-uploader {
		line-height: 0;
		height: 0;
	}
</style>
