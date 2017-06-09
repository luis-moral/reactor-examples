package es.molabs.reactor.examples.repository;

public abstract class BaseRepository<K, V> implements Repository<K, V> 
{
	private final RepositoryPublisher<K, V> repositoryPublisher;
	
	protected BaseRepository(RepositoryPublisher<K, V> repositoryPublisher)
	{
		this.repositoryPublisher = repositoryPublisher;
	}
	
	protected RepositoryPublisher<K, V> getRepositoryPublisher()
	{
		return repositoryPublisher;
	}
}