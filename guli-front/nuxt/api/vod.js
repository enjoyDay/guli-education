import request from '@/utils/request'
export default {
    getPlayVideoUrl(vid) {
        return request({
            url: `/vodservice/edu-video/getPlayVideoUrl/${vid}`,
            method: 'get'
        })
    }

}