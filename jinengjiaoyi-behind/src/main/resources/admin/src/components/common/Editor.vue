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

	const toolbarOptions = [
		["bold", "italic", "underline", "strike"],
		["blockquote", "code-block"],
		[{ header: 1 }, { header: 2 }],
		[{ list: "ordered" }, { list: "bullet" }],
		[{ script: "sub" }, { script: "super" }],
		[{ indent: "-1" }, { indent: "+1" }],
		[{ size: ["small", false, "large", "huge"] }],
		[{ header: [1, 2, 3, 4, 5, 6, false] }],
		[{ color: [] }, { background: [] }],
		[{ font: [] }],
		[{ align: [] }],
		["clean"],
		["link", "image", "video"],
	];

	export default {
		components: { QuillEditor },
		props: {
			modelValue: { type: String },
			action: { type: String },
			maxSize: { type: Number, default: 4000 },
			myQuillEditor: { type: String },
		},
		emits: ['update:modelValue'],
		mounted() {
		},
		data() {
			let page_this = this
			return {
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
					'Token': this.$storage ? this.$storage.get("Token") : ''
				}
			};
		},
		computed: {
			getActionUrl() {
				return `/${this.$base.name}/` + this.action;
			}
		},
		methods: {
			audioPlay() {
				var div = document.createElement('div')
				div.innerHTML = this.modelValue
				var text = div.textContent
				const utterance = new SpeechSynthesisUtterance();
				utterance.text = text;
				utterance.lang = 'zh-CN';
				utterance.rate = 1.5;
				utterance.pitch = 1.0;
				utterance.volume = 1.0;
				window.speechSynthesis.speak(utterance);
			},
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
					quill.insertEmbed(length, "image", this.$base.url + "upload/" + res.file);
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
	.editor .ql-toolbar .ql-table { width: auto !important; padding: 0 6px !important; font-size: 12px; }
	.editor .ql-toolbar .ql-table::before { content: "表格"; }
	.editor { line-height: normal !important; }
	.ql-snow .ql-tooltip[data-mode="link"]::before { content: "请输入链接地址:"; }
	.ql-snow .ql-tooltip.ql-editing a.ql-action::after { border-right: 0px; content: "保存"; padding-right: 0px; }
	.ql-snow .ql-tooltip[data-mode="video"]::before { content: "请输入视频地址:"; }
	.ql-container { }
	.ql-snow .ql-picker.ql-size .ql-picker-label::before, .ql-snow .ql-picker.ql-size .ql-picker-item::before { content: "14px"; }
	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before, .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before { content: "10px"; }
	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before, .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before { content: "18px"; }
	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before, .ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before { content: "32px"; }
	.ql-snow .ql-picker.ql-header .ql-picker-label::before, .ql-snow .ql-picker.ql-header .ql-picker-item::before { content: "文本"; }
	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before, .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before { content: "标题1"; }
	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before, .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before { content: "标题2"; }
	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before, .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before { content: "标题3"; }
	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before, .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before { content: "标题4"; }
	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before, .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before { content: "标题5"; }
	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before, .ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before { content: "标题6"; }
	.ql-snow .ql-picker.ql-font .ql-picker-label::before, .ql-snow .ql-picker.ql-font .ql-picker-item::before { content: "标准字体"; }
	.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before, .ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before { content: "衬线字体"; }
	.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before, .ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before { content: "等宽字体"; }
</style>
