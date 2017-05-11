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
package es.molabs.reactor.examples.util;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Repository<K, V> 
{
	private final RepositoryPublisher<K, V> repositoryPublisher;
	
	public Repository(RepositoryPublisher<K, V> repositoryEmitter)
	{
		this.repositoryPublisher = repositoryEmitter;
	}
	
	public Mono<V> get(K key)
	{
		return Mono.fromCallable(() -> repositoryPublisher.publish(key));
	}
	
	@SuppressWarnings("unchecked")
	public Flux<V> get(K...keys)
	{
		return Flux.fromArray(keys)
					.map(key -> repositoryPublisher.publish(key));
	}
}
