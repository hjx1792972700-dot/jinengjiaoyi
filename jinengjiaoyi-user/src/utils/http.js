import axios from 'axios'
import storage from '@/common/storage'
import config from '@/config/config'

const http = axios.create({
	timeout: 1000 * 86400,
	withCredentials: true,
	baseURL: config.baseUrl,
	headers: {
		'Content-Type': 'application/json; charset=utf-8'
	}
})

http.interceptors.request.use(config => {
	config.headers['Token'] = localStorage.getItem('frontToken') || storage.get('Token')
	return config
}, error => {
	return Promise.reject(error)
})

http.interceptors.response.use(response => {
	if (response.data && response.data.code === 401) {
		storage.set("Token", "")
		window.location.href = '#/login'
	}
	return response
}, error => {
	return Promise.reject(error)
})

export default http
