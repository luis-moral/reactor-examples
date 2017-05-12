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
package es.molabs.reactor.examples.util.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import es.molabs.reactor.examples.util.Repository;
import es.molabs.reactor.examples.util.RepositoryPublisher;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@RunWith(JUnit4.class)
public class RepositoryTest 
{
	private Repository<Integer, String> repository;
	private RepositoryPublisher<Integer, String> repositoryPublisher;
	
	@Test
	public void testEmitMono()
	{
		Integer key = 1;
		
		StepVerifier.create(repository.get(key))
			.expectNextCount(1)
			.expectComplete()
			.verify();
		
		Mockito.verify(repositoryPublisher, Mockito.times(1)).publish(Mockito.anyInt());
	}
	
	@Test
	public void testEmitFlux()
	{
		Integer[] keys = new Integer[] {1, 2, 3};
		
		StepVerifier.create(repository.get(keys))
			.expectNextCount(keys.length)
			.expectComplete()
			.verify();
		
		Mockito.verify(repositoryPublisher, Mockito.times(keys.length)).publish(Mockito.anyInt());
	}
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp()
	{
		repositoryPublisher = Mockito.mock(RepositoryPublisher.class);
		Mockito.doReturn(Mono.just("value")).when(repositoryPublisher).publish(Mockito.anyInt());
		
		repository = new Repository<Integer, String>(repositoryPublisher);
	}
}