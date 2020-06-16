# BigDataTraining-金能征
code and summary for course "Big Data Training"

## date:2020/6/15
### 1.今日成果
(1)今天了解了数据治理的体系架构

(2)学习了解了数据仓库

### 2.数据治理的体系架构

数据治理的体系架构分为顶层设计、数据治理环境、数据治理域、数据治理过程四个层次。

### 2.OLTP和OLAP的区别

联机分析处理 (OLAP) 的概念最早是由关系数据库之父E.F.Codd于1993年提出的，他同时提出了关于OLAP的12条准则。OLAP的提出引起了很大的反响，OLAP作为一类产品同联机事务处理 (OLTP) 明显区分开来。
当今的数据处理大致可以分成两大类：联机事务处理OLTP（on-line transaction processing）、联机分析处理OLAP（On-Line Analytical Processing）。OLTP是传统的关系型数据库的主要应用，主要是基本的、日常的事务处理，例如银行交易。

OLAP是数据仓库系统的主要应用，支持复杂的分析操作，侧重决策支持，并且提供直观易懂的查询结果。

下表列出了OLTP与OLAP之间的比较。

OLTP是细节的、存取瞬间数据、可更新的、性能要求高、响应时间短、面向事务、一次操作数据量小、支持日常操作、常用于客户订单、库存水平和银行账户查询等。

OLAP是综合的或者提炼的，存储历史数据、不包含最近的数据，只读、只追加，通常离线计算好数据，面向分析，一次操作数据量大，支持决策需求，常用于客户收益分析、市场细分等。

### 4.数据仓库

(1)数据仓库的分层架构

ADS层，Application Data Store：应用数据层，用钥匙提供给数据产品和数据分析使用的数据。业务个性化数据，服务于特定场景，复用性不强。

DWS层：Data warehouse Summary：汇总数据层。油层数据即使或者宽表

DWD层：Data Warehouse Detail：明细数据层。在ODS层数据的基础上，对数据进行加工处理，提供标准化和维度完整的数据

ODS层： Operational Data Store：操作数据层，直接从原始业务库、生产库、镜像库接入的数据。

### 5. 问题及解决

暂无遇到太大问题。

## date:2020/6/15
### 1.今日成果
(1)今天学习了模型训练的过程，模型评估的方法，以及使用pipeline进行模型训练，同时还学习和如何使用深度学习进行分类模型的训练。

### 2.今日感悟
机器学习和深度学习在特征选择方面的差别。

机器学习算法一般需要进行特征工程，但是采用深度学习，可以让模型自己选择最好的特征进行学习。这可以通过隐含层训练的参数达到特征选择的效果。


### 3.问题及解决
问题：在模型评估的可视化部分一些图不能正常地显示。

解决：采用老师的建议，加入import语句


## date:2020/6/12
### 1.今日成果
(1)今天完成了大数据实时计算大作业
(2)深入学习率flink的window函数

### 2.window函数
flink的window函数一共有Thumbing Windows、Sliding Windows、和Session Windows三种，第一种滚动窗口、窗口和窗口之间不重合，而滑动窗口的重合区域将会有window size-window slide决定。会话窗口分配器按活动会话对元素进行分组。 与滚动窗口和滑动窗口相比，会话窗口不重叠且没有固定的开始和结束时间。 相反，当会话窗口在一定时间段内未接收到元素时，即在发生不活动间隙时，它将关闭。 会话窗口分配器可以配置有静态会话间隔，也可以配置有会话间隔提取器功能，该功能定义不活动的时间长度。 当此时间段到期时，当前会话将关闭，随后的元素将分配给新的会话窗口。

### 3.问题及解决
问题：遇到用JsonValue的方法解析流报错
解决：
原数据中可能有格式不匹配的数据，自己使用map方法实现Json解析。


## date:2020/6/11
### 1.今日成果
(1)今天开始大数据实时计算大作业，完成了从S3生产数据到Kafka。

(2)学习了flink的datastream的相关API

