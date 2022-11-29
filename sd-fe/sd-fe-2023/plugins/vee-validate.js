import Vue from 'vue'
import {
    ValidationObserver,
    ValidationProvider,
    extend
} from 'vee-validate'
import { required, email, min, confirmed } from 'vee-validate/dist/rules'

extend('required', {
    ...required,
    message: 'Đây là trường bắt buộc!'
})
extend('email', email)
extend('min', {
    ...min,
    message: (_, values) => `This field is ${values?.length} character required`
})
extend('confirmed', confirmed)

extend('isDuplicate', {
    params: ['target'],
    validate(value, target) {
        if (target.target instanceof Array) {
            const arrDuplicate = target.target.filter(i => i.productId === value)
            return arrDuplicate.length <= 1
        }
        return true
    },
    message: 'Sản phẩm này đã được chọn!'
})
Vue.component('ValidationProvider', ValidationProvider)
Vue.component('ValidationObserver', ValidationObserver)