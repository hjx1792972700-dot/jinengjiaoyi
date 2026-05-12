<template>
	<div class="fd-page">
		<div class="fd-header">
			<h2 class="fd-title">{{detail.title || '帖子详情'}}</h2>
			<el-button class="fd-back" size="small" plain @click="backClick">
				<i class="el-icon-arrow-left"></i> 返回
			</el-button>
		</div>

		<div class="fd-body">
			<div class="fd-main">
				<div class="fd-card">
					<div class="fd-meta">
						<span class="fd-meta-item" v-if="detail.addtime">
							<i class="el-icon-time"></i> {{detail.addtime}}
						</span>
						<span class="fd-meta-item">
							<i class="el-icon-user"></i> {{detail.isanon==1&&detail.userid!=userid?'匿名':detail.username}}
						</span>
						<span class="fd-meta-item" v-if="detail.typename">
							<i class="el-icon-collection-tag"></i> {{detail.typename}}
						</span>
					</div>

					<div class="fd-covers" v-if="detailBanner.length">
						<div class="fd-cover-item" v-for="(item, idx) in detailBanner" :key="idx">
						<el-image
							:src="item.substr(0,4)=='http' ? item : baseUrl + item"
							fit="cover"
						></el-image>
						</div>
					</div>

					<div class="fd-content ql-snow ql-editor" v-html="detail.content"></div>

					<div class="fd-interact" v-if="detail.id">
						<div class="fd-ibtn" :class="{active: isThumbsup}" @click="thumbsupClick">
							<i class="el-icon-top"></i>
							<span>{{isThumbsup ? '已赞' : '赞'}} {{detail.thumbsupnum||''}}</span>
						</div>
						<div class="fd-ibtn" :class="{active: isCrazilynum}" @click="crazilyClick">
							<el-icon class="fd-icon-flip"><Top /></el-icon>
							<span>{{isCrazilynum ? '已踩' : '踩'}} {{detail.crazilynum||''}}</span>
						</div>
						<div class="fd-ibtn" :class="{active: isStore}" @click="storeupClick">
							<i :class="isStore ? 'el-icon-star-on' : 'el-icon-star-off'"></i>
							<span>{{isStore ? '已藏' : '收藏'}} {{detail.storeupnum||''}}</span>
						</div>
					</div>
				</div>

				<div class="fd-comments">
					<div class="fd-cm-header">
						<span class="fd-cm-title">评论</span>
						<el-button class="fd-cm-add" size="small" @click="addComment">
							<i class="el-icon-edit"></i> 发表评论
						</el-button>
					</div>
					<div class="fd-cm-list">
						<template v-if="commentList && commentList.length">
						<template v-for="item in commentList">
						<div :key="item.id" v-if="item.sfsh=='是'" class="fd-cm-item" @mouseenter="commentEnter(item.id)" @mouseleave="commentLeave">
								<div class="fd-cm-user">
									<img v-if="item.avatarurl" :src="baseUrl + item.avatarurl">
									<img v-else :src="defaultAvatar">
									<span class="fd-cm-name">{{item.username}}</span>
								</div>
								<div class="fd-cm-body ql-snow ql-editor" v-html="item.content"></div>
								<div class="fd-cm-actions" v-if="showIndex==item.id&&showIndex1==-1">
									<el-button size="mini" type="text" @click="replyClick(item.id)">回复</el-button>
									<el-button size="mini" type="text" class="fd-del" v-if="userid==item.userid" @click="commentDel(item.id)">删除</el-button>
								</div>
								<template v-if="item.childs && item.childs.length">
									<div class="fd-cm-replies">
										<div class="fd-cm-reply" v-for="items in item.childs" :key="items.id" @mouseenter="commentEnter1(items.id)" @mouseleave="commentLeave1">
											<div class="fd-cm-user sm">
												<img v-if="items.avatarurl" :src="baseUrl + items.avatarurl">
												<img v-else :src="defaultAvatar">
												<span class="fd-cm-name">{{items.username}}</span>
											</div>
											<div class="fd-cm-body sm ql-snow ql-editor" v-html="items.content"></div>
											<div class="fd-cm-actions" v-if="showIndex==item.id&&userid==items.userid&&showIndex1==items.id">
												<el-button size="mini" type="text" class="fd-del" @click="commentDel(items.id)">删除</el-button>
											</div>
										</div>
									</div>
								</template>
							</div>
						</template>
						</template>
						<div class="fd-cm-empty" v-else>暂无评论，快来抢沙发吧</div>
					</div>
				</div>
			</div>
		</div>

		<el-dialog title="发表评论" :visible.sync="dialogFormVisible" class="fd-dialog" width="500px">
			<el-form :model="form" :rules="rules" ref="form">
				<el-form-item prop="content">
					<el-input type="textarea" v-model="form.content" :rows="4" placeholder="说说你的想法..." maxlength="500" show-word-limit resize="none"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="dialogFormVisible = false" size="small">取消</el-button>
				<el-button type="primary" @click="addForum('form')" size="small">提交</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script>
	import defaultAvatar from '@/assets/touxiang.png'
	export default {

		//数据集合
		data() {
			return {
				defaultAvatar,
				baseUrl: '',
				id: '',
				detail: {},
				commentList: [],
				dialogFormVisible: false,
				detailBanner: [],
				form: {
					content: '',
					parentid: '',
					userid: Number(localStorage.getItem('frontUserid')),
					username: localStorage.getItem('displayName') || localStorage.getItem('username'),
					avatarurl: '',
				},
				userid: Number(localStorage.getItem('frontUserid')),
				isThumbsup: false,
				isCrazilynum: false,
				isStore: false,
				editorOption: {
					modules: {
						toolbar: [
							["bold", "italic", "underline", "strike"],
							["blockquote", "code-block"],
							[{ header: 1 }, { header: 2 }],
							[{ list: "ordered" }, { list: "bullet" }],
							[{ script: "sub" }, { script: "super" }],
							[{ indent: "-1" }, { indent: "+1" }],
							[{ direction: "rtl" }],
							[{ size: ["small", false, "large", "huge"] }],
							[{ header: [1, 2, 3, 4, 5, 6, false] }],
							[{ color: [] }, { background: [] }],
							[{ font: [] }],
							[{ align: [] }],
							["clean"],
							["image", "video"]
						]
					}
				},
				rules: {
					content: [
						{ required: true, message: '请输入评论', trigger: 'blur' }
					]
				},
				showIndex: -1,
				showIndex1: -1,
				sensitiveWordsArr: [],
			}
		},
		created() {
			this.baseUrl = this.$config.baseUrl;
			this.id = this.$route.query.id
			this.getDetail()
			this.getSensitiveWords()
			this.getInteractStatus()
		},
		mounted() {
		},
		//方法集合
		methods: {
			getSensitiveWords(){
				this.$http.get('sensitivewords/detail/1').then(rs=>{
					this.sensitiveWordsArr = rs.data.data.content.split(',')
				})
			},
			// 返回按钮
			backClick(){
				history.back()
			},
			onEditorReady(editor) {
				editor.root.setAttribute('data-placeholder', "请输入内容...");
			},
			getDetail() {
				this.$http.get(`forum/list/${this.id}`).then(res => {
					if (res.data.code == 0) {
						this.detailBanner = res.data.data.cover ? res.data.data.cover.split(",") : [];
						res.data.data.content = res.data.data.content.replace(/img src/gi,"img style=\"max-width:60%;height:auto;\" src");
						this.detail = res.data.data
						this.commentList = res.data.data.childs;
					}
				});
			},
			// 新增评论
			addComment(){
				this.form.content = '';
				this.form.parentid = this.detail.id
				this.dialogFormVisible = true
			},
			// 鼠标移入
			commentEnter(index){
				this.showIndex = index
			},
			// 鼠标移出
			commentLeave(){
				this.showIndex = -1
			},
			// 二级评论鼠标移入
			commentEnter1(index){
				this.showIndex1 = index
			},
			// 二级评论鼠标移出
			commentLeave1(){
				this.showIndex1 = -1
			},
			// 删除评论
			commentDel(id){
				this.$confirm('是否删除此评论？')
				  .then(_ => {
					this.$http.post('forum/delete',[id]).then(res=>{
						if(res.data&&res.data.code==0){
							this.$message({
								type: 'success',
								message: '删除成功!',
								duration: 1500,
								onClose: () => {
									this.getDetail();
								}
							});
						}
					})
				}).catch(_ => {});
			},
			// 回复评论
			replyClick(id){
				this.form.content = '';
				this.form.parentid = id
				this.dialogFormVisible = true
			},
			addForum(formName) {
				for(var i=0; i<this.sensitiveWordsArr.length; i++){
					var reg = new RegExp(this.sensitiveWordsArr[i],"g");
					if (this.form.content.indexOf(this.sensitiveWordsArr[i]) > -1) {
						this.form.content = this.form.content.replace(reg,"**");
					}
				}
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.form.avatarurl = localStorage.getItem('frontHeadportrait')?localStorage.getItem('frontHeadportrait'):'';
						this.$http.post('forum/add', this.form).then(res => {
							if (res.data.code == 0) {
								this.$message({
									type: 'success',
									message: '评论成功!',
									duration: 1500,
									onClose: () => {
										this.form.content = '';
										this.getDetail();
										this.dialogFormVisible = false;
									}
								});
							} else {
								this.$message.error(res.data.msg || '评论失败');
							}
						});
					} else {
						return false;
					}
				});
			},
			getInteractStatus() {
				if (!this.userid) return;
				this.$http.get('storeup/list', { params: { page: 1, limit: 1, userid: this.userid, refid: this.id, tablename: 'forum', type: 1 } }).then(res => {
					if (res.data.code == 0) this.isStore = res.data.data.total > 0;
				}).catch(() => {});
				this.$http.get('storeup/list', { params: { page: 1, limit: 1, userid: this.userid, refid: this.id, tablename: 'forum', type: 21 } }).then(res => {
					if (res.data.code == 0) this.isThumbsup = res.data.data.total > 0;
				}).catch(() => {});
				this.$http.get('storeup/list', { params: { page: 1, limit: 1, userid: this.userid, refid: this.id, tablename: 'forum', type: 22 } }).then(res => {
					if (res.data.code == 0) this.isCrazilynum = res.data.data.total > 0;
				}).catch(() => {});
			},
			thumbsupClick() {
				if (!this.userid) return this.$message.warning('请先登录');
				if (this.isThumbsup) {
					this.$http.get('storeup/list', { params: { page: 1, limit: 1, userid: this.userid, refid: this.id, tablename: 'forum', type: 21 } }).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.$http.post('storeup/delete', [res.data.data.list[0].id]).then(() => {
								this.isThumbsup = false;
								this.detail.thumbsupnum = Math.max(0, (this.detail.thumbsupnum || 1) - 1);
								this.$http.post('forum/update', { id: this.detail.id, thumbsupnum: this.detail.thumbsupnum });
							});
						}
					});
				} else {
					if (this.isCrazilynum) this.crazilyClick();
					this.$http.post('storeup/add', { userid: this.userid, refid: this.id, tablename: 'forum', name: this.detail.title, picture: this.detail.cover || '', type: 21 }).then(res => {
						if (res.data.code == 0) {
							this.isThumbsup = true;
							this.detail.thumbsupnum = (this.detail.thumbsupnum || 0) + 1;
							this.$http.post('forum/update', { id: this.detail.id, thumbsupnum: this.detail.thumbsupnum });
						}
					});
				}
			},
			crazilyClick() {
				if (!this.userid) return this.$message.warning('请先登录');
				if (this.isCrazilynum) {
					this.$http.get('storeup/list', { params: { page: 1, limit: 1, userid: this.userid, refid: this.id, tablename: 'forum', type: 22 } }).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.$http.post('storeup/delete', [res.data.data.list[0].id]).then(() => {
								this.isCrazilynum = false;
								this.detail.crazilynum = Math.max(0, (this.detail.crazilynum || 1) - 1);
								this.$http.post('forum/update', { id: this.detail.id, crazilynum: this.detail.crazilynum });
							});
						}
					});
				} else {
					if (this.isThumbsup) this.thumbsupClick();
					this.$http.post('storeup/add', { userid: this.userid, refid: this.id, tablename: 'forum', name: this.detail.title, picture: this.detail.cover || '', type: 22 }).then(res => {
						if (res.data.code == 0) {
							this.isCrazilynum = true;
							this.detail.crazilynum = (this.detail.crazilynum || 0) + 1;
							this.$http.post('forum/update', { id: this.detail.id, crazilynum: this.detail.crazilynum });
						}
					});
				}
			},
			storeupClick() {
				if (!this.userid) return this.$message.warning('请先登录');
				if (this.isStore) {
					this.$http.get('storeup/list', { params: { page: 1, limit: 1, userid: this.userid, refid: this.id, tablename: 'forum', type: 1 } }).then(res => {
						if (res.data.code == 0 && res.data.data.list.length > 0) {
							this.$http.post('storeup/delete', [res.data.data.list[0].id]).then(() => {
								this.isStore = false;
								this.detail.storeupnum = Math.max(0, (this.detail.storeupnum || 1) - 1);
								this.$http.post('forum/update', { id: this.detail.id, storeupnum: this.detail.storeupnum });
								this.$message.success('已取消收藏');
							});
						}
					});
				} else {
					this.$http.post('storeup/add', { userid: this.userid, refid: this.id, tablename: 'forum', name: this.detail.title, picture: this.detail.cover || '', type: 1 }).then(res => {
						if (res.data.code == 0) {
							this.isStore = true;
							this.detail.storeupnum = (this.detail.storeupnum || 0) + 1;
							this.$http.post('forum/update', { id: this.detail.id, storeupnum: this.detail.storeupnum });
							this.$message.success('收藏成功');
						}
					});
				}
			}
		}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$primary-light: #38bdf8;
