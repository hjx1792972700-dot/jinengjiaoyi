import { createStore } from 'vuex'
import tagsView from './modules/tagsView'

const store = createStore({
	modules: {
		tagsView,
	}
})

export default store
