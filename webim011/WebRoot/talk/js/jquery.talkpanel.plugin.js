$.fn.talkPanel = function(){
	$(this).find("ul, .subpanel").css({ 'height' : 'auto'}); //Reset subpanel and ul height
	var windowHeight = $(window).height(); //Get the height of the browser viewport
	var panelsub = $(this).find(".subpanel").height(); //Get the height of subpanel	
	var panelAdjust = windowHeight - 100; //Viewport height - 100px (Sets max height of subpanel)
	var ulAdjust =  panelAdjust - 25; //Calculate ul size after adjusting sub-panel (27px is the height of the base panel)
	if ( panelsub >= panelAdjust ) {	 //If subpanel is taller than max height...
		$(this).find(".subpanel").css({ 'height' : panelAdjust }); //Adjust subpanel to max height
		$(this).find("ul").css({ 'height' : ulAdjust}); //Adjust subpanel ul to new size
	}
	else if ( panelsub < panelAdjust ) { //If subpanel is smaller than max height...
		$(this).find("ul").css({ 'height' : 'auto'}); //Set subpanel ul to auto (default size)
	}
	if($(this).attr("id")=="chatingpanel"){
		$(this).find(".subchatpanel").css({ 'height' : 360 }); //Adjust subpanel to max height
		$(this).find("ul").css({ 'height' : 360-25});
	}
};