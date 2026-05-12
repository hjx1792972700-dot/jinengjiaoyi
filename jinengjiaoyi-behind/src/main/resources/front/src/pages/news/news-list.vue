<template>
	<div>
		<div class="breadcrumb-preview">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a>{{item.name}}</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<div class="news-page">
			<!-- 公告资讯主题横幅 -->
			<div class="page-theme-banner news-banner">
				<div class="ptb-bg">
					<svg viewBox="0 0 1200 80" preserveAspectRatio="none" style="width:100%;height:100%">
						<circle cx="150" cy="40" r="35" fill="rgba(14,165,233,0.06)"/>
						<circle cx="1050" cy="35" r="40" fill="rgba(14,165,233,0.04)"/>
						<path d="M0,40 Q300,10 600,40 T1200,40" fill="none" stroke="rgba(14,165,233,0.1)" stroke-width="1.5"/>
					</svg>
				</div>
				<div class="ptb-content">
					<div class="ptb-icon">
						<svg viewBox="0 0 36 36" width="32" height="32">
							<path d="M8 6h20a2 2 0 012 2v20a2 2 0 01-2 2H8a2 2 0 01-2-2V8a2 2 0 012-2z" fill="none" stroke="rgba(14,165,233,0.4)" stroke-width="1.5"/>
							<circle cx="13" cy="14" r="3" fill="#0ea5e9" opacity="0.6"/>
							<line x1="20" y1="12" x2="26" y2="12" stroke="#0ea5e9" stroke-width="1.5" stroke-linecap="round"/>
							<line x1="20" y1="16" x2="24" y2="16" stroke="#7c3aed" stroke-width="1.5" stroke-linecap="round"/>
							<line x1="10" y1="22" x2="26" y2="22" stroke="#0ea5e9" stroke-width="1.5" stroke-linecap="round" opacity="0.5"/>
							<line x1="10" y1="26" x2="22" y2="26" stroke="#7c3aed" stroke-width="1.5" stroke-linecap="round" opacity="0.4"/>
						</svg>
					</div>
					<div class="ptb-text">
						<h3>公告资讯</h3>
						<p>了解平台最新动态与技能交易资讯</p>
					</div>
				</div>
			</div>
			<div class="top-bar">
				<div class="category-tabs">
					<span class="tab" :class="{active: categoryIndex === 0}" @click="categoryClick(0)">全部</span>
					<span v-for="(item,index) in categoryList" :key="index"
						class="tab" :class="{active: categoryIndex === index+1}"
						@click="categoryClick(index+1)">{{item.typename}}</span>
				</div>
				<div class="search-row">
					<div class="inline-search">
						<input class="inline-search-input" v-model="title" placeholder="搜索公告资讯..." @keydown.enter="getNewsList(1)" />
						<button class="inline-search-btn" type="button" @click="getNewsList(1)"><el-icon><Search /></el-icon></button>
					</div>
				</div>
			</div>

			<div class="carousel-section" v-if="newsList.length > 0">
				<el-carousel
					ref="newsCarousel"
					:interval="5000"
					height="420px"
					arrow="always"
					indicator-position="none"
					@change="onSlideChange">
					<el-carousel-item v-for="(item, index) in newsList" :key="index">
						<div class="slide">
							<div class="slide-image">
								<img :src="baseUrl + (item.picture ? item.picture.split(',')[0] : '')" :alt="item.title">
								<div class="image-overlay"></div>
							</div>
							<div class="slide-content">
								<span class="badge">{{item.typename}}</span>
								<h2 class="slide-title">{{item.title}}</h2>
								<p class="slide-desc">{{item.introduction}}</p>
								<div class="slide-meta">
									<span class="meta-item"><el-icon><User /></el-icon> {{item.name}}</span>
									<span class="meta-item"><el-icon><Clock /></el-icon> {{item.addtime ? item.addtime.split(' ')[0] : ''}}</span>
								</div>
								<div class="read-btn" @click="toNewsDetail(item)">阅读全文 <el-icon><ArrowRight /></el-icon></div>
							</div>
						</div>
					</el-carousel-item>
				</el-carousel>

				<div class="slide-indicators">
					<div v-for="(item, index) in newsList" :key="index"
						class="indicator-item" :class="{active: currentSlide === index}"
						@click="goToSlide(index)">
						<span class="indicator-num">{{(index + 1) < 10 ? '0' + (index + 1) : (index + 1)}}</span>
						<span class="indicator-title">{{item.title}}</span>
						<div class="indicator-progress" v-if="currentSlide === index"></div>
					</div>
				</div>
			</div>

			<div v-else class="empty-state">
				<el-icon class="empty-doc-icon"><Document /></el-icon>
				<p>暂无公告资讯</p>
			</div>

			<el-pagination
				v-if="total > pageSize"
				background
				id="pagination" class="pagination"
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
	import { Search, User, Clock, ArrowRight, Document } from '@element-plus/icons-vue'
	export default {
		components: { Search, User, Clock, ArrowRight, Document },
		data() {
			return {
				baseUrl: '',
				breadcrumbItem: [
				  {
					name: '公告资讯'
				  }
				],
				newsList: [],
				total: 1,
				pageSize: 6,
				pageSizes: [],
				totalPage: 1,
				layouts: '',
				title: '',
				categoryIndex: 0,
				categoryList: [],
				currentSlide: 0,
				formSearch: {},
			}
		},
		created() {
			this.baseUrl = this.$config.baseUrl
			this.getCategoryList()
		},
		watch:{
			$route(newValue){
				this.getCategoryList()
			}
		},
		methods: {
			getCategoryList(){
				this.$http.get('newstype/list', {params: {sort: 'typename',order: 'desc'}}).then(res => {
					if (res.data.code == 0) {
						this.categoryList = res.data.data.list;
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
						this.getNewsList(1);
					}
				});
			},
			categoryClick(index) {
				this.categoryIndex = index
				this.getNewsList()
			},
			getNewsList(page) {
				let params = {page, limit: this.pageSize,sort:'addtime',order:'desc'};
				let searchWhere = {};
				if(this.title != '') searchWhere.title = '%' + this.title + '%';
				if(this.categoryIndex!=0){
					searchWhere.typename = this.categoryList[this.categoryIndex - 1].typename
				}
				this.$http.get('news/list', {params: Object.assign(params, searchWhere)}).then(res => {
					if (res.data.code == 0) {
						this.newsList = res.data.data.list;
						this.total = res.data.data.total;
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						this.currentSlide = 0;
						if(this.pageSizes.length==0){
							this.pageSizes = [6, 12, 24];
						}
					}
				});
			},
			onSlideChange(index) {
				this.currentSlide = index;
			},
			goToSlide(index) {
				this.$refs.newsCarousel.setActiveItem(index);
			},
			curChange(page) {
				this.getNewsList(page);
			},
			sizeChange(size){
				this.pageSize = size
				this.getNewsList(1);
			},
			prevClick(page) {
				this.getNewsList(page);
			},
			nextClick(page) {
				this.getNewsList(page);
			},
			toNewsDetail(item) {
				this.$router.push({path: '/main/newsDetail', query: {id: item.id}});
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$primary-light: #38bdf8;
$primary-dark: #7a6235;
$text-dark: #e2e8f0;
$text-gray: #94a3b8;
$bg-light: #0c1222;
$white: #0f172a;
$shadow: 0 4px 20px rgba(0,0,0,0.3);
$radius: 12px;

.page-theme-banner {
	width: 100%;
	border-radius: 14px;
	overflow: hidden;
	position: relative;
	margin: 0 0 20px;

	&.news-banner { background: linear-gradient(135deg, #0f172a 0%, #0c2d48 60%, rgba(14,165,233,0.7) 100%); }

	.ptb-bg { position: absolute; inset: 0; }
	.ptb-content {
		position: relative; z-index: 1;
		display: flex; align-items: center; gap: 14px; padding: 16px 24px;

		.ptb-icon {
			flex-shrink: 0; width: 44px; height: 44px; border-radius: 10px;
			background: rgba(255,255,255,0.08); display: flex; align-items: center; justify-content: center;
		}
		.ptb-text {
			h3 { margin: 0; font-size: 17px; font-weight: 700; color: #fff; letter-spacing: 2px; }
			p { margin: 3px 0 0; font-size: 12px; color: rgba(255,255,255,0.5); letter-spacing: 0.5px; }
		}
	}
}

.news-page {
	max-width: 1100px;
	margin: 0 auto;
	padding: 24px 20px 40px;
}

.top-bar {
	margin-bottom: 28px;
	display: flex;
	align-items: center;
	justify-content: space-between;
	gap: 12px;
	flex-wrap: wrap;
}

.search-row {
	display: flex;
	align-items: center;
	flex-shrink: 0;
}

.inline-search {
	display: flex;
	align-items: center;
	border: 1px solid #e2e8f0;
	border-radius: 20px;
	overflow: hidden;
	background: #fff;
	transition: all 0.3s;
	&:focus-within {
		border-color: $primary;
		box-shadow: 0 0 0 2px rgba(14, 165, 233, 0.1);
	}
}

.inline-search-input {
	border: none;
	outline: none;
	padding: 8px 16px;
	font-size: 14px;
	color: #333;
	background: transparent;
	width: 260px;
	&::placeholder { color: #bbb; }
}

.inline-search-btn {
	border: none;
	background: linear-gradient(135deg, $primary, #0284c7);
	color: $white;
	padding: 8px 20px;
	cursor: pointer;
	font-size: 15px;
	transition: all 0.2s;
	.el-icon { vertical-align: middle; font-size: 16px; }
	&:hover { opacity: 0.85; }
}

.category-tabs {
	display: flex;
	align-items: center;
	gap: 6px;
	flex-wrap: wrap;

	.tab {
		padding: 8px 22px;
		font-size: 14px;
		color: $text-gray;
		cursor: pointer;
		border-radius: 20px;
		transition: all 0.3s;
		font-weight: 500;
		border: 1px solid transparent;

		&:hover {
			color: $primary;
			background: $bg-light;
		}

		&.active {
			color: $white;
			background: $primary;
			border-color: $primary;
		}
	}
}

.carousel-section {
	background: $white;
	border-radius: $radius;
	box-shadow: $shadow;
	overflow: hidden;
}

::v-deep .el-carousel {
	border-radius: $radius $radius 0 0;

	.el-carousel__arrow {
		width: 44px;
		height: 44px;
		font-size: 18px;
		background: rgba(255,255,255,0.9);
		color: $primary;
		box-shadow: 0 2px 12px rgba(0,0,0,0.15);
		transition: all 0.3s;

		&:hover {
			background: $primary;
			color: $white;
			transform: scale(1.05);
		}
	}

	.el-carousel__arrow--left {
		left: 20px;
	}

	.el-carousel__arrow--right {
		right: 20px;
	}
}

.slide {
	display: flex;
	height: 420px;
	background: $white;

	.slide-image {
		width: 55%;
		position: relative;
		overflow: hidden;
		flex-shrink: 0;

		img {
			width: 100%;
			height: 100%;
			object-fit: cover;
			transition: transform 0.6s ease;
		}

		.image-overlay {
			position: absolute;
			top: 0;
			right: 0;
			bottom: 0;
			width: 80px;
			background: linear-gradient(to left, $white, transparent);
		}
	}

	&:hover .slide-image img {
		transform: scale(1.03);
	}

	.slide-content {
		flex: 1;
		padding: 40px 36px;
		display: flex;
		flex-direction: column;
		justify-content: center;

		.badge {
			display: inline-block;
			padding: 4px 14px;
			background: $bg-light;
			color: $primary;
			font-size: 12px;
			font-weight: 600;
			border-radius: 12px;
			margin-bottom: 16px;
			width: fit-content;
			letter-spacing: 1px;
		}

		.slide-title {
			font-size: 24px;
			font-weight: 700;
			color: $text-dark;
			margin: 0 0 16px;
			line-height: 1.4;
			display: -webkit-box;
			-webkit-line-clamp: 2;
			-webkit-box-orient: vertical;
			overflow: hidden;
		}

		.slide-desc {
			font-size: 14px;
			color: $text-gray;
			line-height: 1.8;
			margin: 0 0 24px;
			display: -webkit-box;
			-webkit-line-clamp: 3;
			-webkit-box-orient: vertical;
			overflow: hidden;
		}

		.slide-meta {
			display: flex;
			gap: 20px;
			margin-bottom: 24px;

			.meta-item {
				font-size: 13px;
				color: #aaa;

				.el-icon {
					margin-right: 4px;
					vertical-align: middle;
					color: $primary-light;
				}
			}
		}

		.read-btn {
			display: inline-flex;
			align-items: center;
			gap: 6px;
			padding: 10px 24px;
			background: $primary;
			color: $white;
			border-radius: 24px;
			font-size: 14px;
			font-weight: 500;
			width: fit-content;
			cursor: pointer;
			transition: all 0.3s;

			&:hover {
				background: $primary-dark;
				transform: translateX(4px);
			}

			.el-icon {
				transition: transform 0.3s;
				vertical-align: middle;
			}

			&:hover .el-icon {
				transform: translateX(4px);
			}
		}
	}
}

.slide-indicators {
	display: flex;
	border-top: 1px solid #f0ebe3;
	background: $white;

	.indicator-item {
		flex: 1;
		padding: 14px 12px;
		cursor: pointer;
		position: relative;
		transition: all 0.3s;
		text-align: center;
		overflow: hidden;
		border-right: 1px solid #f0ebe3;

		&:last-child {
			border-right: none;
		}

		&:hover {
			background: $bg-light;
		}

		&.active {
			background: $bg-light;

			.indicator-num {
				color: $primary;
			}
			.indicator-title {
				color: $text-dark;
			}
		}

		.indicator-num {
			display: block;
			font-size: 18px;
			font-weight: 700;
			color: #ddd;
			margin-bottom: 4px;
			transition: color 0.3s;
		}

		.indicator-title {
			display: block;
			font-size: 12px;
			color: $text-gray;
			white-space: nowrap;
			overflow: hidden;
			text-overflow: ellipsis;
			transition: color 0.3s;
		}

		.indicator-progress {
			position: absolute;
			bottom: 0;
			left: 0;
			height: 3px;
			background: $primary;
			animation: progress-fill 5s linear;
		}
	}
}

@keyframes progress-fill {
	from { width: 0; }
	to { width: 100%; }
}

.empty-state {
	text-align: center;
	padding: 80px 20px;
	color: $text-gray;

	.empty-doc-icon {
		font-size: 48px;
		margin-bottom: 16px;
		display: block;
		color: #ddd;
	}

	p {
		font-size: 16px;
	}
}

.pagination {
	margin-top: 30px;
	display: flex;
	justify-content: center;

	::v-deep .el-pager li.active {
		background: $primary;
	}

	::v-deep .btn-prev,
	::v-deep .btn-next {
		border-radius: 6px;
	}
}
</style>
