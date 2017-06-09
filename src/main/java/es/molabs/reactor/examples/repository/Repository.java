package es.molabs.reactor.examples.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Repository<K, V> 
{
	Mono<V> getMono(K key);
	
	Flux<V> getFlux(K key);
}