### 2.Datastream的keyBy函数
在逻辑上将流划分为不相交的分区。具有相同键的所有记录被分配到相同的分区。在内部，keyBy()是通过哈希分区实现的。

这个转换返回一个KeyedStream。
```
dataStream.keyBy("someKey") // Key by field "someKey"
dataStream.keyBy(0) // Key by the first element of a Tuple
```
keyBy后面通常跟一个window窗口函数
```
dataStream.keyBy(0).window(TumblingEventTimeWindows.of(Time.seconds(5))); // Last 5 seconds of data
```
每一个keyedStream在某个时长内对应一个窗口，可对窗口内的数据进行处理计算。

### 3.问题及解决
问题：遇到很多莫名的type mismatch问题

解决：对于这些报错，很多是因为对API不熟悉导致的，另外一些是导错包的问题，因为flink包的一个方法有很多种实现，不同的实现版本的参数要求是不同的。所以有些坑。


## date:2020/6/10
### 1.今日成果
(1) 回顾了pandas_profiling的使用，从前年少无知，做数据挖掘实验的时候苦于不能很好的可视化数据，在挖掘过程遇到了很多的问题。今后会对pandas作进一步的学习。

(2)深入了解了集成学习方法
目前的集成学习方法大致可以分为两类，即个体学习器间存在强依赖关系、必须串行生成的序列化方法，以及个体学习器间不存在强以来关系、棵同时生成的并行化方法；前者的代表是Boosting，后者的代表是Bagging和“随机森林”(random forest)。

### 2.问题与解决
暂无


## date:2020/6/9
### 1.今日成果
(1) 了解了人工智能的发展历史

(2) 了解使用机器学习方法解决实际业务的整个流程

(3) 学习了随机森林分类器

  随机森林就是通过集成学习的思想将多棵树集成的一种算法，它的基本单元是决策树，而它的本质属于机器学习的一大分支——集成学习（Ensemble Learning）方法。

  决策树我们已经在机器学习课程当中进行了深入的学习，而随机森林就是利用多棵决策时进行决策。

### 2.问题与解决：
今天的实操较为顺利，暂无遇到太大问题。



## date:2020/6/8
### 1.今日成果
(1)完成了大数据实时计算拓展作业

(2)学习了flink计算框架的基础知识

### 2.问题及解决
(1) 遇到json字符串无法解析为json对象

更换主题即可


## date:2020/6/5
### 1.今日成果
(1)完成了后端查询数据库表、表字段的功能

(2)开始测试前后端功能
### 2.问题与解决
暂无遇到太大问题


## date:2020/6/4
### 1.今日成果
(1)完成第二次作业中的sparksql查询的后端开发

(2)对已完成的后端功能进行测试

(3)学习了java中HttpSession的原理和使用

### 2.session和cookie的区别
(1)cookie数据存放在客户的浏览器上，session数据放在服务器上。

(2)cookie不是很安全，别人可以分析存放在本地的cookie并进行cookie欺骗，考虑到安全应当使用session。

(3)session会在一定时间内保存在服务器上。当访问增多，会比较占用你服务器的性能，考虑到减轻服务器性能方面，应当使用cookie。

(4)单个cookie保存的数据不能超过4K，很多浏览器都限制一个站点最多保存20个cookie。

(4)可以考虑将登陆信息等重要信息存放为session，其他信息如果需要保留，可以放在cookie中。

### 3. HttpSession的使用
Session机制：

(1)session机制采用的是在服务器端保持 HTTP 状态信息的方案 。

(2)当程序需要为某个客户端的请求创建一个session时，服务器首先检查这个客户端的请求里是否包含了一个session标识(即sessionId),如果已经包含一个sessionId则说明以前已经为此客户创建过session，服务器就按照session id把这个session检索出来使用(如果检索不到，可能会新建一个，这种情况可能出现在服务端已经删除了该用户对应的session对象，但用户人为地在请求的URL后面附加上一个JSESSION的参数)。如果客户请求不包含sessionId，则为此客户创建一个session并且生成一个与此session相关联的sessionId，这个session id将在本次响应中返回给客户端保存。

