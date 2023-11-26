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
            if (!!this.onSuccessFunction) {
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
            if (!!this.onFailedFunction) {
                this.onFailedFunction.call()
                this.status = this.STATE.READY
            }
        }

        startSimpleProcess(process) {
            this.reportProcessing()
            process.then((data) => {
                this.reportSuccess()
            }).catch((error) => {
                this.reportFail()
            })
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
            this.account = {}
            this.name = ''
            this.description = ''
            this.qaList = []
        }

        setAccount(account) {
            this.account.account_id = account.accountId
            this.account.nickname = account.nickname
            this.account.email = account.email
        }

        getAccount() {
            return this.account
        }

        /**
         * wordbook name 을 지정합니다
         * @Param newName {string} 새로운 wordbook name
         * @this NewWordbook 현재 새로 만들어진 wordbook
         */
        setName(newName) {
            this.name = newName
        }

        getName() {
            return this.name
        }

        /**
         * wordbook description 을 지정합니다
         * @Param newDescription {string} 새로운 wordbook description
         * @this NewWordbook 현재 새로 만들어진 wordbook
         */
        setDescription(newDescription) {
            this.description = newDescription
        }

        getDescription() {
            return this.description
        }

        /**
         * Question and Answer 를 추가합니다
         * @Param question {string} 새로운 Question
         * @Param answer {string} 새로운 Answer
         * @this NewWordbook 현재 새로 만들어진 wordbook
         */
        addQa(question, answer, qa_id = 0) {
            this.qaList.push({
                'question': question.toString(),
                'answer': answer.toString(),
                'qa_id': qa_id
            })
        }

        setQaList(qaList) {
            this.qaList = qaList
        }

        getQaList() {
            return this.qaList
        }

        /**
         * 서버에 현재 wordbook을 저장합니다
         * @this NewWordbook 현재 새로 만들어진 wordbook
         * @throws EmptyWordbookException {EmptyWordbookException} 1개 이상의 Qa가 있어야 합니다
         * @returns {Promise<Response>} 다음 동작을 수행할 수 있습니다
         */
        saveToServer() {
            if (this.qaList.length < 1) {
                throw new EmptyWordbookException()
            }
            super.reportProcessing()
            const {createWordbook} = WordbookApis
            super.startSimpleProcess(
                createWordbook({
                    account: this.account,
                    name: this.name,
                    description: this.description,
                    qa_list: this.qaList
                })
            )
        }
    }

    /**
     * 이미 있는 wordbook을 확인하고 수정하는 것을 도와주는 클래스
     * @class
     * @classdesc 각 변수를 채운뒤, saveToServer() 메소드를 통해 서버에 저장합니다
     */
    const CurrentWordbook = class extends NewWordbook {

        constructor() {
            super();
            this.wordbookId = 0
        }

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
            const {getWordbook} = WordbookApis
            getWordbook(id).then(data => {
                console.log(data)
                this.wordbookId = data.wordbook_id
                this.name = data.name
                this.description = data.description
                this.qaList = data.qa_list
                this.account.account_id = data.account.account_id
                this.account.nickname = data.account.nickname
                this.account.email = data.account.email
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
            const {updateWordbook} = WordbookApis
            super.startSimpleProcess(
                updateWordbook({
                    wordbook_id: this.wordbookId,
                    name: this.name,
                    description: this.description,
                    qa_list: this.qaList,
                    account: this.account
                })
            )
        }

        /**
         * qa 를 삭제한다
         * @param question
         * @param answer
         * @param qa_id
         */
        // removeQa(question, answer, qaId) {
        //     this.qaDeleteList.push(qaId)
        // }

        /**
         * 서버에서 현재 wordbook을 삭제합니다
         * @this CurrentWordbook 현재 객체에 저장된 wordbook
         * @returns {Promise<void>} 다음 동작을 수행할 수 있습니다
         */
        deleteFromServer() {
            const {removeWordbook} = WordbookApis
            super.startSimpleProcess(
                removeWordbook(this.wordbookId)
            )
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
            const {getWordbookList} = WordbookApis
            getWordbookList().then((data) => {
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

    /**
     * 나의 wordbooks 를 관리하는 클래스
     * @class
     * @classdesc 나의 wordbooks 를 관리합니다
     */
    const MyWordbooks = class extends Wordbooks {
        /**
         * 나의 wordbooks 를 조회합니다
         * @this MyWordbooks
         * @returns {Promise<void>}
         */
        findMyWordbooks() {
            super.reportProcessing()

            const {getMyWordbooks} = WordbookApis
            getMyWordbooks(undefined).then((data) => {
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
    /**
     * Account 인스턴스 가져옴
     * - 싱글턴 구현
     * - 매 페이지를 로드할때 Account 를 초기화하며,
     *   이후 페이지 내에서 공유하기 위해 사용됨
     * - 처음 초기화할 때 정보를 초기화함
     * @returns {AccountClass}
     * FIXME: 로그인 확인하고 getMyAccount는 constructor로 옮기기로
     */
    const getAccount = function () {
        if (accountInstance === undefined) {
            accountInstance = new AccountClass()
            if (accountInstance.isLoggedIn()) {
                accountInstance.getMyAccount()
            }
        }
        return accountInstance
    }
    let accountInstance
    const AccountClass = class extends Notifiable {

        constructor() {
            super()
            this.accountId = ''
            this.nickname = ''
            this.email = ''
            this.LOGGED_IN = 'WORDBOOK_LOGGED_IN'
        }

        /**
         * 로그인 여부를 확인함
         * - 쿠키에 `REFRESH_TOKEN`이 존재하는 여부로 판단
         * - REFRESH_TOKEN이 있으면 로그인 갱신이 되므로 REFRESH_TOKEN이
         *   로그인 확인 수단으로 적절함
         * @returns {boolean}
         */
        isLoggedIn() {
            const token = CookieUtil.getCookie('REFRESH_TOKEN')
            return (typeof token !== "undefined")
        }

        /**
         * 가입
         * @param nickname {string} 이름
         * @param email {string} 이메일
         * @param password {string} 패스워드
         * @returns {Promise<void>}
         * TODO: 닉네임 중복과 email 중복을 구분해서 처리하기 위해 report 메소드 개선 예정
         */
        create(nickname, email, password) {
            const {createAccount} = WordbookApis
            super.startSimpleProcess(
                createAccount({nickname, email, password})
            )
        }

        /**
         * 로그인
         * @param email {string} 이메일
         * @param password {string} 패스워드
         * @returns {Promise<void>}
         */
        login(email, password) {
            super.reportProcessing()

            const {loginAccount} = WordbookApis
            loginAccount({email, password}).then((data) => {
                CookieUtil.setCookie(this.LOGGED_IN, JSON.stringify(data))
                super.reportSuccess()
            }).catch((error) => {
                super.reportFail()
            })
        }

        /**
         * 로그아웃
         * @returns {Promise<void>}
         */
        logout() {
            super.reportProcessing()

            const {logoutAccount} = WordbookApis
            logoutAccount().then(() => {
                CookieUtil.deleteCookie(this.LOGGED_IN)
                super.reportSuccess()
            })
        }

        /**
         * 내 정보
         */
        getMyAccount() {
            super.reportProcessing()

            // const {getMyAccount} = WordbookApis
            // getMyAccount().then((data) => {
            //     console.log(data)
            //     this.accountId = data.account_id
            //     this.nickname = data.nickname
            //     this.email = data.email
            //     super.reportSuccess()
            // }).catch((error) => {
            //     console.log(error)
            //     super.reportFail()
            // })
            try {
                const data = JSON.parse(CookieUtil.getCookie(this.LOGGED_IN))
                console.log(data)
                this.accountId = data.account_id
                this.nickname = data.nickname
                this.email = data.email
                super.reportSuccess()
            } catch (e) {
                super.reportFail()
            }

        }

        /**
         * 갱신
         * - 서버에선 email, password로 현재 갱신요청이 로그인된 사용자의 요청인지 검증함
         * @param nickname {string} 실제로 바꿀 수 있는 값
         * @param email {string} 필수값임, 실제로 바꿀 수 있는 값은 아님
         * @param password {string} 필수 값임,
         * @param new_password {string} 바꿀 수 있는 값
         * @returns {Promise<void>}
         * TODO: 갱신 페이지 만들기
         */
        update(nickname, email, password, new_password) {
            super.reportProcessing()
            return WordbookApis.updateAccount({nickname, email, password, new_password})
                .then((data) => {
                    this.accountId = data.account_id
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
        MyWordbooks,
        getAccount
    }
})();

/**
 * 빈 단어장일 때 발생하는 에러
 */
function EmptyWordbookException() {
}

EmptyWordbookException.prototype.toString = function () {
    return '적어도 하나의 문제/정답이 들어가야 합니다'
}

