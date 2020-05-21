# BigDataTraining
code and summary for course "Big Data Training"

# date: 2020/5/21
# author: Jackson Kim
1. 今日成果：
    (1)用Java实现实时监控目录下文件变化
    (2)将java程序打包成exe可执行文件
2.Java实现实时监控目录下文件变化
    我使用了commons-io包来实现该功能。采用的是观察者模式来实现。
    (1)需实现功能：
        监控文件的创建、删除和修改
        监控文件夹的创建、删除和修改
    (2)实现步骤：
        i. 下载2.0以上的commnons-io包并添加到项目的build path中， maven项目可之间添加依赖
        ii. 编写继承FileAlterationListenerAdaptor的类FileListener，主要需要重写以下方法
            public void onFileCreate(File file)
            public void onFileChange(File file)
            public void onFileDelete(File file)
            public void onDirectoryCreate(File directory) 
            public void onDirectoryChange(File directory) 
            public void onDirectoryDelete(File directory)
            public void onStart(FileAlterationObserver observer)
            public void onStop(FileAlterationObserver observer)
        iii.编写main方法
            创建观察者对象，创建monitor
3.将java程序打包成exe可执行文件
    (1)实现步骤：
        i.将项目导出为可执行的jar文件
        ii.下载exe4j软件,将可执行的jar文件转化为exe可执行文件。
            具体步骤详见：https://blog.csdn.net/zhangdaiscott/article/details/46988003
4.遇到的问题及其解决：
    (1)生成的exe可执行文件出现闪退
         出现闪退是因为生成exe可执行文件是未注意32bit 和64bit的区别，默认生成32bit的exe文件，与系统版本不匹配，需要在Configure executable处Advanced Options选择64位。
5.总结
    了解了commons-io包的基本用法：https://blog.csdn.net/backbug/article/details/99572931。
    熟悉了java程序生成exe可执行文件的方法。
    通过对commons-io源码的了解，加深了对线程池，线程工厂的理解。
    加深了对观察者模式的理解。
