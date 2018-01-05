$(function () {
    $("#doLog").click(function () {

        var username = $("#username").val();
        var password = $("#password").val();

        if ($("#username").val().trim().length < 1) {

            $("#username").focus();
        } else {

            if ($("#password").val().trim().length < 1) {
                $("#password").focus();
            } else {

                doLogin(username, password);
            }
        }
    });
    $("#register").click(function () {

        if ($("#regUser").val().trim().length < 1) {
            $("#regUser").focus();
        } else {
            if ($("#regName").val().trim().length < 1) {
                $("#regName").focus();
            } else {
                if ($("#regPassword").val().trim().length < 1) {
                    $("#regPassword").focus();
                } else {
                    if ($("#regPassword").val() === $("#repeatPassword").val()) {
                        doReg($("#regUser").val(), $("#regName").val(), $("#regPassword").val());
                    }else{
                        alert("Passwords must match!");
                        $("#repeatPassword").val("");
                        $("#repeatPassword").focus();
                    }
                }
            }
        }

    });
});


function doLogin(user, password) {

    var emess = "Error desconocido, contactar con el administrador!"

    $.ajax({
        type: "POST",
        url: "Login",
        data: {username: user, password: password},
        success: function (rsp) {
            //en vez de un alert deberiamos usar algo del bootstrap ;D
            if (rsp["mess"] === "User does not exists.") {
                alert(rsp["mess"]);
                $("#username").val("");
                $("#password").val("");
                $("#username").focus();
            } else {
                if (rsp["mess"] === "Wrong password.") {
                    alert(rsp["mess"]);
                    $("#password").val("");
                    $("#password").focus();
                } else {
                    if (rsp["mess"] === undefined)
                        location.reload();
                }
            }
        },
        error: function (e) {
            if (e["responseJSON"] === undefined)
                alert(emess);
            else
                alert(e["responseJSON"]["error"]);
        }

    });
}


function doReg(regUser, regName, regPassword) {
    var emess = "There was an error, please contact the developers!"

    $.ajax({
        type: "POST",
        url: "Register",
        data: {username: regUser, password: regPassword, name: regName},
        success: function (rsp) {
            //when succeeds
            alert(rsp["mess"]);
            location.reload();
        },
        error: function (e) {
            if (e["responseJSON"] === undefined)
                alert(emess);
            else
                alert(e["responseJSON"]["error"]);
            if (e["responseJSON"]["error"] == "Username already exists") {
                $("#username").val("");
                $("#username").focus();
            }
        }

    });
}
