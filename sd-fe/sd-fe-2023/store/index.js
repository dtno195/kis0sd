const actions = {
    nuxtServerInit({ commit, state, dispatch }, { app, store, route, req, res, error, redirect }) {
        const user = app.$cookiz.get('user')
        const token = app.$cookiz.get('token')
        if (user && token) {
            commit('auth/SET_USER', user)
            commit('auth/SET_TOKEN', token)
        }
    }
}

export default {
    actions
}