(3)方法：

--获取Session 对象：

request.getSession()， request.getSession(boolean Create);

--属性相关的：

getAttribute()、setAttribute()、removeAttribute()

--使HttpSession 失效：

invalidate()

--设置其最大时效：

setMaxInactiveInterval()

(4)url 重写：它允许不支持Cookie的浏览器也可以与WEB服务器保持连续的会话。将会话标识号以参数形式附加在超链接的URL地址后面的技术称为URL重写。

<%= response.encodeURL("login.jsp")%>
参考资料：https://www.cnblogs.com/daoxiaobai/p/6275524.html

### 问题及解决
今天暂时没有遇到大的问题

## date:2020/6/3
### 1.今日成果
(1)在springboot项目中配置spark开发环境

(2)学习了层次分析法

(3)完成第二次作业中的信息配置的后端开发

### 2.问题及解决
(1) 再导入spark相关依赖后虚拟机在加载类时出现问题

原因：在网上调查后察觉到有可能是jackson-databind包版本与springboot不对应的关系。

解决：一开始按网上将该包的版本改为6.9.5，发现仍报错，最终将该包的版本改为最新版6.11.0，能成功运行，并连接到spark主机。

## date:2020/6/2

### 1.今日成果
(1)学习了大数据流式计算

(2)完善了大数据离线计算的课外拓展题

(3)学习了springboot框架，为大数据离线计算作业做铺垫

(4)学习了spark rdd 的操作

### 2. rdd中常见操作
(1)map
对RDD中的每个元素都执行一个指定的函数来产生一个新的RDD。任何原RDD中的元素在新RDD中都有且只有一个元素与之对应。

举例：

scala> val a = sc.parallelize(1 to 9, 3)

scala> val b = a.map(x => x*2)

scala> a.collect

res10: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)

scala> b.collect

res11: Array[Int] = Array(2, 4, 6, 8, 10, 12, 14, 16, 18)

(2)flatMap

与map类似，区别是原RDD中的元素经map处理后只能生成一个元素，而原RDD中的元素经flatmap处理后可生成多个元素来构建新RDD。

举例：对原RDD中的每个元素x产生y个元素（从1到y，y为元素x的值）

scala> val a = sc.parallelize(1 to 4, 2)

scala> val b = a.flatMap(x => 1 to x)

scala> b.collect

res12: Array[Int] = Array(1, 1, 2, 1, 2, 3, 1, 2, 3, 4,1,2,3,4)

(3)reduceByKey

顾名思义，reduceByKey就是对元素为KV对的RDD中Key相同的元素的Value进行reduce，因此，Key相同的多个元素的值被reduce为一个值，然后与原RDD中的Key组成一个新的KV对。

举例:

scala> val a = sc.parallelize(List((1,2),(3,4),(3,6)))

scala> a.reduceByKey((x,y) => x + y).collect

res7: Array[(Int, Int)] = Array((1,2), (3,10))

上述例子中，对Key相同的元素的值求和，因此Key为3的两个元素被转为了(3,10)。


### 3.问题及解决：

(1) 在实操8中遇到驱动器不匹配的问题

将com.jdbc.mysql.Driver 替换为org.postgresql.Driver

(2) 遇到内网无法解析greenplum域名的问题

使用老师提供的IP和端口即可成功运行

## date:2020/6/1

### 1.今日成果
(1)完成了课内实操题和课外拓展题

(2)学习了sql和高级程序语言结合的方法

(3)学习了通过较为复杂的sql完成相关业务

(4)学会了使用不同计算框架对不同阶段的数据进行计算

(5)学习了使用spark之后计算数据的ETL操作

### 2.为什么Spark的应用场景不适合高并发、和实时读写？

