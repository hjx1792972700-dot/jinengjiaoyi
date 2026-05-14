<template>
	<div class="main-content" style="min-height:100%;width:100%;padding:0;background:none;">
		<!-- Search & Filter -->
		<div class="admin-search">
			<div class="search-field">
				<span class="field-label">用户姓名</span>
				<el-input v-model="searchForm.yonghuxingming" placeholder="请输入姓名搜索" @keydown.enter="search()" clearable size="small"></el-input>
			</div>
			<div class="search-field">
				<span class="field-label">VIP状态</span>
				<el-select v-model="searchForm.vip" placeholder="全部" clearable size="small" @change="search()">
					<el-option label="全部" value=""></el-option>
					<el-option label="VIP用户" value="是"></el-option>
					<el-option label="非VIP用户" value="否"></el-option>
				</el-select>
			</div>
			<div class="search-field">
				<span class="field-label">VIP到期</span>
				<el-select v-model="searchForm.expireStatus" placeholder="全部" clearable size="small" @change="search()">
					<el-option label="全部" value=""></el-option>
					<el-option label="已过期" value="expired"></el-option>
					<el-option label="7天内到期" value="expiring"></el-option>
				</el-select>
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

		<!-- Action Bar -->
		<div class="admin-actions">
			<div class="action-left">
				<el-button class="btn-primary" size="small" :disabled="!dataListSelections.length" @click="batchSetVip('是')">
					<template #icon><Star /></template>
					批量开通VIP
				</el-button>
				<el-button class="btn-danger" size="small" :disabled="!dataListSelections.length" @click="batchSetVip('否')">
					<template #icon><Close /></template>
					批量取消VIP
				</el-button>
			</div>
			<div class="action-right">
				<span style="font-size:12px;color:#64748b;">已选 {{dataListSelections.length}} 项</span>
			</div>
		</div>

		<!-- Table -->
		<div class="admin-table-wrap">
			<el-table
				:data="dataList"
				v-loading="dataListLoading"
				:border="false"
				:stripe="false"
				style="width:100%"
				@selection-change="selectionChangeHandler">
				<el-table-column type="selection" align="center" width="40"></el-table-column>
				<el-table-column type="index" :index="indexMethod" label="序号" width="55" align="center"></el-table-column>
				<el-table-column prop="touxiang" width="60" label="头像" align="center">
					<template #default="scope">
						<div v-if="scope.row.touxiang">
							<img v-if="scope.row.touxiang.substring(0,4)=='http'" :src="scope.row.touxiang.split(',')[0]" width="32" height="32" style="object-fit:cover;border-radius:6px;">
							<img v-else :src="$base.url+scope.row.touxiang.split(',')[0]" width="32" height="32" style="object-fit:cover;border-radius:6px;">
						</div>
						<div v-else style="color:#475569;font-size:11px;">-</div>
					</template>
				</el-table-column>
				<el-table-column sortable prop="yonghuzhanghao" label="账号" min-width="100" show-overflow-tooltip></el-table-column>
				<el-table-column prop="yonghuxingming" label="姓名" width="90" align="center"></el-table-column>
				<el-table-column prop="shoujihao" label="手机号" min-width="120" show-overflow-tooltip></el-table-column>
				<el-table-column sortable prop="vip" label="VIP状态" width="90" align="center">
					<template #default="scope">
						<span class="vip-badge vip-active" v-if="scope.row.vip=='是'">VIP</span>
						<span class="vip-badge vip-inactive" v-else>普通</span>
					</template>
				</el-table-column>
				<el-table-column sortable prop="vipExpire" label="VIP到期时间" width="130" align="center">
					<template #default="scope">
						<template v-if="scope.row.vipExpire">
							<span :class="getExpireClass(scope.row.vipExpire)">{{scope.row.vipExpire.substring(0,10)}}</span>
							<div v-if="isExpired(scope.row.vipExpire)" class="expire-tag expired">已过期</div>
							<div v-else-if="isExpiring(scope.row.vipExpire)" class="expire-tag expiring">即将到期</div>
						</template>
						<span v-else style="color:#475569;">-</span>
					</template>
				</el-table-column>
				<el-table-column prop="xinyuzhishu" label="信誉" width="60" align="center">
					<template #default="scope">{{scope.row.xinyuzhishu != null ? parseInt(scope.row.xinyuzhishu) || 0 : '-'}}</template>
				</el-table-column>
				<el-table-column label="操作" width="220" fixed="right" align="center">
					<template #default="scope">
						<div style="display:flex;justify-content:center;gap:4px;flex-wrap:nowrap;">
							<el-button size="mini" type="warning" plain @click="openVipDialog(scope.row)">设置VIP</el-button>
							<el-button size="mini" v-if="scope.row.vip=='是'" type="success" plain @click="extendVip(scope.row)">续期30天</el-button>
							<el-button size="mini" v-if="scope.row.vip=='是'" type="danger" plain @click="cancelVip(scope.row)">取消VIP</el-button>
						</div>
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
				background
				:page-size="pageSize"
				layout="total,prev,pager,next,jumper"
				:total="totalPage"
				prev-text="<"
				next-text=">"
				:hide-on-single-page="false"
			></el-pagination>
		</div>

		<!-- VIP Setting Dialog -->
		<el-dialog title="设置VIP" :visible.sync="vipDialogVisible" width="480px" :close-on-click-modal="false">
			<el-form ref="vipForm" :model="vipForm" label-width="100px">
				<el-form-item label="用户">
					<el-input :value="vipForm.yonghuxingming + ' (' + vipForm.yonghuzhanghao + ')'" readonly></el-input>
				</el-form-item>
				<el-form-item label="VIP状态">
					<el-select v-model="vipForm.vip" placeholder="请选择" style="width:100%">
						<el-option label="是" value="是"></el-option>
						<el-option label="否" value="否"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="到期时间">
					<el-date-picker
						v-model="vipForm.vipExpire"
						type="date"
						format="YYYY-MM-DD"
						value-format="YYYY-MM-DD"
						placeholder="选择到期时间"
						style="width:100%"
					></el-date-picker>
				</el-form-item>
				<el-form-item label="快捷设置">
					<el-button size="small" @click="quickSetExpire(30)">30天</el-button>
					<el-button size="small" @click="quickSetExpire(90)">90天</el-button>
					<el-button size="small" @click="quickSetExpire(180)">半年</el-button>
					<el-button size="small" @click="quickSetExpire(365)">一年</el-button>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button class="btn-plain" size="small" @click="vipDialogVisible=false">取消</el-button>
				<el-button class="btn-primary" size="small" @click="saveVipSetting">确认保存</el-button>
			</template>
		</el-dialog>
	</div>
