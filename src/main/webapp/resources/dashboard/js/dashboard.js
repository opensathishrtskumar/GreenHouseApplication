 $(function () {

      var options = {
        float: false,
        alwaysShowResizeHandle: false,
        resizable: {
            handles: 'e, se, s, sw, w'
        },
        animate:true,
        removable:true,
        rtl: true
      };
      
      $('.grid-stack').gridstack(options);
      
      $("#guage").myfunc({
    	  gagueLabel:'PF',
    	  maxVal:1,
    	  divFact: 0.10,
    	  edgeRadius:130,
    	  numbH:18,
    	  nobW:14,
    	  speedPositionTxtWH:70,
    	  dangerLevel:0.70
    	  }).changePosition(0.0);
      
      
    });