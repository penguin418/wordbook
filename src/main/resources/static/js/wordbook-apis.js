const WordbookApis = (function () {
    /**
     * @typedef AccountCreateDto
     * @property nickname {string}
     * @property email {string}
     * @property password {string}
     */

    /**
     * @typedef AccountLoginDto
     * @property email {string}
     * @suppress password {string}
     */

    /**
     * @typedef AccountInfoDto
     * @property account_id {number}
     * @property nickname {string}
     * @property email {string}
     */

    /**
     * @typedef AccountUpdateDto
     * @property account_id {number}
     * @property nickname {string}
     * @property email {string}
     * @suppress password {string}
     * @suppress new_password {string}
     */

    /**
     * @typedef ErrorType
     * @property code {number}
     * @property message {string}
     */

    /**
     *
     * @param response {Response}
     * @param resolve {function}
     * @param reject {function}
     */
    function resolveOrReject(response, resolve, reject) {
        if (!response.ok) {
            console.warn(response)
            response.json().then((result)=>reject(result))
        }else {
            response.json().then((result) => resolve(result))
        }
    }

    function resolveOrRejectNoContent(response, resolve, reject) {
        if (!response.ok) {
            console.warn(response)
            reject()
        }else {
            resolve()
        }
    }

    /**
     * 계정 생성
     * @param requestBody {AccountCreateDto}
     * @returns {Promise<AccountInfoDto>|Promise<ErrorType>}
     */
    const createAccount = function (requestBody) {
        return new Promise((resolve, reject) => {
            fetch('/api/accounts', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    nickname: requestBody.nickname,
                    email: requestBody.email,
                    password: requestBody.password
                })
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        })
    }

    /**
     * 계정 갱신
     * @param requestBody {AccountUpdateDto}
     * @returns {Promise<AccountInfoDto>|Promise<ErrorType>}
     */
    const updateAccount = function (requestBody) {
        return new Promise((resolve, reject) => {
            fetch('/api/accounts', {
                method: 'PUT',
                body: JSON.stringify({
                    account_id: requestBody.account_id,
                    nickname: requestBody.nickname,
                    email: requestBody.email,
                    password: requestBody.password,
                    new_password: requestBody.new_password
                })
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        })
    }

    /**
     * 내 계정 조회
     * @returns {Promise<AccountInfoDto>|Promise<ErrorType>}
     */
    const getMyAccount = function () {
        return new Promise((resolve, reject) => {
            fetch('/api/accounts/my', {
                method: 'GET',
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        })
    }

    /**
     * 로그인
     * @param requestBody {AccountLoginDto}
     * @returns {Promise<AccountInfoDto>|Promise<ErrorType>}
     */
    const loginAccount = function (requestBody) {
        return new Promise((resolve, reject) => {
            fetch('/api/accounts/login', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: requestBody.email,
                    password: requestBody.password
                }),
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        })
    }

    /**
     * 로그아웃
     * @returns {Promise<void*>|Promise<ErrorType>}
     */
    const logoutAccount = function () {
        return new Promise((resolve, reject) => {
            return fetch('/api/accounts/logout', {
                method: 'POST'
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        })
    }

    /**
     * @typedef QaCreateDto
     * @property question {string}
     * @property answer {string}
     */

    /**
     * @typedef QaUpdateDto
     * @property qa_id {number}
     * @property question {string}
     * @property answer {string}
     */

    /**
     * @typedef QaResponseDto
     * @property qa_id {number}
     * @property question {string}
     * @property answer {string}
     */

    /**
     * @typedef WordbookCreateDto
     * @property account {AccountInfoDto}
     * @property name {string}
     * @property description {string
     * @property qa_list {QaCreateDto[]}
     */

    /**
     * @typedef WordbookUpdateDto
     * @property wordbook_id {number}
     * @property account {AccountInfoDto}
     * @property name {string}
     * @property description {string
     * @property qa_list {QaUpdateDto[]}
     */

    /**
     * @typedef WordbookDetailDto
     * @property wordbook_id {number}
     * @property account {AccountInfoDto}
     * @property name {string}
     * @property description {string
     * @property qa_list {QaResponseDto[]}
     */

    /**
     * @typedef WordbookItemDto
     * @property wordbook_id {number}
     * @property account {AccountInfoDto}
     * @property name {string}
     * @property description {string
     */

    /**
     * @typedef MyWordbookResultSetDto
     * @property total {number}
     * @property page {number}
     * @property contents {WordbookItemDto[]}
     */

    /**
     * @typedef WordbookQueryDto
     * @property name {string}
     */

    /**
     * 단어장 생성
     * @param requestBody {WordbookCreateDto}
     * @returns {Promise<WordbookDetailDto>|Promise<ErrorType>}
     */
    const createWordbook = function (requestBody) {
        return new Promise((resolve, reject) => {
            fetch('/api/wordbooks', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    name: requestBody.name,
                    description: requestBody.description,
                    qa_list: requestBody.qa_list,
                    account: requestBody.account
                })
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        })
    }

    /**
     * 단어장 갱신
     * @param requestBody {WordbookUpdateDto}
     * @returns {Promise<WordbookDetailDto>|Promise<ErrorType>}
     */
    const updateWordbook = function (requestBody) {
        return new Promise((resolve, reject) => {
            fetch('/api/wordbooks/' + requestBody.wordbook_id, {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    wordbook_id: requestBody.wordbook_id,
                    account: requestBody.account,
                    name: requestBody.name,
                    description: requestBody.description,
                    qa_list: requestBody.qa_list,
                })
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        })
    }

    /**
     * 단어장 조회
     * @param wordbookId
     * @returns {Promise<WordbookDetailDto>|Promise<ErrorType>}
     */
    const getWordbook = function (wordbookId){
        return new Promise(((resolve, reject) => {
            fetch(`/api/wordbooks/${id}`, {
                method: 'GET',
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        }))
    }

    /**
     * 단어장 삭제
     * @param wordbookId
     * @returns {Promise<void*>|Promise<ErrorType>}
     */
    const removeWordbook = function(wordbookId){
        return new Promise(((resolve, reject) => {
            fetch(`/api/wordbooks/${id}`, {
                method: 'DELETE',
            }).then((response) => {
                console.log('delete response=', response)
                resolveOrRejectNoContent(response, resolve, reject)
            })
        }))
    }


    /**
     * 쿼리 스트링 만들기
     * @param requestBody {WordbookQueryDto}
     * @returns queryString {string}
     */
    function makeQuery(requestBody=undefined){
        return ''
    }

    /**
     * 모든 단어장
     * @param requestBody {WordbookQueryDto}
     * @returns {Promise<MyWordbookResultSetDto>|Promise<ErrorType>}
     */
    const getWordbookList = function (requestBody=undefined) {
        return new Promise((resolve, reject) => {
            fetch('/api/wordbooks?' + makeQuery(requestBody), {
                method: 'GET',
            }).then((response) => {
                resolveOrReject(response, resolve, reject)
            })
        })
    }

    return {
        createAccount,
        updateAccount,
        getMyAccount,
        loginAccount,
        logoutAccount,
        createWordbook,
        updateWordbook,
        removeWordbook,
        getWordbookList,
        getWordbook,
    }
})()