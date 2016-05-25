# spring-data-jdbc motivation
spring-data-commons framework를 확장하여 spring-data-jpa와 같은 기능을 제공 하고자 합니다.
현재는 spring-data-jpa와 같은 모든 기능을 제공하지 않습니다.
구현을 하면서, 이럴 바에 jpa를 사용하고 말지 라는 생각을 하곤 합니다.

iBatis or MyBatis를 사용하거나, hibernate와 jdbcTemplate을 혼용 하여 사용 하는 방식 보다
spring에서 제공하는 막강 파워 jdbcTemplate 만을 가지고 할 수는 없을까 하고, 고민을 합니다.

이것을 만들기 까지 아래의 글과 소스코드를 참고 하고, 일부 소스는 변경없이 인용 하였습니다.
[Java에서 XML없이 SQL개발하기 by 정상혁](http://blog.benelog.net/2708621)<br>
[benelog/multiline](https://github.com/benelog/multiline)<br>
[jirutka/spring-data-jdbc-repository](https://github.com/jirutka/spring-data-jdbc-repository)

# candidate domain entity class
JdbcPersistable or Persistentable interface를 구현 해야 하는 제약 사항이 있습니다.

key가 되는 field에는 Id annotation을 붙여 줘야 합니다.

모든 java class의 field명은 database table의 field명과 같아야 하는 제약 사항도 있습니다.

역시 이 모든게 불변합니다. jpa와 jpa 구현체를 학습 하는 것이 더 좋겠습니다.

