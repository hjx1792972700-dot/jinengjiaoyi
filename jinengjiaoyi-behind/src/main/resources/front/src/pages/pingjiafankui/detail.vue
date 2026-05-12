<template>
	<div class="apply-detail-page">
		<div class="page-header">
			<div class="header-left">
				<el-breadcrumb :separator="'/'">
					<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
					<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a @click="backClick">{{item.name}}</a></el-breadcrumb-item>
					<el-breadcrumb-item class="item3"><a href="javascript:void(0);">评价详情</a></el-breadcrumb-item>
				</el-breadcrumb>
			</div>
			<div class="header-right">
				<span class="status-badge good" v-if="detail.pingjiajieguo=='好评'"><el-icon><StarFilled /></el-icon> 好评</span>
				<span class="status-badge bad" v-else-if="detail.pingjiajieguo=='差评'"><el-icon><Star /></el-icon> 差评</span>
				<span class="status-badge normal" v-else-if="detail.pingjiajieguo=='一般'"><el-icon><Minus /></el-icon> 一般</span>
				<span class="status-badge audit-pass" v-if="centerType && detail.sfsh=='是'"><el-icon><CircleCheck /></el-icon> 已通过</span>
				<span class="status-badge audit-fail" v-if="centerType && detail.sfsh=='否'"><el-icon><CircleClose /></el-icon> 未通过</span>
				<span class="status-badge audit-pending" v-if="centerType && (detail.sfsh=='待审核'||!detail.sfsh)"><el-icon><Clock /></el-icon> 待审核</span>
				<el-button class="back-btn" size="small" @click="backClick" :icon="ArrowLeft">返回</el-button>
			</div>
		</div>

		<div class="exchange-flow">
			<div class="flow-card applicant-card">
				<div class="card-label"><el-icon><EditPen /></el-icon> 评价人</div>
				<div class="card-avatar">
					<el-icon><UserFilled /></el-icon>
				</div>
				<div class="card-name">{{detail.yonghuxingming || '--'}}</div>
				<div class="card-account">{{detail.yonghuxingming || '--'}}</div>
				<div class="card-tags">
					<span class="mini-tag"><el-icon><Phone /></el-icon> {{detail.shoujihao || '--'}}</span>
				</div>
			</div>

			<div class="flow-center">
				<div class="skill-item left-skill">
					<div class="skill-item-label">评价人技能</div>
					<div class="skill-item-name">{{detail.biaoti || '--'}}</div>
				</div>
				<div class="exchange-badge">
					<el-icon><StarFilled /></el-icon>
					<span>评价反馈</span>
				</div>
				<div class="skill-item right-skill">
					<div class="skill-item-label">被评价人技能</div>
					<div class="skill-item-name">{{detail.jiaohuanjineng || '--'}}</div>
				</div>
			</div>

			<div class="flow-card owner-card">
				<div class="card-label"><el-icon><User /></el-icon> 被评价人</div>
				<div class="card-avatar owner">
					<el-icon><UserFilled /></el-icon>
				</div>
				<div class="card-name">{{detail.jiaohuanrenxingming || '--'}}</div>
				<div class="card-account">{{detail.jiaohuanrenxingming || '--'}}</div>
				<div class="card-tags">
					<span class="mini-tag"><el-icon><Phone /></el-icon> {{detail.lianxifangshi || '--'}}</span>
				</div>
			</div>
		</div>

		<div class="detail-bottom">
			<div class="remark-card" v-if="detail.jiaohuanbeizhu">
				<div class="remark-icon"><el-icon><ChatLineSquare /></el-icon></div>
				<div class="remark-body">
					<div class="remark-title">交换备注</div>
					<div class="remark-text">{{detail.jiaohuanbeizhu}}</div>
				</div>
			</div>

			<div class="remark-card reply-card" v-if="detail.huifuneirong">
				<div class="remark-icon reply"><el-icon><ChatDotRound /></el-icon></div>
				<div class="remark-body">
					<div class="remark-title">回复内容</div>
					<div class="remark-text">{{detail.huifuneirong}}</div>
				</div>
			</div>

			<div class="remark-card audit-card" v-if="centerType && detail.shhf">
				<div class="remark-icon audit"><el-icon><Document /></el-icon></div>
				<div class="remark-body">
					<div class="remark-title">审核回复</div>
					<div class="remark-text">{{detail.shhf}}</div>
				</div>
			</div>

			<div class="media-row" v-if="detailBanner.length > 1 || detail.huifushipin || detail.huifufujian">
				<div class="media-item thumbs" v-if="detailBanner.length > 1">
					<div class="thumb" v-for="(img, idx) in detailBanner" :key="idx" @click="currentImg = idx">
						<img v-if="img.substr(0,4)=='http'" :src="img" />
						<img v-else :src="baseUrl + img" />
					</div>
				</div>
				<div class="media-item" v-if="detail.huifushipin">
					<video :src="baseUrl + detail.huifushipin" controls></video>
				</div>
				<div class="media-item" v-if="detail.huifufujian">
					<el-button class="dl-btn" size="small" :icon="Download" @click="download(detail.huifufujian)">下载附件</el-button>
				</div>
			</div>

			<div class="reply-form-card">
				<div class="reply-form-title"><el-icon><Edit /></el-icon> {{detail.huifuneirong ? '追加回复' : '回复评价'}}</div>
				<el-input
					type="textarea"
					:rows="3"
					v-model="replyContent"
					:placeholder="detail.huifuneirong ? '追加回复内容...' : '输入回复内容...'"
				></el-input>
				<div class="reply-form-actions">
					<el-button class="reply-submit-btn" size="small" :icon="Promotion" @click="submitReply" :loading="replyLoading">提交回复</el-button>
				</div>
			</div>

			<div class="bottom-row">
				<div class="time-info">
					<el-icon><Clock /></el-icon> 评价时间：{{detail.pingjiashijian || '--'}}
				</div>
			
			</div>
		</div>

		<el-dialog title="审核" v-model="sfshVisiable" width="50%">
			<el-form ref="shForm" :model="shForm" label-width="80px">
				<el-form-item label="审核状态">
					<el-select v-model="shForm.sfsh" placeholder="审核状态">
						<el-option label="通过" value="是"></el-option>
						<el-option label="不通过" value="否"></el-option>
						<el-option label="待审核" value="待审核"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="内容">
					<el-input type="textarea" :rows="8" v-model="shForm.shhf"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<div class="dialog-footer">
					<el-button @click="sfshVisiable = false">取 消</el-button>
					<el-button type="primary" @click="shHandler">确 定</el-button>
				</div>
			</template>
		</el-dialog>
	</div>
