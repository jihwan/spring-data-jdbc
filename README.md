# spring-data-jdbc
spring-data-jpa 기능처럼 JdbcTemplate을 이용하여 구현하고자 합니다.

# Spring Proxy
> spring에서 proxy object 생성시 사용되는 기술은 두가지 이다.
> 첫번째는 jdk proxy 기법이고, 두번째는 CGLIB 를 이용한 기법이다.
> spring에서는 proxy 객체 생성시 ProxyFactory를 이용하여 일관된 기법으로 proxy 객체 생성이 가능하다.
> 주의 할 점은, interface 지정 여부에 따라, proxy 객체 생성 기법이 다르다는 점이다.
> target class가 interface implementation 여부는 상관이 없다.
> interface 지정시는 jdk proxy 방법으로, 지정 하지 않으면 CGLIB 방법으로 proxy 객체가 생성 된다.

# 오늘 이야기 할 proxy 이야기
> proxy하면 떠오르는 것이 바로 AOP 기술이다. 나 역시 책이나 웹검색을 통해 지금까지 얻은 지식이 AOP 기술에 국한되어 있었다.
> 하지만 지금 부터 이야기 하고자 하는 것은 [spring-data-jpa](http://projects.spring.io/spring-data-jpa/)에서 구현한 기술이다.
> 최근, [spring](http://spring.io "spring") 페이지에 들어가 보니 [spring-data-rest](http://projects.spring.io/spring-data-rest/) 기술까지 선보이고 있다. 과거에 [OSAF](http://whiteship.me/?cat=1777 "OSAF")를 보면서 대단하다고 생각 했던 것들이, 생각을 어떻게 하느냐에 따라 점점 더 좋은 기술들을 만들어 낼 수 있구나 라는 생각이 들곤 한다.
> 이 프로젝트를 만든 개발자는 DRY(Don't Repeat Yourself)한 코드 작성보다는, 업무로직에 집중할 수 있도록 하고자 상당히 많은 고민을 한 것 같다.

# spring-data-jdbc 핵심 구현 code
> [spring-data-jpa](http://projects.spring.io/spring-data-jpa/) 기술을 기반으로, spring-data-jdbc의 핵심 기능을 이야기 하고자 한다.
> 앞에서 이야기 하였듯이, 나는 proxy 기능을 이용할 때 AOP 접근법이 머리에 박혀 있어서(사실 고민을 하지 않았다.), InvocationHandler 를 implementation 하여, 어떤 기능을 삽입할 것인가에 대한 생각만 하곤 했다. 관점을 달리해서, 일반적인 공통 기능을 하는 기본 문맥을 기반으로 하여, java Generic을 활용한다면 더 멋진 기능을 구현 할 수 있었을 텐데 말이다.

> 먼저 [spring-data-jpa](http://projects.spring.io/spring-data-jpa/)의 핵심 기능을 아래 몇가지 코드를 가지고 이해 해 보도록 하자. 가장 핵심되는 코드는, [RepositoryFactorySupport](https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/repository/core/support/RepositoryFactorySupport.java) 라고 생각된다.
	
	public <T> T getRepository(Class<T> repositoryInterface, Object customImplementation) {

		RepositoryMetadata metadata = getRepositoryMetadata(repositoryInterface);
		Class<?> customImplementationClass = null == customImplementation ? null : customImplementation.getClass();
		RepositoryInformation information = getRepositoryInformation(metadata, customImplementationClass);

		validate(information, customImplementation);

		Object target = getTargetRepository(information);

		// Create proxy
		ProxyFactory result = new ProxyFactory();
		result.setTarget(target);
		result.setInterfaces(new Class[] { repositoryInterface, Repository.class });

		result.addAdvice(ExposeInvocationInterceptor.INSTANCE);

		if (TRANSACTION_PROXY_TYPE != null) {
			result.addInterface(TRANSACTION_PROXY_TYPE);
		}

		for (RepositoryProxyPostProcessor processor : postProcessors) {
			processor.postProcess(result, information);
		}

		if (IS_JAVA_8) {
			result.addAdvice(new DefaultMethodInvokingMethodInterceptor());
		}

		result.addAdvice(new QueryExecutorMethodInterceptor(information, customImplementation, target));

		return (T) result.getProxy(classLoader);
	}

> 모든것을 다 만들기는 어려우므로 가장 핵심이 되는 기능을 구현하기로 한다.
> [테스트코드]()

# 참고
[spring-data-rest sample](https://spring.io/guides/gs/accessing-data-rest/)