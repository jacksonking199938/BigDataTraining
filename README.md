# BigDataTraining-金能征
code and summary for course "Big Data Training"
## date:2020/6/25
### 1.今日成果
(1)了解了ODS的概念

(2)学习了DW和DM的概念及其区别

### 2.DW、ODS、DM的概念及其区别
一、整体结构
在具体分析数据仓库之前先看下一下数据中心的整体架构以及数据流向<br>

数据中心整体架构<br>

DB 是现有的数据来源(也称各个系统的元数据)，可以为mysql、SQLserver、文件日志等，为数据仓库提供数据来源的一般存在于现有的业务系统之中。<br>

ETL的是 Extract-Transform-Load 的缩写，用来描述将数据从来源迁移到目标的几个过程：<br>
Extract，数据抽取，也就是把数据从数据源读出来。<br>
Transform，数据转换，把原始数据转换成期望的格式和维度。如果用在数据仓库的场景下，Transform也包含数据清洗，清洗掉噪音数据。<br>
Load 数据加载，把处理后的数据加载到目标处，比如数据仓库。<br>

ODS(Operational Data Store) 操作性数据，是作为数据库到数据仓库的一种过渡，ODS的数据结构一般与数据来源保持一致，便于减少ETL的工作复杂性，而且ODS的数据周期一般比较短。ODS的数据最终流入DW<br>

DW (Data Warehouse)数据仓库，是数据的归宿，这里保持这所有的从ODS到来的数据，并长期保存，而且这些数据不会被修改。<br>

DM(Data Mart) 数据集市,为了特定的应用目的或应用范围，而从数据仓库中独立出来的一部分数据，也可称为部门数据或主题数据。面向应用。<br>

二、数据仓库<br>
数据仓库(Data Warehouse) 简称DW，顾名思义，数据仓库是一个很大的数据存储集合，出于企业的分析性报告和决策支持目的而创建，对多样的业务数据进行筛选与整合。它为企业提供一定的BI（商业智能）能力，指导业务流程改进、监视时间、成本、质量以及控制。<br>
数据仓库存储是一个面向主题（移动的用户分析也可做为一个主题）的，反映历史变化数据，用于支撑管理决策。<br>

特征：<br>

效率足够高，要对进入的数据快速处理。<br>
数据质量高，数据仓库是提供很多决策需要的数据支撑，DW的数据应该是唯一的具有权威性的数据，企业的所有系统只能从DW取数据，所以需要定期对DW里面的数据进行质量审，保证DW里边数据的唯一、权威、准确性。<br>
扩展性，企业业务扩展和降低企业建设数据仓库的成本考虑<br>
面向主题，数据仓库中的数据是按照一定的主题域进行组织的，每一个主题对应一个宏观的分析领域，数据仓库排除对决策无用的数据，提供特定主题的简明视图。<br>
数据仓库主要提供查询服务，并且需要查询能够及时响应<br>
DW的数据也是只允许增加不允许删除和修改，数据仓库主要是提供查询服务，删除和修改在分布式系统.<br>

三、操作性数据<br>
操作性数据(Operational Data Store) 简称ODS，作为数据库到数据仓库的一种过渡形式，与数据仓库在物理结构上不同。ODS存储的是当前的数据情况，给使用者提供当前的状态，提供即时性的、操作性的、集成的全体信息的需求。ODS作为数据库到数据仓库的一种过渡形式，能提供高性能的响应时间，ODS设计采用混合设计方式。ODS中的数据是"实时值"，而数据仓库的数据却是"历史值"，一般ODS中储存的数据不超过一个月，而数据仓库为10年或更多。<br>

特征：<br>

ODS直接存放从业务抽取过来的数据，这些数据从结构和数据上与业务系统保持一致，降低了数据抽取的复杂性。<br>
转移一部分业务系统的细节查询功能，因为ODS存放的数据与业务系统相同，原来有业务系统产生的报表，现在可以从ODS中产生。<br>
完成数据仓库中不能完成的功能，ODS存放的是明细数据，数据仓库DW或数据集市DM都存放的是汇聚数据，ODS提供查询明细的功能。<br>
ODS数据只能增加不能修改，而且数据都是业务系统原样拷贝，所以可能存在数据冲突的可能，解决办法是为每一条数据增加一个时间版本来区分相同的数据。<br>

