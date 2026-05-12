  
<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<template v-if="showFlag ">
			<div class="admin-search">
				<div class="search-field">
					<el-button class="btn-plain" size="small" @click="backClick()"><template #icon><Back /></template>返回</el-button>
				</div>
			</div>
			<div class="admin-actions">
				<div class="action-left">
					<el-button class="btn-plain" size="small" @click="refreshClick()"><template #icon><RefreshRight /></template>刷新</el-button>
					<el-button class="btn-danger" v-if="isAuth('discussxuexiziliao','删除')" :disabled="dataListSelections.length?false:true" size="small" @click="deleteHandler()"><template #icon><Delete /></template>删除</el-button>
				</div>
			</div>
			<div class="admin-table-wrap">
				<el-table class="tables"
					:stripe='false'
					:border='false'
					:data="dataList"
					v-loading="dataListLoading"
					@selection-change="selectionChangeHandler">
					<el-table-column :resizable='true' type="selection" align="center" width="50"></el-table-column>
					<el-table-column :resizable='true' :sortable='true' label="序号" type="index" width="50" />
				<el-table-column :resizable='true' :sortable='true'
											prop="nickname"
					label="昵称">
						<template #default="scope">
							{{scope.row.nickname}}
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true' prop="content" label="评论内容">
						<template #default="scope">
							<span class="ql-snow ql-editor" v-html="scope.row.content"></span>
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true' prop="reply" label="回复内容">
						<template #default="scope">
							<span class="ql-snow ql-editor" v-html="scope.row.reply"></span>
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true' prop="istop" label="是否置顶">
						<template #default="scope">
							<el-switch
								v-model="scope.row.istop"
								:width="36"
								active-text="开"
								inactive-text="关"
								:active-value="1"
								:inactive-value="0"
								style="--el-switch-on-color: #875F41; --el-switch-off-color: #ccc;"
								@change="(e)=>discussistopChange(e,scope.row)">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column width="300" label="操作">
						<template #default="scope">
							<el-button v-if=" isAuth('discussxuexiziliao','修改') " type="warning" plain size="small" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
							<el-button v-if="isAuth('discussxuexiziliao','查看评论')" type="success" plain size="small" @click="disscussListHandler(scope.row.id)">查看评论</el-button>
							<el-button type="info" plain size="small" @click="addOrUpdateHandler(scope.row.id)">回复</el-button>
							<el-button v-if="isAuth('xuexiziliao','删除评论')" type="danger" plain size="small" @click="deleteHandler(scope.row.id ,scope.row.refid)">删除</el-button>
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
			Back,
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
					refid : this.$route.query.refid,
				}
				let user = JSON.parse(this.$storage.getObj('userForm'))
				this.$http({
					url: "discussxuexiziliao/page",
					method: "get",
					params: params
				}).then(({ data }) => {
					if (data && data.code === 0) {
						for(let x in data.data.list) {
							if(data.data.list[x].content) {
								data.data.list[x].content = data.data.list[x].content.replace(/img src/gi,"img style=\"width:100%;\" src");
							}
							if(data.data.list[x].reply) {
								data.data.list[x].reply = data.data.list[x].reply.replace(/img src/gi,"img style=\"width:100%;\" src");
							}
						}
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
				this.$router.push({path:'/discussxuexiziliao',query:{refid:id}});
			},
			backClick(){
				history.back()
			},
			discussistopChange(e,row){
				this.$http({
					url:'discussxuexiziliao/update',
					method:'post',
					data:row
				}).then(res=>{})
			},
			// 删除
			async deleteHandler(id ,refid ) {
				var ids = id? [Number(id)]: this.dataListSelections.map(item => {
					return Number(item.id);
				});
				await this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
					confirmButtonText: "确定",
					cancelButtonText: "取消",
					type: "warning"
				}).then(async () => {
					await this.$http({
						url: "discussxuexiziliao/delete",
						method: "post",
						data: ids
					}).then(async ({ data }) => {
						if (data && data.code === 0) {
							this.$http({
								url: 'xuexiziliao/info/' + refid,
								method: "get",
							}).then(res=>{
								if(res.data&&res.data.code==0){
									res.data.data.discussnum = res.data.data.discussnum - Number(ids.length)
									this.$http({
										url: 'xuexiziliao/update',
										method: "post",
										data:res.data.data
									}).then(res1=>{
										if(res1.data&&res1.data.code==0){
											this.$message({
												message: "操作成功",
												type: "success",
												duration: 1500,
												onClose: () => {
													this.search();
												}
											});
										}
									})
								}
							})
			
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
