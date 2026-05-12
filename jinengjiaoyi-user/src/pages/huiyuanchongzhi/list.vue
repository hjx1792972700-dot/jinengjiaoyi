<template>
	<div :class="{'dark-embedded': embedded}">
		<div class="breadcrumb-preview" v-if="!embedded">
			<el-breadcrumb :separator="'/'">
				<el-breadcrumb-item class="item1" to="/main/home"><a>首页</a></el-breadcrumb-item>
				<el-breadcrumb-item class="item2"><a>会员中心</a></el-breadcrumb-item>
			</el-breadcrumb>
		</div>

		<div class="vip-center">
			<!-- ======= 已是VIP ======= -->
			<template v-if="isVip && step === 0">
				<div class="vip-status-banner gold">
					<div class="banner-decor"></div>
					<div class="banner-left">
						<div class="vip-badge-icon"><i class="el-icon-user-solid"></i></div>
						<div class="banner-info">
							<div class="banner-name">VIP会员<span class="plan-label" v-if="vipInfo.taocan">{{vipInfo.taocan}}</span></div>
							<div class="banner-expire" v-if="vipInfo.permanent">永久会员 · 永不过期</div>
							<div class="banner-expire" v-else>
								{{vipInfo.expireDate}} 到期
								<span class="days-tag" :class="{'warn': vipInfo.remainDays <= 7}">剩余{{vipInfo.remainDays}}天</span>
							</div>
						</div>
					</div>
				</div>

				<div class="auto-renew-notice" v-if="!vipInfo.permanent">
					<i class="el-icon-info"></i>
					<span>会员未过期，无需手动续费。待会员到期时系统将自动扣费完成续费。</span>
				</div>

				<div class="perk-section">
					<div class="section-title">我的特权</div>
					<div class="perk-grid">
						<div class="perk-item" v-for="p in perks" :key="p.name">
							<div class="perk-ic" :class="p.cls"><i :class="'el-icon-' + p.icon"></i></div>
							<div class="perk-name">{{p.name}}</div>
							<div class="perk-desc">{{p.desc}}</div>
						</div>
					</div>
				</div>

				<div class="record-section" v-if="dataList.length">
					<div class="section-title">充值记录</div>
					<div class="record-list">
						<div class="record-item" v-for="(item, i) in dataList" :key="i">
							<div class="rec-icon"><i class="el-icon-wallet"></i></div>
							<div class="rec-body">
								<div class="rec-title">{{item.taocan || 'VIP会员'}}</div>
								<div class="rec-time">{{item.addtime ? item.addtime.split(' ')[0] : ''}}</div>
							</div>
							<div class="rec-amount">-¥{{item.chongzhijine}}</div>
						</div>
					</div>
				</div>
			</template>

			<!-- ======= VIP过期 ======= -->
			<template v-if="vipExpired && !isVip && step === 0">
				<div class="vip-status-banner expired">
					<div class="banner-decor"></div>
					<div class="banner-left">
						<div class="vip-badge-icon grey"><i class="el-icon-user-solid"></i></div>
						<div class="banner-info">
							<div class="banner-name">VIP已过期</div>
							<div class="banner-expire">续费后即刻恢复全部特权</div>
						</div>
					</div>
				</div>
			</template>

			<!-- ======= 开通/续费页 ======= -->
			<template v-if="!isVip && step === 0">
				<div class="vip-status-banner normal" v-if="!vipExpired">
					<div class="banner-decor"></div>
					<div class="banner-left">
						<div class="vip-badge-icon"><i class="el-icon-user-solid"></i></div>
						<div class="banner-info">
							<div class="banner-name">开通VIP会员</div>
							<div class="banner-expire">解锁技能发布与优先推荐</div>
						</div>
					</div>
				</div>

				<div class="plan-section">
					<div class="plan-tabs">
						<div class="plan-tab" v-for="p in plans" :key="p.id"
							:class="{ active: selectedPlan === p.id }"
							@click="selectedPlan = p.id">
							<div class="tab-badge" v-if="p.badge">{{p.badge}}</div>
							<div class="tab-name">{{p.name}}</div>
							<div class="tab-price">
								<span class="tp-yen">¥</span><span class="tp-num">{{p.price}}</span>
							</div>
							<div class="tab-original" v-if="p.original">¥{{p.original}}</div>
							<div class="tab-avg" v-if="p.avg">≈¥{{p.avg}}/月</div>
						</div>
					</div>
				</div>

				<div class="pay-section">
					<div class="section-title">支付方式</div>
					<div class="pay-method-list">
						<div class="pay-method-row" :class="{active: payMethod==='alipay'}" @click="payMethod='alipay'">
							<div class="pm-icon alipay-bg"><i class="el-icon-coin"></i></div>
							<span class="pm-label">支付宝</span>
							<div class="pm-check">
								<el-icon v-if="payMethod==='alipay'"><Check /></el-icon>
							</div>
						</div>
						<div class="pay-method-row" :class="{active: payMethod==='wechat'}" @click="payMethod='wechat'">
							<div class="pm-icon wechat-bg"><i class="el-icon-chat-dot-round"></i></div>
							<span class="pm-label">微信支付</span>
							<div class="pm-check">
								<el-icon v-if="payMethod==='wechat'"><Check /></el-icon>
							</div>
						</div>
					</div>
				</div>

				<div class="perk-section compact">
					<div class="section-title">会员特权</div>
					<div class="perk-row-inline">
						<div class="perk-inline" v-for="p in perks" :key="p.name">
							<el-icon class="perk-inline-ic"><component :is="p.icon" /></el-icon>
							<span>{{p.name}}</span>
						</div>
					</div>
				</div>

				<div class="agreement-row">
					<el-checkbox v-model="agreed" class="agree-ck" disabled></el-checkbox>
					<span class="agree-text">
						已阅读并同意
						<a @click.stop="noticeType = 'recharge'; showNotice = true" :class="{'read-done': rechargeRead}">《充值服务协议》</a>
						<a @click.stop="noticeType = 'autoRenew'; showNotice = true" :class="{'read-done': autoRenewRead}">《自动续费协议》</a>
					</span>
				</div>

				<div class="pay-bottom">
					<div class="pay-total">
						<span class="pt-label">合计：</span>
						<span class="pt-yen">¥</span>
						<span class="pt-amount">{{currentPlan.price}}</span>
						<span class="pt-expire" v-if="expectedExpireDesc">有效期至 {{expectedExpireDesc}}</span>
					</div>
					<el-button class="pay-now-btn" :disabled="!agreed" :loading="creating" @click="createOrder">
						{{vipExpired ? '立即续费' : '立即开通'}}
					</el-button>
				</div>

				<!-- 充值须知 -->
				<div class="notice-section">
					<div class="notice-header" @click="noticeOpen = !noticeOpen">
						<span>充值须知</span>
						<i :class="noticeOpen ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
					</div>
					<div class="notice-body" v-show="noticeOpen">
						<p>1. 会员服务开通后立即生效，有效期内享受全部VIP特权。</p>
						<p>2. 不同套餐有效期不同：连续包月（1个自然月）、连续包季（3个自然月）、年度会员（1年）、永久会员（永不过期）。</p>
						<p>3. 会员到期后将自动失去VIP特权，已发布的技能不受影响，但无法新增发布。</p>
						<p>4. 会员到期时系统将自动扣费完成续费，无需手动操作。永久会员无需续费。</p>
						<p>5. 虚拟服务一经开通概不退款，请确认后再进行支付。</p>
						<p>6. 如有疑问，请联系客服或在帮助中心查看详细说明。</p>
					</div>
				</div>

				<div class="notice-section" style="margin-top: 0;">
					<div class="notice-header" @click="autoRenewOpen = !autoRenewOpen">
						<span>自动续费说明</span>
						<i :class="autoRenewOpen ? 'el-icon-arrow-up' : 'el-icon-arrow-down'"></i>
					</div>
					<div class="notice-body" v-show="autoRenewOpen">
						<p>1. 开通连续包月/包季套餐表示您同意授权平台在到期前自动扣款续费。</p>
						<p>2. 自动续费将在到期前24小时内尝试扣款，届时会有消息通知。</p>
						<p>3. 如需关闭自动续费，请在会员到期前至少24小时在个人中心-设置中取消。</p>
						<p>4. 取消自动续费后，已享有的会员权益在当前周期内依然有效。</p>
					</div>
				</div>
			</template>

			<!-- ======= 扫码支付 ======= -->
			<template v-if="step === 1">
				<div class="scan-page">
					<div class="scan-nav">
						<span class="nav-back" @click="cancelPay"><i class="el-icon-arrow-left"></i> 返回</span>
						<span class="nav-title">扫码支付</span>
					</div>

					<div class="scan-card">
						<div class="scan-method-bar" :class="payMethod">
							<i :class="payMethod==='alipay' ? 'el-icon-coin' : 'el-icon-chat-dot-round'"></i>
							<span>{{payMethod==='alipay'?'支付宝':'微信'}}扫码支付</span>
						</div>

						<div class="scan-price-display">
							<span class="spd-yen">¥</span>
							<span class="spd-amount">{{currentPlan.price}}</span>
							<span class="spd-decimal">.00</span>
						</div>

						<div class="qr-wrapper">
							<div class="qr-border">
								<img v-if="qrImgUrl" :src="qrImgUrl" class="qr-img" />
								<div v-else class="qr-loading"><el-icon class="is-loading"><Loading /></el-icon></div>
							</div>
						</div>

						<div class="scan-hint">
							打开 <b>{{payMethod==='alipay'?'支付宝':'微信'}}</b> 扫一扫，扫描上方二维码完成支付
						</div>

						<div class="scan-meta">
							<span>订单号：{{orderNo}}</span>
							<span>·</span>
							<span>{{currentPlan.name}}</span>
						</div>
					</div>

					<div class="scan-btns">
						<el-button class="btn-done" @click="confirmPayment" :loading="confirming">
							<el-icon v-if="!confirming"><CircleCheck /></el-icon> 我已完成支付
						</el-button>
						<el-button class="btn-trouble" @click="cancelPay">支付遇到问题？</el-button>
					</div>
				</div>
			</template>

			<!-- ======= 支付成功 ======= -->
			<template v-if="step === 2">
				<div class="success-page">
					<div class="success-icon-wrap">
						<div class="success-circle"><i class="el-icon-check"></i></div>
					</div>
					<h3 class="success-title">支付成功</h3>
					<p class="success-sub">恭喜您成为VIP会员，所有特权已生效！</p>

					<div class="success-info-card">
						<div class="si-row"><span>订单编号</span><span>{{orderNo}}</span></div>
						<div class="si-row"><span>套餐类型</span><span>{{currentPlan.name}}</span></div>
						<div class="si-row"><span>支付金额</span><span class="si-gold">¥{{currentPlan.price}}.00</span></div>
						<div class="si-row"><span>支付方式</span><span>{{payMethod==='alipay'?'支付宝':'微信支付'}}</span></div>
						<div class="si-row"><span>会员有效期至</span><span class="si-gold">{{currentPlan.id === 'forever' ? '永久有效' : newExpireDate}}</span></div>
					</div>

					<el-button class="finish-btn" @click="finishPayment">完成</el-button>
				</div>
			</template>

			<!-- 协议弹窗 -->
			<div class="notice-modal" v-if="showNotice" @click.self="showNotice = false">
				<div class="notice-modal-content">
					<div class="nm-header">
						<span>{{noticeType === 'autoRenew' ? '自动续费协议' : '充值服务协议'}}</span>
						<i class="el-icon-close nm-close" @click="showNotice = false"></i>
					</div>
					<div class="nm-body" v-if="noticeType === 'recharge'">
						<h4>一、服务内容</h4>
						<p>技能交易平台VIP会员服务（以下简称"本服务"）是指用户通过支付相应费用后，获得平台提供的特权功能，包括但不限于：技能发布权限、优先推荐展示、VIP身份标识、智能匹配等增值服务。</p>
						<h4>二、服务期限</h4>
						<p>会员服务自支付成功后立即生效。连续包月有效期为1个自然月，连续包季有效期为3个自然月，年度会员有效期为1年，永久会员永不过期。续费时有效期在原基础上累加。</p>
						<h4>三、费用说明</h4>
						<p>会员费用以页面显示价格为准。选择连续包月/包季的用户，平台将在到期前自动续费，届时会通知用户。用户可随时在个人中心取消自动续费。</p>
						<h4>四、退款政策</h4>
						<p>VIP会员属于虚拟服务产品，一经开通不支持退款。请用户在充值前仔细确认所选套餐。</p>
						<h4>五、使用规范</h4>
						<p>用户应合法合规使用VIP特权，不得利用特权发布违法违规、虚假、侵权等内容。平台有权对违规用户进行处罚，包括但不限于取消VIP资格且不予退款。</p>
						<h4>六、免责声明</h4>
						<p>平台将尽力保障VIP服务的稳定运行，但因不可抗力或技术原因导致服务中断时，平台将尽快恢复，并视情况为受影响用户延长会员时间。</p>
					</div>
					<div class="nm-body" v-if="noticeType === 'autoRenew'">
						<h4>一、自动续费服务</h4>
						<p>自动续费是指您授权平台在会员服务到期前，自动从您的支付账户中扣除下一个计费周期的费用，以延续会员服务。开通连续包月或连续包季套餐即表示您同意开启自动续费。</p>
						<h4>二、扣费规则</h4>
						<p>自动续费将在当前会员服务到期前24小时内尝试扣款。扣费金额以您开通时所选套餐的当前价格为准。若扣款失败，平台将在到期后再次尝试，连续3次失败后将停止自动续费。</p>
						<h4>三、续费通知</h4>
						<p>平台将在自动续费扣款前通过站内消息通知您。请确保您的支付账户余额充足，以免续费失败导致会员权益中断。</p>
						<h4>四、取消方式</h4>
						<p>您可以随时在个人中心的会员管理页面取消自动续费功能。取消后，当前计费周期内的会员权益不受影响，到期后将不再自动扣款。请在到期前至少24小时完成取消操作。</p>
						<h4>五、退款说明</h4>
						<p>自动续费成功后，当期费用不支持退款。如因系统原因导致多扣或误扣，请联系客服处理，平台将在核实后7个工作日内原路退回。</p>
						<h4>六、协议变更</h4>
						<p>平台有权根据业务调整修改自动续费价格或规则，修改前将提前通知用户。若您不同意变更，可在变更生效前取消自动续费。继续使用则视为同意变更后的条款。</p>
					</div>
					<div class="nm-footer">
						<el-button class="nm-btn" @click="confirmRead">我已阅读并同意</el-button>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
