<template>
	<div class="my-forum-wrap">
		<!-- 顶部概览 + 操作区 -->
		<div class="top-bar">
			<div class="stat-chips">
				<div class="chip" :class="{active: filterStatus==='all'}" @click="filterStatus='all'; getList()">
					<i class="el-icon-grid"></i>
					<span class="chip-label">全部</span>
					<span class="chip-num">{{stats.total}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='待审核'}" @click="filterStatus='待审核'; getList()">
					<i class="el-icon-time"></i>
					<span class="chip-label">待审核</span>
					<span class="chip-num">{{stats.pending}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='是'}" @click="filterStatus='是'; getList()">
					<i class="el-icon-circle-check"></i>
					<span class="chip-label">已通过</span>
					<span class="chip-num">{{stats.approved}}</span>
				</div>
				<div class="chip" :class="{active: filterStatus==='否'}" @click="filterStatus='否'; getList()">
					<i class="el-icon-circle-close"></i>
					<span class="chip-label">未通过</span>
					<span class="chip-num">{{stats.rejected}}</span>
				</div>
			</div>
			<button class="publish-btn" type="button" @click="publishPost">
				<i class="el-icon-edit"></i> 发布新帖
			</button>
		</div>

		<!-- 空状态 -->
		<div class="empty-state" v-if="!loading && tableData.length === 0">
			<div class="empty-icon">
				<svg viewBox="0 0 80 80" width="80" height="80">
					<rect x="12" y="16" width="56" height="48" rx="8" fill="none" stroke="rgba(0,212,255,0.2)" stroke-width="1.5"/>
					<line x1="24" y1="32" x2="56" y2="32" stroke="rgba(0,212,255,0.15)" stroke-width="2" stroke-linecap="round"/>
					<line x1="24" y1="40" x2="48" y2="40" stroke="rgba(0,212,255,0.1)" stroke-width="2" stroke-linecap="round"/>
					<line x1="24" y1="48" x2="40" y2="48" stroke="rgba(0,212,255,0.08)" stroke-width="2" stroke-linecap="round"/>
					<circle cx="58" cy="52" r="12" fill="rgba(0,212,255,0.08)" stroke="rgba(0,212,255,0.25)" stroke-width="1.5"/>
					<line x1="55" y1="52" x2="61" y2="52" stroke="rgba(0,212,255,0.4)" stroke-width="1.5" stroke-linecap="round"/>
					<line x1="58" y1="49" x2="58" y2="55" stroke="rgba(0,212,255,0.4)" stroke-width="1.5" stroke-linecap="round"/>
				</svg>
			</div>
			<p class="empty-title">还没有发布过帖子</p>
			<p class="empty-sub">在社区分享你的技能故事，与大家交流互动</p>
			<button class="empty-btn" type="button" @click="publishPost"><i class="el-icon-edit"></i> 写第一篇帖子</button>
		</div>

		<!-- 帖子列表 -->
		<div class="post-list" v-loading="loading" v-if="tableData.length > 0 || loading">
			<div class="post-card" v-for="item in tableData" :key="item.id" @click="toDetail(item)">
				<div class="post-cover" v-if="item.cover">
					<img :src="$config.baseUrl + item.cover.split(',')[0]" />
				</div>
				<div class="post-body">
					<div class="post-top">
						<h3 class="post-title">{{item.title || '未命名帖子'}}</h3>
						<span class="status-tag" :class="item.sfsh==='是' ? 'st-pass' : (item.sfsh==='否' ? 'st-fail' : 'st-wait')">
							{{item.sfsh==='是' ? '已通过' : (item.sfsh==='否' ? '未通过' : '待审核')}}
						</span>
					</div>
					<p class="post-excerpt" v-if="item.content">{{stripHtml(item.content)}}</p>
					<div class="post-meta">
						<span class="meta-chip" v-if="item.typename"><i class="el-icon-collection-tag"></i>{{item.typename}}</span>
						<span class="meta-chip"><i class="el-icon-date"></i>{{item.addtime ? item.addtime.split(' ')[0] : '--'}}</span>
						<span class="meta-chip anon" v-if="item.isanon==1"><i class="el-icon-user-solid"></i>匿名</span>
						<span class="meta-chip private" v-if="item.isdone==='关闭'"><i class="el-icon-lock"></i>私人</span>
					</div>
					<div class="post-bottom">
						<div class="post-stats">
							<span><i class="el-icon-top"></i>{{item.thumbsupnum||0}}</span>
							<span><i class="el-icon-star-off"></i>{{item.storeupnum||0}}</span>
						</div>
						<div class="post-actions" @click.stop>
							<button class="act-btn act-view" type="button" @click="toDetail(item)"><i class="el-icon-view"></i>查看</button>
							<button class="act-btn act-edit" type="button" v-if="item.sfsh!=='是'" @click="handleEdit(item)"><i class="el-icon-edit"></i>修改</button>
							<button class="act-btn act-del" type="button" @click="handleDelete(item)"><i class="el-icon-delete"></i>删除</button>
						</div>
					</div>
				</div>
			</div>
		</div>

		<el-pagination v-if="total > pageSize" background class="my-pager"
			:page-size="pageSize" :total="total" :current-page="page"
			layout="total, prev, pager, next"
			@current-change="pageChange">
		</el-pagination>
	</div>
