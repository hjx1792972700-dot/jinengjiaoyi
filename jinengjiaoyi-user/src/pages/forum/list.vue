<template>
	<div>
		<div class="breadcrumb-preview">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2"><a>技能社区</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div class="forum-wrap">
			<!-- 技能社区主题横幅 -->
			<div class="forum-banner">
				<div class="forum-banner-bg">
					<svg viewBox="0 0 1200 100" preserveAspectRatio="none" class="banner-wave">
						<circle cx="150" cy="50" r="45" fill="rgba(14,165,233,0.06)"/>
						<circle cx="1050" cy="30" r="35" fill="rgba(124,58,237,0.05)"/>
						<path d="M0,50 C200,20 400,80 600,50 C800,20 1000,80 1200,50" fill="none" stroke="rgba(14,165,233,0.12)" stroke-width="1.5"/>
					</svg>
				</div>
				<div class="forum-banner-content">
					<div class="forum-banner-icon">
						<svg viewBox="0 0 40 40" width="36" height="36">
							<rect x="4" y="6" width="14" height="14" rx="3" fill="none" stroke="#0ea5e9" stroke-width="1.5"/>
							<rect x="22" y="6" width="14" height="14" rx="3" fill="none" stroke="#7c3aed" stroke-width="1.5"/>
							<rect x="4" y="24" width="14" height="14" rx="3" fill="none" stroke="#7c3aed" stroke-width="1.5"/>
							<rect x="22" y="24" width="14" height="14" rx="3" fill="none" stroke="#0ea5e9" stroke-width="1.5"/>
							<line x1="18" y1="13" x2="22" y2="13" stroke="rgba(255,255,255,0.3)" stroke-width="1" stroke-dasharray="2,2"/>
							<line x1="11" y1="20" x2="11" y2="24" stroke="rgba(255,255,255,0.3)" stroke-width="1" stroke-dasharray="2,2"/>
							<line x1="29" y1="20" x2="29" y2="24" stroke="rgba(255,255,255,0.3)" stroke-width="1" stroke-dasharray="2,2"/>
							<line x1="18" y1="31" x2="22" y2="31" stroke="rgba(255,255,255,0.3)" stroke-width="1" stroke-dasharray="2,2"/>
						</svg>
					</div>
					<div class="forum-banner-text">
						<h3>技能社区</h3>
						<p>分享你的技能故事，发现志同道合的伙伴</p>
					</div>
				</div>
			</div>
			<div class="category-bar">
				<div class="cate-left">
					<div class="cate-item" :class="{active: categoryIndex === 0}" @click="categoryClick(0)">全部</div>
					<div v-for="(item,index) in categoryList" :key="index"
						class="cate-item" :class="{active: categoryIndex === index+1}"
						@click="categoryClick(index+1)">{{item.typename}}</div>
				</div>
				<div class="cate-right">
					<div class="inline-search">
						<input class="inline-search-input" v-model="title" placeholder="搜索帖子标题..." @keydown.enter="getForumList(1)" />
						<button class="inline-search-btn" type="button" @click="getForumList(1)"><i class="el-icon-search"></i></button>
					</div>
					<button class="publish-btn" type="button" @click="toForumAdd">
						<i class="el-icon-edit"></i>
						发布帖子
					</button>
				</div>
			</div>

			<div class="forum-grid" v-if="forumList.length">
				<div class="forum-card" v-for="item in forumList" :key="item.id" @click="toForumDetail(item)">
					<div class="card-cover">
						<img v-if="item.cover" :src="$config.baseUrl + item.cover.split(',')[0]" />
						<div v-else class="no-cover-placeholder"><i class="el-icon-picture"></i></div>
						<div class="cover-overlay"></div>
						<span class="card-tag" v-if="item.typename">{{item.typename}}</span>
					</div>
					<div class="card-body">
						<h3 class="card-title">{{item.title}}</h3>
						<p class="card-intro" v-if="item.content">{{item.content.replace(/<[^>]+>/g,'').substring(0,60)}}</p>
						<div class="card-meta">
							<div class="meta-user">
								<i class="el-icon-user"></i>
								<span>{{item.isanon==1&&item.userid!=userid?'匿名':item.username}}</span>
							</div>
							<div class="meta-time">
								<i class="el-icon-time"></i>
								<span>{{item.addtime ? item.addtime.split(' ')[0] : ''}}</span>
							</div>
						</div>
					</div>
				</div>
			</div>

			<div class="empty-state" v-else>
				<el-icon class="empty-state-icon"><ChatDotRound /></el-icon>
				<p>暂无帖子，快来发布第一个吧</p>
			</div>

			<el-pagination
				v-if="total > pageSize"
				background
				class="pagination"
				:pager-count="7"
				:page-size="pageSize"
				:page-sizes="pageSizes"
				prev-text="<"
				next-text=">"
				:hide-on-single-page="true"
				:layout='["total","prev","pager","next","sizes","jumper"].join()'
				:total="total"
				@current-change="curChange"
				@size-change="sizeChange"
				@prev-click="prevClick"
				@next-click="nextClick"
			></el-pagination>
		</div>

	</div>
