# hotel-room-occupancy-analysis

## 1. 项目简介

这是《大数据基础框架应用（Hadoop）》课程设计项目，主题是酒店客房出租率分析。  
当前阶段主要先把工程搭起来，后续再逐步完善 MapReduce 业务逻辑与统计分析流程。

## 2. 技术栈

- Java 8
- Hadoop 3.x（伪分布式环境）
- MapReduce
- Maven
- 开发工具：Cursor / IntelliJ IDEA

## 3. 项目结构说明

```text
hotel-room-occupancy-analysis/
├── data/
│   ├── raw/                    # 原始输入数据
│   │   └── hotel_data.csv
│   └── output/                 # MapReduce 输出目录（运行前需清空）
├── docs/                       # 课程设计文档预留目录
├── screenshots/                # 运行截图预留目录
├── src/
│   └── main/
│       └── java/
│           └── hotel/
│               ├── mapper/
│               │   └── OccupancyMapper.java
│               ├── reducer/
│               │   └── OccupancyReducer.java
│               ├── driver/
│               │   └── OccupancyDriver.java
│               └── utils/      # 工具类预留目录
├── .gitignore
├── pom.xml
└── README.md
```

## 4. Hadoop 运行环境

- 操作系统：Windows 11 + WSL Ubuntu
- Hadoop 版本：3.x（伪分布式）
- JDK 版本：1.8
- 构建工具：Maven

> 说明：本项目默认在本地伪分布式环境调试，后续可根据课程要求切换到集群环境。

## 5. 当前开发进度

- [x] 初始化 Maven 工程
- [x] 搭建标准 MapReduce Java 目录结构
- [x] 编写基础 `README`
- [x] 创建示例输入数据
- [x] 创建 Mapper / Reducer / Driver 占位类
- [ ] 完成出租率统计逻辑
- [ ] 增加结果校验与测试数据

## 6. 后续计划

1. 明确输入字段规范与异常数据处理规则。  
2. 完成 Mapper 和 Reducer 统计逻辑（按日期、房型等维度）。  
3. 输出出租率结果并进行样例验证。  
4. 补充运行截图与课程设计文档。  

## 7. 如何运行（占位）

后续会补充完整运行命令，预计流程如下：

1. 准备输入数据到 HDFS；
2. 使用 Maven 打包生成 Jar；
3. 提交 MapReduce 任务；
4. 查看 HDFS 输出结果并下载分析。

（当前阶段先完成工程初始化，运行命令将在业务逻辑完善后补齐。）
