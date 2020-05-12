<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>Like page</title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css"
          integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/style.css">
</head>
<body style="background-color: #f5f5f5;">

<form method="post" style="width: 100%">
    <div class="col-4 offset-4">
        <div class="card">
            <div class="card-body" style="height: 500px">
                <div class="row">
                    <div class="col-12 col-lg-12 col-md-12 text-center">
                        <img src="${user.profilePic}" alt="" class="mx-auto rounded-circle img-fluid"
                             style="height: 370px; width: 300px;">
                        <h3 class="mb-0 text-truncated">${user.username}</h3>
                        <br>
                    </div>
                    <div class="col-12 col-lg-6">
                        <button type="submit" name="button" value="dislike" href="/users"
                                class="btn btn-outline-danger btn-block"><span class="fa fa-times"></span> Dislike
                        </button>
                    </div>
                    <div class="col-12 col-lg-6">
                        <button type="submit" name="button" value="like" href="/users"
                                class="btn btn-outline-success btn-block"><span
                                    class="fa fa-heart"></span> Like
                        </button>
                    </div>
                    <!--/col-->
                </div>
                <!--/row-->
            </div>
            <!--/card-block-->
        </div>
</form>

<a href="/liked" class="btn-lg btn-primary col-12 nav-link" style="display: block; margin: 10px auto; text-align: center">Users you liked</a>
<a href="/logout" class="btn-lg btn-danger col-12 nav-link"
   style="display: block; margin: 10px auto; text-align: center">Log
    out</a>

</div>

</body>
</html>