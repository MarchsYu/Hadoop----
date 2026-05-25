function loadJson(path, fallback) {
    return fetch(path)
        .then(function (res) {
            if (!res.ok) {
                throw new Error("load failed");
            }
            return res.json();
        })
        .catch(function () {
            return fallback;
        });
}

function renderRankingChart(data) {
    var chart = echarts.init(document.getElementById("rankingChart"));
    chart.setOption({
        title: { text: "酒店销售数量排名（示例）" },
        tooltip: { trigger: "axis" },
        xAxis: {
            type: "category",
            axisLabel: { rotate: 25 },
            data: data.map(function (item) { return item.hotel; })
        },
        yAxis: { type: "value", name: "销售数量" },
        series: [{
            name: "销售数量",
            type: "bar",
            data: data.map(function (item) { return item.sales; }),
            itemStyle: { color: "#5470c6" }
        }]
    });
}

function renderCityChart(data) {
    var chart = echarts.init(document.getElementById("cityChart"));
    chart.setOption({
        title: { text: "各城市整体出租率（示例）" },
        tooltip: { trigger: "axis" },
        xAxis: {
            type: "category",
            data: data.map(function (item) { return item.city; })
        },
        yAxis: { type: "value", name: "出租率(%)" },
        series: [{
            name: "城市出租率",
            type: "bar",
            data: data.map(function (item) { return item.rate; }),
            itemStyle: { color: "#91cc75" }
        }]
    });
}

function renderRoomTypeChart(data) {
    var chart = echarts.init(document.getElementById("roomTypeChart"));
    chart.setOption({
        title: { text: "不同房型出租率对比（示例）" },
        tooltip: { trigger: "axis" },
        legend: { data: data.cities },
        xAxis: { type: "category", data: data.roomTypes },
        yAxis: { type: "value", name: "出租率(%)" },
        series: data.cities.map(function (city, idx) {
            return {
                name: city,
                type: "bar",
                data: data.values[idx]
            };
        })
    });
}

var rankingFallback = [
    { hotel: "外滩云景酒店", sales: 1200 },
    { hotel: "京华商务酒店", sales: 1000 },
    { hotel: "中央大街冰城酒店", sales: 910 },
    { hotel: "长安雅居酒店", sales: 860 },
    { hotel: "浦东会展酒店", sales: 780 }
];

var cityFallback = [
    { city: "北京", rate: 15.6 },
    { city: "上海", rate: 17.3 },
    { city: "哈尔滨", rate: 14.2 }
];

var roomTypeFallback = {
    roomTypes: ["单人间", "大床", "标间", "商务间"],
    cities: ["北京", "上海", "哈尔滨"],
    values: [
        [13.4, 16.2, 14.8, 11.3],
        [14.6, 18.9, 16.4, 12.7],
        [12.8, 15.1, 13.6, 10.9]
    ]
};

Promise.all([
    loadJson("./data/ranking.json", rankingFallback),
    loadJson("./data/city_occupancy.json", cityFallback),
    loadJson("./data/room_type_occupancy.json", roomTypeFallback)
]).then(function (result) {
    renderRankingChart(result[0]);
    renderCityChart(result[1]);
    renderRoomTypeChart(result[2]);
});
