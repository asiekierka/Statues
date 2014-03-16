package info.jbcs.minecraft.statues;

import info.jbcs.minecraft.gui.GuiElement;
import info.jbcs.minecraft.gui.InputMouseEvent;
import info.jbcs.minecraft.gui.TexturedBox;

public class Gui2dScroller extends GuiElement {
	public double u,v;
	int sliderX,sliderY,sliderW,sliderH;
	int sliderDownX=-1,sliderDownY;
	TexturedBox boxSlider;

	
	public Gui2dScroller(int x, int y, int w, int h,String rulerTexture,int rw,int rh,int ru,int rv,double initU,double initV) {
		super(x, y, w, h);
		
		boxSlider = new TexturedBox(rulerTexture, ru, rv, sliderW=rw, sliderH=rh, 0, 0, 0, 0);
		set(initU,initV);
	}
	
	public void set(double uu,double vv){
		sliderX=(int) ((u=uu)*(w-sliderW));
		sliderY=(int) ((v=vv)*(h-sliderH));
		
		onChange();
	}

	@Override
	public void onAdded() {
	}

	@Override
	public void render() {
		boxSlider.render(gui, (x+sliderX), (y+sliderY), sliderW, sliderH);
	}

	@Override
	public void mouseDown(InputMouseEvent ev) {
		if(! isMouseOverSlider(ev)){
			sliderDownX=-1;
			return;
		}
		
		sliderDownX=ev.x-x-sliderX;
		sliderDownY=ev.y-y-sliderY;
	}

	@Override
	public void mouseUp(InputMouseEvent ev) {
		sliderDownX=-1;
	}
	

	@Override
	public void mouseMove(InputMouseEvent ev) {
		if(sliderDownX==-1) return;
		
		int oldSliderX=sliderX;
		int oldSliderY=sliderY;
		sliderX=ev.x-sliderDownX-x; if(sliderX<0) sliderX=0; if(sliderX>=w-sliderW) sliderX=w-sliderW;
		sliderY=ev.y-sliderDownY-y; if(sliderY<0) sliderY=0; if(sliderY>=h-sliderH) sliderY=h-sliderH;
		
		u=1.0*sliderX/(w-sliderW);
		v=1.0*sliderY/(h-sliderH);
		
		if(oldSliderX!=sliderX || oldSliderY!=sliderY)
			onChange();
	}	

	boolean isMouseOverSlider(InputMouseEvent ev) {
		return ev.x >= x+sliderX && ev.x < x+sliderX + sliderW && ev.y >= y+sliderY && ev.y < y+sliderY + sliderH;
	}

	void onChange(){
		
	}
}