</template>

<script>
	export default {

		data() {
			return {
				formSearch: {},
				title: '',
				forumList: [],
				total: 1,
				pageSize: 8,
				pageSizes: [],
				totalPage: 1,
				userid: Number(localStorage.getItem('frontUserid')),
				categoryIndex: 0,
				categoryList: [],
			}
		},
		created() {
			this.getCategoryList()
		},
		watch:{
			$route(){
				this.getCategoryList()
			}
		},
		methods: {
			getCategoryList(){
				this.$http.get('forumtype/list', {params: { limit: 1000 }}).then(res => {
					if (res.data.code == 0) {
						this.categoryList = res.data.data.list
						if(this.$route.query.homeFenlei){
							for(let i=0;i<this.categoryList.length;i++) {
								if(this.$route.query.homeFenlei == this.categoryList[i].typename) {
									this.categoryIndex = i + 1;
									const currentRoute = this.$route;
									const routeWithoutQuery = { ...currentRoute };
									delete routeWithoutQuery.query;
									this.$router.replace(routeWithoutQuery)
									break;
								}
							}
						}
						this.getForumList(1);
					}
				});
			},
			categoryClick(index) {
				this.categoryIndex = index
				this.getForumList(1)
			},
			getForumList(page) {
				let params = {
					page,
					limit: this.pageSize,
					isdone: '开放',
					sort: 'istop,toptime',
					order: 'desc,desc',
					delflag: 0,
					sfsh: '是',
					parentid: 0
				};
				let searchWhere = {};
				if(this.title != '') searchWhere.title = '%' + this.title + '%';
				if(this.categoryIndex!=0){
					searchWhere.typename = this.categoryList[this.categoryIndex - 1].typename
				}
				this.$http.get('forum/flist', {params: Object.assign(params, searchWhere)}).then(res => {
					if (res.data.code == 0) {
						this.forumList = res.data.data.list;
						this.total = res.data.data.total;
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						if(this.pageSizes.length==0){
							this.pageSizes = [8, 16, 24];
						}
					}
				});
			},
			curChange(page) {
				this.getForumList(page);
			},
			prevClick(page) {
				this.getForumList(page);
			},
			sizeChange(size){
				this.pageSize = size
				this.getForumList(1);
			},
			nextClick(page) {
				this.getForumList(page);
			},
			toForumAdd() {
				this.$router.push('/main/forumAdd');
			},
			toForumDetail(item) {
				this.$router.push({path: '/main/forumDetail', query: {id: item.id}});
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$primary-light: #38bdf8;
$primary-dark: #0284c7;
$purple: #7c3aed;
$text-dark: #e2e8f0;
$text-gray: #94a3b8;
$bg: #0c1222;
$white: #0f172a;
$radius: 12px;

.forum-wrap {
	max-width: 1200px;
	margin: 0 auto;
	padding: 0 20px 40px;
}

.forum-banner {
	position: relative;
	border-radius: 14px;
	overflow: hidden;
	background: linear-gradient(135deg, #0f172a 0%, #1e3a5f 60%, rgba(124,58,237,0.8) 100%);
	margin-bottom: 20px;

	.forum-banner-bg {
		position: absolute;
		inset: 0;
		.banner-wave { width: 100%; height: 100%; }
	}

	.forum-banner-content {
		position: relative;
		z-index: 1;
		display: flex;
		align-items: center;
		gap: 14px;
		padding: 20px 24px;

		.forum-banner-icon {
			flex-shrink: 0;
			width: 48px; height: 48px;
			border-radius: 12px;
			background: rgba(255,255,255,0.08);
			display: flex;
			align-items: center;
			justify-content: center;
			backdrop-filter: blur(4px);
		}

		.forum-banner-text {
			h3 {
				margin: 0;
				font-size: 18px;
				font-weight: 700;
				color: #fff;
				letter-spacing: 2px;
			}
			p {
				margin: 4px 0 0;
				font-size: 12px;
				color: rgba(255,255,255,0.55);
				letter-spacing: 0.5px;
			}
		}
	}
}

.category-bar {
	display: flex;
	align-items: center;
	justify-content: space-between;
	gap: 12px;
	flex-wrap: wrap;
	margin-bottom: 18px;

	.cate-left {
		display: flex;
		align-items: center;
		gap: 6px;
		flex-wrap: wrap;
	}

	.cate-right {
		display: flex;
		align-items: center;
		gap: 10px;
		flex-shrink: 0;
	}

	.cate-item {
		padding: 6px 18px;
		font-size: 13px;
		color: $text-gray;
		cursor: pointer;
		border-radius: 18px;
		transition: all 0.25s;
		font-weight: 500;
		border: 1px solid transparent;

		&:hover {
			color: $primary;
			background: $bg;
		}

		&.active {
			color: $white;
			background: linear-gradient(135deg, $primary, $purple);
			border-color: transparent;
			box-shadow: 0 2px 8px rgba(14,165,233,0.2);
		}
	}

	.inline-search {
		display: flex;
		align-items: center;
		border: 1px solid #e2e8f0;
		border-radius: 20px;
		overflow: hidden;
		background: $white;
		transition: all 0.3s;
		&:focus-within {
			border-color: $primary;
			box-shadow: 0 0 0 2px rgba(14, 165, 233, 0.1);
		}
	}

	.inline-search-input {
		border: none;
		outline: none;
		padding: 6px 14px;
		font-size: 13px;
		color: $text-dark;
		background: transparent;
		width: 180px;
		&::placeholder { color: #bbb; }
	}

	.inline-search-btn {
		border: none;
		background: linear-gradient(135deg, $primary, $primary-dark);
		color: $white;
		padding: 6px 16px;
		cursor: pointer;
		font-size: 14px;
		transition: all 0.2s;
		&:hover { opacity: 0.85; }
		.el-icon { font-size: 14px; vertical-align: middle; }
	}

	.publish-btn {
		display: flex;
		align-items: center;
		gap: 5px;
		height: 32px;
		padding: 0 18px;
		background: linear-gradient(135deg, $primary, $purple);
		color: $white;
		border: none;
		border-radius: 22px;
		font-size: 13px;
		font-weight: 500;
		cursor: pointer;
		transition: all 0.25s;
		flex-shrink: 0;

		.el-icon { font-size: 15px; vertical-align: middle; }

		&:hover {
			transform: translateY(-1px);
			box-shadow: 0 4px 16px rgba(14,165,233,0.3);
		}
	}
}

.forum-grid {
	display: grid;
	grid-template-columns: repeat(4, 1fr);
	gap: 16px;
}

.forum-card {
	background: $white;
	border-radius: $radius;
	overflow: hidden;
	box-shadow: 0 2px 10px rgba(0,0,0,.06);
	cursor: pointer;
	transition: all 0.3s ease;

	&:hover {
		transform: translateY(-4px);
		box-shadow: 0 8px 24px rgba(0,0,0,.1);

		.card-cover img {
			transform: scale(1.06);
		}

		.card-title {
			color: $primary;
		}
	}

	.card-cover {
		position: relative;
		overflow: hidden;
		height: 150px;

		img {
			width: 100%;
			height: 100%;
			object-fit: cover;
			transition: transform 0.4s ease;
		}
		.no-cover-placeholder {
			width: 100%; height: 100%; display: flex; align-items: center; justify-content: center;
			background: linear-gradient(135deg, #e2e8f0, #cbd5e1);
			.el-icon { font-size: 36px; color: #94a3b8; }
		}

		.cover-overlay {
			position: absolute;
			bottom: 0;
			left: 0;
			right: 0;
			height: 50px;
			background: linear-gradient(transparent, rgba(0,0,0,0.3));
			pointer-events: none;
		}

		.card-tag {
			position: absolute;
			top: 10px;
			left: 10px;
			padding: 2px 10px;
			background: linear-gradient(135deg, rgba(14,165,233,0.85), rgba(124,58,237,0.85));
			color: $white;
			font-size: 11px;
			border-radius: 10px;
			font-weight: 500;
			letter-spacing: 0.5px;
			backdrop-filter: blur(4px);
		}
	}

	.card-body {
		padding: 12px 14px 14px;
	}

	.card-title {
		font-size: 14px;
		font-weight: 600;
		color: $text-dark;
		margin: 0 0 6px;
		line-height: 1.4;
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
		transition: color 0.25s;
	}

	.card-intro {
		font-size: 12px;
		color: #aaa;
		line-height: 1.6;
		margin: 0 0 10px;
		overflow: hidden;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}

	.card-meta {
		display: flex;
		align-items: center;
		justify-content: space-between;
		font-size: 11px;
		color: #bbb;

		.meta-user, .meta-time {
			display: flex;
			align-items: center;
			gap: 3px;

			.el-icon { font-size: 12px; color: $primary-light; }
		}
	}
}

.empty-state {
	text-align: center;
	padding: 60px 20px;
	color: $text-gray;

	.empty-state-icon {
		font-size: 48px;
		display: block;
		margin-bottom: 12px;
		color: #ddd;
	}

	p { font-size: 14px; }
}

.pagination {
	margin-top: 24px;
	display: flex;
	justify-content: center;

	::v-deep .el-pager li.active {
		background: linear-gradient(135deg, $primary, $purple);
	}
}

@media (max-width: 1100px) {
	.forum-grid { grid-template-columns: repeat(3, 1fr); }
}

@media (max-width: 768px) {
	.forum-grid { grid-template-columns: repeat(2, 1fr); }
	.category-bar { flex-direction: column; align-items: stretch; }
	.category-bar .cate-left { justify-content: center; }
	.category-bar .cate-right { justify-content: center; }
}
</style>
