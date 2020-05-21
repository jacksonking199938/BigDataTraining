# BigDataTraining
code and summary for course "Big Data Training"

##date: 2020/5/21<br>

###1. 今日成果:<br>
>(1)用Java实现实时监控目录下文件变化<br> 
>(2)将java程序打包成exe可执行文件<br>
2.Java实现实时监控目录下文件变化<br>
>我使用了commons-io包来实现该功能。采用的是观察者模式来实现<br>
>(1)需实现功能:<br> 
>>监控文件的创建、删除和修改<br>
>>监控文件夹的创建、删除和修改<br>
>(2)实现步骤:<br>
>>i. 下载2.0以上的commnons-io包并添加到项目的build path中， maven项目可之间添加依赖<br>
>>ii. 编写继承FileAlterationListenerAdaptor的类FileListener，主要需要重写以下方法<br>
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

###3.将java程序打包成exe可执行文件<br>
>(1)实现步骤：<br>
>>i.将项目导出为可执行的jar文件<br>
>>ii.下载exe4j软件,将可执行的jar文件转化为exe可执行文件。<br>
>>>具体步骤详见：https://blog.csdn.net/zhangdaiscott/article/details/46988003<br>

###4.遇到的问题及其解决：<br>
>(1)生成的exe可执行文件出现闪退出现闪退是因为生成exe可执行文件是未注意32bit 和64bit的区别，默认生成32bit的exe文件，与系统版本不匹配，需要在Configure executable处Advanced Options选择64位。<br>

###5.总结<br>
>了解了commons-io包的基本用法：https://blog.csdn.net/backbug/article/details/99572931。<br>
>熟悉了java程序生成exe可执行文件的方法。<br>
>通过对commons-io源码的了解，加深了对线程池，线程工厂的理解。<br>
>加深了对观察者模式的理解。<br>

