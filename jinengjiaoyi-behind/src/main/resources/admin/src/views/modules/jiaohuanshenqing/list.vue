  
<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<div class="admin-search">
			<div class="search-field">
				<span class="field-label">技能标题</span>
				<el-input v-model="searchForm.xuqiubiaoti" placeholder="技能标题" @keydown.enter="search()" clearable size="small"></el-input>
			</div>
				<div class="search-actions">
					<el-button class="btn-primary" size="small" @click="search()">
						<template #icon><Search /></template>
						查询
					</el-button>
					<el-button class="btn-plain" size="small" @click="searchForm={};search()">
						<template #icon><Refresh /></template>
						重置
					</el-button>
				</div>
			</div>

			<div class="admin-actions">
				<div class="action-left">
					<el-button class="btn-warning" size="small" v-if="isAuth('jiaohuanshenqing','审核')" :disabled="!dataListSelections.length" @click="shBatchDialog()">
						<template #icon><CircleCheck /></template>
						审核
					</el-button>
				</div>
			</div>

			<div class="admin-table-wrap">
				<el-table
					v-if="isAuth('jiaohuanshenqing','查看')"
					:data="dataList"
					v-loading="dataListLoading"
					style="width:100%"
					@selection-change="selectionChangeHandler">
	<el-table-column :resizable='true' type="selection" align="center" width="40"></el-table-column>
	<el-table-column type="index" :index="indexMethod" label="序号" width="50" align="center"></el-table-column>
	<el-table-column :resizable='true' prop="fengmian" width="68" label="封面" align="center">
			<template #default="scope">
				<div v-if="scope.row.fengmian">
					<img v-if="scope.row.fengmian.substring(0,4)=='http'" :src="scope.row.fengmian.split(',')[0]" width="44" height="44" style="object-fit: cover;border-radius:6px;cursor:pointer" @click="imgPreView(scope.row.fengmian.split(',')[0])" @error="(e)=>{e.target.style.display='none'}">
					<img v-else :src="$base.url+scope.row.fengmian.split(',')[0]" width="44" height="44" style="object-fit: cover;border-radius:6px;cursor:pointer" @click="imgPreView($base.url+scope.row.fengmian.split(',')[0])" @error="(e)=>{e.target.style.display='none'}">
				</div>
				<div v-else style="color:#64748b;font-size:12px;">无图片</div>
			</template>
		</el-table-column>
		<el-table-column :resizable='true' prop="xuqiubiaoti"
			label="技能标题" min-width="160" show-overflow-tooltip>
			<template #default="scope">
				{{scope.row.xuqiubiaoti}}
			</template>
		</el-table-column>
		<el-table-column :resizable='true' prop="jinengfenlei"
			label="技能分类" width="75" align="center">
			<template #default="scope">
				{{scope.row.jinengfenlei}}
			</template>
		</el-table-column>
		<el-table-column :resizable='true' prop="gongjirenxingming"
			label="申请人" width="72" align="center">
			<template #default="scope">
				{{scope.row.gongjirenxingming}}
			</template>
		</el-table-column>
		<el-table-column :resizable='true' prop="yonghuxingming"
			label="发布者" width="72" align="center">
			<template #default="scope">
				{{scope.row.yonghuxingming}}
			</template>
		</el-table-column>
		<el-table-column :resizable='true' prop="shenqingshijian"
			label="申请时间" width="135" align="center">
			<template #default="scope">
				{{scope.row.shenqingshijian}}
			</template>
		</el-table-column>
		<el-table-column :resizable='true' prop="sfsh" label="审核" width="85" align="center">
			<template #default="scope">
			<el-tooltip v-if="scope.row.shhf" :content="scope.row.shhf" placement="top" effect="dark" :open-delay="300">
				<span class="audit-badge audit-fail" v-if="scope.row.sfsh=='否'">未通过</span>
				<span class="audit-badge audit-pending" v-if="scope.row.sfsh=='待审核'">待审核</span>
				<span class="audit-badge audit-confirming" v-if="scope.row.sfsh=='待确认'">待确认</span>
				<span class="audit-badge audit-pass" v-if="scope.row.sfsh=='已完成'">已完成</span>
				<span class="audit-badge audit-pass" v-if="scope.row.sfsh=='是'">通过</span>
			</el-tooltip>
			<template v-else>
				<span class="audit-badge audit-fail" v-if="scope.row.sfsh=='否'">未通过</span>
				<span class="audit-badge audit-pending" v-if="scope.row.sfsh=='待审核'">待审核</span>
				<span class="audit-badge audit-confirming" v-if="scope.row.sfsh=='待确认'">待确认</span>
				<span class="audit-badge audit-pass" v-if="scope.row.sfsh=='已完成'">已完成</span>
				<span class="audit-badge audit-pass" v-if="scope.row.sfsh=='是'">通过</span>
			</template>
			</template>
		</el-table-column>
			<el-table-column width="100" label="操作" fixed="right" align="center">
				<template #default="scope">
					<el-button size="mini" v-if="isAuth('jiaohuanshenqing','查看')" type="success" plain @click="addOrUpdateHandler(scope.row.id,'info')">查看</el-button>
				</template>
			</el-table-column>
				</el-table>
			</div>
			<div class="admin-pagination">
				<el-pagination
					@size-change="sizeChangeHandle"
					@current-change="currentChangeHandle"
					:current-page="pageIndex"
					background
					:page-size="pageSize"
					:layout="layouts.join()"
					:total="totalPage"
					prev-text="< "
					next-text="> "
					:hide-on-single-page="false"
				></el-pagination>
			</div>
		</template>
		<!-- 添加/修改页面 -->
		<add-or-update v-if="addOrUpdateFlag && detailType!='info'" :parent="this" ref="addOrUpdate"></add-or-update>

		<!-- 详情卡片页面 -->
		<template v-if="detailFlag">
			<div class="apply-detail-page">
				<div class="page-header">
					<div class="header-right">
						<span class="status-badge done" v-if="detailData.sfsh=='已完成'"><i class="el-icon-circle-check"></i> 已完成</span>
						<span class="status-badge confirming" v-else-if="detailData.sfsh=='待确认'"><i class="el-icon-select"></i> 待确认</span>
						<span class="status-badge fail" v-else-if="detailData.sfsh=='否'"><i class="el-icon-circle-close"></i> 已拒绝</span>
						<span class="status-badge pass" v-else-if="detailData.sfsh=='是'"><i class="el-icon-check"></i> 已通过</span>
						<span class="status-badge pending" v-else><i class="el-icon-time"></i> 待处理</span>
						<el-button class="back-btn" size="small" @click="closeDetail">
							<i class="el-icon-arrow-left"></i> 返回
						</el-button>
					</div>
				</div>

				<div class="exchange-flow">
					<div class="flow-card applicant-card">
						<div class="card-label"><i class="el-icon-promotion"></i> 申请人</div>
						<div class="card-avatar">
							<span class="avatar-text">{{(detailData.gongjirenxingming || '?').substring(0,1)}}</span>
						</div>
						<div class="card-name">{{detailData.gongjirenxingming || '--'}}</div>
						<div class="card-account">{{detailData.gongjirenzhanghao || ''}}</div>
						<div class="card-tags">
							<span class="mini-tag" v-if="detailData.lianxifangshi"><i class="el-icon-phone"></i> {{detailData.lianxifangshi}}</span>
						</div>
					</div>

					<div class="flow-center">
						<div class="skill-item left-skill">
							<div class="skill-item-label">申请人提供</div>
							<div class="skill-item-name">{{detailData.xuanzejinengbiaoti || '待选择'}}</div>
						</div>
						<div class="exchange-badge">
							<i class="el-icon-sort"></i>
							<span>技能交换</span>
						</div>
						<div class="skill-item right-skill">
							<div class="skill-item-label">发布者提供</div>
							<div class="skill-item-name">{{detailData.xuqiubiaoti || '--'}}</div>
						</div>
					</div>

					<div class="flow-card owner-card">
						<div class="card-label"><i class="el-icon-goods"></i> 发布者</div>
						<div class="card-avatar owner">
							<span class="avatar-text">{{(detailData.yonghuxingming || '?').substring(0,1)}}</span>
						</div>
						<div class="card-name">{{detailData.yonghuxingming || '--'}}</div>
						<div class="card-account">{{detailData.yonghuzhanghao || ''}}</div>
						<div class="card-tags">
							<span class="mini-tag" v-if="detailData.shoujihao"><i class="el-icon-phone"></i> {{detailData.shoujihao}}</span>
						</div>
					</div>
				</div>

				<div class="detail-bottom">
					<div class="info-grid">
						<div class="info-item">
							<span class="info-label">需求编号</span>
							<span class="info-value">{{detailData.xuqiubianhao || '--'}}</span>
						</div>
						<div class="info-item">
							<span class="info-label">技能分类</span>
							<span class="info-value">{{detailData.jinengfenlei || '--'}}</span>
						</div>
						<div class="info-item">
							<span class="info-label">需求费用</span>
							<span class="info-value" v-if="detailData.xuqiufeiyong">¥{{detailData.xuqiufeiyong}}</span>
							<span class="info-value" v-else>无</span>
						</div>
						<div class="info-item">
							<span class="info-label">申请时间</span>
							<span class="info-value">{{detailData.shenqingshijian || '--'}}</span>
						</div>
					</div>
					<div class="cover-section" v-if="detailData.fengmian">
						<span class="info-label">封面</span>
						<div class="cover-img">
							<img v-if="detailData.fengmian.substring(0,4)=='http'" :src="detailData.fengmian.split(',')[0]" />
							<img v-else :src="$base.url + detailData.fengmian.split(',')[0]" />
						</div>
					</div>
					<div class="remark-card" v-if="detailData.shenqingbeizhu">
						<div class="remark-icon"><i class="el-icon-chat-line-square"></i></div>
						<div class="remark-body">
							<div class="remark-title">{{detailData.gongjirenxingming}} 的申请备注：</div>
							<div class="remark-text">{{detailData.shenqingbeizhu}}</div>
						</div>
					</div>
					<div class="remark-card reply-card" v-if="detailData.shhf">
						<div class="remark-icon reply"><i class="el-icon-chat-dot-round"></i></div>
						<div class="remark-body">
							<div class="remark-title">审核回复：</div>
							<div class="remark-text">{{detailData.shhf}}</div>
						</div>
					</div>
				</div>
			</div>
		</template>

		<jiaohuanjilu-cross-add-or-update v-if="jiaohuanjiluCrossAddOrUpdateFlag" :parent="this" ref="jiaohuanjiluCrossaddOrUpdate"></jiaohuanjilu-cross-add-or-update>

		
		<el-dialog :title="this.batchIds.length>1?'批量审核':'审核'" :visible.sync="sfshBatchVisiable" width="480px" :close-on-click-modal="false">
			<el-form ref="shBatchForm" :model="shBatchForm" :rules="shRules" label-width="80px">
				<el-form-item label="审核状态" prop="sfsh">
					<el-select v-model="shBatchForm.sfsh" placeholder="审核状态" style="width:100%">
						<el-option label="通过" value="是"></el-option>
						<el-option label="不通过" value="否"></el-option>
						<el-option label="待审核" value="待审核"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="内容" prop="shhf">
					<el-input type="textarea" :rows="8" v-model="shBatchForm.shhf"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<div class="dialog-footer">
				<el-button class="btn-plain" size="small" @click="sfshBatchVisiable=false">取消</el-button>
				<el-button class="btn-primary" size="small" @click="shBatchHandler">确定</el-button>
				</div>
			</template>
		</el-dialog>

		<el-dialog title="预览图" :visible.sync="previewVisible" width="480px" :close-on-click-modal="false">
			<img :src="previewImg" alt="" style="width: 100%;">
		</el-dialog>
	</div>
