# Vue 2 → Vue 3 迁移计划

## 技术栈变更

| 旧 (Vue 2) | 新 (Vue 3) |
|-------------|-------------|
| Vue 2.6 | Vue 3.4+ |
| Vue CLI 4 | Vite 5 |
| Vue Router 3 | Vue Router 4 |
| Vuex 3 | Pinia |
| Element UI 2 | Element Plus |
| vue-resource | axios |
| vue-quill-editor | @vueup/vue-quill |
| vue-template-compiler | @vue/compiler-sfc (内置) |
| vue-baidu-map | @vuemap/vue-baidu-map |

## 迁移策略

保留 Options API 风格（最小改动），只更新框架层面的 breaking changes。

## 关键代码变更模式

1. `new Vue()` → `createApp()`
2. `Vue.use()` → `app.use()`
3. `Vue.prototype.$xxx` → `app.config.globalProperties.$xxx`
4. `Vue.component()` → `app.component()`
5. `this.$http` (vue-resource) → axios 封装保持相同 API
6. `<el-dialog :visible.sync>` → `<el-dialog v-model>`
7. `slot="xxx"` → `#xxx`
8. Vuex → Pinia

## Front 用户端进度

- [x] 项目脚手架 (Vite + Vue 3)
- [x] 核心基础设施 (main.js, router, store, config, utils)
- [x] 通用组件迁移
- [x] 页面迁移
- [x] 样式迁移

## Admin 管理端进度

- [x] 项目脚手架
- [x] 核心基础设施
- [x] 通用组件迁移
- [x] 页面迁移
- [x] 样式迁移

## 验证

- [ ] Front 项目构建通过
- [ ] Admin 项目构建通过
