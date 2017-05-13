package es.molabs.reactor.examples.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Helper 
{	
	public static <T> T log(T value) 
	{
		System.out.println(Thread.currentThread().getName() + " -> " + value);

		return value;
	}

	public static List<Integer> getRandomIntegerList(int count, int maxValue) 
	{
		Random random = new Random();
		List<Integer> randomList = new ArrayList<>(count);

		for (int i = 0; i < count; i++) {
			randomList.add(random.nextInt(maxValue));
		}

		return randomList;
	}
}
