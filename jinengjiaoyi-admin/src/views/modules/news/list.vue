

<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<div class="admin-search">
				<el-button class="btn-primary" size="small" icon="el-icon-plus" v-if="isAuth('news','新增')" @click="addOrUpdateHandler()">新增</el-button>
				<div class="search-field">
					<span class="field-label">标题</span>
					<el-input v-model="searchForm.title" placeholder="标题" @keydown.enter="search()" clearable size="small"></el-input>
				</div>
				<div class="search-actions">
					<el-button class="btn-primary" size="small" icon="el-icon-search" @click="search()">查询</el-button>
					<el-button class="btn-plain" size="small" icon="el-icon-refresh" @click="searchForm.title='';search()">重置</el-button>
				</div>
			</div>

			<div class="admin-table-wrap">
				<el-table
					v-if="isAuth('news','查看')"
					:data="dataList"
					v-loading="dataListLoading"
					style="width:100%"
					@selection-change="selectionChangeHandler">
					<el-table-column :resizable='true' type="selection" align="center" width="50"></el-table-column>
					<el-table-column type="index" :index="indexMethod" :resizable='true' :sortable='true' label="序号" width="50" />
					<el-table-column :resizable='true' :sortable='true'
												prop="title"
						label="标题">
						<template #default="scope">
							{{scope.row.title}}
						</template>
					</el-table-column>
					<el-table-column  :resizable='true' prop="picture" width="200" label="图片">
						<template #default="scope">
							<div v-if="scope.row.picture">
								<img v-if="scope.row.picture.substring(0,4)=='http'&&scope.row.picture.split(',w').length>1" :src="scope.row.picture" width="100" height="100" style="object-fit: cover" @click="imgPreView(scope.row.picture)">
								<img v-else-if="scope.row.picture.substring(0,4)=='http'" :src="scope.row.picture.split(',')[0]" width="100" height="100" style="object-fit: cover" @click="imgPreView(scope.row.picture.split(',')[0])">
								<img v-else :src="$base.url+scope.row.picture.split(',')[0]" width="100" height="100" style="object-fit: cover" @click="imgPreView($base.url+scope.row.picture.split(',')[0])">
							</div>
							<div v-else>无图片</div>
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="name"
						label="发布人">
						<template #default="scope">
							{{scope.row.name}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="typename"
						label="分类名称">
						<template #default="scope">
							{{scope.row.typename}}
						</template>
					</el-table-column>
					<el-table-column width="300" label="操作">
						<template #default="scope">
							<el-button size="small" v-if=" isAuth('news','查看')" type="success" plain @click="addOrUpdateHandler(scope.row.id,'info')">查看</el-button>
							<el-button size="small" v-if=" isAuth('news','修改') " type="warning" plain @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
							<el-button size="small" v-if="isAuth('news','删除')" type="danger" plain @click="deleteHandler(scope.row.id)">删除</el-button>
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
	export default {
		data() {
			return {
				indexQueryCondition: '',
				typenameOptions: [],
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
			imgPreView(url){
				this.previewImg = url
				this.previewVisible = true
				
			},
			init () {
				this.$http({
					url: `option/newstype/typename`,
					method: "get"
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.typenameOptions = data.data;
					} else {
						this.$message.error(data.msg);
					}
				});
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
				if(this.searchForm.title!='' && this.searchForm.title!=undefined){
					params['title'] = '%' + this.searchForm.title + '%'
				}
				if(this.searchForm.typename!='' && this.searchForm.typename!=undefined){
					params['typename'] = this.searchForm.typename
				}
				let user = JSON.parse(this.$storage.getObj('userForm'))
				this.$http({
					url: "news/page",
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
						url: "news/delete",
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
.chat-content {
	padding-bottom: 20px; width: 100%; margin-bottom: 10px;
	max-height: 300px; height: 300px; overflow-y: auto;
	border: 1px solid rgba(255,255,255,0.06); background: #1e293b; border-radius: 6px;
	.addtime { width: 100%; text-align: center; font-size: 12px; color: #64748b; }
	.left-content { float: left; margin-bottom: 10px; padding: 10px; max-width: 80%; display: flex; align-items: center; }
	.right-content { float: right; margin-bottom: 10px; padding: 10px; max-width: 80%; display: flex; align-items: center; }
}
.clear-float { clear: both; }
.noList { color: #64748b; width: 100%; text-align: center; padding: 60px 0; }
</style>
