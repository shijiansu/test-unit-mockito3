# How to know the feature and the examples

- See this source code, it is the best document - <https://github.com/mockito/mockito/blob/release/3.x/src/main/java/org/mockito/Mockito.java>
- References
  - <https://site.mockito.org/>
  - <https://dzone.com/refcardz/mockito>
  - BDD style - <https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/BDDMockito.html>

# Concept

Test doubles can be divided into a few groups:

- Dummy - an empty object passed in an invocation (usually only to satisfy a compiler when a method ar- gument is required)
- Fake - an object having a functional implementation, but usually in a simplified form, just to satisfy the test (e.g., an in-memory database)
- Stub - an object with hardcoded behavior suitable for a given test (or a group of tests)
- Mock - an object with the ability to a) have a programmed expected behavior, and b) verify the interactions occurring in its lifetime (this object is usually created with the help of mocking framework)
- Spy - a mock created as a proxy to an existing real object; some methods can be stubbed, while the un- stubbed ones are forwarded to the covered object

also can view more details from - <https://martinfowler.com/articles/mocksArentStubs.html>

# Limitations

Mockito cannot:

- mock final classes (Since 2.1.0 - 通过 mockito-inline 实现)
- mock enums (Since 2.1.0 - 通过 mockito-inline 实现)
- mock final methods (Since 2.1.0 - 通过 mockito-inline 实现)
- mock static methods (Since 3.4.0 - 通过 mockito-inline 实现)
- mock private methods
- mock hashCode() and equals()

# How to enable inline mock maker

org.mockito.exceptions.base.MockitoException: 
The used MockMaker SubclassByteBuddyMockMaker does not support the creation of static mocks

Mockito's inline mock maker supports static mocks based on the Instrumentation API.
You can simply enable this mock mode, by placing the 'mockito-inline' artifact where you are currently using 'mockito-core'.
Note that Mockito's inline mock maker is not supported on Android.

方法1 - 使用mockito-inline依赖取代mockito-core

- `2.7.6` 版本开始 - <https://www.cnblogs.com/yanlongpankow/p/7226895.html>
- <https://mvnrepository.com/artifact/org.mockito/mockito-inline>

方法2 - 通过mockito extension mechanism - <https://www.cnblogs.com/yanlongpankow/p/6823223.html>

Mockito使用cglib子类化来实现mocking，从而无法支持 mocking final 类型和方法。为了改变这种状况，Rafael Winterhalter在Mockito `2.1.0`版本里创建了第二种 mock方法叫Inline mock maker。

Inline mock maker的奇妙之处在于它实现了Mockito对字节码产生的有效支持，这种支持通过类InlineByteBuddyMockMake来实现。在InlineByteBuddyMockMake类中，定义了一个 Incubating的Annotation@Incubating，这个注释的含义是说InlineByteBuddyMockMake是新创建的类，Mockito开发者还在等待社区使用者的反馈信息. 不仅如此,这个新功能现在还是可选的.也就是说当前缺省情况下，功能是关闭的。为何如此呢，因为它基于完全不同的mocking机制，需要使用者更多的反馈.我们想要使用它来Mocking final 类型和方法时，可以通过`mockito extension mechanism`来激活它。具体操作只有在配置文件`src/test/resources/mockito-extensions/org.mockito.plugins.MockMaker`里加入`mock-maker-inline`这个值作为一行就可以了: `mock-maker-inline` (这个方法在Mockito.java的Javadoc的第39点提出)

## 方法1和方法2如何选用

由于本例子在Maven3的`pom.xml`使用了`mockito-junit-jupiter`, 这个会自动依赖`junit5`的代码库, 所以使用`方法2`启动配置.

在Gradle5里面, 尝试过使用`mockito-junit-jupiter`, 这个时候并没有自动依赖`junit5`的代码库, 而且在Gradle里面使用`junit-jupiter-api`并不足够.
这个时候, 也要手动依赖`junit5`, 可考虑使用`方法1`, 替换`mockito-core`到`mockito-inline`, 毕竟是后期版本出来的功能.

## 启用mock inline以后运行时候有个warning - 未深入分析

```bash
OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended

# 在用的Java版本
penjdk 13.0.2 2020-01-14
OpenJDK Runtime Environment Zulu13.29+9-CA (build 13.0.2+6-MTS)
OpenJDK 64-Bit Server VM Zulu13.29+9-CA (build 13.0.2+6-MTS, mixed mode, sharing)
```

## pom.xml例子

其中依赖的关系是,

- mockito-core
- junit-jupiter-api
- mockito-junit-jupiter (few additional MockitoExtension files)
  - mockito-core
  - junit-jupiter-api
- mockito-inline (only the configuration file to enable mock inline)
  - mockito-core

```xml
  <dependencies>
    <!-- START -->
    <!-- Option 1 -->
    <!-- 使用auto dependencies + extension + configuration to enable mock inline -->
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-junit-jupiter</artifactId>
      <version>3.4.0</version>
      <scope>test</scope>
    </dependency>
    <!-- Option 2 -->
    <!-- 单独包导入, 通过mockito-junit-jupiter查询匹配junit5版本; 可以替换mockito-core为mock inline -->
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-core</artifactId>
      <version>3.4.0</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-junit-jupiter</artifactId>
      <version>3.4.0</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-api</artifactId>
      <version>5.4.2</version>
      <scope>test</scope>
    </dependency>
    <!-- Option 3 -->
    <!-- 如果要使用mockito-inline(其实里面也是依赖mockito-core), 可以排除其他的自动依赖 -->
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-inline</artifactId>
      <version>3.4.0</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.mockito</groupId>
      <artifactId>mockito-junit-jupiter</artifactId>
      <version>3.4.0</version>
      <scope>test</scope>
      <exclusions>
        <exclusion>
          <groupId>org.mockito</groupId>
          <artifactId>mockito-core</artifactId>
        </exclusion>
      </exclusions>
    </dependency>
    <!-- END -->
  </dependencies>
```

# Java development environment

`sdk list java && sdk current java`

# Setup Maven wrapper

- https://github.com/takari/maven-wrapper

`mvn -N io.takari:maven:0.7.7:wrapper -Dmaven=3.5.4`