spark是一个分布式计算框架， 从他的作业调度可以看到http://spark.apache.org/docs/latest/job-scheduling.html，它的资源分配粒度很粗，CPU的核数进行分配的，集群的CPU资源是有限的，同时spark sql资源计算时需要把大量数据加载到内存中，需要消耗集群大量的内存资源，再做shuffle的时候，又需要消耗大量的网络IO和磁盘IO, 如果同时多个job执行，那么每个job获得资源要么少，要么需要排队。而不能像关系型数据库那么提供高并发的服务。

所以我们将数据计算完之后，需要导出到合适实时读取的数据库中。

### 3.问题及解决：
(1)遇到toDF can not be resolve的问题

解决：遗漏了import sparkclient._语句

(2)登录mysql主机上出现错误

解决：主要是格式问题，如空格

## date:2020/5/29
### 1.今日成果
(1)进一步了解spark和greenPlum的区别<br>
(2)完成课程拓展题

### 2. spark和greenPlum的区别
根据上一节的学习，我们知道spark和greenPlum都属于大数据计算的工具或者是计算框架。
spark和greenPlum的重要区别是它们依赖的底层架构是不一样的
spark是基于hadoop的，而greenPlum是基于MPP的。理论上MPPDB与Hadoop都是将运算分布到节点中独立运算后进行结果合并(分布式计算)，但由于依据的理论和采用的技术路线不同而有各自的优缺点和适用范围。两种技术以及传统数据库技术的对比如下：
特征|Hadoop|MPPDB|传统数据仓库
:---:|:----:|:----:|:---:
平台开放性|高|低|低
运维负责度|高|中|中
扩展能力|高|中|低
拥有成本|低|中|高
系统和数据管理成本|高|中|中
应用开发维护成本|高|中|中
SQL支持|中(低)|高|高
数据规模|PB级别|部分PB|TB级别
计算性能|对非关系型操作效率高|对关系型操作效率高|对关系型操作效率中
数据结构|机构化、半结构化和非机构化数据|结构化数据|结构化数据

原文链接：https://blog.csdn.net/qq_42189083/article/details/80610092


