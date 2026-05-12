<template>
	<div :class="{'dark-embedded': embedded}">
		<div class="breadcrumb-preview" v-if="!embedded">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2" v-for="(item, index) in breadcrumbItem" :key="index"><a>{{item.name}}</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>
		<div v-if="centerType && !embedded" class="back_box">
			<el-button class="backBtn" size="mini" @click="backClick">
				<span class="icon iconfont icon-fanhui01"></span>
				<span class="text">返回</span>
			</el-button>
		</div>
		<div class="list-preview">
			<el-form :inline="true" :model="formSearch" class="list-form-pv">
				<el-form-item class="list-item" v-if="queryChange(['管理员'])">
					<div class="lable">用户姓名：</div>
					<el-input v-model="formSearch.yonghuxingming" placeholder="用户姓名" @keydown.enter="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
				<el-form-item class="list-item" >
					<div class="lable">需求标题：</div>
					<el-input v-model="formSearch.xuqiubiaoti" placeholder="需求标题" @keydown.enter="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
			<el-form-item class="list-item" >
				<div class="lable">技能分类：</div>
				<el-select v-model="formSearch.jinengfenlei" placeholder="请选择分类" clearable @change="getList(1, curFenlei)">
					<el-option v-for="item in fenleiOptions" :key="item" :label="item" :value="item"></el-option>
				</el-select>
			</el-form-item>
				<el-form-item class="list-item" v-if="queryChange(['管理员'])">
				<div class="lable">技能发布者：</div>
				<el-input v-model="formSearch.gongjirenxingming" placeholder="技能发布者" @keydown.enter="getList(1, curFenlei)" clearable></el-input>
				</el-form-item>
				<div class="list-btn-box">
					<el-button class="list-search-btn" v-if=" true " type="primary" @click="getList(1, curFenlei)">
						<span class="icon iconfont icon-fangdajing07"></span>
						搜索
					</el-button>
					<el-button class="list-add-btn" v-if="btnAuth('jiaohuanshenqing','新增')" type="primary" @click="add('/main/jiaohuanshenqingAdd')">
						<span class="icon iconfont icon-tianjia7"></span>
						新增
					</el-button>
  
				</div>
			</el-form>
			<div class="list">
				<div class="list5">
					<div v-for="(item,index) in dataList" :key="index" class="list-item" @click.stop="toDetail(item)" >
						<div class="card-inner">
							<div class="imgbox">
								<img v-if="item.fengmian && item.fengmian.substr(0,4)=='http'&&item.fengmian.split(',w').length>1" :src="item.fengmian" class="image" />
								<img v-else-if="item.fengmian && item.fengmian.substr(0,4)=='http'" :src="item.fengmian.split(',')[0]" class="image" />
								<img v-else :src="baseUrl + (item.fengmian?item.fengmian.split(',')[0]:'')" class="image" />
								<div class="card-badge">
									<el-icon><CircleCheck /></el-icon> 申请
								</div>
							</div>
							<div class="infoBox">
								<div class="card-title">{{item.xuqiubiaoti}}</div>
								<div class="card-tags-row">
									<div class="card-tag">{{item.jinengfenlei}}</div>
									<div class="card-status done" v-if="item.sfsh=='已完成'"><el-icon><CircleCheck /></el-icon> 已完成</div>
									<div class="card-status confirming" v-else-if="item.sfsh=='待确认'"><el-icon><Select /></el-icon> 待你确认</div>
									<div class="card-status fail" v-else-if="item.sfsh=='否'"><el-icon><CircleClose /></el-icon> 已拒绝</div>
									<div class="card-status pending" v-else><el-icon><Clock /></el-icon> 待处理</div>
								</div>
								<div class="selected-skill-info" v-if="(item.sfsh=='待确认' || item.sfsh=='已完成') && item.xuanzejinengbiaoti">
									<el-icon><Sort /></el-icon>
									<span>对方选择了你的技能：<strong>{{item.xuanzejinengbiaoti}}</strong></span>
								</div>
								<div class="bottomInfo">
									<div class="time_item">
										<el-icon><Clock /></el-icon>
										<span class="text">{{item.addtime.split(' ')[0]}}</span>
									</div>
									<div class="publisher_item">
										<el-icon><Goods /></el-icon>
										<span class="text">技能发布者：{{item.yonghuxingming || item.yonghuzhanghao}}</span>
									</div>
								</div>
								<div class="card-confirm-actions" v-if="centerType && item.sfsh=='待确认'" @click.stop>
								<button class="confirm-btn accept" @click.stop="confirmExchange(item)">
									<el-icon><Check /></el-icon> 确认交换
								</button>
								<button class="confirm-btn decline" @click.stop="declineExchange(item)">
									<el-icon><Close /></el-icon> 拒绝
								</button>
							</div>
							<div class="card-action" v-else>
									<span class="action-text">查看详情</span>
									<el-icon><ArrowRight /></el-icon>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

	
			<el-pagination
				background
				id="pagination"
				class="pagination"
				:pager-count="7"
				:page-size="pageSize"
				prev-text="<"
				next-text=">"
				:hide-on-single-page="true"
				:layout='["total","prev","pager","next","sizes","jumper"].join()'
				:total="total"
				:page-sizes="pageSizes"
				@current-change="curChange"
				@size-change="sizeChange"
				@prev-click="prevClick"
				@next-click="nextClick"
				></el-pagination>
		</div>

		<div class="custom-modal-mask" v-if="declineVisible" @click.self="declineVisible = false">
			<div class="custom-modal">
				<div class="cm-header">
					<span>拒绝交换</span>
					<el-icon class="cm-close-icon" @click="declineVisible = false"><Close /></el-icon>
				</div>
				<div class="cm-body">
					<p style="font-size:13px;color:#94a3b8;margin-bottom:12px;">确定拒绝此交换？对方将看到拒绝信息。</p>
					<el-input type="textarea" :rows="3" v-model="declineReason" placeholder="请输入拒绝原因（选填）"></el-input>
				</div>
				<div class="cm-footer">
					<button class="cm-btn cm-btn-cancel" @click="declineVisible = false">取消</button>
					<button class="cm-btn cm-btn-danger" @click="submitDecline" :disabled="declineLoading">
						<el-icon class="is-loading" v-if="declineLoading"><Loading /></el-icon>确认拒绝
					</button>
				</div>
			</div>
		</div>
	</div>
