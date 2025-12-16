package com.insightcrew.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.insightcrew.service.TripImportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TripImportController {

	private final TripImportService service;

	@GetMapping("/import/trips")
	public String importTrip() {
	    service.importAll();
	    return "OK";
	}
}
