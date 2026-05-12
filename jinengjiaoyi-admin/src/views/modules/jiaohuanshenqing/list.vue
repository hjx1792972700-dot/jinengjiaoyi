  
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
					<el-button class="btn-primary" size="small" v-if="isAuth('jiaohuanshenqing','新增')" @click="addOrUpdateHandler()">
						<template #icon><Plus /></template>
						新增
					</el-button>
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
		<el-table-column type="index" label="序号" width="50" align="center"></el-table-column>
		<el-table-column :resizable='true' prop="fengmian" width="72" label="封面" align="center">
				<template #default="scope">
					<div v-if="scope.row.fengmian">
						<img v-if="scope.row.fengmian.substring(0,4)=='http'" :src="scope.row.fengmian.split(',')[0]" width="48" height="48" style="object-fit: cover;border-radius:6px;cursor:pointer" @click="imgPreView(scope.row.fengmian.split(',')[0])" @error="(e)=>{e.target.style.display='none'}">
						<img v-else :src="$base.url+scope.row.fengmian.split(',')[0]" width="48" height="48" style="object-fit: cover;border-radius:6px;cursor:pointer" @click="imgPreView($base.url+scope.row.fengmian.split(',')[0])" @error="(e)=>{e.target.style.display='none'}">
					</div>
					<div v-else style="color:#64748b;font-size:12px;">无图片</div>
				</template>
			</el-table-column>
			<el-table-column :resizable='true' prop="xuqiubiaoti"
				label="技能标题" min-width="140" show-overflow-tooltip>
				<template #default="scope">
					{{scope.row.xuqiubiaoti}}
				</template>
			</el-table-column>
			<el-table-column :resizable='true' prop="jinengfenlei"
				label="技能分类" width="80" align="center">
				<template #default="scope">
					{{scope.row.jinengfenlei}}
				</template>
			</el-table-column>
			<el-table-column :resizable='true' prop="yonghuxingming"
				label="申请人" width="80" align="center">
				<template #default="scope">
					{{scope.row.yonghuxingming}}
				</template>
			</el-table-column>
			<el-table-column :resizable='true' prop="gongjirenxingming"
				label="技能发布者" width="90" align="center">
				<template #default="scope">
					{{scope.row.gongjirenxingming}}
				</template>
			</el-table-column>
			<el-table-column :resizable='true' prop="shenqingshijian"
				label="申请时间" width="160" align="center">
				<template #default="scope">
					{{scope.row.shenqingshijian}}
				</template>
			</el-table-column>
			<el-table-column :resizable='true' prop="sfsh" label="审核" width="90" align="center">
				<template #default="scope">
				<span class="audit-badge audit-fail" v-if="scope.row.sfsh=='否'">未通过</span>
				<span class="audit-badge audit-pending" v-if="scope.row.sfsh=='待审核'">待审核</span>
				<span class="audit-badge audit-confirming" v-if="scope.row.sfsh=='待确认'">待确认</span>
				<span class="audit-badge audit-pass" v-if="scope.row.sfsh=='已完成'">已完成</span>
				<span class="audit-badge audit-pass" v-if="scope.row.sfsh=='是'">通过</span>
					<div v-if="scope.row.shhf" style="font-size:11px;color:#94a3b8;margin-top:2px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;max-width:80px" :title="scope.row.shhf">{{scope.row.shhf}}</div>
				</template>
			</el-table-column>
				<el-table-column width="180" label="操作" align="center">
					<template #default="scope">
						<el-button size="mini" v-if="isAuth('jiaohuanshenqing','查看')" type="success" plain @click="addOrUpdateHandler(scope.row.id,'info')">查看</el-button>
						<el-button size="mini" v-if="isAuth('jiaohuanshenqing','交换')" type="info" plain @click="jiaohuanjiluCrossAddOrUpdateHandler(scope.row,'cross','是','','[1]','已处理')">交换</el-button>
						<el-button size="mini" v-if="isAuth('jiaohuanshenqing','修改') && scope.row.sfsh=='待审核'" type="warning" plain @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
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
		<!-- 添加/修改页面  将父组件的search方法传递给子组件-->
		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>

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
				pageSize: 15,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				showFlag: true,
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
			jiaohuanjiluCrossAddOrUpdate,
		},
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
</style>
