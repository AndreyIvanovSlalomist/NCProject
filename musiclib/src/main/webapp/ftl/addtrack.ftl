<#ftl encoding='UTF-8'>
<html>
<head>
    <title>Title</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <li><a href="/"><i></i>Гравная</a></li>
            <li><a class="active" href="/tracks">Треки</a></li>
            <li><a href="/users">Пользователи</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a href="/signUp"><span class="glyphicon glyphicon-user"></span> Регистрация</a></li>
            <li><a href="/signIn"><span class="glyphicon glyphicon-log-in"></span> Авторизация</a></li>
        </ul>
    </div>
</nav>
<div class="container">
    <h2>Добавить трек</h2>
    <form method="get" action="/tracks" autocomplete="off" class="form-inline">
        <div class="form-group" for="name">
            <input autofocus type="text" class="form-control" name="name" id="name" placeholder="Название">
        </div>
        <div class="form-group" for="singer">
            <input autofocus type="text" class="form-control" name="singer" id="singer" placeholder="Исполнитель">
        </div>
        <div class="form-group" for="album">
            <input autofocus type="text" class="form-control" name="album" id="album" placeholder="Альбом">
        </div>
        <div class="form-group" for="length">
                    <input autofocus type="text" class="form-control" name="length" id="length" placeholder="Длительность">
        </div>
        <div class="form-group" for="genreName">
            <input autofocus type="text" class="form-control" name="genreName" id="genreName" placeholder="Жанр" >
        </div>
        <input type="submit" class="btn btn-primary" value="Добавить">
        <a href="/tracks"><div class="btn btn-primary">Отмена</div></a>
    </form>
</div>
</body>
</html>