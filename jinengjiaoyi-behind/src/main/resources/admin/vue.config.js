// var webpack = require('webpack');
//vue2
const path = require('path')
const AutoDllPlugin = require('autodll-webpack-plugin');
function resolve(dir) {
	return path.join(__dirname, dir)
}
function publicPath(){
	if (process.env.NODE_ENV == 'production') {
		return "././";
	} else {
		return "/";
	}
}
// vue.config.js
module.exports = {
	// publicPath:"././",
	publicPath: publicPath(),
	transpileDependencies: [
		/vue-quill-editor-support-insert-table/,
		/quill/,
		/quill-better-table/
	],
	// 国际化配置 使用其它语言，默认情况下中文语言包依旧是被引入的
	configureWebpack: {
		plugins: [
			// ...其他插件
			new AutoDllPlugin({
				inject: true, // 自动注入生成的DLL文件到HTML中
				filename: '[name].dll.js', // DLL文件名格式
				entry: {
					vendor: ['vue', 'vue-router', 'vuex'] // 需要单独打包的第三方库
				}
			})
		],
		resolve: {
			alias: {
				'@': resolve('src')
			}
		}
	},
lintOnSave: false,
	devServer: {
		host: "0.0.0.0",
		port: 8081,
		hot: true,
		https: false,
		watchOptions: {
			poll: 1000,
			ignored: /node_modules/
		},
		proxy: {
			'/': {
				target: 'http://localhost:8082/',
				changeOrigin: true,
				secure: false,
				ws: false
			}
		}
	},
chainWebpack(config) {
	config.module
	  .rule('svg')
	  .exclude.add(resolve('src/icons'))
	  .end()
	config.module
	  .rule('icons')
	  .test(/\.svg$/)
	  .include.add(resolve('src/icons'))
	  .end()
	  .use('svg-sprite-loader')
	  .loader('svg-sprite-loader')
	  .options({
		symbolId: 'icon-[name]'
	  })
	  .end()
}
}
