<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
<html>
<head>
    <title>Изминение роли</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<@macros.navMenu/>
<div class="container">
    <h2>Изминение роли</h2>
    <form action="/user/?id=${user.id}" method="POST" autocomplete="off">
        <div class="form-group" for="name">
            <input autofocus required = true type="text" class="form-control" name="name" id="name" placeholder="Название" value = "${user.userName}">
        </div>
        <div class="form-group" for="singer">
            <input autofocus required = true type="text" class="form-control" name="singer" id="singer" placeholder="Исполнитель" value = "${user.role.roleName}">
        </div>
        <input type="submit" class="btn btn-primary" value="Сохранить">
        <a href="/users"><div class="btn btn-primary">Отмена</div></a>
    </form>
</div>
</body>
</html>