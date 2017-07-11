<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
    <head>
        <spring:url value="/resources/css/stylesheet.css" var="stylesheet" />
        <link type="text/css" rel="stylesheet" href="${stylesheet}" />
        <title>Web Chat</title>
        <meta http-equiv="Content-Type" content="text/html; charset=utf8" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <spring:url value="/resources/js/script.js" var="script" />
        <script type="text/javascript" src="${script}"></script>
    </head>

    <body>
        <div id="header">
            <div id="right_panel">
                <div>
                    <form>
                        <input placeholder="Login" class="login_input" name="login" id="login_input">
                        <input placeholder="Password" class="login_input" name="password" id="password_input">
                        <div id="user"></div>
                    </form>
                </div>
                <div>
                    <div class="chat_button" id="login">Log in</div>
                    <div class="chat_button" id="register">Register</div>
                    <div class="chat_button" id="logout">Log out</div>
                </div>
            </div>
            <div id="page_name">WEB CHAT</div>
        </div>
        <div id="chat_window" style="font-size: 0;">
            <div id="mode_buttons" style="margin: 0px;">
                <div class="mode_button" id="group" draggable="false">Group</div>
                <div class="mode_button" id="private">Private</div>
            </div>
            <div id="mode_name"></div>
            <div id="msg_zone">
                <div id="chat_inputs">
                    <form>
                        <input placeholder="To" id="to_input" class="input">
                        <input placeholder="Type your message here" id="msg_input" class="input">
                    </form>
                </div>
                <div id="chat_buttons">
                    <div class="chat_button" id="load">Load</div>
                    <div class="chat_button" id="send">Send</div>
                </div>
            </div>
            <div id="chat_area"></div>
        </div>
    </body>
</html>