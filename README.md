# spring-data-jdbc motivation
[spring-data-commons](http://projects.spring.io/spring-data/) framework를 확장하여 [spring-data-jpa](http://projects.spring.io/spring-data-jpa/)와 같은 기능을 제공 하고자 합니다.
현재는 [spring-data-jpa](http://projects.spring.io/spring-data-jpa/)와 같은 모든 기능을 제공하지 않습니다.
구현을 하면서, 이럴 바에 [JPA](http://www.tutorialspoint.com/jpa/index.htm)를 사용하고 말지 라는 생각을 하곤 합니다.

[MyBatis](http://blog.mybatis.org/)를 사용하거나, [Hibernate](http://hibernate.org)와 [JdbcTemplate](https://github.com/spring-projects/spring-framework/blob/master/spring-jdbc/src/main/java/org/springframework/jdbc/core/JdbcTemplate.java)을 혼용 하여 사용 하는 방식 보다
[Spring](https://spring.io)에서 제공하는 막강 파워 [JdbcTemplate](https://github.com/spring-projects/spring-framework/blob/master/spring-jdbc/src/main/java/org/springframework/jdbc/core/JdbcTemplate.java) 만을 가지고 할 수는 없을까 하고, 고민을 합니다.

이것을 만들기 까지 아래의 글과 소스코드를 참고 하고, 일부 소스는 변경없이 인용 하였습니다.<br>
[Java에서 XML없이 SQL개발하기 by 정상혁](http://blog.benelog.net/2708621)<br>
[benelog/multiline](https://github.com/benelog/multiline)<br>
[jirutka/spring-data-jdbc-repository](https://github.com/jirutka/spring-data-jdbc-repository)

# candidate domain entity class
[Persistable](https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/domain/Persistable.java) or [JdbcPersistable](https://github.com/jihwan/spring-data-jdbc/blob/master/src/main/java/org/springframework/data/jdbc/domain/JdbcPersistable.java) interface를 구현 해야 하는 제약 사항이 있습니다.
두 interface는 database table의 primary key field 생성 전략에 따라 선택 되어야 하고, 그에 따라 `isNew()` 메소드를 구현 해 주어야 하는 규칙 혹은 제약 사항이 있습니다. 
### 자동 증가 key field
pk 값은 insert시 dbms로 부터 할당 받습니다. 그렇다면 java 객체가 database에 영속 되기 직전에는 값이 null입니다.
따라서, `isNew()` 구현은 java id 멤버변수에 대한 null 테스트 코드로 구현하면 됩니다.
```java
@Persistent @Data
public class Zoo implements Persistable<Long> {

	@Id
	private Long id;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}
}
```
### 수동 설정 key field
application business logic에서 생성한 값으로 database pk로 사용할 경우는, 앞의 자동증가 key field에서 알아 보았듯이 java 객체 id 멤버 변수의 null 테스트 코드로는 불가능 합니다. 영속화 상태 자체를 정보로 가지고 있어야 합니다.
따라서, entity 객체는 `boolean persisted = false;` 와 같이 영속 상태 관리를 합니다. persisted field 상태 수정은 [SimpleJdbcRepository](https://github.com/jihwan/spring-data-jdbc/blob/master/src/main/java/org/springframework/data/jdbc/repository/support/SimpleJdbcRepository.java) 내부에서 이루어 지도록 하였습니다.

```java
@Persistent @Data
public class Foo implements JdbcPersistable<Foo, Bar> {

	private transient boolean persisted; // 영속화 대상 field가 아니므로 transient modifier 사용
	
	@Id
	Bar bar;
	
	@Reference
	Address addR;
	
	String name;

	public Foo() {}
	
	public Foo(Bar bar) {
		this.bar = bar;
	}
	
	public static Foo instance(Bar bar) {
		Foo foo = new Foo(bar);
		return foo;
	}
	
	@Override
	public Bar getId() {
		return this.bar;
	}
	
	@Override @Transient
	public boolean isNew() {
		return !persisted;
	}

    // SimpleJdbcRepository 내부에서 영속화 제어를 한다.
    // application 코드에서는 이 함수를 호출 하면 안되는 제약 사항이 있음
	@Override
	public void persist(boolean persisted) {
		this.persisted = persisted;
	}
}
```
# 테스트 코드
[Go](https://github.com/jihwan/spring-data-jdbc/blob/master/src/test/java/org/springframework/data/jdbc/repository/JdbcRepositoriesTest.java)
