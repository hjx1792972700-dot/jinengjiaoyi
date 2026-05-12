<template>
	<div class="received-apply">
		<div class="ra-header">
			<div class="ra-title">
				<div class="title-icon"><el-icon><Bell /></el-icon></div>
				<div class="title-text">
					<h3>收到的交换申请</h3>
					<p>其他用户对你发布的技能提出的交换请求</p>
				</div>
			</div>
			<div class="ra-stats" v-if="total > 0">
				<div class="stat-chip pending" v-if="pendingCount > 0">
					<span class="stat-dot"></span>
					<span>{{pendingCount}} 条待处理</span>
				</div>
				<div class="stat-chip all">
					<span>共 {{total}} 条</span>
				</div>
			</div>
		</div>

		<div class="ra-toolbar">
			<div class="toolbar-left">
				<div class="search-box">
					<el-icon><Search /></el-icon>
					<input v-model="searchForm.xuqiubiaoti" placeholder="搜索技能名称..." @keydown.enter="loadData(1)" />
					<el-icon v-if="searchForm.xuqiubiaoti" class="clear-btn" @click="searchForm.xuqiubiaoti=''; loadData(1)"><CircleClose /></el-icon>
				</div>
			</div>
			<div class="toolbar-right">
				<div class="status-tabs">
					<div :class="['tab-item', {active: searchForm.sfsh===''}]" @click="switchStatus('')">全部</div>
					<div :class="['tab-item pending', {active: searchForm.sfsh==='待审核'}]" @click="switchStatus('待审核')">
						待处理
						<span class="tab-badge" v-if="pendingCount > 0">{{pendingCount}}</span>
					</div>
					<div :class="['tab-item confirming', {active: searchForm.sfsh==='待确认'}]" @click="switchStatus('待确认')">待对方确认</div>
					<div :class="['tab-item approved', {active: searchForm.sfsh==='已完成'}]" @click="switchStatus('已完成')">已完成</div>
					<div :class="['tab-item rejected', {active: searchForm.sfsh==='否'}]" @click="switchStatus('否')">已拒绝</div>
				</div>
			</div>
		</div>

		<div class="ra-body" v-loading="loading" element-loading-background="rgba(12,18,34,0.6)">
			<div v-if="dataList.length === 0 && !loading" class="empty-state">
				<div class="empty-icon">
					<svg viewBox="0 0 80 80" width="80" height="80">
						<rect x="10" y="18" width="60" height="44" rx="6" fill="none" stroke="currentColor" stroke-width="2" opacity="0.3"/>
						<path d="M10 30 L40 48 L70 30" fill="none" stroke="currentColor" stroke-width="2" opacity="0.4"/>
						<circle cx="40" cy="40" r="12" fill="none" stroke="currentColor" stroke-width="1.5" opacity="0.2"/>
						<path d="M36 40 L39 43 L44 37" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" opacity="0.3"/>
					</svg>
				</div>
				<p class="empty-title">暂无收到的申请</p>
				<p class="empty-desc">当其他用户对你的技能发起交换申请时，将在这里显示</p>
			</div>

			<transition-group name="card-anim" tag="div" class="card-list">
				<div class="apply-card" v-for="item in dataList" :key="item.id"
					:class="{'is-pending': item.sfsh==='待审核', 'is-confirming': item.sfsh==='待确认', 'is-approved': item.sfsh==='已完成', 'is-rejected': item.sfsh==='否'}">

					<div class="card-status-bar">
						<span class="status-indicator" v-if="item.sfsh==='待审核'">
							<span class="pulse-dot"></span>待处理
						</span>
						<span class="status-indicator confirming" v-else-if="item.sfsh==='待确认'">
							<el-icon><CircleCheck /></el-icon>待对方确认
						</span>
						<span class="status-indicator approved" v-else-if="item.sfsh==='已完成'">
							<el-icon><CircleCheck /></el-icon>已完成
						</span>
						<span class="status-indicator rejected" v-else-if="item.sfsh==='否'">
							<el-icon><CircleClose /></el-icon>已拒绝
						</span>
						<span class="card-time">
							<el-icon><Clock /></el-icon>{{item.shenqingshijian || item.addtime}}
						</span>
					</div>

					<div class="card-main">
						<div class="card-cover">
							<template v-if="item.fengmian">
								<img v-if="item.fengmian.substring(0,4)=='http'" :src="item.fengmian.split(',')[0]">
								<img v-else :src="baseUrl + item.fengmian.split(',')[0]">
							</template>
							<div class="cover-placeholder" v-else>
								<el-icon><Box /></el-icon>
							</div>
						</div>

						<div class="card-info">
							<div class="info-top">
								<h4 class="skill-title">{{item.xuqiubiaoti}}</h4>
								<div class="skill-meta">
									<span class="category-tag">{{item.jinengfenlei}}</span>
									<span class="price-tag" v-if="item.xuqiufeiyong">¥{{item.xuqiufeiyong}}</span>
									<span class="price-tag free" v-else>面议</span>
								</div>
							</div>
							<div class="info-bottom">
								<div class="applicant-row">
									<div class="applicant-avatar">
										{{(item.gongjirenxingming || '?').substring(0,1)}}
									</div>
									<div class="applicant-detail">
										<span class="applicant-name">申请人：{{item.gongjirenxingming}}</span>
										<span class="applicant-id">{{item.gongjirenxingming}}</span>
									</div>
									<div class="applicant-phone" v-if="item.lianxifangshi">
										<el-icon><Phone /></el-icon>{{item.lianxifangshi}}
									</div>
								</div>
								<div class="selected-skill-row" v-if="item.sfsh==='待确认' || item.sfsh==='已完成'">
									<el-icon><Sort /></el-icon>
									<span>你选择的对方技能：<strong>{{item.xuanzejinengbiaoti}}</strong></span>
								</div>
								<div class="remark-row" v-if="item.shenqingbeizhu">
									<el-icon><ChatDotRound /></el-icon>
									<span>{{item.shenqingbeizhu}}</span>
								</div>
								<div class="reply-row" v-if="item.shhf && item.sfsh !== '待审核'">
									<el-icon><Comment /></el-icon>
									<span>回复：{{item.shhf}}</span>
								</div>
							</div>
						</div>

						<div class="card-actions">
							<template v-if="item.sfsh==='待审核'">
							<button type="button" class="action-btn approve" @click="handleApprove(item)">
								<el-icon><Check /></el-icon>
								<span>选择技能交换</span>
							</button>
							<button type="button" class="action-btn reject" @click="handleReject(item)">
								<el-icon><Close /></el-icon>
								<span>拒绝</span>
							</button>
							</template>
							<template v-if="item.sfsh==='待确认'">
								<div class="waiting-hint">
									<el-icon class="is-loading"><Loading /></el-icon>
									<span>等待对方确认</span>
								</div>
							</template>
						</div>
					</div>
				</div>
			</transition-group>
		</div>

		<div class="ra-pagination" v-if="total > pageSize">
			<el-pagination
				background
				:page-size="pageSize"
				:total="total"
				layout="total, prev, pager, next"
				@current-change="loadData"
			></el-pagination>
		</div>

		<div class="custom-modal-mask" v-if="rejectVisible" @click.self="rejectVisible = false">
			<div class="custom-modal reject-modal">
				<div class="cm-header">
					<span>拒绝申请</span>
					<el-icon class="cm-close-icon" @click="rejectVisible = false"><Close /></el-icon>
				</div>
				<div class="cm-body">
					<div class="reject-hint">
						<p>确定拒绝 <strong>{{rejectItem && rejectItem.gongjirenxingming}}</strong> 对技能 <strong>{{rejectItem && rejectItem.xuqiubiaoti}}</strong> 的交换申请？</p>
					</div>
					<el-input type="textarea" :rows="3" v-model="rejectReason" placeholder="请填写拒绝原因（选填），对方将看到此信息"></el-input>
				</div>
				<div class="cm-footer">
					<button class="cm-btn cm-btn-cancel" @click="rejectVisible = false">取消</button>
					<button class="cm-btn cm-btn-danger" @click="submitReject" :disabled="submitLoading">
						<el-icon class="is-loading" v-if="submitLoading"><Loading /></el-icon>确认拒绝
					</button>
				</div>
			</div>
		</div>

		<div class="custom-modal-mask" v-if="selectSkillVisible" @click.self="selectSkillVisible = false">
			<div class="custom-modal select-skill-modal">
				<div class="cm-header">
					<span>选择对方技能</span>
					<el-icon class="cm-close-icon" @click="selectSkillVisible = false"><Close /></el-icon>
				</div>
				<div class="cm-body">
					<div class="select-skill-hint">
						<p><strong>{{approveItem && approveItem.gongjirenxingming}}</strong> 想交换你的技能 <strong>{{approveItem && approveItem.xuqiubiaoti}}</strong></p>
						<p class="hint-sub">请从对方发布的技能中选择一项你想要的技能作为交换：</p>
					</div>
					<div class="skill-list-loading" v-if="skillLoading">
						<el-icon class="is-loading"><Loading /></el-icon> 加载中...
					</div>
					<div class="skill-list-empty" v-if="!skillLoading && applicantSkills.length === 0">
						<el-icon><Warning /></el-icon>
						<p>对方暂未发布任何技能</p>
					</div>
					<div class="skill-select-list" v-if="!skillLoading && applicantSkills.length > 0">
						<div class="skill-option" v-for="skill in applicantSkills" :key="skill.id"
							:class="{'is-selected': selectedSkill && selectedSkill.id === skill.id}"
							@click="selectedSkill = skill">
							<div class="skill-opt-cover">
								<img v-if="skill.fengmian" :src="skill.fengmian.substr(0,4)==='http' ? skill.fengmian.split(',')[0] : baseUrl + skill.fengmian.split(',')[0]" />
								<el-icon v-else><Box /></el-icon>
							</div>
							<div class="skill-opt-info">
								<div class="skill-opt-title">{{skill.xuqiubiaoti}}</div>
								<div class="skill-opt-meta">
									<span class="skill-opt-cat">{{skill.jinengfenlei}}</span>
									<span class="skill-opt-desc" v-if="skill.xuqiumiaoshu">{{skill.xuqiumiaoshu}}</span>
								</div>
							</div>
							<div class="skill-opt-check">
								<el-icon v-if="selectedSkill && selectedSkill.id === skill.id"><CircleCheck /></el-icon>
								<el-icon v-else><CirclePlus /></el-icon>
							</div>
						</div>
					</div>
				</div>
				<div class="cm-footer">
					<button class="cm-btn cm-btn-cancel" @click="selectSkillVisible = false">取消</button>
					<button class="cm-btn cm-btn-primary" @click="submitSelectSkill" :disabled="!selectedSkill || submitLoading">
						<el-icon class="is-loading" v-if="submitLoading"><Loading /></el-icon>确认选择并提交
					</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	props: { embedded: { type: Boolean, default: false } },
	data() {
		return {
			baseUrl: '',
			dataList: [],
			total: 0,
			pendingCount: 0,
			pageSize: 10,
			loading: false,
			searchForm: { xuqiubiaoti: '', sfsh: '' },
			rejectVisible: false,
			rejectReason: '',
			rejectItem: null,
			submitLoading: false,
			selectSkillVisible: false,
			approveItem: null,
			applicantSkills: [],
			selectedSkill: null,
			skillLoading: false,
		}
	},
	created() {
		this.baseUrl = this.$config.baseUrl
		this.loadData(1)
		this.loadPendingCount()
	},
	methods: {
		switchStatus(status) {
			this.searchForm.sfsh = status
			this.loadData(1)
		},
		loadPendingCount() {
			this.$http.get('jiaohuanshenqing/receivedApply', {
				params: { page: 1, limit: 1, sfsh: '待审核' }
			}).then(({ data }) => {
				if (data && data.code === 0) this.pendingCount = data.data.total || 0
			})
		},
		loadData(page) {
			this.loading = true
			let params = { page, limit: this.pageSize, sort: 'id', order: 'desc' }
			if (this.searchForm.xuqiubiaoti) params.xuqiubiaoti = '%' + this.searchForm.xuqiubiaoti + '%'
			if (this.searchForm.sfsh) params.sfsh = this.searchForm.sfsh
			this.$http.get('jiaohuanshenqing/receivedApply', { params }).then(({ data }) => {
				if (data && data.code === 0) {
					this.dataList = data.data.list
					this.total = data.data.total
				}
				this.loading = false
			}).catch(() => { this.loading = false })
		},
		handleApprove(item) {
			this.$http.get('huiyuanchongzhi/vipStatus').then(res => {
				if (res.data.code !== 0) {
					this.$message.error('VIP状态查询失败，请稍后再试')
					return
				}
				const d = res.data.data
				if (!d.vip) {
					let user = JSON.parse(localStorage.getItem('sessionForm'))
					this.$http.get('jinengxuqiu/page', {
						params: { page: 1, limit: 1, yonghuzhanghao: user.yonghuzhanghao }
					}).then(r => {
						let hasPublished = r.data.code == 0 && r.data.data.total > 0
						let msg = (d.expired || hasPublished)
							? '您的VIP权限已到期，需要续费VIP后才能同意交换申请'
							: '只有VIP会员才可以同意交换申请，请先开通会员'
						this.$confirm(msg, '需要VIP权限', {
							confirmButtonText: '去充值',
							cancelButtonText: '取消',
							type: 'warning',
						}).then(() => {
							this.$router.push({ path: '/main/center', query: { tab: 'huiyuanchongzhi' } })
						}).catch(() => {})
					})
					return
				}
				this.approveItem = item
				this.selectedSkill = null
				this.applicantSkills = []
				this.selectSkillVisible = true
				this.skillLoading = true
				this.$http.get('jinengxuqiu/list', {
					params: { page: 1, limit: 999, yonghuzhanghao: item.gongjirenzhanghao, leixing: '技能' }
				}).then(({ data }) => {
					this.skillLoading = false
					if (data && data.code === 0) {
						this.applicantSkills = data.data.list || []
					}
				}).catch(() => { this.skillLoading = false })
			}).catch(() => {
				this.$message.error('网络异常，请稍后再试')
			})
		},
		submitSelectSkill() {
			if (!this.selectedSkill || !this.approveItem) return
			this.submitLoading = true
			this.$http.post('jiaohuanshenqing/selectSkill', {
				id: this.approveItem.id,
				xuanzejinengid: this.selectedSkill.id,
				xuanzejinengbiaoti: this.selectedSkill.xuqiubiaoti
			}).then(({ data }) => {
				this.submitLoading = false
				if (data && data.code === 0) {
					this.selectSkillVisible = false
					this.$message.success('已提交，等待对方确认')
					this.loadData(1)
					this.loadPendingCount()
					this.$emit('count-changed')
				} else {
					this.$message.error(data.msg || '操作失败')
				}
			}).catch(() => { this.submitLoading = false })
		},
		handleReject(item) {
			this.rejectItem = item
			this.rejectReason = ''
			this.rejectVisible = true
		},
		submitReject() {
			this.submitLoading = true
			this.doAudit(this.rejectItem.id, '否', this.rejectReason)
		},
		doAudit(id, sfsh, shhf) {
			this.$http.post(
				'jiaohuanshenqing/shBatch?sfsh=' + encodeURIComponent(sfsh) + '&shhf=' + encodeURIComponent(shhf),
				[id]
			).then(({ data }) => {
				this.submitLoading = false
				if (data && data.code === 0) {
					this.rejectVisible = false
					this.$message.success('操作成功')
					this.loadData(1)
					this.loadPendingCount()
					this.$emit('count-changed')
				} else {
					this.$message.error(data.msg || '操作失败')
				}
			}).catch(() => { this.submitLoading = false })
		},
	}
}
</script>

