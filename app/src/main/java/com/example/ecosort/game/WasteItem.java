package com.example.ecosort.game;

import java.io.Serializable;

/**
 * Represents one waste item that falls from the top of the screen.
 * category: 0 = Organic, 1 = Recyclable, 2 = Hazardous
 */
public class WasteItem implements Serializable {

    public static final int ORGANIC    = 0;
    public static final int RECYCLABLE = 1;
    public static final int HAZARDOUS  = 2;

    public final String emoji;
    public final String name;
    public final int    category;
    public final String fact; // Added for Encyclopedia
    public boolean      isCorrectlySorted = false; 

    public WasteItem(String emoji, String name, int category, String fact) {
        this.emoji    = emoji;
        this.name     = name;
        this.category = category;
        this.fact     = fact;
    }

    /** Full catalogue of items used in the game. */
    public static final WasteItem[] ALL = {
            // ── Organic ────────────────────────────────
            new WasteItem("🍌", "Banana peel",      ORGANIC,    "Banana peels decompose in ~2-4 weeks."),
            new WasteItem("🍎", "Apple core",       ORGANIC,    "Organic waste can be composted into fertilizer."),
            new WasteItem("🥦", "Vegetable waste",  ORGANIC,    "Kitchen scraps make up 40% of household waste."),
            new WasteItem("🍚", "Leftover rice",    ORGANIC,    "Food waste in landfills produces methane gas."),
            new WasteItem("🌿", "Grass clippings",  ORGANIC,    "Grass recycling returns nutrients to the soil."),
            new WasteItem("🍂", "Dried leaves",     ORGANIC,    "Brown leaves provide carbon for composting."),
            new WasteItem("☕", "Coffee grounds",   ORGANIC,    "Coffee grounds are acidic and great for roses."),
            new WasteItem("🥚", "Egg shell",        ORGANIC,    "Eggshells add calcium to the soil."),
            new WasteItem("🍞", "Stale bread",      ORGANIC,    "Dry bread can be composted easily."),
            new WasteItem("🐟", "Fish bones",       ORGANIC,    "Bones take longer to decompose than soft tissue."),
            new WasteItem("🍊", "Orange peel",      ORGANIC,    "Citrus peels take ~6 months to decompose."),
            new WasteItem("🥕", "Carrot tops",      ORGANIC,    "Carrot greens are 100% biodegradable."),
            new WasteItem("🌽", "Corn husk",        ORGANIC,    "Corn husks can be used for eco-friendly packaging."),
            new WasteItem("🍵", "Used tea bag",     ORGANIC,    "Most tea bags contain a small amount of plastic."),
            new WasteItem("🥜", "Nut shells",       ORGANIC,    "Nut shells decompose very slowly."),
            new WasteItem("🌾", "Straw / hay",      ORGANIC,    "Straw is an excellent mulch for gardens."),
            new WasteItem("🍇", "Grape stems",      ORGANIC,    "Composting reduces the need for chemicals."),
            new WasteItem("🪴", "Dead plant",       ORGANIC,    "Plants return to the earth to feed new life."),
            new WasteItem("🐾", "Pet fur",          ORGANIC,    "Animal hair is rich in nitrogen."),
            new WasteItem("🌰", "Chestnut shell",   ORGANIC,    "Organic waste belongs in the green/brown bin."),

            // ── Recyclable ─────────────────────────────
            new WasteItem("🥤", "Plastic bottle",   RECYCLABLE, "It takes 450 years for a plastic bottle to decompose."),
            new WasteItem("📰", "Newspaper",        RECYCLABLE, "Recycling paper saves 17 trees per ton."),
            new WasteItem("🥫", "Tin can",          RECYCLABLE, "Aluminium can be recycled indefinitely."),
            new WasteItem("📦", "Cardboard box",    RECYCLABLE, "Recycling 1 ton of cardboard saves 9 cubic yards of space."),
            new WasteItem("🍶", "Glass jar",        RECYCLABLE, "Glass never wears out and can be recycled forever."),
            new WasteItem("👜", "Plastic bag",      RECYCLABLE, "The average plastic bag is used for only 12 minutes."),
            new WasteItem("🖨️", "Old printer",     RECYCLABLE, "E-waste contains valuable metals like gold."),
            new WasteItem("📱", "Old phone",        RECYCLABLE, "Phones can be refurbished or parts recycled."),
            new WasteItem("📚", "Old books",        RECYCLABLE, "Hardcover books must have covers removed to recycle."),
            new WasteItem("✉️", "Envelope",         RECYCLABLE, "Envelopes with windows can still be recycled."),
            new WasteItem("🍷", "Glass bottle",     RECYCLABLE, "Recycling glass reduces energy use by 40%."),
            new WasteItem("🥡", "Takeaway box",     RECYCLABLE, "Only clean cardboard boxes should be recycled."),
            new WasteItem("🪣", "Plastic bucket",   RECYCLABLE, "Rigid plastics are highly recyclable."),
            new WasteItem("🗞️", "Magazine",         RECYCLABLE, "Glossy magazines are recyclable!"),
            new WasteItem("🎁", "Wrapping paper",   RECYCLABLE, "If it can't be scrunched, it can't be recycled."),
            new WasteItem("🪙", "Aluminium foil",   RECYCLABLE, "Foil must be clean of food to be recycled."),
            new WasteItem("🖥️", "Old monitor",     RECYCLABLE, "Modern LCD monitors are recyclable e-waste."),
            new WasteItem("⌨️", "Old keyboard",    RECYCLABLE, "Keyboards are made of various recyclable plastics."),
            new WasteItem("🪑", "Plastic chair",    RECYCLABLE, "Large plastic items should go to recycling centres."),
            new WasteItem("🧴", "Shampoo bottle",   RECYCLABLE, "Rinse your bottles before recycling them."),

            // ── Hazardous ──────────────────────────────
            new WasteItem("🔋", "Battery",          HAZARDOUS,  "Batteries leak toxic chemicals into the soil."),
            new WasteItem("💡", "Light bulb",       HAZARDOUS,  "Energy-saving bulbs contain small amounts of mercury."),
            new WasteItem("🖥️", "CRT monitor",     HAZARDOUS,  "Old CRT TVs contain several pounds of lead."),
            new WasteItem("🧴", "Chemical bottle",  HAZARDOUS,  "Never pour chemicals down the drain."),
            new WasteItem("🩺", "Syringe",          HAZARDOUS,  "Medical waste requires special incineration."),
            new WasteItem("🎨", "Paint can",        HAZARDOUS,  "Dried paint can sometimes go in trash; wet cannot."),
            new WasteItem("🧪", "Lab chemical",     HAZARDOUS,  "Lab waste can contaminate water supplies."),
            new WasteItem("🔌", "E-waste cable",    HAZARDOUS,  "Cables contain copper and plastic insulation."),
            new WasteItem("🌡️", "Thermometer",     HAZARDOUS,  "Silver thermometers contain dangerous mercury."),
            new WasteItem("🧯", "Fire extinguisher",HAZARDOUS,  "Extinguishers are under pressure and can explode."),
            new WasteItem("☢️", "Radioactive waste",HAZARDOUS,  "Hospitals produce minor radioactive waste."),
            new WasteItem("🪫", "Dead battery",     HAZARDOUS,  "Rechargeable batteries are even more toxic than alkaline."),
            new WasteItem("💻", "Old laptop",       HAZARDOUS,  "Laptops contain lithium-ion batteries — fire risk!"),
            new WasteItem("🖱️", "Old mouse",       HAZARDOUS,  "Small electronics should not go in general trash."),
            new WasteItem("📷", "Old camera",       HAZARDOUS,  "Camera sensors contain heavy metals."),
            new WasteItem("🔦", "Fluorescent tube", HAZARDOUS,  "Broken tubes release mercury vapour."),
            new WasteItem("🛢️", "Motor oil can",   HAZARDOUS,  "One quart of oil can contaminate 1 million gallons of water."),
            new WasteItem("💊", "Expired medicine", HAZARDOUS,  "Drugs in water harm fish and wildlife."),
            new WasteItem("🧲", "Old hard drive",   HAZARDOUS,  "Hard drives contain rare earth magnets."),
            new WasteItem("🔧", "Aerosol spray",    HAZARDOUS,  "Cans are pressurized and highly flammable."),
    };
}