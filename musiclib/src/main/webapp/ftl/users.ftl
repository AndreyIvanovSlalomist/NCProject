<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
       <html>
       <head>
           <title>Title</title>
           <link href="/css/styles.css" rel="stylesheet" type="text/css">
           <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
       </head>
       <body>
        <@macros.navMenu/>
       <div class="container">
         <h2>Пользователь</h2>
         <table  class="table table-hover" id="table">
           <tr>
             <th>Логин</th>
             <th>Роль</th>
           </tr>
           <#list usersFromServer as user>
           <tr>
             <td><a href="/user?id=${user.id}">${user.userName}</a></td>
             <td>${user.role.roleName}</td>
           </tr>
           </#list>
         </table>
       </div>

       </body>
       </html>