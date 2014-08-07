package com.sterlingcommerce.xpedx.webchannel.catalog.itemdetail;

import com.sterlingcommerce.xpedx.webchannel.catalog.XPEDXItemDetailsAction;

/**
 * Each enum value corresponds to an item attribute long description.
 */
public enum ItemSpecificationAttribute {

	// don't show
	Brand_display(null, 1),
	SKU(null, 135),
	Ontology_display(null, 71),
	Sell_copy_item(null, Integer.MAX_VALUE),
	Image_reference(null, Integer.MAX_VALUE),

	// item specs
	Absorbency(Group.ITEM_SPECS, 3),
	Acid_free(Group.ITEM_SPECS, 106),
	Adhesive(Group.ITEM_SPECS, 4),
	Alternate_basis_weight(Group.ITEM_SPECS, 11),
	Alternative_fiber_name(Group.ITEM_SPECS, 124),
	Alternative_fiber_pct(Group.ITEM_SPECS, 123),
	Application(Group.ITEM_SPECS, 5),
	Author_Publisher(Group.ITEM_SPECS, 6),
	Backing_material(Group.ITEM_SPECS, 7),
	Basis_weight(Group.ITEM_SPECS, 10),
	Box_type(Group.ITEM_SPECS, 16),
	Brightness(Group.ITEM_SPECS, 14),
	Burst_strength(Group.ITEM_SPECS, 17),
	Caliper(Group.ITEM_SPECS, 9),
	Capacity(Group.ITEM_SPECS, 18),
	Carrier_material(Group.ITEM_SPECS, 19),
	Chlorine_free(Group.ITEM_SPECS, 107),
	Class1(Group.ITEM_SPECS, 82),
	Closure_type(Group.ITEM_SPECS, 20),
	Coating(Group.ITEM_SPECS, 21),
	Collation(Group.ITEM_SPECS, 57),
	Color(Group.ITEM_SPECS, 23),
	Compatibility(Group.ITEM_SPECS, 30),
	Corners(Group.ITEM_SPECS, 29),
	Custom_print(Group.ITEM_SPECS, 25),
	Custom_print_color(Group.ITEM_SPECS, 26),
	Dilution_ratio(Group.ITEM_SPECS, 31),
	Drilled_holes(Group.ITEM_SPECS, 32),
	Drilled_holes_dimension(Group.ITEM_SPECS, 33),
	Drilled_holes_layout(Group.ITEM_SPECS, 34),
	Drilled_holes_location(Group.ITEM_SPECS, 35),
	Drilled_holes_type(Group.ITEM_SPECS, 36),
	DT_Duplex(Group.ITEM_SPECS, 37),
	ECT(Group.ITEM_SPECS, 38),
	Face_Material(Group.ITEM_SPECS, 41),
	Facestock(Group.ITEM_SPECS, 42),
	Features(Group.ITEM_SPECS, 43),
	Finish(Group.ITEM_SPECS, 42),
	Flap_type(Group.ITEM_SPECS, 40),
	Flute(Group.ITEM_SPECS, 44),
	Form(Group.ITEM_SPECS, 45),
	Generic_color_1(Group.ITEM_SPECS, 24),
	GSM(Group.ITEM_SPECS, 12),
	Label_language(Group.ITEM_SPECS, 46),
	Labels_across(Group.ITEM_SPECS, 48),
	Labels_down(Group.ITEM_SPECS, 49),
	Labels_per_box(Group.ITEM_SPECS, 50),
	Labels_per_roll(Group.ITEM_SPECS, Integer.MAX_VALUE),
	Labels_per_sheet(Group.ITEM_SPECS, 51),
	Language(Group.ITEM_SPECS, 52),
	Liner_material(Group.ITEM_SPECS, 53),
	M_weight(Group.ITEM_SPECS, 13),
	Material(Group.ITEM_SPECS, 19),
	Message(Group.ITEM_SPECS, 55),
	Model(Group.ITEM_SPECS, 56),
	Number_of_parts(Group.ITEM_SPECS, 58),
	Opacity(Group.ITEM_SPECS, 15),
	Operating_rate(Group.ITEM_SPECS, 59),
	Pack_method(Group.ITEM_SPECS, 60),
	Panel_margin(Group.ITEM_SPECS, 62),
	Part_number(Group.ITEM_SPECS, Integer.MAX_VALUE),
	Perf_location(Group.ITEM_SPECS, 64),
	Perf_type(Group.ITEM_SPECS, 65),
	Perforation_description(Group.ITEM_SPECS, 63),
	Perforations(Group.ITEM_SPECS, 66),
	Ply(Group.ITEM_SPECS, 67),
	Postal_requirements(Group.ITEM_SPECS, 68),
	Power_requirements(Group.ITEM_SPECS, 69),
	PPI(Group.ITEM_SPECS, 61),
	Product(Group.ITEM_SPECS, 70),
	Product_Description(Group.ITEM_SPECS, 0),
	Scent(Group.ITEM_SPECS, 72),
	Scoring_description(Group.ITEM_SPECS, 73),
	Seam_type(Group.ITEM_SPECS, 74),
	Special_effect(Group.ITEM_SPECS, 83),
	Speed(Group.ITEM_SPECS, 84),
	Split_pattern(Group.ITEM_SPECS, 85),
	Style(Group.ITEM_SPECS, 86),
	Supplier_name_display(Group.ITEM_SPECS, 2),
	Thickness(Group.ITEM_SPECS, 87),
	Thickness_type(Group.ITEM_SPECS, 88),
	Tint_color(Group.ITEM_SPECS, 89),
	Tint_pattern(Group.ITEM_SPECS, 90),
	UNSPSC(Group.ITEM_SPECS, 137),
	UNSPSC_description(Group.ITEM_SPECS, 138),
	Wall_description(Group.ITEM_SPECS, 91),
	Watermark(Group.ITEM_SPECS, 92),
	Whiteness(Group.ITEM_SPECS, 95),
	Window_1_bottom(Group.ITEM_SPECS, 96),
	Window_1_left(Group.ITEM_SPECS, 97),
	Window_1_material(Group.ITEM_SPECS, 98),
	Window_1_position(Group.ITEM_SPECS, 99),
	Window_1_size(Group.ITEM_SPECS, 100),
	Window_2_bottom(Group.ITEM_SPECS, 101),
	Window_2_left(Group.ITEM_SPECS, 102),
	Window_2_material(Group.ITEM_SPECS, 103),
	Window_2_size(Group.ITEM_SPECS, 104),
	Window_qty(Group.ITEM_SPECS, 105),