$bg-deep: #0c1222;
$bg-card: rgba(15,23,42,0.95);
$border-glow: rgba(14,165,233,0.10);
$text-bright: #e2e8f0;
$text-sub: #94a3b8;
$text-dim: rgba(255,255,255,0.28);

.fd-page {
	max-width: 900px;
	margin: 0 auto;
	padding: 12px 20px 0;
	background: $bg-deep;
	height: calc(100vh - 60px);
	display: flex;
	flex-direction: column;
	overflow: hidden;
}

.fd-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 14px;
	.fd-title {
		font-size: 18px;
		font-weight: 700;
		margin: 0;
		background: linear-gradient(90deg, $text-bright, $primary);
		-webkit-background-clip: text;
		-webkit-text-fill-color: transparent;
		flex: 1;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		margin-right: 12px;
	}
	.fd-back {
		flex-shrink: 0;
		border-color: $border-glow !important;
		color: $text-sub !important;
		background: rgba(255,255,255,0.03) !important;
		border-radius: 18px;
		padding: 5px 14px;
		font-size: 12px;
		&:hover { color: $primary !important; border-color: rgba(14,165,233,0.3) !important; }
	}
}

.fd-body { display: flex; gap: 16px; flex: 1; min-height: 0; overflow-y: auto;
	&::-webkit-scrollbar { width: 4px; }
	&::-webkit-scrollbar-thumb { background: rgba(14,165,233,0.2); border-radius: 4px; }
}

