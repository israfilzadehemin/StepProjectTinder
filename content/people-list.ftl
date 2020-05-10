<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>People list</title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
          integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="container">
    <div class="row">
        <div class="col-8 offset-2">
            <div class="panel panel-default user_panel">
                <div class="panel-heading">
                    <h3 class="panel-title">Users you liked</h3>
                </div>
                <div class="panel-body">
                    <div class="table-container">
                        <form method="post">
                            <table class="table-users table" border="0">
                                <tbody>
                                <#if liked?size==0>
                                    <a href="/users" class="btn btn-primary my-2 col-12"> You have not liked anyone yet. Look at users</a>
                                <#else>
                                <#list liked as user>
                                    <tr>
                                        <td width="10">
                                            <div class="avatar-img">
                                                <img class="img-circle" src="${user.profilePic}"/>  
                                            </div>

                                        </td>
                                        <td class="align-middle">
                                            <button type="submit" name="msg" class="btn-primary btn"
                                                    value="${user.id}"> ${user.username} </button>
                                        </td>
                                        <td class="align-middle">
                                            ${user.mail}
                                        </td>
                                        <td class="align-middle">
                                            Last Login: 6/10/2017<br><small class="text-muted">5 days ago</small>
                                        </td>
                                    </tr>
                                </#list>
                                </#if>
                                </tbody>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <a href="/logout" class="btn-lg btn-danger col-3" style="display: block; margin: 0 auto; text-align: center">Log
        out</a>

</div>

</body>
</html>