/*    */ package net.minecraft.world.item.trading;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class MerchantOffers
/*    */   extends ArrayList<MerchantOffer> {
/* 16 */   public static final Codec<MerchantOffers> CODEC = MerchantOffer.CODEC.listOf().optionalFieldOf("Recipes", List.of())
/* 17 */     .xmap(MerchantOffers::new, Function.identity())
/* 18 */     .codec();
/*    */   
/* 20 */   public static final StreamCodec<RegistryFriendlyByteBuf, MerchantOffers> STREAM_CODEC = MerchantOffer.STREAM_CODEC.apply(ByteBufCodecs.collection(MerchantOffers::new));
/*    */ 
/*    */   
/*    */   public MerchantOffers() {}
/*    */ 
/*    */   
/* 26 */   private MerchantOffers(int initialCapacity) { super(initialCapacity); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   private MerchantOffers(Collection<MerchantOffer> offers) { super(offers); }
/*    */ 
/*    */   
/*    */   public MerchantOffer getRecipeFor(ItemStack buyA, ItemStack buyB, int selectionHint) {
/* 34 */     if (selectionHint > 0 && selectionHint < size()) {
/*    */       
/* 36 */       MerchantOffer offer = (MerchantOffer)get(selectionHint);
/* 37 */       if (offer.satisfiedBy(buyA, buyB)) {
/* 38 */         return offer;
/*    */       }
/* 40 */       return null;
/*    */     } 
/*    */     
/* 43 */     for (int i = 0; i < size(); i++) {
/* 44 */       MerchantOffer offer = (MerchantOffer)get(i);
/* 45 */       if (offer.satisfiedBy(buyA, buyB)) {
/* 46 */         return offer;
/*    */       }
/*    */     } 
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public MerchantOffers copy() {
/* 53 */     MerchantOffers offers = new MerchantOffers(size());
/* 54 */     for (MerchantOffer offer : this) {
/* 55 */       offers.add(offer.copy());
/*    */     }
/* 57 */     return offers;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\item\trading\MerchantOffers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */