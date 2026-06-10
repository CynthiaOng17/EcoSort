package com.example.ecosort.game;

import java.io.Serializable;
import android.content.Context;

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
    public final String nameMs;
    public final int    category;
    public final String fact;
    public final String factMs;
    public boolean      isCorrectlySorted = false; 

    public WasteItem(String emoji, String name, String nameMs, int category, String fact, String factMs) {
        this.emoji    = emoji;
        this.name     = name;
        this.nameMs   = nameMs;
        this.category = category;
        this.fact     = fact;
        this.factMs   = factMs;
    }

    public String getLocalizedName(Context context) {
        String lang = context.getSharedPreferences("EcoSort", Context.MODE_PRIVATE).getString("lang", "en");
        return "ms".equals(lang) ? nameMs : name;
    }

    public String getLocalizedFact(Context context) {
        String lang = context.getSharedPreferences("EcoSort", Context.MODE_PRIVATE).getString("lang", "en");
        return "ms".equals(lang) ? factMs : fact;
    }

    /** Full catalogue of items used in the game. */
    public static final WasteItem[] ALL = {
            // ── Organic ────────────────────────────────
            new WasteItem("🍌", "Banana peel", "Kulit pisang", ORGANIC, "Banana peels decompose in ~2-4 weeks.", "Kulit pisang mereput dalam ~2-4 minggu."),
            new WasteItem("🍎", "Apple core", "Sisa epal", ORGANIC, "Organic waste can be composted into fertilizer.", "Sisa organik boleh dijadikan kompos sebagai baja."),
            new WasteItem("🥦", "Vegetable waste", "Sisa sayuran", ORGANIC, "Kitchen scraps make up 40% of household waste.", "Sisa dapur membentuk 40% daripada sisa isi rumah."),
            new WasteItem("🍚", "Leftover rice", "Sisa nasi", ORGANIC, "Food waste in landfills produces methane gas.", "Sisa makanan di tapak pelupusan menghasilkan gas metana."),
            new WasteItem("🌿", "Grass clippings", "Potongan rumput", ORGANIC, "Grass recycling returns nutrients to the soil.", "Kitar semula rumput mengembalikan nutrien ke tanah."),
            new WasteItem("🍂", "Dried leaves", "Daun kering", ORGANIC, "Brown leaves provide carbon for composting.", "Daun kering membekalkan karbon untuk pengkomposan."),
            new WasteItem("☕", "Coffee grounds", "Serbuk kopi", ORGANIC, "Coffee grounds are acidic and great for roses.", "Serbuk kopi berasid dan adalah baja yang sangat baik untuk bunga mawar."),
            new WasteItem("🥚", "Egg shell", "Kulit telur", ORGANIC, "Eggshells add calcium to the soil.", "Kulit telur menambah kalsium ke dalam tanah."),
            new WasteItem("🍞", "Stale bread", "Roti basi", ORGANIC, "Dry bread can be composted easily.", "Roti kering boleh dijadikan kompos dengan mudah."),
            new WasteItem("🐟", "Fish bones", "Tulang ikan", ORGANIC, "Bones take longer to decompose than soft tissue.", "Tulang mengambil masa lebih lama untuk mereput daripada tisu lembut."),
            new WasteItem("🍊", "Orange peel", "Kulit oren", ORGANIC, "Citrus peels take ~6 months to decompose.", "Kulit sitrus mengambil masa ~6 bulan untuk mereput."),
            new WasteItem("🥕", "Carrot tops", "Daun lobak merah", ORGANIC, "Carrot greens are 100% biodegradable.", "Daun lobak merah adalah 100% terbiodegradasi."),
            new WasteItem("🌽", "Corn husk", "Kulit jagung", ORGANIC, "Corn husks can be used for eco-friendly packaging.", "Kulit jagung boleh digunakan untuk pembungkusan mesra alam."),
            new WasteItem("🍵", "Used tea bag", "Uncang teh terpakai", ORGANIC, "Most tea bags contain a small amount of plastic.", "Kebanyakan uncang teh mengandungi sedikit plastik."),
            new WasteItem("🥜", "Nut shells", "Kulit kacang", ORGANIC, "Nut shells decompose very slowly.", "Kulit kacang mereput dengan sangat perlahan."),
            new WasteItem("🌾", "Straw / hay", "Jerami / rumput kering", ORGANIC, "Straw is an excellent mulch for gardens.", "Jerami adalah mulsa yang sangat baik untuk taman."),
            new WasteItem("🍇", "Grape stems", "Tangkai anggur", ORGANIC, "Composting reduces the need for chemicals.", "Pengkomposan mengurangkan keperluan untuk bahan kimia."),
            new WasteItem("🪴", "Dead plant", "Tumbuhan mati", ORGANIC, "Plants return to the earth to feed new life.", "Tumbuhan kembali ke bumi untuk memberi makan kehidupan baru."),
            new WasteItem("🐾", "Pet fur", "Bulu haiwan", ORGANIC, "Animal hair is rich in nitrogen.", "Bulu haiwan kaya dengan nitrogen."),
            new WasteItem("🌰", "Chestnut shell", "Kulit buah berangan", ORGANIC, "Organic waste belongs in the green/brown bin.", "Sisa organik sepatutnya dibuang ke dalam tong hijau/coklat."),

            // ── Recyclable ─────────────────────────────


            new WasteItem("🥤", "Plastic bottle", "Botol plastik", RECYCLABLE, "It takes 450 years for a plastic bottle to decompose.", "Ia mengambil masa 450 tahun untuk botol plastik mereput."),
            new WasteItem("📰", "Newspaper", "Surat khabar", RECYCLABLE, "Recycling paper saves 17 trees per ton.", "Kitar semula kertas menyelamatkan 17 pokok bagi setiap tan."),
            new WasteItem("🥫", "Tin can", "Tin aluminium", RECYCLABLE, "Aluminium can be recycled indefinitely.", "Aluminium boleh dikitar semula selama-lamanya."),
            new WasteItem("📦", "Cardboard box", "Kotak kadbod", RECYCLABLE, "Recycling 1 ton of cardboard saves 9 cubic yards of space.", "Kitar semula 1 tan kadbod menjimatkan 9 ela padu ruang."),
            new WasteItem("🍶", "Glass jar", "Balang kaca", RECYCLABLE, "Glass never wears out and can be recycled forever.", "Kaca tidak pernah haus dan boleh dikitar semula selama-lamanya."),
            new WasteItem("👜", "Plastic bag", "Beg plastik", RECYCLABLE, "The average plastic bag is used for only 12 minutes.", "Purata beg plastik digunakan selama 12 minit sahaja."),
            new WasteItem("🖨️", "Old printer", "Pencetak lama", RECYCLABLE, "E-waste contains valuable metals like gold.", "E-sisa mengandungi logam berharga seperti emas."),
            new WasteItem("📱", "Old phone", "Telefon lama", RECYCLABLE, "Phones can be refurbished or parts recycled.", "Telefon boleh dibaiki semula atau bahagiannya dikitar semula."),
            new WasteItem("📚", "Old books", "Buku lama", RECYCLABLE, "Hardcover books must have covers removed to recycle.", "Buku berkulit keras mesti ditanggalkan kulitnya untuk dikitar semula."),
            new WasteItem("✉️", "Envelope", "Sampul surat", RECYCLABLE, "Envelopes with windows can still be recycled.", "Sampul surat dengan tingkap plastik masih boleh dikitar semula."),
            new WasteItem("🍷", "Glass bottle", "Botol kaca", RECYCLABLE, "Recycling glass reduces energy use by 40%.", "Kitar semula kaca mengurangkan penggunaan tenaga sebanyak 40%."),
            new WasteItem("🥡", "Takeaway box", "Kotak makanan", RECYCLABLE, "Only clean cardboard boxes should be recycled.", "Hanya kotak kadbod yang bersih boleh dikitar semula."),
            new WasteItem("🪣", "Plastic bucket", "Baldi plastik", RECYCLABLE, "Rigid plastics are highly recyclable.", "Plastik keras adalah sangat mudah dikitar semula."),
            new WasteItem("🗞️", "Magazine", "Majalah", RECYCLABLE, "Glossy magazines are recyclable!", "Majalah berkilat boleh dikitar semula!"),
            new WasteItem("🎁", "Wrapping paper", "Kertas pembalut", RECYCLABLE, "If it can't be scrunched, it can't be recycled.", "Jika ia tidak boleh dironyok, ia tidak boleh dikitar semula."),
            new WasteItem("🪙", "Aluminium foil", "Kertas aluminium", RECYCLABLE, "Foil must be clean of food to be recycled.", "Kertas aluminium mesti bersih daripada makanan untuk dikitar semula."),
            new WasteItem("🖥️", "Old monitor", "Monitor lama", RECYCLABLE, "Modern LCD monitors are recyclable e-waste.", "Monitor LCD moden adalah e-sisa yang boleh dikitar semula."),
            new WasteItem("⌨️", "Old keyboard", "Papan kekunci lama", RECYCLABLE, "Keyboards are made of various recyclable plastics.", "Papan kekunci diperbuat daripada pelbagai plastik yang boleh dikitar semula."),
            new WasteItem("🪑", "Plastic chair", "Kerusi plastik", RECYCLABLE, "Large plastic items should go to recycling centres.", "Item plastik besar harus dihantar ke pusat kitar semula."),
            new WasteItem("🧴", "Shampoo bottle", "Botol syampu", RECYCLABLE, "Rinse your bottles before recycling them.", "Bilas botol anda sebelum mengitar semulanya."),

            // ── Hazardous ──────────────────────────────
            new WasteItem("🔋", "Battery", "Bateri", HAZARDOUS, "Batteries leak toxic chemicals into the soil.", "Bateri membocorkan bahan kimia toksik ke dalam tanah."),
            new WasteItem("💡", "Light bulb", "Mentol lampu", HAZARDOUS, "Energy-saving bulbs contain small amounts of mercury.", "Mentol jimat tenaga mengandungi sedikit merkuri."),
            new WasteItem("🧴", "Chemical bottle", "Botol bahan kimia", HAZARDOUS, "Never pour chemicals down the drain.", "Jangan sesekali menuang bahan kimia ke dalam longkang."),
            new WasteItem("🩺", "Syringe", "Picagari", HAZARDOUS, "Medical waste requires special incineration.", "Sisa perubatan memerlukan pembakaran khas."),
            new WasteItem("🎨", "Paint can", "Tin cat", HAZARDOUS, "Dried paint can sometimes go in trash; wet cannot.", "Cat kering kadangkala boleh dibuang ke sampah; cat basah tidak boleh."),
            new WasteItem("🧪", "Lab chemical", "Bahan kimia makmal", HAZARDOUS, "Lab waste can contaminate water supplies.", "Sisa makmal boleh mencemarkan bekalan air."),
            new WasteItem("🔌", "E-waste cable", "Kabel e-sisa", HAZARDOUS, "Cables contain copper and plastic insulation.", "Kabel mengandungi kuprum dan penebat plastik."),
            new WasteItem("🌡️", "Thermometer", "Termometer", HAZARDOUS, "Silver thermometers contain dangerous mercury.", "Termometer perak mengandungi merkuri yang berbahaya."),
            new WasteItem("🧯", "Fire extinguisher", "Alat pemadam api", HAZARDOUS, "Extinguishers are under pressure and can explode.", "Alat pemadam api berada di bawah tekanan dan boleh meletup."),
            new WasteItem("☢️", "Radioactive waste", "Sisa radioaktif", HAZARDOUS, "Hospitals produce minor radioactive waste.", "Hospital menghasilkan sisa radioaktif tahap rendah."),
            new WasteItem("🪫", "Dead battery", "Bateri mati", HAZARDOUS, "Rechargeable batteries are even more toxic than alkaline.", "Bateri boleh dicas semula lebih toksik daripada bateri alkali."),
            new WasteItem("💻", "Old laptop", "Komputer riba lama", HAZARDOUS, "Laptops contain lithium-ion batteries — fire risk!", "Komputer riba mengandungi bateri litium-ion — risiko kebakaran!"),
            new WasteItem("🖱️", "Old mouse", "Tetikus lama", HAZARDOUS, "Small electronics should not go in general trash.", "Elektronik kecil tidak sepatutnya dibuang ke dalam sampah am."),
            new WasteItem("📷", "Old camera", "Kamera lama", HAZARDOUS, "Camera sensors contain heavy metals.", "Sensor kamera mengandungi logam berat."),
            new WasteItem("🔦", "Fluorescent tube", "Tiub pendarfluor", HAZARDOUS, "Broken tubes release mercury vapour.", "Tiub yang pecah membebaskan wap merkuri."),
            new WasteItem("🛢️", "Motor oil can", "Tin minyak motor", HAZARDOUS, "One quart of oil can contaminate 1 million gallons of water.", "Satu kuart minyak boleh mencemarkan 1 juta gelen air."),
            new WasteItem("💊", "Expired medicine", "Ubat tamat tempoh", HAZARDOUS, "Drugs in water harm fish and wildlife.", "Ubat dalam air memudaratkan ikan dan hidupan liar."),
            new WasteItem("🧲", "Old hard drive", "Cakera keras lama", HAZARDOUS, "Hard drives contain rare earth magnets.", "Cakera keras mengandungi magnet nadir bumi."),
    };
}