<style lang="scss" scoped>
$cyan: #00d4ff;
$purple: #7c3aed;
$green: #10b981;
$red: #ef4444;
$orange: #f59e0b;
$text1: #f1f5f9;
$text2: #cbd5e1;
$text3: #94a3b8;
$text4: #64748b;
$bg-card: rgba(30,41,59,0.5);
$bg-hover: rgba(30,41,59,0.8);
$border: rgba(255,255,255,0.06);
$border-hover: rgba(0,212,255,0.15);
$radius: 12px;

.received-apply {
	padding: 0;
	display: flex;
	flex-direction: column;
	height: 100%;
}

.ra-header {
	display: flex;
	align-items: flex-start;
	justify-content: space-between;
	margin-bottom: 20px;
	flex-wrap: wrap;
	gap: 12px;
}

.ra-title {
	display: flex;
	align-items: center;
	gap: 12px;
	.title-icon {
		width: 40px; height: 40px;
		border-radius: 10px;
		background: linear-gradient(135deg, rgba(0,212,255,0.15), rgba(124,58,237,0.15));
		display: flex; align-items: center; justify-content: center;
		.el-icon { font-size: 20px; color: $cyan; }
	}
	.title-text {
		h3 { font-size: 17px; font-weight: 700; color: $text1; margin: 0 0 2px; }
		p { font-size: 12px; color: $text4; margin: 0; }
	}
}

