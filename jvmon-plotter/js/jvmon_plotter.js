var gcGraph;
var gProcId;
var chartFuncNames;
	
function plotGraph(procId){	
    gProcId = procId;
    var procIdDisplayName = procId.substring(1);
    var chartDivId = ["memory", "classes", "threads", "compilation", "gc", "memPool"];
    chartFuncNames = ["Mem", "Class", "Thrd", "Comp", "GC", "MemPool"];
    var legendLabels = ["memoryLegend", "classesLegend", "threadsLegend", "compilationLegend", "gcLegend", "memPoolLegend"];
    var titles = [procIdDisplayName + " - Memory", procIdDisplayName + " - Classes", procIdDisplayName + 
                " - Threads", procIdDisplayName + " - Compilation", procIdDisplayName + " - Garbage Collectors", 
                procIdDisplayName + " - Memory Pool"];
	
    for(var index in chartDivId){
        if(index !=4) {
            g = new Dygraph(
                document.getElementById(chartDivId[index]),
                eval(procId + chartFuncNames[index]),
                {
                    "title" : titles[index],

                    "xlabel" : "Time",
                    "xAxisLabelWidth" : 65,
				
                    "legend" : "always",

                    "strokeWidth" : 3,
                    "fillGraph" : false,
                    "highlightCircleSize" : 3,

                    "labelsDiv": legendLabels[index],
                    "labelsSeparateLines" : true,

                    "rollPeriod": 1,
                    "showRoller": true
                }
                );
        }
    }
	
    // draw Garbage Collector Graph
    gcGraph = new Dygraph(
        document.getElementById(chartDivId[4]),
        eval(procId + chartFuncNames[4]),
        {
            "title" : titles[4],

            "xlabel" : "Time",
            "xAxisLabelWidth" : 60,
				
            "visibility" : eval(gcColumnChooser(false, true)),

            "legend" : "always",

            "strokeWidth" : 3,
            "fillGraph" : false,
            "highlightCircleSize" : 3,

            "labelsDiv": legendLabels[4],
            "labelsSeparateLines" : true,

            "rollPeriod": 1,
            "showRoller": true
        }
        );
    document.getElementById("gcToggle").innerHTML="Toggle GC Time and Count<input type=\"checkbox\" name=\"gcToggle\" style=\"margin:2px;\" " + 
"onclick=\"updateGCGraph(this.checked);\">";
}	

function gcColumnChooser(col1, col2){
    var gcColumnsLine = eval(gProcId + chartFuncNames[4] + "()").split("\n")[0];
    var gcColumns = gcColumnsLine.split(",");
    var gcColumnVisibility = "["
    for(var index in gcColumns){
        if(index > 0)
            gcColumnVisibility += ",";
        if(index%2 == 0)
            gcColumnVisibility += col1;
        else
            gcColumnVisibility += col2;
    }
    return gcColumnVisibility += "]";
}
			
function updateGCGraph(checkboxStatus){
    if(checkboxStatus){
        gcGraph.updateOptions(
        {
            "visibility" : eval(gcColumnChooser(true, false))
        }
        );
    } else {
        gcGraph.updateOptions(
        {
            "visibility" : eval(gcColumnChooser(false, true))
            }
        );
    }
}
