function validate() {
    if ($('#input').val() === '') {
        alert('insert task instance')
        return false;
    }
    return true;
}
function showAll() {
    const show = document.getElementById('view').checked
    $.ajax({
        type: 'POST',
        crossDomain: true,
        url: 'http://localhost:8080/todo/task',
        dataType: 'text',
        data: ({status: show, work: 'show'})
    }).done(function (data) {
        $('#table td').parent().remove()
        showTask(data)
    })
}

function sendForm() {
    const show = document.getElementById('view').checked
    if (validate()) {
        $.ajax({
            type: 'POST',
            crossDomain: true,
            url: 'http://localhost:8080/todo/task',
            dataType: 'text',
            data: ({ input:$('#input').val(), work: 'input', status: show})
        }).done(function (data) {
            $('#table td').parent().remove()
            showTask(data)
        })
    }
}

$(document).ready(
    function () {
        $.ajax({
            type: 'POST',
            crossDomain: true,
            url: 'http://localhost:8080/todo/task',
            dataType: 'text',
            data: ({work: 'showTask'})
        }).done(function (data) {
            showTask(data)
        })
    }
)


function showTask(data) {
    const tasks = JSON.parse(data)
    console.log(tasks)
    let isDone = ''
    for (let task of tasks) {
        $('#table tr:last').after(
            '<tr><th scope="row">' + task.id + '</th>'
            + '<td>' + task.description + '</td>'
            + '<td>' + task.created + '</td>'
            + '<td>'
            + checkboxOrNot(task.done, task.id)
            + '</td>'
            + '</tr>')
    }
}

function wasDone(id) {
    const show = document.getElementById('view').checked
    $.ajax({
        type: 'POST',
        crossDomain: true,
        url: 'http://localhost:8080/todo/task',
        dataType: 'text',
        data: ({work: 'isDone', id: id, status: show})
    }).done(function (data) {
        $('#table td').parent().remove()
        showTask(data)
    })
}

function checkboxOrNot(done, id) {
    if (done) {
        return '<span class="glyphicon glyphicon-ok"></span>'
    } else {
        return '<input type="checkbox"id="  '+ id +'" name="'+ id +'" onchange="wasDone(id)">'
    }
}
