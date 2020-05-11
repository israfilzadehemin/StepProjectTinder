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


<form class="form-signin" method="post" enctype="multipart/form-data">
    <#if error=='duplicate'>
        <#assign errortext="User with this username or mail alreadys exists">
        <div class="alert alert-danger">${errortext}</div>
    <#elseif error=="noMatch">
        <#assign errortext="Password and confirm password is not same">
        <div class="alert alert-danger">${errortext}</div>
    </#if>
    <img class="mb-4" src="https://getbootstrap.com/assets/brand/bootstrap-solid.svg" alt="" width="72" height="72">
    <h1 class="h3 mb-3 font-weight-normal">Please sign up</h1>
    <input type="text" name="username" id="inputUsername" class="form-control" placeholder="Username" required
           autofocus>
    <input type="text" name="fullname" id="inputFullname" class="form-control" placeholder="Full name" required
           autofocus>
    <input type="email" name="mail" id="inputEmail" class="form-control" placeholder="Email address" required autofocus>
    <input type="password" name="password" id="inputPass" class="form-control" placeholder="Password" required
           autofocus>
    <input type="password" name="passCon" id="inputPassCon" class="form-control" placeholder="Confirm password" required
           autofocus>
    <input type="file" name="profilePic" class="form-control-file my-2">
    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign up</button>
    <p class="mt-5 mb-3 text-muted">&copy; Tinder by Leman Javadova and <br> Emin Israfilzadeh</p>
</form>
</body>
</html>