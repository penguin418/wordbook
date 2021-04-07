$(document).ready(function (message) {
    console.log('ready from nav-ui')

    const event = document.createEvent('Event')
    event.initEvent('ui-ready', true, true)
    document.dispatchEvent(event)
})

const UiHelper = (function () {
    const setQaItem = function ($qaItem, question, answer, qa_id = '0') {
        $qaItem.find('.qa-question').text(question)
        $qaItem.find('.qa-answer').text(answer)
        $qaItem.attr('data-qa-id', qa_id)
    }
    return {
        getQaItem: function ($qaItem) {
            return {
                question: $qaItem.find('.qa-question').text(),
                answer: $qaItem.find('.qa-answer').text(),
                qa_id: $qaItem.attr('data-qa-id')
            }
        },

        getAllQaItems: function (){
            const qaList = []
            $.each($(".qa"), function (index, qaItem) {
                qaList.push(getQaItem($(qaItem)))
            })
            return qaList
        },

        makeQaItem: function ($qaTemplate, question, answer, qa_id = '0'){
            setQaItem($qaTemplate, question, answer, qa_id)
            $qaTemplate.removeAttr('id')
            $qaTemplate.css('display', '')
            $qaTemplate.addClass("qa")
        }
    }
})()

