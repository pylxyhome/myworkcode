var MyWin = new Win();
var imgfile = "images/poluo/"; //设置图片路径,相对或绝对都行
var imgname=new Array();
var img = new Array();
imgname[0] = imgfile+"c.gif";  
imgname[1] = imgfile+"b1.png";
imgname[2] = imgfile+"l1.png";  
imgname[3] = imgfile+"l2.png";  
imgname[4] = imgfile+"r1.png";  
imgname[5] = imgfile+"r2.png";  
imgname[6] = imgfile+"t1.png";  
imgname[7] = imgfile+"t2.png";  
imgname[8] = imgfile+"t3.png";  
//预读图片
for (i=0;i<=imgname.length-1;i++)
{
   img[i] = new Image();
   img[i].src = imgname[i];
}
var zIndex = 0;
var Winid  = 0;
var Ie = /msie/i.test(navigator.userAgent);
function $(Id){return(top.document.getElementById(Id))}
function Win()
{
 this.Create = function(mask,title, wbody, w, h, l, t)
 {
  Winid++;
  mask = mask;
  title = title || "新窗口 - 加载中...";
  wbody = wbody || " <p align='center'>正在载入…</p>";
  w = w || 350;
  h = h || 150;
  cw = top.document.documentElement.clientWidth;
  ch = top.document.documentElement.clientHeight;
  sw = top.document.documentElement.scrollWidth;
  sh = top.document.documentElement.scrollHeight;
        st = (top.document.documentElement.scrollTop || top.document.body.scrollTop);
  if (w > cw)
  ww = 0;
  else
  ww = (cw - w)/2;
  if (h > ch)
  hh = 0;
  else
  hh = (st + (ch - h)/2);
  l = l || ww;
  t = t || hh;
  
        if (mask != "no"){
    var ndiv = top.document.createElement("DIV");
    ndiv.setAttribute("id", "ndiv"+ Winid);
    ndiv.style.cssText = "width:"+ sw +"px;height:"+ sh +"px;left:0px;top:0px;position:absolute;overflow:hidden;background:#fff;filter:alpha(opacity=20); opacity:0.2;-moz-opacity:0.2;";
    top.document.body.appendChild(ndiv);
    if (Ie)
    {
    var niframe = top.document.createElement("iframe");
    niframe.style.width = sw;
    niframe.style.height = sh;    
          niframe.style.top = "0px";    
          niframe.style.left = "0px";  
          niframe.style.visibility = "inherit";    
          niframe.style.filter = "alpha(opacity=0)";    
          niframe.style.position = "absolute";    
          niframe.style.zIndex = -1;   
    ndiv.insertAdjacentElement("afterBegin",niframe);
    }
        }
  var mywin = top.document.createElement("DIV");
  mywin.setAttribute("id", "win"+ Winid);
  mywin.style.cssText = "width:"+ w +"px;height:"+ h +"px;left:0px;top:0px;position:absolute;overflow:hidden;padding:0px;font-family:Arial, 宋体";
  mywin.style.zIndex = ++zIndex;
  top.document.body.appendChild(mywin);
  
  var mytie = top.document.createElement("DIV");
  var myboy = top.document.createElement("DIV");
  var mybom = top.document.createElement("DIV");
  
  mytie.style.cssText = "overflow:hidden;height:30px;font-weight:bold;font-size:14px;width:100%";
  myboy.style.cssText = "overflow:hidden;width:100%";
  mybom.style.cssText = "overflow:hidden;height:30px;width:100%";
  
  mywin.appendChild(mytie);
  mywin.appendChild(myboy);
  mywin.appendChild(mybom);
  var wintag = [[mytie, 30, 15, "t1"], [mytie, 30, w-30, "t2"], [mytie, 30, 15, "t3"], [myboy, h-45, 15, "l1"], [myboy, h-47, w-32], [myboy, h-45, 15, "r1"], [mybom, 15, 15, "l2"], [mybom, 15, w-30, "b1"], [mybom, 15, 15, "r2"]];
  for (var i = 0; i < 9; i++)
  {
   var temp = top.document.createElement("DIV");
   temp.setAttribute("Fid", "win"+ Winid);
   temp.setAttribute("id", "win_1"+ i);
   wintag[i][0].appendChild(temp);
   if(i==5){
     temp.style.cssText = "float:right;height:"+ wintag[i][1] +"px;width:"+ wintag[i][2] +"px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+imgfile+""+ wintag[i][3] +".png', sizingMethod='scale');background:url('"+imgfile+""+ wintag[i][3] +".png') !important;background:;";
   }
   else  if (wintag[i][3])
   {
    temp.style.cssText = "float:left;height:"+ wintag[i][1] +"px;width:"+ wintag[i][2] +"px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+imgfile+""+ wintag[i][3] +".png', sizingMethod='scale');background:url('"+imgfile+""+ wintag[i][3] +".png') !important;background:;";
   }
   else
   {
    temp.style.cssText = "float:left;filter:alpha(Opacity=95,style=0);opacity:0.95;height:"+ wintag[i][1] +"px;width:"+ wintag[i][2] +"px;background:#f7f7f7;border:1px solid #666;overflow:hidden;padding:0px";
   }

  }
  mytie.childNodes[1].innerHTML = "<div id=\"topid\" style=\"cursor:move;width:"+(w-50)+"px;position:absolute;overflow:hidden;height:15px;top:12px;padding-left:4px;padding-right:4px;\"></div><div style=\"position:absolute;background:url('"+imgfile+"c.gif');overflow:hidden;width:43px;height:17px;top:7px !important;cursor:pointer;right:15px\" title=\"关闭窗口\" onclick=\"MyWin.Close('win"+ Winid +"',10); MyWin.ndiv('ndiv"+ Winid +"',10);\"></div>";
  
  this.Title("win"+ Winid, title);
  this.Body("win"+ Winid, wbody);
  this.Move_e("win"+ Winid, l, t, 0, 0);
  Drag.init(top.document.getElementById("topid"), top.document.getElementById("win"+Winid));
  return(mywin);
 }
 this.Title = function(Id, title)
 {
     if (Id == null) return;
     var o = $(Id);
     if (!o) return;
     o.childNodes[0].childNodes[1].childNodes[0].innerHTML = title;
 }
 this.Body = function(Id, wbody)
 {
     if (Id == null) return;
     var o = $(Id);
        if (!o) return;
        if (wbody.slice(0, 4) == "[pg]")
            o.childNodes[1].childNodes[1].innerHTML = "<iframe onfocus=\"MyWin.Show('"+ Id +"',this)\" src='"+ wbody.slice(4) +"' frameBorder='0' marginHeight='0' marginWidth='0' scrolling='no' width='100%' height='100%'></iframe>";
        else
            o.childNodes[1].childNodes[1].innerHTML = wbody;
 }
 this.Show = function(Id)
    {     
     if (Id == null) return;
     var o = $(Id);
        if (!o) return;
     o.style.zIndex = ++zIndex;
    }
    this.Move_e = function(Id, l , t, ll, tt)
    {
     if (typeof(window["ct"+ Id]) != "undefined") clearTimeout(window["ct"+ Id]);
     var o = $(Id);
     if (!o) return;
      o.style.left = l +"px";
      o.style.top = t +"px";
    // window["ct"+ Id] = window.setTimeout("MyWin.Move_e('"+ Id +"', "+ l +" , "+ t +", "+ ll +", "+ tt +")", 1);
    }
    this.Close = function(Id, Opacity)
    {
     if (typeof(window["et"+ Id]) != "undefined") clearTimeout(window["et"+ Id]);
     var o = $(Id);
     if (!o) return;
     if (Opacity == 10) o.childNodes[0].childNodes[1].innerHTML = "";
     if (Ie)
     {
      o.style.filter = "alpha(opacity="+ Opacity +",style=0)";
     }
     else
     {
      o.style.opacity = Opacity / 10;
     }
     if (Opacity > 20)
      Opacity -= 10;
     else
      Opacity--;
     if (Opacity <= 0)
     {
         if (o.getElementsByTagName("IFRAME").length != 0)
         {
             o.getElementsByTagName("IFRAME").src = "about:blank";
         }
         o.innerHTML = "";
      top.document.body.removeChild(o);
      return;
     }
     window["et"+ Id] = window.setTimeout("MyWin.Close('"+ Id +"', "+ Opacity +")", 1);
    }
    this.ndiv = function(Id, Opacity)
    {
     var o = $(Id);
     if (!o) return;
     o.innerHTML = "";
  top.document.body.removeChild(o);
  return;
    }
}



