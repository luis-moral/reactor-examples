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

import java.util.concurrent.TimeUnit;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class DelayRepository<K, V> extends Repository<K, V>
{
	private final long delayInMillis;

	public DelayRepository(RepositoryPublisher<K, V> repositoryPublisher, long delay, TimeUnit timeUnit)
	{
		super(repositoryPublisher);
		
		this.delayInMillis = timeUnit.toMillis(delay);
	}
	
	public Mono<V> get(K key)
	{
		return super.get(key)
				.doOnNext(value -> delayValue(delayInMillis));
	}
	
	@SuppressWarnings("unchecked")
	public Flux<V> get(K...keys)
	{
		return super.get(keys)
				.doOnNext(value -> delayValue(delayInMillis));
	}
	
	private void delayValue(long delayInMillis)
	{
		try
		{
			Thread.sleep(delayInMillis);
		}
		catch (InterruptedException Ie)
		{
			throw new IllegalArgumentException(Ie);
		}
	}
}