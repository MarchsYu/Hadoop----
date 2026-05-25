#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

echo "[run_all] 1/6 Maven 打包..."
cd "${ROOT_DIR}"
mvn clean package -DskipTests

echo "[run_all] 2/6 初始化 HDFS 输入..."
bash "${SCRIPT_DIR}/init_hdfs.sh"

echo "[run_all] 3/6 清理旧 HDFS 输出..."
bash "${SCRIPT_DIR}/clean_hdfs.sh"

JAR_PATH="$(ls "${ROOT_DIR}"/target/hotel-room-occupancy-analysis-*.jar 2>/dev/null | head -n 1)"
if [ -z "${JAR_PATH}" ]; then
  echo "[run_all] 未找到打包后的 jar 文件，请检查 mvn package 输出。"
  exit 1
fi

echo "[run_all] 使用 Jar: ${JAR_PATH}"

echo "[run_all] 4/6 执行任务1：酒店销售数量排名..."
hadoop jar "${JAR_PATH}" hotel.driver.SalesRankingDriver \
  /hotel/input/raw/hotel_info.csv \
  /hotel/output/ranking

echo "[run_all] 5/6 执行任务2：各城市各酒店出租率..."
hadoop jar "${JAR_PATH}" hotel.driver.HotelOccupancyDriver \
  /hotel/input/raw/hotel_orders.csv \
  /hotel/input/raw/hotel_rooms.csv \
  /hotel/output/hotel_occupancy

echo "[run_all] 6/6 执行任务3：各城市整体出租率..."
hadoop jar "${JAR_PATH}" hotel.driver.CityOccupancyDriver \
  /hotel/output/hotel_occupancy \
  /hotel/output/city_occupancy

echo "[run_all] 所有任务完成。"
echo "[run_all] 可执行以下命令查看结果："
echo "  hdfs dfs -cat /hotel/output/ranking/part-r-00000"
echo "  hdfs dfs -cat /hotel/output/hotel_occupancy/part-r-00000"
echo "  hdfs dfs -cat /hotel/output/city_occupancy/part-r-00000"
