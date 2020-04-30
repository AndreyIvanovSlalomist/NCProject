<#ftl encoding='UTF-8'>
<html>
<head>
    <title>Регистрация</title>
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body class= "login-page">
<#if error??>
<div class="alert alert-danger" role="alert">Пользователь уже существует</div>
</#if>
<main>
    <div class="login-block">
        <div class="center">
        <h2>Регистрация</h2>
        </div>
        <form action="" method="POST" autocomplete="off">
            <div class="form-group" for="username">
                <label for="userName">Логин</label>
                <input autofocus required = true type="text" class="form-control" name="userName" id="userName" placeholder="Введите имя пользователя" autocomplete="username" >
            </div>
            <div class="form-group" for="password">
                <label for="password">Пароль</label>
                <input autofocus required = true type="password" class="form-control" name="password" id="password" placeholder="Введите пароль" autocomplete="new-password">
            </div>
            <input type="submit" class="btn btn-primary form-control" value="Зарегистрироваться">
        </form>
    </div>
    <div class="login-links">
         <p class = "text-center">Уже зарегистрированы? <a href="${contextPath}/signIn">Войдите</a></p>
         <p class="text-center"><a href = "${contextPath}/">На главную</a></p>
    </div>
    </main>
</body>
</html>