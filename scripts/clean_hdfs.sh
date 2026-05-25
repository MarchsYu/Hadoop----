#!/usr/bin/env bash
set -e

echo "[clean_hdfs] 删除旧输出目录 /hotel/output ..."
hdfs dfs -rm -r -f /hotel/output >/dev/null 2>&1 || true
hdfs dfs -mkdir -p /hotel/output
echo "[clean_hdfs] 完成。"
