<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>


<div class="container-fluid">
	<div class="grid-stack grid-stack-instance-1055" data-gs-current-height="16" style="height: 1260px;">
		<div data-gs-x="0" data-gs-y="0" data-gs-width="3" data-gs-height="5" class="grid-stack-item ui-draggable ui-resizable ui-resizable-autohide">
			<div class="grid-stack-item-content ui-draggable-handle"><input class="guage" style="visibility: hidden;" value="0.0"/></div>
			<div class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se" style="z-index: 90; display: none;"></div>
		</div>
		<div data-gs-x="3" data-gs-y="0" data-gs-width="7" data-gs-height="5" class="grid-stack-item ui-draggable ui-resizable ui-resizable-autohide" style="">
			<div class="grid-stack-item-content ui-draggable-handle">
				
				<div >
					<div class="chartjs-size-monitor">	
						<div class="chartjs-size-monitor-expand">
							<div class=""></div>
						</div>
						<div class="chartjs-size-monitor-shrink">
							<div class=""></div>
						</div>
					</div>
					<canvas deviceid="1" feeder-name="Meter 1" dataset="kw1,kw2" class="canvas" style="display: block; height: 300px; width: 800px;" width="800" height="300" class="chartjs-render-monitor"></canvas>
				</div>
				
			</div>
			<div class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se" style="z-index: 90; display: none;"></div>
		</div>
		<div data-gs-x="10" data-gs-y="0" data-gs-width="2" data-gs-height="5" class="grid-stack-item ui-draggable ui-resizable ui-resizable-autohide" style="">
			<div class="grid-stack-item-content ui-draggable-handle">2</div>
			<div class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se" style="z-index: 90; display: none;"></div>
		</div>
	</div>
</div>

<br>

<div class="container-fluid">
	<div class="grid-stack grid-stack-instance-1055" data-gs-current-height="16" style="height: 1260px;">
		<div data-gs-x="0" data-gs-y="0" data-gs-width="3" data-gs-height="5" class="grid-stack-item ui-draggable ui-resizable ui-resizable-autohide">
			<div class="grid-stack-item-content ui-draggable-handle"><input class="guage" style="visibility: hidden;" value="0.0"/></div>
			<div class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se" style="z-index: 90; display: none;"></div>
		</div>
		<div data-gs-x="3" data-gs-y="0" data-gs-width="7" data-gs-height="5" class="grid-stack-item ui-draggable ui-resizable ui-resizable-autohide" style="">
			<div class="grid-stack-item-content ui-draggable-handle">
				
				<div >
					<div class="chartjs-size-monitor">	
						<div class="chartjs-size-monitor-expand">
							<div class=""></div>
						</div>
						<div class="chartjs-size-monitor-shrink">
							<div class=""></div>
						</div>
					</div>
					<canvas deviceid="2" feeder-name="Meter 2" dataset="w1,w2" class="canvas" style="display: block; height: 300px; width: 800px;" width="800" height="300" class="chartjs-render-monitor"></canvas>
				</div>
				
			</div>
			<div class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se" style="z-index: 90; display: none;"></div>
		</div>
		<div data-gs-x="10" data-gs-y="0" data-gs-width="2" data-gs-height="5" class="grid-stack-item ui-draggable ui-resizable ui-resizable-autohide" style="">
			<div class="grid-stack-item-content ui-draggable-handle">2</div>
			<div class="ui-resizable-handle ui-resizable-se ui-icon ui-icon-gripsmall-diagonal-se" style="z-index: 90; display: none;"></div>
		</div>
	</div>
</div>
