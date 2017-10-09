var number_of_player = 4,
	number_of_chess = 4,
	number_of_steps_0 = [1, 3, 4, 6, 4, 3, 6, 3, 4, 6, 4, 3, 3, 5, 1],
	number_of_steps_1 = [1, 4, 3, 6, 3, 4, 6, 4, 3, 6, 3, 4, 3, 5, 1],
	number_of_steps_p1 = 0,
	number_of_steps_p2 = 1,
	number_of_steps_p3 = 0,
	number_of_steps_p4 = 1,
	turn_of_steps = ['r', 'u', 'r', 'd', 'r', 'd', 'l', 'd', 'l', 'u', 'l', 'u'],
	turn_of_steps_p1 = 'd',
	turn_of_steps_p2 = 'l',
	turn_of_steps_p3 = 'u',
	turn_of_steps_p4 = 'r',
	/* color */
	color_p1 = 'yellow',
	color_p2 = 'blue',
	color_p3 = 'green',
	color_p4 = 'red',
	color_of_steps = [color_p4, color_p1, color_p2, color_p3],
	color_of_steps_p1 = 0,
	color_of_steps_p2 = 1,
	color_of_steps_p3 = 2,
	color_of_steps_p4 = 3,
	/* common */
	common_distance = 10,
	/* container chess */
	container_chess_sky_prefix = 'sk',
	container_chess_land_prefix = 'ld',
	container_chess_goal_prefix = 'go',
	container_chess_sky_points = [3, 16, 29, 42];
	container_chess_common_radius = 19,
	container_chess_common_startAngle = 0,
	container_chess_common_endAngle = 2 * Math.PI,
	container_chess_common_distance = 2 * container_chess_common_radius,
	container_chess_common_distance_2 = common_distance % (container_chess_common_radius / common_distance),
	container_chess_c1_x = container_chess_common_radius,
	container_chess_c1_y = container_chess_common_radius,
	container_chess_c2_x = container_chess_c1_x + container_chess_common_distance + common_distance,
	container_chess_c2_y = container_chess_c1_y,
	container_chess_c3_x = container_chess_c1_x,
	container_chess_c3_y = container_chess_c2_y + container_chess_common_distance + common_distance,
	container_chess_c4_x = container_chess_c2_x,
	container_chess_c4_y = container_chess_c3_y,
	/* container home */
	container_home_prefix = 'ba',
	container_home_common_distance = common_distance * 2 - container_chess_common_radius,
	container_home_common_width = number_of_chess * container_chess_common_radius + common_distance,
	container_home_common_height = container_home_common_width,
	container_home_p1_x = container_chess_common_radius + container_chess_common_distance,
	container_home_p1_y = container_chess_common_radius,
	container_home_p2_x = 13 * container_chess_common_distance + common_distance,
	container_home_p2_y = container_home_p1_y + container_home_common_distance,
	container_home_p3_x = container_home_p2_x - container_chess_common_distance,
	container_home_p3_y = container_home_p2_x,
	container_home_p4_x = container_home_p1_x - container_chess_common_distance,
	container_home_p4_y = container_home_p3_y,
	/* container player chess */
	container_start_prefix = 'to',
	container_start_common_distance = 3 * container_chess_common_radius,
	container_start_p1_x = container_chess_c3_x + container_chess_common_distance_2,
	container_start_p1_y = container_chess_c3_y + container_start_common_distance + common_distance,
	container_start_p1_p = 0,
	container_start_p2_x = container_chess_c1_x - container_start_common_distance - common_distance + container_chess_common_distance_2,
	container_start_p2_y = container_chess_c1_y,
	container_start_p2_p = 3,
	container_start_p3_x = container_chess_c2_x,
	container_start_p3_y = container_chess_c2_y - container_start_common_distance - common_distance,
	container_start_p3_p = 6,
	container_start_p4_x = container_start_p3_x + container_start_common_distance + common_distance,
	container_start_p4_y = container_chess_c3_y,
	container_start_p4_p = 9,
	/* canvas */
	canvas_width = container_home_p2_x + container_home_common_width,
	canvas_height = container_home_p2_x + (2 * container_home_common_width),
	/* players */
	p1, p2, p3, p4,
	/* color */
	system_color = '#1520ffb3',
	system_default_color = '#565656',
	system_alert_color = '#ce0000';

