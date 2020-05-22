<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
<#assign  security=JspTaglibs["http://www.springframework.org/security/tags"] />
       <html>
       <head>
           <title>Треки</title>
           <link href="${contextPath}/css/styles.css" rel="stylesheet" type="text/css">
       <!--    <link href="/css/bootstrap.min.css" rel="stylesheet"/>  если нет интернета (не рисует glyphicon)-->
           <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
       </head>
       <body>
       <@macros.navMenu/>
       <div class="container">
        <#if message??>
          <div class="alert alert-${type}" role="alert">${message}</div>
        </#if>
       <h2>Треки</h2>
       <form method="get" action="${contextPath}/tracks" autocomplete="off" class="form-inline">
           <div class="form-group" for="name">
               <input type="text" autofocus class="form-control" name="name" id="name" placeholder="Название" value="${name}"">
           </div>
           <div class="form-group" for="singer">
               <input type="text" class="form-control" name="singer" id="singer" placeholder="Исполнитель" value="${singer}"">
           </div>
           <div class="form-group" for="album">
               <input type="text" class="form-control" name="album" id="album" placeholder="Альбом" value="${album}"">
           </div>
           <div class="form-group" for="genreName">
               <input type="text" class="form-control" name="genreName" id="genreName" placeholder="Жанр" value="${genreName}">
           </div>
           <input type="submit" class="btn btn-primary" value="Искать">
           <a href="${contextPath}/tracks"><div class="btn btn-primary">Отмена</div></a>
           <@security.authorize url="/tracks/add">
           <a href="${contextPath}/tracks/add"><div class="btn btn-primary" style="float: right">Добавить трек</div></a>
           </@security.authorize>
       </form>
         <table  class="table table-hover" id="table">
           <tr>
             <th onclick="sort(0, this)">Название</th>
             <th onclick="sort(1, this)">Исполнитель</th>
             <th onclick="sort(2, this)">Альбом</th>
             <th onclick="sort(3, this)">Длина трека</th>
             <th onclick="sort(4, this)">Жанр</th>
             <th></th>
           </tr>
           <#list tracksFromServer as track>
           <tr>
             <td><a href="${contextPath}/tracks/${track.id}">${track.name}</a></td>
             <td>${track.singer}</td>
             <td>${track.album}</td>
             <td>${track.length}</td>
             <td>${track.genreName}</td>
             <td align="right" nowrap >
             <form>
           <@security.authorize url="/tracks/{id}/update">
             <input type="submit" formaction="${contextPath}/tracks/${track.id}/update" class="btn btn-primary" value="&#9998" title="Редактировать">
             </@security.authorize>
           <@security.authorize url="/tracks/{id}/delete">
             <input type="button" data-toggle="modal" data-target="#confirmation" data-id="${track.id}" class="btn btn-primary" value="&#10007" title="Удалить">
             </@security.authorize>
             </form>
             </td>
           </tr>
           </#list>
         </table>
       </div>

         <div id="confirmation" class="modal fade" role="dialog">
           <div class="modal-dialog modal-sm">
             <div class="modal-content">
               <div class="modal-body">
                 <p>Удалить этот трек?</p>
               </div>
               <div class="modal-footer">
               <form id = "deleteForm" action = "" method="post">
                 <input type="submit" class="btn btn-primary" value = "Да">
                 <input type="button" class="btn btn-secondary" data-dismiss="modal" value = "Отмена">
               </form>
               </div>
             </div>
           </div>
         </div>

         <script type="text/javascript">
           function sort(number, ColHeader) {
             var elem = document.querySelector("#table");

             var up = true;

             if (ColHeader.classList.contains("up")) {
               up = false;
               ColHeader.classList.remove("up");
             }else{
               up = true;
               ColHeader.classList.add("up");
             }

             var tbody = table.getElementsByTagName("tr").item(0);
             for (var i = 0; (node = tbody.getElementsByTagName("th").item(i)); i++) {
               node.classList.remove("up");
             }

             let sortedRows = Array.from(table.rows)
               .slice(1)
               .sort((rowA, rowB) => rowA.cells[number].innerHTML > rowB.cells[number].innerHTML ? 1 : -1);
             if (!up) {
               sortedRows.reverse();
               ColHeader.classList.remove("up");
             }else{
               ColHeader.classList.add("up");
             }
             table.tBodies[0].append(...sortedRows);
           }
         </script>
         <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
         <script src="${contextPath}/js/bootstrap.min.js"></script>
         <script>
            $('#confirmation').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);
                var id = button.data('id');
                $('#deleteForm').attr("action", "${contextPath}/tracks/" + id + "/delete" );
            })
         </script >
       </body>
       </html>