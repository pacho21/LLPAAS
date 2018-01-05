var y = 5; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
var v = 0;
var g = 1.622;
var a = g; //a= -g es para motor encendido
var dt = 0.016683;
var timer;
var gasolina = 100;
var dificultad = 1;
var gasolinaTotal = 100;
var intentos = 1;
var modeloNave = 1;
var modeloLuna = 1;
var timerFuel = null;
var confXML = [];
var tempConf;
$(document).ready(function () {

    cargarGSON();
    $("#cargarConf").click(function () { //al hacer click en guardar -> ejecutar metodo ->
        cargarConfiguracion();
    });
    $("#guardar").click(function () {
        guardarConfiguracion();
        $("#conf_name").val("");
    });

});
window.onload = function arrancarJuego() {


    //CAPTURA SI EL DISPOSITIVO RECIBE EVENTOS OUNTOUCH (SMARTPHONE)
    function is_touch_device() {
        if ('ontouchstart' in window) {
            document.getElementById("botonOn").style.display = "inline-block";
        }
    }
    is_touch_device();
    //CAPTURANDO EVENTOS DEL PANEL DERECHA

    document.getElementById("reanudar").onclick = function () {
        reanudar();
    };
    document.getElementById("pausa").onclick = function () {
        pausar();
    };
    document.getElementById("reinicia").onclick = function () {
        reiniciarJuego();
    };
    document.getElementById("instrucciones").onclick = function () {
        mostrarInstrucciones();
    };
    document.getElementById("botonAjustes").onclick = function () {
        mostrarAjustes();
    };
    //CAPTURANDO EVENTOS PARA EL PANEL DERECHO EN SMARTPHONE
    document.getElementById("reanudaSmartphone").onclick = function () {
        reanudarSmartphone();
    };
    document.getElementById("pausaSmartphone").onclick = function () {
        pausarSmartphone();
    };
    document.getElementById("reiniciaSmartphone").onclick = function () {
        reiniciarJuegoSmartphone();
    };
    document.getElementById("ayudaSmartphone").onclick = function () {
        mostrarInstruccionesSmartphone();
    };
    document.getElementById("botonAjustesSmartphone").onclick = function () {
        mostrarAjustesSmartphone();
    };
    //EVENTOS DE FIN DEL JUEGO
    document.getElementById("jugarOtraVez").onclick = function () {
        reiniciarJuego();
    };
    document.getElementById("jugarOtraVezSmartphone").onclick = function () {
        reiniciarJuegoSmartphone();
    };
    document.getElementById("jugarAgain").onclick = function () {
        reiniciarJuego();
    };
    document.getElementById("jugarAgainSmartphone").onclick = function () {
        reiniciarJuegoSmartphone();
    };
    //CAMBIAR LA DIFICULTAD DEL JUEGO
    document.getElementById("dificultad").onclick = function () {
        dificultad++;
        if (dificultad > 3)
            dificultad = 1;
        crearNave(modeloNave, dificultad, modeloLuna);
    };
    //CAMBIAR LA IMAGEN DE LA LUNA
    document.getElementById("modeloLuna").onclick = function () {
        modeloLuna++;
        if (modeloLuna > 2)
            modeloLuna = 1;
        crearNave(modeloNave, dificultad, modeloLuna);
    };
    //CAMBIAR LA IMAGEN DE LA NAVE Y EL MOTOR
    document.getElementById("modeloNave").onclick = function () {
        modeloNave++;
        if (modeloNave > 2)
            modeloNave = 1;
        crearNave(modeloNave, dificultad, modeloLuna);
    };
    //Empezar a mover nave
    start();
    //ASIGNAR EVENTOS TOUCH SCREEN PARA LA VERSION SMARTPHONE
    var botonOnSmartphone = document.getElementById("botonOn");
    botonOnSmartphone.addEventListener("touchstart", handlerFunction, false);
    botonOnSmartphone.addEventListener("touchend", endingFunction, false);
    function handlerFunction(event) {
        encenderMotor();
    }
    function endingFunction(event) {
        apagarMotor();
    }

    //CON TECLADO (tecla ESPACIO)
    window.onkeydown = function (e) {
        var claveTecla;
        if (window.event)
            claveTecla = window.event.keyCode;
        else if (e)
            claveTecla = e.which;
        if ((claveTecla === 32))
        {
            encenderMotor();
        }
    };
    window.onkeyup = apagarMotor;
    mostrarAjustes();
}//TERMINA EL WINDOW.ONLOAD

