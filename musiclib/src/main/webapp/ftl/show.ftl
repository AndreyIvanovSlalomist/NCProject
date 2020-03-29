<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
<html>
<head>
    <title>Информация о треке</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<@macros.navMenu/>
<div class="container">
    <h2 style="float: left">Информация о треке</h2>
    <form action="/tracks/${track.id}/delete" method="POST">
    <input type="submit" class="btn btn-primary" style="float: right; margin: 15px 0 0 5px" value="Удалить">
    <a href="/tracks"><div class="btn btn-primary" style="float: right; margin: 15px 0 0 5px">Редактировать</div></a>
    <form>
    <table class="table table-hover" id="table">
        <tr>
            <td>Название</td>
            <td>${track.name}</td>
        </tr>
        <tr>
            <td>Исполнитель</td>
            <td>${track.singer}</td>
        </tr>
        <tr>
            <td>Альбом</td>
            <td>${track.album}</td>
        </tr>
        <tr>
            <td>Длительность</td>
            <td>${track.length}</td>
        </tr>
        <tr>
            <td>Жанр</td>
            <td>${track.genreName}</td>
        </tr>
    </table>

</div>
</body>
</html>