</template>

<script>
import axios from 'axios'
import {
	ArrowLeft,
	StarFilled,
	Star,
	Minus,
	CircleCheck,
	CircleClose,
	Clock,
	EditPen,
	UserFilled,
	User,
	Phone,
	ChatLineSquare,
	ChatDotRound,
	Document,
	Download,
	Edit,
	Promotion,
} from '@element-plus/icons-vue'
export default {
	components: {
		ArrowLeft,
		StarFilled,
		Star,
		Minus,
		CircleCheck,
		CircleClose,
		Clock,
		EditPen,
		UserFilled,
		User,
		Phone,
		ChatLineSquare,
		ChatDotRound,
		Document,
		Download,
		Edit,
		Promotion,
	},
	data() {
		return {
			tablename: 'pingjiafankui',
			baseUrl: '',
			breadcrumbItem: [{ name: '评价反馈' }],
			title: '',
			detailBanner: [],
			currentImg: 0,
			userid: Number(localStorage.getItem('frontUserid')),
			id: 0,
			detail: {},
			replyContent: '',
			replyLoading: false,
			tabsNum: 0,
			activeName: '1',
			sfshVisiable: false,
			shForm: { sfsh: '是', shhf: '' },
			centerType: false,
			storeupType: false,
			sensitiveWordsArr: [],
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
					if(this.detail.huifutupian) {
						let raw = this.detail.huifutupian;
						if(raw.indexOf(',w') > -1 && raw.split(',w').length > 1 && raw.split(',w')[1].match(/^[_\d]/)) {
							this.detailBanner = [raw];
						} else {
							this.detailBanner = raw.split(',').map(s => s.trim()).filter(s => s);
						}
					} else {
						this.detailBanner = [];
					}
					this.$forceUpdate();
				}
			});
		},
		getSensitiveWords() {
			this.$http.get('sensitivewords/detail/1').then(rs => {
				this.sensitiveWordsArr = rs.data.data.content.split(',')
			})
		},
		onSH() {
			this.sfshVisiable = true;
		},
		shHandler() {
			this.$confirm('确定操作?', '提示', {
				confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
			}).then(async () => {
				this.detail.sfsh = this.shForm.sfsh;
				this.detail.shhf = this.shForm.shhf;
				await this.$http.post(this.tablename + '/update', this.detail).then(async res => {
					if(res.data.code == 0) {
						this.sfshVisiable = false;
						this.$message({ message: '操作成功', type: 'success', duration: 1500 });
					}
				});
			});
		},
		backClick() {
			if(this.storeupType) {
				history.back()
			} else if(this.centerType) {
				this.$router.push({ path: '/main/center', query: { tab: 'pingjiafankui' } });
			} else {
				this.$router.push({ path: '/main/pingjiafankui' });
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
		btnAuth(tableName, key) {
			if(this.centerType) { return this.isBackAuth(tableName, key) }
			else { return this.isAuth(tableName, key) }
		},
		submitReply() {
			if (!this.replyContent.trim()) {
				this.$message({ type: 'warning', message: '请输入回复内容', duration: 1500 });
				return;
			}
			this.replyLoading = true;
			let newReply = this.replyContent.trim();
			if (this.detail.huifuneirong) {
				newReply = this.detail.huifuneirong + '\n---\n' + newReply;
			}
			let updateData = Object.assign({}, this.detail, { huifuneirong: newReply });
			this.$http.post('pingjiafankui/update', updateData).then(res => {
				this.replyLoading = false;
				if (res.data.code == 0) {
					this.$message({ type: 'success', message: '回复成功', duration: 1500 });
					this.detail.huifuneirong = newReply;
					this.replyContent = '';
				} else {
					this.$message({ type: 'error', message: res.data.msg || '回复失败', duration: 1500 });
				}
			}).catch(() => {
				this.replyLoading = false;
				this.$message({ type: 'error', message: '网络错误', duration: 1500 });
			});
		},
		editClick() {
			this.$router.push('/main/pingjiafankuiAdd?type=edit&&id=' + this.detail.id);
		},
		async delClick() {
			await this.$confirm('是否删除此评价反馈？').then(_ => {
				this.$http.post('pingjiafankui/delete', [this.detail.id]).then(async res => {
					if(res.data.code == 0) {
						this.$message({ type: 'success', message: '删除成功!', duration: 1500, onClose: () => { history.back() } });
					}
				});
			}).catch(_ => {});
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
	padding: 16px 20px 40px;
	min-height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
}

.page-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 16px;
	flex-shrink: 0;
	.header-left { flex: 1; }
	.header-right {
		display: flex; align-items: center; gap: 10px;
	}
	.status-badge {
		display: inline-flex; align-items: center; gap: 4px;
		padding: 5px 14px; border-radius: 20px; font-size: 13px; font-weight: 600;
		i { font-size: 14px; }
		&.good { background: rgba(52,211,153,0.12); color: #34d399; border: 1px solid rgba(52,211,153,0.25); }
		&.bad { background: rgba(251,113,133,0.12); color: #fb7185; border: 1px solid rgba(251,113,133,0.25); }
		&.normal { background: rgba(245,158,11,0.12); color: $gold; border: 1px solid rgba(245,158,11,0.25); }
		&.audit-pass { background: rgba(52,211,153,0.12); color: #34d399; border: 1px solid rgba(52,211,153,0.25); }
		&.audit-fail { background: rgba(251,113,133,0.12); color: #fb7185; border: 1px solid rgba(251,113,133,0.25); }
		&.audit-pending { background: rgba(245,158,11,0.12); color: $gold; border: 1px solid rgba(245,158,11,0.25); }
	}
	.back-btn {
		border-radius: 20px; border: 1px solid $border; color: $text-sub; font-size: 12px; padding: 5px 14px;
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
	padding: 20px 16px;
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
	padding: 3px 14px;
	border-radius: 0 0 8px 8px;
	font-size: 11px;
	font-weight: 700;
	letter-spacing: 0.5px;
	white-space: nowrap;
	i { margin-right: 3px; }
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
	width: 56px;
	height: 56px;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	margin: 16px 0 10px;
	background: linear-gradient(135deg, rgba(14,165,233,0.1), rgba(14,165,233,0.2));
	i { font-size: 28px; color: $primary; }
	&.owner {
		background: linear-gradient(135deg, rgba(167,139,250,0.1), rgba(167,139,250,0.2));
		i { color: $purple; }
	}
}

.card-name {
	font-size: 16px;
	font-weight: 700;
	color: $text-dark;
	margin-bottom: 2px;
}

.card-account {
	font-size: 12px;
	color: $text-dim;
	margin-bottom: 10px;
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
		padding: 3px 8px;
		border-radius: 4px;
		border: 1px solid rgba(14,165,233,0.08);
		display: inline-flex;
		align-items: center;
		gap: 3px;
		i { font-size: 11px; color: $text-dim; }
	}
}

.flow-center {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 0 10px;
	flex-shrink: 0;
	width: 220px;
	align-self: center;
	gap: 10px;
}

.skill-item {
	width: 100%;
	padding: 10px 14px;
	border-radius: 10px;
	text-align: center;
	.skill-item-label {
		font-size: 10px;
		color: $text-dim;
		font-weight: 600;
		letter-spacing: 0.5px;
		margin-bottom: 4px;
	}
	.skill-item-name {
		font-size: 13px;
		font-weight: 700;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}
	&.left-skill {
		background: rgba(14,165,233,0.06);
		border: 1px solid rgba(14,165,233,0.15);
		.skill-item-name { color: $primary; }
	}
	&.right-skill {
		background: rgba(167,139,250,0.06);
		border: 1px solid rgba(167,139,250,0.15);
		.skill-item-name { color: #38bdf8; }
	}
}

.exchange-badge {
	display: flex;
	align-items: center;
	gap: 6px;
	padding: 5px 14px;
	border-radius: 20px;
	background: linear-gradient(135deg, rgba(245,158,11,0.1), rgba(251,113,133,0.06));
	border: 1px solid rgba(245,158,11,0.2);
	white-space: nowrap;
	i { font-size: 14px; color: $gold; }
	span { font-size: 11px; font-weight: 600; color: $gold; }
}

.detail-bottom {
	margin-top: 14px;
	flex: 1;
	min-height: 0;
	display: flex;
	flex-direction: column;
	gap: 10px;
}

.remark-card {
	display: flex;
	gap: 12px;
	align-items: flex-start;
	background: $white;
	border-radius: 10px;
	border: 1px solid $border;
	padding: 12px 16px;
	box-shadow: 0 1px 6px rgba(0,0,0,0.03);
	.remark-icon {
		width: 36px;
		height: 36px;
		border-radius: 8px;
		background: linear-gradient(135deg, rgba(14,165,233,0.08), rgba(14,165,233,0.15));
		display: flex;
		align-items: center;
		justify-content: center;
		flex-shrink: 0;
		i { font-size: 16px; color: $primary; }
		&.reply {
			background: linear-gradient(135deg, rgba(52,211,153,0.08), rgba(52,211,153,0.15));
			i { color: $green; }
		}
		&.audit {
			background: linear-gradient(135deg, rgba(245,158,11,0.08), rgba(245,158,11,0.15));
			i { color: $gold; }
		}
	}
	.remark-body { flex: 1; min-width: 0; }
	.remark-title { font-size: 12px; color: $text-dim; margin-bottom: 2px; font-weight: 600; }
	.remark-text { font-size: 13px; color: $text-dark; line-height: 1.6; white-space: pre-wrap; }
}

.media-row {
	display: flex;
	gap: 12px;
	align-items: flex-start;
	flex-wrap: wrap;
	.media-item {
		video {
			max-width: 320px;
			max-height: 180px;
			border-radius: 10px;
			display: block;
			background: #000;
		}
	}
	.thumbs {
		display: flex;
		gap: 8px;
		flex-wrap: wrap;
		.thumb {
			width: 64px;
			height: 48px;
			border-radius: 8px;
			overflow: hidden;
			cursor: pointer;
			border: 2px solid transparent;
			transition: all 0.25s;
			&:hover { border-color: $primary; box-shadow: 0 2px 8px rgba(14,165,233,0.2); }
			img { width: 100%; height: 100%; object-fit: cover; display: block; }
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

.reply-form-card {
	background: $white;
	border-radius: 10px;
	border: 1px solid $border;
	padding: 14px 16px;
	.reply-form-title {
		font-size: 13px;
		font-weight: 600;
		color: $text-dark;
		margin-bottom: 10px;
		i { color: $primary; margin-right: 4px; }
	}
	::v-deep .el-textarea__inner {
		background: $bg;
		border: 1px solid rgba(14,165,233,0.15);
		border-radius: 8px;
		color: $text-dark;
		font-size: 13px;
		&:focus { border-color: $primary; }
		&::placeholder { color: $text-dim; }
	}
	.reply-form-actions {
		margin-top: 10px;
		text-align: right;
	}
	.reply-submit-btn {
		background: linear-gradient(135deg, $primary, $purple);
		border: none;
		color: #fff;
		border-radius: 8px;
		font-size: 12px;
		font-weight: 500;
		padding: 8px 20px;
		&:hover { box-shadow: 0 3px 12px rgba(14,165,233,0.3); }
	}
}

.bottom-row {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-top: auto;
	padding-top: 10px;
}

.time-info {
	font-size: 12px;
	color: $text-dim;
	display: flex;
	align-items: center;
	gap: 4px;
	i { font-size: 13px; }
}

.action-bar {
	display: flex;
	gap: 8px;
	flex-wrap: wrap;
	.act-btn { border-radius: 8px; font-size: 12px; padding: 7px 14px; font-weight: 500; transition: all 0.3s; }
	.act-edit { background: linear-gradient(135deg, $primary, $purple); border: none; color: #fff; &:hover { box-shadow: 0 3px 12px rgba(14,165,233,0.25); } }
	.act-del { background: $white; border: 1px solid rgba(251,113,133,0.4); color: $red; &:hover { background: rgba(251,113,133,0.06); border-color: $red; } }
	.act-audit { background: $white; border: 1px solid $gold; color: #d97706; &:hover { background: rgba(245,158,11,0.06); border-color: #d97706; } }
}
</style>