.ra-stats {
	display: flex; gap: 8px; align-items: center;
	.stat-chip {
		display: flex; align-items: center; gap: 6px;
		padding: 5px 12px; border-radius: 20px;
		font-size: 12px; font-weight: 500;
		&.pending {
			background: rgba(245,158,11,0.12); color: $orange;
			.stat-dot {
				width: 6px; height: 6px; border-radius: 50%;
				background: $orange;
				animation: dotPulse 2s ease-in-out infinite;
			}
		}
		&.all { background: rgba(255,255,255,0.04); color: $text3; }
	}
}
@keyframes dotPulse {
	0%, 100% { opacity: 1; transform: scale(1); }
	50% { opacity: 0.5; transform: scale(1.4); }
}

.ra-toolbar {
	display: flex; align-items: center; justify-content: space-between;
	margin-bottom: 16px; gap: 12px; flex-wrap: wrap;
}
.toolbar-left { flex-shrink: 0; }
.search-box {
	display: flex; align-items: center; gap: 8px;
	background: rgba(255,255,255,0.04);
	border: 1px solid $border;
	border-radius: 8px; padding: 0 12px; height: 34px;
	transition: border-color 0.2s;
	&:focus-within { border-color: rgba(0,212,255,0.3); }
	.el-icon { color: $text4; font-size: 14px; flex-shrink: 0; }
	input {
		flex: 1; background: none; border: none; outline: none;
		color: $text2; font-size: 13px; min-width: 140px;
		&::placeholder { color: rgba(255,255,255,0.2); }
	}
	.clear-btn { cursor: pointer; &:hover { color: $text2; } }
}
.status-tabs {
	display: flex; gap: 2px;
	background: rgba(255,255,255,0.03);
	border-radius: 8px; padding: 3px;
	border: 1px solid $border;
}
.tab-item {
	padding: 5px 14px; border-radius: 6px; font-size: 12px;
	color: $text4; cursor: pointer; transition: all 0.2s;
	display: flex; align-items: center; gap: 4px;
	white-space: nowrap; position: relative;
	&:hover { color: $text2; background: rgba(255,255,255,0.04); }
	&.active {
		color: $text1; font-weight: 600;
		background: rgba(0,212,255,0.1);
	}
	&.pending.active { background: rgba(245,158,11,0.12); color: $orange; }
	&.confirming.active { background: rgba(0,212,255,0.12); color: $cyan; }
	&.approved.active { background: rgba(16,185,129,0.12); color: $green; }
	&.rejected.active { background: rgba(239,68,68,0.12); color: $red; }
	.tab-badge {
		display: inline-flex; align-items: center; justify-content: center;
		min-width: 16px; height: 16px; padding: 0 4px;
		border-radius: 8px; background: $orange; color: #fff;
		font-size: 10px; font-weight: 700;
	}
}

