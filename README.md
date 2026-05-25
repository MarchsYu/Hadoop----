# hotel-room-occupancy-analysis

## 1. 项目简介

这是《大数据基础框架应用（Hadoop）》课程设计项目，主题是“酒店客房出租率分析”。  
开发方式是：本地 Windows + Cursor 编码，推送 GitHub，再到学校 Linux 虚拟机 `git clone` 后命令行运行。

## 2. 课程设计目标

1. 使用 HDFS 管理酒店原始数据与统计结果。  
2. 使用 MapReduce 完成清洗和分析。  
3. 完成三个核心任务：  
   - 酒店销售数量排名  
   - 各城市各酒店出租率统计  
   - 各城市整体出租率统计  
4. 使用 ECharts 进行可视化展示。  

## 3. 技术选型

- Java 8
- Maven
- Hadoop 3.x（HDFS + MapReduce）
- ECharts（静态页面）

本项目**不使用** Hive、Spark、Docker，不依赖数据库和 IDE 配置。

## 4. 为什么暂不真实接入 Sqoop / Web 采集

老师案例中提到 Sqoop 和 Web 采集，这里先作为背景说明处理。  
当前版本重点是保证课程答辩时“在 Linux 虚拟机能稳定跑通”。

所以先采用 CSV 模拟数据导入 HDFS，后续如有时间再扩展真实采集链路。

## 5. 项目结构说明

```text
hotel-room-occupancy-analysis/
├── data/
│   ├── raw/
│   │   ├── hotel_info.csv
│   │   ├── hotel_orders.csv
│   │   └── hotel_rooms.csv
│   ├── cleaned/
│   ├── output/
│   └── result/
├── docs/
│   ├── requirement/
│   │   └── teacher-case-requirement.md
│   └── run-guide.md
├── echarts/
│   ├── index.html
│   ├── js/
│   │   └── app.js
│   └── data/
│       ├── ranking.json
│       ├── city_occupancy.json
│       └── room_type_occupancy.json
├── scripts/
│   ├── init_hdfs.sh
│   ├── clean_hdfs.sh
│   ├── run_all.sh
│   └── export_result.sh
├── screenshots/
├── src/main/java/hotel/
│   ├── common/
│   ├── ranking/
│   ├── occupancy/
│   ├── driver/
│   ├── mapper/      # 保留原 Occupancy 占位结构
│   └── reducer/     # 保留原 Occupancy 占位结构
├── pom.xml
├── README.md
└── .gitignore
```

## 6. HDFS 输入输出路径

- 输入目录：`/hotel/input/raw`
  - `/hotel/input/raw/hotel_info.csv`
  - `/hotel/input/raw/hotel_orders.csv`
  - `/hotel/input/raw/hotel_rooms.csv`
- 输出目录：
  - `/hotel/output/ranking`
  - `/hotel/output/hotel_occupancy`
  - `/hotel/output/city_occupancy`

> 路径常量统一在 `hotel.common.JobPathConfig` 中维护。

## 7. MapReduce 任务说明

### 任务1：酒店销售数量排名

- 输入：`hotel_info.csv`
- 清洗：销售数量字段支持 `1000+消费`、`520消费` 这类格式，提取数字
- 输出：`城市,酒店名称,酒店类型,销售数量`
- 说明：当前输出为可排序数据，若需严格全局降序，可在导出后再按第 4 列排序

### 任务2：各城市各酒店出租率统计

- 输入：`hotel_orders.csv` + `hotel_rooms.csv`
- 逻辑：  
  - 订单按 `城市+酒店+房型` 汇总入住天数（作为已出租房晚数）  
  - 客房信息提供房量  
  - 出租率 = 已出租房晚数 / (客房数量 × 365) × 100%
- 输出：`城市,酒店名称,客房类型,已出租房晚数,客房数量,出租率`

### 任务3：各城市整体出租率统计

- 输入：任务2输出
- 逻辑：按城市汇总已出租房晚数和可出租房晚数，计算整体出租率
- 输出：`城市,已出租房晚数,总可出租房晚数,整体出租率`

## 8. GitHub clone 后运行流程（Linux 命令行）

```bash
git clone <你的仓库地址>
cd hotel-room-occupancy-analysis
chmod +x scripts/*.sh
bash scripts/run_all.sh
bash scripts/export_result.sh
```

查看结果：

```bash
hdfs dfs -cat /hotel/output/ranking/part-r-00000
hdfs dfs -cat /hotel/output/hotel_occupancy/part-r-00000 | head
hdfs dfs -cat /hotel/output/city_occupancy/part-r-00000
```

## 9. Linux 虚拟机运行步骤（分步版）

1. 检查环境：
   ```bash
   java -version
   mvn -version
   hadoop version
   ```
2. 打包：
   ```bash
   mvn clean package -DskipTests
   ```
3. 初始化输入：
   ```bash
   bash scripts/init_hdfs.sh
   ```
4. 清理输出：
   ```bash
   bash scripts/clean_hdfs.sh
   ```
5. 运行任务：
   ```bash
   JAR=$(ls target/hotel-room-occupancy-analysis-*.jar | head -n 1)
   hadoop jar "$JAR" hotel.driver.SalesRankingDriver /hotel/input/raw/hotel_info.csv /hotel/output/ranking
   hadoop jar "$JAR" hotel.driver.HotelOccupancyDriver /hotel/input/raw/hotel_orders.csv /hotel/input/raw/hotel_rooms.csv /hotel/output/hotel_occupancy
   hadoop jar "$JAR" hotel.driver.CityOccupancyDriver /hotel/output/hotel_occupancy /hotel/output/city_occupancy
   ```

## 10. 常见问题

### 1) output already exists

```bash
bash scripts/clean_hdfs.sh
```

### 2) java version 不匹配

确保是 Java 8，必要时设置：

```bash
export JAVA_HOME=/path/to/jdk8
export PATH=$JAVA_HOME/bin:$PATH
```

### 3) mvn command not found

Maven 未安装或未配置 PATH，安装后确认 `mvn -version`。

### 4) hadoop command not found

Hadoop 未安装或未配置 PATH，确认 `hadoop version` 可执行。

### 5) CSV 中文乱码

- 本仓库 CSV 为 UTF-8；
- Linux 端建议使用支持 UTF-8 的终端与编辑器；
- Windows 查看建议使用 VSCode/Cursor。

## 11. 展示时建议截图

1. `git clone` 成功
2. `mvn package` 成功
3. `hdfs dfs -ls /hotel/input/raw`
4. MapReduce 进度 `map 100% reduce 100%`
5. `hdfs dfs -cat` 任务结果
6. `echarts/index.html` 页面效果

## 12. 备注

- 所有脚本均使用相对路径，不依赖本机绝对路径。  
- 项目可通过命令行独立运行，不依赖 IntelliJ/Cursor。  
- `target/`、日志和临时输出已在 `.gitignore` 中排除。  
