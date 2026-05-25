#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"
RESULT_DIR="${ROOT_DIR}/data/result"
ECHARTS_DATA_DIR="${ROOT_DIR}/echarts/data"

mkdir -p "${RESULT_DIR}"
mkdir -p "${ECHARTS_DATA_DIR}"

echo "[export_result] 清理旧的本地结果..."
rm -f "${RESULT_DIR}/ranking.csv" \
      "${RESULT_DIR}/hotel_occupancy.csv" \
      "${RESULT_DIR}/city_occupancy.csv"

echo "[export_result] 从 HDFS 导出结果..."
hdfs dfs -getmerge /hotel/output/ranking/part-r-* "${RESULT_DIR}/ranking.csv"
hdfs dfs -getmerge /hotel/output/hotel_occupancy/part-r-* "${RESULT_DIR}/hotel_occupancy.csv"
hdfs dfs -getmerge /hotel/output/city_occupancy/part-r-* "${RESULT_DIR}/city_occupancy.csv"

echo "[export_result] 同步到 echarts/data ..."
cp "${RESULT_DIR}/ranking.csv" "${ECHARTS_DATA_DIR}/ranking.csv"
cp "${RESULT_DIR}/hotel_occupancy.csv" "${ECHARTS_DATA_DIR}/hotel_occupancy.csv"
cp "${RESULT_DIR}/city_occupancy.csv" "${ECHARTS_DATA_DIR}/city_occupancy.csv"

echo "[export_result] 导出完成。"
