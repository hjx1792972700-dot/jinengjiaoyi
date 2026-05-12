<template>
	<div class="msg-fullpage">
		<div class="msg-top-bar">
			<div class="msg-top-left">
				<div class="msg-top-icon">
					<svg viewBox="0 0 40 40" width="28" height="28">
						<rect x="4" y="4" width="32" height="32" rx="8" fill="none" stroke="rgba(14,165,233,0.4)" stroke-width="1.5"/>
						<path d="M14 16h12M14 21h8M14 26h10" stroke="#0ea5e9" stroke-width="2" stroke-linecap="round"/>
						<circle cx="30" cy="28" r="5" fill="#7c3aed" opacity="0.3"/>
						<path d="M28 28l2 2 3-3" fill="none" stroke="#7c3aed" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
					</svg>
				</div>
				<div class="msg-top-text">
					<h3>留言反馈</h3>
					<p>分享你的想法，帮助我们改进技能交易体验</p>
				</div>
			</div>
			<div class="msg-top-right">
				<el-tabs v-model="activeName" @tab-click="getInfo(1)" class="msg-tabs">
					<el-tab-pane label="全部留言" name="first"></el-tab-pane>
					<el-tab-pane label="我的留言" name="second"></el-tab-pane>
				</el-tabs>
				<span class="msg-count-badge">{{total}} 条</span>
			</div>
		</div>

		<div class="msg-body-layout">
			<div class="msg-list-panel">
				<div v-if="infoList.length === 0" class="empty-state">
					<el-icon class="msg-empty-icon"><ChatDotSquare /></el-icon>
					<p>暂无留言，快来发表第一条吧</p>
				</div>
				<div class="msg-scroll-area">
					<div v-for="item in infoList" :key="item.id" class="msg-card">
						<div class="msg-head">
							<el-avatar :size="32" :src="item.avatarurl ? ($config.baseUrl + item.avatarurl) : defaultAvatar"></el-avatar>
							<div class="msg-meta">
								<span class="msg-user">{{item.username}}</span>
								<span class="msg-time">{{item.addtime}}</span>
							</div>
							<el-button v-if="userid==item.userid" type="text" class="msg-del" @click="delClick(item.id)"><el-icon><Delete /></el-icon></el-button>
						</div>
						<div class="msg-content">
							<div class="ql-snow ql-editor msg-text" v-html="item.content"></div>
							<div v-if="item.cpicture" class="msg-img">
								<el-image :src="$config.baseUrl + item.cpicture" fit="cover"></el-image>
							</div>
						</div>
						<div v-if="item.reply" class="msg-reply">
							<div class="reply-tag"><el-icon><Service /></el-icon> 管理员回复</div>
							<div class="ql-snow ql-editor msg-text" v-html="item.reply"></div>
							<div v-if="item.rpicture" class="msg-img">
								<el-image :src="$config.baseUrl + item.rpicture" fit="cover"></el-image>
							</div>
						</div>
					</div>
				</div>
				<el-pagination
					v-if="total > pageSize"
					background
					class="pagination"
					:pager-count="5"
					:page-size="pageSize"
					:page-sizes="pageSizes"
					prev-text="<"
					next-text=">"
					:hide-on-single-page="true"
					:layout="'total, prev, pager, next'"
					:total="total"
					@current-change="curChange"
					@prev-click="prevClick"
					@size-change="sizeChange"
					@next-click="nextClick"
				></el-pagination>
			</div>

			<div class="msg-side-panel">
				<div class="compose-card">
					<div class="compose-title"><el-icon><EditPen /></el-icon> 发表留言</div>
					<textarea v-model="plainContent" class="compose-textarea" placeholder="写下你的想法或建议..." rows="3"></textarea>
					<div class="compose-footer">
						<div class="compose-upload">
							<file-upload
								tip="附图"
								action="file/upload"
								:limit="1"
								:multiple="true"
								:fileUrls="form.cpicture?form.cpicture:''"
								@change="cpictureUploadChange"
							></file-upload>
						</div>
						<button class="compose-btn" type="button" @click="submitForm">
							<el-icon><Promotion /></el-icon> 发布
						</button>
					</div>
				</div>

				<div class="tips-card">
					<div class="tips-title"><el-icon><InfoFilled /></el-icon> 温馨提示</div>
					<ul class="tips-list">
						<li>留言内容请文明用语</li>
						<li>可附带一张图片</li>
						<li>管理员会尽快回复</li>
						<li>自己的留言可删除</li>
					</ul>
				</div>

				<div class="skill-tips-card">
					<div class="skill-tips-icon">
						<svg viewBox="0 0 32 32" width="24" height="24">
							<circle cx="16" cy="16" r="14" fill="none" stroke="rgba(14,165,233,0.3)" stroke-width="1"/>
							<path d="M10 20 L14 12 L18 18 L22 10" fill="none" stroke="#0ea5e9" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
						</svg>
					</div>
					<div class="skill-tips-text">
						<span class="skill-tips-label">技能交易小知识</span>
						<span class="skill-tips-content">协同过滤算法会根据你的偏好，为你推荐最匹配的技能服务</span>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	import { ChatDotSquare, Service, EditPen, Promotion, InfoFilled, Delete } from '@element-plus/icons-vue'
	import defaultAvatar from '@/assets/touxiang.png'
	export default {
		components: { ChatDotSquare, Service, EditPen, Promotion, InfoFilled, Delete },
		data() {
			return {
				defaultAvatar,
				form: {
					content: '',
					userid: Number(localStorage.getItem('frontUserid')),
					username: localStorage.getItem('username')
				},
				plainContent: '',
				total: 0,
				pageSize: 6,
				pageSizes: [6, 12, 24],
				totalPage: 1,
				infoList: [],
				userid: localStorage.getItem('frontUserid'),
				sensitiveWordsArr: [],
				activeName: 'first',
			}
		},
		created() {
			this.getInfo(1);
			this.getSensitiveWords()
		},
		mounted() {
			window.scrollTo(0, 0);
			const footer = document.querySelector('.bottom-preview');
			if (footer) footer.style.display = 'none';
			const body = document.querySelector('.body-containers');
			if (body) {
				body.scrollTop = 0;
				body.style.overflow = 'hidden';
				body.style.height = '100vh';
			}
		},
		beforeUnmount() {
			const footer = document.querySelector('.bottom-preview');
			if (footer) footer.style.display = '';
			const body = document.querySelector('.body-containers');
			if (body) {
				body.style.overflow = '';
				body.style.height = '';
			}
		},
		methods: {
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			getInfo(page) {
				let params = { page, limit: this.pageSize, sort: "addtime", order: "desc" }
				if(this.activeName=='second') {
					params.userid = this.userid
				}
				this.$http.get('messages/list', {params}).then(res => {
					if (res.data.code == 0) {
						this.infoList = res.data.data.list;
						this.total = res.data.data.total;
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
					}
				});
			},
			curChange(page) { this.getInfo(page) },
			prevClick(page) { this.getInfo(page) },
			sizeChange(size){ this.pageSize = size; this.getInfo(1) },
			nextClick(page) { this.getInfo(page) },
			cpictureUploadChange(fileUrls) {
				this.form.cpicture = fileUrls;
			},
			submitForm() {
				if (!this.plainContent.trim()) {
					this.$message.error('请输入留言内容')
					return
				}
				this.form.content = '<p>' + this.plainContent.replace(/\n/g, '</p><p>') + '</p>'
				if(this.form.cpicture) {
					this.form.cpicture = this.form.cpicture.replace(/^\/+/, "");
				}
				this.form.avatarurl = localStorage.getItem('frontHeadportrait') || '';
				let content = this.form.content
				for(var i=0; i<this.sensitiveWordsArr.length; i++){
					var reg = new RegExp(this.sensitiveWordsArr[i],"g");
					if (content.indexOf(this.sensitiveWordsArr[i]) > -1) {
						content = content.replace(reg,"**");
					}
				}
				this.form.content = content
				this.$http.post('messages/add', this.form).then(res => {
					if (res.data.code == 0) {
						this.$message({ type: 'success', message: '留言成功!', duration: 1500, onClose: () => {
							this.plainContent = ''
							this.form.content = ''
							this.form.cpicture = ''
							this.getInfo(1)
						}})
					}
				});
			},
			delClick(id){
				this.$confirm('确定要删除这条留言吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(_ => {
					this.$http.post('messages/delete', [id]).then(res => {
						if (res.data.code == 0) {
							this.$message({ type: 'success', message: '删除成功!', duration: 1500, onClose: () => { this.getInfo(1) }})
						}
					});
				}).catch(_ => {});
			},
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$primary-light: #38bdf8;
$purple: #7c3aed;
$bg-deep: #0f172a;
$bg-card: rgba(255,255,255,0.04);
$border-glow: rgba(14,165,233,0.08);
$cyan: #00d4ff;
$text-main: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: #64748b;

.msg-fullpage {
	height: calc(100vh - 102px);
	display: flex;
	flex-direction: column;
	padding: 12px 3% 0;
	background: linear-gradient(180deg, $bg-deep 0%, #101828 100%);
	overflow: hidden;
	box-sizing: border-box;
}

.msg-top-bar {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 10px 16px;
	background: rgba(255,255,255,0.03);
	border: 1px solid $border-glow;
	border-radius: 12px;
	margin-bottom: 12px;
	flex-shrink: 0;

	.msg-top-left {
		display: flex;
		align-items: center;
		gap: 12px;

		.msg-top-icon {
			width: 40px; height: 40px;
			border-radius: 10px;
			background: rgba(14,165,233,0.08);
			display: flex;
			align-items: center;
			justify-content: center;
			flex-shrink: 0;
		}
		.msg-top-text {
			h3 {
				margin: 0;
				font-size: 16px;
				font-weight: 700;
				color: #fff;
				letter-spacing: 1px;
			}
			p {
				margin: 2px 0 0;
				font-size: 11px;
				color: $text-dim;
			}
		}
	}

	.msg-top-right {
		display: flex;
		align-items: center;
		gap: 12px;

		.msg-tabs {
			::v-deep .el-tabs__header { margin: 0; border: none; }
			::v-deep .el-tabs__nav-wrap::after { display: none; }
			::v-deep .el-tabs__item {
				height: 32px;
				line-height: 32px;
				font-size: 13px;
				color: $text-sub;
				border: none !important;
				padding: 0 12px;
				&.is-active { color: $cyan; font-weight: 600; }
				&:hover { color: $cyan; }
			}
			::v-deep .el-tabs__active-bar { background: linear-gradient(90deg, $primary, $purple); height: 2px; }
		}

		.msg-count-badge {
			font-size: 11px;
			color: $cyan;
			background: rgba(14,165,233,0.1);
			padding: 3px 10px;
			border-radius: 10px;
			font-weight: 500;
			white-space: nowrap;
		}
	}
}

.msg-body-layout {
	flex: 1;
	display: flex;
	gap: 12px;
	overflow: hidden;
	min-height: 0;
}

.msg-list-panel {
	flex: 1;
	min-width: 0;
	background: rgba(255,255,255,0.03);
	border: 1px solid $border-glow;
	border-radius: 12px;
	display: flex;
	flex-direction: column;
	overflow: hidden;
}

.msg-scroll-area {
	flex: 1;
	overflow-y: auto;
	padding: 0 16px;
	&::-webkit-scrollbar { width: 4px; }
	&::-webkit-scrollbar-thumb {
		background: rgba(14,165,233,0.15);
		border-radius: 4px;
	}
}

.empty-state {
	text-align: center;
	padding: 40px 0;
	color: $text-dim;
	.msg-empty-icon {
		font-size: 36px; display: block; margin-bottom: 8px; color: rgba(14,165,233,0.3);
	}
	p { font-size: 13px; margin: 0; }
}

.msg-card {
	padding: 14px 0;
	border-bottom: 1px solid rgba(255,255,255,0.04);
	transition: background 0.2s;
	&:last-child { border-bottom: none; }
	&:hover { background: rgba(14,165,233,0.02); margin: 0 -16px; padding-left: 16px; padding-right: 16px; border-radius: 8px; }
}

.msg-head {
	display: flex;
	align-items: center;
	gap: 10px;
	margin-bottom: 8px;

	::v-deep .el-avatar { border: 1.5px solid rgba(14,165,233,0.2); }

	.msg-meta {
		flex: 1;
		display: flex;
		flex-direction: column;
	}
	.msg-user {
		font-size: 13px;
		font-weight: 600;
		color: $text-main;
	}
	.msg-time {
		font-size: 10px;
		color: $text-dim;
		margin-top: 1px;
	}
	.msg-del {
		color: $text-dim !important;
		font-size: 13px;
		padding: 4px;
		&:hover { color: #f56c6c !important; }
	}
}

.msg-content {
	padding-left: 42px;
}

.msg-text {
	padding: 0 !important;
	font-size: 13px;
	line-height: 1.7;
	color: $text-sub;
	min-height: auto !important;
}

.msg-img {
	margin-top: 6px;
	::v-deep .el-image {
		max-width: 160px;
		max-height: 120px;
		border-radius: 8px;
		overflow: hidden;
		cursor: pointer;
		border: 1px solid rgba(255,255,255,0.06);
	}
}

.msg-reply {
	margin: 8px 0 0 42px;
	background: rgba(14,165,233,0.04);
	border-radius: 8px;
	padding: 8px 12px;
	border-left: 3px solid $primary;

	.reply-tag {
		font-size: 11px;
		font-weight: 600;
		color: $primary-light;
		margin-bottom: 4px;
		.el-icon { font-size: 12px; vertical-align: middle; }
	}
}

.pagination {
	padding: 8px 16px;
	display: flex;
	justify-content: center;
	flex-shrink: 0;
	border-top: 1px solid rgba(255,255,255,0.04);

	::v-deep .btn-prev, ::v-deep .btn-next {
		background: rgba(255,255,255,0.04) !important;
		color: $text-sub !important;
	}
	::v-deep .el-pager li {
		background: rgba(255,255,255,0.04) !important;
		color: $text-sub !important;
		&.active {
			background: linear-gradient(135deg, $primary, $purple) !important;
			color: #fff !important;
		}
	}
	::v-deep .el-pagination__total { color: $text-dim !important; font-size: 12px; }
}

.msg-side-panel {
	width: 280px;
	flex-shrink: 0;
	display: flex;
	flex-direction: column;
	gap: 10px;
	overflow-y: auto;
	&::-webkit-scrollbar { width: 3px; }
	&::-webkit-scrollbar-thumb { background: rgba(14,165,233,0.1); border-radius: 3px; }
}

.compose-card {
	background: rgba(255,255,255,0.03);
	border: 1px solid $border-glow;
	border-radius: 12px;
	padding: 14px;

	.compose-title {
		font-size: 14px;
		font-weight: 600;
		color: $text-main;
		margin-bottom: 10px;
		.el-icon { color: $cyan; margin-right: 4px; vertical-align: middle; }
	}

	.compose-textarea {
		width: 100%;
		border: 1px solid rgba(255,255,255,0.08);
		border-radius: 8px;
		padding: 8px 10px;
		font-size: 12px;
		color: $text-main;
		background: rgba(255,255,255,0.03);
		resize: none;
		outline: none;
		font-family: inherit;
		line-height: 1.6;
		transition: border-color 0.25s;
		box-sizing: border-box;
		&:focus { border-color: $primary; box-shadow: 0 0 0 2px rgba(14,165,233,0.1); }
		&::placeholder { color: $text-dim; }
	}

	.compose-footer {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 8px;
	}

	.compose-upload {
		::v-deep .upload .upload-img {
			width: 28px !important;
			height: 28px !important;
			font-size: 14px !important;
			line-height: 28px !important;
			border-radius: 6px !important;
			border: 1px dashed rgba(255,255,255,0.12) !important;
			color: $text-dim !important;
			background: rgba(255,255,255,0.03) !important;
		}
		::v-deep .el-upload-list .el-upload-list__item {
			width: 28px !important;
			height: 28px !important;
			border-radius: 6px !important;
		}
		::v-deep .el-upload__tip {
			font-size: 10px !important;
			color: $text-dim !important;
		}
	}

	.compose-btn {
		display: flex;
		align-items: center;
		gap: 4px;
		height: 30px;
		padding: 0 16px;
		background: linear-gradient(135deg, $primary, $purple);
		color: #fff;
		border: none;
		border-radius: 15px;
		font-size: 12px;
		font-weight: 500;
		cursor: pointer;
		transition: all 0.25s;
		.el-icon { font-size: 13px; vertical-align: middle; }
		&:hover {
			box-shadow: 0 4px 16px rgba(14,165,233,0.3);
			transform: translateY(-1px);
		}
	}
}

.tips-card {
	background: rgba(255,255,255,0.03);
	border: 1px solid $border-glow;
	border-radius: 12px;
	padding: 12px 14px;

	.tips-title {
		font-size: 13px;
		font-weight: 600;
		color: $text-main;
		margin-bottom: 8px;
		.el-icon { color: $cyan; margin-right: 4px; font-size: 14px; vertical-align: middle; }
	}

	.tips-list {
		margin: 0;
		padding: 0 0 0 14px;
		li {
			font-size: 11px;
			color: $text-dim;
			line-height: 1.9;
			&::marker { color: rgba(14,165,233,0.3); }
		}
	}
}

.skill-tips-card {
	background: rgba(14,165,233,0.04);
	border: 1px solid rgba(14,165,233,0.1);
	border-radius: 12px;
	padding: 12px;
	display: flex;
	align-items: flex-start;
	gap: 10px;

	.skill-tips-icon {
		flex-shrink: 0;
		width: 32px; height: 32px;
		border-radius: 8px;
		background: rgba(14,165,233,0.08);
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.skill-tips-text {
		display: flex;
		flex-direction: column;
		gap: 2px;
		.skill-tips-label {
			font-size: 11px;
			font-weight: 600;
			color: $primary-light;
			letter-spacing: 0.5px;
		}
		.skill-tips-content {
			font-size: 10px;
			color: $text-dim;
			line-height: 1.5;
		}
	}
}

@media (max-width: 768px) {
	.msg-body-layout { flex-direction: column-reverse; }
	.msg-side-panel { width: 100%; flex-direction: row; flex-wrap: wrap; }
	.msg-top-bar { flex-direction: column; gap: 8px; align-items: flex-start; }
}
</style>
