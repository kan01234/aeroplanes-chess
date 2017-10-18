<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>Aeroplanes Chess</title>
<link href="/css/style.css" rel="stylesheet"></link>
<link rel="icon" href="favicon.png"/>
</head>
<body>
	<div class="wrapper-header">
		<h1 id="title">Aeroplane Chess</h1>
	</div>
	<div id="game-id"><img src="image/ic_link_white_24dp/web/ic_link_white_24dp_1x.png"><span id="game-id-span"></span></div>
	<input id="game-id-hidden">
	<img id="game-chess-hidden-1" src="image/aeroplanes1.png">
	<img id="game-chess-hidden-2" src="image/aeroplanes2.png">
	<img id="game-chess-hidden-3" src="image/aeroplanes3.png">
	<img id="game-chess-hidden-4" src="image/aeroplanes4.png">
	<div id="board-mask">
		<form id="board-main" action="javascript:start()">
			<input id="name" type="text" placeholder="N A M E" maxlength="6" />
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
			<div id="dice"></div>
			<div>
				<input id="roll" type="button" onclick="roll()" value="ROLL" disabled>
			</div>
		</div>
		<div class="wrapper-message">
			<div id="wrapper-system" class="wrapper-system"></div>
			<div id="wrapper-chat" class="wrapper-chat"></div>
		</div>
		<div class="wrapper-rule">
			<div>
				Rules
			</div>
			<ol class="rule">
				<li>Roll 2, 4, 6 can move one of the plane from base to take off point</li>
				<li>Can send back opposing plane after jump</li>
				<li>if destination of the move has more than two opposing planes, moved plane back to the base</li>
				<li>Roll 6 can continue the turn, however if the third roll is 6, all of the plane of that player need to back to the base</li>
				<li>An additional shortcut when the plane land exactly on that cell, but it will not send back opposing planes on the lane</li>
				<li>Have fun!</li>
			</ol>
		</div>
		<div id="count"></div>
	</div>
</body>
<script>
	var gameId = "${gameId}";
</script>
<script async src="//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js"></script>
<script>
	(adsbygoogle = window.adsbygoogle || []).push({
		google_ad_client: "ca-pub-2935863720703520",
		enable_page_level_ads: true
	});
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="script/script.js"></script>
</html>
