$(function() {
    var wsUri = "ws://" + document.location.host + document.location.pathname + "calcendpoint";
    var websocket = new WebSocket(wsUri);

    websocket.onopen = function(event) {
        websocket.opened = true;
    };
    
    websocket.onmessage = function(event) {
        var data = JSON.parse(event.data);
        if (data.command === 'GENERATE') {
            $(".p").val(data.p);
            $(".q").val(data.q);
        } if (data.command === 'ERROR') {
            alert(data.text);
        } else {
            $(".text").val(data.text);
        }
    };
    
    function sendText(text) {
        if (!websocket.opened) {
            alert("connection not opened yet");
        } else {
            console.log(text);
            
            websocket.send(text);
        }
    }
    
    function sendObj(obj) {
        sendText(JSON.stringify(obj));
    }
    
    function initConfig() {
        var p = $(".p").val();
        var q = $(".q").val();
        var text = $(".text").val();
        
        return {p: p, q: q, text: text};
    }
    
    $(".generate").click(function() {
        sendObj({command: "GENERATE"});
    });
    
    $(".encrypt").click(function() {
        var config = initConfig();
        config.command = "ENCODE";
        sendObj(config);
    });
    
    $(".decrypt").click(function() {
        var config = initConfig();
        config.command = "DECODE";
        sendObj(config);
    });
});


