/**
 * Copyright (C) 2017 Luis Moral Guerrero <luis.moral@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.examples.zip;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ArrayZip
{
	public static void main(String[] args) throws Exception
	{
		List<Mono<?>> singleList = new LinkedList<Mono<?>>();
		
		for (int i=0; i<5; i++)
		{
			singleList.add(createMono(i));
		}
		
		Flux.merge(
					Flux.zip(singleList, values -> Flux.fromArray(values))
				)
			.publishOn(Schedulers.newParallel("ComplexZip"))		
			.map(value -> Integer.toString((Integer) value))			
			.collect(Collectors.joining(","))
			.map(v -> print(v))
			.block();				
	}
	
	private static Mono<Integer> createMono(int value)
	{
		return Mono.just(value)
				.publishOn(Schedulers.parallel())
				.map(v -> print(v));
	}
	
	private static<T> T print(T value)
	{
		System.out.println(Thread.currentThread().getName() + " -> " + value);
		
		return value;
	}
}