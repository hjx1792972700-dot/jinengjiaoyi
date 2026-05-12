<template>
	<div class="home-preview">
		<!-- 技能市场推荐 -->
		<div id="animate_recommendjinengxuqiu" class="recommend animate__animated">
			<div class="recommend_title_box">
				<span class="recommend_title">技能市场推荐</span>
				<span class="recommend_subhead">{{userSkillCategories.length > 0 ? '基于你的需求「' + userSkillCategories.slice(0,3).join('、') + '」智能推荐' : '为你精选优质技能服务'}}</span>
			</div>
			<div class="list list21 index-pv1">
				<div class="list-body">
					<div class="list-item" v-for="(item,index) in jinengxuqiuRecommend.slice(0, 4)" :key="index" @click="toDetail('jinengxuqiuDetail', item)">
						<div class="img" :style="getDefaultBg(index, 'skill')">
							<img v-if="preHttp(item.fengmian)&&preHttp2(item.fengmian)" :src="item.fengmian" alt="" @error="onImgError($event, index, 'skill')" />
							<img v-else-if="preHttp(item.fengmian)" :src="item.fengmian.split(',')[0]" alt="" @error="onImgError($event, index, 'skill')" />
							<img v-else :src="baseUrl + (item.fengmian?item.fengmian.split(',')[0]:'')" alt="" @error="onImgError($event, index, 'skill')" />
						</div>
						<div class="infoBox">
							<div class="name">{{item.xuqiubiaoti}}</div>
							<div class="tag-line">
								<span class="tag">{{item.jinengfenlei}}</span>
								<span class="price-badge" v-if="item.xuqiufeiyong">¥{{item.xuqiufeiyong}}</span>
								<span class="price-badge price-free" v-else>面议</span>
							</div>
							<div class="bottomInfo">
								<div class="stat-row">
									<span class="stat-item"><span class="icon iconfont icon-zan10"></span>{{item.thumbsupnum}}</span>
									<span class="stat-item"><span class="icon iconfont icon-shoucang10"></span>{{item.storeupnum}}</span>
									<span class="stat-item"><span class="icon iconfont icon-chakan2"></span>{{item.clicknum}}</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="moreBtn-new" @click="moreBtn('jinengxuqiu')">
				<span class="text">查看更多技能</span>
				<span class="icon iconfont icon-jiantou25"></span>
			</div>
		</div>

		<!-- 学习资料推荐 -->
		<div id="animate_recommendxuexiziliao" class="recommend animate__animated">
			<div class="recommend_title_box">
				<span class="recommend_title">学习资料推荐</span>
				<span class="recommend_subhead">{{userSkillCategories.length > 0 ? '根据你的需求偏好智能匹配' : '海量优质学习资源等你发现'}}</span>
			</div>
			<div class="list list21 index-pv1">
				<div class="list-body">
					<div class="list-item" v-for="(item,index) in xuexiziliaoRecommend.slice(0, 4)" :key="index" @click="toDetail('xuexiziliaoDetail', item)">
						<div class="img" :style="getDefaultBg(index, 'material')">
							<img v-if="preHttp(item.ziliaofengmian)&&preHttp2(item.ziliaofengmian)" :src="item.ziliaofengmian" alt="" @error="onImgError($event, index, 'material')" />
							<img v-else-if="preHttp(item.ziliaofengmian)" :src="item.ziliaofengmian.split(',')[0]" alt="" @error="onImgError($event, index, 'material')" />
							<img v-else :src="baseUrl + (item.ziliaofengmian?item.ziliaofengmian.split(',')[0]:'')" alt="" @error="onImgError($event, index, 'material')" />
						</div>
						<div class="infoBox">
							<div class="name">{{item.ziliaobiaoti}}</div>
							<div class="bottomInfo">
								<div class="time_item">
									<span class="icon iconfont icon-shijian21"></span>
									<span class="text">{{item.addtime.split(' ')[0]}}</span>
								</div>
								<div class="collect_item">
									<span class="icon iconfont icon-shoucang10"></span>
									<span class="text">{{item.storeupnum}}</span>
								</div>
								<div class="view_item">
									<span class="icon iconfont icon-chakan2"></span>
									<span class="text">{{item.clicknum}}</span>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="moreBtn-new" @click="moreBtn('xuexiziliao')">
				<span class="text">查看更多资料</span>
				<span class="icon iconfont icon-jiantou25"></span>
			</div>
		</div>

		<!-- 推荐用户 -->
		<div id="animate_recommendusers" class="recommend animate__animated" v-if="recommendUsers.length">
			<div class="recommend_title_box">
				<span class="recommend_title">推荐技能达人</span>
				<span class="recommend_subhead">{{userSkillCategories.length > 0 ? '为你匹配的技能服务伙伴' : '发现优质技能服务伙伴'}}</span>
			</div>
			<div class="user-cards">
				<div class="user-card" v-for="(user, idx) in recommendUsers.slice(0, 4)" :key="idx">
					<div class="user-avatar">
						<img v-if="user.touxiang" :src="baseUrl + user.touxiang" @error="$event.target.style.display='none'" />
						<div class="avatar-fallback" v-else>
							<el-icon><User /></el-icon>
						</div>
					</div>
					<div class="user-info">
						<div class="user-name">{{user.yonghuxingming}}</div>
						<div class="user-meta">
							<span class="meta-tag" v-if="user.xingbie">{{user.xingbie}}</span>
							<span class="meta-tag" v-if="user.age">{{user.age}}岁</span>
						</div>
						<div class="user-credit">
							<span class="credit-label">信誉指数</span>
							<span class="credit-val">{{user.xinyuzhishu || '--'}}</span>
						</div>
					</div>
				</div>
			</div>
		</div>

		<!-- 公告资讯 -->
		<div id="animate_newsnews" class="news-section animate__animated">
			<div class="recommend_title_box">
				<span class="recommend_title">公告资讯</span>
				<span class="recommend_subhead">平台最新动态一览</span>
			</div>
			<div class="news-layout">
				<div class="news-featured" v-if="newsList.length" @click="toDetail('newsDetail', newsList[0])">
					<div class="featured-badge">最新</div>
					<div class="featured-body">
						<div class="featured-title">{{newsList[0].title}}</div>
						<div class="featured-desc" v-if="newsList[0].introduction">{{newsList[0].introduction}}</div>
						<div class="featured-meta">
							<span class="meta-item"><span class="icon iconfont icon-yonghu"></span>{{newsList[0].name || '系统管理员'}}</span>
							<span class="meta-item"><span class="icon iconfont icon-shijian21"></span>{{newsList[0].addtime}}</span>
						</div>
					</div>
					<div class="featured-arrow"><span class="icon iconfont icon-jiantou25"></span></div>
				</div>
				<div class="news-cards">
					<div class="news-card" v-for="(item, idx) in newsList.slice(1)" :key="idx" @click="toDetail('newsDetail', item)">
						<div class="card-date" :class="'theme-' + (idx % 4)">
							<span class="date-day">{{item.addtime ? item.addtime.substring(8,10) : '--'}}</span>
							<span class="date-sep"></span>
							<span class="date-ym">{{item.addtime ? item.addtime.substring(0,7) : '----'}}</span>
						</div>
						<div class="card-body">
							<div class="card-title">{{item.title}}</div>
							<div class="card-meta">{{item.name || '系统管理员'}}</div>
						</div>
						<div class="card-arrow"><span class="icon iconfont icon-jiantou25"></span></div>
					</div>
				</div>
			</div>
			<div class="moreBtn-new" @click="moreBtn('news')">
				<span class="text">查看全部公告</span>
				<span class="icon iconfont icon-jiantou25"></span>
			</div>
		</div>
	</div>
