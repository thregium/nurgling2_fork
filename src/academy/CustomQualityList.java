package academy;

import haven.*;
import haven.Button;

import java.awt.*;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CustomQualityList extends WidgetList<CustomQualityList.Item> {

    public static ArrayList<ColorQuality> qualityList;
    public static Color NewColor = Color.WHITE;

    public static final Comparator<Item> ITEM_COMPARATOR = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return Double.compare(o1.staticNumber, o2.staticNumber);
        }
    };
    public static final Comparator<ColorQuality> QUALITY_COMPARATOR = new Comparator<ColorQuality>() {
        @Override
        public int compare(ColorQuality o1, ColorQuality o2) {
            return Double.compare(o1.number, o2.number);
        }
    };

    public CustomQualityList() {
        super(UI.scale(180, 25), 10);
        for (ColorQuality cq : qualityList) additem(new CustomQualityList.Item(cq.number, cq.color, cq.a));
        update();
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    @Override
    public void wdgmsg(Widget sender, String msg, Object... args) {
        switch (msg) {
            case "changed": {
                double oldNumber = (double) args[0];
                double number = (double) args[1];
                Color color = (Color) args[2];
                boolean a = (boolean) args[3];
                synchronized (qualityList) {
                    if (contains(oldNumber)) {
                        get(oldNumber).number = number;
                        get(number).color = color;
                        get(number).a = a;
                    } else
                        qualityList.add(new ColorQuality(number, color, a));
                }
                upsert(oldNumber, number, color, a);
                update();
                break;
            }
            case "delete": {
                double number = (double) args[0];
                synchronized (qualityList) {
                    qualityList.remove(get(number));
                }
                remove(number);
                removeitem((Item) sender, true);
                update();
                break;
            }
            default:
                super.wdgmsg(sender, msg, args);
                break;
        }
    }

    @SuppressWarnings("SynchronizeOnNonFinalField")
    public void add(double oldNumber, double number, Color color, boolean a) {
        if (number != 0 && !contains(number)) {
            synchronized (qualityList) {
                qualityList.add(new ColorQuality(number, color, a));
            }
            upsert(oldNumber, number, color, a);
            additem(new Item(number, color, a));
            update();
        }
    }

    public boolean contains(double number) {
        for (ColorQuality i : qualityList)
            if (i.number == number) return true;

        return false;
    }

    public ColorQuality get(double number) {
        for (ColorQuality i : qualityList)
            if (i.number == number) return i;

        return null;
    }

    private void update() {
        Collections.sort(list, ITEM_COMPARATOR);
        qualityList.sort(QUALITY_COMPARATOR);
        int n = listitems();
        for (int i = 0; i < n; i++) {
            listitem(i).c = itempos(i);
        }
    }

    public static void init() {


        if (qualityList == null || qualityList.isEmpty()) {
            qualityList = new ArrayList<ColorQuality>() {{
                add(new ColorQuality(9.999, new Color(127, 127, 127), true));
                add(new ColorQuality(19.999, new Color(127, 63, 0), true));
                add(new ColorQuality(29.999, new Color(0, 127, 0), true));
                add(new ColorQuality(39.999, new Color(0, 255, 0), true));
                add(new ColorQuality(49.999, new Color(0, 255, 127), true));
                add(new ColorQuality(64.999, new Color(0, 255, 255), true));
                add(new ColorQuality(79.999, new Color(0, 127, 255), true));
                add(new ColorQuality(99.999, new Color(0, 0, 255), true));
                add(new ColorQuality(124.999, new Color(127, 0, 255), true));
                add(new ColorQuality(149.999, new Color(191, 0, 255), true));
                add(new ColorQuality(174.999, new Color(255, 255, 0), true));
                add(new ColorQuality(199.999, new Color(255, 191, 0), true));
                add(new ColorQuality(249.999, new Color(255, 127, 0), true));
                add(new ColorQuality(299.999, new Color(255, 63, 0), true));
                add(new ColorQuality(349.999, new Color(255, 0, 0), true));
                add(new ColorQuality(399.999, new Color(255, 63, 63), true));
                add(new ColorQuality(449.999, new Color(255, 63, 127), true));
                add(new ColorQuality(499.999, new Color(255, 127, 191), true));
                add(new ColorQuality(599.999, new Color(255, 191, 255), true));
                add(new ColorQuality(699.999, new Color(255, 255, 255), true));
            }};
            upsert(9.999, 9.999, new Color(127, 127, 127), true);
            upsert(19.999, 19.999, new Color(127, 63, 0), true);
            upsert(29.999, 29.999, new Color(0, 127, 0), true);
            upsert(39.999, 39.999, new Color(0, 255, 0), true);
            upsert(49.999, 49.999, new Color(0, 255, 127), true);
            upsert(64.999, 64.999, new Color(0, 255, 255), true);
            upsert(79.999, 79.999, new Color(0, 127, 255), true);
            upsert(99.999, 99.999, new Color(0, 0, 255), true);
            upsert(124.999, 124.999, new Color(127, 0, 255), true);
            upsert(149.999, 149.999, new Color(191, 0, 255), true);
            upsert(174.999, 174.999, new Color(255, 255, 0), true);
            upsert(199.999, 199.999, new Color(255, 191, 0), true);
            upsert(249.999, 249.999, new Color(255, 127, 0), true);
            upsert(299.999, 299.999, new Color(255, 63, 0), true);
            upsert(349.999, 349.999, new Color(255, 0, 0), true);
            upsert(399.999, 399.999, new Color(255, 63, 63), true);
            upsert(449.999, 449.999, new Color(255, 63, 127), true);
            upsert(499.999, 499.999, new Color(255, 127, 191), true);
            upsert(599.999, 599.999, new Color(255, 191, 255), true);
            upsert(699.999, 699.999, new Color(255, 255, 255), true);
        }
    }
    public static void upsert(double oldNumber, double number, Color color, boolean a) {

    }
    public void remove(double number) {

    }

    public static class ColorQuality {
        public double number;
        public Color color;
        public boolean a;

        public ColorQuality(double number, Color color, boolean a) {
            this.number = number;
            this.color = color;
            this.a = a;
        }
    }
    protected static class Item extends Widget {

        public double oldNumber, staticNumber;
        public Color staticColor;
        public boolean staticA;
        private final CheckBox cb;
        private final TextEntry te;
        private final ColorPreview colorPreview;
        private boolean a = false;
        private UI.Grab grab;

        public Item(double number, Color color, boolean a) {
            super(UI.scale(180, 25));
            oldNumber = staticNumber = number;
            staticColor = color;
            staticA = a;

            cb = add(new CheckBox("") {
                {
                    a = staticA;
                    canactivate = true;
                }

                public void set(boolean val) {
                    staticA = val;
                    a = staticA;
                    oldNumber = staticNumber;
                    super.wdgmsg("ch");
                }
            }, UI.scale(3, 3));

            te = add(new TextEntry(UI.scale(100), staticNumber + "") {
                @Override
                public void activate(String text) {
                    try {
                        oldNumber = staticNumber;
                        staticNumber = Double.parseDouble(text);
                        super.wdgmsg("ch");
                    } catch (Exception e) {
                        e.printStackTrace();
                        settext(staticNumber + "");
                    }
                }
            }, UI.scale(25, 1));

            colorPreview = add(new ColorPreview(UI.scale(17, 17), color, val -> {
                oldNumber = staticNumber;
                staticColor = val;
                super.wdgmsg("ch");
            }), UI.scale(130, 4));

            add(new Button(UI.scale(24), "X") {
                @Override
                public void click() {
                    super.wdgmsg("activate", oldNumber);
                }

                @Override
                public boolean mouseup(Coord c, int button) {
                    //FIXME:a little hack, because WidgetList does not pass correct click coordinates if scrolled
                    return super.mouseup(Coord.z, button);
                }
            }, UI.scale(160, 0));
        }

        /**
         * @Override public boolean mousedown(Coord c, int button) {
         * if (super.mousedown(c, button)) {
         * return true;
         * }
         * if (button != 1)
         * return (false);
         * a = true;
         * grab = ui.grabmouse(this);
         * return (true);
         * }
         * @Override public boolean mouseup(Coord c, int button) {
         * if (a && button == 1) {
         * a = false;
         * if (grab != null) {
         * grab.remove();
         * grab = null;
         * }
         * if (c.isect(new Coord(0, 0), sz))
         * click();
         * return (true);
         * }
         * return (false);
         * }
         **/

        /*private void click() {
            cb.a = !cb.a;
            wdgmsg("changed", staticNumber, staticColor, staticA);
        }*/
        @Override
        public void wdgmsg(Widget sender, String msg, Object... args) {
            switch (msg) {
                case "ch":
                    wdgmsg("changed", oldNumber, staticNumber, staticColor, staticA);
                    break;
                case "activate":
                    wdgmsg("delete", oldNumber);
                    break;
                default:
                    super.wdgmsg(sender, msg, args);
                    break;
            }
        }
    }
}
