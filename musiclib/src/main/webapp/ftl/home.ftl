<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
<html>
      <head>
          <title>Музыкальная библиотека</title>
          <link href="/css/styles.css" rel="stylesheet" type="text/css">
          <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
      </head>
      <body>
       <@macros.navMenu/>
       <div class="container">
       <div class="text-center">
       <h1>Добро пожаловать в Музыкальную библиотеку!</h1>
       <p><a href = "/signIn">Войдите</a>, чтобы продолжить, или <a href="/signUp">зарегистрируйтесь</a>.</p>
       </div>
       </div>
      </body>
</html>