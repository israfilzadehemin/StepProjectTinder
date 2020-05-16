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
    <link rel="stylesheet" href="css/main.css">
</head>
<body class="bg-gra-01">

<div class="container">
    <div class="row">
        <div class="col-10 m-auto">
            <div class="panel panel-default user_panel" style="background-color: #555;">
                <div class="panel-heading" style="background-color: #555; color: white">
                    <h3 class="panel-title col-6 d-inline-block font-weight-bold">Users you visited</h3>
                    <form method="post" class="col-5 d-inline-block">
                        <p class="d-inline-block"> Show only:</p>
                        <button class="btn btn-link col-1 m-0" type="submit" name="action" value="liked"><i
                                    class="fas fa-thumbs-up"></i></button>
                        <button class="btn btn-link col-1 m-0" type="submit" name="action" value="disliked"><i
                                    class="fas fa-thumbs-down"></i></button>
                        <button class="btn btn-link nav-link col-1 m-0" type="submit" name="action" value="all">All</button>
                    </form>

                </div>
                <div class="panel-body" style="max-height: 500px; overflow: auto">
                    <div class="table-container">
                        <form method="post">
                            <table class="table-users table table-striped table-dark table-hover" border="0">
                                <tbody>
                                <#if liked?size==0>
                                    <a href="/users" class="btn nav-link my-2 col-12"> You have not liked anyone yet.
                                        Find out new friends</a>
                                <#else>
                                    <#list liked as user>
                                        <tr>
                                            <td width="10">
                                                <div class="avatar-img">
                                                    <img class="img-circle" src="${user.profilePic}"/>  
                                                </div>

                                            </td>
                                            <td class="align-middle">
                                                <button type="submit" name="msg" class="btn-dark btn"
                                                        value="${user.id}"> ${user.username} </button>
                                            </td>
                                            <td class="align-middle font-weight-normal" style="font-size: 19px">
                                                ${user.fullName}
                                            </td>
                                            <td class="align-middle font-weight-light font-italic">
                                                <p class="d-inline-block m-0 font-weight-bold">Last seen: </p> <br>
                                                ${user.lastLogin}
                                            </td>
                                            <td class="align-middle font-weight-normal" style="font-size: 19px">
                                                <button type="submit" name="delete" value="${user.id}" type="button"
                                                        class="btn btn-danger btn-sm float-right font-weight-bold"
                                                        aria-label="Close">
                                                    x
                                                </button>
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
            <a href="/users" class="btn-lg btn-primary card-link col-6 float-left"
               style="display: block; margin: 0 auto; text-align: center">Find more friends</a>
            <a href="/logout" class="btn-lg btn-danger card-link col-6 float-right"
               style="display: block; margin: 0 auto; text-align: center">Log
                out</a>

        </div>

    </div>

</div>

</body>
</html>