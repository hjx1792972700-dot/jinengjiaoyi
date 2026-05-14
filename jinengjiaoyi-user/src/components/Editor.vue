<template>
  <div class="modern-editor" :class="{'is-focused': isFocused}">
    <div class="editor-toolbar">
      <button type="button" class="tb-btn" @click="exec('bold')" title="加粗"><b>B</b></button>
      <button type="button" class="tb-btn" @click="exec('italic')" title="斜体"><i>I</i></button>
      <button type="button" class="tb-btn" @click="exec('underline')" title="下划线"><u>U</u></button>
      <span class="tb-sep"></span>
      <button type="button" class="tb-btn" @click="exec('formatBlock','<h2>')" title="标题">H</button>
      <button type="button" class="tb-btn" @click="exec('formatBlock','<h3>')" title="小标题">h</button>
      <button type="button" class="tb-btn" @click="exec('formatBlock','<p>')" title="正文">¶</button>
      <span class="tb-sep"></span>
      <button type="button" class="tb-btn" @click="exec('insertUnorderedList')" title="无序列表">
        <svg viewBox="0 0 16 16" width="14" height="14"><circle cx="2" cy="4" r="1.2" fill="currentColor"/><line x1="5" y1="4" x2="14" y2="4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><circle cx="2" cy="8" r="1.2" fill="currentColor"/><line x1="5" y1="8" x2="14" y2="8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><circle cx="2" cy="12" r="1.2" fill="currentColor"/><line x1="5" y1="12" x2="14" y2="12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
      </button>
      <button type="button" class="tb-btn" @click="exec('insertOrderedList')" title="有序列表">
        <svg viewBox="0 0 16 16" width="14" height="14"><text x="1" y="5.5" fill="currentColor" font-size="5" font-weight="600">1</text><line x1="5" y1="4" x2="14" y2="4" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><text x="1" y="9.5" fill="currentColor" font-size="5" font-weight="600">2</text><line x1="5" y1="8" x2="14" y2="8" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><text x="1" y="13.5" fill="currentColor" font-size="5" font-weight="600">3</text><line x1="5" y1="12" x2="14" y2="12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
      </button>
      <span class="tb-sep"></span>
      <button type="button" class="tb-btn" @click="insertLink" title="链接">
        <svg viewBox="0 0 16 16" width="14" height="14"><path d="M6.5 9.5l3-3M5.5 7.5l-1.3 1.3a2 2 0 002.8 2.8l1.3-1.3M10.5 8.5l1.3-1.3a2 2 0 00-2.8-2.8L7.7 5.7" fill="none" stroke="currentColor" stroke-width="1.3" stroke-linecap="round"/></svg>
      </button>
      <button type="button" class="tb-btn" @click="insertImage" title="图片" v-if="action">
        <svg viewBox="0 0 16 16" width="14" height="14"><rect x="1.5" y="3" width="13" height="10" rx="1.5" fill="none" stroke="currentColor" stroke-width="1.2"/><circle cx="5" cy="6.5" r="1.2" fill="currentColor"/><path d="M1.5 11l3.5-3 2.5 2L11 7l3.5 4" fill="none" stroke="currentColor" stroke-width="1.2" stroke-linecap="round" stroke-linejoin="round"/></svg>
      </button>
      <span class="tb-sep"></span>
      <button type="button" class="tb-btn" @click="exec('removeFormat')" title="清除格式">
        <svg viewBox="0 0 16 16" width="14" height="14"><path d="M3 3l10 10M7 3h6l-3 10H4" fill="none" stroke="currentColor" stroke-width="1.3" stroke-linecap="round" stroke-linejoin="round"/></svg>
      </button>
    </div>
    <div class="editor-body"
      ref="editorBody"
      contenteditable="true"
      @input="onInput"
      @focus="isFocused=true"
      @blur="isFocused=false"
      @paste="onPaste"
      :data-placeholder="placeholder"
    ></div>
    <input type="file" ref="imgInput" accept="image/*" style="display:none" @change="onImageSelected" />
  </div>