.ra-body { flex: 1; overflow-y: auto; padding-right: 4px; }

.empty-state {
	display: flex; flex-direction: column; align-items: center;
	justify-content: center; padding: 60px 20px; color: $text4;
	.empty-icon { color: $text4; margin-bottom: 16px; opacity: 0.5; }
	.empty-title { font-size: 15px; font-weight: 600; color: $text3; margin: 0 0 6px; }
	.empty-desc { font-size: 12px; color: $text4; margin: 0; }
}

.card-list { display: flex; flex-direction: column; gap: 10px; }

.card-anim-enter-active, .card-anim-leave-active {
	transition: all 0.35s ease;
}
.card-anim-enter { opacity: 0; transform: translateY(12px); }
.card-anim-leave-to { opacity: 0; transform: translateX(-20px); }

.apply-card {
	background: $bg-card;
	border: 1px solid $border;
	border-radius: $radius;
	overflow: hidden;
	transition: all 0.25s;
	&:hover { border-color: $border-hover; background: $bg-hover; }
	&.is-pending { border-left: 3px solid $orange; }
	&.is-confirming { border-left: 3px solid $cyan; }
	&.is-approved { border-left: 3px solid $green; }
	&.is-rejected { border-left: 3px solid rgba(239,68,68,0.4); opacity: 0.75; }
}

