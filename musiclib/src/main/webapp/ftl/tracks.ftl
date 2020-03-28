<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
       <html>
       <head>
           <title>Треки</title>
           <link href="/css/styles.css" rel="stylesheet" type="text/css">
       <!--    <link href="/css/bootstrap.min.css" rel="stylesheet"/>  если нет интернета (не рисует glyphicon)-->
           <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
       </head>
       <body>
        <@macros.navMenu/>
       <div class="container">
         <h2>Треки</h2>

       <form method="get" action="/tracks" autocomplete="off" class="form-inline">
           <div class="form-group" for="name">
               <input autofocus type="text" class="form-control" name="name" id="name" placeholder="Название" value="${name}"">
           </div>
           <div class="form-group" for="singer">
               <input autofocus type="text" class="form-control" name="singer" id="singer" placeholder="Исполнитель" value="${singer}"">
           </div>
           <div class="form-group" for="album">
               <input autofocus type="text" class="form-control" name="album" id="album" placeholder="Альбом" value="${album}"">
           </div>
           <div class="form-group" for="genreName">
               <input autofocus type="text" class="form-control" name="genreName" id="genreName" placeholder="Жанр" value="${genreName}">
           </div>
           <input type="submit" class="btn btn-primary" value="Искать">
           <a href="/tracks"><div class="btn btn-primary">Отмена</div></a>
           <a href="/tracks/add"><div class="btn btn-primary">Добавить трек</div></a>
       </form>
         <table  class="table table-hover" id="table">
           <tr>
             <th onclick="sort(0, this)">Название</th>
             <th onclick="sort(1, this)">Исполнитель</th>
             <th onclick="sort(2, this)">Альбом</th>
             <th onclick="sort(3, this)">Длина трека</th>
             <th onclick="sort(4, this)">Жанр</th>
           </tr>
           <#list tracksFromServer as track>
           <tr>
             <td><a href="/tracks/${track.id}">${track.name}</a></td>
             <td>${track.singer}</td>
             <td>${track.album}</td>
             <td>${track.length}</td>
             <td>${track.genreName}</td>
           </tr>
           </#list>
         </table>
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
       </body>
       </html>