package com.example.servingwebcontent;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;

@Controller
public class TicketGeneratorController {

	@GetMapping("/generate-tickets")
	public String generateTickets(@RequestParam(name="count", required=false, defaultValue="6") Integer count, Model model) {
		Tambola.Ticket[] tickets = Tambola.getTickets(count);
		System.out.println(Arrays.toString(tickets));
		model.addAttribute("tickets", tickets);
		return "6tickets";
	}

}
