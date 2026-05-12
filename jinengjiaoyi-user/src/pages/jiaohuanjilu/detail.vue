<template>
	<div class="apply-detail-page">
		<div class="page-header">
			<div class="header-left">
				<el-breadcrumb :separator="'/'">
					<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
					<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"
						:to="centerType ? '/main/center' : '/main/jiaohuanjilu'">
						<a>{{item.name}}</a>
					</el-breadcrumb-item>
					<el-breadcrumb-item class="item3"><a href="javascript:void(0);">交换详情</a></el-breadcrumb-item>
				</el-breadcrumb>
			</div>
			<div class="header-right">
				<span class="status-badge done"><i class="el-icon-circle-check"></i> 已交换</span>
				<el-button class="back-btn" size="small" @click="backClick">
					<template #icon><ArrowLeft /></template>
					返回
				</el-button>
			</div>
		</div>

		<div class="exchange-flow">
			<div class="flow-card applicant-card">
				<div class="card-label"><i class="el-icon-promotion"></i> 申请人</div>
				<div class="card-avatar">
					<i class="el-icon-avatar"></i>
				</div>
				<div class="card-name">{{detail.gongjirenxingming || '--'}}</div>
				<div class="card-tags">
					<span class="mini-tag"><i class="el-icon-phone"></i> {{detail.lianxifangshi || '--'}}</span>
				</div>
				<div class="provided-skill" v-if="detail.shenqingrentigong">
					<div class="provided-label">提供技能</div>
					<div class="provided-value">{{detail.shenqingrentigong}}</div>
				</div>
			</div>

			<div class="flow-center">
				<div class="skill-item left-skill">
					<div class="skill-item-label">申请人提供</div>
					<div class="skill-item-name">{{detail.shenqingrentigong || '--'}}</div>
					<div class="skill-item-arrow"><i class="el-icon-arrow-right"></i></div>
				</div>
				<div class="exchange-badge">
					<i class="el-icon-sort"></i>
					<span>技能交换</span>
				</div>
				<div class="skill-item right-skill">
					<div class="skill-item-arrow"><i class="el-icon-arrow-right"></i></div>
					<div class="skill-item-label">发布者提供</div>
					<div class="skill-item-name">{{detail.fabuzhetigong || detail.biaoti || '--'}}</div>
				</div>
			</div>

			<div class="flow-card owner-card">
				<div class="card-label"><i class="el-icon-goods"></i> 技能发布者</div>
				<div class="card-avatar owner">
					<i class="el-icon-user-solid"></i>
				</div>
				<div class="card-name">{{detail.yonghuxingming || '--'}}</div>
				<div class="card-tags">
					<span class="mini-tag"><i class="el-icon-phone"></i> {{detail.shoujihao || '--'}}</span>
				</div>
				<div class="provided-skill" v-if="detail.fabuzhetigong">
					<div class="provided-label">提供技能</div>
					<div class="provided-value">{{detail.fabuzhetigong}}</div>
				</div>
			</div>
		</div>

		<div class="detail-bottom">
			<div class="remark-card" v-if="detail.gongjineirong">
				<div class="remark-icon"><i class="el-icon-document"></i></div>
				<div class="remark-body">
					<div class="remark-title">供给内容</div>
					<div class="remark-text">{{detail.gongjineirong}}</div>
				</div>
			</div>

			<div class="media-row" v-if="detail.gongjishipin || detail.gongjifujian">
				<div class="media-item" v-if="detail.gongjishipin">
					<video :src="baseUrl + detail.gongjishipin" controls></video>
				</div>
				<div class="media-item" v-if="detail.gongjifujian">
					<el-button class="dl-btn" size="small" @click="download(detail.gongjifujian)">
						<template #icon><Download /></template>
						下载附件
					</el-button>
				</div>
			</div>

			<div class="bottom-row">
				<div class="time-info">
					<i class="el-icon-time"></i> 交换时间：{{detail.jiaohuanshijian || '--'}}
				</div>
				<div class="action-bar">
					<el-button class="act-btn act-review" size="small" @click="goReview">
						<template #icon><StarFilled /></template>
						查看评价详情
					</el-button>
				</div>
			</div>

			<div class="review-section">
				<div class="section-title"><i class="el-icon-chat-dot-round"></i> 评价反馈</div>

				<div class="existing-review" v-if="existingReview">
					<div class="review-meta">
						<span class="reviewer-name"><i class="el-icon-user"></i> {{existingReview.jiaohuanrenxingming || existingReview.jiaohuanrenzhanghao}}</span>
						<span class="review-result good" v-if="existingReview.pingjiajieguo=='好评'"><i class="el-icon-star-on"></i> 好评</span>
						<span class="review-result bad" v-else-if="existingReview.pingjiajieguo=='差评'"><i class="el-icon-star-off"></i> 差评</span>
						<span class="review-result normal" v-else-if="existingReview.pingjiajieguo"><i class="el-icon-minus"></i> {{existingReview.pingjiajieguo}}</span>
						<span class="review-time">{{existingReview.pingjiashijian}}</span>
					</div>
					<div class="review-body" v-if="existingReview.jiaohuanbeizhu">{{existingReview.jiaohuanbeizhu}}</div>
					<div class="review-reply" v-if="existingReview.huifuneirong">
						<div class="reply-label"><i class="el-icon-chat-line-square"></i> 回复</div>
						<div class="reply-text">{{existingReview.huifuneirong}}</div>
					</div>
				</div>
				<div class="no-review" v-else-if="reviewLoaded">
					<i class="el-icon-info"></i> 暂无评价
				</div>

			</div>
		</div>
	</div>
