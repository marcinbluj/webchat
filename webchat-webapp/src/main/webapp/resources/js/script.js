/*jslint browser: true*/
/*global $, jQuery, alert*/

$(document).ready(function () {
    "use strict";
    var activeEntityType = "group",
        isUserLoggedIn = false,
        loggedInUser = "";

    function renderToHTML(jsonMsgs) {
        var i = 0,
            htmlMsgs = "",
            sender = "",
            msgBody = "",
            fullMsg = "";

        for (i = 0; i < jsonMsgs.length; i += 1) {
            sender = jsonMsgs[i].fromName;
            msgBody = jsonMsgs[i].body;
            fullMsg = sender + ": " + msgBody;

            if (i % 2 === 0) {
                htmlMsgs += ("<div class=\"message_one\">" + fullMsg + "</div>");
            } else {
                htmlMsgs += ("<div class=\"message_two\">" + fullMsg + "</div>");
            }
        }
        return htmlMsgs;
    }

    function loadGroupMsgs(groupName) {
        var request = new XMLHttpRequest();
        request.open("GET", "/webchat/data/group/" + groupName, true);
        request.onload = function () {
            var response = request.responseText,
                jsonMsgs = JSON.parse(response),
                htmlMsgs = "";

            htmlMsgs = renderToHTML(jsonMsgs);
            $("#chat_area").empty();
            $("#chat_area").append(htmlMsgs);
        };
        request.onerror = function () {
            alert("an error occurred");
        };
        request.send();
    }

    function loadPrivateMsgs(sender, receiver) {
        var request = new XMLHttpRequest();
        request.open("GET", "/webchat/data/private?u1=" + sender + "&u2=" + receiver, true);
        request.onload = function () {
            var response = request.responseText,
                jsonMsgs = JSON.parse(response),
                htmlMsgs = "";

            htmlMsgs = renderToHTML(jsonMsgs);
            $("#chat_area").empty();
            $("#chat_area").append(htmlMsgs);
        };
        request.onerror = function () {
            alert("an error occurred");
        };
        request.send();
    }

    function displayGroupButtonClick() {
        $("#group").css("box-shadow", "0.01rem 0.01rem 0.1rem rgba(42, 42, 46, 0.45)");
        $("#group").css("z-index", 1);
        $("#private").css("box-shadow", "0.1rem 0.1rem 0.3rem rgba(42, 42, 46, 0.45)");
        $("#private").css("z-index", 2);
    }

    function displayPrivateButtonClick() {
        $("#private").css("box-shadow", "0.01rem 0.01rem 0.1rem rgba(42, 42, 46, 0.45)");
        $("#private").css("z-index", 1);
        $("#group").css("box-shadow", "0.1rem 0.1rem 0.3rem rgba(42, 42, 46, 0.45)");
        $("#group").css("z-index", 2);
    }

    function showLogoutDisplay() {
        $("#logout").hide();
        $("#send").hide();
        $("#chat_inputs").hide();
        $("#send").hide();
        $("#user").hide();

        $("#login_input").show();
        $("#password_input").show();
        $("#login").show();
        $("#register").show();

        activeEntityType = "group";
        loadGroupMsgs("global");
        $("#user").text("");
        $("#to_input").val("global");
        $("#mode_name").text("Group - global");

        displayGroupButtonClick();
    }

    function showLoginDisplay() {
        $("#login_input").hide();
        $("#password_input").hide();
        $("#login").hide();
        $("#register").hide();

        $("#logout").show();
        $("#send").show();
        $("#chat_inputs").show();
        $("#send").show();
        $("#load").show();
        $("#chat_area").show();
        $("#user").show();

        activeEntityType = "group";
        loadGroupMsgs("global");
        $("#user").text(loggedInUser);
        $("#to_input").val("global");
        $("#mode_name").text("Group - global");

        displayGroupButtonClick();
    }

    function onHover() {
        $(".mode_button").hover(function () {
            $(this).addClass("mode_button_highlight");
        }, function () {
            $(this).removeClass("mode_button_highlight");
        });

        $(".chat_button").hover(function () {
            $(this).addClass("chat_button_highlight");
        }, function () {
            $(this).removeClass("chat_button_highlight");
        });
    }

    function onRegularButtonClick() {
        var activeRegularButtonId,
            isClicked = false;

        $(".chat_button").mousedown(function (e) {
            e.preventDefault();
            $(this).css("box-shadow", "0.01rem 0.01rem 0.1rem rgba(42, 42, 46, 0.45)");
            activeRegularButtonId = $(this).attr("id");
            isClicked = true;
        });

        $(document).mouseup(function () {
            if (isClicked) {
                $("#" + activeRegularButtonId).css("box-shadow", "0.1rem 0.1rem 0.3rem rgba(42, 42, 46, 0.45)");
                isClicked = false;
            }
        });
    }

    function onModeButtonClick() {
        var tempActiveButtonId,
            activeButtonId = "group",
            isOver = true,
            isOut = false;

        $(".mode_button")
            .mousedown(function (e) {
                e.preventDefault();
                $(this).css("box-shadow", "0.01rem 0.01rem 0.1rem rgba(42, 42, 46, 0.45)");
                tempActiveButtonId = $(this).attr("id");
                isOver = true;
                isOut = false;
            })
            .click(function () {
                $(this).css("box-shadow", "0.01rem 0.01rem 0.1rem rgba(42, 42, 46, 0.45)");
                if ($(this).is("#group")) {
                    displayGroupButtonClick();
                    activeButtonId = tempActiveButtonId;
                } else if ($(this).is("#private")) {
                    displayPrivateButtonClick();
                    activeButtonId = tempActiveButtonId;
                }
            })
            .mouseover(function () {
                if ($(this).attr("id") === tempActiveButtonId) {
                    isOver = true;
                    isOut = false;
                }
            })
            .mouseout(function () {
                if ($(this).attr("id") === tempActiveButtonId) {
                    isOut = true;
                    isOver = false;
                }
            });

        $(document).mouseup(function () {
            if (isOut && tempActiveButtonId !== activeButtonId) {
                $("#" + tempActiveButtonId).css("box-shadow", "0.1rem 0.1rem 0.3rem rgba(42, 42, 46, 0.45)");
            }
        });
    }

    function onLoadButtonClick() {
        $("#load").mouseup(function () {
            var receiver = $("#to_input").val(),
                sender = loggedInUser;
            switch (activeEntityType) {
            case "group":
                $("#mode_name").text("Group - " + receiver);
                loadGroupMsgs(receiver);
                break;
            case "user":
                $("#mode_name").text("Private - " + sender + " " + receiver);
                loadPrivateMsgs(sender, receiver);
                break;
            }
        });
    }

    function onSendButtonClick() {
        $("#send").click(function () {
            var receiver = $("#to_input").val(),
                message = $("#msg_input").val(),
                sender = loggedInUser,
                jsonBody = {from_name: sender, entity: receiver, type: activeEntityType, msg: message},
                request = new XMLHttpRequest();

            if (isUserLoggedIn && receiver.length > 0 && message.length > 0 && sender.length > 0) {
                request.open("POST", "/webchat/data/send", true);
                request.onload = function () {
                    var response = request.responseText,
                        jsonData = JSON.parse(response),
                        isValid = false;
                    isValid = jsonData;

                    if (isValid) {
                        switch (activeEntityType) {
                        case "group":
                            $("#mode_name").text("Group - " + receiver);
                            loadGroupMsgs(receiver);
                            break;
                        case "user":
                            $("#mode_name").text("Private - " + sender + " " + receiver);
                            loadPrivateMsgs(sender, receiver);
                            break;
                        }
                    }
                };
                request.setRequestHeader("Content-Type", "application/json");
                request.onerror = function () {
                    alert("an error occurred");
                };
                request.send(JSON.stringify(jsonBody));
                $("#msg_input").val("");
            }
        });
    }

    function onGroupButtonClick() {
        $("#group").click(function () {
            var receiver = $("#to_input").val();
            loadGroupMsgs(receiver);
            activeEntityType = "group";
            $("#to_input").val("global");
            $("#mode_name").text("Group - global");
            $("#load").show();
            $("#chat_area").show();
        });
    }

    function onPrivateButtonClick() {
        $("#private").click(function () {
            activeEntityType = "user";
            $("#chat_area").empty();
            if (isUserLoggedIn) {
                $("#to_input").val("");
                $("#mode_name").text("Private");
            } else {
                $("#mode_name").text("Private - not logged in");
                $("#load").hide();
                $("#chat_area").hide();
            }
        });
    }

    function onLogInButtonClick() {
        $("#login").click(function () {
            var loginInputVal = $("input[name=\"login\"]").val(),
                passwordInputVal = $("input[name=\"password\"]").val(),
                jsonBody = {login: loginInputVal, password: passwordInputVal},
                request = new XMLHttpRequest();
            if (loginInputVal.length > 0 && passwordInputVal.length > 0) {
                request.open("POST", "/webchat/data/login", true);
                request.onload = function () {
                    var response = request.responseText,
                        jsonData = JSON.parse(response),
                        isValid = false;

                    isValid = jsonData;
                    if (isValid) {
                        loggedInUser = loginInputVal;
                        showLoginDisplay();
                        isUserLoggedIn = true;
                    } else {
                        alert("Incorrect login or password.");
                    }
                };
                request.onerror = function () {
                    alert("an error occurred");
                };
                request.setRequestHeader("Content-Type", "application/json");
                request.send(JSON.stringify(jsonBody));
            }
        });
    }

    function onLogOutButtonClick() {
        $("#logout").click(function () {
            if (isUserLoggedIn) {
                showLogoutDisplay();
                isUserLoggedIn = false;
                loggedInUser = "";
                $("#msg_input").val("");
                $("#to_input").val("global");
            }
        });
    }

    function onRegisterButtonClick() {
        $("#register").click(function () {
            var loginInputVal = $("input[name=\"login\"]").val(),
                passwordInputVal = $("input[name=\"password\"]").val(),
                jsonBody = {login: loginInputVal, password: passwordInputVal},
                request = new XMLHttpRequest();
            if (loginInputVal.length > 0 && passwordInputVal.length > 0) {
                request.open("POST", "/webchat/data/register", true);
                request.onload = function () {
                    var response = request.responseText,
                        jsonData = JSON.parse(response),
                        isValid = false;

                    isValid = jsonData;
                    if (isValid) {
                        loggedInUser = loginInputVal;
                        showLoginDisplay();
                        isUserLoggedIn = true;
                    }
                };
                request.onerror = function () {
                    alert("an error occurred");
                };
                request.setRequestHeader("Content-Type", "application/json");
                request.send(JSON.stringify(jsonBody));
            }
        });
    }

    onHover();
    onRegularButtonClick();
    onModeButtonClick();
    onPrivateButtonClick();
    onGroupButtonClick();
    onSendButtonClick();
    onLoadButtonClick();
    onLogInButtonClick();
    onLogOutButtonClick();
    onRegisterButtonClick();
    showLogoutDisplay();
});