<template>
	<router-view></router-view>
</template>

<script>
	export default {
		name: 'App',
		created() {
			
		}
	}
</script>

<style>
	* {
		box-sizing: border-box;
	}

	html,
	body {
		width: 100%;
		height: 100%;
		margin: 0;
		padding: 0;
	}

	[v-cloak] {
		display: none
	}

	.el-tabs__item {
		font-size: 18px;
	}

	.el-loading-spinner .el-loading-text {
		color: #4169E1;
	}

	.el-loading-spinner .path {
		stroke: #4169E1;
	}

	.el-breadcrumb {
		line-height: 60px;
		font-size: 18px;
	}

	.ql-editor {
		/*height: 500px;*/
	}

	.ql-snow .ql-tooltip[data-mode="link"]::before {
		content: "请输入链接地址:";
	}

	.ql-snow .ql-tooltip.ql-editing a.ql-action::after {
		border-right: 0px;
		content: "保存";
		padding-right: 0px;
	}

	.ql-snow .ql-tooltip[data-mode="video"]::before {
		content: "请输入视频地址:";
	}

	.ql-snow .ql-picker.ql-size .ql-picker-label::before,
	.ql-snow .ql-picker.ql-size .ql-picker-item::before {
		content: "14px";
	}

	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="small"]::before,
	.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="small"]::before {
		content: "10px";
	}

	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="large"]::before,
	.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="large"]::before {
		content: "18px";
	}

	.ql-snow .ql-picker.ql-size .ql-picker-label[data-value="huge"]::before,
	.ql-snow .ql-picker.ql-size .ql-picker-item[data-value="huge"]::before {
		content: "32px";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item::before {
		content: "文本";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="1"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="1"]::before {
		content: "标题1";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="2"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="2"]::before {
		content: "标题2";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="3"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="3"]::before {
		content: "标题3";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="4"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="4"]::before {
		content: "标题4";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="5"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="5"]::before {
		content: "标题5";
	}

	.ql-snow .ql-picker.ql-header .ql-picker-label[data-value="6"]::before,
	.ql-snow .ql-picker.ql-header .ql-picker-item[data-value="6"]::before {
		content: "标题6";
	}

	.ql-snow .ql-picker.ql-font .ql-picker-label::before,
	.ql-snow .ql-picker.ql-font .ql-picker-item::before {
		content: "标准字体";
	}

	.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="serif"]::before,
	.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="serif"]::before {
		content: "衬线字体";
	}

	.ql-snow .ql-picker.ql-font .ql-picker-label[data-value="monospace"]::before,
	.ql-snow .ql-picker.ql-font .ql-picker-item[data-value="monospace"]::before {
		content: "等宽字体";
	}
	.breadcrumb-preview {
		display: none !important;
		border-radius: 0;
		padding: 0px 10%;
		margin: 0px auto;
		z-index: 100;
		background: none;
		width: 100%;
		border-color: rgba(14,165,233,0.15);
		border-width: 0 0 1px;
		position: relative;
		border-style: solid;
	}
	.breadcrumb-preview::before {
		content: '';
		position: absolute;
		top: 0; left: 0; right: 0; bottom: 0;
		background: linear-gradient(90deg, rgba(14,165,233,0.03) 0%, rgba(124,58,237,0.02) 50%, transparent 100%);
		pointer-events: none;
	}
	.breadcrumb-preview .el-breadcrumb {
		border-radius: 4px;
		padding: 0 0 0 40px;
		background: #fff;
		font-size: 14px;
		line-height: 48px;
		position: relative;
	}
	.breadcrumb-preview .el-breadcrumb::before {
		content: '\e6d0';
		font-family: 'element-icons';
		position: absolute;
		left: 12px;
		top: 50%;
		transform: translateY(-50%);
		font-size: 16px;
		color: #0ea5e9;
	}
	.breadcrumb-preview .el-breadcrumb .el-breadcrumb__separator {
		margin: 0 9px;
		color: #ccc;
		font-weight: 500;
	}
	.breadcrumb-preview .el-breadcrumb .item1 .el-breadcrumb__inner a {
		color: #666;
		display: inline-block;
	}
	.breadcrumb-preview .el-breadcrumb .item2 .el-breadcrumb__inner a {
		color: #666;
		display: inline-block;
	}
	.breadcrumb-preview .el-breadcrumb .item3 .el-breadcrumb__inner a {
		color: #999;
	}
	.back_box {
		border-radius: 0;
		padding: 0px 10%;
		margin: 10px auto 10px;
		background: none;
		display: flex;
		width: 100%;
		justify-content: flex-end;
	}
	.back_box .backBtn {
		border: 0;
		cursor: pointer;
		border-radius: 20px;
		padding: 0 20px 0 16px;
		margin: 0;
		outline: none;
		background: linear-gradient(135deg, #0ea5e9, #7c3aed);
		width: auto;
		font-size: 14px;
		line-height: 40px;
		opacity: 1;
		height: 40px;
		transition: all 0.3s;
		box-shadow: 0 2px 8px rgba(14,165,233,0.2);
	}
	.back_box .backBtn:hover {
		transform: translateY(-1px);
		box-shadow: 0 4px 16px rgba(14,165,233,0.3);
	}
	.back_box .backBtn .icon {
		margin: 0 4px 0 0;
		color: #fff;
		font-size: 14px;
	}
	.back_box .backBtn .text {
		color: #fff;
	}
	.back_box .backBtn:hover {
		opacity: 0.8;
	}
	.back_box .backBtn:hover .icon {
		color: rgba(255, 255, 255, 1);
	}
	.back_box .backBtn:hover .text {
		color: rgba(255, 255, 255, 1);
	}
	/* 分页样式 */
	#pagination {
		padding: 0;
		margin: 20px auto;
		color: #333;
		white-space: nowrap;
		font-weight: 500;
		width: 100%;
		font-size: 16px;
		text-align: center;
		order: 50;
	}
	#pagination.el-pagination .el-pagination__total {
		margin: 0 10px 0 0;
		color: inherit;
		font-weight: 400;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .btn-prev {
		border: none;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #666;
		background: #f4f4f5;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		min-width: 35px;
		height: 28px;
	}
	
	#pagination.el-pagination .btn-next {
		border: none;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #666;
		background: #f4f4f5;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		min-width: 35px;
		height: 28px;
	}
	
	#pagination.el-pagination .btn-prev:disabled {
		border: none;
		cursor: not-allowed;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #C0C4CC;
		background: #f4f4f5;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .btn-next:disabled {
		border: none;
		cursor: not-allowed;
		border-radius: 2px;
		padding: 0;
		margin: 0 5px;
		color: #C0C4CC;
		background: #f4f4f5;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pager {
		padding: 0;
		margin: 0;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
	}
	
	#pagination.el-pagination .el-pager .number {
		cursor: pointer;
		padding: 0 4px;
		margin: 0 5px;
		color: #666;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		border-radius: 2px;
		background: #f4f4f5;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pager .number:hover {
		cursor: pointer;
		padding: 0 4px;
		margin: 0 5px;
		color: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		border-radius: 2px;
		background: #0ea5e9;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pager .number.active {
		cursor: default;
		padding: 0 4px;
		margin: 0 5px;
		color: #fff;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		border-radius: 2px;
		background: #0ea5e9;
		text-align: center;
		min-width: 30px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__sizes {
		display: inline-block;
		vertical-align: top;
		font-size: 13px;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__sizes .el-input {
		margin: 0 5px;
		width: 100px;
		position: relative;
	}
	
	#pagination.el-pagination .el-pagination__sizes .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		cursor: pointer;
		padding: 0 25px 0 8px;
		color: #606266;
		display: inline-block;
		font-size: 13px;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__sizes .el-input span.el-input__suffix {
		top: 0;
		position: absolute;
		right: 0;
		height: 100%;
	}
	
	#pagination.el-pagination .el-pagination__sizes .el-input .el-input__suffix .el-select__caret {
		cursor: pointer;
		color: #C0C4CC;
		width: 25px;
		font-size: 14px;
		line-height: 28px;
		text-align: center;
	}
	
	#pagination.el-pagination .el-pagination__jump {
		margin: 0 0 0 24px;
		color: inherit;
		display: inline-block;
		vertical-align: top;
		font-size: inherit;
		line-height: 28px;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__jump .el-input {
		border-radius: 3px;
		padding: 0 2px;
		margin: 0 2px;
		display: inline-block;
		width: 50px;
		font-size: inherit;
		line-height: 18px;
		position: relative;
		text-align: center;
		height: 28px;
	}
	
	#pagination.el-pagination .el-pagination__jump .el-input .el-input__inner {
		border: 1px solid #DCDFE6;
		cursor: pointer;
		padding: 0 3px;
		color: #333;
		display: inline-block;
		font-size: inherit;
		line-height: 28px;
		border-radius: 3px;
		outline: 0;
		background: #FFF;
		width: 100%;
		text-align: center;
		height: 28px;
	}

	/* ===== 技能交易主题全局装饰 ===== */
	.theme-deco-dots {
		position: fixed;
		top: 0; left: 0; right: 0; bottom: 0;
		pointer-events: none;
		z-index: 0;
		overflow: hidden;
	}
	.theme-deco-dots .dot {
		position: absolute;
		border-radius: 50%;
		opacity: 0.08;
		animation: floatDot 20s infinite ease-in-out;
	}
	.theme-deco-dots .dot:nth-child(1) { width: 120px; height: 120px; background: #0ea5e9; top: 10%; left: 5%; animation-delay: 0s; }
	.theme-deco-dots .dot:nth-child(2) { width: 80px; height: 80px; background: #7c3aed; top: 30%; right: 8%; animation-delay: -5s; }
	.theme-deco-dots .dot:nth-child(3) { width: 60px; height: 60px; background: #0ea5e9; bottom: 20%; left: 15%; animation-delay: -10s; }
	.theme-deco-dots .dot:nth-child(4) { width: 100px; height: 100px; background: #7c3aed; bottom: 10%; right: 20%; animation-delay: -15s; }
	.theme-deco-dots .dot:nth-child(5) { width: 40px; height: 40px; background: #0ea5e9; top: 60%; left: 50%; animation-delay: -8s; }

	@keyframes floatDot {
		0%, 100% { transform: translate(0, 0) scale(1); }
		25% { transform: translate(20px, -30px) scale(1.1); }
		50% { transform: translate(-15px, 20px) scale(0.9); }
		75% { transform: translate(25px, 15px) scale(1.05); }
	}

	.page-section-title {
		display: flex;
		align-items: center;
		justify-content: center;
		gap: 12px;
		margin: 20px 0 16px;
		font-size: 18px;
		font-weight: 700;
		color: #1e293b;
	}
	.page-section-title::before,
	.page-section-title::after {
		content: '';
		width: 40px;
		height: 2px;
		background: linear-gradient(90deg, #0ea5e9, #7c3aed);
		border-radius: 1px;
	}
	.page-section-title .deco-icon {
		display: inline-flex;
		align-items: center;
		justify-content: center;
		width: 32px;
		height: 32px;
		border-radius: 8px;
		background: linear-gradient(135deg, rgba(14,165,233,0.1), rgba(124,58,237,0.1));
		color: #0ea5e9;
		font-size: 16px;
	}

	#pagination.el-pagination .el-pager .number:hover {
		background: linear-gradient(135deg, #0ea5e9, #7c3aed) !important;
	}
	#pagination.el-pagination .el-pager .number.active {
		background: linear-gradient(135deg, #0ea5e9, #7c3aed) !important;
	}

	/* ===== 全局交互增强 ===== */

	/* 平滑滚动 */
	html { scroll-behavior: smooth; }

	/* 全局选中颜色 */
	::selection {
		background: rgba(14,165,233,0.25);
		color: inherit;
	}

	/* 全局链接过渡 */
	a { transition: color 0.25s, opacity 0.25s; }

	/* Element UI 按钮增强 */
	.el-button {
		transition: all 0.3s cubic-bezier(0.4,0,0.2,1) !important;
	}
	.el-button:active {
		transform: scale(0.96) !important;
	}

	/* Element UI 对话框增强 */
	.el-dialog__wrapper .el-dialog {
		animation: dialogPopIn 0.3s cubic-bezier(0.34,1.56,0.64,1);
	}
	@keyframes dialogPopIn {
		from { opacity: 0; transform: scale(0.92) translateY(10px); }
		to { opacity: 1; transform: scale(1) translateY(0); }
	}

	/* 消息提示增强 */
	.el-message {
		border-radius: 12px !important;
		box-shadow: 0 8px 32px rgba(0,0,0,0.12) !important;
		border: none !important;
		animation: msgSlideIn 0.35s cubic-bezier(0.34,1.3,0.64,1) !important;
	}
	@keyframes msgSlideIn {
		from { opacity: 0; transform: translateY(-20px); }
		to { opacity: 1; transform: translateY(0); }
	}

	/* Element UI 输入框 focus 增强 */
	.el-input__inner:focus {
		box-shadow: 0 0 0 3px rgba(14,165,233,0.1) !important;
		transition: all 0.3s !important;
	}

	/* 全局加载旋转增强 */
	.el-loading-spinner {
		.circular { animation: loading-rotate 1.5s linear infinite !important; }
		.el-loading-text {
			font-weight: 500;
			letter-spacing: 1px;
		}
	}

	/* 表格行 hover 过渡增强 */
	.el-table__row {
		transition: background-color 0.25s ease !important;
	}

	/* 卡片全局悬浮增强 */
	.el-card {
		transition: all 0.3s cubic-bezier(0.4,0,0.2,1) !important;
	}
	.el-card:hover {
		box-shadow: 0 8px 24px rgba(0,0,0,0.08) !important;
	}

	/* 标签页切换动画 */
	.el-tabs__active-bar {
		transition: all 0.35s cubic-bezier(0.4,0,0.2,1) !important;
	}

	/* 全局自定义滚动条 */
	::-webkit-scrollbar { width: 6px; height: 6px; }
	::-webkit-scrollbar-track { background: transparent; }
	::-webkit-scrollbar-thumb {
		background: rgba(14,165,233,0.15);
		border-radius: 3px;
		transition: background 0.3s;
	}
	::-webkit-scrollbar-thumb:hover { background: rgba(14,165,233,0.3); }

	/* 工具提示增强 */
	.el-tooltip__popper {
		border-radius: 8px !important;
		box-shadow: 0 4px 16px rgba(0,0,0,0.15) !important;
		font-size: 13px !important;
	}

	/* "查看更多"按钮全局样式增强 */
	.moreBtn-new {
		transition: all 0.3s cubic-bezier(0.4,0,0.2,1) !important;
		&:active {
			transform: scale(0.96) !important;
		}
	}
</style>