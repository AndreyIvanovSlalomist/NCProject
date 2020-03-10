<#ftl encoding='UTF-8'>
<html>
<head>
    <title>Title</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
</head>
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