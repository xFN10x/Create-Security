package dev.xplate.create_security.reg;

import dev.engine_room.flywheel.lib.model.baked.PartialModel;

import static dev.xplate.create_security.CSSecurity.res;

public class SecurityPartialModels {

    public static final PartialModel DIODE_CENTER = PartialModel.of(res("block/laser_diode_center"));
    public static final PartialModel FINI_GOGGLES_LIT = PartialModel.of(res("item/goggle_offsets/fini_goggles_lit_part"));

    public static final PartialModel FINI_GOGGLES = PartialModel.of(res("item/fini_goggles"));
    public static final PartialModel FINI_GOGGLES_PLUS1 = PartialModel.of(res("item/goggle_offsets/fini_goggles_plus1"));
    public static final PartialModel FINI_GOGGLES_PLUS2 = PartialModel.of(res("item/goggle_offsets/fini_goggles_plus2"));
    public static final PartialModel FINI_GOGGLES_PLUS3 = PartialModel.of(res("item/goggle_offsets/fini_goggles_plus3"));

    public static final PartialModel FINI_GOGGLES_MINUS1 = PartialModel.of(res("item/goggle_offsets/fini_goggles_minus1"));
    public static final PartialModel FINI_GOGGLES_MINUS2 = PartialModel.of(res("item/goggle_offsets/fini_goggles_minus2"));
    public static final PartialModel FINI_GOGGLES_MINUS3 = PartialModel.of(res("item/goggle_offsets/fini_goggles_minus3"));

    public static void reg() {

    }
}