## date:2020/5/28
### 1.今日成果
(1)初试spark进行字频统计<br>
(2)学习spark.rdd的使用<br>
(3)操作使用reduce方法实现求和<br>
### 2.spark rdd的使用
详见https://www.cnblogs.com/sharpxiajun/p/5506822.html
### 3.问题及结局
遇到错误：Could not locate executable null\bin\winutils.exe in the Hadoop binaries<br>
解决方法:Hadoop都是运行在Linux系统下的，在windows下eclipse中运行mapreduce程序，要首先安装Windows下运行的支持插件<br>
>详见：[Could not locate executable null\bin\winutils.exe in the Hadoop binaries解决方式](https://blog.csdn.net/weixin_41122339/article/details/81141913?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.nonecase)

## date:2020/5/27
### 1.今日成果v
(1)了解了大数据的获取、存储和计算的计算体系<br>
* 数据来源有：
  
  * RDB
  * Log
  * 文档
  * 图片
  * 视频
  * ...
* 数据采集
  
  * Kafka
  * Flume
  * Sqoop
  * 其他ETL工具
* 数据存储

  * 对象存储服务(S3)
  * 块存储服务(HDFS)<br>
    Hadoop Distributed File System, HDFS采用了主从（Master/Slave）结构模型，一个HDFS集群是由一个NameNode和若干个DataNode组成的。其中NameNode作为主服务器，管理文件系统的命名空间和客户端对文件的访问操作；集群中的DataNode管理存储的数据。<br>
  * 列式存储服务(HBase)<br>
    列式存储是指，一列中的数据在存储介质中是连续存储的；行式存储是指一行中的数据在存储介质中是连续存储的。简单的说，你可以把列式数据库认为是每一列都是一个表，这个表只有一列，如果只在该列进行条件查询，速度就很快。
    那这两种不同的存储方式对数据的CRUD有什么不同的影响呢?一般说的是下面两点
    1。行数据库适用于读取出少行，多列的情况；列数据库相反，适用于读取出少数列，多数行的情况。
    2。列数据库可以节省空间，如果某一行的某一列没有数据，那在列存储时，就可以不存储该列的值。这比行数据库节省空间
* 大数据计算
  
    * 离线计算
      
      * Hive
      * Spark
      * GreenPlum
    * 实时计算
      * SparkStreaming
      * Flink
    * 人工智能
      * 机器学习
      * 深度学习
      * 知识图谱
* 数据治理
  * 数据治理
  * 数据安全
  * 数据资源管理
* 数据开发
  * API网关  


## date:2020/5/26
### 1.今日成果
>(1)完成了文件同步器中断任务检查和一致性检查<br>
>(2)完成用户交互的功能<br>
### 2.问题及解决
>(1)Scanner没等输入就报错 java.util.NoSuchElementException: No line found<br>
>原因及解决：
>>多个scanner对象的情况下，关闭了其中一个就会导致System.in也关闭，自然就无法继续读了，所以需要保持同一个scanner<br>

>(2)注意next（）方法和nextLine()方法混用出现异常<br>
>原因及解决：
>>next（）一定要读取到有效字符后才可以结束输入，对输入有效字符之前遇到的空格键、Tab键或Enter键等结束符，next（）方法会自动将其去掉，只有在输入有效字符之后，next（）方法才将其后输入的空格键、Tab键或Enter键等视为分隔符或结束符。简单地说，next（）查找并返回来自此扫描器的下一个完整标记。完整标记的前后是与分隔模式匹配的输入信息，所以next方法不能得到带空格的字符串。<br>

>>而nextLine（）方法的结束符只是Enter键，即nextLine（）方法返回的是Enter键之前的所有字符，它是可以得到带空格的字符串的。<br>

>>鉴于以上两种方法的只要区别，同学们一定要注意next（）方法和nextLine()方法的连用，下面举个例子来说明：<br>

>>import java.util.Scanner;<br>
>>public class NextTest {<br>

>>public static void main(String[] args) {<br>
>>  // TODO Auto-generated method stub<br>
>>  String s1,s2;<br>
>>  Scanner sc=new Scanner(System.in);<br>
>>  System.out.print("请输入第一个字符串：");<br>
>>  s1=sc.nextLine();<br>
>>  System.out.print("请输入第二个字符串：");<br>
>>  s2=sc.next();<br>
>>  System.out.println("输入的字符串是："+s1+"  "+s2);<br>
>> }<br>
>>}<br>

>>运行结果：<br>

>>请输入第一个字符串：home
>>请输入第二个字符串：work
>>输入的字符串是：home  work

>>但如果把程序改一下， s1=sc.next(); s2=sc.nextLine();

>>运行结果是：

>>请输入第一个字符串：home
>>请输入第二个字符串：输入的字符串是：home

>>可以看到，nextLine（）自动读取了被next（）去掉的Enter作为他的结束符，所以没办法给s2从键盘输入值。经过验证，我发现其他的next的方法，如double nextDouble()  ， float nextFloat() ， int >>nextInt() 等与nextLine（）连用时都存在这个问题，解决的办法是：在每一个 next（）、nextDouble()  、 nextFloat()、nextInt() 等语句之后加一个nextLine（）语句，将被next（）去掉<br>>>的Enter结束符过滤掉。例如上面的程序改写为：

>>import java.util.Scanner;
>>public class NextTest {

>>public static void main(String[] args) {
  // TODO Auto-generated method stub
  String s1,s2;
  Scanner sc=new Scanner(System.in);
  System.out.print("请输入第一个字符串：");
  s1=sc.next();
  sc.nextLine();
  System.out.print("请输入第二个字符串：");
  s2=sc.nextLine();
  System.out.println("输入的字符串是："+s1+"  "+s2);
 }
}

>>运行结果是：

>>请输入第一个字符串：home
>>请输入第二个字符串：work
>>输入的字符串是：home  work

### 2.问题及解决
>暂无遇到太大问题

## date:2020/5/25
### 1.今日成果
>(1)完成了文件同步器分块下载的功能<br>
>(2)将xml配置文件替换成json配置文件，更方便操作<br>
>(3)学习了fastjson的使用过<br>

### 2.问题及解决
>发现分块下载中断后再恢复出现文件大小不一致的情况<br>
>解决：<br>
>>主要是继续写入的是后seek(filePosition)寻得的位置不正确, 将filePosition记录为：filePosition+=readlen即可解决问题。<br>


## date:2020/5/22
### 1.今日成果
>(1)在实现文件目录的监听的基础上实现增加文件的同步，构造初步的fileSynchronizer<br>
>(2)实现初步的InfoSaver，使得程序在退出再重新进入时，可以读取上一次的运行信息<br>
>(3)学习了dom4j包的使用

### 2.关于InfoSaver
>这里创建了一个InfoSaver，作用是在程序退出时执行特定的代码，保存信息。所以它需要知道程序何时结束。<br>
>首先需获取的Runtime实例:
>>每个Java应用程序都有一个类运行时实例，该实例允许应用程序与运行应用程序的环境进行交互。可以从getRuntime方法获得当前runtime。<br>

>获取runtime实例之后，通过addShutDownHook方法传入一个在程序关闭时要执行指定代码的线程，从而达到保存信息的效果。

### 3.关于信息的保存格式
>这个查阅了一些资料，发现用xml文件保存程序的配置信息比较方便，所以我学习使用了dom4j包来操作xml文件。

### 4.问题及解决：
>今天的任务较为顺利，没有遇到太大的坑。


## date: 2020/5/21<br>

### 1. 今日成果:<br>
>(1)用Java实现实时监控目录下文件变化<br> 
>(2)将java程序打包成exe可执行文件<br>

### 2.Java实现实时监控目录下文件变化<br>
>我使用了commons-io包来实现该功能。采用的是观察者模式来实现<br>

>(1)需实现功能:<br> 
>>监控文件的创建、删除和修改<br>
>>监控文件夹的创建、删除和修改<br>

>(2)实现步骤:<br>
>>i. 下载2.0以上的commnons-io包并添加到项目的build path中， maven项目可直接添加依赖<br>

>>ii. 编写继承FileAlterationListenerAdaptor的类FileListener，需要重写以下方法<br>
>>>public void onFileCreate(File file)<br>
>>>public void onFileChange(File file)<br>
>>>public void onFileDelete(File file)<br>
>>>public void onDirectoryCreate(File directory)<br>
>>>public void onDirectoryChange(File directory)<br>
>>>public void onDirectoryDelete(File directory)<br>
>>>public void onStart(FileAlterationObserver observer)<br>
>>>public void onStop(FileAlterationObserver observer)<br>

>>iii.编写main方法<br>
>>>创建观察者对象，创建monitor<br>

### 3.将java程序打包成exe可执行文件<br>
>(1)实现步骤：<br>
>>i.将项目导出为可执行的jar文件<br>
>>ii.下载exe4j软件,将可执行的jar文件转化为exe可执行文件。<br>
>>>具体步骤详见：https://blog.csdn.net/zhangdaiscott/article/details/46988003<br>

### 4.遇到的问题及其解决：<br>
>(1)生成的exe可执行文件出现闪退<br>
>>出现闪退是因为生成exe可执行文件是未注意32bit 和64bit的区别，默认生成32bit的exe文件，与系统版本不匹配，需要在Configure executable处Advanced Options选择64位。<br>

### 5.总结<br>
>了解了commons-io包的基本用法：https://blog.csdn.net/backbug/article/details/99572931。<br>
>熟悉了java程序生成exe可执行文件的方法。<br>
>通过对commons-io源码的了解，加深了对线程池，线程工厂的理解。<br>
>加深了对观察者模式的理解。<br>

