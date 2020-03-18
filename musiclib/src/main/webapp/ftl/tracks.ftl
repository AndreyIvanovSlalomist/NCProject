<#ftl encoding='UTF-8'>
<html>
<head>
    <title>Title</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
<!--    <link href="/css/bootstrap.min.css" rel="stylesheet"/>  если нет интернета (не рисует glyphicon)-->
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
  <h2>Треки</h2>

<form method="get" action="/tracks" autocomplete="off" class="form-inline">
    <div class="form-group" for="name">
        <input autofocus type="text" class="form-control" name="name" id="name" placeholder="Название" value="${name}"">
    </div>
    <div class="form-group" for="singer">
        <input autofocus type="text" class="form-control" name="singer" id="singer" placeholder="Исполнитель" value="${singer}"">
    </div>
    <div class="form-group" for="album">
        <input autofocus type="text" class="form-control" name="album" id="album" placeholder="Альбом" value="${album}"">
    </div>
    <div class="form-group" for="genreName">
        <input autofocus type="text" class="form-control" name="genreName" id="genreName" placeholder="Жанр" value="${genreName}">
    </div>
    <input type="submit" class="btn btn-primary" value="Искать">
    <a href="/tracks"><div class="btn btn-primary">Отмена</div></a>
</form>
  <table  class="table table-hover">
    <tr>
      <th>Название</th>
      <th>Исполнитель</th>
    </tr>
    <#list tracksFromServer as track>
    <tr>
      <td><a href="/track?id=${track.id}">${track.name}</a></td>
      <td>${track.singer}</td>
    </tr>
    </#list>
  </table>
</div>
</body>
</html>