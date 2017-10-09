package com.aeroplanechess.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GameController {

	@RequestMapping(value = "/")
	public String index(HttpServletRequest request, Model model, @RequestParam(defaultValue = "null") String gameId) {
		model.addAttribute("gameId", gameId);
		return "index";
	}

}