.fd-main { flex: 1; min-width: 0; }

.fd-card {
	background: $bg-card;
	backdrop-filter: blur(16px);
	border: 1px solid $border-glow;
	border-radius: 14px;
	padding: 18px 22px;
	margin-bottom: 14px;
}

.fd-meta {
	display: flex;
	flex-wrap: wrap;
	gap: 14px;
	margin-bottom: 14px;
	padding-bottom: 12px;
	border-bottom: 1px solid rgba(255,255,255,0.05);
	.fd-meta-item {
		font-size: 12px;
		color: $text-sub;
		display: flex;
		align-items: center;
		gap: 4px;
		.el-icon { font-size: 13px; color: rgba(14,165,233,0.5); }
	}
}

.fd-covers {
	display: flex;
	gap: 8px;
	margin-bottom: 14px;
	flex-wrap: wrap;
	.fd-cover-item {
		width: 120px;
		height: 90px;
		border-radius: 8px;
		overflow: hidden;
		border: 1px solid rgba(255,255,255,0.06);
		flex-shrink: 0;
		::v-deep .el-image {
			width: 100%;
			height: 100%;
			display: block;
			img { object-fit: cover; width: 100%; height: 100%; }
		}
	}
}

.fd-content {
	color: $text-bright;
	font-size: 14px;
	line-height: 1.8;
	padding: 0 !important;
	background: none !important;
	::v-deep img {
		max-width: 60% !important;
		width: auto !important;
		border-radius: 8px;
		margin: 6px 0;
	}
	::v-deep p { margin: 0 0 6px; }
}

