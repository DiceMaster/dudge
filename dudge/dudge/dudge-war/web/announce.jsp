<%@page import="dudge.db.Contest" pageEncoding="UTF-8"%>
<jsp:useBean id="announceForm" class="dudge.web.forms.AnnounceForm" scope="session" />
<jsp:useBean id="announceAction" class="dudge.web.actions.AnnounceAction" scope="session"/>

<div class="jumbotron">
    <h1>Заголовок</h1>
    <p>Dudge переводится на новый фреймворк для фронтенда. Вместе с этим будет переработана и главная страница, содержавшая ранее список соревнований.
       Теперь вместо них здесь будут анонсы, результаты соревнований и прочие информационные сообщения.</p>
    <p>Самое важное на данный момент сообщение, например информация о предстоящем или идущем соревновании, будет выделяться таким блоком.</p>
    <p><a class="btn btn-primary btn-lg" href="#">Подробнее</a></p>
</div>
<div class="row">
    <div class="col-lg-12">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h2>Еще одна новость</h2>
            </div>
            <div class="panel-body">
                <p>Остальные анонсы будут выглядеть в виде подобных блоков.</p>
                <p> <a class="btn btn-default" href="#">Подробнее</a> </p>
            </div>
        </div>        
    </div>
</div>
