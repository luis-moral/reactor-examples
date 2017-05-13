package es.molabs.reactor.examples.groupby;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import es.molabs.reactor.examples.util.Helper;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

public class GroupBySorted 
{
	public static void main(String[] args) 
	{
		AtomicInteger counter = new AtomicInteger(1);

		Flux.fromIterable(Helper.getRandomIntegerList(8, 50))
			.map(value -> Tuples.of(counter.getAndIncrement(), value))
			.groupBy(tuple -> (tuple.getT2() % 2) == 0).flatMap(flux -> 
				{
					if (flux.key()) 
					{
						Flux<Tuple2<Integer, Integer>> cache = flux.cache();
						Flux<Integer> keyTrueFlux = getByCount(cache);

						return cache
								.zipWith(keyTrueFlux, (fluxValue, keyTrueValue) -> Tuples.of(fluxValue.getT1(), keyTrueValue))
								.publishOn(Schedulers.newParallel("keyTrueFlux"))
								.doOnNext(value -> Helper.log(value));
					} 
					else 
					{
						Flux<Tuple2<Integer, Integer>> cache = flux.cache();
						Flux<Integer> keyFalseFlux = getById(cache);

						return cache
								.zipWith(keyFalseFlux, (fluxValue, keyFalseValue) -> Tuples.of(fluxValue.getT1(), keyFalseValue))
								.publishOn(Schedulers.newParallel("keyFalseFlux"))
								.doOnNext(value -> Helper.log(value));
					}
				})
			.publishOn(Schedulers.parallel())
			.sort(Comparator.comparingInt(Tuple2::getT1))
			.doOnNext(value -> Helper.log(value)).blockLast();
	}

	private static Flux<Integer> getByCount(Flux<Tuple2<Integer, Integer>> idFlux) 
	{
		return idFlux.map(id -> 200 + id.getT2());
	}

	private static Flux<Integer> getById(Flux<Tuple2<Integer, Integer>> idFlux) 
	{
		return idFlux.map(id -> 500 + id.getT2());
	}
}