.fd-interact {
	display: flex;
	gap: 8px;
	margin-top: 14px;
	padding-top: 12px;
	border-top: 1px solid rgba(255,255,255,0.05);
	.fd-ibtn {
		display: flex;
		align-items: center;
		gap: 4px;
		cursor: pointer;
		font-size: 12px;
		color: $text-dim;
		padding: 5px 14px;
		border-radius: 16px;
		border: 1px solid rgba(255,255,255,0.06);
		background: rgba(255,255,255,0.02);
		transition: all 0.25s;
		.el-icon { font-size: 14px; vertical-align: middle; }
		.fd-icon-flip { transform: rotate(180deg); }
		&:hover { color: $primary; border-color: rgba(14,165,233,0.25); background: rgba(14,165,233,0.05); }
		&.active { color: $primary; border-color: rgba(14,165,233,0.3); background: rgba(14,165,233,0.08); font-weight: 600; }
	}
}

.fd-comments {
	background: $bg-card;
	backdrop-filter: blur(16px);
	border: 1px solid $border-glow;
	border-radius: 14px;
	padding: 16px 20px;
}

.fd-cm-header {
	display: flex;
	align-items: center;
	justify-content: space-between;
	margin-bottom: 12px;
	.fd-cm-title {
		font-size: 15px;
		font-weight: 600;
		color: $text-bright;
		padding-left: 10px;
		border-left: 3px solid $primary;
	}
	.fd-cm-add {
		background: linear-gradient(135deg, $primary, $primary-light) !important;
		border: none !important;
		color: #fff !important;
		border-radius: 16px;
		padding: 5px 16px;
		font-size: 12px;
		font-weight: 500;
		&:hover { box-shadow: 0 4px 12px rgba(14,165,233,0.25); }
	}
}