</template>
<script>
	export default {
		props: { embedded: { type: Boolean, default: false } },
		//数据集合
		data() {
			return {
				layouts: '',
				swiperIndex: -1,
				baseUrl: '',
				breadcrumbItem: [
					{
						name: '交换申请'
					}
				],
				formSearch: {
					yonghuxingming: '',
					xuqiubiaoti: '',
					jinengfenlei: '',
					gongjirenxingming: '',
				},
			fenlei: [],
			fenleiOptions: [],
			dataList: [],
				total: 1,
				pageSize: 8,
				pageSizes: [],
				totalPage: 1,
				curFenlei: '全部',
				isPlain: false,
				indexQueryCondition: '',
				timeRange: [],
				centerType:false,
				sortType: 'id',
				sortOrder: 'desc',
				declineVisible: false,
				declineReason: '',
				declineItem: null,
				declineLoading: false,
			}
		},
		async created() {
			if(this.embedded || (this.$route.query.centerType&&this.$route.query.centerType!=0)){
				this.centerType = true
			}
			this.baseUrl = this.$config.baseUrl;
			if(this.centerType) {
			}
			await this.getFenlei();
			let fenlei = '全部'
			if(this.$route.query.homeFenlei){
				fenlei = this.$route.query.homeFenlei
			}
			this.getList(1, fenlei);
		},
		watch:{
			$route(newValue){
				this.getList(1, newValue.query.homeFenlei);
			}
		},
		computed: {
			role(){
				return localStorage.getItem('frontRole');
			},
			username() {
				return localStorage.getItem('username')
			}
		},
		//方法集合
		methods: {
			queryChange(arr){
				for(let x in arr) {
					if(arr[x] == this.role) {
						return true
					}
				}
				return false
			},
			add(path) {
				let query = {}
				if(this.centerType){
					query.centerType = 1
				}
				this.$router.push({path: path,query:query});
			},
		async getFenlei() {
			await this.$http.get('option/jinengfenlei/jinengfenlei').then(res => {
				if (res.data.code == 0) {
					this.fenleiOptions = res.data.data || [];
				}
			});
		},
			getList(page, fenlei, ref = '') {
				let params = {
					page,
					limit: this.pageSize,
				};
				let searchWhere = {};
				if (this.formSearch.yonghuxingming != '') searchWhere.yonghuxingming = '%' + this.formSearch.yonghuxingming + '%';
				if (this.formSearch.xuqiubiaoti != '') searchWhere.xuqiubiaoti = '%' + this.formSearch.xuqiubiaoti + '%';
				if (this.formSearch.jinengfenlei != '') searchWhere.jinengfenlei = this.formSearch.jinengfenlei;
				if (this.formSearch.gongjirenxingming != '') searchWhere.gongjirenxingming = '%' + this.formSearch.gongjirenxingming + '%';
				if(!this.centerType){
					params['sfsh'] = '已完成';
				}
				let user = JSON.parse(localStorage.getItem('sessionForm'))
				if (this.sortType) searchWhere.sort = this.sortType
				if (this.sortOrder) searchWhere.order = this.sortOrder
				this.$http.get(`jiaohuanshenqing/${this.centerType?'myApply':'list'}`, {params: Object.assign(params, searchWhere)}).then(async res => {
					if (res.data.code == 0) {
						this.dataList = res.data.data.list;
						this.total = Number(res.data.data.total);
						this.pageSize = Number(res.data.data.pageSize);
						this.totalPage = res.data.data.totalPage;
						if(this.pageSizes.length==0){
							this.pageSizes = [8, 16, 24];
						}
					}
				});
			},
			curChange(page) {
				this.getList(page);
			},
			prevClick(page) {
				this.getList(page);
			},
			sizeChange(size){
				this.pageSize = size
				this.getList(1);
			},
			nextClick(page) {
				this.getList(page);
			},
			toDetail(item) {
				let params = {
					id: item.id
				}
				if(this.centerType){
					params.centerType = 1
				}
				this.$router.push({path: '/main/jiaohuanshenqingDetail', query: params});
			},
			btnAuth(tableName,key){
				if(this.centerType){
					return this.isBackAuth(tableName,key)
				}else{
					return this.isAuth(tableName,key)
				}
			},
			backClick() {
				this.$router.push({path: '/main/center'});
			},

		chapterClick (row){
				let params = {
					refid: row.id
				}
				if(this.centerType){
					params.centerType = 1
				}
				this.$router.push({path: '/main/chapterjiaohuanshenqing', query: params});
			},
			confirmExchange(item) {
				this.$confirm(`确认与「${item.yonghuxingming}」完成技能交换？\n你提供：${item.xuanzejinengbiaoti}\n对方提供：${item.xuqiubiaoti}`, '确认交换', {
					confirmButtonText: '确认',
					cancelButtonText: '取消',
					type: 'success'
				}).then(() => {
					this.$http.post('jiaohuanshenqing/confirmExchange', { id: item.id }).then(({ data }) => {
						if (data && data.code === 0) {
							this.$message.success('交换完成！已自动生成交换记录')
							this.getList(1, this.curFenlei)
						} else {
							this.$message.error(data.msg || '操作失败')
						}
					})
				}).catch(() => {})
			},
			declineExchange(item) {
				this.declineItem = item
				this.declineReason = ''
				this.declineVisible = true
			},
			submitDecline() {
				if (!this.declineItem) return
				this.declineLoading = true
				this.$http.post('jiaohuanshenqing/rejectExchange', {
					id: this.declineItem.id,
					shhf: this.declineReason
				}).then(({ data }) => {
					this.declineLoading = false
					if (data && data.code === 0) {
						this.declineVisible = false
						this.$message.success('已拒绝')
						this.getList(1, this.curFenlei)
					} else {
						this.$message.error(data.msg || '操作失败')
					}
				}).catch(() => { this.declineLoading = false })
			},
	}
	}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$primary: #0ea5e9;
