<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- Search & Filter -->
		<div class="admin-search">
			<div class="search-field">
				<span class="field-label">需求标题</span>
				<el-input v-model="searchForm.xuqiubiaoti" placeholder="请输入需求标题" @keydown.enter="search()" clearable size="small"></el-input>
			</div>
			<div class="search-field">
				<span class="field-label">审核状态</span>
				<el-select v-model="searchForm.sfsh" placeholder="全部" clearable size="small" @change="search()">
					<el-option label="全部" value=""></el-option>
					<el-option label="待审核" value="待审核"></el-option>
					<el-option label="已通过" value="是"></el-option>
					<el-option label="已拒绝" value="否"></el-option>
				</el-select>
			</div>
			<div class="search-actions">
				<el-button class="btn-primary" size="small" @click="search()">
					<template #icon><Search /></template>
					查询
				</el-button>
				<el-button class="btn-plain" size="small" @click="searchForm.xuqiubiaoti='';searchForm.sfsh='';search()">
					<template #icon><Refresh /></template>
					重置
				</el-button>
			</div>
		</div>

		<!-- Action Bar -->
		<div class="admin-actions">
			<div class="action-left">
				<el-button class="btn-success" size="small" :disabled="!pendingSelections.length" @click="batchAudit('是')">
					<template #icon><CircleCheck /></template>
					批量通过
				</el-button>
				<el-button class="btn-danger" size="small" :disabled="!pendingSelections.length" @click="batchAudit('否')">
					<template #icon><Close /></template>
					批量拒绝
				</el-button>
			</div>
			<div class="action-right">
				<span style="font-size:12px;color:#64748b;">待审核选中 {{pendingSelections.length}} 项</span>
			</div>
		</div>

		<!-- Table -->
		<div class="admin-table-wrap">
			<el-table
				ref="auditTable"
				:data="dataList"
				v-loading="loading"
				style="width:100%"
				@selection-change="selectionChangeHandler">
			<el-table-column type="selection" align="center" width="42"></el-table-column>
			<el-table-column type="index" :index="indexMethod" label="序号" width="55" align="center"></el-table-column>
			<el-table-column prop="xuqiubiaoti" label="需求标题" min-width="160" show-overflow-tooltip>
					<template #default="scope">
						<span style="cursor:pointer;color:#38bdf8;" @click="viewDetail(scope.row)">{{scope.row.xuqiubiaoti}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="jinengfenlei" label="技能分类" min-width="100">
					<template #default="scope">{{scope.row.jinengfenlei}}</template>
				</el-table-column>
				<el-table-column prop="xuqiumiaoshu" label="需求描述" min-width="180" show-overflow-tooltip>
					<template #default="scope">{{scope.row.xuqiumiaoshu || '-'}}</template>
				</el-table-column>
				<el-table-column prop="yonghuxingming" label="发布人" min-width="90">
					<template #default="scope">{{scope.row.yonghuxingming}}</template>
				</el-table-column>
			<el-table-column prop="fabushijian" label="发布时间" min-width="110">
					<template #default="scope">{{scope.row.fabushijian}}</template>
				</el-table-column>
				<el-table-column prop="sfsh" label="审核状态" min-width="85" align="center">
					<template #default="scope">
					<span class="audit-badge audit-pending" v-if="scope.row.sfsh=='待审核'">待审核</span>
					<span class="audit-badge audit-pass" v-else-if="scope.row.sfsh=='是'">已通过</span>
					<span class="audit-badge audit-fail" v-else-if="scope.row.sfsh=='否'">已拒绝</span>
					</template>
				</el-table-column>
				<el-table-column prop="shhf" label="审核回复" min-width="120" show-overflow-tooltip>
					<template #default="scope">{{scope.row.shhf || '-'}}</template>
				</el-table-column>
				<el-table-column label="操作" width="200" fixed="right" align="center">
					<template #default="scope">
						<el-button size="mini" type="info" plain @click="viewDetail(scope.row)">详情</el-button>
						<template v-if="scope.row.sfsh=='待审核'">
							<el-button size="mini" type="success" plain @click="quickAudit(scope.row, '是')">通过</el-button>
							<el-button size="mini" type="danger" plain @click="singleAudit(scope.row, '否')">拒绝</el-button>
						</template>
						<template v-else>
							<el-button size="mini" type="warning" plain @click="resetAudit(scope.row)">重置</el-button>
						</template>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- Pagination -->
		<div class="admin-pagination">
			<el-pagination
				@size-change="sizeChangeHandle"
				@current-change="currentChangeHandle"
				:current-page="pageIndex"
				:page-size="pageSize"
				background
				layout="total, prev, pager, next, jumper"
				:total="totalPage">
			</el-pagination>
		</div>

		<el-dialog :title="auditDialogTitle" :visible.sync="auditDialogVisible" width="480px" :close-on-click-modal="false" top="30vh">
			<el-form ref="auditForm" :model="auditForm" :rules="auditRules" label-width="80px">
				<el-form-item label="审核结果" prop="sfsh">
					<el-radio-group v-model="auditForm.sfsh" size="medium">
						<el-radio-button label="是">通过</el-radio-button>
						<el-radio-button label="否">拒绝</el-radio-button>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="拒绝原因" prop="shhf">
					<el-input type="textarea" :rows="3" v-model="auditForm.shhf" :placeholder="auditForm.sfsh === '否' ? '请说明拒绝原因' : '选填'"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button class="btn-plain" @click="auditDialogVisible = false" size="small">取消</el-button>
				<el-button :class="auditForm.sfsh === '是' ? 'btn-success' : 'btn-danger'" @click="submitAudit" :loading="auditLoading" size="small">
					{{auditForm.sfsh === '是' ? '确认通过' : '确认拒绝'}}
				</el-button>
			</template>
		</el-dialog>

		<el-dialog title="需求详情" :visible.sync="detailDialogVisible" width="680px" top="8vh">
			<div v-if="detailData">
				<el-form label-width="100px">
					<el-form-item label="需求标题">{{detailData.xuqiubiaoti}}</el-form-item>
					<el-form-item label="技能分类">{{detailData.jinengfenlei}}</el-form-item>
					<el-form-item label="发布人">{{detailData.yonghuxingming}}</el-form-item>
					<el-form-item label="手机号" v-if="detailData.shoujihao">{{detailData.shoujihao}}</el-form-item>
					<el-form-item label="发布时间">{{detailData.fabushijian}}</el-form-item>
					<el-form-item label="审核状态">
						<el-tag size="small" v-if="detailData.sfsh=='待审核'" type="warning" effect="dark">待审核</el-tag>
						<el-tag size="small" v-else-if="detailData.sfsh=='是'" type="success" effect="dark">已通过</el-tag>
						<el-tag size="small" v-else-if="detailData.sfsh=='否'" type="danger" effect="dark">已拒绝</el-tag>
					</el-form-item>
					<el-form-item label="审核回复" v-if="detailData.shhf">{{detailData.shhf}}</el-form-item>
					<el-form-item label="需求描述">{{detailData.xuqiumiaoshu || '暂无'}}</el-form-item>
					<el-form-item label="需求详情" v-if="detailData.xuqiuxiangqing">
						<div v-html="detailData.xuqiuxiangqing" class="ql-snow ql-editor" style="padding:0;max-height:300px;overflow-y:auto;"></div>
					</el-form-item>
				</el-form>
			</div>
			<template #footer>
				<template v-if="detailData && detailData.sfsh=='待审核'">
					<el-button type="success" size="small" @click="detailDialogVisible = false; quickAudit(detailData, '是')">通过</el-button>
					<el-button type="danger" size="small" @click="detailDialogVisible = false; singleAudit(detailData, '否')">拒绝</el-button>
				</template>
				<el-button @click="detailDialogVisible = false" size="small">关 闭</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script>
export default {
	data() {
		return {
			searchForm: {
				xuqiubiaoti: '',
				sfsh: '',
			},
			dataList: [],
			pageIndex: 1,
			pageSize: 7,
			totalPage: 0,
			loading: false,
			dataListSelections: [],
			auditDialogVisible: false,
			auditDialogTitle: '审核',
			auditForm: { sfsh: '', shhf: '' },
			auditRules: {
				sfsh: [{ required: true, message: '请选择审核结果', trigger: 'change' }],
			},
			auditIds: [],
			auditLoading: false,
			detailDialogVisible: false,
			detailData: null,
		};
	},
	computed: {
		pendingSelections() {
			return this.dataListSelections.filter(item => item.sfsh === '待审核');
		}
	},
	created() {
		this.getDataList();
	},
	methods: {
		indexMethod(index) {
			return (this.pageIndex - 1) * this.pageSize + index + 1;
		},
		search() {
			this.pageIndex = 1;
			this.getDataList();
		},
		getDataList() {
			this.loading = true;
			let params = {
				page: this.pageIndex,
				limit: this.pageSize,
				auditSort: 'true',
				leixing: '需求',
			};
			if (this.searchForm.xuqiubiaoti) params['xuqiubiaoti'] = '%' + this.searchForm.xuqiubiaoti + '%';
			if (this.searchForm.sfsh) params['sfsh'] = this.searchForm.sfsh;
			this.$http({ url: 'jinengxuqiu/page', method: 'get', params }).then(({ data }) => {
				if (data && data.code === 0) {
					this.dataList = data.data.list;
					this.totalPage = data.data.total;
				} else {
					this.dataList = [];
					this.totalPage = 0;
				}
				this.loading = false;
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
		quickAudit(row, sfsh) {
			const label = sfsh === '是' ? '通过' : '拒绝';
			this.$confirm(`确定${label}「${row.xuqiubiaoti}」？`, '审核确认', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: sfsh === '是' ? 'success' : 'warning',
			}).then(() => {
				this.doAudit([row.id], sfsh, '');
			}).catch(() => {});
		},
		singleAudit(row, sfsh) {
			this.auditIds = [row.id];
			this.auditForm.sfsh = sfsh;
			this.auditForm.shhf = '';
			this.auditDialogTitle = '审核 - ' + row.xuqiubiaoti;
			this.auditDialogVisible = true;
		},
		batchAudit(sfsh) {
			if (this.pendingSelections.length === 0) {
				this.$message.warning('请选择待审核的记录');
				return;
			}
			this.auditIds = this.pendingSelections.map(item => item.id);
			this.auditForm.sfsh = sfsh;
			this.auditForm.shhf = '';
			this.auditDialogTitle = '批量审核（' + this.auditIds.length + '条）';
			this.auditDialogVisible = true;
		},
		resetAudit(row) {
			this.$confirm(`确定将「${row.xuqiubiaoti}」重置为待审核？`, '重置确认', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning',
			}).then(() => {
				this.doAudit([row.id], '待审核', '');
			}).catch(() => {});
		},
		submitAudit() {
			this.$refs['auditForm'].validate(valid => {
				if (!valid) return;
				this.auditLoading = true;
				this.doAudit(this.auditIds, this.auditForm.sfsh, this.auditForm.shhf || '');
			});
		},
		doAudit(ids, sfsh, shhf) {
			this.$http({
				url: 'jinengxuqiu/shBatch?sfsh=' + encodeURIComponent(sfsh) + '&shhf=' + encodeURIComponent(shhf),
				method: 'post',
				data: ids,
			}).then(({ data }) => {
				this.auditLoading = false;
				if (data && data.code === 0) {
					this.auditDialogVisible = false;
					this.$message.success('操作成功');
					this.getDataList();
					this.$emit('audit-changed');
				} else {
					this.$message.error(data.msg);
				}
			}).catch(() => {
				this.auditLoading = false;
			});
		},
		viewDetail(row) {
			this.$http({ url: 'jinengxuqiu/info/' + row.id, method: 'get' }).then(({ data }) => {
				if (data && data.code === 0) {
					this.detailData = data.data;
					this.detailDialogVisible = true;
				} else {
					this.$message.error('获取详情失败');
				}
			});
		},
	},
};
</script>
<style lang="scss" scoped>
.audit-badge {
	display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 12px; font-weight: 600;
}
.audit-pass { background: rgba(34,197,94,0.15); color: #4ade80; border: 1px solid rgba(34,197,94,0.3); }
.audit-pending { background: rgba(245,158,11,0.15); color: #fbbf24; border: 1px solid rgba(245,158,11,0.3); }
.audit-fail { background: rgba(239,68,68,0.15); color: #f87171; border: 1px solid rgba(239,68,68,0.3); }
</style>
