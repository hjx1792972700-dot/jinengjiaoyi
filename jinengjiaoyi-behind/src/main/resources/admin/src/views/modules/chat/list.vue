<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- 列表页 -->
		<div v-if="!showFlag" style="width: 100%;">
			<div class="admin-search">
				<div class="search-field">
					<label class="item-label">是否回复</label>
					<el-select v-model="searchForm.isreply" placeholder="请选择" size="small">
						<el-option label="已回复" :value="0"></el-option>
						<el-option label="未回复" :value="1"></el-option>
					</el-select>
				</div>
				<div class="search-field">
					<label class="item-label">是否已读</label>
					<el-select v-model="searchForm.isread" placeholder="请选择" size="small">
						<el-option label="已读" :value="1"></el-option>
						<el-option label="未读" :value="0"></el-option>
					</el-select>
				</div>
				<div class="search-actions">
					<el-button class="btn-primary" size="small" type="primary" @click="search()">
						<template #icon><Search /></template>
						查询
					</el-button>
					<el-button class="btn-plain" size="small" @click="resetSearch()">
						<template #icon><RefreshRight /></template>
						重置
					</el-button>
				</div>
			</div>
			<div>
				<div class="admin-table-wrap">
				<el-table
					:stripe='false'
					:data="dataList"
					:border='false'
					v-loading="dataListLoading"
					style="width: 100%;"
					>
					<el-table-column
						:resizable='true' :sortable='true'
						prop="allnode"
						header-align="center"
						align="center"
						sortable
						label="用户"
						>
						<template #default="scope">
							<div style="display: flex;align-items: center;">
								<img :src="scope.row.uimage?$base.url + scope.row.uimage.split(',')[0]:require('@/assets/img/avator.png')" style="width: 80px;height: 80px;border-radius: 50%;margin: 0 5px 0 0;" alt="" />
								{{scope.row.uname}}
							</div>
						</template>
					</el-table-column>
					<el-table-column :resizable='true' :sortable='true' prop="ask" :formatter="askFormat" header-align="center" align="center" sortable label="新消息"></el-table-column>
					<el-table-column
						:resizable='true' :sortable='true'
						prop="allnode"
						header-align="center"
						align="center"
						sortable
						label="是否回复"
						min-width="150"
						>
						<template #default="scope">
							<el-tag v-if="scope.row.isreply==1" type="success">未回复</el-tag>
							<el-tag v-if="scope.row.isreply==0" type="info">已回复</el-tag>
						</template>
					</el-table-column>
					<el-table-column
						:resizable='true' :sortable='true'
						prop="allnode"
						header-align="center"
						align="center"
						sortable
						label="是否已读"
						min-width="150"
						>
						<template #default="scope">
							<el-tag v-if="scope.row.isread==1" type="success">已读</el-tag>
							<el-tag v-if="scope.row.isread==0" type="info">未读</el-tag>
						</template>
					</el-table-column>
					<el-table-column
						header-align="center"
						align="center"
						width="150"
						label="操作"
						>
						<template #default="scope">
							<el-button
								type="info"
								plain
								size="small"
								@click="addOrUpdateHandler(scope.row)"
							>回复</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>

				<div class="admin-pagination"><el-pagination
					@size-change="sizeChangeHandle"
					@current-change="currentChangeHandle"
					:current-page="pageIndex"

					:page-size="pageSize"
					:total="totalPage"
					:layout="layouts.join()"
					prev-text="<"
					next-text=">"
					:hide-on-single-page="false"
				></el-pagination></div>
			</div>
		</div>
		<!-- 添加/修改页面  将父组件的search方法传递给子组件-->
		<add-or-update v-else :parent="this" ref="addOrUpdate"></add-or-update>
	</div>
</template>
<script>
	import AddOrUpdate from "./chat-add-or-update.vue";
	import {
		Search,
		RefreshRight,
	} from '@element-plus/icons-vue'

	export default {
		components: {
			AddOrUpdate,
			Search,
			RefreshRight,
		},
		data() {
			return {
				layouts: ["total","prev","pager","next","jumper"],
				searchForm: {
					isreply:1,
					isread: 0
				},
				dataList: [],
				pageIndex: 1,
				pageSize: 15,
				totalPage: 0,
				dataListLoading: false,
				showFlag: false,
				dataListSelections: [],
				inter: null,
			};
		},
		created() {
			var that = this;
			that.getDataList();
			var inter = setInterval(function(){
				that.getDataList();
			},5000);
			this.inter = inter;
		},
		unmounted(){
			clearInterval(this.inter);
		},
		methods: {
			askFormat(row, column) {
				if (row.ask && row.type==2) {
					return '[图片]'
				} else if(row.ask&&row.type==3) {
					return '[视频]'
				} else if(row.ask&&row.type==4) {
					return '[文件]'
				} else {
					return row.ask
				}
			},
			search() {
				this.pageIndex = 1
				this.getDataList()
			},
			resetSearch() {
				this.searchForm = { isreply: 1, isread: 0 }
				this.search()
			},
			getDataList() {
				let params = {
					page: this.pageIndex,
					limit: this.pageSize,
					sort: 'id',
				}
				params.isreply = this.searchForm.isreply
				params.isread = this.searchForm.isread
				this.dataListLoading = true;
				this.$http({
					url: this.$api.chatpage,
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
			// 回复
			addOrUpdateHandler(row) {
				this.showFlag = true;
				this.$nextTick(() => {
					this.$refs.addOrUpdate.init(row.userid);
				});
			}
		}
	};
</script>
<style lang="scss" scoped>
</style>
