/*     */ package net.minecraft.world.item.trading;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class MerchantOffer {
/*  13 */   public static final Codec<MerchantOffer> CODEC = RecordCodecBuilder.create(i -> i.group(ItemCost.CODEC
/*  14 */         .fieldOf("buy").forGetter(()), ItemCost.CODEC
/*  15 */         .lenientOptionalFieldOf("buyB").forGetter(()), ItemStack.CODEC
/*  16 */         .fieldOf("sell").forGetter(()), Codec.INT
/*  17 */         .lenientOptionalFieldOf("uses", Integer.valueOf(0)).forGetter(()), Codec.INT
/*  18 */         .lenientOptionalFieldOf("maxUses", Integer.valueOf(4)).forGetter(()), Codec.BOOL
/*  19 */         .lenientOptionalFieldOf("rewardExp", Boolean.valueOf(true)).forGetter(()), Codec.INT
/*  20 */         .lenientOptionalFieldOf("specialPrice", Integer.valueOf(0)).forGetter(()), Codec.INT
/*  21 */         .lenientOptionalFieldOf("demand", Integer.valueOf(0)).forGetter(()), Codec.FLOAT
/*  22 */         .lenientOptionalFieldOf("priceMultiplier", Float.valueOf(0.0F)).forGetter(()), Codec.INT
/*  23 */         .lenientOptionalFieldOf("xp", Integer.valueOf(1)).forGetter(()))
/*  24 */       .apply(i, MerchantOffer::new));
/*     */   
/*  26 */   public static final StreamCodec<RegistryFriendlyByteBuf, MerchantOffer> STREAM_CODEC = StreamCodec.of(MerchantOffer::writeToStream, MerchantOffer::createFromStream);
/*     */   
/*     */   private final ItemCost baseCostA;
/*     */   
/*     */   private final Optional<ItemCost> costB;
/*     */   private final ItemStack result;
/*     */   private int uses;
/*     */   private final int maxUses;
/*     */   private final boolean rewardExp;
/*     */   private int specialPriceDiff;
/*     */   private int demand;
/*     */   private final float priceMultiplier;
/*     */   private final int xp;
/*     */   
/*     */   private MerchantOffer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int uses, int maxUses, boolean rewardExp, int specialPriceDiff, int demand, float priceMultiplier, int xp) {
/*  41 */     this.baseCostA = baseCostA;
/*  42 */     this.costB = costB;
/*  43 */     this.result = result;
/*  44 */     this.uses = uses;
/*  45 */     this.maxUses = maxUses;
/*  46 */     this.rewardExp = rewardExp;
/*  47 */     this.specialPriceDiff = specialPriceDiff;
/*  48 */     this.demand = demand;
/*  49 */     this.priceMultiplier = priceMultiplier;
/*  50 */     this.xp = xp;
/*     */   }
/*     */ 
/*     */   
/*  54 */   public MerchantOffer(ItemCost buy, ItemStack result, int maxUses, int xp, float priceMultiplier) { this(buy, Optional.empty(), result, maxUses, xp, priceMultiplier); }
/*     */ 
/*     */ 
/*     */   
/*  58 */   public MerchantOffer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int maxUses, int xp, float priceMultiplier) { this(baseCostA, costB, result, 0, maxUses, xp, priceMultiplier); }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public MerchantOffer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int uses, int maxUses, int xp, float priceMultiplier) { this(baseCostA, costB, result, uses, maxUses, xp, priceMultiplier, 0); }
/*     */ 
/*     */ 
/*     */   
/*  66 */   public MerchantOffer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int uses, int maxUses, int xp, float priceMultiplier, int demand) { this(baseCostA, costB, result, uses, maxUses, true, 0, demand, priceMultiplier, xp); }
/*     */ 
/*     */   
/*     */   private MerchantOffer(MerchantOffer offer) {
/*  70 */     this(offer.baseCostA, offer.costB, offer.result
/*     */ 
/*     */         
/*  73 */         .copy(), offer.uses, offer.maxUses, offer.rewardExp, offer.specialPriceDiff, offer.demand, offer.priceMultiplier, offer.xp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   public ItemStack getBaseCostA() { return this.baseCostA.itemStack(); }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   public ItemStack getCostA() { return this.baseCostA.itemStack().copyWithCount(getModifiedCostCount(this.baseCostA)); }
/*     */ 
/*     */   
/*     */   private int getModifiedCostCount(ItemCost cost) {
/*  94 */     int basePrice = cost.count();
/*     */ 
/*     */     
/*  97 */     int demandDiff = Math.max(0, Mth.floor((basePrice * this.demand) * this.priceMultiplier));
/*     */     
/*  99 */     return Mth.clamp(basePrice + demandDiff + this.specialPriceDiff, 1, cost.itemStack().getMaxStackSize());
/*     */   }
/*     */ 
/*     */   
/* 103 */   public ItemStack getCostB() { return (ItemStack)this.costB.map(ItemCost::itemStack).orElse(ItemStack.EMPTY); }
/*     */ 
/*     */ 
/*     */   
/* 107 */   public ItemCost getItemCostA() { return this.baseCostA; }
/*     */ 
/*     */ 
/*     */   
/* 111 */   public Optional<ItemCost> getItemCostB() { return this.costB; }
/*     */ 
/*     */ 
/*     */   
/* 115 */   public ItemStack getResult() { return this.result; }
/*     */ 
/*     */ 
/*     */   
/* 119 */   public void updateDemand() { this.demand = this.demand + this.uses - this.maxUses - this.uses; }
/*     */ 
/*     */ 
/*     */   
/* 123 */   public ItemStack assemble() { return this.result.copy(); }
/*     */ 
/*     */ 
/*     */   
/* 127 */   public int getUses() { return this.uses; }
/*     */ 
/*     */ 
/*     */   
/* 131 */   public void resetUses() { this.uses = 0; }
/*     */ 
/*     */ 
/*     */   
/* 135 */   public int getMaxUses() { return this.maxUses; }
/*     */ 
/*     */ 
/*     */   
/* 139 */   public void increaseUses() { this.uses++; }
/*     */ 
/*     */ 
/*     */   
/* 143 */   public int getDemand() { return this.demand; }
/*     */ 
/*     */ 
/*     */   
/* 147 */   public void addToSpecialPriceDiff(int add) { this.specialPriceDiff += add; }
/*     */ 
/*     */ 
/*     */   
/* 151 */   public void resetSpecialPriceDiff() { this.specialPriceDiff = 0; }
/*     */ 
/*     */ 
/*     */   
/* 155 */   public int getSpecialPriceDiff() { return this.specialPriceDiff; }
/*     */ 
/*     */ 
/*     */   
/* 159 */   public void setSpecialPriceDiff(int value) { this.specialPriceDiff = value; }
/*     */ 
/*     */ 
/*     */   
/* 163 */   public float getPriceMultiplier() { return this.priceMultiplier; }
/*     */ 
/*     */ 
/*     */   
/* 167 */   public int getXp() { return this.xp; }
/*     */ 
/*     */ 
/*     */   
/* 171 */   public boolean isOutOfStock() { return (this.uses >= this.maxUses); }
/*     */ 
/*     */ 
/*     */   
/* 175 */   public void setToOutOfStock() { this.uses = this.maxUses; }
/*     */ 
/*     */ 
/*     */   
/* 179 */   public boolean needsRestock() { return (this.uses > 0); }
/*     */ 
/*     */ 
/*     */   
/* 183 */   public boolean shouldRewardExp() { return this.rewardExp; }
/*     */ 
/*     */   
/*     */   public boolean satisfiedBy(ItemStack buyA, ItemStack buyB) {
/* 187 */     if (!this.baseCostA.test(buyA) || buyA.getCount() < getModifiedCostCount(this.baseCostA)) {
/* 188 */       return false;
/*     */     }
/* 190 */     if (this.costB.isPresent()) {
/* 191 */       return (((ItemCost)this.costB.get()).test(buyB) && buyB.getCount() >= ((ItemCost)this.costB.get()).count());
/*     */     }
/* 193 */     return buyB.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean take(ItemStack buyA, ItemStack buyB) {
/* 198 */     if (!satisfiedBy(buyA, buyB)) {
/* 199 */       return false;
/*     */     }
/*     */     
/* 202 */     buyA.shrink(getCostA().getCount());
/* 203 */     if (!getCostB().isEmpty()) {
/* 204 */       buyB.shrink(getCostB().getCount());
/*     */     }
/* 206 */     return true;
/*     */   }
/*     */ 
/*     */   
/* 210 */   public MerchantOffer copy() { return new MerchantOffer(this); }
/*     */ 
/*     */   
/*     */   private static void writeToStream(RegistryFriendlyByteBuf output, MerchantOffer offer) {
/* 214 */     ItemCost.STREAM_CODEC.encode(output, offer.getItemCostA());
/* 215 */     ItemStack.STREAM_CODEC.encode(output, offer.getResult());
/* 216 */     ItemCost.OPTIONAL_STREAM_CODEC.encode(output, offer.getItemCostB());
/* 217 */     output.writeBoolean(offer.isOutOfStock());
/* 218 */     output.writeInt(offer.getUses());
/* 219 */     output.writeInt(offer.getMaxUses());
/* 220 */     output.writeInt(offer.getXp());
/* 221 */     output.writeInt(offer.getSpecialPriceDiff());
/* 222 */     output.writeFloat(offer.getPriceMultiplier());
/* 223 */     output.writeInt(offer.getDemand());
/*     */   }
/*     */   
/*     */   public static MerchantOffer createFromStream(RegistryFriendlyByteBuf input) {
/* 227 */     ItemCost buy = (ItemCost)ItemCost.STREAM_CODEC.decode(input);
/* 228 */     ItemStack sell = (ItemStack)ItemStack.STREAM_CODEC.decode(input);
/* 229 */     Optional<ItemCost> buyB = (Optional)ItemCost.OPTIONAL_STREAM_CODEC.decode(input);
/* 230 */     boolean isExhausted = input.readBoolean();
/* 231 */     int uses = input.readInt();
/* 232 */     int maxUses = input.readInt();
/* 233 */     int xp = input.readInt();
/* 234 */     int specialPriceDiff = input.readInt();
/* 235 */     float priceMultiplier = input.readFloat();
/* 236 */     int demand = input.readInt();
/*     */     
/* 238 */     MerchantOffer offer = new MerchantOffer(buy, buyB, sell, uses, maxUses, xp, priceMultiplier, demand);
/* 239 */     if (isExhausted) {
/* 240 */       offer.setToOutOfStock();
/*     */     }
/* 242 */     offer.setSpecialPriceDiff(specialPriceDiff);
/* 243 */     return offer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\trading\MerchantOffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */