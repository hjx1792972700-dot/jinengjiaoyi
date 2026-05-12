<template>
	<div class="news-detail-page">
		<div class="nd-topbar">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" to="/main/news"><a>公告资讯</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item3"><a href="javascript:void(0);">详情</a></el-breadcrumb-item>
			</el-breadcrumb>
			<el-button class="back-btn" size="small" @click="backClick">
				<i class="el-icon-arrow-left"></i>
				返回
			</el-button>
		</div>

		<div class="nd-scroll">
			<div class="nd-card">
				<h1 class="nd-title">{{detail.title}}</h1>
				<div class="nd-meta">
					<span class="nd-meta-item" v-if="detail.addtime">
						<i class="el-icon-time"></i> {{detail.addtime.split(' ')[0]}}
					</span>
					<span class="nd-meta-item" v-if="detail.name">
						<i class="el-icon-user"></i> {{detail.name}}
					</span>
					<span class="nd-meta-item" v-if="detail.typename">
						<i class="el-icon-collection-tag"></i> {{detail.typename}}
					</span>
				</div>

				<div class="nd-cover" v-if="detail.picture">
					<img :src="baseUrl + (detail.picture ? detail.picture.split(',')[0] : '')">
				</div>

				<div class="nd-content ql-snow ql-editor" v-html="detail.content"></div>
			</div>

			<div class="nd-nav">
				<div class="nd-nav-btn" @click="prepDetailClick">
					<i class="el-icon-arrow-left"></i>
					<span>上一篇：{{currentIndex==0?'没有了':allList[currentIndex - 1].title}}</span>
				</div>
				<div class="nd-nav-btn" @click="nextDetailClick">
					<span>下一篇：{{currentIndex==allList.length - 1?'没有了':allList[currentIndex + 1].title}}</span>
					<i class="el-icon-arrow-right"></i>
				</div>
			</div>

			<div class="nd-tags" v-if="categoryList.length">
				<span class="nd-tag" v-for="(item,index) in categoryList" :key="index">{{ item.typename }}</span>
			</div>
		</div>
	</div>
</template>

<script>
	export default {

		data() {
			return {
				id: 0,
				detail: {},
				baseUrl:'',
				categoryList: [],
				currentIndex: 0,
				allList: [],
				sensitiveWordsArr: [],
				storeupType: false,
			}
		},
		created() {
			if(this.$route.query.storeupType&&this.$route.query.storeupType!=0) {
				this.storeupType = true
			}
			this.id = this.$route.query.id
			this.baseUrl = this.$config.baseUrl;
			this.getCategoryList()
			this.getDetail()
			this.getNewsList()
			this.getSensitiveWords()
		},
		watch:{
			$route(newValue) {
				this.id = this.$route.query.id
			    this.getDetail()
			}
		},
		methods: {
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			backClick() {
				if(this.storeupType){
					history.back()
				}else {
					this.$router.push({path: '/main/news'});
				}
			},
			getNewsList() {
				let params = {page:1, limit: 100,sort:'addtime',order:'desc'};
				this.$http.get('news/list', {params: params}).then(res => {
					if (res.data.code == 0) {
						for(let x in res.data.data.list){
							if(res.data.data.list[x].id == this.id){
								this.currentIndex = Number(x)
								break
							}
						}
						this.allList = res.data.data.list
					}
				});
			},
			prepDetailClick(){
				if(this.currentIndex == 0){
					this.$message.error('已经是第一篇了')
					return false
				}
				this.currentIndex--
				this.$router.push({path: '/main/newsDetail', query: {id: this.allList[this.currentIndex].id}});
			},
			nextDetailClick(){
				if(this.currentIndex == this.allList.length - 1){
					this.$message.error('已经是最后一篇了')
					return false
				}
				this.currentIndex++
				this.$router.push({path: '/main/newsDetail', query: {id: this.allList[this.currentIndex].id}});
			},
			getCategoryList(){
				this.$http.get('newstype/list', {}).then(res => {
					if (res.data.code == 0) {
						this.categoryList = res.data.data;
					}
				});
			},
			toDetail(id){
				this.$router.push({path: '/main/newsDetail', query: {id: id}});
			},
			getDetail(){
				this.$http.get(`news/detail/${this.id}`,{}).then(res=>{
					if(res.data&&res.data.code==0){
						this.detail = res.data.data
					}
				})
			},
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$primary-light: #38bdf8;
$text-dark: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: #64748b;
$border: rgba(14,165,233,0.12);
$bg: #0c1222;
$white: #0f172a;
$radius: 14px;

.news-detail-page {
	height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
	overflow: hidden;
	padding: 12px 24px 0;
	max-width: 900px;
	margin: 0 auto;
	width: 100%;
	background: $bg;
}

.nd-topbar {
	display: flex; align-items: center; justify-content: space-between;
	margin-bottom: 10px; flex-shrink: 0;
	::v-deep .el-breadcrumb__inner, ::v-deep .el-breadcrumb__inner a { color: $text-dim !important; font-size: 13px; }
	::v-deep .el-breadcrumb__separator { color: rgba(255,255,255,0.3) !important; }
	.back-btn {
		border-radius: 20px; border: 1px solid rgba(255,255,255,0.15); color: rgba(255,255,255,0.6);
		background: rgba(255,255,255,0.05); font-size: 12px; padding: 5px 14px;
		&:hover { color: #fff; border-color: rgba(255,255,255,0.3); background: rgba(255,255,255,0.1); }
	}
}

.nd-scroll {
	flex: 1; min-height: 0; overflow-y: auto; padding-bottom: 20px;
	&::-webkit-scrollbar { width: 4px; }
	&::-webkit-scrollbar-thumb { background: rgba(14,165,233,0.2); border-radius: 4px; }
}

.nd-card {
	background: $white; border-radius: $radius;
	border: 1px solid $border; padding: 28px 32px; margin-bottom: 14px;
}

.nd-title {
	font-size: 22px; font-weight: 700; color: $text-dark;
	margin: 0 0 14px; line-height: 1.4; text-align: center;
}

.nd-meta {
	display: flex; justify-content: center; gap: 20px;
	padding-bottom: 16px; margin-bottom: 20px;
	border-bottom: 1px solid $border;
	.nd-meta-item {
		font-size: 13px; color: $text-sub;
		display: flex; align-items: center; gap: 5px;
		.el-icon { font-size: 14px; color: $primary; opacity: 0.6; vertical-align: middle; }
	}
}

.nd-cover {
	margin-bottom: 20px; border-radius: 10px; overflow: hidden;
	img { width: 100%; display: block; border-radius: 10px; }
}

.nd-content {
	color: $text-sub; font-size: 15px; line-height: 1.9; padding: 0 !important;
	::v-deep img { max-width: 100%; border-radius: 8px; margin: 10px 0; }
	::v-deep h3 { color: $text-dark; font-size: 17px; margin: 18px 0 10px; }
	::v-deep p { margin: 0 0 10px; }
	::v-deep ul, ::v-deep ol { padding-left: 20px; margin: 8px 0; }
	::v-deep li { margin-bottom: 4px; }
	::v-deep table { width: 100%; border-collapse: collapse; margin: 12px 0; font-size: 13px;
		th, td { padding: 8px 12px; border: 1px solid $border; color: $text-sub; }
		th { background: rgba(14,165,233,0.08); color: $text-dark; font-weight: 600; }
	}
	::v-deep strong { color: $text-dark; }
}

.nd-nav {
	display: flex; justify-content: space-between; gap: 12px; margin-bottom: 14px;
	.nd-nav-btn {
		flex: 1; display: flex; align-items: center; gap: 6px;
		padding: 10px 16px; background: $white; border-radius: 10px;
		border: 1px solid $border; cursor: pointer;
		font-size: 13px; color: $text-sub; transition: all 0.25s;
		overflow: hidden; white-space: nowrap; text-overflow: ellipsis;
		.el-icon { color: $primary; flex-shrink: 0; vertical-align: middle; }
		span { overflow: hidden; text-overflow: ellipsis; }
		&:hover { border-color: rgba(14,165,233,0.3); color: $primary; background: rgba(14,165,233,0.05); }
		&:last-child { justify-content: flex-end; text-align: right; }
	}
}

.nd-tags {
	display: flex; flex-wrap: wrap; gap: 8px; padding: 12px 16px;
	background: $white; border-radius: 10px; border: 1px solid $border;
	.nd-tag {
		font-size: 12px; color: $text-sub; padding: 4px 14px;
		background: rgba(14,165,233,0.08); border-radius: 14px;
		border: 1px solid rgba(14,165,233,0.12);
		transition: all 0.2s;
		&:hover { color: $primary; border-color: rgba(14,165,233,0.25); }
	}
}
</style>