var Drag={
    "obj":null,
	"init":function(handle, dragBody, e){
		if (e == null) {
			handle.onmousedown=Drag.start;
		}
		handle.root = dragBody;
		if(isNaN(parseInt(handle.root.style.left)))handle.root.style.left="0px";
		if(isNaN(parseInt(handle.root.style.top)))handle.root.style.top="0px";
		handle.root.onDragStart=new Function();
		handle.root.onDragEnd=new Function();
		handle.root.onDrag=new Function();
		if (e !=null) {
			var handle=Drag.obj=handle;
			e=Drag.fixe(e);
			var top=parseInt(handle.root.style.top);
			var left=parseInt(handle.root.style.left);
			handle.root.onDragStart(left,top,e.pageX,e.pageY);
			handle.lastMouseX=e.pageX;
			handle.lastMouseY=e.pageY;
			top.document.onmousemove=Drag.drag;
			top.document.onmouseup=Drag.end;
		}
	},
	"start":function(e){
		var handle=Drag.obj=this;
		e=Drag.fixEvent(e);
		var top=parseInt(handle.root.style.top);
		var left=parseInt(handle.root.style.left);
		//alert(left);
		handle.root.onDragStart(left,top,e.pageX,e.pageY);
		handle.lastMouseX=e.pageX;
		handle.lastMouseY=e.pageY;
		top.document.onmousemove=Drag.drag;
		top.document.onmouseup=Drag.end;
		return false;
	},
	"drag":function(e){
		e=Drag.fixEvent(e);
				
		var handle=Drag.obj;
		var mouseY=e.pageY;
		var mouseX=e.pageX;
		var top=parseInt(handle.root.style.top);
		var left=parseInt(handle.root.style.left);
		
		if(top.document.all){Drag.obj.setCapture();}else{e.preventDefault();};//作用是将所有鼠标事件捕获到handle对象，对于firefox，以用preventDefault来取消事件的默认动作：

		var currentLeft,currentTop;
		currentLeft=left+mouseX-handle.lastMouseX;
		currentTop=top+(mouseY-handle.lastMouseY);
		handle.root.style.left=currentLeft +"px";
		handle.root.style.top=currentTop+"px";
		handle.lastMouseX=mouseX;
		handle.lastMouseY=mouseY;
		handle.root.onDrag(currentLeft,currentTop,e.pageX,e.pageY);
		return false;
	},
	"end":function(){
		if(top.document.all){Drag.obj.releaseCapture();};//取消所有鼠标事件捕获到handle对象
		top.document.onmousemove=null;
		top.document.onmouseup=null;
		Drag.obj.root.onDragEnd(parseInt(Drag.obj.root.style.left),parseInt(Drag.obj.root.style.top));
		Drag.obj=null;
	},
	"fixEvent":function(e){//格式化事件参数对象
		var sl = Math.max(top.document.documentElement.scrollLeft, top.document.body.scrollLeft);
		var st = Math.max(top.document.documentElement.scrollTop, top.document.body.scrollTop);
		if(typeof e=="undefined")e=window.event;
		if(typeof e.layerX=="undefined")e.layerX=e.offsetX;
		if(typeof e.layerY=="undefined")e.layerY=e.offsetY;
		if(typeof e.pageX == "undefined")e.pageX = e.clientX + sl - top.document.body.clientLeft;
		if(typeof e.pageY == "undefined")e.pageY = e.clientY + st - top.document.body.clientTop;
		return e;
	}
};
