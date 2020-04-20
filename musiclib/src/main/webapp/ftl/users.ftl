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
       <#if message??>
         <div class="alert alert-${type}" role="alert">${message}</div>
       </#if>

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
                <a href="#confirmation" data-toggle="modal" data-username="${user.userName}"><div class="btn btn-primary title="Удалить">&#10007</div></a>
             </td>
           </tr>
           </#list>
         </table>

         <div id="confirmation" class="modal fade" role="dialog">
                  <div class="modal-dialog modal-sm">
                    <div class="modal-content">
                      <div class="modal-body">
                        <p></p>
                      </div>
                      <div class="modal-footer">
                      <form id = "deleteUserForm" action = "" method="post">
                        <input type="submit" class="btn btn-primary" value = "Да">
                        <input type="button" class="btn btn-secondary" data-dismiss="modal" value = "Отмена">
                      </form>
                      </div>
                    </div>
             </div>
         </div>
       </div>

       <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
       <script src="/js/bootstrap.min.js"></script>
       <script>
         $('#confirmation').on('show.bs.modal', function (event) {
           var button = $(event.relatedTarget);
           var username = button.data('username');
           var modal = $(this);
           modal.find('.modal-body p').text("Удалить пользователя "+username+"?");
           $('#deleteUserForm').attr("action", "${contextPath}/user/"+username+"/delete");
         })
       </script >

       </body>
       </html>