</template>

<script>
export default {
	data() {
		return {
			searchForm: {},
			dataList: [],
			pageIndex: 1,
			pageSize: 10,
			totalPage: 0,
			dataListLoading: false,
			dataListSelections: [],
			vipDialogVisible: false,
			vipForm: {
				id: '',
				yonghuzhanghao: '',
				yonghuxingming: '',
				vip: '',
				vipExpire: ''
			},
		}
	},
	created() {
		this.getDataList()
	},
	methods: {
		indexMethod(index) {
			return (this.pageIndex - 1) * this.pageSize + index + 1
		},
		search() {
			this.pageIndex = 1
			this.getDataList()
		},
		getDataList() {
			this.dataListLoading = true
			let params = {
				page: this.pageIndex,
				limit: this.pageSize,
				sort: 'id',
				order: 'desc',
			}
			if (this.searchForm.yonghuxingming) {
				params['yonghuxingming'] = '%' + this.searchForm.yonghuxingming + '%'
			}
			if (this.searchForm.vip) {
				params['vip'] = this.searchForm.vip
			}
			this.$http({
				url: 'yonghu/page',
				method: 'get',
				params
			}).then(({ data }) => {
				if (data && data.code === 0) {
					let list = data.data.list
					if (this.searchForm.expireStatus === 'expired') {
						list = list.filter(u => u.vip === '是' && this.isExpired(u.vipExpire))
					} else if (this.searchForm.expireStatus === 'expiring') {
						list = list.filter(u => u.vip === '是' && this.isExpiring(u.vipExpire) && !this.isExpired(u.vipExpire))
					}
					this.dataList = list
					this.totalPage = this.searchForm.expireStatus ? list.length : data.data.total
				} else {
					this.dataList = []
					this.totalPage = 0
				}
				this.dataListLoading = false
			})
		},
		sizeChangeHandle(val) {
			this.pageSize = val
			this.pageIndex = 1
			this.getDataList()
		},
		currentChangeHandle(val) {
			this.pageIndex = val
			this.getDataList()
		},
		selectionChangeHandler(val) {
			this.dataListSelections = val
		},
		isExpired(dateStr) {
			if (!dateStr) return false
			return new Date(dateStr.substring(0, 10)) < new Date(new Date().toISOString().substring(0, 10))
		},
		isExpiring(dateStr) {
			if (!dateStr) return false
			const expire = new Date(dateStr.substring(0, 10))
			const now = new Date(new Date().toISOString().substring(0, 10))
			const diff = (expire - now) / (1000 * 60 * 60 * 24)
			return diff >= 0 && diff <= 7
		},
		getExpireClass(dateStr) {
			if (this.isExpired(dateStr)) return 'expire-text expired-text'
			if (this.isExpiring(dateStr)) return 'expire-text expiring-text'
			return 'expire-text normal-text'
		},
		openVipDialog(row) {
			this.vipForm = {
				id: row.id,
				yonghuzhanghao: row.yonghuzhanghao,
				yonghuxingming: row.yonghuxingming,
				vip: row.vip || '否',
				vipExpire: row.vipExpire ? row.vipExpire.substring(0, 10) : ''
			}
			this.vipDialogVisible = true
		},
		quickSetExpire(days) {
			const base = this.vipForm.vipExpire && !this.isExpired(this.vipForm.vipExpire)
				? new Date(this.vipForm.vipExpire)
				: new Date()
			base.setDate(base.getDate() + days)
			this.vipForm.vipExpire = base.toISOString().substring(0, 10)
			this.vipForm.vip = '是'
		},
		saveVipSetting() {
			const payload = {
				id: this.vipForm.id,
				vip: this.vipForm.vip,
				vipExpire: this.vipForm.vipExpire ? this.vipForm.vipExpire + ' 00:00:00' : null
			}
			if (this.vipForm.vip === '否') {
				payload.vipExpire = null
			}
			this.$http({
				url: 'yonghu/update',
				method: 'post',
				data: payload
			}).then(({ data }) => {
				if (data && data.code === 0) {
					this.$message({ message: 'VIP设置成功', type: 'success', duration: 1500 })
					this.vipDialogVisible = false
					this.getDataList()
				} else {
					this.$message.error(data.msg || '操作失败')
				}
			})
		},
		extendVip(row) {
			const base = row.vipExpire && !this.isExpired(row.vipExpire)
				? new Date(row.vipExpire.substring(0, 10))
				: new Date()
			base.setDate(base.getDate() + 30)
			const newExpire = base.toISOString().substring(0, 10)
			this.$confirm(`确定为用户 ${row.yonghuxingming} 续期30天？到期时间将变为 ${newExpire}`, '续期确认', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'info'
			}).then(() => {
				this.$http({
					url: 'yonghu/update',
					method: 'post',
					data: { id: row.id, vip: '是', vipExpire: newExpire + ' 00:00:00' }
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.$message({ message: '续期成功', type: 'success', duration: 1500 })
						this.getDataList()
					} else {
						this.$message.error(data.msg || '操作失败')
					}
				})
			}).catch(() => {})
		},
		cancelVip(row) {
			this.$confirm(`确定取消用户 ${row.yonghuxingming} 的VIP资格？`, '取消VIP', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(() => {
				this.$http({
					url: 'yonghu/update',
					method: 'post',
					data: { id: row.id, vip: '否', vipExpire: null }
				}).then(({ data }) => {
					if (data && data.code === 0) {
						this.$message({ message: 'VIP已取消', type: 'success', duration: 1500 })
						this.getDataList()
					} else {
						this.$message.error(data.msg || '操作失败')
					}
				})
			}).catch(() => {})
		},
		batchSetVip(status) {
			const ids = this.dataListSelections.map(item => item.id)
			const label = status === '是' ? '开通' : '取消'
			this.$confirm(`确定为选中的 ${ids.length} 个用户${label}VIP？`, '批量操作', {
				confirmButtonText: '确定',
				cancelButtonText: '取消',
				type: 'warning'
			}).then(async () => {
				let expire = null
				if (status === '是') {
					const d = new Date()
					d.setDate(d.getDate() + 365)
					expire = d.toISOString().substring(0, 10) + ' 00:00:00'
				}
				for (const id of ids) {
					await this.$http({
						url: 'yonghu/update',
						method: 'post',
						data: { id, vip: status, vipExpire: expire }
					})
				}
				this.$message({ message: `批量${label}VIP成功`, type: 'success', duration: 1500 })
				this.getDataList()
			}).catch(() => {})
		}
	}
}
</script>

<style lang="scss" scoped>
.vip-badge {
	display: inline-block; padding: 2px 10px; border-radius: 10px; font-size: 11px; font-weight: 700;
}
.vip-active {
	background: linear-gradient(135deg, rgba(245,158,11,0.2), rgba(234,179,8,0.15));
	color: #fbbf24; border: 1px solid rgba(245,158,11,0.4);
}
.vip-inactive {
	background: rgba(100,116,139,0.1); color: #64748b; border: 1px solid rgba(100,116,139,0.2);
}
.expire-text { font-size: 12px; }
.expired-text { color: #f87171; }
.expiring-text { color: #fbbf24; }
.normal-text { color: #e2e8f0; }
.expire-tag {
	display: inline-block; padding: 1px 6px; border-radius: 6px; font-size: 10px; font-weight: 600; margin-top: 2px;
}
.expire-tag.expired { background: rgba(239,68,68,0.15); color: #f87171; border: 1px solid rgba(239,68,68,0.3); }
.expire-tag.expiring { background: rgba(245,158,11,0.15); color: #fbbf24; border: 1px solid rgba(245,158,11,0.3); }
</style>
