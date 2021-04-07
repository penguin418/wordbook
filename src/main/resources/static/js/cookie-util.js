const CookieUtil = (function () {
    /**
     * 쿠키 설정
     * @param name {string} 쿠키 이름
     * @param value {string,boolean,number} 쿠키 값
     * @param timeout 종료시간을 정합니다
     * @param timeout.hour 시간
     * @param timeout.day  일
     * @param timeout.week 주
     */
    const setCookie = function (name, value, timeout = undefined) {
        const expires = calcTimeout(timeout)
        document.cookie = name + '=' + value + '; expires=' + expires.toUTCString() + '; path=/';
    };

    /**
     * 쿠키 조회
     * @param name {string} 쿠키 이름
     * @returns {string|null}
     */
    const getCookie = function (name) {
        const cookieName = name + "="
        const cookies = decodeURIComponent(document.cookie).split('; ')
        let res;
        cookies.forEach(val => {
            if (val.indexOf(cookieName) === 0) {
                // 마지막에 설정한 값
                res = val.substring(cookieName.length)
            }
        })
        return res;
    };

    /**
     * 쿠키 삭제
     * @param name {string} 쿠키 이름
     */
    const deleteCookie = function (name) {
        setCookie(name, "", {hour: 0})
    }

    /**
     * 소멸 시간을 계산합니다
     * @param timeout 종료시간을 정합니다
     * @param timeout.hour 시간
     * @param timeout.day  일
     * @param timeout.week 주
     */
    const calcTimeout = function (timeout = undefined) {
        const date = new Date();
        if (!timeout) {
            date.setTime(date.getTime() + 1000 * 60 * 60 * 24);
        } else if (!!timeout.hour) {
            date.setTime(date.getTime() + 1000 * 60 * 60 * timeout.hour);
        } else if (!!timeout.day) {
            date.setTime(date.getTime() + 1000 * 60 * 60 * 24 * timeout.day);
        } else if (!!timeout.week) {
            date.setTime(date.getTime() + 1000 * 60 * 60 * 24 * 7 * week);
        }
        return date
    }

    return {
        setCookie,
        getCookie,
        deleteCookie,
    }
})()