四、数据集市<br>
数据集市(Data Mart)简称DM，是为了特定的应用目的或应用范围，而从数据仓库中独立出来的一部分数据，也可称为部门数据或主题数据（subjectarea）。在数据仓库的实施过程中往往可以从一个部门的数据集市着手，以后再用几个数据集市组成一个完整的数据仓库。需要注意的就是在实施不同的数据集市时，同一含义的字段定义一定要相容，这样再以后实施数据仓库时才不会造成大麻烦。<br>
数据集市，以某个业务应用为出发点而建设的局部DW,DW只关心自己需要的数据，不会全盘考虑企业整体的数据架构和应用，每个应用有自己的DM<br>

特征：<br>

DM结构清晰，针对性强，扩展性好，因为DM仅仅是单对一个领域而建立，容易维护修改<br>
DM建设任务繁重，公司有众多业务，每个业务单独建立表。<br>
DM的建立更多的消耗存储空间，单独一个DM可能数据量不大，但是企业所有领域都建立DM这个数据量就会增加多倍。<br>

## date:2020/6/24
### 1.今日成果
(1)了解了sql中decode函数的用法

### 2.decode函数
decode 函数基本语法：

decode（字段|表达式，条件1，结果1，条件2，结果2，...，条件n，结果n，缺省值）；<br>
--缺省值可以省略<br>
 表示如果 字段|表达式 等于 条件1 时，DECODE函数的结果返回 条件1 ,...,如果不等于任何一个条件值，则返回缺省值。<br>
【注意】：decode 函数 ，只能在select 语句用。<br>

例子：

（1）进行变量的替换

select decode(a.jdlx,'DZQD','电子渠道','HZQD','合作渠道','ZYQD','自营渠道') jdmc,sum(t.jg) xssr from data_yys.t_yys_kdxs t
inner join data_yys.tr_yys_chnl a on t.jdbm = a.jdbm
group by a.jdlx;

(2)函数分段
```sql
select e.ename ,e.sal,
       decode(sign(e.sal-5000),
       1, 'high sal',
       0, 'hign sal',
       -1,
          decode(sign(e.sal-3000),
          1, 'mid sal',
          0, 'mid sal',
          -1,'low sal'
             )
          )
        as "工资等级"
from scott.emp e;
```

更多例子可查看：https://www.cnblogs.com/jiaxinwei/p/10252513.html

## date:2020/6/23
### 1.今日成果
(1).学习了信创背景下大数据的发展趋势

(2).学习了物理机、虚拟机、docker、裸金属的区别

### 2.物理机、虚拟机、docker、裸金属的区别
物理机，对应采购的服务器设备，又叫裸设备，bare metal，随着摩尔定律的增长，单个服务器的性能逐渐提升，物理服务器的性能是如此的高，以致于我们实际使用到的物理机的性能只占了百分之几甚至更低，为了灵活的部署系统和应用，需要一层虚拟层在物理机之上，这就是虚拟机。

虚拟机，与现在流行的“云计算”的概念的有着紧密联系，虚拟机的概念在IaaS层，即基础设施即服务部分，可以自行学习一下。为了让全人类能够像使用自来水、电一样使用计算资源，我们需要在几百上千台物理机上部署虚拟化软件，如vmware等等，使得他们的表现就像一台巨大的计算机。同时它还具有灵活性和解耦性，你可以在一台物理机上部署10台虚拟机，使得一台物理机的表现就像10台性能略差的服务器，当你不需要他们时，你又可以随时的回收资源重新分配。

