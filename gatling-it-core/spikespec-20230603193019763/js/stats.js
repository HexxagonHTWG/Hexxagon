var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "104",
        "ok": "104",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "7",
        "ok": "7",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1364",
        "ok": "1364",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "718",
        "ok": "718",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "452",
        "ok": "452",
        "ko": "-"
    },
    "percentiles1": {
        "total": "516",
        "ok": "516",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1194",
        "ok": "1194",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1323",
        "ok": "1323",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1351",
        "ok": "1351",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 59,
    "percentage": 57
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 21,
    "percentage": 20
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 24,
    "percentage": 23
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "6.933",
        "ok": "6.933",
        "ko": "-"
    }
},
contents: {
"req_field-6f16a": {
        type: "REQUEST",
        name: "Field",
path: "Field",
pathFormatted: "req_field-6f16a",
stats: {
    "name": "Field",
    "numberOfRequests": {
        "total": "104",
        "ok": "104",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "7",
        "ok": "7",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1364",
        "ok": "1364",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "718",
        "ok": "718",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "452",
        "ok": "452",
        "ko": "-"
    },
    "percentiles1": {
        "total": "516",
        "ok": "516",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1194",
        "ok": "1194",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1323",
        "ok": "1323",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1351",
        "ok": "1351",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 59,
    "percentage": 57
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 21,
    "percentage": 20
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 24,
    "percentage": 23
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "6.933",
        "ok": "6.933",
        "ko": "-"
    }
}
    }
}

}

function fillStats(stat){
    $("#numberOfRequests").append(stat.numberOfRequests.total);
    $("#numberOfRequestsOK").append(stat.numberOfRequests.ok);
    $("#numberOfRequestsKO").append(stat.numberOfRequests.ko);

    $("#minResponseTime").append(stat.minResponseTime.total);
    $("#minResponseTimeOK").append(stat.minResponseTime.ok);
    $("#minResponseTimeKO").append(stat.minResponseTime.ko);

    $("#maxResponseTime").append(stat.maxResponseTime.total);
    $("#maxResponseTimeOK").append(stat.maxResponseTime.ok);
    $("#maxResponseTimeKO").append(stat.maxResponseTime.ko);

    $("#meanResponseTime").append(stat.meanResponseTime.total);
    $("#meanResponseTimeOK").append(stat.meanResponseTime.ok);
    $("#meanResponseTimeKO").append(stat.meanResponseTime.ko);

    $("#standardDeviation").append(stat.standardDeviation.total);
    $("#standardDeviationOK").append(stat.standardDeviation.ok);
    $("#standardDeviationKO").append(stat.standardDeviation.ko);

    $("#percentiles1").append(stat.percentiles1.total);
    $("#percentiles1OK").append(stat.percentiles1.ok);
    $("#percentiles1KO").append(stat.percentiles1.ko);

    $("#percentiles2").append(stat.percentiles2.total);
    $("#percentiles2OK").append(stat.percentiles2.ok);
    $("#percentiles2KO").append(stat.percentiles2.ko);

    $("#percentiles3").append(stat.percentiles3.total);
    $("#percentiles3OK").append(stat.percentiles3.ok);
    $("#percentiles3KO").append(stat.percentiles3.ko);

    $("#percentiles4").append(stat.percentiles4.total);
    $("#percentiles4OK").append(stat.percentiles4.ok);
    $("#percentiles4KO").append(stat.percentiles4.ko);

    $("#meanNumberOfRequestsPerSecond").append(stat.meanNumberOfRequestsPerSecond.total);
    $("#meanNumberOfRequestsPerSecondOK").append(stat.meanNumberOfRequestsPerSecond.ok);
    $("#meanNumberOfRequestsPerSecondKO").append(stat.meanNumberOfRequestsPerSecond.ko);
}