//FUNCION EMPEZAR EL JUEGO
function start() {
    timer = setInterval(function () {
        moverNave();
    }, dt * 1000);
}

//FUNCION PARAR NAVE Y CONTROLADORES
function stop() {
    clearInterval(timer);
}

//FUNCION PARA QUE CAIGA LA NAVE A TRAVES DE LA PANTALLA
function moverNave() {
    v += a * dt;
    document.getElementById("velocidad").innerHTML = v.toFixed(2);
    y += v * dt;
    document.getElementById("altura").innerHTML = y.toFixed(2);
    //mover hasta que top sea un 70% de la pantalla
    if (y < 70) {
        document.getElementById("nave").style.top = y + "%";
    } else {
        stop();
        finalizarJuego();
    }
}

//HACER QUE LOS DIVS IZQUIERDA Y DERECHA NO RECIBAN EVENTOS ONCLICK
function eventosOff() {
    document.getElementById("izquierda").style.pointerEvents = 'none';
    document.getElementById("derecha").style.pointerEvents = 'none';
}
//HACER QUE LOS DIVS IZQUIERDA Y DERECHA SI RECIBAN EVENTOS ONCLICK
function eventosOn() {
    document.getElementById("izquierda").style.pointerEvents = 'auto';
    document.getElementById("derecha").style.pointerEvents = 'auto';
}

//FUNCION PARA ACABAR EL JUEGO
function finalizarJuego() {
    if (v > 5) {
        switch (modeloNave) {
            case 1:
                eventosOff();
                document.getElementById("imgNave").src = "img/nave_rota.gif";
                document.getElementById("gameOver").style.display = "block";
                document.getElementById("intentos").innerHTML = intentos;
                break;
            case 2:
                eventosOff();
                document.getElementById("imgNave").src = "img/mod2rota.gif";
                document.getElementById("gameOver").style.display = "block";
                document.getElementById("intentos").innerHTML = intentos;
                break;
        }
    } else {
        document.getElementById("userWin").style.display = "block";
        eventosOff();
    }
}

//FUNCION QUE ACTUA EN CUANTO SE ENCIENDE EL MOTOR
function encenderMotor() {
    a = -g;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "rgb(" + 0 + (100 - porcentajeGasolina()) + "%, 0%, 0%)";
    document.getElementById("imgMotor").style.display = "block";
    if (timerFuel === null) {
        timerFuel = setInterval(function () {
            actualizarGasolina();
        }, 100);
    }
    if (gasolina <= 0) {
        apagarMotor();
        document.getElementById("fuel").innerHTML = 0;
    }
}

//CALCULO EL PORCENTAJE DE GASOLINA QUE QUEDA
function porcentajeGasolina() {
    var result = gasolina * 100 / gasolinaTotal;
    return result.toFixed(0);
}


//FUNCION QUE ACTUALIZA EL MARCADOR DE FUEL
function actualizarGasolina() {
    gasolina -= 1;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "rgb(" + 0 + (100 - porcentajeGasolina()) + "%, 0%, 0%)";
    if (gasolina <= 0) {
        apagarMotor();
        document.getElementById("fuel").innerHTML = 0;
    }
}
//FUNCION QUE RESPONDE AL MOMENTO DE APAGAR EL MOTOR DE LA NAVE
function apagarMotor() {
    a = g;
    document.getElementById("imgMotor").style.display = "none";
    clearInterval(timerFuel);
    timerFuel = null;
}

function mostrarAjustes() {
    pausar();
    eventosOff();
    document.getElementById("settings").style.display = "block";
}
function ocultarAjustes() {
    document.getElementById("settings").style.display = "none";
    eventosOn();
    reanudar();
}

function mostrarInstrucciones() {
    pausar();
    eventosOff();
    document.getElementById("menuInstrucciones").style.display = "block";
}

function ocultarInstrucciones() {
    document.getElementById("menuInstrucciones").style.display = "none";
    eventosOn();
}

