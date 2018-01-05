# Lunar Lander Team Work / JPA ORM PAAS

Play the game on PAAS : //link

This application is based in the classic game Lunar Lander. The project base started from the following github : https://github.com/urbinapro/lunar-landing-javascript 
The application has been developed by Polina Svetlinova (frontend: https://github.com/polinasvetlinova/ ) and Plamen Valentinov (backend: https://github.com/pacho21/ )

Find the documentation here : http://bit.ly/2F0FV6i  
Find the workflow distribution here: http://bit.ly/2CWM8ja

# Versions

-  v.0 - Fork base project from https://github.com/urbinapro/lunar-landing-javascript and workflow design for the developers.
- v.1 - Frontend uploaded basic login / register form.
- v.2 - Backend created database and mapped it to the project (JPA / ORM).
- v.3 - Fix for the login / register form. 
- v.4 - Frontend updated game.jsp with new design.
- v.5 - Backend uploaded the new servlets to get data from the database.


### How to use:
##### Login and Register
>In order to Login an user must first create an account. To create an account go to Register, fill up your data, make sure to complete all the camps and that the passwords are the same.
Once done click on register. If the desired username is taken, an alert message will pop up. Otherwise a notification of successful registration will show up.
Login into your account and start playing. To login you have to enter your username and your password. After you do so, you will be redirected to the game.

##### Gameplay
>To play the game firs you need to have an account and login. 
Once you are in the game you can press start and the spaceship will start falling down. When you press the spacebar the spaceship will acelerate and consume fuel. In order to win you must land with a proper speed (not higher than 5ms). If the user wants to challenge himself he can switch between dificulties. As more dificult game, the user will earn more points.
The player can save different configurations of the game that will be stored on our database. He can either choose between the color of the moon, the model of the spaceship and the dificulty and also give a name to it.