$primary-light: #38bdf8;
$purple: #7c3aed;
$text-dark: #e2e8f0;
$text-gray: #94a3b8;
$border: rgba(0,212,255,0.12);
$bg: #0c1222;
$white: #0f172a;
$radius: 12px;
$shadow: 0 4px 20px rgba(0,0,0,0.3);

.list-preview {
	max-width: 1100px;
	margin: 0 auto;
	padding: 20px;

	.list {
		width: 100%;
	}
}

.list-form-pv {
	display: flex;
	flex-wrap: wrap;
	align-items: flex-end;
	gap: 12px;
	margin-bottom: 24px;
	padding: 20px;
	background: $white;
	border-radius: $radius;
	box-shadow: $shadow;

	.list-item {
		margin: 0;
		::v-deep .el-form-item__content {
			display: flex;
			align-items: center;
			flex-wrap: wrap;
		}
		.lable {
			font-size: 13px;
			color: $text-gray;
			margin-bottom: 4px;
		}
		.el-input {
			min-width: 140px;
		}
		.el-input ::v-deep .el-input__inner {
			border-radius: 8px;
			border-color: $border;
			height: 38px;
			font-size: 14px;
			color: $text-dark;
			&:focus {
				border-color: $primary;
			}
		}
		.el-select {
			min-width: 140px;
		}
		.el-select ::v-deep .el-input__inner {
			border-radius: 8px;
			border-color: $border;
			height: 38px;
			font-size: 14px;
			color: $text-dark;
			&:focus {
				border-color: $primary;
			}
		}
		.el-date-editor {
			min-width: 140px;
		}
		.el-date-editor ::v-deep .el-input__inner {
			border-radius: 8px;
			border-color: $border;
			height: 38px;
			font-size: 14px;
			color: $text-dark;
			&:focus {
				border-color: $primary;
			}
		}
		.datetimerange {
			border: 1px solid $border;
			border-radius: 8px;
		}
	}

	.list-btn-box {
		display: flex;
		gap: 8px;
		flex-wrap: wrap;
		align-items: center;
	}
	.list-search-btn,
	.list-add-btn {
		background: linear-gradient(135deg, $primary, $purple);
		border: none;
		border-radius: 20px;
		padding: 0 22px;
		height: 38px;
		color: #fff;
		box-shadow: 0 2px 8px rgba(14,165,233,0.2);
		transition: all 0.3s;
		.icon {
			margin: 0 3px 0 0;
		}
		&:hover {
			box-shadow: 0 4px 16px rgba(14,165,233,0.3);
			transform: translateY(-1px);
		}
	}
	.list-static-btn {
		border-radius: 20px;
		padding: 0 20px;
		height: 38px;
	}
}

