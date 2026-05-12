<template>
	<div class="detail-page">
		<div class="detail-topbar">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a @click="backClick">{{item.name}}</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item3"><a href="javascript:void(0);">详情</a></el-breadcrumb-item>
			</el-breadcrumb>
			<el-button class="back-btn" size="small" @click="backClick" icon="el-icon-arrow-left">返回</el-button>
		</div>

		<div class="detail-card">
			<div class="card-left" v-if="detailBanner.length">
				<div class="img-main">
					<img v-if="detailBanner[currentImg] && detailBanner[currentImg].substr(0,4)=='http'" :src="detailBanner[currentImg]" class="main-image" />
					<img v-else :src="baseUrl + detailBanner[currentImg]" class="main-image" />
				</div>
				<div class="img-thumbs" v-if="detailBanner.length > 1">
					<div class="thumb" v-for="(img, idx) in detailBanner" :key="idx"
						:class="{active: currentImg === idx}" @click="currentImg = idx">
						<img v-if="img.substr(0,4)=='http'" :src="img" />
						<img v-else :src="baseUrl + img" />
					</div>
				</div>
			</div>
			<div class="card-left card-left-empty" v-else>
				<div class="empty-img">
					<i class="el-icon-picture"></i>
					<span>暂无图片</span>
				</div>
			</div>

			<div class="card-right">
				<div class="info-grid">
					<div class="info-item">
						<div class="info-icon"><i class="el-icon-tickets"></i></div>
						<div class="info-body">
							<div class="info-label">充值编号</div>
							<div class="info-value">{{detail.chongzhibianhao || '--'}}</div>
						</div>
					</div>
					<div class="info-item">
						<div class="info-icon price"><i class="el-icon-coin"></i></div>
						<div class="info-body">
							<div class="info-label">充值金额</div>
							<div class="info-value highlight">{{detail.chongzhijine ? '¥' + detail.chongzhijine : '--'}}</div>
						</div>
					</div>
					<div class="info-item">
						<div class="info-icon"><i class="el-icon-date"></i></div>
						<div class="info-body">
							<div class="info-label">充值日期</div>
							<div class="info-value">{{detail.chongzhiriqi || '--'}}</div>
						</div>
					</div>
					<div class="info-item">
						<div class="info-icon"><i class="el-icon-postcard"></i></div>
						<div class="info-body">
							<div class="info-label">用户姓名</div>
							<div class="info-value">{{detail.yonghuxingming || '--'}}</div>
						</div>
					</div>
				</div>
				<div class="action-bar">
					<el-button class="act-btn act-edit" v-if="btnAuth('huiyuanchongzhi','修改')" @click="editClick" icon="el-icon-edit">修改</el-button>
					<el-button class="act-btn act-del" v-if="btnAuth('huiyuanchongzhi','删除')" @click="delClick" icon="el-icon-delete">删除</el-button>
					<el-button class="act-btn act-chapter" v-if="btnAuth('huiyuanchongzhi','章节管理')" @click="allchapterClick()" icon="el-icon-reading">章节管理</el-button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import axios from 'axios'
	export default {
		components: {
		},
		data() {
			return {
				tablename: 'huiyuanchongzhi',
				baseUrl: '',
				breadcrumbItem: [{ name: '会员充值' }],
				title: '',
				detailBanner: [],
				userid: Number(localStorage.getItem('frontUserid')),
				id: 0,
				detail: {},
				tabsNum: 0,
				activeName: '1',
				buynumber: 1,
				centerType: false,
				storeupType: false,
				sensitiveWordsArr: [],
				currentImg: 0,
			}
		},
		created() {
			if(this.$route.query.centerType&&this.$route.query.centerType!=0) {
				this.centerType = true
			}
			if(this.$route.query.storeupType&&this.$route.query.storeupType!=0) {
				this.storeupType = true
			}
			this.init();
		},
		computed: {
			username() {
				return localStorage.getItem('username')
			}
		},
		methods: {
			init() {
				this.id = this.$route.query.id
				this.baseUrl = this.$config.baseUrl;
				this.$http.get(this.tablename + '/detail/' + this.id, {}).then(async res => {
					if (res.data.code == 0) {
						this.detail = res.data.data;
						this.getSensitiveWords()
						if(this.detail.tupian) {
							this.detailBanner = this.detail.tupian.split(",w").length>1?[this.detail.tupian]:this.detail.tupian.split(',');
						}
						this.$forceUpdate();
					}
				});
			},
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			backClick(){
				if(this.storeupType){
					history.back()
				}else if(this.centerType){
					this.$router.push({path: '/main/center', query: {tab: 'huiyuanchongzhi'}});
				}else{
					this.$router.push({path: '/main/huiyuanchongzhi'});
				}
			},
			download(file){
				if(!file) {
					this.$message({ type: 'error', message: '文件不存在', duration: 1500 });
					return;
				}
				let arr = file.replace(new RegExp('upload/', "g"), "")
				axios.get(this.baseUrl + 'file/download?fileName=' + arr, {
					headers: { token: localStorage.getItem("frontToken") },
					responseType: "blob"
				}).then(({ data }) => {
					const objectUrl = window.URL.createObjectURL(new Blob([data], { type: 'application/pdf;chartset=UTF-8' }))
					const a = document.createElement('a')
					a.href = objectUrl
					a.download = arr
					a.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }))
					window.URL.revokeObjectURL(data)
				})
			},
			btnAuth(tableName,key){
				if(this.centerType){ return this.isBackAuth(tableName,key) }
				else { return this.isAuth(tableName,key) }
			},
			editClick(){
				this.$router.push(`/main/huiyuanchongzhiAdd?type=edit&&id=${this.detail.id}`);
			},
			async delClick(){
				await this.$confirm('是否删除此会员充值？').then(_ => {
					this.$http.post('huiyuanchongzhi/delete', [this.detail.id]).then(async res => {
						if (res.data.code == 0) {
							this.$message({
								type: 'success', message: '删除成功!', duration: 1500,
								onClose: () => { history.back() }
							});
						}
					});
				}).catch(_ => {});
			},
			allchapterClick(){
				let params = { refid: this.detail.id }
				if(this.centerType){ params.centerType = 1 }
				this.$router.push({path: '/main/chapterhuiyuanchongzhi', query: params});
			},
			getUUID() { return new Date().getTime(); },
		},
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$purple: #38bdf8;
$text-dark: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: #64748b;
$border: rgba(14,165,233,0.12);
$bg: #0c1222;
$white: #0f172a;
$radius: 14px;

