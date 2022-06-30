package me.pepperbell.itemmodelfix.util;

import net.minecraft.text.Text;
import net.minecraft.util.Language;

public class ParsingUtil {
	public static Text[] parseNewlines(String translationKey) {
		if (!Language.getInstance().hasTranslation(translationKey)) {
			return null;
		}

		String[] strings = Language.getInstance().get(translationKey).split("\n|\\\\n");
		Text[] texts = new Text[strings.length];
		for (int i = 0; i < strings.length; i++) {
			texts[i] = Text.literal(strings[i]);
		}

		return texts;
	}
}
