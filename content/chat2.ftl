<!------ Include the above in your HEAD tag ---------->

<!DOCTYPE html>
<html>
<head>
    <title>Chat</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css"
          integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">

    <link rel="stylesheet" href="css/chat.css">
</head>
<body>
<div class="container-fluid h-100">
    <div class="row justify-content-center h-100">
        <div class="col-md-8 col-xl-6 chat">
            <div class="card">
                <div class="card-header msg_head">
                    <div class="d-flex bd-highlight">
                        <div class="img_cont">
                            <img src="${other.profilePic}" class="rounded-circle user_img">
                        </div>
                        <div class="user_info">
                            <span>Chat with ${other.username}</span>
                        </div>
                    </div>
                    <span id="action_menu_btn"><a class="btn btn-danger" href="/liked">X</a></span>
                    <div class="action_menu">
                    </div>
                </div>
                <div class="card-body msg_card_body">
                    <#list messages as message>
                        <#if message.from == other.id>

                            <div class="d-flex justify-content-start mb-4">
                                <div class="img_cont_msg">
                                    <img src="${other.profilePic}" class="rounded-circle user_img_msg">
                                </div>
                                <div class="msg_cotainer">
                                    ${message.body}
                                    <span class="msg_time" style="width: 75px;">${message.time}</span>
                                </div>
                            </div>
                        <#else>
                            <div class="d-flex justify-content-end mb-4">
                                <div class="msg_cotainer_send">
                                    ${message.body}
                                    <span class="msg_time_send" style="width: 75px;">${message.time}</span>
                                </div>
                                <div class="img_cont_msg">
                                    <img src="${current.profilePic}" class="rounded-circle user_img_msg">
                                </div>
                            </div>
                        </#if>

                    </#list>
                </div>
                <div class="card-footer">
                    <form method="post">
                    <div class="input-group">
                        <input name="text" class="form-control type_msg" placeholder="Type your message..."></input>
                        <div class="input-group-append">
                            <button name="submit" type="submit" class="btn btn-lg btn-outline-primary bg-transparent" style="width: 100%">
                                <span class="d-block input-group-text send_btn" style="width: 100%"><i class="fas fa-location-arrow"></i></span>
                            </button>
                        </div>
                    </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
