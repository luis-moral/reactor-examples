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
package es.molabs.reactor.examples.repository.test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import es.molabs.reactor.examples.repository.DelayRepository;
import es.molabs.reactor.examples.repository.RepositoryPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnit4.class)
public class DelayRepositoryTest 
{
	private final static long DELAY = 50;
	private final static long DELAY_MARGIN = 50;
	private final static TimeUnit DELAY_TIME_UNIT = TimeUnit.MILLISECONDS;
	
	private DelayRepository<Integer, String> delayRepository;
	private RepositoryPublisher<Integer, String> repositoryPublisher;
	
	@Test
	public void testEmitMono()
	{
		Integer key = 5;
		
		Mockito
			.doReturn(Mono.just("mock_value"))
			.when(repositoryPublisher)
			.getMono(key);		
		
		StepVerifier.create(delayRepository.getMono(key))
			.expectNextCount(1)
			.expectComplete()
			.verifyThenAssertThat()
			.tookMoreThan(Duration.ofMillis(DELAY))
			.tookLessThan(Duration.ofMillis(DELAY + DELAY_MARGIN));
		
		Mockito.verify(repositoryPublisher, Mockito.times(1)).getMono(key);
	}
	
	@Test
	public void testEmitFlux()
	{
		Integer key = 5;
		List<String> valueList = Arrays.asList("mock_value1", "mock_value2", "mock_value3");
		
		Mockito
			.doReturn(Flux.fromIterable(valueList))
			.when(repositoryPublisher)
			.getFlux(key);		
		
		StepVerifier.create(delayRepository.getFlux(key))
			.expectNextCount(valueList.size())
			.expectComplete()
			.verifyThenAssertThat()
			.tookMoreThan(Duration.ofMillis(DELAY * valueList.size()))
			.tookLessThan(Duration.ofMillis((DELAY * valueList.size()) + DELAY_MARGIN));
		
		Mockito.verify(repositoryPublisher, Mockito.times(1)).getFlux(key);
	}
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		repositoryPublisher = Mockito.mock(RepositoryPublisher.class);		
		
		delayRepository = new DelayRepository<Integer, String>(repositoryPublisher, DELAY, DELAY_TIME_UNIT);
	}
}