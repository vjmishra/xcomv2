package com.sterlingcommerce.xpedx.webchannel.profile.org;

import java.util.List;
import java.util.Map;

import com.sterlingcommerce.webchannel.core.WCAttributeScope;
import com.sterlingcommerce.webchannel.core.WCMashupAction;

@SuppressWarnings("all")
public class XPEDXImpositionCalculator extends WCMashupAction {

	private String sheetSizeW;
	private String sheetSizeH;
	private String trimSizeW;
	private String trimSizeH;
	private String gripperWidth;
	private String colorBarWidth;
	private String sideGuide;
	private String gutter;
	private String option;
	private String selectedNumout;
	private String selectedWaste;
	private List<Map<String, String>> numWasteList;

	public String getSelectedNumout() {
		return selectedNumout;
	}

	public void setSelectedNumout(String selectedNumout) {
		this.selectedNumout = selectedNumout;
	}

	public String getSelectedWaste() {
		return selectedWaste;
	}

	public void setSelectedWaste(String selectedWaste) {
		this.selectedWaste = selectedWaste;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public List<Map<String, String>> getNumWasteList() {
		return numWasteList;
	}

	public void setNumWasteList(List<Map<String, String>> numWasteList) {
		this.numWasteList = numWasteList;
	}

	public String getSheetSizeW() {
		return sheetSizeW;
	}

	public void setSheetSizeW(String sheetSizeW) {
		this.sheetSizeW = sheetSizeW;
	}

	public String getSheetSizeH() {
		return sheetSizeH;
	}

	public void setSheetSizeH(String sheetSizeH) {
		this.sheetSizeH = sheetSizeH;
	}

	public String getTrimSizeW() {
		return trimSizeW;
	}

	public void setTrimSizeW(String trimSizeW) {
		this.trimSizeW = trimSizeW;
	}

	public String getTrimSizeH() {
		return trimSizeH;
	}

	public void setTrimSizeH(String trimSizeH) {
		this.trimSizeH = trimSizeH;
	}

	public String getGripperWidth() {
		return gripperWidth;
	}

	public void setGripperWidth(String gripperWidth) {
		this.gripperWidth = gripperWidth;
	}

	public String getColorBarWidth() {
		return colorBarWidth;
	}

	public void setColorBarWidth(String colorBarWidth) {
		this.colorBarWidth = colorBarWidth;
	}

	public String getSideGuide() {
		return sideGuide;
	}

	public void setSideGuide(String sideGuide) {
		this.sideGuide = sideGuide;
	}

	public String getGutter() {
		return gutter;
	}

	public void setGutter(String gutter) {
		this.gutter = gutter;
	}

	public String execute() {
		return SUCCESS;
	}

	public String getCalculation() {

		List<Map<String, String>> numoutWasteList = XPEDXComputeImposition
				.calcPage(getSheetSizeW(), getSheetSizeH(), getTrimSizeW(),
						getTrimSizeH(), getGripperWidth(), getColorBarWidth(),
						getSideGuide(), getGutter());

		setNumWasteList(numoutWasteList);

		return SUCCESS;
	}

	public String getPageDetails() {

		List<Map<String, String>> numWastList = XPEDXComputeImposition
				.calcPage(getSheetSizeW(), getSheetSizeH(), getTrimSizeW(),
						getTrimSizeH(), getGripperWidth(), getColorBarWidth(),
						getSideGuide(), getGutter());

		int selectedOptionValue = Integer.parseInt(getOption()) - 1;

		Map<String, String> map = numWastList.get(selectedOptionValue);

		String selectedNumoutValue = map.keySet().iterator().next();
		String selectedWasteValue = map.values().iterator().next();

		setSelectedNumout(selectedNumoutValue);
		setSelectedWaste(selectedWasteValue);

		String pageOutput = XPEDXComputeImposition.drawPage(getOption());

		wcContext.setWCAttribute("pageDetail", pageOutput,
				WCAttributeScope.REQUEST);

		return SUCCESS;
	}

}