export default {
	components: {
	},
	props: { embedded: { type: Boolean, default: false } },
	data() {
		return {
			isVip: false,
			vipExpired: false,
			vipInfo: { remainDays: 0, expireDate: '', taocan: '', permanent: false },
			step: 0,
			selectedPlan: 'year',
			payMethod: 'alipay',
			agreed: false,
			creating: false,
			confirming: false,
			orderNo: '',
			orderId: null,
			qrImgUrl: '',
			pollTimer: null,
			dataList: [],
			centerType: false,
			showNotice: false,
			noticeType: 'recharge',
			rechargeRead: false,
			autoRenewRead: false,
			noticeOpen: false,
			autoRenewOpen: false,
			newExpireDate: '',
			plans: [
				{ id: 'month', name: '连续包月', price: 15, unit: '月', badge: '', hot: false, original: 30, avg: null },
				{ id: 'quarter', name: '连续包季', price: 40, unit: '季', badge: '省50元', hot: false, original: 90, avg: '13.3' },
				{ id: 'year', name: '年度会员', price: 148, unit: '年', badge: '推荐', hot: true, original: 360, avg: '12.3' },
				{ id: 'forever', name: '永久会员', price: 298, unit: '', badge: '最划算', hot: false, original: 599, avg: null },
			],
			perks: [
				{ name: '发布技能', icon: 'EditPen', cls: 'bg-blue', desc: '发布个人技能到市场' },
				{ name: '优先推荐', icon: 'Promotion', cls: 'bg-purple', desc: '技能在首页优先展示' },
				{ name: 'VIP标识', icon: 'Medal', cls: 'bg-amber', desc: '专属VIP身份标识' },
				{ name: '智能匹配', icon: 'Opportunity', cls: 'bg-green', desc: 'AI智能匹配合适技能' },
			],
		}
	},
	computed: {
		currentPlan() {
			return this.plans.find(p => p.id === this.selectedPlan) || this.plans[2];
		},
		expectedExpireDesc() {
			const plan = this.currentPlan;
			if (!plan) return '';
			if (plan.id === 'forever') return '永久有效';
			const d = new Date();
			if (plan.id === 'month') d.setMonth(d.getMonth() + 1);
			else if (plan.id === 'quarter') d.setMonth(d.getMonth() + 3);
			else if (plan.id === 'year') d.setFullYear(d.getFullYear() + 1);
			const mm = String(d.getMonth() + 1).padStart(2, '0');
			const dd = String(d.getDate()).padStart(2, '0');
			return d.getFullYear() + '-' + mm + '-' + dd;
		}
	},
	async created() {
		if(this.embedded || (this.$route.query.centerType && this.$route.query.centerType != 0)){
			this.centerType = true;
		}
		await this.checkVipStatus();
		this.loadRecords();
	},
	beforeDestroy() {
		if (this.pollTimer) clearInterval(this.pollTimer);
	},
	methods: {
		confirmRead() {
			if (this.noticeType === 'recharge') {
				this.rechargeRead = true;
			} else {
				this.autoRenewRead = true;
			}
			this.showNotice = false;
			if (this.rechargeRead && this.autoRenewRead) {
				this.agreed = true;
			}
		},
		async checkVipStatus() {
			try {
				const res = await this.$http.get('huiyuanchongzhi/vipStatus');
				if (res.data.code === 0) {
					const d = res.data.data;
					this.isVip = d.vip;
					this.vipExpired = d.expired || false;
					this.vipInfo = {
						remainDays: d.remainDays || 0,
						expireDate: d.expireDate || '',
						taocan: d.taocan || '',
						permanent: d.permanent || false
					};
				}
			} catch(e) {}
		},
		loadRecords() {
			this.$http.get('huiyuanchongzhi/page', {
				params: { page: 1, limit: 20, sort: 'id', order: 'desc' }
			}).then(res => {
				if (res.data.code === 0) {
					this.dataList = (res.data.data.list || []).filter(i => i.zhuangtai === '已支付');
				}
			});
		},
		async createOrder() {
			if (!this.agreed) {
				this.$message.warning('请先阅读并同意充值服务协议');
				return;
			}
			this.creating = true;
			try {
				const res = await this.$http.post('huiyuanchongzhi/createOrder', {
					amount: this.currentPlan.price,
					taocan: this.currentPlan.name,
					payMethod: this.payMethod === 'alipay' ? '支付宝' : '微信支付'
				});
				if (res.data.code === 0) {
					const d = res.data.data;
					this.orderNo = d.orderNo;
					this.orderId = d.orderId;
					this.qrImgUrl = 'https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=' + encodeURIComponent(d.payUrl);
					this.step = 1;
					this.startPolling();
				} else {
					this.$message.error(res.data.msg || '创建订单失败');
				}
			} catch(e) {
				this.$message.error('网络错误，请重试');
			}
			this.creating = false;
		},
		startPolling() {
			if (this.pollTimer) clearInterval(this.pollTimer);
			this.pollTimer = setInterval(async () => {
				try {
					const res = await this.$http.get('huiyuanchongzhi/queryOrder', { params: { orderNo: this.orderNo } });
					if (res.data.code === 0 && res.data.data.status === '已支付') {
						clearInterval(this.pollTimer);
						this.isVip = true;
						this.step = 2;
						this.loadRecords();
					}
				} catch(e) {}
			}, 3000);
		},
		async confirmPayment() {
			this.confirming = true;
			try {
				const res = await this.$http.post('huiyuanchongzhi/payCallback', { orderNo: this.orderNo });
				if (res.data.code === 0) {
					if (this.pollTimer) clearInterval(this.pollTimer);
					this.isVip = true;
					this.newExpireDate = (res.data.data && res.data.data.expireDate) || '';
					this.step = 2;
					this.loadRecords();
				} else {
					this.$message.error(res.data.msg || '支付确认失败');
				}
			} catch(e) {
				this.$message.error('网络错误');
			}
			this.confirming = false;
		},
		cancelPay() {
			if (this.pollTimer) clearInterval(this.pollTimer);
			this.step = 0;
		},
		async finishPayment() {
			await this.checkVipStatus();
			this.vipExpired = false;
			this.newExpireDate = '';
			this.step = 0;
		}
	}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
$cyan: #00d4ff;
$purple: #7c3aed;
$gold: #f59e0b;
$gold-light: #fbbf24;
$green: #22c55e;
$blue: #0ea5e9;
$red: #ef4444;

.vip-center { max-width: 100%; padding: 0; }

/* ===================== 顶部状态条 ===================== */
.vip-status-banner {
	display: flex; align-items: center; justify-content: space-between;
	padding: 16px 20px; border-radius: 12px; position: relative; overflow: hidden; margin-bottom: 16px;
	.banner-decor {
		position: absolute; top: -60%; right: -5%; width: 160px; height: 160px;
		border-radius: 50%; pointer-events: none;
	}
	.banner-left { display: flex; align-items: center; gap: 14px; position: relative; z-index: 1; }
	.banner-info { .banner-name { font-size: 16px; font-weight: 700; } .banner-expire { font-size: 12px; opacity: 0.7; margin-top: 2px; } }
	&.gold {
		background: linear-gradient(135deg, #78350f, #92400e);
		.banner-decor { background: radial-gradient(circle, rgba($gold-light, 0.15), transparent 70%); }
		.banner-name { color: $gold-light; }
		.banner-expire { color: #d4a574; }
	}
	&.expired {
		background: linear-gradient(135deg, #44403c, #57534e);
		.banner-decor { background: radial-gradient(circle, rgba(255,255,255,0.04), transparent 70%); }
		.banner-name { color: #d6d3d1; }
		.banner-expire { color: #a8a29e; }
	}
	&.normal {
		background: linear-gradient(135deg, #1e293b, #334155);
		.banner-decor { background: radial-gradient(circle, rgba($cyan, 0.1), transparent 70%); }
		.banner-name { color: #e2e8f0; }
		.banner-expire { color: #94a3b8; }
	}
}
.vip-badge-icon {
	width: 42px; height: 42px; border-radius: 50%;
	background: linear-gradient(135deg, $gold, #d97706);
	display: flex; align-items: center; justify-content: center;
	i, :deep(.el-icon) { font-size: 22px; color: #fff; }
	&.grey { background: linear-gradient(135deg, #78716c, #a8a29e); }
}
.plan-label {
	display: inline-block; padding: 1px 8px; border-radius: 10px; font-size: 10px; margin-left: 8px;
	background: rgba($gold-light, 0.15); color: $gold-light; font-weight: 500; vertical-align: middle;
}
.days-tag {
	display: inline-block; padding: 1px 8px; border-radius: 10px; font-size: 10px; margin-left: 6px;
	background: rgba($green, 0.15); color: $green;
	&.warn { background: rgba($red, 0.15); color: #f87171; }
}
.renew-btn-sm {
	border-radius: 16px !important; border: none !important; font-size: 12px !important; font-weight: 600 !important;
	color: #78350f !important; padding: 0 18px !important; height: 30px !important;
	background: linear-gradient(135deg, $gold-light, $gold) !important;
}
.renew-alert {
	display: flex; align-items: center; gap: 8px;
	padding: 10px 16px; border-radius: 8px; margin-bottom: 14px;
	background: rgba($red, 0.06); border: 1px solid rgba($red, 0.1);
	font-size: 12px; color: #a8a29e;
	i { color: #f87171; font-size: 14px; }
	a { color: $gold-light; cursor: pointer; margin-left: auto; white-space: nowrap; font-weight: 500; }
}
.auto-renew-notice {
	display: flex; align-items: center; gap: 8px;
	padding: 10px 16px; border-radius: 8px; margin-bottom: 14px;
	background: rgba($green, 0.06); border: 1px solid rgba($green, 0.12);
	font-size: 12px; color: #a8a29e;
	i, :deep(.el-icon) { color: $green; font-size: 14px; }
}

/* ===================== 套餐选择 - QQ音乐风格 ===================== */
.plan-section { margin-bottom: 16px; }
.plan-tabs {
	display: grid; grid-template-columns: repeat(4, 1fr); gap: 8px;
}
.plan-tab {
	position: relative; padding: 14px 6px 10px; border-radius: 10px; text-align: center;
	cursor: pointer; transition: all 0.2s;
	background: rgba(255,255,255,0.03); border: 2px solid rgba(255,255,255,0.06);
	&:hover { border-color: rgba($gold, 0.2); background: rgba($gold, 0.02); }
	&.active {
		border-color: $gold; background: rgba($gold, 0.06);
		.tab-name { color: $gold-light; }
	}
}
.tab-badge {
	position: absolute; top: -1px; left: 50%; transform: translateX(-50%);
	background: linear-gradient(135deg, $gold, #d97706); color: #fff;
	font-size: 9px; font-weight: 700; padding: 1px 8px; border-radius: 0 0 6px 6px;
	white-space: nowrap;
}
.tab-name { font-size: 12px; color: #cbd5e1; font-weight: 600; margin-bottom: 6px; margin-top: 4px; }
.tab-price {
	margin-bottom: 2px;
	.tp-yen { font-size: 12px; color: $gold-light; font-weight: 700; vertical-align: super; }
	.tp-num { font-size: 26px; color: $gold-light; font-weight: 800; line-height: 1; }
}
.tab-original { font-size: 10px; color: #64748b; text-decoration: line-through; }
.tab-avg { font-size: 10px; color: $cyan; margin-top: 2px; }

/* ===================== 支付方式 ===================== */
.pay-section { margin-bottom: 12px; }
.section-title {
	font-size: 13px; color: #94a3b8; font-weight: 600; margin-bottom: 10px;
	letter-spacing: 0.5px;
}
.pay-method-list { display: flex; flex-direction: column; gap: 0; }
.pay-method-row {
	display: flex; align-items: center; gap: 12px;
	padding: 12px 14px; cursor: pointer; transition: all 0.15s;
	border-bottom: 1px solid rgba(255,255,255,0.04);
	&:last-child { border-bottom: none; }
	&:hover { background: rgba(255,255,255,0.02); }
	&.active { background: rgba($cyan, 0.03); }
}
.pm-icon {
	width: 30px; height: 30px; border-radius: 6px; flex-shrink: 0;
	display: flex; align-items: center; justify-content: center;
	i, :deep(.el-icon) { font-size: 16px; color: #fff; }
	&.alipay-bg { background: linear-gradient(135deg, #1677ff, #4096ff); }
	&.wechat-bg { background: linear-gradient(135deg, #07c160, #2aae67); }
}
.pm-label { flex: 1; font-size: 13px; color: #e2e8f0; font-weight: 500; }
.pm-check {
	width: 20px; height: 20px; border-radius: 50%; flex-shrink: 0;
	border: 2px solid rgba(255,255,255,0.12); display: flex; align-items: center; justify-content: center;
	i, :deep(.el-icon) { font-size: 12px; color: $cyan; }
}
.pay-method-row.active .pm-check { border-color: $cyan; background: rgba($cyan, 0.1); }

/* ===================== 特权（开通页紧凑版） ===================== */
.perk-section { margin-bottom: 12px; }
.perk-section.compact { margin-bottom: 8px; }
.perk-grid {
	display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px;
}
.perk-item {
	text-align: center; padding: 14px 6px; border-radius: 10px;
	background: rgba(255,255,255,0.02); border: 1px solid rgba(255,255,255,0.04);
}
.perk-ic {
	width: 36px; height: 36px; border-radius: 10px; margin: 0 auto 8px;
	display: flex; align-items: center; justify-content: center;
	i, :deep(.el-icon) { font-size: 18px; color: #fff; }
	&.bg-blue { background: linear-gradient(135deg, $blue, #38bdf8); }
	&.bg-purple { background: linear-gradient(135deg, $purple, #a78bfa); }
	&.bg-amber { background: linear-gradient(135deg, $gold, $gold-light); }
	&.bg-green { background: linear-gradient(135deg, $green, #4ade80); }
}
.perk-name { font-size: 12px; color: #e2e8f0; font-weight: 600; margin-bottom: 2px; }
.perk-desc { font-size: 10px; color: #64748b; }

.perk-row-inline { display: flex; flex-wrap: wrap; gap: 8px; }
.perk-inline {
	display: flex; align-items: center; gap: 6px;
	padding: 6px 12px; border-radius: 6px;
	background: rgba(255,255,255,0.02); border: 1px solid rgba(255,255,255,0.04);
	span { font-size: 11px; color: #cbd5e1; }
}
.perk-inline-ic { font-size: 13px; color: $gold-light; }

/* ===================== 协议勾选 ===================== */
.agreement-row {
	display: flex; align-items: center; gap: 6px; margin: 10px 0;
}
.agree-ck ::v-deep .el-checkbox__inner {
	background: transparent !important; border-color: rgba(255,255,255,0.2) !important;
	width: 14px; height: 14px;
}
.agree-ck.is-checked ::v-deep .el-checkbox__inner {
	background: $cyan !important; border-color: $cyan !important;
}
.agree-text {
	font-size: 11px; color: #64748b;
	a { color: $cyan; cursor: pointer; &:hover { text-decoration: underline; } }
	a.read-done { color: #22c55e; }
}

/* ===================== 底部支付栏 ===================== */
.pay-bottom {
	display: flex; justify-content: space-between; align-items: center;
	padding: 14px 18px; border-radius: 10px; margin-bottom: 12px;
	background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.06);
}
.pay-total {
	.pt-label { font-size: 12px; color: #94a3b8; }
	.pt-yen { font-size: 14px; color: $gold-light; font-weight: 700; vertical-align: super; }
	.pt-amount { font-size: 28px; color: $gold-light; font-weight: 800; line-height: 1; }
	.pt-expire { display: block; font-size: 11px; color: #94a3b8; margin-top: 2px; }
}
.pay-now-btn {
	height: 40px; padding: 0 32px; border-radius: 20px; border: none !important;
	font-size: 14px; font-weight: 600; color: #fff !important;
	background: linear-gradient(135deg, $gold, #d97706) !important;
	box-shadow: 0 4px 14px rgba($gold, 0.25);
	&:hover { box-shadow: 0 6px 18px rgba($gold, 0.4); }
	&.is-disabled {
		opacity: 0.5; cursor: not-allowed;
		background: linear-gradient(135deg, #78716c, #a8a29e) !important;
		box-shadow: none;
	}
}

/* ===================== 充值须知 ===================== */
.notice-section {
	margin-bottom: 6px;
	border-bottom: 1px solid rgba(255,255,255,0.04);
}
.notice-header {
	display: flex; justify-content: space-between; align-items: center;
	padding: 10px 0; cursor: pointer;
	span { font-size: 12px; color: #64748b; font-weight: 500; }
	i, :deep(.el-icon) { font-size: 12px; color: #64748b; }
	&:hover span { color: #94a3b8; }
}
.notice-body {
	padding: 0 0 12px;
	p { font-size: 11px; color: #64748b; line-height: 1.7; margin: 0; padding: 1px 0; }
}

/* ===================== 扫码支付页 ===================== */
.scan-page {}
.scan-nav {
	display: flex; align-items: center; gap: 12px; margin-bottom: 16px;
	.nav-back {
		font-size: 13px; color: #94a3b8; cursor: pointer;
		&:hover { color: $cyan; } i, :deep(.el-icon) { margin-right: 2px; }
	}
	.nav-title { font-size: 15px; color: #e2e8f0; font-weight: 600; }
}
.scan-card {
	background: rgba(255,255,255,0.03); border: 1px solid rgba(255,255,255,0.06);
	border-radius: 12px; padding: 24px; text-align: center;
}
.scan-method-bar {
	display: inline-flex; align-items: center; gap: 6px;
	padding: 4px 14px; border-radius: 14px; font-size: 12px; font-weight: 600; margin-bottom: 10px;
	i, :deep(.el-icon) { font-size: 14px; }
	&.alipay { background: rgba(#1677ff, 0.1); color: #4096ff; }
	&.wechat { background: rgba(#07c160, 0.1); color: #07c160; }
}
.scan-price-display {
	margin-bottom: 18px;
	.spd-yen { font-size: 16px; color: $red; font-weight: 700; vertical-align: super; }
	.spd-amount { font-size: 36px; color: $red; font-weight: 800; line-height: 1; }
	.spd-decimal { font-size: 16px; color: $red; font-weight: 700; }
}
.qr-wrapper { margin: 0 auto 16px; }
.qr-border {
	width: 180px; height: 180px; margin: 0 auto;
	border: 2px solid rgba(255,255,255,0.08); border-radius: 10px; overflow: hidden;
	padding: 6px; background: #fff;
}
.qr-img { width: 100%; height: 100%; display: block; }
.qr-loading {
	width: 100%; height: 100%; display: flex; align-items: center; justify-content: center;
	background: rgba(255,255,255,0.05); border-radius: 8px;
	i, :deep(.el-icon) { font-size: 28px; color: $cyan; animation: rotating 1.5s linear infinite; }
}
.scan-hint {
	font-size: 12px; color: #94a3b8; margin-bottom: 10px;
	b { color: #e2e8f0; }
}
.scan-meta {
	display: flex; justify-content: center; gap: 8px;
	font-size: 11px; color: #64748b;
}

.scan-btns {
	display: flex; gap: 10px; margin-top: 16px;
	.btn-done {
		flex: 1; height: 42px; border-radius: 10px; border: none !important;
		font-size: 14px; font-weight: 600; color: #fff !important;
		background: linear-gradient(135deg, $green, #16a34a) !important;
		i, :deep(.el-icon) { margin-right: 4px; }
	}
	.btn-trouble {
		height: 42px; padding: 0 18px; border-radius: 10px;
		font-size: 12px; color: #94a3b8 !important;
		background: rgba(255,255,255,0.03) !important; border: 1px solid rgba(255,255,255,0.06) !important;
		&:hover { color: $cyan !important; border-color: rgba($cyan, 0.2) !important; }
	}
}

/* ===================== 支付成功 ===================== */
.success-page { text-align: center; padding: 16px 0; }
.success-icon-wrap { margin-bottom: 14px; }
.success-circle {
	width: 64px; height: 64px; border-radius: 50%; margin: 0 auto;
	background: linear-gradient(135deg, $green, #16a34a);
	display: flex; align-items: center; justify-content: center;
	box-shadow: 0 8px 24px rgba($green, 0.25);
	animation: popIn 0.5s cubic-bezier(0.175,0.885,0.32,1.275);
	i, :deep(.el-icon) { font-size: 32px; color: #fff; }
}
.success-title { font-size: 17px; color: #e2e8f0; margin: 0 0 4px; font-weight: 700; }
.success-sub { font-size: 12px; color: #94a3b8; margin: 0 0 18px; }
.success-info-card {
	background: rgba(255,255,255,0.03); border-radius: 10px;
	padding: 14px 18px; margin: 0 auto 18px; max-width: 340px; text-align: left;
	.si-row {
		display: flex; justify-content: space-between; padding: 6px 0;
		font-size: 12px; color: #94a3b8;
		span:last-child { color: #e2e8f0; }
		.si-gold { color: $gold-light; font-weight: 600; }
	}
}
.finish-btn {
	width: 180px; height: 40px; border-radius: 20px; border: none !important;
	font-size: 14px; font-weight: 600; color: #fff !important;
	background: linear-gradient(135deg, $gold, #d97706) !important;
}

/* ===================== 充值记录 ===================== */
.record-section { margin-top: 4px; }
.record-list {
	background: rgba(255,255,255,0.02); border: 1px solid rgba(255,255,255,0.04);
	border-radius: 10px; overflow: hidden;
}
.record-item {
	display: flex; align-items: center; gap: 12px;
	padding: 12px 14px; border-bottom: 1px solid rgba(255,255,255,0.03);
	&:last-child { border-bottom: none; }
}
.rec-icon {
	width: 32px; height: 32px; border-radius: 8px; flex-shrink: 0;
	background: rgba($gold, 0.1); display: flex; align-items: center; justify-content: center;
	i, :deep(.el-icon) { font-size: 16px; color: $gold-light; }
}
.rec-body { flex: 1; }
.rec-title { font-size: 13px; color: #e2e8f0; font-weight: 500; }
.rec-time { font-size: 11px; color: #64748b; margin-top: 1px; }
.rec-amount { font-size: 14px; color: $gold-light; font-weight: 600; }

/* ===================== 协议弹窗 ===================== */
.notice-modal {
	position: fixed; top: 0; left: 0; right: 0; bottom: 0; z-index: 9999;
	background: rgba(0,0,0,0.6); display: flex; align-items: center; justify-content: center;
	backdrop-filter: blur(4px);
}
.notice-modal-content {
	width: 480px; max-width: 90vw; max-height: 70vh; border-radius: 14px;
	background: #1e293b; border: 1px solid rgba(255,255,255,0.08);
	display: flex; flex-direction: column; overflow: hidden;
}
.nm-header {
	display: flex; justify-content: space-between; align-items: center;
	padding: 16px 20px; border-bottom: 1px solid rgba(255,255,255,0.06);
	span { font-size: 15px; color: #e2e8f0; font-weight: 600; }
	.nm-close { font-size: 18px; color: #94a3b8; cursor: pointer; &:hover { color: #e2e8f0; } }
}
.nm-body {
	flex: 1; overflow-y: auto; padding: 16px 20px;
	h4 { font-size: 13px; color: #e2e8f0; font-weight: 600; margin: 12px 0 6px; &:first-child { margin-top: 0; } }
	p { font-size: 12px; color: #94a3b8; line-height: 1.7; margin: 0 0 8px; }
}
.nm-footer {
	padding: 14px 20px; border-top: 1px solid rgba(255,255,255,0.06); text-align: center;
}
.nm-btn {
	width: 100%; height: 40px; border-radius: 10px; border: none !important;
	font-size: 14px; font-weight: 600; color: #fff !important;
	background: linear-gradient(135deg, $cyan, $purple) !important;
}

@keyframes popIn { 0% { transform: scale(0); } 100% { transform: scale(1); } }
@keyframes rotating { from { transform: rotate(0deg); } to { transform: rotate(360deg); } }
</style>