.card-status-bar {
	display: flex; align-items: center; justify-content: space-between;
	padding: 8px 16px;
	background: rgba(255,255,255,0.02);
	border-bottom: 1px solid rgba(255,255,255,0.03);
}
.status-indicator {
	display: flex; align-items: center; gap: 6px;
	font-size: 12px; font-weight: 600; color: $orange;
	.pulse-dot {
		width: 7px; height: 7px; border-radius: 50%;
		background: $orange;
		box-shadow: 0 0 6px rgba(245,158,11,0.5);
		animation: dotPulse 2s ease-in-out infinite;
	}
	&.confirming { color: $cyan; .el-icon { font-size: 14px; } }
	&.approved { color: $green; .el-icon { font-size: 14px; } }
	&.rejected { color: rgba(239,68,68,0.7); .el-icon { font-size: 14px; } }
}
.card-time {
	font-size: 11px; color: $text4;
	display: flex; align-items: center; gap: 4px;
	.el-icon { font-size: 12px; }
}

.card-main {
	display: flex; align-items: stretch; gap: 14px;
	padding: 14px 16px;
}

.card-cover {
	flex-shrink: 0; width: 80px; height: 80px;
	border-radius: 8px; overflow: hidden;
	background: rgba(255,255,255,0.03);
	img {
		width: 100%; height: 100%; object-fit: cover; display: block;
		transition: transform 0.3s;
	}
	&:hover img { transform: scale(1.05); }
	.cover-placeholder {
		width: 100%; height: 100%;
		display: flex; align-items: center; justify-content: center;
		.el-icon { font-size: 28px; color: $text4; opacity: 0.4; }
	}
}

