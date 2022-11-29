import { setClient } from '@/services/core/api-client'

export default ({ app, store }) => {
  setClient(app.$axios)
}