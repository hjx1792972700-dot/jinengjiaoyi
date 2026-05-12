<template>
	<div>
		<el-upload :disabled="disabled" v-if="type==1" ref="upload" :action="getActionUrl" list-type="picture-card"
			:multiple="multiple" :limit="limit" :headers="myHeaders" :file-list="fileList" :on-exceed="handleExceed"
			:on-preview="handleUploadPreview" :on-remove="handleRemove" :on-success="handleUploadSuccess"
			:on-error="handleUploadErr" :timeout="600000">
			<el-icon><Plus /></el-icon>
			<template #tip><div class="el-upload__tip">{{tip}}</div></template>
		</el-upload>
		<el-upload :disabled="disabled" v-else-if="type==2||type==3" drag ref="upload" :action="getActionUrl" :multiple="multiple"
			:limit="limit" :accept="accept" :headers="myHeaders" :file-list="fileList" :on-exceed="handleExceed"
			:on-preview="handleUploadPreview" :on-remove="handleRemove" :on-success="handleUploadSuccess"
			:on-error="handleUploadErr" :timeout="600000">
			<el-icon class="el-icon--upload"><UploadFilled /></el-icon>
			<div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
			<template #tip><div class="el-upload__tip">{{tip}}</div></template>
		</el-upload>
		<el-upload :disabled="disabled" v-else drag ref="upload" :action="getActionUrl" :multiple="multiple"
			:limit="limit" :accept="accept" :headers="myHeaders" :file-list="fileList" :on-exceed="handleExceed"
			:on-preview="handleUploadPreview" :on-remove="handleRemove" :on-success="handleUploadSuccess"
			:on-error="handleUploadErr" :timeout="600000" :before-upload="handleBeforeUpload">
			<el-icon class="el-icon--upload"><UploadFilled /></el-icon>
			<div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
			<template #tip><div class="el-upload__tip">{{tip}}</div></template>
		</el-upload>
		<el-dialog v-model="dialogVisible" append-to-body>
			<img width="100%" v-if="type==1" :src="dialogImageUrl" alt>
			<video width="100%" v-if="type==2" :src="dialogImageUrl" alt controls />
		</el-dialog>
	</div>
</template>
<script>
	import storage from "@/common/storage";
	import config from "@/config/config";
	import { Plus, UploadFilled } from '@element-plus/icons-vue'
	export default {
		components: { Plus, UploadFilled },
		data() {
			return {
				baseUrl: config.baseUrl,
				baseUrl2: config.name,
				dialogVisible: false,
				dialogImageUrl: "",
				fileList: [],
				fileUrlList: [],
				myHeaders: {}
			};
		},
		props: {
			tip: { type: String },
			action: { type: String },
			limit: { type: Number, default: 3 },
			multiple: { type: Boolean, default: false },
			fileUrls: { type: String },
			type: { type: Number, default: 1 },
			disabled: { type: Boolean, default: false },
			accept: { type: String }
		},
		mounted() {
			this.init();
			this.myHeaders = {
				'Token': storage.get("frontToken")
			}
		},
		watch: {
			fileUrls(val, oldVal) {
				this.init();
			}
		},
		computed: {
			getActionUrl() {
				return this.baseUrl + this.action;
			}
		},
		methods: {
			init() {
				let that = this
				if (this.fileUrls) {
					this.fileUrlList = this.fileUrls.split(",");
					let fileArray = [];
					this.fileUrlList.forEach(function(item, index) {
						var url = item;
						var name = that.type==4?'检测文件':index;
						var file = { name: name, url: url };
						fileArray.push(file);
					});
					this.setFileList(fileArray);
				} else {
					this.fileUrlList = [];
					this.setFileList(this.fileUrlList);
				}
			},
			async handleBeforeUpload(file) {
				let that = this
				return new Promise(async(resolve, reject) => {
					const isLt3M = file.size / 1024 / 1024 < 3;
					if (!isLt3M) {
						that.$message.error('文件大小不能超过3MB!');
						reject()
						return false
					}
					if (file.type == 'video/mp4') {
						try {
							const duration = await that.getAudioDuration(file);
							if (duration > 60) {
								that.$message.error('视频时长不能超过1分钟!');
								reject()
								return false
							}
						} catch (error) {
							that.$message.error('视频解析失败');
							reject()
							return false
						}
					}
					resolve()
				});
			},
			getAudioDuration(file) {
				return new Promise((resolve, reject) => {
					const video = document.createElement('video');
					const url = URL.createObjectURL(file);
					video.src = url;
					video.onloadedmetadata = () => {
						resolve(video.duration)
						URL.revokeObjectURL(url);
					};
					video.onerror = () => {
						reject()
						URL.revokeObjectURL(url);
					};
				});
			},
			handleUploadSuccess(res, file, fileList) {
				if (res && res.code === 0) {
					fileList[fileList.length - 1]["url"] = "upload/" + file.response.file;
					this.setFileList(fileList);
					this.$emit("change", this.fileUrlList.join(","));
				} else {
					this.$message.error(res.msg);
				}
			},
			handleUploadErr(err, file, fileList) {
				this.$message.error("文件上传失败");
			},
			handleRemove(file, fileList) {
				this.setFileList(fileList);
				this.$emit("change", this.fileUrlList.join(","));
			},
			handleUploadPreview(file) {
				if (this.type > 2) {
					window.open(file.url)
					return false
				}
				this.dialogImageUrl = file.url;
				this.dialogVisible = true;
			},
			handleExceed(files, fileList) {
				this.$message.warning(`最多上传${this.limit}张图片`);
			},
			setFileList(fileList) {
				var fileArray = [];
				var fileUrlArray = [];
				var token = storage.get("frontToken");
				let _this = this;
				fileList.forEach(function(item, index) {
					var url = item.url.split("?")[0];
					if (!url.startsWith("http")) {
						url = (_this.baseUrl + url).replace(/\/\//g, '/')
					}
					var name = item.name;
					var file = {
						name: name,
						url: url + "?token=" + token
					};
					fileArray.push(file);
					fileUrlArray.push(url);
				});
				this.fileList = fileArray;
				this.fileUrlList = fileUrlArray;
			}
		}
	};
</script>
<style lang="scss" scoped>
</style>
