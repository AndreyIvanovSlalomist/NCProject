<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
<html>
<head>
    <title>Редактировать трек</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<@macros.navMenu/>
<div class="container">
    <h2>Редактировать трек</h2>
    <form action="${contextPath}/tracks/${track.id}/update" method="POST" autocomplete="off">
        <div class="form-group" for="name">
            <input autofocus required = true type="text" class="form-control" name="name" id="name" placeholder="Название" value = "${track.name}">
        </div>
        <div class="form-group" for="singer">
            <input autofocus required = true type="text" class="form-control" name="singer" id="singer" placeholder="Исполнитель" value = "${track.singer}">
        </div>
        <div class="form-group" for="album">
            <input autofocus required = true type="text" class="form-control" name="album" id="album" placeholder="Альбом" value = "${track.album}">
        </div>
        <div class="form-group" for="length">
            <input autofocus required = true type="text" class="form-control" name="length" id="length" placeholder="Длительность [ЧЧ:ММ:CC]" value = "${track.length}" pattern="^(?:(?:([01]?\d|2[0-3]):)?([0-5]?\d):)?([0-5]?\d)$">
        </div>
        <div class="form-group" for="genreName">
            <input autofocus required = true type="text" class="form-control" name="genreName" id="genreName" placeholder="Жанр" value = "${track.genreName}">
        </div>
        <input type="submit" class="btn btn-primary" value="Сохранить">
        <a href="${contextPath}/tracks"><div class="btn btn-primary">Отмена</div></a>
    </form>
</div>
</body>
</html>