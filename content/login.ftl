<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>Signin Template for Bootstrap</title>

    <!-- Bootstrap core CSS -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="css/style.css">
</head>

<body class="text-center">


<form class="form-signin" method="post">
    <#if error=='wrongUser'>
        <div class="alert alert-danger">Please check username and password!</div>
    </#if>
    <h1 class="h3 mb-3 font-weight-normal">Welcome :)</h1>
    <label for="inputEmail" class="sr-only">Email address</label>
    <input type="email" name="mail" id="inputEmail" class="form-control" placeholder="Email address" required autofocus>
    <label for="inputPassword" class="sr-only">Password</label>
    <input name="password" type="password" id="inputPassword" class="form-control" placeholder="Password" required>
    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    <label class="col-6">OR</label>
    <a href="/registration" class="btn btn-success btn-block">Sign up</a>
    <p class="mt-2 mb-2 text-muted">&copy; Tinder by Leman Javadova and <br> Emin Israfilzadeh</p>
</form>
</body>
</html>