var sessionId,
	gameId,
	diceInterval,
	countDownInterval,
	index,
	dices = ['&#9856;', '&#9857;', '&#9858;', '&#9859;', '&#9860;', '&#9861;' ],
	colors =	["yellow", "blue", "green", "red" ],
	sockjs,
	stompClient;

var board,
	boardChess,
	boardHover;

window.addEventListener('load', () => {
	board = document.getElementById("board");
	boardChess = document.getElementById('board-chess');
	boardHover = document.getElementById('board-hover');

	/* board */
	resizeCanvas(board);
	resizeCanvas(boardChess);
	resizeCanvas(boardHover);
	elementlDisabled('roll', true);

	var i;
	/* player */
	for (i = 1; i <= number_of_player; i++) {
		this[`p${i}`] = new Player(`Player ${i}`, this[`number_of_steps_p${i}`], this[`turn_of_steps_p${i}`],
			this[`color_p${i}`], this[`color_of_steps_p${i}`],
			new Container_Home(this[`container_home_p${i}_x`], this[`container_home_p${i}_y`]),
			new Container_Start(this[`container_start_p${i}_x`], this[`container_start_p${i}_y`], this[`container_start_p${i}_p`])
		);

		for (j = 1; j <= number_of_chess; j++)
			this[`p${i}`].addChess(new Chess(`Chess ${j}`, this[`container_chess_c${j}_x`], this[`container_chess_c${j}_y`]))
	}
	
	drawCanvas(board, boardChess);
})

var join = (name) => {
	sockjs = new SockJS('/aeroplanechess-websocket'),
	stompClient = Stomp.over(sockjs);

	/* socket */
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);
		sessionId = /\/([^\/]+)\/websocket/.exec(sockjs._transport.url)[1];
		console.log("connected, session id: " + sessionId);

		// TODO review error handling
		stompClient.subscribe(`/game/${gameId}/error`, function(res) {
			var body = JSON.parse(res.body)
			console.log(body);
			appendSystemMessage(`Error, ${body.message}`);
		});

		stompClient.subscribe(`/game/joined-${sessionId}`, function(res) {
			res = JSON.parse(res.body);
			gameId = res["game-id"];
			if(res.error) {
				// TODO review error handling
				appendSystemMessage('Error, the game is full or not existing in waiting game list.')
				return;
			}
			index = res.index + 1;
			joined();
		});

		stompClient.send(`/app/join/${gameId}/${name}`);
	});
}

var start = () => {
	var name = document.getElementById('name').value
	join(name);
	document.getElementById('board-mask').style.display = 'none';
	appendSystemMessage(`Welcome ${name}!`);
	appendSystemMessage('Waiting player to join...');
}

var resizeCanvas = (canvas) => {
	canvas.width = canvas_width;
	canvas.height = canvas_height;
}

