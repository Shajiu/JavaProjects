### 关于LiquiBase的学习笔记
$\quad$ 如果您使用的是Spring，则有可能使用诸如Hibernate，Jooq或Ebean之类的持久性技术从数据库中获取数据。功能来去去去发生变化；数据模型也发生变化，当您需要更改数据模型时会发生什么？您运行数据库迁移以添加或删除列或进行其他更改。
$\quad$过去通常是手动进行，有人会登录到数据库，运行大量SQL，并依赖于您在应用程序中进行了相同的更改。虽然这里还有很多人为错误的余地。通过尝试使数据库更改更安全，我们对此进行了改进。版本控制，可重复性，可测试性；这些概念适用于堆栈的更多部分。包括数据库。
$\quad$Liquibase和Flyway都可以帮助我们进行这些迁移。Spring会依次帮助我们使用Liquibase和Flyway。今天本文针对Liquibase通过实例进行简要介绍。
$\quad$Liquibase是一个用于跟踪、管理数据库变化的开源数据库重构工具。它将数据库的所有变化（包括结构和数据）都保存在XML文件中，便于版本控制。Liquibase不依赖特定数据库，支持常见的12种数据库，包括：Oracle,MySQL, DB2,SQL Server,PostgreSQL,h2等。
### Spring boot中使用LiquiBase
- 添加依赖
```xml
<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
    <version>3.5.3</version>
</dependency>
```
- application.yml配置方式
```xml
可参考路径：src/main/resources/application.yml
```
- java配置方式
```java
可参考路径：com/example/mybatis_plus/mybatis_plus_learn/config/LiquibaseConfig.java
```
- 配置DB change

$\quad$DB配置一般分为master配置和细节配置，master中使用include引入细节配置文件。
- 添加 master.xml
```xml
可参考路径: src/main/resources 目录下 config/liquibase/master.xml
可参考路径: src/main/resources/config/liquibase/master.xml
```
- 添加子配置文件201712022057_add_entity_Base.xml
```xml
可参考路径:src/main/resources/config/liquibase/changelog/201712022057_add_entity_Base.xml
```
执行以上脚本会在DB中生成如下数据表
*  本次待创建的数据表 (tb_member)
*  记录database上执行的所有statements (databasechangelog)
*  用于确保两台机器不会同时修改database  (databasechangeloglock)

