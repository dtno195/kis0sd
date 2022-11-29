import { Notification } from 'element-ui'

export default function({ $axios, app, store, redirect }) {
    $axios.onRequest((config) => {
        const token = localStorage.getItem('token') || app.$cookiz.get('token') || store.state.auth.token
            // Add X-Access-Token header to every request, you can add other custom headers here
        if (token) {
            config.headers['Authorization'] = 'Bearer ' + token;
        }
        return config
    })
    $axios.onRequestError((error) => {
        Promise.reject(error)
    })

    $axios.onResponse((response) => {
        if (response.data.code && response.data.message) {
            Notification.error({
                type: "error",
                message: response.data.message,
                position: "bottom-right",
            });
            return Promise.reject(response.data.message);
        } else {
            return response;
        }
    })
    $axios.onResponseError((error) => {
        if (error && error.message) {
            Notification.error({
                type: "error",
                message: error.message,
                position: "bottom-right",
            });
        }
        if (error && error.response && error.response.status && error.response.status === 401) {
            redirect('/login');
        }
        return Promise.reject(error)
    })
}