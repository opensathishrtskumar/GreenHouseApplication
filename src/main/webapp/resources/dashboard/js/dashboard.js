 $(function () {

      var gridOptions = {
        float: false,
        alwaysShowResizeHandle: false,
        resizable: { handles: 'e, se, s, sw, w'},
        animate:true,
        removable:true,
        rtl: true
      };
      
      var guageOptions = {
        	  gagueLabel:'PF',
        	  maxVal:1,
        	  divFact: 0.10,
        	  edgeRadius:130,
        	  numbH:18,
        	  nobW:14,
        	  speedPositionTxtWH:70,
        	  dangerLevel:0.70,
        	  eventListenerType:'keyup'
        };

      $('.grid-stack').gridstack(gridOptions);
      
      $('.guage').each(function(i,item){
    	  var ref = $(item).myfunc(guageOptions); 
          setInterval(function(){
        	  var val = parseFloat(Math.random()).toFixed(2)
        	  ref.setGuageValue(val);
          },2000);
      });
      
    });