function validate() {
    if ($('#input').val() === '') {
        alert('insert task instance')
        return false;
    }
    return true;
}

function registrationValidate() {
    if ($('#inputName').val() === '') {
        alert('Field name is empty, please enter your name')
        return false
    }
    if ($('#email').val() === '') {
        alert('Field email is empty, please enter your email address')
        return false
    }
    if ($('#password').val() === '') {
        alert('Field password is empty, please enter your password')
        return false
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
        }).fail(function (err) {
            alert(err)
        })
    }
}

function sendRegData() {
    if (registrationValidate()) {
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/todo/reg',
            dataType: 'text',
            data: ({name: $('#inputName').val(), email: $('#email').val(), password: $('#password')})
        }).fail(function (err) {
            alert(err)
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
        }).fail(function (err) {
            alert(err)
        })
    }
)


function showTask(data) {
    const tasks = JSON.parse(data)
    console.log(tasks)
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
    }).fail(function (err) {
        alert(err)
    })
}

function checkboxOrNot(done, id) {
    if (done) {
        return '<span class="glyphicon glyphicon-ok"></span>'
    } else {
        return '<input type="checkbox" id="  '+ id +'" name="'+ id +'" onchange="wasDone(id)">'
    }
}
