package com.sterlingcommerce.xpedx.webchannel.catalog.itemdetail;

import com.sterlingcommerce.xpedx.webchannel.catalog.XPEDXItemDetailsAction;

/**
 * Each enum value corresponds to an item attribute long description.
 */
public enum ItemSpecificationAttribute {

	// don't show
	Brand_display(null),
	SKU(null),
	Ontology_display(null),
	Sell_copy_item(null),
	Image_reference(null),

	// item specs
	Absorbency(Group.ITEM_SPECS),
	Acid_free(Group.ITEM_SPECS),
	Adhesive(Group.ITEM_SPECS),
	Alternate_basis_weight(Group.ITEM_SPECS),
	Alternative_fiber_name(Group.ITEM_SPECS),
	Alternative_fiber_pct(Group.ITEM_SPECS),
	Application(Group.ITEM_SPECS),
	Author_Publisher(Group.ITEM_SPECS),
	Backing_material(Group.ITEM_SPECS),
	Basis_weight(Group.ITEM_SPECS),
	Box_type(Group.ITEM_SPECS),
	Brightness(Group.ITEM_SPECS),
	Burst_strength(Group.ITEM_SPECS),
	Caliper(Group.ITEM_SPECS),
	Capacity(Group.ITEM_SPECS),
	Carrier_material(Group.ITEM_SPECS),
	Chlorine_free(Group.ITEM_SPECS),
	Class1(Group.ITEM_SPECS),
	Closure_type(Group.ITEM_SPECS),
	Coating(Group.ITEM_SPECS),
	Collation(Group.ITEM_SPECS),
	Color(Group.ITEM_SPECS),
	Compatibility(Group.ITEM_SPECS),
	Corners(Group.ITEM_SPECS),
	Custom_print(Group.ITEM_SPECS),
	Custom_print_color(Group.ITEM_SPECS),
	Dilution_ratio(Group.ITEM_SPECS),
	Drilled_holes(Group.ITEM_SPECS),
	Drilled_holes_dimension(Group.ITEM_SPECS),
	Drilled_holes_layout(Group.ITEM_SPECS),
	Drilled_holes_location(Group.ITEM_SPECS),
	Drilled_holes_type(Group.ITEM_SPECS),
	DT_Duplex(Group.ITEM_SPECS),
	ECT(Group.ITEM_SPECS),
	Face_Material(Group.ITEM_SPECS),
	Facestock(Group.ITEM_SPECS),
	Features(Group.ITEM_SPECS),
	Finish(Group.ITEM_SPECS),
	Flap_type(Group.ITEM_SPECS),
	Flute(Group.ITEM_SPECS),
	Form(Group.ITEM_SPECS),
	Generic_color_1(Group.ITEM_SPECS),
	GSM(Group.ITEM_SPECS),
	Label_language(Group.ITEM_SPECS),
	Labels_across(Group.ITEM_SPECS),
	Labels_down(Group.ITEM_SPECS),
	Labels_per_box(Group.ITEM_SPECS),
	Labels_per_roll(Group.ITEM_SPECS),
	Labels_per_sheet(Group.ITEM_SPECS),
	Language(Group.ITEM_SPECS),
	Liner_material(Group.ITEM_SPECS),
	M_weight(Group.ITEM_SPECS),
	Material(Group.ITEM_SPECS),
	Message(Group.ITEM_SPECS),
	Model(Group.ITEM_SPECS),
	Number_of_parts(Group.ITEM_SPECS),
	Opacity(Group.ITEM_SPECS),
	Operating_rate(Group.ITEM_SPECS),
	Pack_method(Group.ITEM_SPECS),
	Panel_margin(Group.ITEM_SPECS),
	Part_number(Group.ITEM_SPECS),
	Perf_location(Group.ITEM_SPECS),
	Perf_type(Group.ITEM_SPECS),
	Perforation_description(Group.ITEM_SPECS),
	Perforations(Group.ITEM_SPECS),
	Ply(Group.ITEM_SPECS),
	Postal_requirements(Group.ITEM_SPECS),
	Power_requirements(Group.ITEM_SPECS),
	PPI(Group.ITEM_SPECS),
	Product(Group.ITEM_SPECS),
	Product_Description(Group.ITEM_SPECS),
	Scent(Group.ITEM_SPECS),
	Scoring_description(Group.ITEM_SPECS),
	Seam_type(Group.ITEM_SPECS),
	Special_effect(Group.ITEM_SPECS),
	Speed(Group.ITEM_SPECS),
	Split_pattern(Group.ITEM_SPECS),
	Style(Group.ITEM_SPECS),
	Supplier_name_display(Group.ITEM_SPECS),
	Thickness(Group.ITEM_SPECS),
	Thickness_type(Group.ITEM_SPECS),
	Tint_color(Group.ITEM_SPECS),
	Tint_pattern(Group.ITEM_SPECS),
	UNSPSC(Group.ITEM_SPECS),
	UNSPSC_description(Group.ITEM_SPECS),
	Wall_description(Group.ITEM_SPECS),
	Watermark(Group.ITEM_SPECS),
	Whiteness(Group.ITEM_SPECS),
	Window_1_bottom(Group.ITEM_SPECS),
	Window_1_left(Group.ITEM_SPECS),
	Window_1_material(Group.ITEM_SPECS),
	Window_1_position(Group.ITEM_SPECS),
	Window_1_size(Group.ITEM_SPECS),
	Window_2_bottom(Group.ITEM_SPECS),
	Window_2_left(Group.ITEM_SPECS),
	Window_2_material(Group.ITEM_SPECS),
	Window_2_size(Group.ITEM_SPECS),
	Window_qty(Group.ITEM_SPECS),