</template>

<script>
import 'animate.css'
import { User } from '@element-plus/icons-vue'

	export default {
		components: { User },
		//数据集合
		data() {
			return {
				baseUrl: '',
				newsList: [],
				jinengxuqiuRecommend: [],
				xuexiziliaoRecommend: [],
				recommendUsers: [],
				userSkillCategories: [],

				recommendIndex21jinengxuqiu: 0,
				recommendList21jinengxuqiu: [],
				recommendColumn21jinengxuqiu: '',
			}
		},
	created() {
		this.baseUrl = this.$config.baseUrl;
		this.loadAllData();
	},
	mounted() {
		window.addEventListener('scroll', this.handleScroll)
		setTimeout(()=>{
			this.handleScroll()
		},100)
		
		this.swiperChanges()
	},
	beforeUnmount() {
		window.removeEventListener('scroll', this.handleScroll)
	},
		computed: {
			username() {
				return localStorage.getItem('username')
			}
		},
		//方法集合
		methods: {
		loadAllData() {
			this.getNewsList();
			this.getUserSkillCategories().then(() => {
				this.getList();
			});
			this.jinengxuqiuRecommendCate();
			this.getRecommendUsers();
		},
		swiperChanges() {
			setTimeout(()=>{
			},750)
		},
			jinengxuqiuRecommendCate(){
				this.$http.get('jinengfenlei/list',{
					params: {
						limit: Number(6)
					}
				}).then(res => {
					if (res.data.code == 0) {
						this.recommendList21jinengxuqiu = res.data.data.list
					}
				});
				this.recommendColumn21jinengxuqiu = 'jinengfenlei'
			},
			async recommendIndexClick21(index, name) {
				this['recommendIndex21' + name] = index
				await this.getList()
			},


			handleScroll() {
				let arr = [
					{id:'about',css:'animate__'},
					{id:'system',css:'animate__'},
					{id:'animate_recommendjinengxuqiu',css:'animate__'},
					{id:'animate_recommendxuexiziliao',css:'animate__'},
					{id:'animate_recommendusers',css:'animate__'},
					{id:'animate_newsnews',css:'animate__'},
				]
			
				for (let i in arr) {
					let doc = document.getElementById(arr[i].id)
					if (doc) {
						let top = doc.offsetTop
						let win_top = window.innerHeight + window.pageYOffset
						// console.log(top,win_top)
						if (win_top > top && doc.classList.value.indexOf(arr[i].css) < 0) {
							// console.log(doc)
							doc.classList.add(arr[i].css)
						}
					}
				}
			},
		getDefaultBg(index, type) {
			const skillGradients = [
				'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
				'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
				'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
				'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
				'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
				'linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)',
			]
			const materialGradients = [
				'linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%)',
				'linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%)',
				'linear-gradient(135deg, #d4fc79 0%, #96e6a1 100%)',
				'linear-gradient(135deg, #fbc2eb 0%, #a6c1ee 100%)',
				'linear-gradient(135deg, #fdcbf1 0%, #e6dee9 100%)',
				'linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%)',
			]
			const gradients = type === 'skill' ? skillGradients : materialGradients
			return { background: gradients[index % gradients.length] }
		},
		getNewsBg(index) {
			const colors = ['#667eea', '#f5576c', '#43e97b', '#4facfe', '#fa709a']
			return { background: colors[index % colors.length] }
		},
		onImgError(e, index, type) {
			e.target.style.display = 'none'
		},
		preHttp(str) {
			return str && str.substr(0,4)=='http';
		},
		preHttp2(str) {
			return str && str.split(',w').length>1;
		},
			getNewsList() {
				let data = {
					page: 1,
					limit: 5,
					sort: 'addtime',
					order: 'desc'
				}
				this.$http.get('news/list', {params: data}).then(res => {
					if (res.data.code == 0) {
						this.newsList = res.data.data.list;
					
					}
				});
			},
			async getUserSkillCategories() {
				if (!localStorage.getItem('frontToken')) return;
				try {
					let user = JSON.parse(localStorage.getItem('sessionForm'));
					if (!user || !user.yonghuzhanghao) return;
					let res = await this.$http.get('jinengxuqiu/page', {
						params: { page: 1, limit: 50, yonghuzhanghao: user.yonghuzhanghao }
					});
					if (res.data.code == 0 && res.data.data.list.length > 0) {
						let cats = [...new Set(res.data.data.list.map(i => i.jinengfenlei).filter(Boolean))];
						this.userSkillCategories = cats;
					}
				} catch(e) {}
			},
		getRecommendUsers() {
			if (!localStorage.getItem('frontToken')) return;
			let user = JSON.parse(localStorage.getItem('sessionForm'));
			this.$http.get('yonghu/recommend', {
				params: { limit: 6 }
			}).then(res => {
				if (res.data.code == 0) {
					this.recommendUsers = (res.data.data.list || [])
						.filter(u => u.yonghuzhanghao !== (user && user.yonghuzhanghao));
				}
			}).catch(() => {
				this.$http.get('yonghu/list', {
					params: { page: 1, limit: 6, sort: 'xinyuzhishu', order: 'desc' }
				}).then(res => {
					if (res.data.code == 0) {
						this.recommendUsers = res.data.data.list
							.filter(u => u.yonghuzhanghao !== (user && user.yonghuzhanghao));
					}
				});
			});
		},
			getList() {
			let autoSortUrl = "jinengxuqiu/autoSort";
			if(localStorage.getItem('frontToken')) {
				autoSortUrl = "jinengxuqiu/autoSort2";
			}
			let data = { page: 1, limit: 6 };
			data['sfsh'] = '是';
			if(this.recommendIndex21jinengxuqiu != 0){
				data.jinengfenlei = this.recommendList21jinengxuqiu[this.recommendIndex21jinengxuqiu - 1].jinengfenlei
			} else if (this.userSkillCategories.length > 0) {
				data.jinengfenlei = this.userSkillCategories[0];
			}
			this.$http.get(autoSortUrl, {params: data}).then(res => {
				if (res.data.code == 0) {
					this.jinengxuqiuRecommend = res.data.data.list;
					if (this.jinengxuqiuRecommend.length < 4 && this.userSkillCategories.length > 0) {
						let fallbackData = { page: 1, limit: 6, sfsh: '是' };
						let fallbackUrl = localStorage.getItem('frontToken') ? "jinengxuqiu/autoSort2" : "jinengxuqiu/autoSort";
						this.$http.get(fallbackUrl, {params: fallbackData}).then(r2 => {
							if (r2.data.code == 0) {
								let existing = this.jinengxuqiuRecommend.map(i => i.id);
								let extras = r2.data.data.list.filter(i => !existing.includes(i.id));
								this.jinengxuqiuRecommend = this.jinengxuqiuRecommend.concat(extras).slice(0, 6);
							}
						});
					}
				}
			});

			autoSortUrl = "xuexiziliao/autoSort";
			if(localStorage.getItem('frontToken')) {
				autoSortUrl = "xuexiziliao/autoSort2";
			}
			data = { page: 1, limit: 6 };
			if (this.userSkillCategories.length > 0) {
				data.ziliaoleixing = this.userSkillCategories[0];
			}
			this.$http.get(autoSortUrl, {params: data}).then(res => {
				if (res.data.code == 0) {
					this.xuexiziliaoRecommend = res.data.data.list;
					if (this.xuexiziliaoRecommend.length < 4 && this.userSkillCategories.length > 0) {
						let fallbackData = { page: 1, limit: 6 };
						let fallbackUrl = localStorage.getItem('frontToken') ? "xuexiziliao/autoSort2" : "xuexiziliao/autoSort";
						this.$http.get(fallbackUrl, {params: fallbackData}).then(r2 => {
							if (r2.data.code == 0) {
								let existing = this.xuexiziliaoRecommend.map(i => i.id);
								let extras = r2.data.data.list.filter(i => !existing.includes(i.id));
								this.xuexiziliaoRecommend = this.xuexiziliaoRecommend.concat(extras).slice(0, 6);
							}
						});
					}
				}
			});
		},
			toDetail(path, item) {
				this.$router.push({path: '/main/' + path, query: {id: item.id, storeupType: 1}});
			},
			moreBtn(path) {
				this.$router.push({path: '/main/' + path});
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$cyan: #0ea5e9;
$purple: #7c3aed;
$accent: #0284c7;
$bg-page: #f0f4f8;
$bg-card: #ffffff;
$border-light: #e2e8f0;
$text-dark: #1e293b;
$text-body: #475569;
$text-sub: #94a3b8;

.home-preview {
	padding: 20px 0 30px;
	margin: 0 auto;
	overflow: hidden;
	display: flex;
	width: 100%;
	flex-wrap: wrap;

	.recommend_title_box {
		margin: 24px auto 12px;
		display: flex;
		width: 100%;
		justify-content: center;
		flex-wrap: wrap;
		.recommend_title {
			background: linear-gradient(90deg, $text-dark 0%, $cyan 60%, $purple 100%);
			-webkit-background-clip: text;
			-webkit-text-fill-color: transparent;
			background-clip: text;
			font-weight: 700;
			display: block;
			letter-spacing: 4px;
			width: 100%;
			font-size: 22px;
			line-height: 1.5;
			text-align: center;
		}
		.recommend_subhead {
			color: $text-sub;
			font-weight: 400;
			display: block;
			width: auto;
			font-size: 13px;
			line-height: 24px;
			text-align: center;
		}
	}

	.recommend {
		padding: 0 0 16px;
		margin: 0 auto;
		width: 100%;
		position: relative;
		order: 1;

		.list21 {
			padding: 0;
			width: 100%;
			.list-body {
				margin: 12px auto 0 -8px;
				display: flex;
				width: calc(100% + 16px);

				.list-item {
					cursor: pointer;
					margin: 0 8px;
					background: $bg-card;
					border: 1px solid $border-light;
					width: calc(25% - 16px);
					position: relative;
					border-radius: 14px;
					overflow: hidden;
					transition: all 0.3s ease;
					box-shadow: 0 2px 8px rgba(0,0,0,0.04);

					.img {
						width: 100%;
						height: 200px;
						overflow: hidden;
						display: flex;
						align-items: center;
						justify-content: center;
						img {
							object-fit: cover;
							width: 100%;
							height: 100%;
							transition: transform 0.4s ease;
						}
					}
					.infoBox {
						padding: 10px 12px;
						left: 0;
						background: linear-gradient(transparent, rgba(0,0,0,.75));
						bottom: 0;
						position: absolute;
						right: 0;
						.name {
							margin: 0 0 4px;
							overflow: hidden;
							color: #fff;
							white-space: nowrap;
							font-weight: 600;
							font-size: 14px;
							line-height: 24px;
							text-overflow: ellipsis;
							text-shadow: 0 1px 3px rgba(0,0,0,.4);
						}
						.tag-line {
							display: flex;
							align-items: center;
							gap: 6px;
							margin: 0 0 4px;
							.tag {
								padding: 1px 8px;
								background: rgba(14,165,233,0.85);
								color: #fff;
								font-size: 11px;
								border-radius: 10px;
								line-height: 18px;
							}
							.price-badge {
								padding: 1px 6px;
								background: linear-gradient(135deg, $purple, #9b59b6);
								color: #fff;
								font-size: 11px;
								font-weight: bold;
								border-radius: 8px;
								margin-left: auto;
							}
							.price-badge.price-free {
								background: linear-gradient(135deg, $cyan, #0284c7);
							}
						}
						.bottomInfo {
							overflow: hidden;
							transition: all 0.3s;
							height: 0;
							.stat-row {
								display: flex;
								gap: 10px;
								.stat-item {
									color: rgba(255,255,255,.9);
									font-size: 12px;
									display: flex;
									align-items: center;
									gap: 2px;
									.icon { font-size: 12px; }
								}
							}
						}
					}
				}
				.list-item::after {
					bottom: 0;
					background: linear-gradient(90deg, $cyan, $purple);
					width: 0;
					position: absolute;
					content: "";
					transition: all 0.3s;
					height: 2px;
					border-radius: 1px;
				}
				.list-item:hover {
					border-color: rgba(14,165,233,0.3);
					box-shadow: 0 8px 28px rgba(14,165,233,0.1);
					transform: translateY(-4px);
					.img img { transform: scale(1.05); }
					.infoBox .bottomInfo { height: 24px; }
				}
				.list-item:hover::after { width: 100%; }
			}
		}

		.moreBtn-new {
			cursor: pointer;
			margin: 16px auto 0;
			display: flex;
			align-items: center;
			justify-content: center;
			gap: 6px;
			width: 170px;
			height: 38px;
			border-radius: 19px;
			border: 1px solid rgba(14,165,233,0.4);
			background: transparent;
			transition: all 0.3s;
			.text {
				color: $cyan;
				font-size: 13px;
				font-weight: 500;
			}
			.icon {
				color: $cyan;
				font-size: 12px;
			}
		}
		.moreBtn-new:hover {
			background: $cyan;
			border-color: $cyan;
			box-shadow: 0 4px 16px rgba(14,165,233,0.25);
			.text, .icon { color: #fff; }
		}
	}

	.news-section {
		padding: 0 0 16px;
		margin: 0 auto;
		width: 100%;
		order: 9;

		.news-layout {
			display: flex;
			gap: 16px;
			align-items: stretch;
		}

		.news-featured {
			flex: 0 0 44%;
			background: linear-gradient(135deg, #0f172a 0%, #1e3a5f 60%, #0ea5e9 100%);
			border-radius: 16px;
			padding: 28px 24px;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			cursor: pointer;
			position: relative;
			overflow: hidden;
			transition: transform 0.3s, box-shadow 0.3s;

			&::before {
				content: '';
				position: absolute;
				top: -40px; right: -40px;
				width: 140px; height: 140px;
				border-radius: 50%;
				background: rgba(14,165,233,0.12);
			}
			&::after {
				content: '';
				position: absolute;
				bottom: -30px; left: -20px;
				width: 100px; height: 100px;
				border-radius: 50%;
				background: rgba(124,58,237,0.1);
			}

			&:hover {
				transform: translateY(-3px);
				box-shadow: 0 8px 32px rgba(14,165,233,0.2);
			}

			.featured-badge {
				display: inline-block;
				padding: 3px 12px;
				background: rgba(255,255,255,0.15);
				color: #fff;
				font-size: 11px;
				font-weight: 600;
				border-radius: 12px;
				width: fit-content;
				letter-spacing: 2px;
				backdrop-filter: blur(4px);
				margin-bottom: 16px;
			}

			.featured-body {
				flex: 1;
				position: relative;
				z-index: 1;

				.featured-title {
					font-size: 20px;
					font-weight: 700;
					color: #fff;
					line-height: 1.5;
					margin-bottom: 10px;
					display: -webkit-box;
					-webkit-line-clamp: 2;
					-webkit-box-orient: vertical;
					overflow: hidden;
					text-shadow: 0 1px 4px rgba(0,0,0,0.2);
				}

				.featured-desc {
					font-size: 13px;
					color: rgba(255,255,255,0.75);
					line-height: 1.6;
					display: -webkit-box;
					-webkit-line-clamp: 2;
					-webkit-box-orient: vertical;
					overflow: hidden;
					margin-bottom: 14px;
				}

				.featured-meta {
					display: flex;
					gap: 16px;
					flex-wrap: wrap;
					.meta-item {
						font-size: 12px;
						color: rgba(255,255,255,0.65);
						display: flex;
						align-items: center;
						gap: 4px;
						.icon { font-size: 13px; }
					}
				}
			}

			.featured-arrow {
				position: absolute;
				bottom: 20px; right: 20px;
				width: 34px; height: 34px;
				border-radius: 50%;
				background: rgba(255,255,255,0.15);
				display: flex;
				align-items: center;
				justify-content: center;
				transition: background 0.2s;
				z-index: 1;
				.icon { color: #fff; font-size: 14px; }
			}
			&:hover .featured-arrow {
				background: rgba(255,255,255,0.3);
			}
		}

		.news-cards {
			flex: 1;
			display: flex;
			flex-direction: column;
			gap: 10px;
		}

		.news-card {
			display: flex;
			align-items: center;
			padding: 14px 16px;
			background: $bg-card;
			border-radius: 12px;
			cursor: pointer;
			transition: all 0.25s ease;
			border: 1px solid $border-light;
			box-shadow: 0 1px 4px rgba(0,0,0,0.03);

			&:hover {
				transform: translateX(4px);
				border-color: rgba(14,165,233,0.3);
				box-shadow: 0 4px 16px rgba(14,165,233,0.08);
				.card-body .card-title { color: $cyan; }
				.card-arrow .icon { color: $cyan; transform: translateX(2px); }
			}

			.card-date {
				flex-shrink: 0;
				width: 52px;
				height: 52px;
				border-radius: 12px;
				display: flex;
				flex-direction: column;
				align-items: center;
				justify-content: center;
				margin-right: 14px;
				.date-day {
					font-size: 20px;
					font-weight: 800;
					color: #fff;
					line-height: 1.1;
				}
				.date-sep {
					width: 16px;
					height: 1px;
					background: rgba(255,255,255,0.4);
					margin: 3px 0;
				}
				.date-ym {
					font-size: 9px;
					color: rgba(255,255,255,0.85);
					font-weight: 500;
					letter-spacing: 0.5px;
				}
				&.theme-0 { background: linear-gradient(135deg, $cyan, $purple); }
				&.theme-1 { background: linear-gradient(135deg, $purple, #e040fb); }
				&.theme-2 { background: linear-gradient(135deg, #43e97b, $cyan); }
				&.theme-3 { background: linear-gradient(135deg, #4facfe, $purple); }
			}

			.card-body {
				flex: 1;
				overflow: hidden;
				.card-title {
					font-size: 14px;
					font-weight: 600;
					color: $text-dark;
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
					line-height: 1.5;
					transition: color 0.2s;
				}
				.card-meta {
					font-size: 12px;
					color: $text-sub;
					margin-top: 3px;
				}
			}

			.card-arrow {
				flex-shrink: 0;
				margin-left: 10px;
				.icon {
					font-size: 14px;
					color: #cbd5e1;
					transition: all 0.2s;
				}
			}
		}

		.moreBtn-new {
			cursor: pointer;
			margin: 16px auto 0;
			display: flex;
			align-items: center;
			justify-content: center;
			gap: 6px;
			width: 170px;
			height: 38px;
			border-radius: 19px;
			border: 1px solid rgba(14,165,233,0.4);
			background: transparent;
			transition: all 0.3s;
			.text {
				color: $cyan;
				font-size: 13px;
				font-weight: 500;
			}
			.icon {
				color: $cyan;
				font-size: 12px;
			}
		}
		.moreBtn-new:hover {
			background: $cyan;
			border-color: $cyan;
			box-shadow: 0 4px 16px rgba(14,165,233,0.25);
			.text, .icon { color: #fff; }
		}
	}

	.user-cards {
		display: grid;
		grid-template-columns: repeat(4, 1fr);
		gap: 20px;
		max-width: 1100px;
		margin: 0 auto;
		padding: 0 20px;
	}
	.user-card {
		display: flex;
		align-items: center;
		gap: 14px;
		padding: 18px 20px;
		background: #fff;
		border-radius: 14px;
		border: 1px solid $border-light;
		transition: all 0.3s;
		cursor: default;
		&:hover {
			transform: translateY(-3px);
			box-shadow: 0 8px 24px rgba(14, 165, 233, 0.1);
			border-color: rgba(14, 165, 233, 0.3);
		}
		.user-avatar {
			flex-shrink: 0;
			width: 52px;
			height: 52px;
			border-radius: 50%;
			overflow: hidden;
			background: linear-gradient(135deg, $cyan, $purple);
			display: flex;
			align-items: center;
			justify-content: center;
			img {
				width: 100%;
				height: 100%;
				object-fit: cover;
			}
			.avatar-fallback {
				color: #fff;
				font-size: 24px;
				.el-icon {
					font-size: 24px;
				}
			}
		}
		.user-info {
			flex: 1;
			min-width: 0;
		}
		.user-name {
			font-size: 15px;
			font-weight: 600;
			color: $text-dark;
			margin-bottom: 4px;
		}
		.user-meta {
			display: flex;
			gap: 6px;
			margin-bottom: 6px;
			.meta-tag {
				font-size: 11px;
				padding: 1px 8px;
				border-radius: 8px;
				background: rgba(14, 165, 233, 0.08);
				color: $cyan;
			}
		}
		.user-credit {
			display: flex;
			align-items: center;
			gap: 6px;
			.credit-label {
				font-size: 11px;
				color: $text-sub;
			}
			.credit-val {
				font-size: 14px;
				font-weight: 700;
				color: $purple;
			}
		}
	}

	@media (max-width: 900px) {
		.user-cards { grid-template-columns: repeat(2, 1fr); }
	}
	@media (max-width: 500px) {
		.user-cards { grid-template-columns: 1fr; }
	}
}
</style>