</template>

<script>
export default {
  name: 'Editor',
  props: {
    value: { type: String, default: '' },
    action: { type: String, default: '' },
    placeholder: { type: String, default: '请输入内容...' }
  },
  data() {
    return { isFocused: false, innerUpdate: false }
  },
  mounted() {
    if (this.value) {
      this.$refs.editorBody.innerHTML = this.value;
    }
  },
  watch: {
    value(val) {
      if (this.innerUpdate) { this.innerUpdate = false; return; }
      if (this.$refs.editorBody && val !== this.$refs.editorBody.innerHTML) {
        this.$refs.editorBody.innerHTML = val || '';
      }
    }
  },
  methods: {
    exec(cmd, val) {
      this.$refs.editorBody.focus();
      document.execCommand(cmd, false, val || null);
    },
    onInput() {
      this.innerUpdate = true;
      this.$emit('input', this.$refs.editorBody.innerHTML);
    },
    onPaste(e) {
      e.preventDefault();
      const text = (e.clipboardData || window.clipboardData).getData('text/html')
        || (e.clipboardData || window.clipboardData).getData('text/plain');
      document.execCommand('insertHTML', false, text);
    },
    insertLink() {
      const url = prompt('请输入链接地址:', 'https://');
      if (url) this.exec('createLink', url);
    },
    insertImage() {
      this.$refs.imgInput.click();
    },
    onImageSelected(e) {
      const file = e.target.files[0];
      if (!file) return;
      const formData = new FormData();
      formData.append('file', file);
      this.$http.post(this.$config.baseUrl + this.action, formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      }).then(res => {
        if (res.data.code == 0) {
          const url = this.$config.baseUrl + 'upload/' + res.data.file;
          this.exec('insertImage', url);
        }
      });
      e.target.value = '';
    }
  }
}
</script>

<style scoped>
.modern-editor {
  border: 1.5px solid #e2e8f0;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s;
  background: #fff;
}
.modern-editor.is-focused {
  border-color: #0ea5e9;
  box-shadow: 0 0 0 3px rgba(14,165,233,0.1);
}

.editor-toolbar {
  display: flex;
  align-items: center;
  gap: 2px;
  padding: 8px 12px;
  background: #f8fafc;
  border-bottom: 1px solid #f1f5f9;
  flex-wrap: wrap;
}
.tb-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 30px;
  border: none;
  border-radius: 6px;
  background: transparent;
  color: #475569;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
  font-family: inherit;
}
.tb-btn:hover {
  background: #e0f2fe;
  color: #0ea5e9;
}
.tb-btn:active {
  background: #bae6fd;
  transform: scale(0.95);
}
.tb-sep {
  width: 1px;
  height: 18px;
  background: #e2e8f0;
  margin: 0 4px;
}

.editor-body {
  min-height: 220px;
  max-height: 500px;
  overflow-y: auto;
  padding: 16px 18px;
  font-size: 14px;
  line-height: 1.8;
  color: #1e293b;
  outline: none;
}
.editor-body:empty::before {
  content: attr(data-placeholder);
  color: #94a3b8;
  pointer-events: none;
}
.editor-body::-webkit-scrollbar { width: 4px; }
.editor-body::-webkit-scrollbar-thumb { background: rgba(14,165,233,0.15); border-radius: 4px; }

.editor-body >>> h2 { font-size: 20px; font-weight: 700; margin: 8px 0; color: #0f172a; }
.editor-body >>> h3 { font-size: 16px; font-weight: 600; margin: 6px 0; color: #1e293b; }
.editor-body >>> p { margin: 4px 0; }
.editor-body >>> ul, .editor-body >>> ol { padding-left: 20px; margin: 6px 0; }
.editor-body >>> a { color: #0ea5e9; text-decoration: underline; }
.editor-body >>> img { max-width: 100%; border-radius: 8px; margin: 8px 0; }
</style>
