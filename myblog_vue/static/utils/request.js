import axios from 'axios';
// 通用公用方法
const req = (method, url, params) => {
    return axios({
        method: method,
        url: url,
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        data: params,
        traditional: true,
        transformRequest: [
            function(data) {
                // console.log("req parms ::: ", data);
                return data;
                // let ret = ''
                // for (let it in data) {
                //     // ret +=
                //     //     encodeURIComponent(it) +
                //     //     '=' +
                //     //     encodeURIComponent(data[it]) +
                //     //     '&'
                //     ret +=
                //         it +
                //         '=' +
                //         data[it] +
                //         '&'

                // }
                // console.log("ret parms ::: ", ret);
                // return ret
            }
        ]
    }).then(res => res.data);
};

export const httpget = (url) => { return req("get", url) }

export const httppost = (url, params) => {
    return req("post", url, params)
};