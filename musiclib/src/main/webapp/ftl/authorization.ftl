<#ftl encoding='UTF-8'>
<html>
<head>
    <title>Авторизация</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
    <link href="/css/styles.css" rel="stylesheet" type="text/css">
</head>
<body class= "login-page">
<#if error??>
<div class="alert alert-danger" role="alert">Логин или пароль введены неверно</div>
</#if>
<main>
    <div class="login-block">
        <div class="center">
        <h2>Авторизация</h2>
        </div>
        <form action="" method="POST" autocomplete="off">
            <div class="form-group" for="username">
                <label for="username">Логин</label>
                <input autofocus required = true type="text" class="form-control" name="username" id="username" placeholder="Введите имя пользователя" >
            </div>
            <div class="form-group" for="password">
                <label for="password">Пароль</label>
                <input required = true type="password" class="form-control" name="password" id="password" placeholder="Введите пароль" >
            </div>
            <input type="submit" class="btn btn-primary form-control" value="Войти">
        </form>
    </div>
    </main>
</body>
</html>
