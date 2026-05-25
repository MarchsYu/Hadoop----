# Linux 虚拟机运行指南

本指南用于学校网页端 Linux 虚拟机现场展示，全部步骤均可命令行执行。

## 1. 环境准备

- JDK 1.8
- Maven 3.x
- Hadoop 3.x（含 HDFS、MapReduce）
- Git

建议先检查版本：

```bash
java -version
mvn -version
hadoop version
```

## 2. 拉取项目

```bash
git clone <你的仓库地址>
cd hotel-room-occupancy-analysis
```

## 3. 给脚本加执行权限

```bash
chmod +x scripts/*.sh
```

## 4. 一键运行（推荐演示）

```bash
bash scripts/run_all.sh
```

该脚本会自动执行：

1. `mvn clean package`
2. 初始化 HDFS 输入目录并上传 CSV
3. 清理旧输出目录
4. 依次执行 3 个 MapReduce 任务

## 5. 导出结果到本地目录

```bash
bash scripts/export_result.sh
```

导出后主要文件：

- `data/result/ranking.csv`
- `data/result/hotel_occupancy.csv`
- `data/result/city_occupancy.csv`

## 6. 关键检查命令（建议现场展示）

```bash
hdfs dfs -ls /hotel/input/raw
hdfs dfs -cat /hotel/output/ranking/part-r-00000
hdfs dfs -cat /hotel/output/hotel_occupancy/part-r-00000 | head
hdfs dfs -cat /hotel/output/city_occupancy/part-r-00000
```

## 7. ECharts 展示

先执行 `export_result.sh`，然后打开：

- `echarts/index.html`

如果浏览器限制本地 `fetch`，可在项目根目录临时启动静态服务（可选）：

```bash
python3 -m http.server 8000
```

然后访问：

- `http://localhost:8000/echarts/index.html`

> 注：ECharts 页面已提供默认示例 JSON，即使未导出数据也可展示页面结构。

## 8. 常见问题

### output already exists

报错示例：`File already exists: /hotel/output/...`  
处理：

```bash
bash scripts/clean_hdfs.sh
```

### java version 不匹配

如果 Hadoop 要求 Java 8，请确认 `java -version` 为 1.8，并正确设置 `JAVA_HOME`。

### mvn command not found

说明 Maven 未安装或 PATH 未配置。请安装 Maven 3.x，并把 `mvn` 加入环境变量。

### hadoop command not found

说明 Hadoop 未安装或 PATH 未配置。请确认 `hadoop` 可执行文件已加入 PATH。

### CSV 中文乱码

- 本仓库 CSV 为 UTF-8 编码；
- Linux 查看建议使用 `less` 或 `cat`；
- Windows 打开建议使用支持 UTF-8 的编辑器（例如 VSCode、Cursor）。

## 9. 演示截图建议

1. `git clone` 成功
2. `mvn package` 成功
3. `hdfs dfs -ls /hotel/input/raw`
4. MapReduce 执行日志 `map 100% reduce 100%`
5. `hdfs dfs -cat` 结果
6. `echarts/index.html` 可视化页面
