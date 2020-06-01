# BigDataTraining-金能征
code and summary for course "Big Data Training"

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

