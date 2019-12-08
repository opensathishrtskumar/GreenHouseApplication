$(function() {

	var datasetMaxSize = 30;
	var COLORS = ['#4dc9f6','#f67019','#f53794','#537bc4','#acc236','#166a8f','#00a950','#58595b','#8549ba'];

	var config = {
		type: 'line',
		data: {labels: [],datasets: []},
		options: {
			responsive: true,
			animation :  {duration: 0},
			title: {display: false, text: 'Feeder Name' },
			tooltips: {ode: 'index',intersect: true},
			hover: {mode: 'nearest',intersect: true},
			elements: {line: {}},
			scales: {
				xAxes: [{
					display: true,
					scaleLabel: { display: true,labelString: 'Time'}
				}],
				yAxes: [{
					display: true,
					scaleLabel: {display: false,labelString: 'Value'}
				}]
			}
		}
	};

	
	var linechart = {};
	
	window.onload = function() {
		//Creates 2d context for all canvas
		$('.canvas').each(function(index, element){
			var ctx = element.getContext('2d');
			var tempConfig = deepClone(config);
			var datasets = $(element).attr('dataset').split(",");
			tempConfig.options.title.text = $(element).attr('feeder-name');
			var deviceid = $(element).attr('deviceid');
			tempConfig.options.title.deviceid = deviceid;
			
			//for every chart instances, add its own dataset as per configuration
			$(datasets).each(function(i,key){
				var dataSetColor = COLORS[i];
				var dataset = {
						label: key,
						backgroundColor: dataSetColor,
						borderColor: dataSetColor,
						data: [],
						fill: false
				};
				tempConfig.data.datasets.push(dataset);
			});
			
			linechart[deviceid] = new Chart(ctx, deepClone(tempConfig));
			
			setInterval(function(){
				var chartInstance = linechart[deviceid];
				var uniqueid = chartInstance.config.options.title.deviceid;
				var dataConfigSets = chartInstance.config.data.datasets;
				var sizeReached = false;
				
				if(chartInstance.config.data.labels.length > datasetMaxSize) {
					chartInstance.config.data.labels.splice(0,1);
					sizeReached = true;
				}
				
				chartInstance.config.data.labels.push(Math.round(Math.random() * 10));
				
				$(dataConfigSets).each(function(j, con){
					
					//Removes entry from begining of stack to give room for more data
					if(sizeReached){
						con.data.splice(0,1);
					}
					//get data using label - con.label
					con.data.push(Math.round(Math.random() * 100));
				});
				
				linechart[uniqueid].update();
				
			},5000);
		});
	};

	/*function addData(){
		Object.keys(linechart).forEach(function(i,element){
			console.log(linechart[element]);
		});
	}*/
	
	function deepClone(src) {
	  return JSON.parse(JSON.stringify(src));
	}
});