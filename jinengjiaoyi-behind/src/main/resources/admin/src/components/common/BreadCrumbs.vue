<template>
	<div class="breadcrumb-preview">
		<el-breadcrumb separator="/">
			<el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
				<span v-if="item.redirect==='noRedirect'||index==levelList.length-1" class="no-redirect">{{changeInList(item)}}</span>
				<a v-else @click.prevent="handleLink(item)">首页</a>
			</el-breadcrumb-item>
		</el-breadcrumb>
	</div>
</template>

<script>
	export default {
		data() { return { levelList: null } },
		watch: { $route() { this.getBreadcrumb() } },
		created() { this.getBreadcrumb() },
		methods: {
			changeInList(row) {
				return row.name
			},
			getBreadcrumb() {
				let route = this.$route
				let matched = route.matched.filter(item => item.meta)
				matched = [{ path: '/index' }].concat(matched)
				this.levelList = matched.filter(item => item.meta)
			},
			handleLink(item) {
				const { redirect, path } = item
				if (redirect) { this.$router.push(redirect); return }
				this.$router.push(path || '/')
			},
		}
	}
</script>

<style lang="scss" scoped>
.el-breadcrumb {
	:deep(.el-breadcrumb__separator) {
		margin: 0 6px;
		color: #475569;
		font-weight: 500;
	}
	:deep(.el-breadcrumb__inner a) {
		color: #00b4d8;
		font-weight: 600;
		font-size: 14px;
	}
	:deep(.el-breadcrumb__inner) {
		padding: 4px 0;
		color: #94a3b8;
		font-size: 14px;
	}
}
</style>