</template>

<script>
export default {
	props: { embedded: { type: Boolean, default: false } },
	data() {
		return {
			tableData: [], total: 0, page: 1, pageSize: 10,
			loading: false, filterStatus: 'all',
			stats: { total: 0, pending: 0, approved: 0, rejected: 0 },
		}
	},
	created() { this.getList(); },
	methods: {
		stripHtml(html) {
			if (!html) return '';
			let t = html.replace(/<[^>]+>/g, '').replace(/&nbsp;/g, ' ');
			return t.length > 100 ? t.substring(0, 100) + '…' : t;
		},
		getList() {
			this.loading = true;
			let params = { page: this.page, limit: this.pageSize, parentid: 0, sort: 'addtime', order: 'desc' };
			if (this.filterStatus !== 'all') params.sfsh = this.filterStatus;
			this.$http.get('forum/page', { params }).then(res => {
				this.loading = false;
				if (res.data.code == 0) {
					this.tableData = res.data.data.list;
					this.total = Number(res.data.data.total);
				}
			}).catch(() => { this.loading = false; });
			this.$http.get('forum/page', { params: { page: 1, limit: 999, parentid: 0 } }).then(res => {
				if (res.data.code == 0) {
					let ls = res.data.data.list || [];
					this.stats.total = ls.length;
					this.stats.pending = ls.filter(i => i.sfsh === '待审核' || !i.sfsh).length;
					this.stats.approved = ls.filter(i => i.sfsh === '是').length;
					this.stats.rejected = ls.filter(i => i.sfsh === '否').length;
				}
			});
		},
		pageChange(v) { this.page = v; this.getList(); },
		publishPost() {
			let q = {}; if (this.embedded) q.centerType = 1;
			this.$router.push({ path: '/main/forumAdd', query: q });
		},
		toDetail(item) { this.$router.push({ path: '/main/forumDetail', query: { id: item.id } }); },
		handleEdit(item) {
			let q = { id: item.id }; if (this.embedded) q.centerType = 1;
			this.$router.push({ path: '/main/forumAdd', query: q });
		},
		handleDelete(item) {
			this.$confirm('确定删除该帖子？删除后不可恢复', '提示', { type: 'warning' }).then(() => {
				this.$http.post('forum/delete', [item.id]).then(res => {
					if (res.data.code == 0) { this.$message.success('删除成功'); this.getList(); }
				});
			}).catch(() => {});
		}
	}
}
</script>

<style lang="scss" scoped>
$cyan: #00d4ff;
$purple: #7c3aed;
$green: #22c55e;
$amber: #f59e0b;
$red: #ef4444;
$blue: #0ea5e9;
$text1: #f1f5f9;
$text2: #94a3b8;
$text3: #64748b;
$glass: rgba(255,255,255,0.03);
$glass-border: rgba(255,255,255,0.06);

.my-forum-wrap { padding: 0; }

/* ===== 顶部操作栏 ===== */
.top-bar {
	display: flex; justify-content: space-between; align-items: center;
	margin-bottom: 20px; gap: 16px; flex-wrap: wrap;
}
.stat-chips {
	display: flex; gap: 8px; flex-wrap: wrap;
}
.chip {
	display: flex; align-items: center; gap: 6px;
	padding: 8px 16px; border-radius: 20px;
	background: $glass; border: 1px solid $glass-border;
	cursor: pointer; transition: all 0.25s; user-select: none;
	.el-icon { font-size: 14px; color: $text3; transition: color 0.25s; vertical-align: middle; }
	.chip-label { font-size: 13px; color: $text2; transition: color 0.25s; }
	.chip-num {
		font-size: 12px; font-weight: 700; color: $text3;
		background: rgba(255,255,255,0.04); padding: 1px 8px; border-radius: 10px;
		min-width: 20px; text-align: center; transition: all 0.25s;
	}
	&:hover { border-color: rgba($cyan, 0.25); background: rgba($cyan, 0.04);
		.el-icon, .chip-label { color: $cyan; }
	}
	&.active {
		background: rgba($cyan, 0.08); border-color: rgba($cyan, 0.3);
		.el-icon { color: $cyan; }
		.chip-label { color: $text1; font-weight: 600; }
		.chip-num { background: rgba($cyan, 0.15); color: $cyan; }
	}
}
.publish-btn {
	display: flex; align-items: center; gap: 6px;
	padding: 9px 22px; border-radius: 22px; border: none;
	background: linear-gradient(135deg, $cyan, $purple);
	color: #fff; font-size: 13px; font-weight: 600;
	cursor: pointer; transition: all 0.3s; white-space: nowrap;
	box-shadow: 0 4px 16px rgba(0,212,255,0.2);
	.el-icon { font-size: 15px; vertical-align: middle; }
	&:hover { transform: translateY(-2px); box-shadow: 0 6px 24px rgba(0,212,255,0.3); }
}

