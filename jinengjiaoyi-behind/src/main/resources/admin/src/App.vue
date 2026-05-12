<template>
	<div id="app" class="">
		<router-view></router-view>
	</div>
</template>

<script>
	export default {
		name: 'app',
		data() {
			return{
			}
		},
		created() {
			
		},
	}
</script>

<style>
	* {
		padding: 0;
		margin: 0;
		box-sizing: border-box;
	}

	html, body {
		width: 100%;
		height: 100%;
		overflow: hidden;
		background: #0f172a;
		color: #e2e8f0;
	}

	#app {
		height: 100%;
		background: #0f172a;
	}

	body {
		padding: 0;
		margin: 0;
		font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'PingFang SC', 'Microsoft YaHei', sans-serif;
	}

	::-webkit-scrollbar {
		width: 6px;
		height: 6px;
	}
	::-webkit-scrollbar-track {
		background: #0f172a;
	}
	::-webkit-scrollbar-thumb {
		background: rgba(14,165,233,0.3);
		border-radius: 3px;
	}
	::-webkit-scrollbar-thumb:hover {
		background: rgba(14,165,233,0.5);
	}

	.form-content, [class*="form-content"] {
		background: #1a2236 !important;
		border: 1px solid rgba(14,165,233,0.1) !important;
		border-radius: 10px !important;
		padding: 16px !important;
		color: #94a3b8 !important;
	}
	.table-content, [class*="table-content"] {
		background: #1a2236 !important;
		border: 1px solid rgba(14,165,233,0.1) !important;
		border-radius: 10px !important;
		padding: 8px 16px !important;
		color: #94a3b8 !important;
	}
	.el-form-item__content .el-input__inner,
	.el-form-item__content .el-textarea__inner {
		background-color: #0f172a !important;
		border-color: rgba(14,165,233,0.15) !important;
		color: #e2e8f0 !important;
	}
	.el-table__body-wrapper {
		background: transparent !important;
	}
	.el-checkbox__inner {
		background-color: #1e293b !important;
		border-color: rgba(14,165,233,0.2) !important;
	}
	.el-checkbox__input.is-checked .el-checkbox__inner {
		background-color: #0ea5e9 !important;
		border-color: #0ea5e9 !important;
	}
	.el-radio__inner {
		background-color: #1e293b !important;
		border-color: rgba(14,165,233,0.2) !important;
	}
	.el-switch__core {
		background-color: #334155 !important;
		border-color: #334155 !important;
	}

	.el-table {
		background-color: transparent !important;
		color: #94a3b8 !important;
	}
	.el-table th.el-table__cell {
		background-color: rgba(14,165,233,0.06) !important;
		color: #e2e8f0 !important;
		border-bottom: 1px solid rgba(14,165,233,0.12) !important;
		font-weight: 600;
	}
	.el-table tr {
		background-color: transparent !important;
	}
	.el-table td.el-table__cell {
		border-bottom: 1px solid rgba(14,165,233,0.06) !important;
	}
	.el-table--enable-row-hover .el-table__body tr:hover > td.el-table__cell {
		background-color: rgba(14,165,233,0.06) !important;
	}
	.el-table--striped .el-table__body tr.el-table__row--striped td.el-table__cell {
		background-color: rgba(14,165,233,0.03) !important;
	}
	.el-table::before, .el-table--border::after {
		background-color: rgba(14,165,233,0.1) !important;
	}
	.el-table--border td.el-table__cell, .el-table--border th.el-table__cell {
		border-right: 1px solid rgba(14,165,233,0.06) !important;
	}
	.el-table__empty-text {
		color: #64748b !important;
	}

	.el-input__inner {
		background-color: rgba(30,41,59,0.8) !important;
		border-color: rgba(14,165,233,0.15) !important;
		color: #e2e8f0 !important;
	}
	.el-input__inner:focus {
		border-color: #0ea5e9 !important;
		box-shadow: 0 0 12px rgba(14,165,233,0.15);
	}
	.el-input__inner::placeholder {
		color: rgba(148,163,184,0.4) !important;
	}
	.el-textarea__inner {
		background-color: rgba(30,41,59,0.8) !important;
		border-color: rgba(14,165,233,0.15) !important;
		color: #e2e8f0 !important;
	}

	.el-select-dropdown {
		background-color: rgba(15,23,42,0.95) !important;
		border: 1px solid rgba(14,165,233,0.15) !important;
	}
	.el-select-dropdown__item {
		color: #94a3b8 !important;
	}
	.el-select-dropdown__item.hover, .el-select-dropdown__item:hover {
		background-color: rgba(14,165,233,0.1) !important;
		color: #e2e8f0 !important;
	}
	.el-select-dropdown__item.selected {
		color: #0ea5e9 !important;
		font-weight: 600;
	}
	.popper__arrow::after {
		border-bottom-color: rgba(15,23,42,0.95) !important;
	}

	.el-dialog {
		background: rgba(15,23,42,0.95) !important;
		border: 1px solid rgba(14,165,233,0.15);
		border-radius: 16px !important;
		box-shadow: 0 8px 40px rgba(0,0,0,0.5) !important;
	}
	.el-dialog__title {
		color: #e2e8f0 !important;
		font-weight: 600;
	}
	.el-dialog__header {
		border-bottom: 1px solid rgba(14,165,233,0.1);
	}
	.el-dialog__headerbtn .el-dialog__close {
		color: #64748b !important;
	}
	.el-dialog__headerbtn .el-dialog__close:hover {
		color: #0ea5e9 !important;
	}
	.el-dialog__body {
		color: #cbd5e1 !important;
	}
	.el-dialog__body .ql-editor,
	.el-dialog__body .ql-snow .ql-editor {
		color: #e2e8f0 !important;
	}
	.el-dialog__body .ql-editor p,
	.el-dialog__body .ql-editor li,
	.el-dialog__body .ql-editor h1,
	.el-dialog__body .ql-editor h2,
	.el-dialog__body .ql-editor h3,
	.el-dialog__body .ql-editor h4,
	.el-dialog__body .ql-editor h5,
	.el-dialog__body .ql-editor h6,
	.el-dialog__body .ql-editor span,
	.el-dialog__body .ql-editor div,
	.el-dialog__body .ql-editor blockquote {
		color: #e2e8f0 !important;
	}
	.el-dialog__body .ql-editor strong {
		color: #f1f5f9 !important;
	}
	.el-dialog__body .ql-editor blockquote {
		border-left-color: #0ea5e9 !important;
		background: rgba(14,165,233,0.06) !important;
		padding: 10px 16px !important;
		border-radius: 0 6px 6px 0 !important;
	}
	.el-dialog__body .el-form-item__content {
		color: #e2e8f0 !important;
	}

	.el-form-item__label {
		color: #94a3b8 !important;
	}

	.el-card {
		background-color: rgba(15,23,42,0.7) !important;
		border: 1px solid rgba(14,165,233,0.1) !important;
		border-radius: 12px !important;
		color: #94a3b8 !important;
	}
	.el-card__header {
		border-bottom: 1px solid rgba(14,165,233,0.1) !important;
		color: #e2e8f0 !important;
	}

	.el-pagination {
		color: #94a3b8 !important;
	}
	.el-pagination button {
		background-color: rgba(30,41,59,0.8) !important;
		color: #94a3b8 !important;
	}
	.el-pagination .el-pager li {
		background-color: rgba(30,41,59,0.8) !important;
		color: #94a3b8 !important;
	}
	.el-pagination .el-pager li.active {
		background-color: #0ea5e9 !important;
		color: #fff !important;
	}
	.el-pagination .el-pager li:hover {
		color: #0ea5e9 !important;
	}

	.el-breadcrumb__inner {
		color: #64748b !important;
	}
	.el-breadcrumb__inner.is-link:hover {
		color: #0ea5e9 !important;
	}
	.el-breadcrumb__separator {
		color: #475569 !important;
	}

	.el-tag {
		border-radius: 6px;
	}

	.el-button--primary {
		background: linear-gradient(135deg, #0ea5e9 0%, #7c3aed 100%) !important;
		border: none !important;
	}
	.el-button--primary:hover {
		box-shadow: 0 0 16px rgba(14,165,233,0.3);
		opacity: 0.9;
	}

	.el-menu {
		background-color: transparent !important;
		border-right: none !important;
	}

	.el-sub-menu__title i {
		color: inherit !important;
	}

	.detail-form-content {
		background: rgba(15,23,42,0.7) !important;
		border: 1px solid rgba(14,165,233,0.1);
		border-radius: 12px;
	}

	/* ====== 搜索表单区域全局美化 ====== */
	.center-form-pv, .el-form.center-form-pv,
	[class*="center-form"] {
		background: rgba(15, 23, 42, 0.6) !important;
		backdrop-filter: blur(12px);
		border: 1px solid rgba(14, 165, 233, 0.1) !important;
		border-radius: 14px !important;
		padding: 0 !important;
		margin: 0 0 16px 0 !important;
		overflow: hidden;
		transition: border-color 0.3s, box-shadow 0.3s;
	}
	.center-form-pv:hover, .el-form.center-form-pv:hover {
		border-color: rgba(14, 165, 233, 0.18) !important;
		box-shadow: 0 4px 24px rgba(14, 165, 233, 0.06);
	}

	.center-form-pv > .el-row:first-child,
	.el-form.center-form-pv > .el-row:first-child {
		padding: 16px 20px 12px !important;
		display: flex !important;
		flex-wrap: wrap !important;
		align-items: center !important;
		gap: 6px 16px !important;
		border-bottom: 1px solid rgba(14, 165, 233, 0.06);
	}
	.center-form-pv > .el-row,
	.el-form.center-form-pv > .el-row {
		padding: 12px 20px !important;
		display: flex !important;
		flex-wrap: wrap !important;
		align-items: center !important;
		gap: 6px 16px !important;
	}
	.center-form-pv > .el-row > div {
		display: flex !important;
		align-items: center !important;
		margin: 0 !important;
		gap: 0 !important;
	}

	.item-label, .item-label[style],
	label.item-label, label.item-label[style] {
		color: #e2e8f0 !important;
		font-size: 13px !important;
		font-weight: 600 !important;
		white-space: nowrap !important;
		line-height: 34px !important;
		height: auto !important;
		margin: 0 8px 0 0 !important;
		letter-spacing: 0.5px;
		text-shadow: 0 1px 2px rgba(0,0,0,0.2);
	}

	.center-form-pv .el-input__inner,
	.center-form-pv .el-select .el-input__inner {
		background: rgba(15, 23, 42, 0.9) !important;
		border: 1px solid rgba(71, 85, 105, 0.35) !important;
		border-radius: 8px !important;
		color: #f1f5f9 !important;
		height: 34px !important;
		font-size: 13px !important;
		padding: 0 12px !important;
		transition: all 0.25s ease !important;
		min-width: 150px;
	}
	.center-form-pv .el-input__inner:focus,
	.center-form-pv .el-select .el-input__inner:focus {
		border-color: #0ea5e9 !important;
		box-shadow: 0 0 0 3px rgba(14, 165, 233, 0.12) !important;
		background: rgba(15, 23, 42, 1) !important;
	}
	.center-form-pv .el-input__inner::placeholder {
		color: rgba(148, 163, 184, 0.5) !important;
		font-size: 13px !important;
	}
	.center-form-pv .el-input__suffix {
		color: #94a3b8;
	}
	.center-form-pv .el-input__icon {
		line-height: 34px !important;
	}
	.center-form-pv .el-select .el-input .el-select__caret {
		color: #94a3b8 !important;
		font-size: 13px;
	}

	.center-form-pv .el-date-editor .el-input__inner {
		padding-left: 30px !important;
	}

	.center-form-pv .search,
	.center-form-pv .el-button.search {
		background: linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%) !important;
		border: none !important;
		border-radius: 8px !important;
		color: #fff !important;
		font-weight: 600 !important;
		font-size: 13px !important;
		height: 34px !important;
		min-width: 80px !important;
		padding: 0 16px !important;
		letter-spacing: 1px;
		transition: all 0.25s ease !important;
		box-shadow: 0 2px 8px rgba(14, 165, 233, 0.25);
		cursor: pointer;
	}
	.center-form-pv .search:hover,
	.center-form-pv .el-button.search:hover {
		background: linear-gradient(135deg, #38bdf8 0%, #0ea5e9 100%) !important;
		box-shadow: 0 4px 16px rgba(14, 165, 233, 0.4) !important;
		transform: translateY(-1px);
		opacity: 1 !important;
	}
	.center-form-pv .search:active {
		transform: translateY(0);
	}
	.center-form-pv .search .icon {
		font-size: 14px !important;
		margin-right: 4px !important;
		color: #fff !important;
		height: auto !important;
	}

	/* ====== 操作按钮行美化 ====== */
	.actions, .actions[style] {
		background: transparent !important;
		padding: 0 20px 12px !important;
	}
	.actions .el-row {
		display: flex !important;
		flex-wrap: wrap !important;
		gap: 8px !important;
	}

	.add, .el-button.add {
		background: linear-gradient(135deg, #0ea5e9 0%, #7c3aed 100%) !important;
		border: none !important;
		border-radius: 8px !important;
		color: #fff !important;
		font-weight: 600 !important;
		font-size: 13px !important;
		height: 34px !important;
		padding: 0 16px !important;
		transition: all 0.25s ease !important;
		box-shadow: 0 2px 8px rgba(14, 165, 233, 0.15);
	}
	.add:hover, .el-button.add:hover {
		box-shadow: 0 4px 16px rgba(14, 165, 233, 0.3) !important;
		transform: translateY(-1px);
	}

	.del, .el-button.del {
		background: rgba(239, 68, 68, 0.12) !important;
		border: 1px solid rgba(239, 68, 68, 0.3) !important;
		border-radius: 8px !important;
		color: #f87171 !important;
		font-weight: 500 !important;
		font-size: 13px !important;
		height: 34px !important;
		padding: 0 16px !important;
		transition: all 0.25s ease !important;
	}
	.del:hover, .el-button.del:hover {
		background: rgba(239, 68, 68, 0.22) !important;
		border-color: rgba(239, 68, 68, 0.45) !important;
		color: #ef4444 !important;
		transform: translateY(-1px);
	}

	.btn18, .el-button.btn18 {
		background: rgba(14, 165, 233, 0.1) !important;
		border: 1px solid rgba(14, 165, 233, 0.25) !important;
		border-radius: 8px !important;
		color: #38bdf8 !important;
		font-weight: 500 !important;
		font-size: 13px !important;
		height: 34px !important;
		padding: 0 16px !important;
		transition: all 0.25s ease !important;
	}
	.btn18:hover, .el-button.btn18:hover {
		background: rgba(14, 165, 233, 0.18) !important;
		border-color: rgba(14, 165, 233, 0.4) !important;
		transform: translateY(-1px);
	}

	.search, .add, .del, .btn18 {
		border-radius: 8px !important;
	}

	/* ====== 主内容区 ====== */
	.main-content > div {
		background: rgba(26, 34, 54, 0.6) !important;
		border: 1px solid rgba(14, 165, 233, 0.06) !important;
		border-radius: 14px !important;
		backdrop-filter: blur(8px);
	}
	.main-content {
		background: transparent !important;
		height: 100%;
		display: flex;
		flex-direction: column;
		overflow: hidden;
	}

	.tables, .el-table.tables {
		background: transparent !important;
	}
	.el-table__header-wrapper th {
		background-color: rgba(14,165,233,0.06) !important;
	}

	.el-form-item .el-form-item__label {
		color: #94a3b8 !important;
	}

	/* ====== 表格内操作按钮美化 ====== */
	.el-table .el-button--mini,
	.el-table .el-button--small {
		border-radius: 8px !important;
		font-size: 12px !important;
		padding: 5px 12px !important;
		transition: all 0.25s ease;
	}
	.el-table .el-button--primary.el-button--mini,
	.el-table .el-button--primary.el-button--small {
		background: linear-gradient(135deg, #0ea5e9, #0284c7) !important;
		border: none !important;
		box-shadow: 0 1px 4px rgba(14,165,233,0.15);
	}
	.el-table .el-button--primary.el-button--mini:hover,
	.el-table .el-button--primary.el-button--small:hover {
		box-shadow: 0 2px 8px rgba(14,165,233,0.3);
	}
	.el-table .el-button--success.el-button--mini,
	.el-table .el-button--success.el-button--small {
		background: rgba(16, 185, 129, 0.12) !important;
		border: 1px solid rgba(16, 185, 129, 0.25) !important;
		color: #10b981 !important;
	}
	.el-table .el-button--success.el-button--mini:hover,
	.el-table .el-button--success.el-button--small:hover {
		background: rgba(16, 185, 129, 0.2) !important;
	}
	.el-table .el-button--danger.el-button--mini,
	.el-table .el-button--danger.el-button--small {
		background: rgba(239, 68, 68, 0.1) !important;
		border: 1px solid rgba(239, 68, 68, 0.25) !important;
		color: #f87171 !important;
	}
	.el-table .el-button--danger.el-button--mini:hover,
	.el-table .el-button--danger.el-button--small:hover {
		background: rgba(239, 68, 68, 0.2) !important;
	}
	.el-table .el-button--warning.el-button--mini,
	.el-table .el-button--warning.el-button--small {
		background: rgba(245, 158, 11, 0.1) !important;
		border: 1px solid rgba(245, 158, 11, 0.25) !important;
		color: #f59e0b !important;
	}
	.el-table .el-button--warning.el-button--mini:hover,
	.el-table .el-button--warning.el-button--small:hover {
		background: rgba(245, 158, 11, 0.2) !important;
	}
	.el-table .el-button--info.el-button--mini,
	.el-table .el-button--info.el-button--small {
		background: rgba(100, 116, 139, 0.1) !important;
		border: 1px solid rgba(100, 116, 139, 0.25) !important;
		color: #94a3b8 !important;
	}

	.router-view {
		padding: 10px;
	}

	.bread-crumbs {
		color: #64748b;
	}

	.el-message-box {
		background: rgba(15,23,42,0.95) !important;
		border: 1px solid rgba(14,165,233,0.15) !important;
		border-radius: 12px !important;
	}
	.el-message-box__title {
		color: #e2e8f0 !important;
	}
	.el-message-box__content {
		color: #94a3b8 !important;
	}
	.el-message-box__headerbtn .el-message-box__close {
		color: #64748b !important;
	}

	.el-upload-dragger {
		background-color: rgba(30,41,59,0.5) !important;
		border-color: rgba(14,165,233,0.2) !important;
	}
	.el-upload-dragger:hover {
		border-color: #0ea5e9 !important;
	}

	.tags-view-container {
		background: rgba(15,23,42,0.5) !important;
	}

	/* ====== 全面深色主题文字颜色覆盖 ====== */
	.tab-content,
	.tab-content *,
	.tab-page .tab-content label,
	.tab-page .tab-content span,
	.tab-page .tab-content div,
	.tab-page .tab-content p,
	.tab-page .tab-content td,
	.tab-page .tab-content th {
		color: #cbd5e1;
	}

	.tab-content [style],
	.tab-content label[style],
	.tab-content span[style],
	.tab-content div[style],
	.tab-content p[style] {
		color: #cbd5e1 !important;
	}

	.item-label, .item-label[style],
	label.item-label, label.item-label[style] {
		color: #e2e8f0 !important;
		font-size: 13px !important;
		font-weight: 600 !important;
		line-height: 34px !important;
	}

	.el-table .cell {
		color: #cbd5e1 !important;
	}
	.el-table .cell a {
		color: #38bdf8 !important;
	}
	.el-table th.el-table__cell .cell {
		color: #e2e8f0 !important;
		font-weight: 600 !important;
	}

	.el-pagination span,
	.el-pagination button,
	.el-pagination__total,
	.el-pagination__jump {
		color: #94a3b8 !important;
	}
	.el-pagination .el-input__inner {
		background: rgba(30,41,59,0.8) !important;
		color: #e2e8f0 !important;
	}

	.el-table__fixed,
	.el-table__fixed-right {
		background: transparent !important;
	}
	.el-table__fixed::before,
	.el-table__fixed-right::before {
		background-color: rgba(14,165,233,0.1) !important;
	}

	.el-button--text {
		color: #38bdf8 !important;
	}
	.el-button--text:hover {
		color: #0ea5e9 !important;
	}

	.el-link {
		color: #38bdf8 !important;
	}

	.el-checkbox__label,
	.el-radio__label {
		color: #94a3b8 !important;
	}

	.el-tabs__item {
		color: #64748b !important;
	}
	.el-tabs__item.is-active {
		color: #0ea5e9 !important;
	}

	.el-date-table td.normal span,
	.el-date-table td span {
		color: #cbd5e1 !important;
	}
	.el-date-table td.disabled span {
		color: #475569 !important;
	}
	.el-date-table td.current span {
		color: #fff !important;
		background: #0ea5e9 !important;
	}
	.el-picker-panel {
		background: rgba(15,23,42,0.95) !important;
		border: 1px solid rgba(14,165,233,0.15) !important;
		color: #cbd5e1 !important;
	}
	.el-picker-panel__icon-btn {
		color: #94a3b8 !important;
	}
	.el-date-picker__header-label {
		color: #e2e8f0 !important;
	}
	.el-picker-panel__footer {
		background: rgba(15,23,42,0.5) !important;
		border-top: 1px solid rgba(14,165,233,0.1) !important;
	}

	.el-tooltip__popper {
		background: rgba(15,23,42,0.95) !important;
		color: #e2e8f0 !important;
		border: 1px solid rgba(14,165,233,0.15) !important;
	}

	/* ====== 新增/编辑表单全局暗色适配 ====== */
	.addEdit-block {
		background: none !important;
	}

	.add-update-preview, .el-form.add-update-preview {
		background: rgba(15, 23, 42, 0.6) !important;
		backdrop-filter: blur(12px);
		border: 1px solid rgba(14, 165, 233, 0.08) !important;
		border-radius: 16px !important;
		padding: 32px 28px !important;
	}

	.add-update-preview .el-form-item__label {
		color: #94a3b8 !important;
		font-size: 13px !important;
		font-weight: 500 !important;
	}

	.add-update-preview .el-input__inner {
		background: rgba(15, 23, 42, 0.8) !important;
		border: 1px solid rgba(71, 85, 105, 0.3) !important;
		border-radius: 10px !important;
		color: #e2e8f0 !important;
		font-size: 14px !important;
		height: 40px !important;
		transition: all 0.3s ease !important;
	}
	.add-update-preview .el-input__inner:focus {
		border-color: #0ea5e9 !important;
		box-shadow: 0 0 0 3px rgba(14, 165, 233, 0.1) !important;
	}
	.add-update-preview .el-input__inner::placeholder {
		color: rgba(148, 163, 184, 0.45) !important;
	}
	.add-update-preview .el-input__inner[readonly="readonly"] {
		background: rgba(255, 255, 255, 0.02) !important;
		border-color: transparent !important;
		color: #64748b !important;
	}

	.add-update-preview .el-select .el-input__inner {
		background: rgba(15, 23, 42, 0.8) !important;
		border: 1px solid rgba(71, 85, 105, 0.3) !important;
		border-radius: 10px !important;
		color: #e2e8f0 !important;
	}
	.add-update-preview .el-select .is-disabled .el-input__inner {
		background: rgba(255, 255, 255, 0.02) !important;
		border-color: transparent !important;
		color: #64748b !important;
	}

	.add-update-preview .el-input-number .el-input__inner {
		background: rgba(15, 23, 42, 0.8) !important;
		border: 1px solid rgba(71, 85, 105, 0.3) !important;
		color: #e2e8f0 !important;
	}

	.add-update-preview .el-date-editor .el-input__inner {
		background: rgba(15, 23, 42, 0.8) !important;
		border: 1px solid rgba(71, 85, 105, 0.3) !important;
		border-radius: 10px !important;
		color: #e2e8f0 !important;
	}

	.add-update-preview .el-textarea__inner {
		background: rgba(15, 23, 42, 0.8) !important;
		border: 1px solid rgba(71, 85, 105, 0.3) !important;
		border-radius: 10px !important;
		color: #e2e8f0 !important;
		font-size: 14px !important;
	}
	.add-update-preview .el-textarea__inner:focus {
		border-color: #0ea5e9 !important;
	}

	.add-update-preview .upload .upload-img,
	.add-update-preview .el-upload .el-icon-plus {
		border: 2px dashed rgba(14, 165, 233, 0.2) !important;
		border-radius: 10px !important;
		color: #64748b !important;
		background: rgba(14, 165, 233, 0.03) !important;
		transition: all 0.3s !important;
	}
	.add-update-preview .upload .upload-img:hover,
	.add-update-preview .el-upload .el-icon-plus:hover {
		border-color: #0ea5e9 !important;
		color: #0ea5e9 !important;
		background: rgba(14, 165, 233, 0.06) !important;
	}
	.add-update-preview .el-upload-list .el-upload-list__item {
		border: 1px solid rgba(71, 85, 105, 0.3) !important;
		border-radius: 10px !important;
	}
	.add-update-preview .el-upload__tip {
		color: #64748b !important;
	}
	.add-update-preview .el-upload-dragger {
		background: rgba(15, 23, 42, 0.5) !important;
		border-color: rgba(14, 165, 233, 0.2) !important;
		color: #94a3b8 !important;
	}

	.add-update-preview .editor {
		border: 1px solid rgba(71, 85, 105, 0.3) !important;
		border-radius: 10px !important;
		color: #e2e8f0 !important;
		overflow: hidden;
	}
	.add-update-preview .editor .ql-toolbar {
		background: rgba(15, 23, 42, 0.5) !important;
		border-bottom: 1px solid rgba(71, 85, 105, 0.2) !important;
		border-top: none !important;
		border-left: none !important;
		border-right: none !important;
	}
	.add-update-preview .editor .ql-container {
		background: rgba(15, 23, 42, 0.3) !important;
		border: none !important;
		color: #e2e8f0 !important;
	}
	.add-update-preview .editor .ql-toolbar .ql-stroke {
		stroke: #94a3b8 !important;
	}
	.add-update-preview .editor .ql-toolbar .ql-fill {
		fill: #94a3b8 !important;
	}
	.add-update-preview .editor .ql-toolbar .ql-picker-label {
		color: #94a3b8 !important;
	}
	.add-update-preview .editor .ql-blank::before {
		color: #64748b !important;
	}

	.add-update-preview .viewBtn,
	.add-update-preview .downBtn {
		background: linear-gradient(135deg, #0ea5e9, #0284c7) !important;
		border: none !important;
		border-radius: 10px !important;
		color: #fff !important;
	}
	.add-update-preview .unBtn {
		background: rgba(100, 116, 139, 0.15) !important;
		border-radius: 10px !important;
		color: #94a3b8 !important;
	}

	.add-update-preview span.text {
		background: rgba(15, 23, 42, 0.8) !important;
		border: 1px solid rgba(71, 85, 105, 0.3) !important;
		border-radius: 10px !important;
		color: #e2e8f0 !important;
	}

	/* 表单底部确定/取消按钮 */
	.add-update-preview .el-form-item.btn .el-button,
	.add-update-preview .el-form-item.btn .subBtn,
	.add-update-preview .el-form-item.btn span[class*="Btn"] {
		border-radius: 12px !important;
		min-width: 140px !important;
		height: 42px !important;
		font-size: 14px !important;
		font-weight: 600 !important;
		letter-spacing: 1px;
		transition: all 0.3s ease !important;
		border: none !important;
	}
	.add-update-preview .el-form-item.btn span:first-child,
	.add-update-preview .el-form-item.btn .subBtn:first-child {
		background: linear-gradient(135deg, #0ea5e9 0%, #7c3aed 100%) !important;
		color: #fff !important;
		box-shadow: 0 2px 12px rgba(14, 165, 233, 0.25);
	}
	.add-update-preview .el-form-item.btn span:first-child:hover {
		box-shadow: 0 4px 20px rgba(14, 165, 233, 0.4) !important;
		transform: translateY(-1px);
	}
	.add-update-preview .el-form-item.btn span:last-child,
	.add-update-preview .el-form-item.btn .subBtn:last-child {
		background: rgba(100, 116, 139, 0.12) !important;
		color: #94a3b8 !important;
		border: 1px solid rgba(100, 116, 139, 0.2) !important;
	}
	.add-update-preview .el-form-item.btn span:last-child:hover {
		background: rgba(100, 116, 139, 0.2) !important;
		color: #e2e8f0 !important;
	}

	/* detail-form-content 也统一处理 */
	.detail-form-content .el-form-item__label {
		color: #94a3b8 !important;
	}

	/* ====== 列表页内联浅色背景全局覆盖 ====== */
	.main-content[style] {
		padding: 0 !important;
	}
	.main-content > div[style] {
		background: rgba(30, 41, 59, 0.6) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		padding: 16px !important;
		border-radius: 14px !important;
	}

	.main-content .center-form-pv[style] {
		background: rgba(30, 41, 59, 0.6) !important;
		border: 1px solid rgba(255,255,255,0.06) !important;
		backdrop-filter: blur(12px);
	}

	/* 表格暗色适配 */
	.main-content .el-table,
	.main-content .el-table__header-wrapper,
	.main-content .el-table__body-wrapper {
		background: transparent !important;
	}
	.main-content .el-table::before,
	.main-content .el-table::after {
		background: transparent !important;
	}
	.main-content .el-table th,
	.main-content .el-table th.el-table__cell {
		background: rgba(15, 23, 42, 0.5) !important;
		color: #e2e8f0 !important;
		border-color: rgba(71, 85, 105, 0.25) !important;
	}
	.main-content .el-table th .cell {
		color: #e2e8f0 !important;
		font-weight: 600 !important;
		font-size: 13px !important;
	}
	.main-content .el-table td,
	.main-content .el-table td.el-table__cell {
		background: transparent !important;
		color: #cbd5e1 !important;
		border-color: rgba(71, 85, 105, 0.15) !important;
	}
	.main-content .el-table td .cell {
		color: #cbd5e1 !important;
	}
	.main-content .el-table tr {
		background: transparent !important;
	}
	.main-content .el-table__body tr:hover > td {
		background: rgba(14, 165, 233, 0.06) !important;
	}
	.main-content .el-table__empty-block {
		background: transparent !important;
	}
	.main-content .el-table__empty-text {
		color: #64748b !important;
	}

	/* 分页暗色适配 */
	.main-content .el-pagination {
		color: #94a3b8 !important;
	}
	.main-content .el-pagination .el-pagination__total {
		color: #94a3b8 !important;
	}
	.main-content .el-pagination .btn-prev,
	.main-content .el-pagination .btn-next {
		background: transparent !important;
		color: #94a3b8 !important;
		border-color: rgba(71, 85, 105, 0.3) !important;
	}
	.main-content .el-pagination .el-pager .number {
		background: transparent !important;
		color: #94a3b8 !important;
		border-color: rgba(71, 85, 105, 0.3) !important;
	}
	.main-content .el-pagination .el-pager .number.active,
	.main-content .el-pagination .el-pager .number:hover {
		background: linear-gradient(135deg, #0ea5e9, #7c3aed) !important;
		color: #fff !important;
		border-color: transparent !important;
	}
	.main-content .el-pagination .el-pagination__sizes .el-input__inner {
		background: rgba(15, 23, 42, 0.8) !important;
		color: #94a3b8 !important;
		border-color: rgba(71, 85, 105, 0.3) !important;
	}
	.main-content .el-pagination .el-pagination__jump {
		color: #94a3b8 !important;
	}
	.main-content .el-pagination .el-pagination__jump .el-input__inner {
		background: rgba(15, 23, 42, 0.8) !important;
		color: #94a3b8 !important;
		border-color: rgba(71, 85, 105, 0.3) !important;
	}

	/* 表格内操作按钮 */
	.main-content .el-table td .view,
	.main-content .el-table td .edit,
	.main-content .el-table td .del {
		background: none !important;
	}
	.main-content .el-table td .view {
		color: #38bdf8 !important;
	}
	.main-content .el-table td .edit {
		color: #fbbf24 !important;
	}
	.main-content .el-table td .del {
		color: #f87171 !important;
	}

	/* ====== 紧凑表格：一页显示，不下拉 ====== */
	.main-content .el-table {
		font-size: 13px !important;
	}
	.main-content .el-table th .cell,
	.main-content .el-table td .cell {
		padding: 4px 6px !important;
		line-height: 1.3 !important;
	}
	.main-content .el-table th,
	.main-content .el-table th.el-table__cell {
		padding: 6px 0 !important;
		font-size: 12px !important;
	}
	.main-content .el-table td,
	.main-content .el-table td.el-table__cell {
		padding: 4px 0 !important;
	}
	/* 表格图片紧凑 */
	.main-content .el-table td img {
		max-width: 44px !important;
		max-height: 44px !important;
		border-radius: 6px !important;
		object-fit: cover !important;
		vertical-align: middle !important;
	}
	/* 搜索表单紧凑 */
	.center-form-pv {
		padding: 10px 20px !important;
		margin-bottom: 8px !important;
	}
	.center-form-pv .el-form-item {
		margin-bottom: 0 !important;
	}
	/* 操作按钮行紧凑 */
	.main-content .actions {
		padding: 0 20px 10px 20px !important;
	}
	.main-content .actions .el-button {
		padding: 6px 14px !important;
		font-size: 12px !important;
	}
	/* 分页紧凑 */
	.main-content .el-pagination {
		padding: 8px 0 !important;
		margin-top: 4px !important;
	}
	/* 表格操作列按钮紧凑 */
	.main-content .el-table td .el-button--mini {
		padding: 4px 8px !important;
		font-size: 12px !important;
	}
	/* 整体内容区域不超出视口 */
	.main-content .el-table__body-wrapper {
		overflow-y: auto !important;
	}
	/* 搜索行内输入框紧凑 */
	.center-form-pv .el-input__inner,
	.center-form-pv .el-select .el-input__inner {
		height: 34px !important;
		line-height: 34px !important;
		font-size: 13px !important;
	}
	/* tab 标签页紧凑 */
	.main-content .el-tabs__header {
		margin-bottom: 8px !important;
	}
</style>