.card-info {
	flex: 1; min-width: 0;
	display: flex; flex-direction: column; justify-content: space-between;
}
.info-top {
	.skill-title {
		font-size: 15px; font-weight: 600; color: $text1; margin: 0 0 6px;
		white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
	}
	.skill-meta {
		display: flex; align-items: center; gap: 8px; flex-wrap: wrap;
	}
	.category-tag {
		font-size: 11px; color: $cyan;
		background: rgba(0,212,255,0.08);
		padding: 2px 10px; border-radius: 10px;
		border: 1px solid rgba(0,212,255,0.12);
	}
	.price-tag {
		font-size: 13px; font-weight: 700; color: $orange;
		&.free { color: $green; font-weight: 500; font-size: 12px; }
	}
}
.info-bottom { margin-top: 8px; }
.applicant-row {
	display: flex; align-items: center; gap: 8px;
}
.applicant-avatar {
	width: 28px; height: 28px; border-radius: 50%;
	background: linear-gradient(135deg, rgba(0,212,255,0.2), rgba(124,58,237,0.2));
	display: flex; align-items: center; justify-content: center;
	font-size: 12px; font-weight: 700; color: $cyan;
	flex-shrink: 0;
}
.applicant-detail {
	display: flex; align-items: baseline; gap: 4px; min-width: 0;
	.applicant-name { font-size: 13px; font-weight: 600; color: $text2; white-space: nowrap; }
	.applicant-id { font-size: 11px; color: $text4; white-space: nowrap; }
}
.applicant-phone {
	font-size: 11px; color: $text4; margin-left: auto; white-space: nowrap;
	display: flex; align-items: center; gap: 3px;
	.el-icon { font-size: 12px; }
}
.remark-row, .reply-row {
	display: flex; align-items: flex-start; gap: 5px;
	margin-top: 6px; font-size: 12px; color: $text3;
	.el-icon { flex-shrink: 0; margin-top: 2px; font-size: 12px; }
	span { line-height: 1.5; }
}
.reply-row {
	color: $text4; font-style: italic;
	background: rgba(255,255,255,0.02);
	padding: 4px 8px; border-radius: 6px;
}

