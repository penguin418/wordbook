
$(document).ready(function (message) {
    console.log('ready from nav-ui')

    const event = document.createEvent('Event')
    event.initEvent('ui-ready',true,true)
    document.dispatchEvent(event)
})