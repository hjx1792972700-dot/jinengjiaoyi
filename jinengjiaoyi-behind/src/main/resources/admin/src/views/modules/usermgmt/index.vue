<template>
	<div class="tab-page">
		<div class="tab-bar">
			<div v-for="tab in tabs" :key="tab.name"
				:class="['tab-item', {active: activeTab === tab.name}]"
				@click="activeTab = tab.name">
				<el-icon class="tab-tabicon"><component :is="tab.icon" /></el-icon>
				<span>{{tab.label}}</span>
			</div>
		</div>
		<div class="tab-content">
			<keep-alive>
				<component :is="activeTab"></component>
			</keep-alive>
		</div>
	</div>
</template>

<script>
import yonghu from '@/views/modules/yonghu/list.vue'
import huiyuanchongzhi from '@/views/modules/huiyuanchongzhi/list.vue'

import { User, Wallet } from '@element-plus/icons-vue'

export default {
	components: { yonghu, huiyuanchongzhi },
	data() {
		return {
			activeTab: 'yonghu',
			tabs: [
				{ name: 'yonghu', label: '用户管理', icon: User },
				{ name: 'huiyuanchongzhi', label: '会员充值', icon: Wallet },
			]
		}
	}
}
</script>

<style lang="scss" scoped>
$accent: #00b4d8;
$surface: #1e293b;
$text: #e2e8f0;
$text2: #94a3b8;
$border: rgba(255,255,255,0.06);

.tab-page { height: 100%; display: flex; flex-direction: column; overflow: hidden; }

.tab-bar {
	display: flex;
	gap: 4px;
	padding: 4px;
	background: $surface;
	border-radius: 12px;
	margin-bottom: 20px;
	border: 1px solid $border;
	.tab-item {
		display: flex;
		align-items: center;
		gap: 6px;
		padding: 10px 18px;
		border-radius: 8px;
		cursor: pointer;
		font-size: 13px;
		color: $text2;
		white-space: nowrap;
		transition: all 0.25s;
		.tab-tabicon { font-size: 15px; }
		&:hover { color: $text; background: rgba(255,255,255,0.04); }
		&.active {
			color: #fff;
			background: linear-gradient(135deg, rgba(0,180,216,0.2), rgba(167,139,250,0.12));
			font-weight: 600;
			box-shadow: 0 0 12px rgba(0,180,216,0.15);
			.tab-tabicon { color: $accent; }
		}
	}
}

.tab-content {
	background: $surface;
	border-radius: 12px;
	padding: 20px;
	border: 1px solid $border;
	flex: 1; overflow: auto;
}
</style>
