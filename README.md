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

# spring-data-jdbc 구현 하기
> [spring-data-jpa](http://projects.spring.io/spring-data-jpa/) 기술을 기반으로, spring-data-jdbc의 핵심 기능을 이야기 하고자 한다.
> 



# 참고
[spring-data-rest sample](https://spring.io/guides/gs/accessing-data-rest/)