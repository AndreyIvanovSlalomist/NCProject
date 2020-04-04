<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
<html>
<head>
    <title>Изменение роли</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<@macros.navMenu/>
<div class="container">
    <h2>Изменение роли</h2>
    <form action="/user/?id=${user.id}" method="POST" autocomplete="off">
        <div class="form-group" for="name">
            <h2>${user.userName}</h2>
        </div>
        <div class="form-group" for="id_role">
            <select class="form-control" name="id_role" id="id_role">
                <option disabled>Выберите роль</option>
                <#list rolesFromServer as role>
                    <option
                        <#if role.roleName == user.role.roleName >selected</#if>
                        value="${role.id}">${role.roleName}
                    </option>
                </#list>
            </select>

        </div>
        <input type="submit" class="btn btn-primary" value="Сохранить">
        <a href="/users"><div class="btn btn-primary">Отмена</div></a>
    </form>
</div>
</body>
</html>