var drawCanvas = (canvas, canvas_top) => {
	var ctx = canvas.getContext('2d'), ctx_top = canvas_top.getContext('2d'), player, chess, i, j, k, x, y, p, t, n, nc, sc, color, c, s;
	for (i = 1; i <= number_of_player; i++)	{
		player = this[`p${i}`];
		nc = 0;
	
		color = player.color;
		ctx.strokeStyle = color;
		ctx.lineWidth = 2;
		ctx.fillStyle = color;
		ctx.shadowColor = '#999';
		ctx.shadowBlur = 0;
		ctx.shadowOffsetX = 0;
		ctx.shadowOffsetY = 0;
		ctx_top.strokeStyle = '#999';
		ctx_top.lineWidth = 0.5;
		ctx_top.fillStyle = color;
		ctx.globalAlpha = 0.4;
		ctx_top.font = 'bold 30px Arial';
		ctx_top.shadowColor = '#999';
		ctx_top.shadowBlur = 2;
		ctx_top.shadowOffsetX = 2;
		ctx_top.shadowOffsetY = 2;
		// container home
		ctx.fillRect(player.container_home.x, player.container_home.y, container_home_common_width, container_home_common_height);
		ctx.fillStyle = 'white';
		// chess home point
		for (j = 0; j < player.chess.length; j++)	{
			ctx.beginPath();
			ctx.arc(player.container_home.x + player.chess[j].x, player.container_home.y + player.chess[j].y, container_chess_common_radius, container_chess_common_startAngle, container_chess_common_endAngle);
			ctx.stroke();
			ctx.fill();
			ctx_top.beginPath();
			ctx_top.fillText(j + 1, player.container_home.x + player.chess[j].x - common_distance, player.container_home.y + player.chess[j].y + common_distance);
			ctx_top.strokeText(j + 1, player.container_home.x + player.chess[j].x - common_distance, player.container_home.y + player.chess[j].y + common_distance);
			player.addFlow(`${container_home_prefix}${j}`, player.container_home.x + player.chess[j].x, player.container_home.y + player.chess[j].y);
		}
		// chess start point
		x = player.container_home.x + player.container_start.x;
		y = player.container_home.y + player.container_start.y;
		ctx.beginPath();
		ctx.shadowColor = '#999';
		ctx.shadowBlur = 2;
		ctx.shadowOffsetX = 2;
		ctx.shadowOffsetY = 2;
		ctx.arc(x, y, container_chess_common_radius, container_chess_common_startAngle, container_chess_common_endAngle);
		ctx.strokeStyle = color;
		ctx.fillStyle = color;
		ctx.fill();
		ctx.stroke();
		player.addFlow(`${container_start_prefix}${i - 1}`, x, y);
	
		// chess point
		if (i < 5) {
			p = player.container_start.p;
			s = player.number_of_steps;
			c = player.color_of_steps;
			sc = (i - 1)* 5;
			for (j = 0; j < this[`number_of_steps_${s}`].length; j++) {
				t = j === 0? player.turn_of_steps : turn_of_steps[p];
				for (k = 0; k < this[`number_of_steps_${s}`][j]; k++) {
					color = (j + 1) >= this[`number_of_steps_${s}`].length - 1? player.color : color_of_steps[c];
					n = turn[t](x, y, container_chess_common_distance);
					x = n.x;
					y = n.y;
					ctx.beginPath();
					ctx.arc(x, y, container_chess_common_radius, container_chess_common_startAngle, container_chess_common_endAngle);
					ctx.strokeStyle = color;
					ctx.globalAlpha = (j + 1) >= this[`number_of_steps_${s}`].length - 1? 0.4 : 0.2;
					ctx.fillStyle = color;
					ctx.fill();
					ctx.stroke();
					if (j === this[`number_of_steps_${s}`].length - 1) {
						player.addFlow(`${container_chess_goal_prefix}`, x, y);
					} else if (j === this[`number_of_steps_${s}`].length - 2) {
						player.addFlow(`${container_chess_land_prefix}${sc + k}`, x, y);
					} else {
						var calc = container_chess_sky_points[i - 1] + nc >= 52? (container_chess_sky_points[i - 1] + nc) % 52 : container_chess_sky_points[i - 1] + nc;
						player.addFlow(`${container_chess_sky_prefix}${calc}`, x, y);
					}
					c++;
					if (c === number_of_player)
						c = 0;
					nc++;
				}
				if (j != 0) {
					p++;
					if (p === turn_of_steps.length)
					p = 0;
				}
			}
		}
	}
}

