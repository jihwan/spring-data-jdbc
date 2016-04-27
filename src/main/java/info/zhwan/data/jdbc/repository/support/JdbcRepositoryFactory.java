package info.zhwan.data.jdbc.repository.support;

import org.springframework.aop.framework.ProxyFactory;

public class JdbcRepositoryFactory {

	@SuppressWarnings("unchecked")
	public <T> T getRepository(Class<T> repositoryInterface) {
		
		Object target = getTargetRepository();
		
		ProxyFactory proxy = new ProxyFactory();
		proxy.setTarget(target);
		proxy.setInterfaces(repositoryInterface);
//		proxy.addAdvice(new JdbcRepositoryAdvice(target));
		
		return (T) proxy.getProxy();
	}
	
	@SuppressWarnings("rawtypes")
	private Object getTargetRepository() {
		SimpleJdbcRepository<?, ?> simpleJdbcRepository = new SimpleJdbcRepository();
		return simpleJdbcRepository;
	}
}