	// size
	Basic_size_description(Group.SIZE, 8),
	Core_size(Group.SIZE, 27),
	Envelope_length(Group.SIZE, Integer.MAX_VALUE),
	Envelope_size_type(Group.SIZE, 39),
	Envelope_width(Group.SIZE, Integer.MAX_VALUE),
	Label_size(Group.SIZE, 47),
	Roll_diameter(Group.SIZE, 28),
	Roll_width(Group.SIZE, Integer.MAX_VALUE),
	Siz_Name(Group.SIZE, 76),
	Size(Group.SIZE, 75),

	// packing specs
	PKLV_1_UM(Group.PACKING_SPECS, 94),
	PKLV_1_UPC(Group.PACKING_SPECS, 77),
	PKLV_1_Weight(Group.PACKING_SPECS, 93),
	PKLV_2_UPC(Group.PACKING_SPECS, 78),
	PKLV_3_UPC(Group.PACKING_SPECS, 79),
	PKLV_4_Cube_extended(Group.PACKING_SPECS, Integer.MAX_VALUE),
	PKLV_4_UM(Group.PACKING_SPECS, Integer.MAX_VALUE),
	PKLV_4_UPC(Group.PACKING_SPECS, 80),
	PKLV_4_Weight(Group.PACKING_SPECS, Integer.MAX_VALUE),
	PKLV_5_UPC(Group.PACKING_SPECS, 81),

	// enviro specs
	e_BPI(Group.ENVIRO_SPECS, 125),
	e_CRI(Group.ENVIRO_SPECS, 126),
	e_DfE(Group.ENVIRO_SPECS, 127),
	e_EC(Group.ENVIRO_SPECS, 128),
	e_EL(Group.ENVIRO_SPECS, 129),
	e_EPA(Group.ENVIRO_SPECS, 130),
	e_GEI(Group.ENVIRO_SPECS, 131),
	e_GS(Group.ENVIRO_SPECS, 132),
	e_WWF(Group.ENVIRO_SPECS, 133),
	Environmental(Group.ENVIRO_SPECS, 134),
	FSC_Cert(Group.ENVIRO_SPECS, 108),
	FSC_Date(Group.ENVIRO_SPECS, 109),
	FSC_Label(Group.ENVIRO_SPECS, 110),
	LEED_Credit_v2(Group.ENVIRO_SPECS, 117),
	LEED_Credit_v3(Group.ENVIRO_SPECS, 118),
	LEED_Product_category(Group.ENVIRO_SPECS, 119),
	LEED_Sustainability_criterion(Group.ENVIRO_SPECS, 120),
	PEFC_Cert(Group.ENVIRO_SPECS, 114),
	PEFC_Date(Group.ENVIRO_SPECS, 115),
	PEFC_Label(Group.ENVIRO_SPECS, 116),
	Recycled_PCRC(Group.ENVIRO_SPECS, 121),
	Recycled_TRC(Group.ENVIRO_SPECS, 122),
	SFI_Cert(Group.ENVIRO_SPECS, 111),
	SFI_Date(Group.ENVIRO_SPECS, 112),
	SFI_Label(Group.ENVIRO_SPECS, 113),
	;

	private Group group;
	private int sequence;
	private ItemSpecificationAttribute(Group group, int sequence) {
		this.group = group;
		this.sequence = sequence;
	}
	public Group getGroup() {
		return group;
	}
	public int getSequence() {
		return sequence;
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
