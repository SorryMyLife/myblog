export function setCookie(c_name, value, expire) {
    var date = new Date()
    date.setSeconds(date.getSeconds() + expire)
    document.cookie = c_name + "=" + escape(value) + "; expires=" + date.toGMTString()
        //console.log(document.cookie)
}

export function getCookie(c_name) {
    if (document.cookie.length > 0) {
        let c_start = document.cookie.indexOf(c_name + "=")
        if (c_start != -1) {
            c_start = c_start + c_name.length + 1
            let c_end = document.cookie.indexOf(";", c_start)
            if (c_end == -1) c_end = document.cookie.length
            return unescape(document.cookie.substring(c_start, c_end))
        }
    }
    return ""
}

export function delCookie(c_name) {
    setCookie(c_name, "", -1)
}

export function clearAllCookie() {
    var keys = document.cookie.match(/[^ =;]+(?=\=)/g);
    if (keys) {
        for (var i = keys.length; i--;)
            document.cookie = keys[i] + '=0;expires=' + new Date(0).toUTCString() + ";path=/";
    }
}

export function getUID(that) {
    let uid = that.$route.params
    if (uid.uid !== undefined) {
        return uid.uid;
    }

    let dc = document.cookie.split(';');
    for (var i = 0; i < dc.length; i++) {
        var aa = dc[i].split("=")
        if (aa[0].replace(" ", "") === "afff") {
            return aa[1].replace(" ", "")
        }
    }
    return "";
}