# platform
Development Platform


# Oracle JDBC Driver 다운로드
wget http://www.datanucleus.org/downloads/maven2/oracle/ojdbc6/11.2.0.3/ojdbc6-11.2.0.3.jar
# .m2에 설치
mvn install:install-file \
-Dfile=ojdbc6-11.2.0.3.jar \
-DgroupId=com.oracle \
-DartifactId=ojdbc6 \
-Dversion=11.2.0.3 \
-Dpackaging=jar
```

# display CRLF(^M)
``` bash
Display CRLF as ^M:
:e ++ff=unix
```