var joined = () => {
	stompClient.subscribe(`/game/${gameId}/player-list`, function(res) {
		var name,
			player,
			players = JSON.parse(res.body).players;

		for(var i in players) {
			player = players[i];
			if(player == null)
				name = '(empty)';
			else {
				name = player.name;
				if(sessionId == player.sessionId)
					name += " (You)";
			}
			document.getElementById(`p${Number(i) + 1}-info-name`).innerHTML = name;
		}
	});

	stompClient.subscribe(`/game/${gameId}/roll-result`, function(res) {
		var body = JSON.parse(res.body);
		if (index === (body.current + 1)) {
			appendSystemMessage(`You: \t roll ${body.roll}`);
		} else {
			appendSystemMessage(`Player ${body.current + 1}: \t roll ${body.roll}`);
		}
		clearInterval(diceInterval);
		dice.innerHTML = dices[Number(body.roll) - 1];
	});
	
	stompClient.subscribe(`/game/${gameId}/move-${sessionId}`, function(res) {
		appendSystemMessage('Choose a chess to move!', system_alert_color);
		countDown(() => {
			Array.from(document.getElementById(`p${index}`).getElementsByClassName('chess')).every((element) => {
				if (!element.disaled) {
					element.click();
					return true;
				}
			})
		})
	});

	stompClient.subscribe(`/game/${gameId}/move-result`, function(res) {
		appendSystemMessage('System: \t Moved');
		ctx_top = boardChess.getContext('2d');
		ctx_top.clearRect(0, 0, boardChess.width, boardChess.height);
		infoDisabled(true);
		if(!res.leaved) {
			countDown(() => {
				console.log(document.getElementById('roll'));
				if(!document.getElementById('roll').disabled)
					document.getElementById('roll').click();
			});
		} else
			appendSystemMessage(`${colors[res.leaved]} leave the game, all of the aeroplanes of that player back to the base.`);
		var aeroplanes = JSON.parse(res.body).aeroplanes,
			colorCount = -1,
			player, player_flow, player_chess, player_pos;
		ctx_top.font = 'bold 30px Arial';
		for(var i = 0 in aeroplanes) {
			player = this[`p${aeroplanes[i].color + 1}`];
			ctx_top.beginPath();
			ctx_top.strokeStyle = player.color || 'white';
			ctx_top.fillStyle = player.color;
			player_chess = document.getElementById(`p${aeroplanes[i].color + 1}-c${i - (aeroplanes[i].color * 4)}`);
			if (aeroplanes[i].inCellId.indexOf(container_chess_goal_prefix) === 0) {
				player_pos = container_chess_goal_prefix;
				player_chess.style.background = 'grey';
			} else if (aeroplanes[i].inCellId.indexOf(container_home_prefix) === 0) {
				player_pos = `${container_home_prefix}${i - (aeroplanes[i].color * 4)}`;
			} else {
				player_pos = aeroplanes[i].inCellId;
			}
			player_flow = player.flow[player_pos];
			player_chess.dataset.pos = player_pos;
			ctx_top.fillText(i - (aeroplanes[i].color * 4) + 1, player_flow.x - common_distance, player_flow.y + common_distance);
			ctx_top.strokeText(i - (aeroplanes[i].color * 4) + 1, player_flow.x - common_distance, player_flow.y + common_distance);
		}
		rollDice();
	});

	stompClient.subscribe(`/game/${gameId}/start`, function(res) {
		appendSystemMessage('Game Start!', system_color);
		var ctx_hover = boardHover.getContext('2d');
		Array.from(document.getElementById(`p${index}`).getElementsByClassName('chess')).forEach((element) => {
			element.addEventListener('click', () => {
				stompClient.send(`/app/move/${gameId}/${(Number(element.value) - 1)}`);
				infoDisabled(true);
				chessDisabled(true);
			})
		})
		Array.from(document.getElementsByClassName('chess')).forEach((element) => {
			element.addEventListener('mouseover', () => {
				if (!element.dataset.pos || element.dataset.pos.indexOf(container_home_prefix) === 0)
					return;
				var player = this[element.id.substr(0, 2)],
					pos = player.flow[element.dataset.pos];
				ctx_hover.beginPath();
				ctx_hover.fillStyle = player.color;
				ctx_hover.arc(pos.x, pos.y, container_chess_common_radius, container_chess_common_startAngle, container_chess_common_endAngle);
				ctx_hover.fill();
			})
			element.addEventListener('mouseout', () => {
				ctx_hover.clearRect(0, 0, boardHover.width, boardHover.height);
			})
		})
		countDown(() => {
			if(!document.getElementById('roll').disabled)
				document.getElementById('roll').click();
		})
		rollDice();
	});

	stompClient.subscribe(`/game/${gameId}/your-turn-${sessionId}`, function(res) {
		appendSystemMessage('Your Turn!', system_alert_color);
		infoDisabled(false);
		elementlDisabled('roll', false);
	});
	
	stompClient.subscribe(`/game/${gameId}/won`, function(res) {
		// TODO show the game is end, all disable
		body = JSON.parse(res.body);
		appendSystemMessage(`${colors[body['player-won']]} has won the game!!!`);
	});

	stompClient.send(`/app/ready/${gameId}`);
}

