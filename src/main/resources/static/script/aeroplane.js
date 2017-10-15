var stompClient = null;
var sessionId;
var colors =  ["yellow", "blue", "green", "red" ];

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	} else {
		$("#conversation").hide();
	}
	$(".player-list").html("");
}

function connect() {
	var socket = new SockJS('/aeroplanechess-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		sessionId = /\/([^\/]+)\/websocket/.exec(socket._transport.url)[1];
		console.log("connected, session id: " + sessionId);

		stompClient.subscribe("/game/player-list", function(res) {
			$(".player-list").html("");
			var players = JSON.parse(res.body).players;
			for(var i in players) {
				var player = players[i];
				if(player == null) continue;
				var name = player.name;
				if(sessionId == player.sessionId)
					name += " (Me)";
				$(".player-list").append(`<div>${name} <span class="${colors[i]}">${colors[i]}<span></div>`)
			}
		});

		stompClient.subscribe("/game/roll-result", function(res) {
			var body = JSON.parse(res.body);
			logAppend(`${colors[body.current]} Player has rolled ${body.roll}`);
		});
		
		stompClient.subscribe("/game/move-" + sessionId, function(res) {
			console.log(res.body);
			logAppend("pls select a aeroplane to move");
			$(".aeroplane-btn").prop("disabled", false).off("click").one("click", function() {
				disableBtn();
				stompClient.send("/app/move/" + (Number($(this).html()) - 1));
			});
		});
		
		stompClient.subscribe("/game/move-result", function(res) {
			$(".aeroplane-list").html("");
			var aeroplanes = JSON.parse(res.body);
			var colorCount = -1;
			for(var i = 0 in aeroplanes) {
				$(".aeroplane-list").append(`<div class="plane">${i % 4 + 1}. ${colors[aeroplanes[i].color]} aeroplanes in ${aeroplanes[i].inCellId}</div>`);
			}
			logAppend("moved....");
		});
		
		stompClient.subscribe("/game/start", function(res) {
			console.log(res.body);
			logAppend("ready to start");
		});
		
		stompClient.subscribe("/game/your-turn-" + sessionId, function(res) {
			console.log(res.body);
			logAppend("it's your turn to roll the dice");
			$(".roll-btn").prop("disabled", false);
			$(".roll-btn").one("click", function() {
				$(".roll-btn").prop("disabled", true);
				roll();
			});
		});

		stompClient.subscribe("/game/won", function(res) {
			logAppend(`The Game Is Over, ${colors[res.body.playerWon]} Player won the game`);
		});

		stompClient.send("/app/join");
	});
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function roll() {
	logAppend("rolling.......");
	stompClient.send("/app/roll");
}

function logAppend(str) {
	$('.log').prepend(`<p>${new Date().toISOString()} ${str}</p>`);
}

function disableBtn() {
	$(".roll-btn").prop("disabled", true);
	$(".aeroplane-btn").prop("disabled", true);
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendName();
	});
});

$(document).ready(function () {
	connect();
	disableBtn();
});