.detail-page {
	max-width: 1000px;
	margin: 0 auto;
	padding: 12px 24px 0;
	height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
	overflow: hidden;
}

.detail-topbar {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 20px;

	.back-btn {
		border-radius: 20px;
		border: 1px solid $border;
		color: $text-sub;
		font-size: 13px;
		&:hover {
			color: $primary;
			border-color: $primary;
			background: rgba(14,165,233,0.04);
		}
	}
}

.detail-card {
	display: flex;
	gap: 28px;
	background: $white;
	border-radius: $radius;
	box-shadow: 0 4px 24px rgba(0,0,0,0.06);
	padding: 28px;
	border: 1px solid $border;
	min-height: 0;
}

.card-left {
	width: 320px;
	min-width: 320px;
	display: flex;
	flex-direction: column;
	gap: 12px;

	.img-main {
		border-radius: 12px;
		overflow: hidden;
		background: $bg;
		aspect-ratio: 4 / 3;
		display: flex;
		align-items: center;
		justify-content: center;

		.main-image {
			width: 100%;
			height: 100%;
			object-fit: cover;
			display: block;
			transition: transform 0.4s;
			&:hover { transform: scale(1.03); }
		}
	}

	.img-thumbs {
		display: flex;
		gap: 8px;
		overflow-x: auto;
		padding-bottom: 4px;

		.thumb {
			width: 60px;
			height: 45px;
			border-radius: 8px;
			overflow: hidden;
			cursor: pointer;
			border: 2px solid transparent;
			transition: all 0.25s;
			flex-shrink: 0;
			opacity: 0.6;

			&.active {
				border-color: $primary;
				opacity: 1;
				box-shadow: 0 0 0 2px rgba(14,165,233,0.2);
			}
			&:hover { opacity: 1; }

			img {
				width: 100%;
				height: 100%;
				object-fit: cover;
				display: block;
			}
		}
	}
}

.card-left-empty {
	align-items: center;
	justify-content: center;
	background: $bg;
	border-radius: 12px;
	min-height: 200px;

	.empty-img {
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 8px;
		color: $text-dim;
		i, :deep(.el-icon) { font-size: 48px; }
		span { font-size: 13px; }
	}
}

.card-right {
	flex: 1;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	min-width: 0;
}

.info-grid {
	display: flex;
	flex-direction: column;
	gap: 0;
}

.info-item {
	display: flex;
	align-items: center;
	gap: 14px;
	padding: 14px 0;
	border-bottom: 1px solid rgba(14,165,233,0.08);

	&:last-child { border-bottom: none; }

	.info-icon {
		width: 38px;
		height: 38px;
		border-radius: 10px;
		background: linear-gradient(135deg, rgba(14,165,233,0.08), rgba(167,139,250,0.06));
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;

		i, :deep(.el-icon) {
			font-size: 17px;
			color: $primary;
		}

		&.price {
			background: linear-gradient(135deg, rgba(245,158,11,0.1), rgba(251,113,133,0.06));
			i, :deep(.el-icon) { color: #f59e0b; }
		}
	}

	.info-body {
		flex: 1;
		min-width: 0;
	}
	.info-label {
		font-size: 12px;
		color: $text-dim;
		margin-bottom: 2px;
	}
	.info-value {
		font-size: 15px;
		color: $text-dark;
		font-weight: 500;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;

		&.highlight {
			color: #f59e0b;
			font-size: 18px;
			font-weight: 700;
		}
	}
}

.action-bar {
	display: flex;
	gap: 10px;
	margin-top: 20px;
	padding-top: 18px;
	border-top: 1px solid $border;
	flex-wrap: wrap;

	.act-btn {
		border-radius: 10px;
		font-size: 13px;
		padding: 10px 20px;
		font-weight: 500;
		transition: all 0.3s;
	}
	.act-edit {
		background: linear-gradient(135deg, $primary, $purple);
		border: none;
		color: #fff;
		&:hover {
			box-shadow: 0 4px 16px rgba(14,165,233,0.25);
			transform: translateY(-1px);
		}
	}
	.act-del {
		background: $white;
		border: 1px solid rgba(251,113,133,0.4);
		color: #fb7185;
		&:hover {
			background: rgba(251,113,133,0.1);
			border-color: #fb7185;
		}
	}
	.act-chapter {
		background: $white;
		border: 1px solid $border;
		color: $text-sub;
		&:hover {
			color: $primary;
			border-color: $primary;
			background: rgba(14,165,233,0.04);
		}
	}
}

@media (max-width: 768px) {
	.detail-card {
		flex-direction: column;
		padding: 16px;
	}
	.card-left {
		width: 100%;
		min-width: 0;
	}
}
</style>
