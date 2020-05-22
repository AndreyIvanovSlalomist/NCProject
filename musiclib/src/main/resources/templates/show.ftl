<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
<html>
<head>
    <title>Информация о треке</title>
    <link href="${contextPath}/css/styles.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<@macros.navMenu/>
<div class="container">
    <h2 style="float: left">Информация о треке</h2>
    <a href="${contextPath}/tracks"><div class="btn btn-primary" style="float: right; margin: 15px 0 0 5px">Назад</div></a>
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