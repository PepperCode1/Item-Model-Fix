package me.pepperbell.itemmodelfix.util;

import java.util.Optional;

import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public enum ModelGenerationType {
	VANILLA(new LiteralText("Performance impact: \u00A7b\u00A7lnone"), new LiteralText("Uses Vanilla model generation.")),
	UNLERP(new LiteralText("Performance impact: \u00A7b\u00A7lnone"), new LiteralText("Uses Vanilla model generation,"), new LiteralText("but applies some math to make it look correct."), new LiteralText("Might cause flickering along model edges.")),
	PIXEL(new LiteralText("Performance impact: \u00A7a\u00A7llow\u00A7r-\u00A7e\u00A7lmedium\u00A7r"), new LiteralText("Generates all pixels individually."), new LiteralText("Provides best results but causes a larger impact"), new LiteralText("with higher resolution resource packs."));
	
	private Optional<Text[]> tooltip;
	
	private ModelGenerationType(Text... tooltip) {
		this.tooltip = tooltip.length != 0 && tooltip != null ? Optional.of(tooltip) : Optional.empty();
	}
	
	public Optional<Text[]> getTooltip() {
		return tooltip;
	}
}
