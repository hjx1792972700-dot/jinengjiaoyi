<template>
	<div>
		<el-upload :class="refname + 'upload'" :action="getActionUrl" name="file" :headers="header" :show-file-list="false" style="display: none;"
			:on-success="uploadSuccess" :on-error="uploadError" :before-upload="beforeUpload"></el-upload>

		<QuillEditor
			ref="quillEditorRef"
			theme="snow"
			:content="modelValue"
			content-type="html"
			:options="editorOption"
			@update:content="onEditorChange"
		/>
	</div>
</template>
<script>
	import { QuillEditor } from '@vueup/vue-quill'
	import '@vueup/vue-quill/dist/vue-quill.snow.css'
	import config from "@/config/config"

	const toolbarOptions = [
		["bold", "italic", "underline", "strike"],
		["blockquote", "code-block"],
		[{ header: 1 }, { header: 2 }],
		[{ list: "ordered" }, { list: "bullet" }],
		[{ indent: "-1" }, { indent: "+1" }],
		[{ align: [] }],
		["link", "image"],
		["clean"],
	]

	export default {
		components: { QuillEditor },
		props: {
			modelValue: { type: String },
			action: { type: String },
			maxSize: { type: Number, default: 4000 },
			myQuillEditor: { type: String },
		},
		emits: ['update:modelValue'],
		data() {
			let page_this = this
			return {
				baseUrl: config.baseUrl,
				baseUrl2: config.name,
				refname: this.myQuillEditor || 'editor',
				quillUpdateImg: false,
				editorOption: {
					modules: {
						toolbar: {
							container: toolbarOptions,
							handlers: {
								image(value) {
									if (value) {
										document.querySelector("." + page_this.refname + "upload input").click();
									} else {
										this.quill.format("image", false);
									}
								},
							},
						},
					},
					placeholder: "请输入正文",
				},
				header: {
					'Token': localStorage.getItem('frontToken')
				}
			}
		},
		computed: {
			getActionUrl() {
				return this.baseUrl + this.action;
			}
		},
		methods: {
			onEditorChange(content) {
				this.$emit("update:modelValue", content);
			},
			beforeUpload() {
				this.quillUpdateImg = true;
			},
			uploadSuccess(res, file) {
				let quill = this.$refs.quillEditorRef.getQuill()
				if (res.code === 0) {
					let length = quill.getSelection()?.index || 0;
					quill.insertEmbed(length, "image", this.baseUrl + "upload/" + res.file);
					quill.setSelection(length + 1);
				} else {
					this.$message.error("图片插入失败");
				}
				this.quillUpdateImg = false;
			},
			uploadError() {
				this.quillUpdateImg = false;
				this.$message.error("图片插入失败");
			}
		}
	};
</script>
<style>
	.editor { line-height: normal !important; }
	.ql-snow .ql-tooltip[data-mode="link"]::before { content: "请输入链接地址:"; }
	.ql-snow .ql-tooltip.ql-editing a.ql-action::after { border-right: 0px; content: "保存"; padding-right: 0px; }
	.ql-snow .ql-tooltip[data-mode="video"]::before { content: "请输入视频地址:"; }
	.ql-container { }
</style>