function restart() {
    stop();
    y = 5; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
    v = 0;
    g = 1.622;
    a = g;
    dt = 0.016683;
    gasolina = gasolinaTotal;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "black";
}
//OJO COMPORTAMIENTO ESCRITORIO
function reiniciarJuego() {
    stop();
    document.getElementById("reanudar").style.display = "none";
    document.getElementById("pausa").style.display = "inline-block";
    intentos++;
    y = 5; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
    v = 0;
    g = 1.622;
    a = g;
    dt = 0.016683;
    gasolina = gasolinaTotal;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "black";
    reanudar();
    clearInterval(timer);
    start();
    eventosOn();
    document.getElementById("intentos").innerHTML = intentos;
    document.getElementById("gameOver").style.display = "none";
    document.getElementById("userWin").style.display = "none";
    if (modeloNave == 1) {
        document.getElementById("imgNave").src = "img/nave.png";
    } else {
        document.getElementById("imgNave").src = "img/mod2nave.gif";
    }
}

function reanudar() {
    stop();
    start();
    document.getElementById("reanudar").style.display = "none";
    document.getElementById("pausa").style.display = "inline-block";
}
function pausar() {
    stop();
    document.getElementById("pausa").style.display = "none";
    document.getElementById("reanudar").style.display = "inline-block";
}

//OJO COMPORTAMIENTO SMARTPHONE
function reanudarSmartphone() {
    start();
    document.getElementById("reanudaSmartphone").style.display = "none";
    document.getElementById("pausaSmartphone").style.display = "inline-block";
    document.getElementById("reiniciaSmartphone").style.display = "none";
    document.getElementById("ayudaSmartphone").style.display = "none";
    document.getElementById("botonAjustesSmartphone").style.display = "none";
    document.getElementById('izquierda').style.display = "inline-block";
    document.getElementById('nave').style.display = "inline-block";
    document.getElementById('zonaAterrizaje').style.display = "inline-block";
    document.getElementById('derechaSmartphone').style.backgroundImage = 'url(img/sol.png)';
    document.getElementById('derechaSmartphone').style.backgroundSize = '60%';
    document.getElementById('derechaSmartphone').style.backgroundRepeat = 'no-repeat';
    document.getElementById('derechaSmartphone').style.width = '35%';
}

function pausarSmartphone() {
    stop();
    document.getElementById("pausaSmartphone").style.display = "none";
    document.getElementById("reanudaSmartphone").style.display = "inline-block";
    document.getElementById("reiniciaSmartphone").style.display = "inline-block";
    document.getElementById("ayudaSmartphone").style.display = "inline-block";
    document.getElementById("botonAjustesSmartphone").style.display = "inline-block";
    document.getElementById('derechaSmartphone').style.backgroundImage = 'url(img/fondo_menu.jpg)';
    document.getElementById('derechaSmartphone').style.backgroundSize = 'auto';
    document.getElementById('derechaSmartphone').style.backgroundRepeat = 'repeat';
    document.getElementById('derechaSmartphone').style.width = '100%';
}

function reiniciarJuegoSmartphone() {
    stop();
    intentos++;
    y = 5; // altura inicial y0=10%, debe leerse al iniciar si queremos que tenga alturas diferentes dependiendo del dispositivo
    v = 0;
    g = 1.622;
    a = g;
    dt = 0.016683;
    gasolina = gasolinaTotal;
    document.getElementById("fuel").innerHTML = porcentajeGasolina();
    document.getElementById("fuel").style.color = "black";
    reanudarSmartphone();
    clearInterval(timer);
    start();
    eventosOn();
    document.getElementById("intentos").innerHTML = intentos;
    document.getElementById("gameOver").style.display = "none";
    document.getElementById("userWin").style.display = "none";
    if (modeloNave == 1) {
        document.getElementById("imgNave").src = "img/nave.png";
    } else {
        document.getElementById("imgNave").src = "img/mod2nave.gif";
    }
}

function mostrarAjustesSmartphone() {
    pausarSmartphone();
    document.getElementById("settings").style.display = "block";
}

function mostrarInstruccionesSmartphone() {
    pausarSmartphone();
    document.getElementById("menuInstrucciones").style.display = "block";
}


