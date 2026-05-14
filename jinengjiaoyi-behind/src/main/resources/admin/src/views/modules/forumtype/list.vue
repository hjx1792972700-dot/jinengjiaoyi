  
<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<div class="admin-search">
				<el-button class="btn-primary" v-if="isAuth('forumtype','新增')" size="small" icon="el-icon-plus" @click="addOrUpdateHandler()">新增</el-button>
				<div class="search-field">
					<span class="field-label">分类名称</span>
					<el-input v-model="searchForm.typename" placeholder="分类名称" @keydown.enter="search()" clearable size="small"></el-input>
				</div>
				<div class="search-actions">
					<el-button class="btn-primary" size="small" icon="el-icon-search" @click="search()">查询</el-button>
					<el-button class="btn-plain" size="small" icon="el-icon-refresh" @click="searchForm={ key: '' }; search()">重置</el-button>
				</div>
			</div>
			<div class="admin-table-wrap">
				<el-table class="tables"
					:stripe='false'
					:border='false'
					v-if="isAuth('forumtype','查看')"
					:data="dataList"
					v-loading="dataListLoading"
					@selection-change="selectionChangeHandler">
					<el-table-column :resizable='true' type="selection" align="center" width="50"></el-table-column>
					<el-table-column :resizable='true' :sortable='true' type="index" :index="indexMethod" label="序号" width="50" />
					<el-table-column :resizable='true' :sortable='true'
												prop="typename"
						label="分类名称">
						<template #default="scope">
							{{scope.row.typename}}
						</template>
					</el-table-column>
				<el-table-column width="200" label="操作">
					<template #default="scope">
						<el-button v-if=" isAuth('forumtype','修改') " type="warning" plain size="small" @click="modifyWithCheck(scope.row)">修改</el-button>
						<el-button v-if="isAuth('forumtype','删除')" type="danger" plain size="small" @click="deleteWithCheck(scope.row)">删除</el-button>
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





	</div>
</template>

<script>
	import * as echarts from 'echarts'
	import chinaJson from "@/components/echarts/china.json";
	import axios from 'axios';
	import AddOrUpdate from "./add-or-update.vue";
	export default {
		data() {
			return {
				indexQueryCondition: '',
				searchForm: {
					key: ""
				},
				form:{},
				dataList: [],
				pageIndex: 1,
				pageSize: 7,
				totalPage: 0,
				dataListLoading: false,
				dataListSelections: [],
				showFlag: true,
				addOrUpdateFlag:false,
				layouts: ["total","prev","pager","next","jumper"],
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
			AddOrUpdate},
		methods: {
			htmlfilter(val) {
				return String(val == null ? '' : val).replace(/<[^>]*>/g, '').replace(/undefined/g, '');
			},
			queryChange(arr){
				for(let x in arr) {
					if(arr[x] == this.role) {
						return true
					}
				}
				return false
			},
			init () {
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
				if(this.searchForm.typename!='' && this.searchForm.typename!=undefined){
					params['typename'] = '%' + this.searchForm.typename + '%'
				}
				let user = JSON.parse(this.$storage.getObj('userForm'))
				this.$http({
					url: "forumtype/page",
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
			async checkRelatedData(row) {
				try {
					const { data } = await this.$http({
						url: 'forum/list',
						method: 'get',
						params: { page: 1, limit: 1, typename: row.typename }
					});
					if (data && data.code === 0 && data.data.list && data.data.list.length > 0) {
						return true;
					}
				} catch(e) {}
				return false;
			},
			async modifyWithCheck(row) {
				const hasData = await this.checkRelatedData(row);
				if (hasData) {
					await this.$confirm('该类型下已有帖子数据，确定要修改吗？', '提示', {
						confirmButtonText: '确定修改',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.addOrUpdateHandler(row.id);
					}).catch(() => {});
				} else {
					this.addOrUpdateHandler(row.id);
				}
			},
			async deleteWithCheck(row) {
				const hasData = await this.checkRelatedData(row);
				if (hasData) {
					this.$message.error('该类型下已有帖子数据，无法删除');
					return;
				}
				this.deleteHandler(row.id);
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
						url: "forumtype/delete",
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
</style>
