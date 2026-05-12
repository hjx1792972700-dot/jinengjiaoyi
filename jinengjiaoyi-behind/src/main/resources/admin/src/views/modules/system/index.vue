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
import jinengfenlei from '@/views/modules/jinengfenlei/list.vue'
import ziliaoleixing from '@/views/modules/ziliaoleixing/list.vue'
import newstype from '@/views/modules/newstype/list.vue'
import forumtype from '@/views/modules/forumtype/list.vue'
import news from '@/views/modules/news/list.vue'
import messages from '@/views/modules/messages/list.vue'
import {
	Menu,
	Folder,
	Notebook,
	ChatDotSquare,
	Document,
	Message,
} from '@element-plus/icons-vue'

export default {
	components: { jinengfenlei, ziliaoleixing, newstype, forumtype, news, messages },
	data() {
		return {
			activeTab: 'jinengfenlei',
			tabs: [
				{ name: 'jinengfenlei', label: '技能分类', icon: Menu },
				{ name: 'ziliaoleixing', label: '资料类型', icon: Folder },
				{ name: 'newstype', label: '资讯分类', icon: Notebook },
				{ name: 'forumtype', label: '论坛类型', icon: ChatDotSquare },
				{ name: 'news', label: '公告资讯', icon: Document },
				{ name: 'messages', label: '留言反馈', icon: Message },
			]
		}
	},
	created() {
		if (this.$route.query.tab) {
			this.activeTab = this.$route.query.tab
		}
	},
	watch: {
		'$route.query.tab'(val) {
			if (val) this.activeTab = val
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
	overflow-x: auto;
	flex-wrap: wrap;
		.tab-item {
		display: flex;
		align-items: center;
		gap: 6px;
		padding: 10px 14px;
		border-radius: 8px;
		cursor: pointer;
		font-size: 13px;
		color: $text2;
		white-space: nowrap;
		transition: all 0.25s;
		.tab-tabicon { font-size: 14px; }
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
