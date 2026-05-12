const path = require('path')

function resolve(dir) {
	return path.join(__dirname, dir)
}

module.exports = {
	publicPath: '/',
	lintOnSave: false,
	configureWebpack: {
		resolve: {
			alias: {
				'@': resolve('src')
			}
		}
	},
	devServer: {
		host: "0.0.0.0",
		port: 8081,
		hot: true,
		https: false,
		proxy: {
			'/': {
				target: 'http://localhost:48081/',
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
