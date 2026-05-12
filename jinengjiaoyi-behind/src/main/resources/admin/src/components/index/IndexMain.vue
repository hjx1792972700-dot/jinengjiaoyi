<template>
	<div class="tech-layout">
		<div class="tech-sidebar-wrapper">
			<index-aside></index-aside>
		</div>
		<div class="tech-main">
			<index-header class="tech-header"></index-header>
			<bread-crumbs :title="title" class="tech-breadcrumb"></bread-crumbs>
			<tags-view />
			<div class="tech-content">
				<router-view></router-view>
			</div>
		</div>
	</div>
</template>

<script>
	import IndexAside from '@/components/index/IndexAsideStatic.vue'
	import IndexHeader from '@/components/index/IndexHeader.vue'
	import TagsView from '@/components/index/TagsView/index.vue'
	import menu from "@/utils/menu";
	export default {
		components: { IndexAside, IndexHeader, TagsView },
		data() {
			return { menuList: [], role: "", title: '' }
		},
		mounted() {
			let menus = menu.list();
			this.menuList = menus;
			this.role = this.$storage.get("role");
		},
	}
</script>

<style lang="scss" scoped>
$sidebar-w: 200px;
$header-h: 56px;
$bg: #0f172a;
$surface: #1e293b;

.tech-layout {
	display: flex;
	height: 100vh;
	overflow: hidden;
	background: $bg;
}

.tech-sidebar-wrapper {
	width: $sidebar-w;
	flex-shrink: 0;
	position: fixed;
	top: 0;
	left: 0;
	bottom: 0;
	z-index: 1001;
}

.tech-main {
	flex: 1;
	margin-left: $sidebar-w;
	display: flex;
	flex-direction: column;
	height: 100vh;
	overflow: hidden;
}

.tech-header {
	height: $header-h;
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 0 24px;
	background: $surface;
	border-bottom: 1px solid rgba(255,255,255,0.06);
	position: sticky;
	top: 0;
	z-index: 100;
	flex-shrink: 0;
}

.tech-breadcrumb {
	padding: 14px 24px;
	flex-shrink: 0;
}

.tech-content {
	flex: 1;
	padding: 0 24px 24px;
	overflow-y: auto;
	overflow-x: hidden;
}
</style>
