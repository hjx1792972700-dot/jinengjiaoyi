<template>
	<div class="tech-sidebar">
		<div class="sidebar-logo">
			<i class="el-icon-s-platform logo-elicon"></i>
			<span class="logo-text">技能交易系统</span>
		</div>
		<div class="sidebar-user" v-if="user">
			<div class="user-avatar-wrap">
				<el-image class="user-avatar" :src="avatar ? ($base.url + avatar) : require('@/assets/img/avator.png')" fit="cover"></el-image>
				<span class="user-online-dot"></span>
			</div>
			<div class="user-info">
				<span class="user-name">{{$storage.get('adminName')}}</span>
				<span class="user-role">系统管理员</span>
			</div>
		</div>
		<nav class="sidebar-nav">
			<div v-for="item in navItems" :key="item.path"
				:class="['nav-item', {active: isActive(item.path)}]"
				@click="navigate(item.path)">
				<i :class="'el-icon-' + item.icon"></i>
				<span>{{item.label}}</span>
				<div class="active-bar" v-if="isActive(item.path)"></div>
			</div>
		</nav>
	</div>
</template>

<script>
import menu from '@/utils/menu'
export default {
	data() {
		return {
			menuList: [],
			user: null,
			avatar: '',
			navItems: [
				{ path: '', label: '工作台', icon: 'monitor' },
				{ path: 'statisticsDashboard', label: '数据统计', icon: 's-data' },
				{ path: 'userManagement', label: '用户管理', icon: 'user-solid' },
				{ path: 'businessManagement', label: '业务管理', icon: 'circle-check' },
				{ path: 'systemConfig', label: '系统配置', icon: 'setting' },
			]
		}
	},
	computed: {
		activeMenu() {
			return this.$route.path
		}
	},
	mounted() {
		let menus = menu.list()
		if (menus) {
			this.menuList = menus
			for (let i = 0; i < this.menuList.length; i++) {
				if (this.menuList[i].roleName == this.$storage.get('role')) {
					this.menuList = this.menuList[i]
					break
				}
			}
		}
		let sessionTable = this.$storage.get('sessionTable')
		this.$http({ url: sessionTable + '/session', method: 'get' }).then(({ data }) => {
			if (data && data.code === 0) {
				this.avatar = sessionTable == 'yonghu' ? data.data.touxiang : data.data.image
				this.user = data.data
			}
		})
	},
	methods: {
		isActive(path) {
			const cur = this.$route.path
			if (path === '') return cur === '/'
			return cur === '/' + path
		},
		navigate(path) {
			const target = '/' + path
			if (this.$route.path !== target) this.$router.push(target)
		},
	}
}
</script>

<style lang="scss" scoped>
$bg: #0c1222;
$surface: #141c2f;
$accent: #00b4d8;
$accent-glow: rgba(0, 180, 216, 0.15);
$text: #e2e8f0;
$text2: #64748b;
$border: rgba(255,255,255,0.06);

.tech-sidebar {
	display: flex;
	flex-direction: column;
	height: 100%;
	background: $bg;
	color: $text;
	overflow-y: auto;
	overflow-x: hidden;
}

.sidebar-logo {
	padding: 24px 20px 16px;
	display: flex;
	align-items: center;
	gap: 10px;
	border-bottom: 1px solid $border;
	flex-shrink: 0;
	.logo-elicon {
		font-size: 26px;
		color: $accent;
		filter: drop-shadow(0 0 6px rgba(0,180,216,0.4));
	}
	.logo-text {
		font-size: 15px;
		font-weight: 700;
		letter-spacing: 1px;
		background: linear-gradient(135deg, $accent, #a78bfa);
		-webkit-background-clip: text;
		-webkit-text-fill-color: transparent;
	}
}

.sidebar-user {
	padding: 16px;
	margin: 12px 10px;
	display: flex;
	align-items: center;
	gap: 12px;
	border-radius: 12px;
	background: linear-gradient(135deg, rgba(0,180,216,0.08), rgba(139,92,246,0.06));
	border: 1px solid rgba(0,180,216,0.12);
	flex-shrink: 0;
	cursor: default;
	transition: all 0.3s;
	&:hover {
		border-color: rgba(0,180,216,0.25);
		box-shadow: 0 4px 20px rgba(0,180,216,0.08);
	}
	.user-avatar-wrap {
		position: relative;
		flex-shrink: 0;
	}
	.user-avatar {
		width: 42px;
		height: 42px;
		border-radius: 50%;
		border: 2.5px solid rgba(0,180,216,0.4);
		box-shadow: 0 0 16px rgba(0,180,216,0.15);
	}
	.user-online-dot {
		position: absolute; right: 0; bottom: 1px;
		width: 10px; height: 10px; border-radius: 50%;
		background: #22c55e; border: 2px solid $bg;
		box-shadow: 0 0 6px #22c55e;
	}
	.user-info {
		display: flex; flex-direction: column; gap: 2px;
		overflow: hidden;
	}
	.user-name {
		font-size: 14px;
		font-weight: 600;
		color: $text;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.user-role {
		font-size: 11px;
		color: $accent;
		font-weight: 500;
		letter-spacing: 0.5px;
	}
}

.sidebar-nav {
	flex: 1;
	padding: 12px 10px;
}

.nav-item {
	display: flex;
	align-items: center;
	gap: 12px;
	padding: 14px 16px;
	margin: 4px 0;
	border-radius: 10px;
	cursor: pointer;
	position: relative;
	transition: all 0.25s;
	color: $text2;
	i[class^="el-icon"] {
		font-size: 18px;
		transition: color 0.25s;
	}
	span {
		font-size: 14px;
		font-weight: 500;
	}
	&:hover {
		background: $accent-glow;
		color: $text;
		i[class^="el-icon"] { color: $accent; }
	}
	&.active {
		background: linear-gradient(135deg, rgba(0,180,216,0.12), rgba(167,139,250,0.08));
		color: #fff;
		i[class^="el-icon"] { color: $accent; filter: drop-shadow(0 0 4px rgba(0,180,216,0.5)); }
		span { font-weight: 600; }
		.active-bar {
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 3px;
			height: 20px;
			background: $accent;
			border-radius: 0 3px 3px 0;
			box-shadow: 0 0 8px rgba(0,180,216,0.6);
		}
	}
}

</style>
