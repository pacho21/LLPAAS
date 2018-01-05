<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Lunar Lander</title>
        <meta charset="UTF-8">
        <meta name="description" content="Minigame of classic Lunar Lander using html, CSS, JavaScript and Data Access">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel='stylesheet' media='screen and (min-width: 601px)' href='css/responsive.css'>
        <link rel='stylesheet' media='screen and (max-width: 600px)' href='css/smartphone.css'>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="js/js.js"></script>
    </head>

    <body id="contenedor">

        <div id="nave">
            <img id ="imgNave" src="img/nave.png" alt="img nave">
            <img id ="imgMotor" src="img/motor.gif" alt="img motor">
        </div>

        <div id="izquierda">
            <div id="cpanel">
                <div class="controlesNave">Speed:<br><b><span id="velocidad">100</span></b> m/s</div>
                <div class="controlesNave">Fuel:<br><b><span id="fuel">100</span></b> %</div>
                <div class="controlesNave">Height:<br><b><span id="altura">60</span></b> m</div>
                <div id="botonOn"></div>
            </div>
        </div>

        <div id="zonaAterrizaje"><img src="img/luna.png" alt="luna" id="luna"></div>

        <div id="derechaSmartphone">
            <div id="reanudaSmartphone"><p>Play</p></div>
            <div id="pausaSmartphone">Menú</div>
            <div id="reiniciaSmartphone"><p>Restart</p></div>
            <div id="ayudaSmartphone"><p>Help</p></div>
            <div id="botonAjustesSmartphone"><p>Setting</p></div>
        </div>

        <div id="derecha">
            <div id="reanudar"><p>Play</p></div>
            <div id="pausa">Pause</div>
            <div id="reinicia"><p>Restart</p></div>
            <div id="instrucciones"><p>Help</p></div>
            <div id="botonAjustes"><p>Setting</p></div>
        </div>

        <div id="gameOver">
            <h2>¡¡¡GAME OVER!!!</h2>
            Try again! <br>
            The speed of the ship must not exceed 5 meters / second<br><br>
            <button type="button" class="btn btn-default btn-lg active" id="jugarOtraVez"><h3>Try again!</h3></button>
            <button type="button" class="btn btn-default btn-lg active" id="jugarOtraVezSmartphone"><h3>Try again</h3></button>
            <br><br>
            Attempts made: <b><span id="intentos">0</span></b><br><br>
        </div>

        <div id="userWin">
            <h2>CONGRATULATIONS!!!</h2>
           NASA would be proud to have pilots like you ... <br><br>
            <img src="img/Enhorabuena.gif"><br>
            <button type="button" class="btn btn-default btn-lg active" id="jugarAgain"><h3>Play again</h3></button>
            <button type="button" class="btn btn-default btn-lg active" id="jugarAgainSmartphone"><h3>Play again</h3></button><br><br>
        </div>

        <div id="menuInstrucciones">
            <a href="#" onclick="ocultarInstrucciones();"><img id="close" src="img/close.png" alt="close"></a>
            <h3>SETTINGS</h3>
            <p>The game consists in stopping the fall of the ship by using the engine, use the key
<b>&nbsp;&nbsp;space&nbsp;&nbsp;</b> or the button <b>ON</b> 
for the smartphone version, so that it can land properly on the lunar surface.<br><br>
If the player does not brake the fall of the ship sufficiently, at speed lower than 5 m / s, it will crash and the player will not overcome the game. <br><br> Keep in mind that the ship has a gas meter that will run out if the player abuses the use of the ship's engine. </p><br><br><a href="acerca.html"><button type="button" class="btn btn-default btn-lg active"><h3>Acerca de...</h3></button></a>
        </div>
        <div id="settings">
            <a href="#" onclick="ocultarAjustes();"><img id="close" src="img/close.png" alt="close"></a>
            <h3>SETTINGS</h3>
            <p><h4>Charged configuration:</h4> 
            <select id="xmlSettings">                                       
            </select>
            <button type="button" class="btn btn-default btn-lg active" id="cargarConf" >Charged configuration</button><br>
            Difficulty of the game:<br>
            (Decrease the gas tank)<br>
            <button id="dificultad">Easy</button><br><br>
            Moon model:<br>
            <button type="button" class="btn btn-default btn-lg active" id="modeloLuna">Yellow</button>
        </p>
        Moon model:<br>
        <button type="button" class="btn btn-default btn-lg active" id="modeloNave">Standard model Estándar</button><br><br>
        <input id="conf_name" type="text" placeholder="Nombre de configuracion">
        <button type="button" class="btn btn-default btn-lg active" id="guardar">Save settings</button><br><br>
        </body>
        </html>