>虚拟化，是指通过虚拟化技术将一台计算机虚拟为多台逻辑计算机。在一台计算机上同时运行多个逻辑计算机，每个逻辑计算机可运行不同的操作系统，并且应用程序都可以在相互独立的空间内运行而互不影响，从而显著提高计算机的工作效率。--百度词条<br><br>
>虚拟化是表示计算机资源的逻辑组（或子集）的过程，这样就可以用从原始配置中获益的方式访问它们。这种资源的新虚拟视图并不受实现、地理位置或底层资源的物理配置的限制。 -- Wikipedia<br><br>
>虚拟化技术可针对具体应用目的创建特定目的的虚拟环境，安全、效率高，快照、克隆、备份、迁移等方便。系统虚拟化是将一台物理计算机虚拟成一台或多台虚拟计算机系统，每个都有自己的虚拟硬件，其上的操作系统任认为自己运行在一台独立的主机上，计算机软件在一个虚拟的平台上而不是真实的硬件平台上运行。虚拟化技术可以扩大硬件的容量，简化软件的重新配置过程。其中CPU的虚拟化可以单CPU模拟多CPU并行运行，允许一个平台同时运行多个操作系统，并且应用程序可以在相互独立的空间内运行而互不影响。虚拟化技术在降低硬件成本的同时，还可以显著提高系统的工作效率和安全性。<br><br>
>虚拟化系统的实现通常是在操作系统和硬件之间加入一个虚拟机监控程序，称为Hypervisor（如图所示）。由Hypervisor主要负责各个操作系统之间的硬件资源协调。虚拟机监控程序是一种特殊操作系统，直接在裸机上运行（针对完全虚拟化技术）。虚拟机监控程序创建一个底层硬件平台抽象，一个或多个虚拟机（VM）共享这个底层硬件平台。在这种环境中，VM只是操作系统及其应用程序的容器，一个 VM 与虚拟机监控程序上运行的其他 VMs 隔离，这支持多个操作系统或多个配置不同的相似操作系统。<br><br>
>1.虚拟计算机系统三层含义-同质、高效、资源受控。<br>
同质-本质上虚拟机和物理机是相同的、表现上有所差异，如一个物理核虚拟多个核。<br>
高效-虚拟机效能接近物理机。<br>
资源受控-虚拟机对系统资源有完全的控制能力，包括分配、管理、回收。<br><br>
>2.虚拟化分不同层面的虚拟化<br>
硬件抽象层的虚拟化-客户机与宿主机硬件相似，指令集相似。<br>
操作系统层虚拟化-内核可以提供多个相互隔离的用户态，其拥有独立的文件系统、网络、系统设置和库函数。<br>
库函数层初始化-是不同的操作系统可以拥有共同的库函数接口，应用程序不需修改。<br>
编程语言层虚拟化-编的程序运行在一个虚拟机上，与具体硬件无关。如Java。<br><br>
>3.虚拟机的优点<br>
良好的封装-虚拟机的运行环境保持便捷，便于随时抓取状态、备份、克隆、挂起和恢复。<br>
多实例-最大限度减少物理资源，提高利用率，便于管理。<br>
隔离-每个应用程序可以再独立的操作系统中运行，互不干涉，崩溃也不会影响其他任务。<br>
硬件无关性-只要拥有相同的硬件抽象层，虚拟机就可以无缝迁移，因此维护和升级简单。<br>
安全-便于控制访问权利，病毒入侵检测等。<br><br>
>4.虚拟化分类<br>
按照虚拟化程度分完全虚拟化和类虚拟化。完全虚拟化-客户及操作系统不需要任何修改即可运行，分软件辅助完全虚拟化和硬件辅助完全虚拟化，完全虚拟化能够模拟所有CPU指令。<br>
类虚拟化-操作系统需要做出适应性修改，回避那些难以模拟的指令。<br>
按照宿主机是否存在独立操作系统分为hypervisor模型和宿主模型，前者需支持所有的物理资源管理（系统启动、内存管理、设备驱动等），效率高、复杂；后者只需调用宿主操作系统API实现虚拟化，宿主操作系统可以是windows、linux，效率低、简单。第三类是两者的混和，VMM位于硬件层之上，但让出部分IO设备管理权给一个运行在特权虚拟机上的特权操作系统，VMM负责处理器和内存虚拟化。<br><br>


