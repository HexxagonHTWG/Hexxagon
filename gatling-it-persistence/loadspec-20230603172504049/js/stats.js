var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "400",
        "ok": "304",
        "ko": "96"
    },
    "minResponseTime": {
        "total": "13",
        "ok": "13",
        "ko": "1179"
    },
    "maxResponseTime": {
        "total": "8762",
        "ok": "8762",
        "ko": "4571"
    },
    "meanResponseTime": {
        "total": "2322",
        "ok": "1956",
        "ko": "3480"
    },
    "standardDeviation": {
        "total": "2392",
        "ok": "2613",
        "ko": "666"
    },
    "percentiles1": {
        "total": "2250",
        "ok": "52",
        "ko": "3730"
    },
    "percentiles2": {
        "total": "4003",
        "ok": "4112",
        "ko": "3956"
    },
    "percentiles3": {
        "total": "7321",
        "ok": "7885",
        "ko": "4194"
    },
    "percentiles4": {
        "total": "8437",
        "ok": "8475",
        "ko": "4419"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 178,
    "percentage": 45
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 7,
    "percentage": 2
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 119,
    "percentage": 30
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 96,
    "percentage": 24
},
    "meanNumberOfRequestsPerSecond": {
        "total": "16.667",
        "ok": "12.667",
        "ko": "4"
    }
},
contents: {
"req_mysql---save-7eab2": {
        type: "REQUEST",
        name: "MySql - Save",
path: "MySql - Save",
pathFormatted: "req_mysql---save-7eab2",
stats: {
    "name": "MySql - Save",
    "numberOfRequests": {
        "total": "100",
        "ok": "100",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "2801",
        "ok": "2801",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "8762",
        "ok": "8762",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "5291",
        "ok": "5291",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "1699",
        "ok": "1699",
        "ko": "-"
    },
    "percentiles1": {
        "total": "4646",
        "ok": "4646",
        "ko": "-"
    },
    "percentiles2": {
        "total": "6531",
        "ok": "6531",
        "ko": "-"
    },
    "percentiles3": {
        "total": "8381",
        "ok": "8381",
        "ko": "-"
    },
    "percentiles4": {
        "total": "8553",
        "ok": "8553",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 100,
    "percentage": 100
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "4.167",
        "ok": "4.167",
        "ko": "-"
    }
}
    },"req_mongodb---save-5c0eb": {
        type: "REQUEST",
        name: "MongoDB - Save",
path: "MongoDB - Save",
pathFormatted: "req_mongodb---save-5c0eb",
stats: {
    "name": "MongoDB - Save",
    "numberOfRequests": {
        "total": "100",
        "ok": "100",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "13",
        "ok": "13",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "2559",
        "ok": "2559",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "225",
        "ok": "225",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "516",
        "ok": "516",
        "ko": "-"
    },
    "percentiles1": {
        "total": "33",
        "ok": "33",
        "ko": "-"
    },
    "percentiles2": {
        "total": "52",
        "ok": "52",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1399",
        "ok": "1399",
        "ko": "-"
    },
    "percentiles4": {
        "total": "2221",
        "ok": "2221",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 89,
    "percentage": 89
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 4,
    "percentage": 4
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 7,
    "percentage": 7
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "4.167",
        "ok": "4.167",
        "ko": "-"
    }
}
    },"req_mongodb---load-1e535": {
        type: "REQUEST",
        name: "MongoDB - Load",
path: "MongoDB - Load",
pathFormatted: "req_mongodb---load-1e535",
stats: {
    "name": "MongoDB - Load",
    "numberOfRequests": {
        "total": "100",
        "ok": "98",
        "ko": "2"
    },
    "minResponseTime": {
        "total": "19",
        "ok": "19",
        "ko": "1179"
    },
    "maxResponseTime": {
        "total": "2558",
        "ok": "2558",
        "ko": "2228"
    },
    "meanResponseTime": {
        "total": "228",
        "ok": "198",
        "ko": "1704"
    },
    "standardDeviation": {
        "total": "519",
        "ok": "473",
        "ko": "525"
    },
    "percentiles1": {
        "total": "38",
        "ok": "37",
        "ko": "1704"
    },
    "percentiles2": {
        "total": "49",
        "ok": "49",
        "ko": "1966"
    },
    "percentiles3": {
        "total": "1409",
        "ok": "1329",
        "ko": "2176"
    },
    "percentiles4": {
        "total": "2231",
        "ok": "2154",
        "ko": "2218"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 89,
    "percentage": 89
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 3,
    "percentage": 3
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 6,
    "percentage": 6
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 2,
    "percentage": 2
},
    "meanNumberOfRequestsPerSecond": {
        "total": "4.167",
        "ok": "4.083",
        "ko": "0.083"
    }
}
    },"req_mysql---load-068dd": {
        type: "REQUEST",
        name: "MySql - Load",
path: "MySql - Load",
pathFormatted: "req_mysql---load-068dd",
stats: {
    "name": "MySql - Load",
    "numberOfRequests": {
        "total": "100",
        "ok": "6",
        "ko": "94"
    },
    "minResponseTime": {
        "total": "2171",
        "ok": "3744",
        "ko": "2171"
    },
    "maxResponseTime": {
        "total": "4571",
        "ok": "4120",
        "ko": "4571"
    },
    "meanResponseTime": {
        "total": "3544",
        "ok": "3954",
        "ko": "3518"
    },
    "standardDeviation": {
        "total": "607",
        "ok": "136",
        "ko": "616"
    },
    "percentiles1": {
        "total": "3743",
        "ok": "3944",
        "ko": "3735"
    },
    "percentiles2": {
        "total": "3980",
        "ok": "4082",
        "ko": "3966"
    },
    "percentiles3": {
        "total": "4193",
        "ok": "4118",
        "ko": "4195"
    },
    "percentiles4": {
        "total": "4413",
        "ok": "4120",
        "ko": "4422"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 0,
    "percentage": 0
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 0,
    "percentage": 0
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 6,
    "percentage": 6
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 94,
    "percentage": 94
},
    "meanNumberOfRequestsPerSecond": {
        "total": "4.167",
        "ok": "0.25",
        "ko": "3.917"
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