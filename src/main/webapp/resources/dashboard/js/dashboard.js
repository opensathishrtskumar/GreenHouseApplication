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
      
    });