<#ftl encoding='UTF-8'>
<#import "macros.ftl" as macros/>
<html>
<head>
    <title>Добавить трек</title>
    <link href="${contextPath}/css/styles.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
<@macros.navMenu/>
<div class="container">
    <h2>Добавить трек</h2>
    <form action="${contextPath}/tracks/add" method="POST" autocomplete="off">
        <div class="form-group" for="name">
            <input autofocus required = true type="text" class="form-control" name="name" id="name" placeholder="Название" >
        </div>
        <div class="form-group" for="singer">
            <input autofocus required = true type="text" class="form-control" name="singer" id="singer" placeholder="Исполнитель" >
        </div>
        <div class="form-group" for="album">
            <input autofocus required = true type="text" class="form-control" name="album" id="album" placeholder="Альбом" >
        </div>
        <div class="form-group" for="length">
            <input
                autofocus required = true type="text" class="form-control" name="length" id="length" placeholder="Длительность [ЧЧ:ММ:CC]"
                pattern="^(?:(?:([01]?\d|2[0-3]):)?([0-5]?\d):)?([0-5]?\d)$" >
        </div>
        <div class="form-group" for="genreName">
            <input autofocus required = true type="text" class="form-control" name="genreName" id="genreName" placeholder="Жанр" >
        </div>
        <input type="submit" class="btn btn-primary" value="Добавить">
        <a href="${contextPath}/tracks"><div class="btn btn-primary">Отмена</div></a>
    </form>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script>
    $('input').on('input invalid', function() {
        this.setCustomValidity('')
        if (this.validity.patternMismatch) {
             this.setCustomValidity("Длительность трека должна быть указана в формате [ЧЧ:ММ:CC].")
        }
    })
</script>
</body>
</html>