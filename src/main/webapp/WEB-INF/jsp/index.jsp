<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title></title>
<link href="/css/style.css" rel="stylesheet"></link>
</head>
<body>
	<h1 id="title">Aeroplane Chess</h1>
	<div id="board-mask">
		<form id="board-main" action="javascript:start()">
			<input id="name" type="text" placeholder="N A M E" required maxlength="6" />
			<input type="submit" value="START" />
		</form>
	</div>
	<div id="background"></div>
	<div id="background-mask"></div>
	<div class="wrapper">
		<canvas id="board"></canvas>
		<canvas id="board-chess"></canvas>
		<canvas id="board-hover"></canvas>
		<div class="wrapper-options">
			<div id="dice"></div>
			<div id="p1" class="player-container">
				<div class="info-container">
					<div id="p1-info-name" class="info-name">Player 1</div>
					<div id="p1-info-turn" class="info-turn">Your Turn!</div>
				</div>
				<div class="chess-container">
					<input type="button" id="p1-c0" class="chess" value="1" disabled data-pos="ba0">
					<input type="button" id="p1-c1" class="chess" value="2" disabled data-pos="ba0">
					<input type="button" id="p1-c2" class="chess" value="3" disabled data-pos="ba0">
					<input type="button" id="p1-c3" class="chess" value="4" disabled data-pos="ba0">
				</div>
			</div>
			<div id="p2" class="player-container">
				<div class="info-container">
					<div id="p2-info-name" class="info-name">Player 2</div>
					<div id="p2-info-turn" class="info-turn">Your Turn!</div>
				</div>
				<div class="chess-container">
					<input type="button" id="p2-c0" class="chess" value="1" disabled data-pos="ba1">
					<input type="button" id="p2-c1" class="chess" value="2" disabled data-pos="ba1">
					<input type="button" id="p2-c2" class="chess" value="3" disabled data-pos="ba1">
					<input type="button" id="p2-c3" class="chess" value="4" disabled data-pos="ba1">
				</div>
			</div>
			<div id="p3" class="player-container">
				<div class="info-container">
					<div id="p3-info-name" class="info-name">Player 3</div>
					<div id="p3-info-turn" class="info-turn">Your Turn!</div>
				</div>
				<div class="chess-container">
					<input type="button" id="p3-c0" class="chess" value="1" disabled data-pos="ba2">
					<input type="button" id="p3-c1" class="chess" value="2" disabled data-pos="ba2">
					<input type="button" id="p3-c2" class="chess" value="3" disabled data-pos="ba2">
					<input type="button" id="p3-c3" class="chess" value="4" disabled data-pos="ba2">
				</div>
			</div>
			<div id="p4" class="player-container">
				<div class="info-container">
					<div id="p4-info-name" class="info-name">Player 4</div>
					<div id="p4-info-turn" class="info-turn">Your Turn!</div>
				</div>
				<div class="chess-container">
					<input type="button" id="p4-c0" class="chess" value="1" disabled data-pos="ba3">
					<input type="button" id="p4-c1" class="chess" value="2" disabled data-pos="ba3">
					<input type="button" id="p4-c2" class="chess" value="3" disabled data-pos="ba3">
					<input type="button" id="p4-c3" class="chess" value="4" disabled data-pos="ba3">
				</div>
			</div>
			<div>
				<input id="roll" type="button" onclick="roll()" value="ROLL" disabled>
			</div>
		</div>
		<div class="wrapper-message">
			<div id="wrapper-system" class="wrapper-system"></div>
			<div id="wrapper-chat" class="wrapper-chat"></div>
		</div>
		<div id="count"></div>
	</div>
</body>
<script>
	var gameId = "${gameId}";
</script>
<script src="/webjars/stomp-websocket/stomp.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="/script.js"></script>
</html>
