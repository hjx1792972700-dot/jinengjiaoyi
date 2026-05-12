<template>
	<div class="tab-page">
		<div class="tab-bar">
			<div v-for="tab in visibleTabs" :key="tab.name"
				:class="['tab-item', {active: activeTab === tab.name}]"
				@click="switchTab(tab.name)">
				<el-icon class="tab-tabicon"><component :is="tab.icon" /></el-icon>
				<span>{{tab.label}}</span>
				<span v-if="tab.badge > 0" class="tab-badge" :class="{'pulse': tab.badge > 0}">{{tab.badge}}</span>
			</div>
		</div>
		<transition name="tab-fade" mode="out-in">
			<div class="tab-content" :key="activeTab">
				<keep-alive>
					<component :is="activeTab" @audit-changed="refreshBadges"></component>
				</keep-alive>
			</div>
		</transition>
	</div>
</template>

<script>
import jinengxuqiu from '@/views/modules/jinengxuqiu/list.vue'
import skillAudit from '@/views/modules/jinengxuqiu/skill-audit.vue'
import demandAudit from '@/views/modules/jinengxuqiu/demand-audit.vue'
import jiaohuanshenqing from '@/views/modules/jiaohuanshenqing/list.vue'
import jiaohuanjilu from '@/views/modules/jiaohuanjilu/list.vue'
import forum from '@/views/modules/forum/list.vue'
import xuexiziliao from '@/views/modules/xuexiziliao/list.vue'

export default {
	components: {
		jinengxuqiu,
		'skill-audit': skillAudit,
		'demand-audit': demandAudit,
		jiaohuanshenqing,
		jiaohuanjilu,
		forum,
		xuexiziliao},
	data() {
		return {
			activeTab: 'jinengxuqiu',
			tabs: [
				{ name: 'jinengxuqiu', label: '技能市场', icon: 'Tickets', auth: 'jinengxuqiu', badge: 0 },
				{ name: 'skill-audit', label: '技能审核', icon: 'CircleCheck', auth: 'jinengxuqiu', badge: 0 },
				{ name: 'demand-audit', label: '需求审核', icon: 'DocumentChecked', auth: 'jinengxuqiu', badge: 0 },
				{ name: 'jiaohuanshenqing', label: '交换申请', icon: 'Promotion', auth: 'jiaohuanshenqing', badge: 0 },
				{ name: 'jiaohuanjilu', label: '交换记录', icon: 'Document', auth: 'jiaohuanjilu', badge: 0 },
				{ name: 'forum', label: '技能社区', icon: 'ChatDotRound', auth: 'forum', badge: 0 },
				{ name: 'xuexiziliao', label: '学习资料', icon: 'Reading', auth: 'xuexiziliao', badge: 0 },
			]
		}
	},
	computed: {
		visibleTabs() { return this.tabs }
	},
	created() {
		this.refreshBadges()
	},
	methods: {
		switchTab(name) {
			this.activeTab = name
		},
		refreshBadges() {
			this.$http({
				url: 'jinengxuqiu/page',
				method: 'get',
				params: { page: 1, limit: 1, leixing: '技能', sfsh: '待审核' }
			}).then(({ data }) => {
				if (data && data.code === 0) {
					let auditTab = this.tabs.find(t => t.name === 'skill-audit')
					if (auditTab) auditTab.badge = data.data.total || 0
				}
			})
			this.$http({
				url: 'jinengxuqiu/page',
				method: 'get',
				params: { page: 1, limit: 1, leixing: '需求', sfsh: '待审核' }
			}).then(({ data }) => {
				if (data && data.code === 0) {
					let demandTab = this.tabs.find(t => t.name === 'demand-audit')
					if (demandTab) demandTab.badge = data.data.total || 0
				}
			})
		}
	}
}
</script>

<style lang="scss" scoped>
$accent: #00b4d8;
$surface: #1e293b;
$bg: #0f172a;
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
	overflow-x: auto;
	.tab-item {
		position: relative;
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

.tab-badge {
	display: inline-flex;
	align-items: center;
	justify-content: center;
	min-width: 18px;
	height: 18px;
	padding: 0 5px;
	border-radius: 10px;
	background: #f56c6c;
	color: #fff;
	font-size: 11px;
	font-weight: 700;
	line-height: 1;
	margin-left: 2px;
	&.pulse {
		animation: badge-pulse 2s ease-in-out infinite;
	}
}

@keyframes badge-pulse {
	0%, 100% { transform: scale(1); box-shadow: 0 0 0 0 rgba(245,108,108,0.5); }
	50% { transform: scale(1.1); box-shadow: 0 0 8px 2px rgba(245,108,108,0.3); }
}

.tab-content {
	background: $surface;
	border-radius: 12px;
	padding: 20px;
	border: 1px solid $border;
	flex: 1; overflow: auto;
}

.tab-fade-enter-active, .tab-fade-leave-active {
	transition: opacity 0.2s ease, transform 0.2s ease;
}
.tab-fade-enter {
	opacity: 0;
	transform: translateY(8px);
}
.tab-fade-leave-to {
	opacity: 0;
	transform: translateY(-8px);
}
</style>