容器，也是虚拟层的概念，相对虚拟机而言，容器更加轻量级。虚拟机中需要模拟一台物理机的所有资源，比如你要模拟出有多少CPU、网卡、显卡等等，这些都是在软件层面通过计算资源实现的，这就给物理机凭空增加了不必要的计算量。容器仅仅在操作系统层面向上，对应用的所需各类资源进行了隔离。这也是为何微服务、PaaS和Docker最近如此火爆的原因，资源消耗少，迁移部署简单，成本低。尤其是CNCF提供的一系列工具，更是把容器技术推动到了浪潮之巅。

裸金属服务器

传统物理机：性能天生强大，灵活性天生要凉。
有一些客户为追求极致性能，偏向在云上使用物理机。相对虚拟机，物理机没有虚拟化损耗，性能更强大，但“坑”很多：部署周期慢，运维复杂，架构僵化等等。 
一些云上物理机运维交付都涉及大量人肉操作，产品灵活性偏离了云计算的本意，可能叫“物理服务器托管”更合适。因为不能纯自动化运维，弹性不能很好体现，而且一旦物理机出现宕机，稳定性也不能很好保障。虚拟机的外表 + 物理机的心脏 = 神龙。神龙的全名为弹性裸金属服务器。弹性代表着虚拟机的灵活性，而裸金属意味着物理机的极致性能和安全隔离。神龙架构解决的问题就是让这两者兼得。

>神龙的四大特性 <br>
来看看神龙主打的四大特性：极致性能、分钟级交付、云产品兼容、加密计算。 
极致性能：和物理机一样，客户资源独占，不用和其他客户共享CPU、内存等，性能可以充分挖掘。 <br>
顺带说一下，神龙也具备再次虚拟化的能力，客户线下的专有云可以无缝平移到神龙上，没有嵌套虚拟化的问题。玩法多样，随心而动。 <br>
分钟级交付：在运维管控方面神龙和其他ECS产品保持一致，分钟级交付也能满足企业高性能弹性的需求。 <br>
云产品兼容：与阿里云全站云产品互联互通，比如ECS，VPC，SLB，RDS，EIP等等。
加密计算：除了物理隔离特性外，神龙还采用了芯片级可信执行环境（Intel SGX）。确保加密数据只能在安全可信的环境中计算。这是硬件级别的加密，其信任根是基于处理器芯片，而不是基于底层软件保护，可靠性很高。目前阿里云是国内唯一提供此技术的公有云服务商。 <br>

3.裸金属服务器和传统物理机、虚拟机的区别在哪？

裸金属服务器，让传统物理机具有了自动发放、自动运维、VPC互联、支撑对接共享存储等云的能力。可以项虚拟机一样发放和使用，同时有具备了优秀的集散、存储、网络能力。

弹性云服务器(虚拟机)由多个租户共享物理资源你，而裸金属服务器的资源龟用户独享。对于关键类应用或者性能要求较高的业务（如大数据集群、企业中间件系统），并且要求安全可靠的运行环境，使用裸金属服务器更合适。

裸金属服务器融合了物理机和云主机的各自又是，实现超强超稳的计算能力。

裸金属的又是主要体现在以下几个方面：

1.裸金属服务器具备物理机级别的完整处理器特性

2.裸金属服务器物理机级别的资源隔离又是，特别适合上云部署传统非虚拟化场景的应用。

3.在面对一些复杂的场景及应用上，裸金属服务器比起云主机由更强悍的性能；更高的CPU、内存和带宽性能。

4.逻裸金属服务器上所有数据和应用完全私有化；与传统的服务器租用和服务器托管性质一样。

5.裸金属服务器可以像管理云主机一样简单，通过面板实现意见重启、重载等

6.极高的性价比优势、裸金属服务器结合完全自动化的管理方式、有效降低技术运维成本


## date:2020/6/22
### 1.今日成果
学习了数据标准、数据质量和数据共享

### 2.心得体会
经过老师的讲解，让我认识到了数据统一格式、标准的重要性。良好的数据标准对于数据分析、数据开发的效率大有裨益。

此外我们在获取数据、处理数据时也要保证数据的质量，这样的数据才有利用的价值。

另外使我印象深刻的时数据共享。数据共享主要是处于安全性、隐私性的考虑。是数据处理的重要一个部分。

