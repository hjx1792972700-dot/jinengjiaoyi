import axios from 'axios'
import storage from '@/utils/storage'
import base from '@/utils/base'

const http = axios.create({
	timeout: 1000 * 86400,
	withCredentials: true,
	baseURL: 'http://localhost:48081/',
	headers: {
		'Content-Type': 'application/json; charset=utf-8'
	}
})

http.interceptors.request.use(config => {
	config.headers['Token'] = storage.get('Token')
	return config
}, error => {
	return Promise.reject(error)
})

http.interceptors.response.use(response => {
	if (response.data && response.data.code === 401) {
		storage.set("Token", "")
		window.location.href = base.get().indexUrl
	}
	return response
}, error => {
	return Promise.reject(error)
})

export default http
