<template>
  <div class="editor-container">
    <quill-editor
      ref="quillEditor"
      v-model="content"
      :options="editorOption"
      @change="onEditorChange"
    />
  </div>
</template>

<script>
import { quillEditor } from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

export default {
  name: 'Editor',
  components: { quillEditor },
  props: {
    value: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      content: this.value,
      editorOption: {
        theme: 'snow',
        placeholder: '请输入内容...',
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'],
            [{ 'header': [1, 2, 3, 4, 5, 6, false] }],
            [{ 'list': 'ordered' }, { 'list': 'bullet' }],
            [{ 'color': [] }, { 'background': [] }],
            [{ 'align': [] }],
            ['link', 'image'],
            ['clean']
          ]
        }
      }
    }
  },
  watch: {
    value(val) {
      if (val !== this.content) {
        this.content = val
      }
    }
  },
  methods: {
    onEditorChange({ html }) {
      this.content = html
      this.$emit('input', html)
    }
  }
}
</script>

<style scoped>
.editor-container {
  line-height: normal;
}
.editor-container .ql-editor {
  min-height: 200px;
}
</style>
