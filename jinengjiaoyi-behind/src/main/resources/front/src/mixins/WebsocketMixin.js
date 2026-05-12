import config from '@/config/config'
export const WebsocketMixin = {
	mounted() {
		// this.initWebSocket();
	},
	unmounted() {
		// this.websocketOnclose();
	},
	methods: {
		initWebSocket(toId) {
			var userId = localStorage.getItem('frontUserid')
			let baseUrl = config.baseUrl.replace(/^http/,'ws') 
			var url = `${baseUrl}ws?user_id=` + userId + '&to_id=' + toId;
			if (this.websock == null) {
				this.websock = new WebSocket(url);
				this.websock.onopen = this.websocketOnopen;
				this.websock.onerror = this.websocketOnerror;
				this.websock.onmessage = this.websocketOnmessage;
			}
		},
		websocketOnopen() {
		},
		websocketOnerror(e) {
			this.reconnect();
		},
		websocketOnclose(e) {
			this.websock.close(1000)
			this.websock = null
		},
		websocketOnmessage(e) {
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
