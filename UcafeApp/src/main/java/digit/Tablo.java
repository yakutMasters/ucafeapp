package digit;

import javafx.scene.Parent;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;


    /**
     * Digital tablo of 6 of the Digit classes. Made by yakutmasters
     */
    public class Tablo extends Parent {

        private Digit[] digits;
        private int bank;

        public int getBank() {
            return bank;
        }

        public void setBank(int bank) {
            this.bank = bank;
        }

        public Tablo() {
            // create effect for on LEDs
            Glow onEffect = new Glow(0);
//            onEffect.setInput(new InnerShadow(10, Color.BLACK));
            // create effect for on dot LEDs
//            Glow onDotEffect = new Glow(1.7f);
//            onDotEffect.setInput(new InnerShadow(5,Color.BLACK));
            // create effect for off LEDs
            InnerShadow offEffect = new InnerShadow();
            getTransforms().add(new Scale(0.83f, 0.83f, 0, 0));

            // create digits
            digits = new Digit[7];
            for (int i = 0; i < 6; i++) {
                Digit digit = new Digit(Color.GHOSTWHITE, Color.rgb(50,50,50), onEffect, offEffect);
                digit.setLayoutX(i * 80);
                digits[i] = digit;
                getChildren().add(digit);
            }
        }

        public void refreshClocks() {
            int oneHundredThousand = bank / 100000;
            int deciThousand = (bank % 100000) / 10000;
            int thousand = (bank % 10000) / 1000;
            int hundreds = (bank % 1000) / 100;
            int dozens = (bank % 100) / 10;
            int units = bank % 10;

            digits[0].showNumber(oneHundredThousand);
            digits[1].showNumber(deciThousand);
            digits[2].showNumber(thousand);
            digits[3].showNumber(hundreds);
            digits[4].showNumber(dozens);
            digits[5].showNumber(units);
        }

    }

    /**
     * Simple 7 segment LED style digit. It supports the numbers 0 through 9.
     */
    final class Digit extends Parent {
        private static final boolean[][] DIGIT_COMBINATIONS = new boolean[][]{
                new boolean[]{true, false, true, true, true, true, true},
                new boolean[]{false, false, false, false, true, false, true},
                new boolean[]{true, true, true, false, true, true, false},
                new boolean[]{true, true, true, false, true, false, true},
                new boolean[]{false, true, false, true, true, false, true},
                new boolean[]{true, true, true, true, false, false, true},
                new boolean[]{true, true, true, true, false, true, true},
                new boolean[]{true, false, false, false, true, false, true},
                new boolean[]{true, true, true, true, true, true, true},
                new boolean[]{true, true, true, true, true, false, true}};

        private final Polygon[] polygons = new Polygon[]{
                new Polygon(2, 0, 62, 0, 54, 12, 14, 12),
                new Polygon(14, 58, 54, 58, 66, 64, 54, 70, 14f, 70f, 2f, 64f),
                new Polygon(14, 116, 54, 116, 66, 128, 2, 128),
                new Polygon(0, 2, 12, 14, 12, 56, 0, 62),
                new Polygon(56, 14, 66, 2, 68, 62, 56, 56),
                new Polygon(0, 66, 12, 72, 12, 114, 0, 126),
                new Polygon(56, 72, 68, 66, 68, 126, 56, 114),

        };

        private final Color onColor;
        private final Color offColor;
        private final Effect onEffect;
        private final Effect offEffect;

        public Digit(Color onColor, Color offColor, Effect onEffect, Effect offEffect) {
            this.onColor = onColor;
            this.offColor = offColor;
            this.onEffect = onEffect;
            this.offEffect = offEffect;
            getChildren().addAll(polygons);
            getTransforms().add(new Shear(-0.1,0));
            showNumber(0);
        }

        public void showNumber(Integer num) {
            if (num < 0 || num > 9) num = 0; // default to 0 for non-valid numbers
            for (int i = 0; i < 7; i++) {
                polygons[i].setFill(DIGIT_COMBINATIONS[num][i] ? onColor : offColor);
                polygons[i].setEffect(DIGIT_COMBINATIONS[num][i] ? onEffect : offEffect);
            }
        }
    }


