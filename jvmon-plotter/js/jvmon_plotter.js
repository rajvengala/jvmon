var gcGraph;
var gProcId;
var memPoolGraph;
var chartFuncNames;
	
function plotGraph(procId){	
    gProcId = procId;
    var procIdDisplayName = procId.substring(1);
    var chartDivId = ["memory", "classes", "threads", "compilation", "gc", "memPool"];
    chartFuncNames = ["Mem", "Class", "Thrd", "Comp", "GC", "MemPool"];
    var legendLabels = ["memoryLegend", "classesLegend", "threadsLegend", "compilationLegend", "gcLegend", "memPoolLegend"];
    var titles = [procIdDisplayName + " - Memory", procIdDisplayName + " - Classes", procIdDisplayName + " - Threads", procIdDisplayName + " - Compilation", procIdDisplayName + " - Garbage Collectors",                 procIdDisplayName + " - Memory Pool"];
	
    for(var index in chartDivId){
        if(index < 4) {
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
                });
        }
    }
	
    // draw Garbage Collector Graph
    gcGraph = new Dygraph(
                document.getElementById(chartDivId[4]),
                eval(procId + chartFuncNames[4]),
                {
                    "title" : titles[4],

                    "xlabel" : "Time",
                    "xAxisLabelWidth" : 65,

                    "visibility" : eval(gcColumnChooser(false, true)),

                    "legend" : "always",

                    "strokeWidth" : 3,
                    "fillGraph" : false,
                    "highlightCircleSize" : 3,

                    "labelsDiv": legendLabels[4],
                    "labelsSeparateLines" : true,

                    "rollPeriod": 1,
                    "showRoller": true
                });
    
	document.getElementById("gcToggle").innerHTML="Toggle GC Time and Count<input type=\"checkbox\" name=\"gcToggle\" style=\"margin:2px;\" onclick=\"updateGCGraph(this.checked);\">";    
	
    // draw Memory Pool Graph
    memPoolGraph = new Dygraph(
					document.getElementById(chartDivId[5]),
					eval(procId + chartFuncNames[5]),
					{
						"title" : titles[5],

						"xlabel" : "Time",
						"xAxisLabelWidth" : 65,

						"visibility" : eval(memPoolColumnVisibility()),

						"legend" : "always",

						"strokeWidth" : 3,
						"fillGraph" : false,
						"highlightCircleSize" : 3,

						"labelsDiv": legendLabels[5],
						"labelsSeparateLines" : true,

						"rollPeriod": 1,
						"showRoller": true
					});    
			
    document.getElementById("memPoolChooser").innerHTML = getMemPoolChooserElement();
}	
			
	
function gcColumnChooser(col1, col2){
	var gcColumnsLine = eval(gProcId + chartFuncNames[4] + "()").split("\n")[0];
	var gcColumns = gcColumnsLine.split(",");
	var gcColumnVisibility = new Array();
	for(var index in gcColumns){
		if(index > 0) {
			if(index % 2 == 0)
				gcColumnVisibility.push(col1);
			else 
				gcColumnVisibility.push(col2);
		}
	}
	return gcColumnVisibility;
}

function updateGCGraph(checkboxStatus){
	if(checkboxStatus){
		gcGraph.updateOptions({
			"visibility" : eval(gcColumnChooser(true, false))
		});
	} else {
		gcGraph.updateOptions({
			"visibility" : eval(gcColumnChooser(false, true))
		});
	}
}   

		
var memPoolColumns;
function memPoolColumnVisibility(){
	var memPoolColumnsLine = eval(gProcId + chartFuncNames[5] + "()").split("\n")[0];
	memPoolColumns = memPoolColumnsLine.split(",");
	var memPoolColumnVisibility = new Array();
	for(var index in memPoolColumns){
		if(index > 0 ){
			if(index > 2)
				memPoolColumnVisibility.push(false);
			else
				memPoolColumnVisibility.push(true);
		}
	}
	return memPoolColumnVisibility;
}


function getMemPoolNames(){
	var memPoolNames = new Array();
	for(var i in memPoolColumns){
		if(i % 2 != 0){
			var temp = memPoolColumns[i].split("--")[0];
			memPoolNames.push(temp);
		}
	}
	return memPoolNames;
}

function getMemPoolChooserElement(){
	var memPoolNames = getMemPoolNames();
	var memPoolChooserElement = "";
	for(var j in memPoolNames){
		var memPoolName = memPoolNames[j];
		memPoolChooserElement += memPoolName + "<input type=\"radio\" name=\"group\" id=\"" + memPoolName + "\" style=\"margin:2px;\" onclick=\"updateMemPoolGraph(this.id);\">";
	}
	return memPoolChooserElement;
}

function updateMemPoolGraph(poolId){
	var i = 0;
	var enableMemPool = new Array();
	var memPoolNames = getMemPoolNames();
	for(i in memPoolNames){
		if(memPoolNames[i] == poolId)
			break;
	}
	
	for(var index in memPoolColumns){
		switch(i){
			case index:
				enableMemPool.push(true);
				enableMemPool.push(true);
				break;
				
			default:
				enableMemPool.push(false);
				enableMemPool.push(false);
		}
	}
	memPoolGraph.updateOptions({
		"visibility" : enableMemPool
	});
}  



