var stats = {
    type: "GROUP",
name: "All Requests",
path: "",
pathFormatted: "group_missing-name-b06d1",
stats: {
    "name": "All Requests",
    "numberOfRequests": {
        "total": "100",
        "ok": "100",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "449",
        "ok": "449",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1467",
        "ok": "1467",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "997",
        "ok": "997",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "288",
        "ok": "288",
        "ko": "-"
    },
    "percentiles1": {
        "total": "942",
        "ok": "942",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1258",
        "ok": "1258",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1429",
        "ok": "1429",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1448",
        "ok": "1448",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 33,
    "percentage": 33
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 32,
    "percentage": 32
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 35,
    "percentage": 35
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "50",
        "ok": "50",
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
        "total": "100",
        "ok": "100",
        "ko": "0"
    },
    "minResponseTime": {
        "total": "449",
        "ok": "449",
        "ko": "-"
    },
    "maxResponseTime": {
        "total": "1467",
        "ok": "1467",
        "ko": "-"
    },
    "meanResponseTime": {
        "total": "997",
        "ok": "997",
        "ko": "-"
    },
    "standardDeviation": {
        "total": "288",
        "ok": "288",
        "ko": "-"
    },
    "percentiles1": {
        "total": "942",
        "ok": "942",
        "ko": "-"
    },
    "percentiles2": {
        "total": "1258",
        "ok": "1258",
        "ko": "-"
    },
    "percentiles3": {
        "total": "1429",
        "ok": "1429",
        "ko": "-"
    },
    "percentiles4": {
        "total": "1448",
        "ok": "1448",
        "ko": "-"
    },
    "group1": {
    "name": "t < 800 ms",
    "htmlName": "t < 800 ms",
    "count": 33,
    "percentage": 33
},
    "group2": {
    "name": "800 ms <= t < 1200 ms",
    "htmlName": "t >= 800 ms <br> t < 1200 ms",
    "count": 32,
    "percentage": 32
},
    "group3": {
    "name": "t >= 1200 ms",
    "htmlName": "t >= 1200 ms",
    "count": 35,
    "percentage": 35
},
    "group4": {
    "name": "failed",
    "htmlName": "failed",
    "count": 0,
    "percentage": 0
},
    "meanNumberOfRequestsPerSecond": {
        "total": "50",
        "ok": "50",
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