.card-actions {
	flex-shrink: 0;
	display: flex; flex-direction: column;
	align-items: stretch; justify-content: center;
	gap: 6px; min-width: 110px; padding-left: 10px;
}
.action-btn {
	display: flex; align-items: center; justify-content: center;
	gap: 5px; border: none; border-radius: 8px;
	padding: 8px 16px; font-size: 13px; font-weight: 600;
	cursor: pointer; transition: all 0.2s;
	.el-icon { font-size: 14px; }
	&.approve {
		background: linear-gradient(135deg, #10b981, #059669);
		color: #fff;
		box-shadow: 0 2px 8px rgba(16,185,129,0.3);
		&:hover { transform: translateY(-1px); box-shadow: 0 4px 12px rgba(16,185,129,0.4); }
		&:active { transform: translateY(0); }
	}
	&.reject {
		background: rgba(239,68,68,0.1);
		color: rgba(239,68,68,0.8);
		border: 1px solid rgba(239,68,68,0.15);
		&:hover { background: rgba(239,68,68,0.15); color: $red; }
	}
}

.ra-pagination {
	margin-top: 16px; padding: 4px 0;
	display: flex; justify-content: center;
}

.selected-skill-row {
	display: flex; align-items: center; gap: 5px;
	margin-top: 6px; font-size: 12px; color: $cyan;
	background: rgba(0,212,255,0.04);
	padding: 4px 8px; border-radius: 6px;
	.el-icon { font-size: 13px; }
	strong { color: $text1; font-weight: 600; }
}
.waiting-hint {
	display: flex; align-items: center; gap: 6px;
	font-size: 12px; color: $cyan; font-weight: 500;
	padding: 6px 14px; border-radius: 8px;
	background: rgba(0,212,255,0.06);
	border: 1px solid rgba(0,212,255,0.12);
	.el-icon { font-size: 14px; }
}

::v-deep .el-pagination {
	.btn-prev, .btn-next, .el-pager li {
		background: rgba(255,255,255,0.04) !important;
		color: $text3 !important;
		border: 1px solid $border;
		&.active { background: rgba(0,212,255,0.12) !important; color: $cyan !important; border-color: rgba(0,212,255,0.2); }
		&:hover { color: $cyan !important; }
	}
	.el-pagination__total { color: $text4; font-size: 12px; }
}
</style>

<style lang="scss" scoped>
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
	max-height: 80vh;
	display: flex; flex-direction: column;
	&.reject-modal { width: 440px; max-width: 90vw; }
	&.select-skill-modal { width: 600px; max-width: 90vw; }
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
	flex: 1;
	overflow-y: auto;
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
	.el-icon { font-size: 12px; }
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
.cm-btn-primary {
	background: linear-gradient(135deg, #00d4ff, #7c3aed); color: #fff;
	&:hover:not(:disabled) { box-shadow: 0 4px 14px rgba(0,212,255,0.3); }
}

.reject-hint {
	margin-bottom: 12px; font-size: 13px; color: #94a3b8; line-height: 1.6;
	strong { color: #f1f5f9; }
}
::v-deep .reject-modal .el-textarea__inner {
	background: rgba(255,255,255,0.04) !important;
	border: 1px solid rgba(255,255,255,0.08) !important;
	color: #e2e8f0 !important;
	border-radius: 8px !important;
	&::placeholder { color: rgba(255,255,255,0.25) !important; }
}

.select-skill-hint {
	margin-bottom: 16px;
	p { font-size: 14px; color: #cbd5e1; margin: 0 0 4px; line-height: 1.6;
		strong { color: #f1f5f9; }
	}
	.hint-sub { font-size: 12px; color: #94a3b8; }
}
.skill-list-loading, .skill-list-empty {
	text-align: center; padding: 30px; color: #64748b; font-size: 14px;
	.el-icon { font-size: 24px; display: block; margin-bottom: 8px; }
}
.skill-select-list {
	max-height: 340px; overflow-y: auto;
	display: flex; flex-direction: column; gap: 8px;
	padding-right: 4px;
	&::-webkit-scrollbar { width: 4px; }
	&::-webkit-scrollbar-thumb { background: rgba(0,212,255,0.15); border-radius: 4px; }
}
.skill-option {
	display: flex; align-items: center; gap: 12px;
	padding: 10px 12px; border-radius: 10px;
	background: rgba(255,255,255,0.03);
	border: 1px solid rgba(255,255,255,0.06);
	cursor: pointer; transition: all 0.2s;
	&:hover { border-color: rgba(0,212,255,0.2); background: rgba(0,212,255,0.03); }
	&.is-selected {
		border-color: #00d4ff; background: rgba(0,212,255,0.06);
		box-shadow: 0 0 12px rgba(0,212,255,0.1);
	}
}
.skill-opt-cover {
	width: 56px; height: 56px; border-radius: 8px; overflow: hidden; flex-shrink: 0;
	background: rgba(255,255,255,0.03);
	img { width: 100%; height: 100%; object-fit: cover; display: block; }
	.el-icon { font-size: 22px; color: #64748b; display: flex; width: 100%; height: 100%; align-items: center; justify-content: center; }
}
.skill-opt-info {
	flex: 1; min-width: 0;
	.skill-opt-title { font-size: 14px; font-weight: 600; color: #f1f5f9; margin-bottom: 4px;
		overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
	.skill-opt-meta { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
	.skill-opt-cat { font-size: 11px; color: #00d4ff; background: rgba(0,212,255,0.08);
		padding: 1px 8px; border-radius: 8px; }
	.skill-opt-desc { font-size: 12px; color: #64748b; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 200px; }
}
.skill-opt-check {
	flex-shrink: 0; font-size: 22px; color: #64748b;
}
.skill-option.is-selected .skill-opt-check { color: #00d4ff; }
</style>