function crearNave(m, d, l) {
    modeloNave = m;
    dificultad = d;
    modeloLuna = l;

    if (m == 1) {
        document.getElementById("imgNave").src = "img/nave.png";
        document.getElementById("imgMotor").src = "img/motor.gif";
        document.getElementById("modeloNave").innerHTML = "Modelo Estándar";
    } else {
        document.getElementById("imgNave").src = "img/mod2nave.gif";
        document.getElementById("imgMotor").src = "img/mod2motor.gif";
        document.getElementById("modeloNave").innerHTML = "Modelo PodRacer";
    }
    if (l == 2) {
        document.getElementById("luna").src = "img/mod2luna.png";
        document.getElementById("modeloLuna").innerHTML = "Grey";
    } else {
        document.getElementById("luna").src = "img/luna.png";
        document.getElementById("modeloLuna").innerHTML = "Yellow";
    }

    switch (d) {
        case 1:
            gasolina = 100;
            gasolinaTotal = 100;
            document.getElementById("dificultad").innerHTML = "Easy";
            restart();
            break;
        case 2:
            gasolina = 50;
            gasolinaTotal = 50;
            document.getElementById("dificultad").innerHTML = "Medium";
            restart();
            break;
        case 3:
            gasolina = 25;
            gasolinaTotal = 35;
            document.getElementById("dificultad").innerHTML = "Hard";
            restart();
            break;
    }
    restart();
}

function cargarGSON() {

    var emess = "No se ha podido cargar la configuración!";
    $.ajax({
        type: "GET",
        url: "ServletConfiguraciones", //canviar al Servlet després de comprovar que funciona.
        dataType: "json",
        success: function (jsn) {
            if (jsn["mess"] === "Configuration not found") {
                alert(jsn["mess"] + jsn["user"]);
            }
            $.each(jsn.config, function (i)
            {
                var id = jsn.config[i].id;
                var nom = jsn.config[i].nombre;
                var dif = jsn.config[i].dificultad; //$(this).attr('nombre');
                var nav = jsn.config[i].nave; //$(this).attr('edad');
                var lun = jsn.config[i].luna; //$(this).attr('edad');                           
                var dStr = dificultyToString(dif);
                var navStr = modNaveToString(nav);
                var lunStr = modLunaToString(lun);
                confXML[id] = {nave: nav, difi: dif, luna: lun};
                $("#xmlSettings").append(
                        "<option id='" + id + "'>" + nom + " Dificultad: "
                        + dStr + " Nave: " +
                        navStr + " Luna: " + lunStr
                        );
            });
        },
        error: function (e) {
            if (e["responseJSON"] === undefined)
                alert(emess);
            else
                alert(e["responseJSON"]["error"]);
        }
    });
}

function guardarConfiguracion() {

    var emess = "No se ha podido guardar la configuración!"
    var nam = $("#conf_name").val();
    var len = $("#conf_name").val().trim().length;
    if (len < 1) {
        alert("Debes introducir un nombre de configuración");
        $("#conf_name").focus();
    } else {

        $.ajax({

            type: "POST",
            url: "ServletConfiguraciones",
            data: {nombre: nam, dificultad: dificultad, modeloNave: modeloNave, modeloLuna: modeloLuna},
            success: function (rsp) {
                alert(rsp["mess"]);
                confXML[rsp.id] = {nave: modeloNave, difi: dificultad, luna: modeloLuna};
                var dStr = dificultyToString(dificultad);
                var navStr = modNaveToString(modeloNave);
                var lunStr = modLunaToString(modeloLuna);
                $("#xmlSettings").append(
                        "<option id='" + rsp.id + "'>" + nam + " Dificultad: "
                        + dStr + " Nave: " +
                        navStr + " Luna: " + lunStr
                        );
            },
            error: function (e) {
                if (e["responseJSON"] === undefined)
                    alert(emess);
                else
                    alert(e["responseJSON"]["error"]);
            }
        });
    }
}

//funciones que devuelven el nombre en string de las dificultades / modelos naves
function  dificultyToString(dif) {
    if (dif == 1)
        return "Fácil";
    if (dif == 2)
        return "Media";
    if (dif == 3)
        return "Difícil";
}
function modNaveToString(nav) {
    if (nav == 1)
        return "Modelo Estándar";
    if (nav == 2)
        return "Modelo PodRacer";
}
function modLunaToString(lun) {
    if (lun === 1)
        return "Amarilla";
    if (lun === 2)
        return "Gris";
}

function cargarConfiguracion() {
    var i = $("#xmlSettings option:selected").prop('id');
    crearNave(parseInt(confXML[i].nave), parseInt(confXML[i].difi), parseInt(confXML[i].luna));
    restart();
    ocultarAjustes();
    reanudar();
}
       