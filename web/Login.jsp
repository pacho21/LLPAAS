<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Welcome to LunarLander</title>       
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!--Boostrap-->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <!-- jQuery library -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <!-- Latest compiled JavaScript -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <!-- Personal js -->
        <script src="js/login.js"></script>
        <!-- Personal css -->
        <link href="css/login.css" rel="stylesheet">
    </head>
    <body>
         <div class="container">
            <div class="row">
                <div class="col-md-6">
                    <div class="panel with-nav-tabs panel-info">
                        <div class="panel-heading">
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#login" data-toggle="tab"> Login </a></li>
                                <li><a href="#signup" data-toggle="tab"> Signup </a></li>
                            </ul>
                        </div>
                        <div class="panel-body">
                            <div class="tab-content">
                                <div id="login" class="tab-pane fade in active register">
                                    <div class="container-fluid">
                                        <div class="row">
                                            <h2 class="text-center" style="color: #5cb85c;"> <strong> Login  </strong></h2><hr />

                                            <div class="row">
                                                <div class="col-xs-12 col-sm-12 col-md-12">
                                                    <div class="form-group">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">
                                                                <span class="glyphicon glyphicon-user"></span>
                                                            </div>
                                                            <input id="username" type="text" placeholder="User Name" name="uname" class="form-control">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-xs-12 col-sm-12 col-md-12">
                                                    <div class="form-group">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">
                                                                <span class="glyphicon glyphicon-lock"></span>
                                                            </div>

                                                            <input id="password" type="password" placeholder="Password" name="pass" class="form-control">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <hr />
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-12 col-md-12">
                                                    <button id="doLog" type="submit" class="btn btn-success btn-block btn-lg"> Login </button>
                                                </div>
                                            </div>

                                        </div>
                                    </div> 
                                </div>
                                <div id="signup" class="tab-pane fade">
                                    <div class="container-fluid">
                                        <div class="row">
                                            <h2 class="text-center" style="color: #f0ad4e;"> <Strong> Register </Strong></h2> <hr />
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-12 col-md-12">
                                                    <div class="form-group">
                                                        <div class="input-group">
                                                            <div class="input-group-addon iga1">
                                                                <span class="glyphicon glyphicon-user"></span>
                                                            </div>
                                                            <input id="regUser" type="text" class="form-control" placeholder="Enter Username" name="username">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>

                                            <div class="row">
                                                <div class="col-xs-12 col-sm-12 col-md-12">
                                                    <div class="form-group">
                                                        <div class="input-group">
                                                            <div class="input-group-addon iga1">
                                                                <span class="glyphicon glyphicon-sunglasses"></span>
                                                            </div>
                                                            <input id="regName" type="text" class="form-control" placeholder="Enter your name" name="name">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>                                            
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-12 col-md-12">
                                                    <div class="form-group">
                                                        <div class="input-group">
                                                            <div class="input-group-addon iga1">
                                                                <span class="glyphicon glyphicon-lock"></span>
                                                            </div>
                                                            <input id="regPassword" type="password" class="form-control" placeholder="Enter Password" name="pass">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-12 col-md-12">
                                                    <div class="form-group">
                                                        <div class="input-group">
                                                            <div class="input-group-addon iga1">
                                                                <span class="glyphicon glyphicon-lock"></span>
                                                            </div>
                                                            <input id="repeatPassword" type="password" class="form-control" placeholder="Repeat your Password" name="pass2">
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <hr>
                                            <div class="row">
                                                <div class="col-xs-12 col-sm-12 col-md-12">
                                                    <div class="form-group">
                                                        <button id="register" type="submit" class="btn btn-lg btn-block btn-warning"> Register</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>                
        </div>
        
       
        
        
    </body>
</html>