Liquibase执行时会检查表databasechangelog，从而只执行没有执行过的changeSet。
### Liquibase构件讲解
$\quad$databasechangelog中包含元素include，property，changeSet，preConditions
- [preConditions](https://docs.liquibase.com/concepts/advanced/preconditions.html)

$\quad$preConditions作用于databaseChangelog或changeSet来控制执行。changelog level的precoditions应用于所有的changeSet，包含include进来的changeSet。preConditions条件满足，按顺序执行其中的changeSet和include；preConditions条件不满足，失败退出并解释失败的原因。
下面是使用preConditions的示例。只有database type为oracle且username为DEVNEW时才执行该changelog。同时，只有oldtable中没有数据时才能drop table。
```xml
<?xml version="1.0" encoding="UTF-8"?>
<databaseChangeLog
        xmlns="http://www.liquibase.org/xml/ns/dbchangelog/1.8"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog/1.8
         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-1.8.xsd">
    <preConditions>
        <dbms type="oracle" />
        <runningAs username="SYSTEM" />
    </preConditions>
    <changeSet id="1" author="bob">
        <preConditions onFail="WARN">
            <sqlCheck expectedResult="0">select count(*) from oldtable</sqlCheck>
        </preConditions>
        <comment>Comments should go after preCondition. If they are before then liquibase
            usually gives error.</comment>
        <dropTable tableName="oldtable"/>
    </changeSet>
</databaseChangeLog>
```
① 对应的常见参数有：

[![对应的常见参数有](https://s6.jpg.cm/2021/12/13/L6ozNr.jpg)](https://imagelol.com/image/L6ozNr)

② 如果出现以上结果时，可以进行一些处理

③ 当结果为onFail或onError时，可执行下面操作

[![当结果为onFail或onError时](https://s6.jpg.cm/2021/12/13/L6o9kG.jpg)](https://imagelol.com/image/L6o9kG)

④ 当结果为onUpdateSQL时，可执行下面操作

[![当结果为onUpdateSQL时](https://s6.jpg.cm/2021/12/13/L6oD2p.jpg)](https://imagelol.com/image/L6oD2p)

⑤ 条件之间可以使用AND/OR/NOT进行拼接来构建复杂的环境，默认为and。下面为示例
```xml
<preConditions>
     <or>
         <and>
            <dbms type="oracle" />
            <runningAs username="SYSTEM" />
         </and>
         <and>
            <dbms type="mssql" />
            <runningAs username="sa" />
         </and>
     </or>
 </preConditions>
```
⑥ precoditions包括下面item
```xml
<dbms>
<runningAs>
<changeSetExecuted>
<columnExists>
<tableExists>
<viewExists>
<foreignKeyConstraintExists>
<indexExists>
<sequenceExists>
<primaryKeyExists>
<sqlCheck>
```
- [property](https://docs.liquibase.com/concepts/basic/changelog-property-substitution.html)

$\quad$Liquibase允许动态替换changelog中的参数，参数一般使用${}表示。参数值的搜索顺序：
$\quad$ a. Liquibase runner的参数；
$\quad$ b. JVM系统参数；
$\quad$ c. changelog中<property>提供的参数。
$\quad$ property具有如下属性，设置context或dbms，参数只会在这些context或dbms中使用。
[![设置context或dbms](https://s6.jpg.cm/2021/12/13/L64G4E.jpg)](https://imagelol.com/image/L64G4E)
① property的示例如下
```xml
<property name="simpleproperty" value="somevalue"/>
<property name="clob.type" value="clob" dbms="oracle,h2"/>
<property name="clob.type" value="longtext" dbms="mysql"/>
<property name="myproperty" value="yes" context="common,test"/>
```

- [changeSet](https://docs.liquibase.com/concepts/basic/changeset.html)

$\quad$ changeSet将database changes组合在一起，使用id,author以及changelog file path来唯一识别。Liquibase顺序执行changelog中的changeSet，根据表databasechangelog判断id/author/filepath是否已经执行过，若执行过则跳过该changeSet，除非指定runAlways为true. 当changeset执行完，在表databasechangelog插入一行来记录id/author/filepath以及changeset的MD5sum值。
Liquibase通常在一个transaction中执行changeSet。
下面是changeSet具有的属性

[![changeSet具有的属性](https://s6.jpg.cm/2021/12/13/L64tBt.jpg)](https://imagelol.com/image/L64tBt)
changeSet中可以包含如下的sub-tags

[![changeSet中可以包含如下的sub-tags](https://s6.jpg.cm/2021/12/13/L64NOR.jpg)](https://imagelol.com/image/L64NOR)
- [include](https://docs.liquibase.com/concepts/advanced/include.html)

用来在root change log file中引入其他的change log file
```xml
<?xml version="1.0" encoding="UTF-8"?>

<databaseChangeLog
  xmlns="http://www.liquibase.org/xml/ns/dbchangelog/1.9"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog/1.9
         http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-1.9.xsd">
    <include file="com/example/news/news.changelog.xml"/>
    <include file="com/example/directory/directory.changelog.xml"/>
</databaseChangeLog>
```
include具有如下属性
[![include具有属性](https://s6.jpg.cm/2021/12/13/L64zTS.jpg)](https://imagelol.com/image/L64zTS)

$\quad$ include目前具有重复引用和循环引用的问题。重复引用因为不会执行已经执行过的changeSet而自动解决。

- [contexts](https://docs.liquibase.com/concepts/advanced/contexts.html)

$\quad$ Liquibase使用contexts作为tag来限制changeSet是否执行，可将changeSet进行分组。若不设置context，该changeSet将总是执行。context可以具有任意名称且大小写不敏感。
使用context的示例
```xml
<changeSet id="2" author="bob" context="test">
        <insert tableName="news">
            <column name="id" value="1"/>
            <column name="title" value="Liquibase 0.8 Released"/>
        </insert>
        <insert tableName="news">
            <column name="id" value="2"/>
            <column name="title" value="Liquibase 0.9 Released"/>
        </insert>
    </changeSet>
```
$\quad$ 可以使用and,or,!以及()来组合context，优先级顺序! > and > or。context也可以用,分隔，优先级比较高。test, qa and master等价于(test) or (qa and master)。

- [changes](https://docs.liquibase.com/change-types/home.html)


Liquibase中提供了很多可以作用于database的changes。Table/View。包括create, drop, rename table/view/column.index 包括create,drop index/procedure/primary key/foreign key/unique constraint/not null constraint/default value。insert/delete
创建table的示例
```xml
<changeSet author="liquibase-docs" id="createTable-example">
    <createTable catalogName="cat"
            remarks="A String"
            schemaName="public"
            tableName="person"
            tablespace="A String">
        <column name="address" type="varchar(255)"/>
    </createTable>
</changeSet>
```
### 总结
$\quad$ Liquibase可以on demand执行，也可以自动执行。On demand执行的方式包括command line, Ant, Maven。自动执行的方式包括servlet listener, spring listener。更多信息参见文章Running。
推荐关于Liquibase的资料:

① [Log trim](https://docs.liquibase.com/workflows/liquibase-community/trimming-changelogs.html)

② [Document](https://docs.liquibase.com/home.html)

③ [Special care exist project](https://docs.liquibase.com/workflows/liquibase-community/existing-project.html)
