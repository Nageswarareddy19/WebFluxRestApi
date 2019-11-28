package com.customer.service;

import java.time.Duration;
import java.util.Date;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.model.CustomerEvent;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RestController
public class CustomerWebFluxService {

	@GetMapping(value = "/customerEvent")
	public Mono<CustomerEvent> customerById() {
		return Mono.just(new CustomerEvent(101, "Nreddy", new Date()));
	}

	@GetMapping(value = "/customerEvents", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<CustomerEvent> streamOfCustomer() {
		// create customerEvent object

		Supplier<CustomerEvent> events = () -> {
			CustomerEvent event = new CustomerEvent();
			event.setId(101);
			event.setCustomerName("Nreddy");
			event.setDate(new Date());
			return event;

		};

		// create stream
		Flux<CustomerEvent> eventsOfFlux = Flux.fromStream(Stream.generate(events));

		// create time interval
		Flux<Long> duration = Flux.interval(Duration.ofSeconds(4));
		// combine event of streams and duration using zip
		return Flux.zip(eventsOfFlux, duration).map(Tuple2::getT1);

	}

}
