<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="img/favicon.ico">

    <title>Chat</title>
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
        <div class="chat-main col-6 offset-3">
            <div class="col-md-12 chat-header">
                <div class="row header-one text-white p-1">
                    <div class="col-md-6 name pl-2">
                        <i class="fa fa-comment"></i>
                        <h6 class="ml-1 mb-0">${other.username}</h6>
                    </div>
                    <div class="col-md-6 options text-right pr-0 float-left">
                        <form method="post">
                            <button class="btn btn-dark btn-sm" type="submit" name="exit" value="exit"><i
                                        class="fa fa-times hover text-center pt-1"></i></button>
                        </form>

                    </div>

                </div>
                <div class="row header-two w-100">
                    <div class="col-md-6 options-left pl-1">
                        <i class="fa fa-video-camera mr-3"></i>
                        <i class="fa fa-user-plus"></i>
                    </div>
                    <div class="col-md-6 options-right text-right pr-2">
                        <i class="fa fa-cog"></i>
                    </div>
                </div>
            </div>
            <div class="chat-content">
                <div class="col-md-12 chats pt-3 pl-2 pr-3 pb-3" style="max-height: 500px; overflow: auto">
                    <ul class="p-0">
                        <#list messages as message>
                            <#if message.from == current.id>
                                <li class=" send-msg float-right mb-2">
                                    <p class="pt-1 pb-1 pl-2 pr-2 m-0 rounded">
                                        ${message.body}
                                    </p>
                                </li>
                            <#else>
                                <li class="receive-msg float-left mb-2">
                                    <div class="sender-img">
                                        <img src="${other.profilePic}" class="float-left">
                                    </div>
                                    <div class="receive-msg-desc float-left ml-2">
                                        <p class="bg-white m-0 pt-1 pb-1 pl-2 pr-2 rounded">
                                            ${message.body}
                                        </p>
                                        <span class="receive-msg-time">${other.username}, ${message.time}</span>
                                    </div>
                                </li>
                            </#if>
                        </#list>
                    </ul>
                </div>
                <form method="post">
                    <div class="col-12 p-2 msg-box border border-success float-left">
                        <input type="text" name="text" id="text" class="col-10 form-control d-inline-block"
                               placeholder=" Send message"/>
                        <button class="btn btn-success btn-sm border border-success col-2 float-right"
                                type="submit" disabled id="send" name="submit" style="min-height: 35px;">Send
                        </button>

                    </div>
                </form>

            </div>
        </div>
    </div>
</div>

<script>
    let text = document.querySelector("#text")
    let send = document.querySelector("#send")

    text.addEventListener("keyup", function () {
        if (text.value !== null) {
            send.disabled = false
        }
    })


</script>

</body>
</html>