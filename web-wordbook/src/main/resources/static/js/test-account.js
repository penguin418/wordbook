/**
 * wordbook-service.js의 account 관련 api 가 잘 작동하는지 확인합니다
 */
$(document).ready(function (message) {
    const {URLs, getAccount} = wordbookService
    const account = getAccount()


    // given
    const nickname = 'test'
    const email = 'test@test.com'
    const password = 'test'

    testCreate()

    // 생성 테스트
    function testCreate() {
        try {
            account.create(nickname, email, password)
                .then(() => {
                    document.write("<br>account create success")
                    testLogin()
                })
        } catch (e) {
            console.log(e)
            document.write("<br>account create FAILED")
            testLogin()
        }
    }

    // 로그인 테스트
    function testLogin() {
        try {
            account.login(email, password)
                .then(() => {
                    document.write("<br>account login success")
                    testIsLoggedIn()
                })
        } catch (e) {
            console.log(e)
            document.write("<br>account login FAILED")
            testIsLoggedIn()
        }
    }

    // 상태 테스트
    function testIsLoggedIn() {
        try {
            const loggedIn = account.isLoggedIn()
            document.write("<br>account logged in' success " + loggedIn)
            testGetMyAccount()
        } catch (e) {
            console.log(e)
            document.write("<br>account logged in' FAILED")
            testGetMyAccount()
        }
    }

    // 내 정보 테스트
    function testGetMyAccount() {
        try {
            account.getMyAccount()
                .then(() => {
                    document.write("<br>account getMyAccount success")
                    testLogout()
                })
        } catch (e) {
            document.write("<br>account getMyAccount FAILED")
            testLogout()
        }

    }

    // 로그아웃 테스트
    function testLogout() {
        try {
            account.logout()
                .then(() => {
                    document.write("<br>account logout success")
                })
        } catch (e) {
            document.write("<br>account logout FAILED")
            console.log(e)
        }
    }
})