	// size
	Basic_size_description(Group.SIZE),
	Core_size(Group.SIZE),
	Envelope_length(Group.SIZE),
	Envelope_size_type(Group.SIZE),
	Envelope_width(Group.SIZE),
	Label_size(Group.SIZE),
	Roll_diameter(Group.SIZE),
	Roll_width(Group.SIZE),
	Siz_Name(Group.SIZE),
	Size(Group.SIZE),

	// packing specs
	PKLV_1_UM(Group.PACKING_SPECS),
	PKLV_1_UPC(Group.PACKING_SPECS),
	PKLV_1_Weight(Group.PACKING_SPECS),
	PKLV_2_UPC(Group.PACKING_SPECS),
	PKLV_3_UPC(Group.PACKING_SPECS),
	PKLV_4_Cube_extended(Group.PACKING_SPECS),
	PKLV_4_UM(Group.PACKING_SPECS),
	PKLV_4_UPC(Group.PACKING_SPECS),
	PKLV_4_Weight(Group.PACKING_SPECS),
	PKLV_5_UPC(Group.PACKING_SPECS),


	// enviro specs
	e_BPI(Group.ENVIRO_SPECS),
	e_CRI(Group.ENVIRO_SPECS),
	e_DfE(Group.ENVIRO_SPECS),
	e_EC(Group.ENVIRO_SPECS),
	e_EL(Group.ENVIRO_SPECS),
	e_EPA(Group.ENVIRO_SPECS),
	e_GEI(Group.ENVIRO_SPECS),
	e_GS(Group.ENVIRO_SPECS),
	e_WWF(Group.ENVIRO_SPECS),
	Environmental(Group.ENVIRO_SPECS),
	FSC_Cert(Group.ENVIRO_SPECS),
	FSC_Date(Group.ENVIRO_SPECS),
	FSC_Label(Group.ENVIRO_SPECS),
	LEED_Credit_v2(Group.ENVIRO_SPECS),
	LEED_Credit_v3(Group.ENVIRO_SPECS),
	LEED_Product_category(Group.ENVIRO_SPECS),
	LEED_Sustainability_criterion(Group.ENVIRO_SPECS),
	PEFC_Cert(Group.ENVIRO_SPECS),
	PEFC_Date(Group.ENVIRO_SPECS),
	PEFC_Label(Group.ENVIRO_SPECS),
	Recycled_PCRC(Group.ENVIRO_SPECS),
	Recycled_TRC(Group.ENVIRO_SPECS),
	SFI_Cert(Group.ENVIRO_SPECS),
	SFI_Date(Group.ENVIRO_SPECS),
	SFI_Label(Group.ENVIRO_SPECS),
	;

	private Group group;
	private ItemSpecificationAttribute(Group group) {
		this.group = group;
	}
	public Group getGroup() {
		return group;
	}

	/**
	 * All item specs are grouped into one of the 4 groups. The index value is tightly coupled with XPEDXItemDetailsAction.
	 * @see XPEDXItemDetailsAction#getSpecificationGroups()
	 */
	public static enum Group {
		ITEM_SPECS(0), SIZE(1), PACKING_SPECS(2), ENVIRO_SPECS(3);

		private int index;
		private Group(int index) {
			this.index = index;
		}
		public int getIndex() {
			return index;
		}
	}

}
