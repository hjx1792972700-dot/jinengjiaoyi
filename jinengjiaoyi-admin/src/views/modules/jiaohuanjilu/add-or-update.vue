
































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
				<el-form-item class="input" v-if="type!='info'"  label="手机号" prop="shoujihao" >
					<el-input v-model="ruleForm.shoujihao" placeholder="手机号" clearable  :readonly="ro.shoujihao"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="手机号" prop="shoujihao" >
					<el-input v-model="ruleForm.shoujihao" placeholder="手机号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="标题" prop="biaoti" >
					<el-input v-model="ruleForm.biaoti" placeholder="标题" clearable  :readonly="ro.biaoti"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="标题" prop="biaoti" >
					<el-input v-model="ruleForm.biaoti" placeholder="标题" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="技能分类" prop="jinengfenlei" >
					<el-input v-model="ruleForm.jinengfenlei" placeholder="技能分类" clearable  :readonly="ro.jinengfenlei"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="技能分类" prop="jinengfenlei" >
					<el-input v-model="ruleForm.jinengfenlei" placeholder="技能分类" readonly></el-input>
				</el-form-item>
				<el-form-item class="upload" v-if="type!='info' && !ro.gongjitupian" label="供给图片" prop="gongjitupian" >
					<file-upload
						tip="点击上传供给图片"
						action="file/upload"
						:limit="3"
						:disabled="ro.gongjitupian"
						:multiple="true"
						:fileUrls="ruleForm.gongjitupian?ruleForm.gongjitupian:''"
						@change="gongjitupianUploadChange"
					></file-upload>
				</el-form-item>
				<el-form-item class="upload" v-else-if="ruleForm.gongjitupian" label="供给图片" prop="gongjitupian" >
					<img v-if="ruleForm.gongjitupian.substring(0,4)=='http'&&ruleForm.gongjitupian.split(',w').length>1" class="upload-img" style="margin-right:20px;" v-bind:key="index" :src="ruleForm.gongjitupian" width="100" height="100" @click="imgPreView(ruleForm.tupian)">
					<img v-else-if="ruleForm.gongjitupian.substring(0,4)=='http'" class="upload-img" style="margin-right:20px;" v-bind:key="index" :src="ruleForm.gongjitupian.split(',')[0]" width="100" height="100" @click="imgPreView(ruleForm.tupian.split(',')[0])">
					<img v-else class="upload-img" style="margin-right:20px;" v-bind:key="index" v-for="(item,index) in ruleForm.gongjitupian.split(',')" :src="$base.url+item" width="100" height="100" @click="imgPreView($base.url+item)">
				</el-form-item>
				<el-form-item class="select" v-if="type!='info'"  label="交换技能" prop="jiaohuanjineng" >
					<el-select :disabled="ro.jiaohuanjineng" v-model="ruleForm.jiaohuanjineng" placeholder="请选择交换技能"  filterable>
						<el-option
							v-for="(item,index) in jiaohuanjinengOptions"
							v-bind:key="index"
							:label="item"
							:value="item">
						</el-option>
					</el-select>
				</el-form-item>
				<el-form-item v-else class="input" label="交换技能" prop="jiaohuanjineng" >
					<el-input v-model="ruleForm.jiaohuanjineng"
						placeholder="交换技能" readonly></el-input>
				</el-form-item>
				<el-form-item class="fileupload" v-if="type!='info'&& !ro.gongjishipin" label="供给视频" prop="gongjishipin" >
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
				<el-form-item v-else-if="ruleForm.gongjishipin" label="供给视频" prop="gongjishipin" >
					<el-button class="viewBtn" type="text" size="small" @click="download($base.url+ruleForm.gongjishipin)">
						<span class="icon iconfont icon-chakan2"></span>
						预览
					</el-button>
				</el-form-item>
				<el-form-item v-else-if="!ruleForm.gongjishipin" label="供给视频" prop="gongjishipin" >
					<el-button class="unBtn" type="text" size="small">
						<span class="icon iconfont icon-jubao"></span>
						暂无
					</el-button>
				</el-form-item>
				<el-form-item class="fileupload" v-if="type!='info'&& !ro.gongjifujian" label="供给附件" prop="gongjifujian" >
					<file-upload
						tip="点击上传供给附件"
						action="file/upload"
						:limit="1"
						:type="2"
						:multiple="true"
						:disabled="ro.gongjifujian"
						:fileUrls="ruleForm.gongjifujian?ruleForm.gongjifujian:''"
						@change="gongjifujianUploadChange"
					></file-upload>
				</el-form-item>  
				<el-form-item v-else-if="ruleForm.gongjifujian" label="供给附件" prop="gongjifujian" >
					<el-button class="downBtn" type="text" size="small" @click="download($base.url+ruleForm.gongjifujian)">
						<span class="icon iconfont icon-xiazai7"></span>
						下载
					</el-button>
				</el-form-item>
				<el-form-item v-else-if="!ruleForm.gongjifujian" label="供给附件" prop="gongjifujian" >
					<el-button class="unBtn" type="text" size="small">
						<span class="icon iconfont icon-jubao"></span>
						暂无
					</el-button>
				</el-form-item>
				<el-form-item class="date" v-if="type!='info'" label="交换时间" prop="jiaohuanshijian" >
					<el-date-picker
						format="YYYY 年 MM 月 DD 日"
						value-format="YYYY-MM-DD"
						v-model="ruleForm.jiaohuanshijian" 
						type="date"
						:readonly="ro.jiaohuanshijian"
						placeholder="交换时间"
					></el-date-picker> 
				</el-form-item>
				<el-form-item class="input" v-else-if="ruleForm.jiaohuanshijian" label="交换时间" prop="jiaohuanshijian" >
					<el-input v-model="ruleForm.jiaohuanshijian" placeholder="交换时间" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="供给人账号" prop="gongjirenzhanghao" >
					<el-input v-model="ruleForm.gongjirenzhanghao" placeholder="供给人账号" clearable  :readonly="ro.gongjirenzhanghao"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="供给人账号" prop="gongjirenzhanghao" >
					<el-input v-model="ruleForm.gongjirenzhanghao" placeholder="供给人账号" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="供给人姓名" prop="gongjirenxingming" >
					<el-input v-model="ruleForm.gongjirenxingming" placeholder="供给人姓名" clearable  :readonly="ro.gongjirenxingming"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="供给人姓名" prop="gongjirenxingming" >
					<el-input v-model="ruleForm.gongjirenxingming" placeholder="供给人姓名" readonly></el-input>
				</el-form-item>
				<el-form-item class="input" v-if="type!='info'"  label="联系方式" prop="lianxifangshi" >
					<el-input v-model="ruleForm.lianxifangshi" placeholder="联系方式" clearable  :readonly="ro.lianxifangshi"></el-input>
				</el-form-item>
				<el-form-item v-else class="input" label="联系方式" prop="lianxifangshi" >
					<el-input v-model="ruleForm.lianxifangshi" placeholder="联系方式" readonly></el-input>
				</el-form-item>
			</template>
			<el-form-item class="textarea" v-if="type!='info'" label="供给内容" prop="gongjineirong" >
				<el-input
					style="min-width: 200px; max-width: 600px;"
					type="textarea"
					:rows="8"
					placeholder="供给内容"
					v-model="ruleForm.gongjineirong" >
				</el-input>
			</el-form-item>
			<el-form-item v-else-if="ruleForm.gongjineirong" label="供给内容" prop="gongjineirong"  class="textBox">
				<span class="text">{{ruleForm.gongjineirong}}</span>
			</el-form-item>
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
	export default {
		data() {
			return {
				id: '',
				type: '',
			
			
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
			this.ruleForm.jiaohuanshijian = this.getCurDate()
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
							this.ruleForm.gongjitupian = obj[o];
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
				}

				// 获取用户信息
				this.$http({
					url: `${this.sessionTable}/session`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						var json = data.data;
						if(((json.yonghuzhanghao!=''&&json.yonghuzhanghao) || json.yonghuzhanghao==0) && this.sessionTable!="users"){
							this.ruleForm.gongjirenzhanghao = json.yonghuzhanghao
							this.ro.gongjirenzhanghao = true;
						}
						if(((json.yonghuxingming!=''&&json.yonghuxingming) || json.yonghuxingming==0) && this.sessionTable!="users"){
							this.ruleForm.gongjirenxingming = json.yonghuxingming
							this.ro.gongjirenxingming = true;
						}
						if(((json.shoujihao!=''&&json.shoujihao) || json.shoujihao==0) && this.sessionTable!="users"){
							this.ruleForm.lianxifangshi = json.shoujihao
							this.ro.lianxifangshi = true;
						}
					} else {
						this.$message.error(data.msg);
					}
				});
				this.$http({
					url: `option/jinengfenlei/jinengfenlei`,
					method: "get",
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.jiaohuanjinengOptions = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
			
			},
			// 多级联动参数

			async info(id) {
				await this.$http({
					url: `jiaohuanjilu/info/${id}`,
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
					if(this.ruleForm.gongjitupian!=null) {
						this.ruleForm.gongjitupian = this.ruleForm.gongjitupian.split(',').map(u => u.replace(/^\/+/, '')).join(',');
					}
					if(this.ruleForm.gongjishipin!=null) {
						this.ruleForm.gongjishipin = this.ruleForm.gongjishipin.split(',').map(u => u.replace(/^\/+/, '')).join(',');
					}
					if(this.ruleForm.gongjifujian!=null) {
						this.ruleForm.gongjifujian = this.ruleForm.gongjifujian.split(',').map(u => u.replace(/^\/+/, '')).join(',');
					}
					var objcross = this.$storage.getObj('crossObj');
					//更新跨表属性
					var crossuserid;
					var crossrefid;
					var crossoptnum;
					var finishNum = 0;
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
									else {
										crossuserid=this.$storage.get('userid');
										crossrefid=obj['id'];
										crossoptnum=this.$storage.get('statusColumnName');
										crossoptnum=crossoptnum.replace(/\[/,"").replace(/\]/,"");
									}
								}
								if(crossrefid && crossuserid) {
									this.ruleForm.crossuserid = crossuserid;
									this.ruleForm.crossrefid = crossrefid;
									let params = { 
										page: 1, 
										limit: 10, 
										crossuserid:this.ruleForm.crossuserid,
										crossrefid:this.ruleForm.crossrefid,
									} 
									await this.$http({ 
										url: "jiaohuanjilu/page", 
										method: "get", 
										params: params 
									}).then(({ 
										data 
									}) => { 
										if (data && data.code === 0) {
											finishNum = data.data.total
										}
									})
								}
								if(finishNum>=crossoptnum) {
									this.$message.error(this.$storage.get('tips'));
									return false;
								}
							}
							await this.$http({
								url: `jiaohuanjilu/${!this.ruleForm.id ? "save" : "update"}`,
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
											this.parent.jiaohuanjiluCrossAddOrUpdateFlag = false;
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
				this.parent.jiaohuanjiluCrossAddOrUpdateFlag = false;
			},
			gongjitupianUploadChange(fileUrls) {
				this.ruleForm.gongjitupian = fileUrls;
			},
			gongjishipinUploadChange(fileUrls) {
				this.ruleForm.gongjishipin = fileUrls;
			},
			gongjifujianUploadChange(fileUrls) {
				this.ruleForm.gongjifujian = fileUrls;
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