var roll = () => {
	stompClient.send(`/app/roll/${gameId}`);
	elementlDisabled('roll', true);
	chessDisabled(false);
}

var rollDice = () => {
	dice = document.getElementById('dice');
	diceInterval = setInterval(() => {
		var random = Math.floor(Math.random() * 6);
		dice.innerHTML = dices[random];
	}, 50)
}

var countDown = (fn) => {
	clearInterval(countDownInterval);
	count = 5;
	countDownInterval = setInterval(() => {
			document.getElementById('count').innerHTML = count;
			if (count === 0) {
				if (fn)
					fn();
				clearInterval(countDownInterval);
			}
			count--;
		}, 1000);
}

var elementlDisabled = (id, disabled) => {
	document.getElementById(id).disabled = disabled;
}

var chessDisabled = (disabled) => {
	Array.from(document.getElementById(`p${index}`).getElementsByClassName('chess')).forEach((element) => {
		element.style.opacity = disabled? 0.3 : 1;
		element.disabled = window.getComputedStyle(element)['background-color'] === 'rgb(128, 128, 128)'? true : disabled;
	})
}

var infoDisabled = (disabled) => {
	if (disabled) {
		Array.from(document.getElementsByClassName('info-turn')).forEach((element) => {
			element.style.display = 'none';
		})
	} else {
		document.getElementById(`p${index}-info-turn`).style.display = 'block';
	}
}

var appendSystemMessage = (text, color) => {
	var container = document.getElementById('wrapper-system'),
		element = document.createElement('div');
	element.innerHTML = color? 'Kevin: \t' + text : text;
	element.style.color = color? color : system_default_color;
	container.append(element);
	container.scrollTop = container.scrollHeight;
}

var turn = {
	r: (x, y, n, s) => {
		return {
			x: x + n + (s || 0),
			y: y
		}
	},
	l: (x, y, n, s) => {
		return {
			x: x - n - (s || 0),
			y: y
		}
	},
	u: (x, y, n, s) => {
		return {
			x: x,
			y: y - n - (s || 0)
		}
	},
	d: (x, y, n, s) => {
		return {
			x: x,
			y: y + n + (s || 0)
		}
	}
}

class Player {
	constructor(name, number_of_steps, turn_of_steps, color, color_of_steps, container_home, container_start) {
		this.name = name;
		this.number_of_steps = number_of_steps;
		this.turn_of_steps = turn_of_steps;
		this.color = color;
		this.color_of_steps = color_of_steps;
		this.container_home = container_home;
		this.container_start = container_start;
		this.chess = [];
		this.flow = {};
	}

	addChess(chess) {
		this.chess.push(chess);
	}

	addFlow(id, x, y) {
		this.flow[id] = {
			x: x,
			y: y
		}
	}
}

class Chess {
	constructor(name, x, y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}
}

class Container_Home {
	constructor(x, y) {
		this.x = x;
		this.y = y;
	}
}

class Container_Start {
	constructor(x, y, p) {
		this.x = x;
		this.y = y;
		this.p = p;
	}
}