/* ===== 空状态 ===== */
.empty-state {
	text-align: center; padding: 60px 20px;
	.empty-icon { margin-bottom: 16px; opacity: 0.8; }
	.empty-title { font-size: 16px; color: $text2; margin: 0 0 6px; font-weight: 500; }
	.empty-sub { font-size: 13px; color: $text3; margin: 0 0 20px; }
	.empty-btn {
		padding: 10px 28px; border-radius: 22px; border: 1px dashed rgba($cyan, 0.3);
		background: transparent; color: $cyan; font-size: 13px; cursor: pointer; transition: all 0.3s;
		.el-icon { margin-right: 4px; vertical-align: middle; }
		&:hover { background: rgba($cyan, 0.06); border-color: $cyan; transform: translateY(-2px); }
	}
}

/* ===== 帖子卡片列表 ===== */
.post-list { display: flex; flex-direction: column; gap: 14px; }

.post-card {
	display: flex; gap: 0; border-radius: 14px; overflow: hidden;
	background: $glass; border: 1px solid $glass-border;
	transition: all 0.3s; cursor: pointer; position: relative;
	&:hover {
		border-color: rgba($cyan, 0.2);
		background: rgba($cyan, 0.025);
		transform: translateX(4px);
		box-shadow: 0 4px 20px rgba(0,0,0,0.15), 0 0 0 1px rgba($cyan, 0.08);
		.post-cover img { transform: scale(1.05); }
		.post-title { color: $cyan; }
	}
}

.post-cover {
	width: 160px; flex-shrink: 0; overflow: hidden; position: relative;
	&::after {
		content: ''; position: absolute; inset: 0;
		background: linear-gradient(90deg, transparent 60%, rgba(15,23,42,0.5));
	}
	img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.5s; display: block; }
}

.post-body {
	flex: 1; min-width: 0; padding: 16px 20px;
	display: flex; flex-direction: column;
}

.post-top {
	display: flex; justify-content: space-between; align-items: flex-start; gap: 12px; margin-bottom: 8px;
}
.post-title {
	font-size: 15px; font-weight: 600; color: $text1; margin: 0;
	line-height: 1.4; transition: color 0.25s;
	overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.status-tag {
	font-size: 11px; padding: 3px 10px; border-radius: 10px; flex-shrink: 0;
	font-weight: 500; letter-spacing: 0.3px;
	&.st-pass { background: rgba($green, 0.1); color: $green; }
	&.st-wait { background: rgba($amber, 0.1); color: $amber; }
	&.st-fail { background: rgba($red, 0.1); color: $red; }
}

.post-excerpt {
	font-size: 13px; color: $text3; line-height: 1.6; margin: 0 0 10px;
	overflow: hidden; text-overflow: ellipsis;
	display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
}

.post-meta {
	display: flex; gap: 8px; flex-wrap: wrap; margin-bottom: 12px;
}
.meta-chip {
	font-size: 11px; color: $text3;
	display: inline-flex; align-items: center; gap: 3px;
	padding: 2px 10px; border-radius: 10px;
	background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.04);
	.el-icon { font-size: 12px; vertical-align: middle; }
	&.anon { color: $purple; border-color: rgba($purple, 0.15); background: rgba($purple, 0.06); }
	&.private { color: $amber; border-color: rgba($amber, 0.15); background: rgba($amber, 0.06); }
}

.post-bottom {
	display: flex; justify-content: space-between; align-items: center;
	margin-top: auto; padding-top: 10px;
	border-top: 1px solid rgba(255,255,255,0.04);
}
.post-stats {
	display: flex; gap: 16px;
	span {
		font-size: 12px; color: $text3;
		display: flex; align-items: center; gap: 5px;
		.el-icon { font-size: 14px; vertical-align: middle; }
	}
}
.post-actions { display: flex; gap: 6px; }
.act-btn {
	display: inline-flex; align-items: center; gap: 3px;
	font-size: 12px; padding: 4px 12px; border-radius: 14px;
	border: 1px solid transparent; background: transparent;
	cursor: pointer; transition: all 0.2s;
	.el-icon { font-size: 13px; vertical-align: middle; }
	&.act-view { color: $cyan;
		&:hover { background: rgba($cyan, 0.08); border-color: rgba($cyan, 0.2); }
	}
	&.act-edit { color: $amber;
		&:hover { background: rgba($amber, 0.08); border-color: rgba($amber, 0.2); }
	}
	&.act-del { color: $text3;
		&:hover { color: $red; background: rgba($red, 0.08); border-color: rgba($red, 0.2); }
	}
}

/* ===== 分页 ===== */
.my-pager {
	display: flex; justify-content: center; margin-top: 20px;
	::v-deep .el-pager li {
		background: rgba(255,255,255,0.04) !important; color: $text2 !important;
		border: none !important; border-radius: 8px !important; min-width: 32px;
		&.active { background: linear-gradient(135deg, $cyan, $purple) !important; color: #fff !important; }
	}
	::v-deep .btn-prev, ::v-deep .btn-next {
		background: rgba(255,255,255,0.04) !important; color: $text2 !important;
		border-radius: 8px !important;
	}
	::v-deep .el-pagination__total { color: $text3 !important; font-size: 12px; }
}
</style>
