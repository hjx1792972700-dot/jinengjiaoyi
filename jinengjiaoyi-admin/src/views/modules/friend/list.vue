  
<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<div class="admin-search"></div>
			<div class="admin-actions">
				<div class="action-left">
					<el-button class="btn-plain" size="small" @click="refreshClick()"><template #icon><RefreshRight /></template>刷新</el-button>
					<el-button class="btn-danger" v-if="isAuth('friend','删除')" :disabled="dataListSelections.length?false:true" size="small" @click="deleteHandler()"><template #icon><Delete /></template>删除</el-button>
				</div>
			</div>
			<div class="admin-table-wrap">
				<el-table class="tables"
					:stripe='false'
					:border='false'
					v-if="isAuth('friend','查看')"
					:data="dataList"
					v-loading="dataListLoading"
					@selection-change="selectionChangeHandler">
					<el-table-column :resizable='true' type="selection" align="center" width="50"></el-table-column>
					<el-table-column :resizable='true' :sortable='true' label="序号" type="index" width="50" />
					<el-table-column :resizable='true' :sortable='true'
												prop="uid"
						label="用户ID">
						<template #default="scope">
							{{scope.row.uid}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="fid"
						label="好友用户ID">
						<template #default="scope">
							{{scope.row.fid}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="name"
						label="名称">
						<template #default="scope">
							{{scope.row.name}}
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
												prop="role"
						label="角色">
						<template #default="scope">
							{{scope.row.role}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="tablename"
						label="表名">
						<template #default="scope">
							{{scope.row.tablename}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="alias"
						label="别名">
						<template #default="scope">
							{{scope.row.alias}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true'
												prop="type"
						label="类型(0:好友申请，1:好友，2:消息)">
						<template #default="scope">
							{{scope.row.type}}
						</template>
					</el-table-column>
					<el-table-column width="300" label="操作">
						<template #default="scope">
							<el-button v-if=" isAuth('friend','查看')" type="success" plain size="small" @click="addOrUpdateHandler(scope.row.id,'info')">查看</el-button>
							<el-button v-if=" isAuth('friend','修改') " type="warning" plain size="small" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
							<el-button v-if="isAuth('friend','删除')" type="danger" plain size="small" @click="deleteHandler(scope.row.id)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<div class="admin-pagination"><el-pagination
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
			></el-pagination></div>
		</template>
		<!-- 添加/修改页面  将父组件的search方法传递给子组件-->
		<add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>




		<el-dialog title="预览图" :visible.sync="previewVisible" width="480px" :close-on-click-modal="false">
			<img :src="previewImg" alt="" style="width: 100%;">
			<template #footer>
				<el-button class="btn-primary" size="small" @click="previewVisible = false">关闭</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script>
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
				pageSize: 15,
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
			AddOrUpdate,
			RefreshRight,
			Delete,
		},
		methods: {
			refreshClick(){
				this.searchForm = {}
				this.search()
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
				let user = JSON.parse(this.$storage.getObj('userForm'))
				this.$http({
					url: "friend/page",
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
			// 查看评论
			disscussListHandler(id,type) {
				this.$router.push({path:'/discussfriend',query:{refid:id}});
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
						url: "friend/delete",
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