</template>

<script>
import axios from 'axios'
export default {
	data() {
		return {
			tablename: 'jiaohuanjilu',
			baseUrl: '',
			breadcrumbItem: [{ name: '交换记录' }],
			title: '',
			detailBanner: [],
			currentImg: 0,
			userid: Number(localStorage.getItem('frontUserid')),
			id: 0,
			detail: {},
			centerType: false,
			storeupType: false,
			sensitiveWordsArr: [],
			existingReview: null,
			reviewLoaded: false,
			reviewForm: { pingjiajieguo: '', content: '' },
			commentLoading: false,
		}
	},
	created() {
		if(this.$route.query.centerType && this.$route.query.centerType != 0) {
			this.centerType = true
		}
		if(this.$route.query.storeupType && this.$route.query.storeupType != 0) {
			this.storeupType = true
		}
		this.init();
	},
	computed: {
		username() { return localStorage.getItem('username') },
	},
	methods: {
		init() {
			this.id = this.$route.query.id
			this.baseUrl = this.$config.baseUrl;
			this.$http.get(this.tablename + '/detail/' + this.id, {}).then(async res => {
				if(res.data.code == 0) {
					this.detail = res.data.data;
					this.currentImg = 0;
					this.getSensitiveWords();
					this.title = this.detail.biaoti;
					if(this.detail.gongjitupian) {
						this.detailBanner = this.detail.gongjitupian.split(',w').length > 1
							? [this.detail.gongjitupian]
							: this.detail.gongjitupian.split(',');
					} else {
						this.detailBanner = [];
					}
					this.loadExistingReview();
					this.$forceUpdate();
				}
			});
		},
		loadExistingReview() {
			let params = { page: 1, limit: 10, biaoti: this.detail.biaoti };
			if (this.detail.yonghuzhanghao) params.yonghuzhanghao = this.detail.yonghuzhanghao;
			if (this.detail.gongjirenzhanghao) params.jiaohuanrenzhanghao = this.detail.gongjirenzhanghao;
			this.$http.get('pingjiafankui/page', { params }).then(res => {
				this.reviewLoaded = true;
				if (res.data.code == 0 && res.data.data.list && res.data.data.list.length > 0) {
					this.existingReview = res.data.data.list[0];
				} else {
					this.loadExistingReviewBroad();
				}
			}).catch(() => { this.reviewLoaded = true; });
		},
		loadExistingReviewBroad() {
			let params = { page: 1, limit: 10, biaoti: this.detail.biaoti };
			if (this.detail.yonghuzhanghao) params.yonghuzhanghao = this.detail.yonghuzhanghao;
			this.$http.get('pingjiafankui/page', { params }).then(res => {
				if (res.data.code == 0 && res.data.data.list && res.data.data.list.length > 0) {
					let list = res.data.data.list;
					this.existingReview = list.find(r =>
						r.jiaohuanrenzhanghao === this.detail.gongjirenzhanghao ||
						r.jiaohuanrenzhanghao === this.detail.yonghuzhanghao
					) || null;
				}
			}).catch(() => {});
		},
		submitComment() {
			if (!this.reviewForm.content.trim()) {
				this.$message({ type: 'warning', message: '请输入内容', duration: 1500 });
				return;
			}
			this.commentLoading = true;
			if (this.existingReview) {
				let newReply = this.reviewForm.content.trim();
				if (this.existingReview.huifuneirong) {
					newReply = this.existingReview.huifuneirong + '\n---\n' + newReply;
				}
				let updateData = Object.assign({}, this.existingReview, { huifuneirong: newReply });
				this.$http.post('pingjiafankui/update', updateData).then(res => {
					this.commentLoading = false;
					if (res.data.code == 0) {
						this.$message({ type: 'success', message: '评论成功', duration: 1500 });
						this.existingReview.huifuneirong = newReply;
						this.reviewForm.content = '';
					} else {
						this.$message({ type: 'error', message: res.data.msg || '操作失败', duration: 1500 });
					}
				}).catch(() => { this.commentLoading = false; });
			} else {
				if (!this.reviewForm.pingjiajieguo) {
					this.commentLoading = false;
					this.$message({ type: 'warning', message: '请选择评价结果', duration: 1500 });
					return;
				}
				let sessionUser = JSON.parse(localStorage.getItem('sessionForm')) || {};
				let userTableName = localStorage.getItem('UserTableName');
				this.$http.get(userTableName + '/session').then(userRes => {
					let json = userRes.data.code == 0 ? userRes.data.data : {};
					let reviewData = {
						yonghuzhanghao: this.detail.yonghuzhanghao,
						yonghuxingming: this.detail.yonghuxingming,
						shoujihao: this.detail.shoujihao,
						biaoti: this.detail.biaoti,
						jinengfenlei: this.detail.jinengfenlei,
						jiaohuanjineng: this.detail.jiaohuanjineng,
						jiaohuanbeizhu: this.reviewForm.content.trim(),
						jiaohuanrenzhanghao: json.yonghuzhanghao || '',
						jiaohuanrenxingming: json.yonghuxingming || '',
						lianxifangshi: json.shoujihao || '',
						pingjiajieguo: this.reviewForm.pingjiajieguo,
						pingjiashijian: new Date().toISOString().split('T')[0],
					};
					this.$http.post('pingjiafankui/add', reviewData).then(res => {
						this.commentLoading = false;
						if (res.data.code == 0) {
							this.$message({ type: 'success', message: '评价成功', duration: 1500 });
							this.reviewForm.content = '';
							this.reviewForm.pingjiajieguo = '';
							this.loadExistingReview();
						} else {
							this.$message({ type: 'error', message: res.data.msg || '操作失败', duration: 1500 });
						}
					}).catch(() => { this.commentLoading = false; });
				}).catch(() => { this.commentLoading = false; });
			}
		},
		getSensitiveWords() {
			this.$http.get('sensitivewords/detail/1').then(rs => {
				this.sensitiveWordsArr = rs.data.data.content.split(',')
			})
		},
		goReview() {
			let params = {
				page: 1,
				limit: 10,
				biaoti: this.detail.biaoti,
			};
			if (this.detail.yonghuzhanghao) params.yonghuzhanghao = this.detail.yonghuzhanghao;
			if (this.detail.gongjirenzhanghao) params.jiaohuanrenzhanghao = this.detail.gongjirenzhanghao;
			this.$http.get('pingjiafankui/page', { params }).then(res => {
				if (res.data.code == 0 && res.data.data.list && res.data.data.list.length > 0) {
					this.goToReview(res.data.data.list[0].id);
				} else {
					this.goReviewBroadSearch();
				}
			}).catch(() => {
				this.autoCreateReview();
			});
		},
		goReviewBroadSearch() {
			let params = {
				page: 1,
				limit: 10,
				biaoti: this.detail.biaoti,
			};
			if (this.detail.yonghuzhanghao) params.yonghuzhanghao = this.detail.yonghuzhanghao;
			this.$http.get('pingjiafankui/page', { params }).then(res => {
				if (res.data.code == 0 && res.data.data.list && res.data.data.list.length > 0) {
					let list = res.data.data.list;
					let match = list.find(r =>
						r.jiaohuanrenzhanghao === this.detail.gongjirenzhanghao ||
						r.jiaohuanrenzhanghao === this.detail.yonghuzhanghao
					);
					this.goToReview((match || list[0]).id);
				} else {
					this.autoCreateReview();
				}
			}).catch(() => {
				this.autoCreateReview();
			});
		},
		goToReview(reviewId) {
			this.$router.push({ path: '/main/pingjiafankuiDetail', query: { id: reviewId, centerType: this.centerType ? 1 : 0 } });
		},
		autoCreateReview() {
			let userTableName = localStorage.getItem('UserTableName');
			this.$http.get(userTableName + '/session').then(userRes => {
				let json = userRes.data.code == 0 ? userRes.data.data : {};
				let reviewData = {
					yonghuzhanghao: this.detail.yonghuzhanghao || '',
					yonghuxingming: this.detail.yonghuxingming || '',
					shoujihao: this.detail.shoujihao || '',
					biaoti: this.detail.biaoti || '',
					jinengfenlei: this.detail.jinengfenlei || '',
					jiaohuanjineng: this.detail.jiaohuanjineng || '',
					jiaohuanrenzhanghao: json.yonghuzhanghao || '',
					jiaohuanrenxingming: json.yonghuxingming || '',
					lianxifangshi: json.shoujihao || '',
					pingjiajieguo: '好评',
					pingjiashijian: new Date().toISOString().split('T')[0],
				};
				this.$http.post('pingjiafankui/add', reviewData).then(res => {
					if (res.data.code == 0) {
						this.goReview();
					} else {
						this.$message({ type: 'error', message: res.data.msg || '创建评价失败', duration: 1500 });
					}
				});
			}).catch(() => {
				this.$message({ type: 'error', message: '获取用户信息失败', duration: 1500 });
			});
		},
		backClick() {
			if(this.storeupType) {
				history.back()
			} else if(this.centerType) {
				this.$router.push({ path: '/main/center', query: { tab: 'jiaohuanjilu' } });
			} else {
				this.$router.push({ path: '/main/jiaohuanjilu' });
			}
		},
		download(file) {
			if(!file) {
				this.$message({ type: 'error', message: '文件不存在', duration: 1500 });
				return;
			}
			let arr = file.replace(new RegExp('upload/', 'g'), '');
			axios.get(this.baseUrl + 'file/download?fileName=' + arr, {
				headers: { token: localStorage.getItem('frontToken') },
				responseType: 'blob'
			}).then(({ data }) => {
				const objectUrl = window.URL.createObjectURL(new Blob([data], { type: 'application/pdf;chartset=UTF-8' }));
				const a = document.createElement('a');
				a.href = objectUrl;
				a.download = arr;
				a.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
				window.URL.revokeObjectURL(data);
			});
		},
		getUUID() { return new Date().getTime(); },
	},
	components: {}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$purple: #38bdf8;
$green: #34d399;
$gold: #f59e0b;
$red: #fb7185;
$text-dark: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: #64748b;
$border: rgba(14,165,233,0.12);
$bg: #0c1222;
$white: #0f172a;
$radius: 14px;

.apply-detail-page {
	max-width: 900px;
	margin: 0 auto;
	padding: 12px 20px 40px;
	min-height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
}

.page-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 12px;
	flex-shrink: 0;
	.header-left { flex: 1; }
	.header-right {
		display: flex; align-items: center; gap: 10px;
	}
	.status-badge {
		display: inline-flex; align-items: center; gap: 4px;
		padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600;
		.el-icon { font-size: 13px; }
		&.done { background: rgba(52,211,153,0.12); color: #4ade80; border: 1px solid rgba(52,211,153,0.25); }
	}
	.back-btn {
		border-radius: 20px; border: 1px solid $border; color: $text-sub; font-size: 12px; padding: 4px 12px;
		&:hover { color: $primary; border-color: $primary; background: rgba(14,165,233,0.04); }
	}
}

.exchange-flow {
	display: flex;
	align-items: stretch;
	gap: 0;
	flex-shrink: 0;
}

.flow-card {
	flex: 1;
	min-width: 0;
	background: $white;
	border-radius: $radius;
	border: 1px solid $border;
	box-shadow: 0 2px 12px rgba(0,0,0,0.04);
	padding: 16px 14px 12px;
	display: flex;
	flex-direction: column;
	align-items: center;
	text-align: center;
	position: relative;
}

.card-label {
	position: absolute;
	top: 0;
	left: 50%;
	transform: translateX(-50%);
	padding: 2px 12px;
	border-radius: 0 0 8px 8px;
	font-size: 11px;
	font-weight: 700;
	letter-spacing: 0.5px;
	white-space: nowrap;
	.el-icon { margin-right: 3px; vertical-align: middle; }
}
.applicant-card .card-label {
	background: linear-gradient(135deg, $primary, #6d5cfc);
	color: #fff;
}
.owner-card .card-label {
	background: linear-gradient(135deg, $purple, #8b6ffa);
	color: #fff;
}

.card-avatar {
	width: 48px;
	height: 48px;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin: 12px 0 6px;
	background: linear-gradient(135deg, rgba(14,165,233,0.1), rgba(14,165,233,0.2));
	.el-icon { font-size: 24px; color: $primary; }
	&.owner {
		background: linear-gradient(135deg, rgba(167,139,250,0.1), rgba(167,139,250,0.2));
		.el-icon { color: $purple; }
	}
}

.card-name {
	font-size: 15px;
	font-weight: 700;
	color: $text-dark;
	margin-bottom: 6px;
}

.card-tags {
	display: flex;
	flex-wrap: wrap;
	gap: 6px;
	justify-content: center;
	.mini-tag {
		font-size: 11px;
		color: $text-sub;
		background: $bg;
		padding: 2px 8px;
		border-radius: 4px;
		border: 1px solid rgba(14,165,233,0.08);
		display: inline-flex;
		align-items: center;
		gap: 3px;
		.el-icon { font-size: 11px; color: $text-dim; }
	}
}

.provided-skill {
	margin-top: 8px;
	padding: 6px 10px;
	background: rgba(14,165,233,0.04);
	border: 1px solid rgba(14,165,233,0.12);
	border-radius: 8px;
	text-align: center;
	.provided-label {
		font-size: 10px;
		color: $text-dim;
		font-weight: 600;
		text-transform: uppercase;
		letter-spacing: 1px;
		margin-bottom: 2px;
	}
	.provided-value {
		font-size: 13px;
		font-weight: 600;
		color: $primary;
	}
}

.flow-center {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 0 10px;
	flex-shrink: 0;
	width: 200px;
	align-self: center;
	gap: 8px;
}

.skill-item {
	width: 100%;
	padding: 8px 12px;
	border-radius: 10px;
	text-align: center;
	position: relative;
	.skill-item-label {
		font-size: 10px;
		color: $text-dim;
		font-weight: 600;
		letter-spacing: 0.5px;
		margin-bottom: 3px;
	}
	.skill-item-name {
		font-size: 13px;
		font-weight: 700;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	.skill-item-arrow {
		position: absolute;
		top: 50%;
		transform: translateY(-50%);
		.el-icon { font-size: 14px; }
	}
	&.left-skill {
		background: rgba(14,165,233,0.06);
		border: 1px solid rgba(14,165,233,0.15);
		.skill-item-name { color: $primary; }
		.skill-item-arrow {
			right: -18px;
			.el-icon { color: rgba(14,165,233,0.4); }
		}
	}
	&.right-skill {
		background: rgba(167,139,250,0.06);
		border: 1px solid rgba(167,139,250,0.15);
		.skill-item-name { color: $purple; }
		.skill-item-arrow {
			left: -18px;
			.el-icon { color: rgba(167,139,250,0.4); transform: rotate(180deg); }
		}
	}
}

.exchange-badge {
	display: flex;
	align-items: center;
	gap: 6px;
	padding: 4px 12px;
	border-radius: 20px;
	background: linear-gradient(135deg, rgba(52,211,153,0.1), rgba(14,165,233,0.08));
	border: 1px solid rgba(52,211,153,0.2);
	white-space: nowrap;
	.el-icon {
		font-size: 14px;
		color: $green;
	}
	span {
		font-size: 11px;
		font-weight: 600;
		color: $green;
	}
}


.detail-bottom {
	margin-top: 10px;
	flex: 1;
	min-height: 0;
	display: flex;
	flex-direction: column;
	gap: 8px;
}

.remark-card {
	display: flex;
	gap: 10px;
	align-items: flex-start;
	background: $white;
	border-radius: 10px;
	border: 1px solid $border;
	padding: 10px 14px;
	box-shadow: 0 1px 6px rgba(0,0,0,0.03);
	.remark-icon {
		width: 32px;
		height: 32px;
		border-radius: 8px;
		background: linear-gradient(135deg, rgba(14,165,233,0.08), rgba(14,165,233,0.15));
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		.el-icon { font-size: 15px; color: $primary; }
	}
	.remark-body { flex: 1; min-width: 0; }
	.remark-title { font-size: 11px; color: $text-dim; margin-bottom: 2px; font-weight: 600; }
	.remark-text { font-size: 13px; color: $text-dark; line-height: 1.5; white-space: pre-wrap; }
}

.media-row {
	display: flex;
	gap: 12px;
	align-items: center;
	.media-item {
		video {
			max-width: 280px;
			max-height: 140px;
			border-radius: 10px;
			display: block;
			background: #000;
		}
	}
	.dl-btn {
		border-radius: 8px;
		background: linear-gradient(135deg, $primary, $purple);
		border: none;
		color: #fff;
		font-weight: 500;
		&:hover { box-shadow: 0 3px 12px rgba(14,165,233,0.25); }
	}
}

.bottom-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-top: auto;
	padding-top: 8px;
}

.time-info {
	font-size: 12px;
	color: $text-dim;
	display: flex;
	align-items: center;
	gap: 4px;
	.el-icon { font-size: 13px; vertical-align: middle; }
}

.action-bar {
	display: flex;
	gap: 8px;
	flex-wrap: wrap;
	.act-btn { border-radius: 8px; font-size: 12px; padding: 7px 16px; font-weight: 500; transition: all 0.3s; }
	.act-review { background: linear-gradient(135deg, $gold, #d97706); border: none; color: #fff; &:hover { box-shadow: 0 3px 12px rgba(245,158,11,0.3); } }
}

.review-section {
	margin-top: 16px;
	background: $white;
	border-radius: $radius;
	border: 1px solid $border;
	padding: 16px;
}

.section-title {
	font-size: 15px;
	font-weight: 700;
	color: $text-dark;
	margin-bottom: 14px;
	padding-bottom: 10px;
	border-bottom: 1px solid rgba(14,165,233,0.08);
	.el-icon { color: $primary; margin-right: 6px; vertical-align: middle; }
}

.existing-review {
	background: $bg;
	border-radius: 10px;
	border: 1px solid rgba(14,165,233,0.08);
	padding: 12px 14px;
	margin-bottom: 14px;
}

.review-meta {
	display: flex;
	align-items: center;
	gap: 10px;
	margin-bottom: 8px;
	flex-wrap: wrap;
	.reviewer-name {
		font-size: 13px; font-weight: 600; color: $text-dark;
		.el-icon { margin-right: 3px; color: $primary; vertical-align: middle; }
	}
	.review-result {
		font-size: 11px; font-weight: 600; padding: 2px 10px; border-radius: 12px;
		display: inline-flex; align-items: center; gap: 3px;
		&.good { background: rgba(52,211,153,0.12); color: $green; }
		&.bad { background: rgba(251,113,133,0.12); color: $red; }
		&.normal { background: rgba(245,158,11,0.12); color: $gold; }
	}
	.review-time { font-size: 11px; color: $text-dim; margin-left: auto; }
}

.review-body {
	font-size: 13px; color: $text-sub; line-height: 1.6; white-space: pre-wrap;
}

.review-reply {
	margin-top: 10px;
	padding: 10px 12px;
	background: rgba(14,165,233,0.04);
	border-radius: 8px;
	border-left: 3px solid $primary;
	.reply-label {
		font-size: 11px; color: $text-dim; font-weight: 600; margin-bottom: 4px;
		.el-icon { margin-right: 3px; vertical-align: middle; }
	}
	.reply-text { font-size: 13px; color: $text-dark; line-height: 1.6; white-space: pre-wrap; }
}

.no-review {
	text-align: center;
	padding: 16px;
	color: $text-dim;
	font-size: 13px;
	.el-icon { margin-right: 4px; vertical-align: middle; }
}

.comment-form {
	background: $bg;
	border-radius: 10px;
	border: 1px solid rgba(14,165,233,0.08);
	padding: 14px;
	.comment-form-title {
		font-size: 13px; font-weight: 600; color: $text-dark; margin-bottom: 10px;
	}
	.comment-form-row {
		display: flex; align-items: center; gap: 10px; margin-bottom: 10px;
		label { font-size: 13px; color: $text-sub; white-space: nowrap; }
		::v-deep .el-select { width: 140px; }
		::v-deep .el-input__inner {
			background: rgba(14,165,233,0.04); border: 1px solid rgba(14,165,233,0.15);
			border-radius: 8px; color: $text-dark;
		}
	}
	::v-deep .el-textarea__inner {
		background: rgba(14,165,233,0.04); border: 1px solid rgba(14,165,233,0.15);
		border-radius: 8px; color: $text-dark; font-size: 13px;
		&:focus { border-color: $primary; }
		&::placeholder { color: $text-dim; }
	}
	.comment-form-actions {
		margin-top: 10px; text-align: right;
	}
	.comment-submit-btn {
		background: linear-gradient(135deg, $primary, $purple);
		border: none; color: #fff; border-radius: 8px; font-size: 12px; font-weight: 500; padding: 8px 20px;
		&:hover { box-shadow: 0 3px 12px rgba(14,165,233,0.3); }
	}
}
</style>
