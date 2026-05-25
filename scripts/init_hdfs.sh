#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(cd "${SCRIPT_DIR}/.." && pwd)"

echo "[init_hdfs] 开始初始化 HDFS 输入目录..."
hdfs dfs -mkdir -p /hotel/input
hdfs dfs -mkdir -p /hotel/input/raw
hdfs dfs -mkdir -p /hotel/output

echo "[init_hdfs] 清理旧输入数据..."
hdfs dfs -rm -r -f /hotel/input/raw >/dev/null 2>&1 || true
hdfs dfs -mkdir -p /hotel/input/raw

echo "[init_hdfs] 上传本地 CSV 到 HDFS..."
hdfs dfs -put "${ROOT_DIR}/data/raw/hotel_info.csv" /hotel/input/raw/
hdfs dfs -put "${ROOT_DIR}/data/raw/hotel_orders.csv" /hotel/input/raw/
hdfs dfs -put "${ROOT_DIR}/data/raw/hotel_rooms.csv" /hotel/input/raw/

echo "[init_hdfs] 当前 HDFS 输入目录："
hdfs dfs -ls /hotel/input/raw
echo "[init_hdfs] 完成。"
