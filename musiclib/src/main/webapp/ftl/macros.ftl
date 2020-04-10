<#macro navMenu>
<#assign  security=JspTaglibs["http://www.springframework.org/security/tags"] />
 <nav class="navbar navbar-inverse">
         <div class="container-fluid">
           <ul class="nav navbar-nav">
             <li><a href="/tracks">Треки</a></li>
<@security.authorize url="/users">
             <li><a href="/users">Пользователи</a></li>
</@security.authorize>
           </ul>
           <ul class="nav navbar-nav navbar-right">

<@security.authorize access="isAuthenticated()">
             <li><a href=""><span class="glyphicon glyphicon-user"></span>  <@security.authentication property="name" /> </a></li>
             <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Выйти</a></li>
</@security.authorize>

<@security.authorize access="! isAuthenticated()">
             <li><a href="/signUp"><span class="glyphicon glyphicon-user"></span> Регистрация</a></li>
             <li><a href="/signIn"><span class="glyphicon glyphicon-log-in"></span> Авторизация</a></li>
</@security.authorize>

           </ul>
         </div>
       </nav>
</#macro>