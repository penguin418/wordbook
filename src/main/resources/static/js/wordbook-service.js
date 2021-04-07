/**
 * @author penguin penguin418@naver.com
 * @version 0.6
 * @file wordbook 을 관리하고 서버와 통신하기 위한 서비스입니다
 * @requires jquery cookie
 */
if (window.wordbookService) {
    console.log('duplicated import')
}
const wordbookService = (function () {

    const URLs = (function () {
        return {
            home: () => {
                return '/'
            },
            newWordbook: () => {
                return '/wordbooks/create'
            },
            wordbookList: () => {
                return '/wordbooks/list'
            },
            wordbookItem: (id) => {
                return '/wordbooks/' + parseInt(id)
            },
            wordbookItemDetail: (id) => {
                return '/wordbooks/' + parseInt(id) + '/update'
            },
            newAccount: () => {
                return '/accounts/create'
            },
            loginAccount: () => {
                return '/accounts/login'
            },
            accountDetail: () => {
                return '/accounts/detail'
            }
        }
    })()

    /**
     * 어떤 프로세스가 종료되었을 때 실행할 함수를 사용할 수 있습니다
     * 프로세스를 실행할때, onSuccessFunction, onFailedFunction 을 초기화하므로
     * onLoad, onLoadFailed 는 실행할 프로세스보다 나중에 지정되어야 합니다
     * @class
     * @classdesc 각 변수를 채운뒤, saveToServer() 메소드를 통해 서버에 저장합니다
     */
    const Notifiable = class {

        /**
         * 프로세스 상태를 초기화 함
         * state {STATUS} READY: 프로세스가 끝난 상태, LOADING: 작동중, SUCCESS: 성공 후 대기, FAILED: 실패 후 대기
         * onSuccessFunction {function} 성공 시 실행 할 함수
         * onFailedFunction {function} 실패 시 실행 할 함수
         * @this Notifiable
         */
        constructor() {
            this.STATE = {
                READY: 0,
                PROCESSING: 1,
                SUCCESS: 2,
                FAILED: 3,
            }
            this.status = this.STATE.READY
            this.onSuccessFunction = null
            this.onFailedFunction = null
        }

        /**
         * 프로세스 종료 시 실행할 함수를 등록하는 콜백
         * 프로세스가 실행 중인 경우, 함수를 등록 -> endProcess() 때 실행됨
         * 프로세스가 성공(SUCCESS)한 경우, 함수를 실행 & 준비 상태(READY)로 변경
         * @param func 종료 시 실행할 함수
         * @this Notifiable
         */
        onLoad(func) {
            if (this.status) {
                this.onSuccessFunction = func
            } else if (this.status === this.STATE.SUCCESS) {
                func.call()
                this.status = this.STATE.READY
            }
        }

        /**
         * 프로세스 종료 시 실행할 함수를 등록하는 콜백
         * 프로세스가 실행 중인 경우 함수를 등록 -> endProcess() 때 실행됨
         * 프로세스가 성공(FAILED)한 경우, 함수를 실행 & 준비 상태(READY)로 변경
         * @param func 종료 시 실행할 함수
         * @this Notifiable
         */
        onLoadFailed(func) {
            if (this.status) {
                this.onFailedFunction = func
            } else if (this.status === this.STATE.FAILED) {
                func.call()
                this.status = this.STATE.READY
            }
        }

        /**
         * 프로세스 실행 전 호출하는 함수
         * 프로세스를 진행중인 상태(PROCESSING)로 변경
         * @this Notifiable
         */
        reportProcessing() {
            this.status = this.STATE.PROCESSING
            this.onSuccessFunction = null
            this.onFailedFunction = null
        }

        /**
         * 프로세스 종료시 호출하는 함수
         * 등록된 함수가 있을 경우, 실행 & 준비 상태(READY)로 만듬
         * 그렇지 않은 경우 놔둠 -> onLoad 때 실행됨
         * @this Notifiable
         */
        reportSuccess() {
            if (this.onSuccessFunction) {
                this.onSuccessFunction.call()
                this.status = this.STATE.READY
            }
        }

        /**
         * 프로세스 실패시 호출하는 함수
         * 등록된 함수가 있을 경우, 실행 & 준비 상태(READY)로 만듬
         * 그렇지 않은 경우 놔둠 -> onLoadFailed 때 실행됨
         * @this Notifiable
         */
        reportFail() {
            if (this.onSuccessFunction) {
                this.onFailedFunction.call()
                this.status = this.STATE.READY
            }
        }
    }

    /**
     * 새로운 wordbook을 생성하는 프로세스를 도와주는 클래스입니다
     * @class
     * @classdesc 각 변수를 채운뒤, saveToServer() 메소드를 통해 서버에 저장합니다
     */
    const NewWordbook = class extends Notifiable {

        constructor() {
            super()
            this.id = 0
            this.name = ''
            this.description = ''
            this.qaList = []
        }

        /**
         * wordbook name 을 지정합니다
         * @Param newName {string} 새로운 wordbook name
         * @this NewWordbook 현재 새로 만들어진 wordbook
         */
        setName(newName) {
            this.name = newName
        }

        /**
         * wordbook description 을 지정합니다
         * @Param newDescription {string} 새로운 wordbook description
         * @this NewWordbook 현재 새로 만들어진 wordbook
         */
        setDescription(newDescription) {
            this.description = newDescription
        }

        /**
         * Question and Answer 를 추가합니다
         * @Param question {string} 새로운 Question
         * @Param answer {string} 새로운 Answer
         * @this NewWordbook 현재 새로 만들어진 wordbook
         */
        addQA(question, answer, id = 0) {
            this.qaList.push({
                'question': question.toString(),
                'answer': answer.toString()
            })
        }

        /**
         * 서버에 현재 wordbook을 저장합니다
         * @this NewWordbook 현재 새로 만들어진 wordbook
         * @throws EmptyWordbookException {EmptyWordbookException} 1개 이상의 QA가 있어야 합니다
         * @returns {Promise<Response>} 다음 동작을 수행할 수 있습니다
         */
        saveToServer() {
            if (this.qaList.length < 1) {
                throw new EmptyWordbookException()
            }
            return fetch('/api/wordbooks', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    name: this.name,
                    description: this.description,
                    qa_list: this.qaList
                }),
            })
        }
    }

    /**
     * 이미 있는 wordbook을 확인하고 수정하는 것을 도와주는 클래스
     * @class
     * @classdesc 각 변수를 채운뒤, saveToServer() 메소드를 통해 서버에 저장합니다
     */
    const CurrentWordbook = class extends NewWordbook {
        loading = false
        onLoadFunction = null

        /**
         * 서버에서 하나의 wordbook을 찾아옵니다
         * @param id
         * @this CurrentWordbook 현재 객체에 저장된 wordbook
         * @returns {Promise<void>} 다음 동작을 수행할 수 있습니다
         */
        findOne(id) {
            if (!Number.isInteger(id)) {
                throw new TypeError('id must be a Number')
            }
            super.reportProcessing()
            return fetch('/api/wordbooks/' + id, {
                method: 'GET',
            }).then((response) => {
                if (!response.ok) {
                    throw new Error(response.statusText)
                }
                return response.json()
            }).then(data => {
                console.log(data)
                this.id = data.id
                this.name = data.name
                this.description = data.description
                this.qaList = data.qa_list
                super.reportSuccess()
            }).catch((error) => {
                console.log(error)
                super.reportFail()
            })
        }

        /**
         * 서버에 현재 wordbook을 저장합니다
         * @this CurrentWordbook 현재 객체에 저장된 wordbook
         * @returns {Promise<Response>} 다음 동작을 수행할 수 있습니다
         */
        saveToServer() {
            super.saveToServer()
            return fetch('/api/wordbooks', {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    id: this.id,
                    name: this.name,
                    description: this.description,
                    qa_list: this.qaList
                }),
            })
        }

        /**
         * 서버에서 현재 wordbook을 삭제합니다
         * @this CurrentWordbook 현재 객체에 저장된 wordbook
         * @returns {Promise<void>} 다음 동작을 수행할 수 있습니다
         */
        deleteFromServer() {
            return fetch('/api/wordbooks/' + this.id, {
                method: 'DELETE',
            }).then((response) => console.log(response))
        }
    }

    /**
     * Wordbooks 를 가져오는 것을 도와주는 클래스
     * @class
     * @classdesc 각 변수를 채운뒤, saveToServer() 메소드를 통해 서버에 저장합니다
     */
    const Wordbooks = class extends Notifiable {

        constructor() {
            super()
            this.contents = []
            this.length = 0
            this.page = 0
        }

        /**
         * 서버에서 모든 wordbooks를 가져옵니다
         * @todo 페이지네이션 구현
         * @this Wordbooks 현재 객체에 저장된 wordbook
         * @returns {Promise<void>} 다음 동작을 수행할 수 있습니다
         */
        findAll() {
            super.reportProcessing()
            return fetch('/api/wordbooks', {
                method: "GET",
            }).then((response) => {
                if (!response.ok) {
                    throw new Error(response.statusText)
                }
                return response.json()
            })
                .then((data) => {
                    this.contents = data.contents
                    this.length = data.length
                    this.page = data.page
                    super.reportSuccess()
                }).catch((error) => {
                    console.log(error)
                    super.reportFail()
                })
        }
    }

    const getAccount = function () {
        if (accountInstance === undefined) {
            accountInstance = new AccountClass()
        }
        return accountInstance
    }
    let accountInstance
    const AccountClass = class extends Notifiable {

        constructor() {
            super()
            this.id = ''
            this.nickname = ''
            this.email = ''
            this.LOGGED_IN = 'WORDBOOK_LOGGED_IN'
        }

        isLoggedIn() {
            const token = CookieUtil.getCookie(this.LOGGED_IN)
            console.log('cookie=', token)
            return (typeof token !== "undefined")
        }

        create(nickname, email, password) {
            super.reportProcessing()
            return fetch('/api/accounts', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    nickname, email, password
                })
            }).then((response) => {
                if (!response.ok) {
                    throw new Error(response.statusText)
                }
                return response.json()
            }).then((data) => {
                super.reportSuccess()
            }).catch((error) => {
                console.log(error)
                super.reportFail()
            })
        }

        login(email, password) {
            super.reportProcessing()
            return fetch('/api/accounts/login', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    email: email.toString(),
                    password: password.toString()
                }),
            }).then((response) => {
                if (!response.ok) {
                    console.warn(response)
                    throw new Error(response.statusText)
                }
                return response.json()
            }).then((data) => {
                console.log(data)
                this.id = data.account_id
                this.nickname = data.nickname
                this.email = data.email
                CookieUtil.setCookie(this.LOGGED_IN, true)
                super.reportSuccess()
            }).catch((error) => {
                console.log(error)
                super.reportFail()
            })
        }


        logout() {
            super.reportProcessing()
            return fetch('/api/accounts/logout', {
                method: 'POST'
            }).then(() => {
                CookieUtil.deleteCookie(this.LOGGED_IN)
                super.reportSuccess()
            })
        }

        getMyAccount() {
            super.reportProcessing()
            return fetch('/api/accounts/my', {
                method: 'GET'

            }).then((response) => {
                if (!response.ok) {
                    console.warn(response)
                    throw new Error(response.statusText)
                }
                return response.json()
            }).then((data) => {
                console.log(data)
                this.id = data.account_id
                this.nickname = data.nickname
                this.email = data.email
                super.reportSuccess()
            }).catch((error) => {
                console.log(error)
                super.reportFail()
            })
        }

        update(nickname, email, password, new_password) {
            super.reportProcessing()
            return fetch('/api/accounts/my', {
                method: 'PUT',
                body: JSON.stringify({
                    nickname, email, password, new_password
                })

            }).then((response) => {
                if (!response.ok) {
                    console.warn(response)
                    throw new Error(response.statusText)
                }
                return response.json()
            }).then((data) => {
                console.log(data)
                this.id = data.account_id
                this.nickname = data.nickname
                this.email = data.email
                super.reportSuccess()
            }).catch((error) => {
                console.log(error)
                super.reportFail()
            })
        }
    }

    return {
        URLs,
        NewWordbook,
        CurrentWordbook,
        Wordbooks,
        getAccount
    }
})();

/**
 * 빈 단어장일 때 발생하는 에러
 */
function EmptyWordbookException() {
}

EmptyWordbookException.prototype.toString = function () {
    return 'At least one QA must be included'
}