## date:2020/6/19
### 1.今日成果
(1)开始完成大数据人工智能大作业
(2)学习了lasso、ridge、elasticNet、xgBoost等回归算法。

### 2.lasso、ridge、elasticNet、XGBoost
(1)lasso回归即加上了L1正则化项的线性回归

(2)ridge回归、岭回归即加上了L2正则化项的线性回归

(3)ElasticNet即引入L1-L2正则化项的线性回归

(4)XGBoost即extreme GBDT，是一种在损失函数中加入正则化项的梯度提升决策树，是回归树的一种。


## date:2020/6/18
### 1.今日成果
(1)今天继续学习了pandas中的dataframe

### 2.DataFrame
DataFrame是一个带有标签的2维数据结构，其中的列可能具有不同的类型。您可以将其看作电子表格或SQL表，或一系列对象的dict。它通常是最常用的panda对象。与Series一样，DataFrame接受许多不同类型的输入：

Dict of 1D ndarrays, lists, dicts, or Series

2-D numpy.ndarray

Structured or record ndarray

A Series

Another DataFrame

(1)DateFrame的初始化

i.通过字典

```python
In [37]: d = {'one': pd.Series([1., 2., 3.], index=['a', 'b', 'c']),
       'two': pd.Series([1., 2., 3., 4.], index=['a', 'b', 'c', 'd'])}
In [38]: df = pd.DataFrame(d)

In [39]: df
Out[39]: 
   one  two
a  1.0  1.0
b  2.0  2.0
c  3.0  3.0
d  NaN  4.0

```
ii.从结构化数组
```python
In [47]: data = np.zeros((2, ), dtype=[('A', 'i4'), ('B', 'f4'), ('C', 'a10')])

In [48]: data[:] = [(1, 2., 'Hello'), (2, 3., "World")]

In [49]: pd.DataFrame(data)
Out[49]: 
   A    B         C
0  1  2.0  b'Hello'
1  2  3.0  b'World'
```
(2)dataframe的增删查改
i.增加
```python
df['one_trunc'] = df['one'][:2]
//直接赋值

df.insert(1, 'bar', df['one'])
//把数据df['one']插入到下标为1的列，标签为’bar‘
```

ii.删除,以下两种都可以
```python
In [65]: del df['two']

In [66]: three = df.pop('three')
```
iii.查和改

|Operation|Syntax|Result|
:---:|:----:|:----:
Select column|df[col]|Series
Select row by label|df.loc[label]|Series
Select row by integer location|df.iloc[loc]|Series
Slice rows|df[5:10]|DataFrame
Select rows by boolean vector|df[bool_vec]|DataFrame

```python
In [82]: df.iloc[2]
Out[82]: 
one             3
bar             3
flag         True
foo           bar
one_trunc     NaN
Name: c, dtype: object
```


####
## date:2020/6/17
### 1.今日成果
(1)今天学习了pandas关于数据结构的部分

### 2.pandas中的series
Series是一维带标签的数组，可以容纳任何数据类型(整数、字符串、浮点数、Python对象等)。axis标签统称为索引。创建系列的基本方法是调用
```
s = pd.Series(data, index=index)
```
比如有以下例子：
```python
In [3]: s = pd.Series(np.random.randn(5), index=['a', 'b', 'c', 'd', 'e'])

In [4]: s
Out[4]: 
a    0.469112
b   -0.282863
c   -1.509059
d   -1.135632
e    1.212112
dtype: float64
```
传入的数据除了是数组之外，还可以是字典类型的数据。事实上，series跟ndarray十分的相似。它也可以通过下标直接访问数据，如s[0]。如果像把series转化成数组，我们可以像下面这样做。

```python
s.array
//把series转换成PandasArray类型
s.to_numpy()
//把Series转换成Numpy数组类型
```

series除了跟ndarray非常相似之外，它的某些操作方式也跟字典如出一辙：如我们访问某一个下标的数据，也可以是s['a'].

最后，Series有一个name属性，可以指定它的名字。
```
 s = pd.Series(np.random.randn(5), name='something')
```


## date:2020/6/16
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

