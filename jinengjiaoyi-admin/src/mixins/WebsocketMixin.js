import base from '@/utils/base'
import storage from '@/utils/storage'
export const WebsocketMixin = {
	mounted() {
		// this.initWebSocket();
	},
	destroyed: function() {
		// 离开页面生命周期函数
		// this.websocketOnclose();
	},
	methods: {
		initWebSocket(toId) {
			console.log("------------WebSocket连接成功");
			var userId = storage.get('userid')
			var url = `${base.get().url.replace(/^http/, "ws")}ws?user_id=` + userId + '&to_id=' + toId;
			if (this.websock == null) {
				this.websock = new WebSocket(url);
				this.websock.onopen = this.websocketOnopen;
				this.websock.onerror = this.websocketOnerror;
				this.websock.onmessage = this.websocketOnmessage;
			}

		},
		websocketOnopen: function() {
			console.log("WebSocket连接成功");
		},
		websocketOnerror: function(e) {
			console.log("WebSocket连接发生错误");
			this.reconnect();
		},
		websocketOnclose: function(e) {
			if (this.websock) {
				try {
					this.websock.close(1000)
				} catch (err) {
					// ignore
				}
				this.websock = null
			}
			console.log('关闭了')
		},
		websocketOnmessage:function(e) {
			console.log('收到了',e)
		},
		websocketSend(text) {
			try {
				this.websock.send(text);
			} catch (err) {
				console.log("send failed (" + err.code + ")");
			}
		},
		reconnect() {
		},
	}

}
