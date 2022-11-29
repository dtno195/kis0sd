const state = () => ({
    user: null,
    token: null
})

const getters = {
    getToken(state) {
        return state.token
    },
    getUser(state) {
        return state.user
    }
}

const mutations = {
    SET_USER(state, user) {
        state.user = user
    },
    SET_TOKEN(state, token) {
        state.token = token
    },
    LOGOUT(state) {
        state.user = ''
        state.token = ''
    }
}

const actions = {}

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions
}