.list5 {
	display: grid;
	grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
	gap: 20px;

	.list-item {
		border-radius: $radius;
		overflow: hidden;
		cursor: pointer;

		.card-inner {
			background: $white;
			border-radius: $radius;
			box-shadow: $shadow;
			overflow: hidden;
			transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
			border: 1px solid $border;

			&:hover {
				transform: translateY(-4px);
				box-shadow: 0 12px 28px rgba(14,165,233,0.12);
				border-color: $primary;

				.card-action {
					background: linear-gradient(135deg, rgba(14,165,233,0.08), rgba(124,58,237,0.05));
					.el-icon { transform: translateX(3px); }
				}
			}
		}

		.imgbox {
			height: 200px;
			overflow: hidden;
			position: relative;
			img {
				width: 100%;
				height: 100%;
				object-fit: cover;
				display: block;
				transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
			}
			&::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 0;
				right: 0;
				height: 60px;
				background: linear-gradient(transparent, rgba(0,0,0,0.3));
				pointer-events: none;
			}
		}
		.card-inner:hover .imgbox img {
			transform: scale(1.08);
		}

		.card-badge {
			position: absolute;
			top: 12px;
			left: 12px;
			background: linear-gradient(135deg, $primary, $purple);
			color: #fff;
			font-size: 11px;
			font-weight: 600;
			padding: 4px 12px;
			border-radius: 20px;
			z-index: 2;
			letter-spacing: 1px;
			box-shadow: 0 2px 8px rgba(14,165,233,0.3);
			.el-icon { margin-right: 3px; vertical-align: middle; }
		}

		.infoBox {
			padding: 16px;

			.card-title {
				font-size: 15px;
				color: $text-dark;
				font-weight: 600;
				margin-bottom: 8px;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}

			.card-tags-row {
				display: flex;
				align-items: center;
				gap: 8px;
				margin-bottom: 12px;
				flex-wrap: wrap;
			}

			.card-tag {
				display: inline-block;
				font-size: 11px;
				color: $primary;
				background: rgba(14,165,233,0.08);
				padding: 2px 10px;
				border-radius: 10px;
			}

			.card-status {
				display: inline-flex;
				align-items: center;
				gap: 3px;
				font-size: 11px;
				font-weight: 600;
				padding: 2px 10px;
				border-radius: 10px;
				.el-icon { font-size: 12px; }
				&.done { color: #16a34a; background: rgba(34,197,94,0.08); }
				&.confirming { color: #00d4ff; background: rgba(0,212,255,0.08); }
				&.fail { color: #ef4444; background: rgba(239,68,68,0.08); }
				&.pending { color: #d97706; background: rgba(245,158,11,0.08); }
			}

			.selected-skill-info {
				display: flex; align-items: center; gap: 4px;
				font-size: 12px; color: #00d4ff; margin-bottom: 8px;
				padding: 4px 8px; border-radius: 6px;
				background: rgba(0,212,255,0.04);
				.el-icon { font-size: 13px; }
				strong { color: #e2e8f0; font-weight: 600; }
			}

			.card-confirm-actions {
				display: flex; gap: 8px; padding: 10px 16px;
				margin: 8px -16px -16px;
				background: rgba(0,212,255,0.03);
				border-top: 1px solid rgba(0,212,255,0.12);
				.confirm-btn {
					flex: 1; border: none; border-radius: 8px;
					padding: 8px 0; font-size: 13px; font-weight: 600;
					cursor: pointer; transition: all 0.2s;
					display: flex; align-items: center; justify-content: center; gap: 4px;
					.el-icon { font-size: 14px; }
					&.accept {
						background: linear-gradient(135deg, #10b981, #059669);
						color: #fff;
						box-shadow: 0 2px 8px rgba(16,185,129,0.3);
						&:hover { transform: translateY(-1px); box-shadow: 0 4px 12px rgba(16,185,129,0.4); }
					}
					&.decline {
						background: rgba(239,68,68,0.1);
						color: rgba(239,68,68,0.8);
						border: 1px solid rgba(239,68,68,0.15);
						&:hover { background: rgba(239,68,68,0.15); color: #ef4444; }
					}
				}
			}

			.bottomInfo {
				display: flex;
				justify-content: space-between;
				align-items: center;
				gap: 8px;
				padding: 10px 0;
				border-top: 1px dashed $border;

				.time_item,
				.publisher_item {
					display: flex;
					align-items: center;
					gap: 4px;
					min-width: 0;
				}
				.text {
					font-size: 12px;
					color: $text-gray;
				}
				.el-icon {
					font-size: 13px;
					color: $primary;
				}
			}

			.card-action {
				display: flex;
				align-items: center;
				justify-content: space-between;
				padding: 10px 14px;
				margin: 8px -16px -16px;
				background: $bg;
				border-top: 1px solid $border;
				transition: all 0.3s;

				.action-text {
					font-size: 13px;
					color: $primary;
					font-weight: 500;
				}
				.el-icon {
					font-size: 14px;
					color: $primary;
					transition: transform 0.3s;
				}
			}
		}
	}
}

.pagination {
	margin-top: 24px;
	display: flex;
	justify-content: center;
	::v-deep .el-pager li.active {
		background: linear-gradient(135deg, $primary, $purple) !important;
		color: $white;
	}
	::v-deep .el-pagination.is-background .el-pager li:not(.disabled).active {
		background: linear-gradient(135deg, $primary, $purple) !important;
		color: $white;
	}
}

.back_box {
	text-align: right;
	margin-bottom: 12px;
	.backBtn {
		border-radius: 20px;
		border-color: $border;
		color: $text-gray;
		&:hover {
			color: $primary;
			border-color: $primary;
		}
	}
}

.custom-modal-mask {
	position: fixed; top: 0; left: 0; right: 0; bottom: 0;
	z-index: 10000;
	background: rgba(0,0,0,0.55);
	display: flex; align-items: center; justify-content: center;
	backdrop-filter: blur(2px);
}
.custom-modal {
	background: #0f172a;
	border: 1px solid rgba(0,212,255,0.12);
	border-radius: 14px;
	box-shadow: 0 20px 60px rgba(0,0,0,0.5);
	width: 400px; max-width: 90vw;
}
.cm-header {
	display: flex; align-items: center; justify-content: space-between;
	padding: 16px 20px;
	background: rgba(0,0,0,0.3);
	border-bottom: 1px solid rgba(255,255,255,0.06);
	span { font-size: 15px; color: #e2e8f0; font-weight: 600; }
	.cm-close-icon { font-size: 18px; color: #94a3b8; cursor: pointer; &:hover { color: #00d4ff; } }
}
.cm-body {
	padding: 20px;
	background: #0b1120;
	color: #e2e8f0;
}
.cm-footer {
	display: flex; justify-content: flex-end; gap: 10px;
	padding: 14px 20px;
	background: rgba(0,0,0,0.2);
	border-top: 1px solid rgba(255,255,255,0.06);
}
.cm-btn {
	padding: 8px 20px; border-radius: 16px; font-size: 13px; font-weight: 600;
	border: none; cursor: pointer; transition: all 0.2s;
	display: flex; align-items: center; gap: 4px;
	&:disabled { opacity: 0.5; cursor: not-allowed; }
}
.cm-btn-cancel {
	background: rgba(255,255,255,0.04); color: #94a3b8;
	border: 1px solid rgba(255,255,255,0.1);
	&:hover:not(:disabled) { color: #e2e8f0; background: rgba(255,255,255,0.08); }
}
.cm-btn-danger {
	background: #ef4444; color: #fff;
	&:hover:not(:disabled) { background: #dc2626; }
}

::v-deep .el-dialog {
	border-radius: $radius;
}
::v-deep .el-table {
	border-radius: $radius;
	overflow: hidden;
	th {
		background: $bg !important;
		color: $text-dark;
		font-weight: 600;
	}
}
::v-deep .el-button--primary {
	background: linear-gradient(135deg, $primary, $purple);
	border: none;
	&:hover, &:focus {
		box-shadow: 0 4px 16px rgba(14,165,233,0.3);
	}
}
::v-deep .el-button--success {
	border-radius: 20px;
}
::v-deep .el-button--danger {
	border-radius: 20px;
}

.dark-embedded {
	.list-preview {
		padding: 0 !important;
		max-width: 100% !important;
	}
	.list-form-pv {
		background: rgba(255,255,255,0.03) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		box-shadow: none !important;
		.lable { color: #94a3b8 !important; }
	}
	.list5 .list-item .card-inner {
		background: rgba(255,255,255,0.03) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		box-shadow: none !important;
		&:hover {
			border-color: rgba(0,212,255,0.2) !important;
			background: rgba(0,212,255,0.04) !important;
			box-shadow: 0 8px 24px rgba(0,212,255,0.08) !important;
		}
	}
	.list5 .list-item .infoBox {
		background: transparent !important;
		.card-title { color: #e2e8f0 !important; }
		.card-tag {
			background: rgba(0,212,255,0.1) !important;
			color: #00d4ff !important;
		}
		.bottomInfo {
			border-top-color: rgba(255,255,255,0.06) !important;
			.text { color: #94a3b8 !important; }
			.el-icon { color: #00d4ff !important; }
		}
		.card-action {
			background: rgba(255,255,255,0.02) !important;
			border-top-color: rgba(255,255,255,0.06) !important;
			.action-text { color: #00d4ff !important; }
			.el-icon { color: #00d4ff !important; }
		}
	}
	.card-inner:hover .card-action {
		background: rgba(0,212,255,0.06) !important;
	}
	::v-deep .el-input__inner {
		background: rgba(255,255,255,0.04) !important;
		border: 1px solid rgba(255,255,255,0.08) !important;
		color: #e2e8f0 !important;
		&::placeholder { color: rgba(255,255,255,0.25) !important; }
	}
	::v-deep .el-select .el-input__inner {
		background: rgba(255,255,255,0.04) !important;
		border: 1px solid rgba(255,255,255,0.08) !important;
		color: #e2e8f0 !important;
	}
	::v-deep .el-button--primary {
		background: linear-gradient(135deg, #00d4ff, #7c3aed) !important;
		border: none !important;
	}
	::v-deep .el-button--success {
		background: linear-gradient(135deg, #22c55e, #059669) !important;
		border: none !important;
	}
	.pagination ::v-deep .el-pager li {
		background: rgba(255,255,255,0.04) !important;
		color: #94a3b8 !important;
		border: none !important;
		&.active {
			background: linear-gradient(135deg, #00d4ff, #7c3aed) !important;
			color: #fff !important;
		}
	}
	.pagination ::v-deep .btn-prev,
	.pagination ::v-deep .btn-next {
		background: rgba(255,255,255,0.04) !important;
		color: #94a3b8 !important;
	}
	.pagination ::v-deep .el-pagination__total { color: rgba(255,255,255,0.35) !important; }
	.pagination ::v-deep .el-pagination__jump { color: rgba(255,255,255,0.35) !important; }
}
</style>