</template>

<script>
	import * as echarts from 'echarts'
	import chinaJson from "@/components/echarts/china.json";
	import axios from 'axios';
	import AddOrUpdate from "./add-or-update.vue";
	import jiaohuanjiluCrossAddOrUpdate from "../jiaohuanjilu/add-or-update.vue";
	export default {
		data() {
			return {
				indexQueryCondition: '',
				searchForm: {
					key: ""
				},
				form:{},
				sfshOptions: [],
				dataList: [],
				pageIndex: 1,
				pageSize: 7,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				showFlag: true,
				detailFlag: false,
				detailData: {},
				detailType: '',
				sfshVisiable: false,
				shForm: {},
				sfshBatchVisiable: false,
				shBatchForm: {
					sfsh:'',
					shhf:''
				},
				shRules: {
					sfsh:[{ required: true, message: '审核状态不能为空', trigger: 'blur' },],
				},
				batchIds:[],
				shList: [],
				addOrUpdateFlag:false,
				jiaohuanjiluCrossAddOrUpdateFlag: false,
				layouts: ["total","prev","pager","next","jumper"],
				previewImg: '',
				previewVisible: false,
			};
		},
		created() {
			if(this.statType) {
				return false
			}
			this.init();
			this.getDataList();
		},
		mounted() {
		},
		watch: {
		},
		computed: {
			tablename(){
				return this.$storage.get('sessionTable')
			},
			role(){
				return this.$storage.get('role')
			},
		},
		components: {
			AddOrUpdate,
			jiaohuanjiluCrossAddOrUpdate},
		methods: {
			queryChange(arr){
				for(let x in arr) {
					if(arr[x] == this.role) {
						return true
					}
				}
				return false
			},
			imgPreView(url){
				this.previewImg = url
				this.previewVisible = true
				
			},
			jiaohuanjiluCrossAddOrUpdateHandler(row,type,crossOptAudit,crossOptPay,statusColumnName,tips,statusColumnValue){
				if(crossOptAudit=='是'&&row.sfsh!='是') {
					this.$message({
						message: "请审核通过后再操作",
						type: "warning",
						duration: 1500,
						onClose: () => {
						}
					});
					return
				}
				this.showFlag = false;
				this.addOrUpdateFlag = false;
				this.jiaohuanjiluCrossAddOrUpdateFlag = true;
				this.$storage.set('crossObj',row);
				this.$storage.set('crossTable','jiaohuanshenqing');
				this.$storage.set('statusColumnName',statusColumnName);
				this.$storage.set('statusColumnValue',statusColumnValue);
				this.$storage.set('tips',tips);
				if(statusColumnName!=''&&!statusColumnName.startsWith("[")) {
					var obj = this.$storage.getObj('crossObj');
					for (var o in obj){
						if(o==statusColumnName && obj[o]==statusColumnValue){
							this.$message({
								message: tips,
								type: "warning",
								duration: 1500,
								onClose: () => {
									this.getDataList();
								}
							});
							this.showFlag = true;
							this.jiaohuanjiluCrossAddOrUpdateFlag = false;
							return;
						}
					}
				}
				this.$nextTick(() => {
					this.$refs.jiaohuanjiluCrossaddOrUpdate.init(row.id,type);
				});
			},
			init () {
				this.sfshOptions = "待审核,待确认,已完成,否".split(',');
			},
			indexMethod(index) {
				return (this.pageIndex - 1) * this.pageSize + index + 1;
			},
			search() {
				this.pageIndex = 1;
				this.getDataList();
			},

			// 获取数据列表
			getDataList() {
				this.dataListLoading = true;
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					sort: 'id',
					order: 'desc',
				}
				if(this.searchForm.yonghuxingming!='' && this.searchForm.yonghuxingming!=undefined){
					params['yonghuxingming'] = '%' + this.searchForm.yonghuxingming + '%'
				}
				if(this.searchForm.xuqiubiaoti!='' && this.searchForm.xuqiubiaoti!=undefined){
					params['xuqiubiaoti'] = '%' + this.searchForm.xuqiubiaoti + '%'
				}
				if(this.searchForm.jinengfenlei!='' && this.searchForm.jinengfenlei!=undefined){
					params['jinengfenlei'] = '%' + this.searchForm.jinengfenlei + '%'
				}
				if(this.searchForm.gongjirenxingming!='' && this.searchForm.gongjirenxingming!=undefined){
					params['gongjirenxingming'] = '%' + this.searchForm.gongjirenxingming + '%'
				}
				if(this.searchForm.sfsh!='' && this.searchForm.sfsh!=undefined){
					params['sfsh'] = this.searchForm.sfsh
				}
				let user = JSON.parse(this.$storage.getObj('userForm'))
				this.$http({
					url: "jiaohuanshenqing/page",
					method: "get",
					params: params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.dataList = data.data.list;
						this.totalPage = data.data.total;
					} else {
						this.dataList = [];
						this.totalPage = 0;
					}
					this.dataListLoading = false;
				});
			},
			// 每页数
			sizeChangeHandle(val) {
				this.pageSize = val;
				this.pageIndex = 1;
				this.getDataList();
			},
			// 当前页
			currentChangeHandle(val) {
				this.pageIndex = val;
				this.getDataList();
			},
			// 多选
			selectionChangeHandler(val) {
				this.dataListSelections = val;
			},
			// 添加/修改
			addOrUpdateHandler(id,type) {
				this.detailType = type;
				if(type === 'info') {
					this.showFlag = false;
					this.detailFlag = true;
					this.$http({ url: `jiaohuanshenqing/info/${id}`, method: 'get' }).then(({ data }) => {
						if (data && data.code === 0) {
							this.detailData = data.data;
						}
					});
					return;
				}
				this.showFlag = false;
				this.addOrUpdateFlag = true;
				this.crossAddOrUpdateFlag = false;
				if(type!='info'&&type!='msg'){
					type = 'else';
				}
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(id,type );
				});
			},
			closeDetail() {
				this.detailFlag = false;
				this.showFlag = true;
			},
			//批量审核窗口
			shBatchDialog(){
				for(let x in this.dataListSelections){
					if(this.tablename!='users') {
						let username = this.$storage.get('adminName')
						if(this.dataListSelections[x].yonghuzhanghao == username) {
							this.$message.error('无权操作');
							return false
						}
					}
					if(this.dataListSelections[x].sfsh&&this.dataListSelections[x].sfsh!='待审核'){
						this.$message.error('存在已审核数据，不能继续操作');
						this.batchIds = []
						return false
					}
					this.batchIds.push(this.dataListSelections[x].id)
				}
				this.shList = this.dataListSelections
				this.sfshBatchVisiable = true
			},
			//批量审核
			shBatchHandler(){
				this.$refs["shBatchForm"].validate(valid => {
					if(valid){
						this.$confirm(`是否${this.batchIds.length>1?'一键审核':'审核'}选中数据?`, "提示", {
							confirmButtonText: "确定",
							cancelButtonText: "取消",
							type: "warning"
						}).then(async() => {
							this.$http({
								url: "jiaohuanshenqing/shBatch?sfsh="+this.shBatchForm.sfsh+"&shhf="+this.shBatchForm.shhf,
								method: "post",
								data: this.batchIds
							}).then(async ({ data }) => {
								if (data && data.code === 0) {
									this.$message({
										message: "操作成功",
										type: "success",
										duration: 1500,
										onClose: () => {
											this.getDataList();
											this.sfshBatchVisiable = false
											this.batchIds = []
										}
									});
								} else {
									this.$message.error(data.msg);
								}
							});
						});
					}
				})
			},
			// 删除
			async deleteHandler(id ) {
				var ids = id? [Number(id)]: this.dataListSelections.map(item => {
					return Number(item.id);
				});
				await this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(async () => {
					await this.$http({
						url: "jiaohuanshenqing/delete",
						method: "post",
						data: ids
					}).then(async ({ data }) => {
						if (data && data.code === 0) {
							this.$message({
								message: "操作成功",
								type: "success",
								duration: 1500,
								onClose: () => {
									this.search();
								}
							});
			
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
.audit-badge {
	display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 12px; font-weight: 600;
}
.audit-pass { background: rgba(34,197,94,0.15); color: #4ade80; border: 1px solid rgba(34,197,94,0.3); }
.audit-confirming { background: rgba(0,212,255,0.15); color: #00d4ff; border: 1px solid rgba(0,212,255,0.3); }
.audit-pending { background: rgba(245,158,11,0.15); color: #fbbf24; border: 1px solid rgba(245,158,11,0.3); }
.audit-fail { background: rgba(239,68,68,0.15); color: #f87171; border: 1px solid rgba(239,68,68,0.3); }

$primary: #38bdf8;
$purple: #7dd3fc;
$green: #34d399;
$border: rgba(14,165,233,0.15);
$bg: #0f172a;
$card-bg: #1e293b;

.apply-detail-page {
	padding: 0;
	display: flex;
	flex-direction: column;
	gap: 14px;
}

.page-header {
	display: flex;
	justify-content: flex-end;
	.header-right { display: flex; align-items: center; gap: 10px; }
	.status-badge {
		display: inline-flex; align-items: center; gap: 4px;
		padding: 5px 14px; border-radius: 20px; font-size: 13px; font-weight: 600;
		&.done { background: rgba(52,211,153,0.12); color: #34d399; border: 1px solid rgba(52,211,153,0.25); }
		&.confirming { background: rgba(14,165,233,0.12); color: #0ea5e9; border: 1px solid rgba(14,165,233,0.25); }
		&.fail { background: rgba(251,113,133,0.12); color: #fb7185; border: 1px solid rgba(251,113,133,0.25); }
		&.pass { background: rgba(34,197,94,0.12); color: #4ade80; border: 1px solid rgba(34,197,94,0.25); }
		&.pending { background: rgba(245,158,11,0.12); color: #fbbf24; border: 1px solid rgba(245,158,11,0.25); }
	}
	.back-btn {
		border-radius: 20px; border: 1px solid $border; color: #cbd5e1; font-size: 12px; padding: 5px 14px; background: transparent;
		&:hover { color: $primary; border-color: $primary; }
	}
}

.exchange-flow {
	display: flex;
	align-items: stretch;
	gap: 0;
}

.flow-card {
	flex: 1;
	min-width: 0;
	background: $card-bg;
	border-radius: 14px;
	border: 1px solid $border;
	padding: 20px 16px;
	display: flex;
	flex-direction: column;
	align-items: center;
	text-align: center;
	position: relative;
}

.card-label {
	position: absolute;
	top: 0; left: 50%; transform: translateX(-50%);
	padding: 3px 14px;
	border-radius: 0 0 8px 8px;
	font-size: 11px; font-weight: 700; letter-spacing: 0.5px; white-space: nowrap;
}
.applicant-card .card-label { background: linear-gradient(135deg, $primary, #6d5cfc); color: #fff; }
.owner-card .card-label { background: linear-gradient(135deg, $purple, #8b6ffa); color: #fff; }

.card-avatar {
	width: 56px; height: 56px; border-radius: 50%;
	display: flex; align-items: center; justify-content: center;
	margin: 16px 0 8px;
	background: linear-gradient(135deg, #0ea5e9, #6d5cfc);
	.avatar-text { font-size: 24px; font-weight: 700; color: #fff; }
	&.owner { background: linear-gradient(135deg, #7c3aed, #38bdf8); }
}

.card-name { font-size: 16px; font-weight: 700; color: #fff; margin-bottom: 2px; }
.card-account { font-size: 12px; color: #94a3b8; margin-bottom: 8px; }

.card-tags {
	display: flex; flex-wrap: wrap; gap: 6px; justify-content: center;
	.mini-tag {
		font-size: 12px; color: #e2e8f0;
		background: $bg; padding: 4px 10px; border-radius: 4px;
		border: 1px solid rgba(14,165,233,0.12);
		display: inline-flex; align-items: center; gap: 3px;
	}
}

.flow-center {
	display: flex; flex-direction: column; align-items: center; justify-content: center;
	padding: 0 10px; flex-shrink: 0; width: 200px; align-self: center; gap: 10px;
}

.skill-item {
	width: 100%; padding: 10px 14px; border-radius: 10px; text-align: center;
	.skill-item-label { font-size: 11px; color: #cbd5e1; font-weight: 600; margin-bottom: 4px; }
	.skill-item-name { font-size: 13px; font-weight: 700; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
	&.left-skill { background: rgba(14,165,233,0.08); border: 1px solid rgba(14,165,233,0.2); .skill-item-name { color: #7dd3fc; } }
	&.right-skill { background: rgba(167,139,250,0.08); border: 1px solid rgba(167,139,250,0.2); .skill-item-name { color: #c4b5fd; } }
}

.exchange-badge {
	display: flex; align-items: center; gap: 6px; padding: 5px 14px;
	border-radius: 20px;
	background: linear-gradient(135deg, rgba(52,211,153,0.1), rgba(14,165,233,0.08));
	border: 1px solid rgba(52,211,153,0.2); white-space: nowrap;
	i { font-size: 14px; color: $green; }
	span { font-size: 11px; font-weight: 600; color: $green; }
}

.detail-bottom {
	display: flex; flex-direction: column; gap: 12px;
}

.info-grid {
	display: grid; grid-template-columns: 1fr 1fr; gap: 10px;
	background: $card-bg; border-radius: 12px; border: 1px solid $border; padding: 16px 20px;
}
.info-item {
	display: flex; flex-direction: column; gap: 4px;
	.info-label { font-size: 12px; color: #64748b; }
	.info-value { font-size: 14px; color: #f1f5f9; font-weight: 500; }
}

.cover-section {
	background: $card-bg; border-radius: 12px; border: 1px solid $border; padding: 16px 20px;
	.info-label { font-size: 12px; color: #64748b; display: block; margin-bottom: 8px; }
	.cover-img {
		width: 120px; height: 90px; border-radius: 8px; overflow: hidden;
		img { width: 100%; height: 100%; object-fit: cover; }
	}
}

.remark-card {
	display: flex; gap: 12px; align-items: flex-start;
	background: $card-bg; border-radius: 10px; border: 1px solid $border; padding: 12px 16px;
	.remark-icon {
		width: 36px; height: 36px; border-radius: 8px;
		background: linear-gradient(135deg, rgba(14,165,233,0.08), rgba(14,165,233,0.15));
		display: flex; align-items: center; justify-content: center; flex-shrink: 0;
		i { font-size: 16px; color: $primary; }
		&.reply { background: linear-gradient(135deg, rgba(167,139,250,0.08), rgba(167,139,250,0.15)); i { color: $purple; } }
	}
	.remark-body { flex: 1; min-width: 0; }
	.remark-title { font-size: 12px; color: #cbd5e1; margin-bottom: 2px; font-weight: 600; }
	.remark-text { font-size: 14px; color: #f1f5f9; line-height: 1.6; }
}
</style>
