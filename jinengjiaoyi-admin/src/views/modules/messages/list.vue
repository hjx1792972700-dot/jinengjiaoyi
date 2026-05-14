<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<template v-if="showFlag">
			<div class="msg-toolbar">
				<el-input v-model="searchForm.username" placeholder="搜索用户名" size="small" clearable style="width:180px" @keyup.enter="search">
					<template #prefix>
						<i class="el-icon-search"></i>
					</template>
				</el-input>
				<el-select v-model="searchForm.replyStatus" placeholder="回复状态" size="small" clearable style="width:140px" @change="search">
					<el-option label="全部" value=""></el-option>
					<el-option label="已回复" value="replied"></el-option>
					<el-option label="待回复" value="unreplied"></el-option>
				</el-select>
				<el-button size="small" type="primary" @click="search"><template #icon><Search /></template>查询</el-button>
				<el-button size="small" @click="refreshClick"><template #icon><RefreshRight /></template>重置</el-button>
			</div>

			<el-table
				:data="dataList"
				v-loading="dataListLoading"
				border
				stripe
				style="width:100%"
				:header-cell-style="{background:'#1a2744',color:'#e2e8f0',borderColor:'rgba(255,255,255,0.06)',fontWeight:600,fontSize:'13px'}"
				:cell-style="{borderColor:'rgba(255,255,255,0.06)',color:'#cbd5e1',fontSize:'13px'}"
				row-class-name="msg-table-row"
			>
			<el-table-column type="index" :index="indexMethod" label="序号" width="55" align="center"></el-table-column>
			<el-table-column prop="username" label="用户" width="80" align="center">
				<template #default="scope">
					<span class="msg-user-name">{{scope.row.username}}</span>
				</template>
			</el-table-column>
			<el-table-column label="留言内容" min-width="180" show-overflow-tooltip>
				<template #default="scope">
					<span class="msg-content-text">{{ stripHtml(scope.row.content) }}</span>
				</template>
			</el-table-column>
			<el-table-column label="回复内容" min-width="180" show-overflow-tooltip>
				<template #default="scope">
					<span v-if="scope.row.reply" class="msg-reply-text">{{ stripHtml(scope.row.reply) }}</span>
					<span v-else class="msg-unreplied"><i class="el-icon-time"></i> 待回复</span>
				</template>
			</el-table-column>
			<el-table-column label="图片" width="60" align="center">
				<template #default="scope">
					<template v-if="scope.row.cpicture">
						<img v-if="scope.row.cpicture.substring(0,4)=='http'" class="msg-thumb" :src="scope.row.cpicture.split(',')[0]" @click="imgPreView(scope.row.cpicture.split(',')[0])">
						<img v-else class="msg-thumb" :src="$base.url+scope.row.cpicture.split(',')[0]" @click="imgPreView($base.url+scope.row.cpicture.split(',')[0])">
					</template>
					<span v-else class="msg-no-img">—</span>
				</template>
			</el-table-column>
			<el-table-column label="状态" width="72" align="center">
				<template #default="scope">
					<el-tag v-if="scope.row.reply" size="small" type="success" effect="dark">已回复</el-tag>
					<el-tag v-else size="small" type="warning" effect="dark">待回复</el-tag>
				</template>
			</el-table-column>
			<el-table-column prop="addtime" label="留言时间" width="150" align="center" sortable>
				<template #default="scope">
					<span class="msg-time-text">{{scope.row.addtime}}</span>
				</template>
			</el-table-column>
			<el-table-column label="操作" width="100" align="center">
				<template #default="scope">
					<div class="msg-actions">
						<span v-if="isAuth('messages','回复')" class="act-link act-reply" @click="addOrUpdateHandler(scope.row.id,'msg')">
							<i class="el-icon-chat-dot-round"></i> 回复
						</span>
						<span v-if="isAuth('messages','删除')" class="act-link act-del" @click="deleteHandler(scope.row.id)">
							<i class="el-icon-delete"></i> 删除
						</span>
					</div>
				</template>
			</el-table-column>
			</el-table>

			<div class="msg-pagination">
				<el-pagination
					@size-change="sizeChangeHandle"
					@current-change="currentChangeHandle"
					:current-page="pageIndex"
					background
					:page-sizes="[10, 15, 20, 50]"
					:page-size="pageSize"
					layout="total, sizes, prev, pager, next, jumper"
					:total="totalPage"
				></el-pagination>
			</div>
		</template>

		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>

		<el-dialog title="预览图" :visible.sync="previewVisible" width="480px" :close-on-click-modal="false">
			<img :src="previewImg" alt="" style="width: 100%;">
		</el-dialog>
	</div>
