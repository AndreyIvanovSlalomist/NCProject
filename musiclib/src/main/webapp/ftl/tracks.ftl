<#ftl encoding='UTF-8'>
<html>
<head>
    <title>Title</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
</head>
<body>
<nav class="top-menu">
  <ul>
    <li><a href="/"><i></i>Гравная</a></li>
    <li><a class="active" href="/tracks">Треки</a></li>
    <li><a href="/users">Пользователи</a></li>
    <li><a href="/signIn">Авторизация</a></li>
    <li><a href="/signUp">Регистрация</a></li>
    <li style="float:right"><a href="/logOut">Выйти</a></li>
  </ul>
</nav>
<div class="form-style-2">
  <div class="form-style-2-heading">Треки</div>
  <table>
    <tr>
      <th>Название</th>
      <th>Исполнитель</th>
    </tr>
    <#list tracksFromServer as track>
    <tr>
      <td>${track.name}</td>
      <td>${track.singer}</td>
    </tr>
    </#list>
  </table>
</div>
</body>
</html>