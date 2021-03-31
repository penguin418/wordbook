/**
 * @author penguin penguin418@naver.com
 * @version 0.6
 * @file wordbook 을 관리하고 서버와 통신하기 위한 서비스입니다
 */
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
            }
        }
    })()

    /**
     * 어떤 프로세스가 종료되었을 때 실행할 함수를 사용할 수 있습니다
     * @class
     * @classdesc 각 변수를 채운뒤, saveToServer() 메소드를 통해 서버에 저장합니다
     */
    const Notifiable = class {

        /**
         * 프로세스 상태를 초기화 함
         * isProcessing {boolean} true: 프로세스가 끝난 상태, false: 프로세스가 끝나고 함수까지 실행된 상태
         * onFinishedFunction {function} 실행될 함수
         * @this Notifiable
         */
        constructor() {
            this.isProcessing = false
            this.onFinishedFunction = null
        }

        /**
         * 프로세스 종료 시 실행할 함수를 등록하는 콜백
         * 프로세스가 실행 중인 경우 함수를 등록 -> endProcess() 때 실행됨
         * 프로세스가 종료된 경우 함수를 실행 후 완전한 종료상태로 변경
         * @param func 종료 시 실행할 함수
         * @this Notifiable
         */
        onLoad(func) {
            if (this.isProcessing) {
                this.onFinishedFunction = func
            } else {
                func.call()
                this.isProcessing = false
            }
        }

        /**
         * 프로세스 실행 전 호출하는 함수
         * 프로세스를 진행중인 상태로 변경
         * @this Notifiable
         */
        startProcessing() {
            this.isProcessing = true
        }

        /**
         * 프로세스 종료시 호출하는 함수
         * 등록된 함수가 있을 경우, 실행하고 완전한 종료상태로 만듬
         * 그렇지 않은 경우 놔둠 -> onLoad때 실행됨
         * @this Notifiable
         */
        endProcessing() {
            if (this.onFinishedFunction) {
                this.onFinishedFunction.call()
                this.isProcessing = false
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
                'answer': answer.toString(),
                'id': id.toString()
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
                    qaList: this.qaList
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
            super.startProcessing()
            return fetch('/api/wordbooks/' + id, {
                method: 'GET',
            }).then(response => response.json())
                .then(data => {
                    console.log(data)
                    this.id = data.id
                    this.name = data.name
                    this.description = data.description
                    this.qaList = data.qaList
                    super.endProcessing()
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
                    qaList: this.qaList
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
            }).then(response => console.log(response))
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
            super.startProcessing()
            return fetch('/api/wordbooks', {
                method: "GET",
            }).then(response => response.json())
                .then((data) => {
                    this.contents = data.contents
                    this.length = data.length
                    this.page = data.page
                    super.endProcessing()
                })
        }
    }

    return {
        URLs,
        NewWordbook,
        CurrentWordbook,
        Wordbooks
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