</template>

<script>
	import AddOrUpdate from "./add-or-update.vue";
	export default {
		data() {
			return {
				searchForm: {
					username: "",
					replyStatus: "",
				},
				dataList: [],
				pageIndex: 1,
				pageSize: 7,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				showFlag: true,
				addOrUpdateFlag: false,
				previewImg: '',
				previewVisible: false,
			};
		},
		created() {
			if (this.statType) return false
			this.getDataList();
		},
		computed: {
			tablename() { return this.$storage.get('sessionTable') },
			role() { return this.$storage.get('role') },
		},
		components: { AddOrUpdate },
		methods: {
			stripHtml(val) {
				if (!val) return '';
				return val.replace(/<[^>]*>/g, '').replace(/&nbsp;/g, ' ').replace(/undefined/g, '').trim();
			},
			refreshClick() {
				this.searchForm = { username: '', replyStatus: '' }
				this.search()
			},
			imgPreView(url) {
				this.previewImg = url
				this.previewVisible = true
			},
			indexMethod(index) {
				return (this.pageIndex - 1) * this.pageSize + index + 1;
			},
			search() {
				this.pageIndex = 1;
				this.getDataList();
			},
			getDataList() {
				this.dataListLoading = true;
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					sort: 'id',
					order: 'desc',
				}
				if (this.searchForm.username) {
					params.username = this.searchForm.username
				}
				this.$http({
					url: "messages/page",
					method: "get",
					params: params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						let list = data.data.list;
						if (this.searchForm.replyStatus === 'replied') {
							list = list.filter(item => item.reply)
						} else if (this.searchForm.replyStatus === 'unreplied') {
							list = list.filter(item => !item.reply)
						}
						this.dataList = list;
						this.totalPage = data.data.total;
					} else {
						this.dataList = [];
						this.totalPage = 0;
					}
					this.dataListLoading = false;
				});
			},
			sizeChangeHandle(val) {
				this.pageSize = val;
				this.pageIndex = 1;
				this.getDataList();
			},
			currentChangeHandle(val) {
				this.pageIndex = val;
				this.getDataList();
			},
			selectionChangeHandler(val) {
				this.dataListSelections = val;
			},
			addOrUpdateHandler(id, type) {
				this.showFlag = false;
				this.addOrUpdateFlag = true;
				if (type !== 'info' && type !== 'msg') {
					type = 'else';
				}
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(id, type);
				});
			},
			deleteHandler(id) {
				var ids = id ? [Number(id)] : this.dataListSelections.map(item => Number(item.id));
				this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(() => {
					this.$http({
						url: "messages/delete",
						method: "post",
						data: ids
					}).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message({ message: "操作成功", type: "success", duration: 1500, onClose: () => { this.search(); } });
						} else {
							this.$message.error(data.msg);
						}
					});
				});
			},
		}
	};
</script>

<style lang="scss" scoped>
.msg-toolbar {
	display: flex;
	align-items: center;
	gap: 10px;
	margin-bottom: 16px;
	flex-wrap: wrap;
}

.msg-user-name {
	font-weight: 600;
	color: #e2e8f0;
	font-size: 13px;
}

.msg-content-text {
	color: #cbd5e1;
	line-height: 1.5;
}

.msg-reply-text {
	color: #94a3b8;
	line-height: 1.5;
}

.msg-unreplied {
	color: #f59e0b;
	font-size: 12px;
	i { margin-right: 3px; }
}

.msg-thumb {
	width: 36px;
	height: 36px;
	object-fit: cover;
	border-radius: 4px;
	cursor: pointer;
	border: 1px solid rgba(255,255,255,0.08);
	transition: transform 0.2s;
	&:hover { transform: scale(1.15); }
}

.msg-no-img {
	color: #475569;
}

.msg-time-text {
	font-size: 12px;
	color: #64748b;
	font-variant-numeric: tabular-nums;
}

.msg-actions {
	display: flex;
	align-items: center;
	justify-content: center;
	gap: 12px;
}

.act-link {
	cursor: pointer;
	font-size: 13px;
	transition: opacity 0.2s;
	white-space: nowrap;
	&:hover { opacity: 0.75; }
	i { margin-right: 2px; }
}

.act-reply { color: #06b6d4; }
.act-del { color: #ef4444; }

.msg-pagination {
	display: flex;
	justify-content: center;
	padding: 20px 0 10px;
}
</style>
