$(document).ready(function (message) {
    const {URLs, getAccount} = wordbookService
    const account = getAccount()


    // given
    const nickname = 'test'
    const email = 'test@test.com'
    const password = 'test'

    testCreate()

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