.fd-cm-list {
	.fd-cm-empty {
		text-align: center;
		color: $text-dim;
		font-size: 13px;
		padding: 20px 0;
	}
}

.fd-cm-item {
	padding: 12px;
	margin-bottom: 8px;
	border: 1px solid rgba(255,255,255,0.05);
	border-radius: 10px;
	background: rgba(255,255,255,0.02);
	transition: border-color 0.2s;
	&:hover { border-color: rgba(14,165,233,0.15); }
}

.fd-cm-user {
	display: flex;
	align-items: center;
	gap: 8px;
	margin-bottom: 6px;
	img {
		width: 28px;
		height: 28px;
		border-radius: 50%;
		object-fit: cover;
		border: 1px solid rgba(14,165,233,0.2);
	}
	&.sm img { width: 22px; height: 22px; }
	.fd-cm-name {
		font-size: 12px;
		color: $text-sub;
		font-weight: 500;
	}
}

.fd-cm-body {
	color: $text-bright;
	font-size: 13px;
	line-height: 1.6;
	padding: 0 !important;
	background: none !important;
	&.sm { font-size: 12px; color: $text-sub; }
	::v-deep img { max-width: 50% !important; border-radius: 6px; }
	::v-deep p { margin: 0 0 4px; }
}

.fd-cm-actions {
	display: flex;
	justify-content: flex-end;
	gap: 4px;
	margin-top: 4px;
	::v-deep .el-button--text { color: $primary; font-size: 11px; padding: 2px 8px; }
	.fd-del ::v-deep .el-button--text,
	&  .fd-del { color: #fb7185 !important; }
}

.fd-cm-replies {
	margin: 6px 0 0 36px;
	.fd-cm-reply {
		padding: 8px 10px;
		margin-bottom: 4px;
		background: rgba(14,165,233,0.03);
		border: 1px solid rgba(255,255,255,0.04);
		border-radius: 8px;
	}
}

::v-deep .fd-dialog {
	border-radius: 14px !important;
	background: #0f172a !important;
	border: 1px solid $border-glow;
	.el-dialog__header {
		border-bottom: 1px solid rgba(255,255,255,0.06);
		.el-dialog__title { color: $text-bright; font-size: 15px; }
		.el-dialog__headerbtn .el-dialog__close { color: $text-sub; }
	}
	.el-dialog__body { padding: 16px 20px; }
	.el-dialog__footer {
		border-top: 1px solid rgba(255,255,255,0.06);
		padding: 12px 20px;
	}
}

::v-deep .fd-dialog {
	.el-textarea__inner {
		background: rgba(255,255,255,0.04) !important;
		border: 1px solid rgba(255,255,255,0.1) !important;
		border-radius: 10px !important;
		color: $text-bright !important;
		font-size: 14px;
		padding: 12px 14px;
		&:focus { border-color: rgba(14,165,233,0.35) !important; box-shadow: 0 0 10px rgba(14,165,233,0.08); }
		&::placeholder { color: $text-dim !important; }
	}
	.el-input__count { background: transparent !important; color: $text-dim !important; }
	.el-dialog__footer .el-button--primary {
		background: linear-gradient(135deg, $primary, $primary-light) !important;
		border: none !important;
		border-radius: 16px;
	}
	.el-dialog__footer .el-button--default {
		background: transparent !important;
		border: 1px solid rgba(255,255,255,0.1) !important;
		color: $text-sub !important;
		border-radius: 16px;
	}
}

video {
	border: 0;
	border-radius: 8px;
	margin: 6px auto;
	outline: none;
	display: block;
	max-width: 60%;
}
</style>
