<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
       <html>
       <head>
           <title>Пользователи</title>
           <link href="/css/styles.css" rel="stylesheet" type="text/css">
           <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
       </head>
       <body>
        <@macros.navMenu/>
       <div class="container">
         <h2>Пользователи</h2>
         <table  class="table table-hover" id="table">
           <tr>
             <th>Логин</th>
             <th>Роль</th>
             <th></th>
           </tr>
           <#list usersFromServer as user>
           <tr>
             <td>${user.userName}</td>
             <td>${user.role.roleName}</td>
             <td align="right" nowrap>
                <a href="${contextPath}/user/?user_name=${user.userName}"><div class="btn btn-primary title="Редактировать">&#9998</div></a>
                <a href="${contextPath}/user/?user_name_delete=${user.userName}"><div class="btn btn-primary title="Удалить">&#10007</div></a>
             </td>
           </tr>
           </#list>
         </table>
       </div>

       </body>
       </html>